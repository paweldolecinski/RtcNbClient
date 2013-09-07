/*
 * Copyright (C) 2009-2011 RtcNbClient Team (http://rtcnbclient.wmi.amu.edu.pl/)
 *
 * This file is part of RtcNbClient.
 *
 * RtcNbClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RtcNbClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RtcNbClient. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.amu.wmi.kino.netbeans.mvp.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.openide.util.lookup.ServiceProvider;

/**
 * Basic implementation of {@link EventBus}.
 */
@ServiceProvider(service=EventBus.class)
public class SimpleEventBus implements EventBus {

    private int firingDepth = 0;
    /**
     * Add and remove operations received during dispatch.
     */
    private List<Runnable> deferredDeltas;
    /**
     * Map of event type to map of event source to list of their handlers.
     */
    private final Map<MvpEvent.Type<?>, Map<Object, List<?>>> map = new HashMap<MvpEvent.Type<?>, Map<Object, List<?>>>();

    @Override
    public <H extends EventHandler> HandlerRegistration addHandler(MvpEvent.Type<H> type, H handler) {
        if (type == null) {
            throw new NullPointerException("Cannot add a handler with a null type");
        }
        if (handler == null) {
            throw new NullPointerException("Cannot add a null handler");
        }

        return doAdd(type, null, handler);
    }

    @Override
    public <H extends EventHandler> HandlerRegistration addHandlerToSource(final MvpEvent.Type<H> type, final Object source, final H handler) {
        if (type == null) {
            throw new NullPointerException("Cannot add a handler with a null type");
        }
        if (source == null) {
            throw new NullPointerException("Cannot add a handler with a null source");
        }
        if (handler == null) {
            throw new NullPointerException("Cannot add a null handler");
        }

        return doAdd(type, source, handler);
    }

    @Override
    public void fireEvent(MvpEvent<?> event) {
        if (event == null) {
            throw new NullPointerException("Cannot fire null event");
        }
        doFire(event, null);
    }

    @Override
    public void fireEventFromSource(MvpEvent<?> event, Object source) {
        if (event == null) {
            throw new NullPointerException("Cannot fire null event");
        }
        if (source == null) {
            throw new NullPointerException("Cannot fire from a null source");
        }
        doFire(event, source);
    }

    /**
     * Package protected to support legacy features in HandlerManager.
     */
     private <H extends EventHandler> void doRemove(MvpEvent.Type<H> type, Object source, H handler) {
        if (firingDepth > 0) {
            enqueueRemove(type, source, handler);
        } else {
            doRemoveNow(type, source, handler);
        }
    }

    private void defer(Runnable command) {
        if (deferredDeltas == null) {
            deferredDeltas = new ArrayList<Runnable>();
        }
        deferredDeltas.add(command);
    }

    private <H extends EventHandler> HandlerRegistration doAdd(final MvpEvent.Type<H> type, final Object source, final H handler) {
        if (firingDepth > 0) {
            enqueueAdd(type, source, handler);
        } else {
            doAddNow(type, source, handler);
        }

        return new HandlerRegistration(new Runnable() {

            public void run() {
                doRemove(type, source, handler);
            }
        });
    }

    private <H extends EventHandler> void doAddNow(MvpEvent.Type<H> type, Object source, H handler) {
        List<H> l = ensureHandlerList(type, source);
        l.add(handler);
    }

    private <H extends EventHandler> void doFire(MvpEvent<H> event, Object source) {
        try {
            firingDepth++;

            if (source != null) {
                event.setSource(source);
            }

            List<H> handlers = getDispatchList(event.getAssociatedType(), source);
            Set<Throwable> causes = null;
            ListIterator<H> it =  handlers.listIterator();
            while (it.hasNext()) {
                H handler = it.next();

                try {
                    event.dispatch(handler);
                } catch (Throwable e) {
                    if (causes == null) {
                        causes = new HashSet<Throwable>();
                    }
                    causes.add(e);
                }
            }

            if (causes != null) {
                throw new UmbrellaException(causes);
            }
        } finally {
            firingDepth--;
            if (firingDepth == 0) {
                handleQueuedAddsAndRemoves();
            }
        }
    }

    private <H> void doRemoveNow(MvpEvent.Type<H> type, Object source, H handler) {
        List<H> l = getHandlerList(type, source);

        boolean removed = l.remove(handler);
        assert removed : "redundant remove call";
        if (removed && l.isEmpty()) {
            prune(type, source);
        }
    }

    private <H extends EventHandler> void enqueueAdd(final MvpEvent.Type<H> type, final Object source, final H handler) {
        defer(new Runnable() {

            public void run() {
                doAddNow(type, source, handler);
            }
        });
    }

    private <H extends EventHandler> void enqueueRemove(final MvpEvent.Type<H> type, final Object source, final H handler) {
        defer(new Runnable() {

            public void run() {
                doRemoveNow(type, source, handler);
            }
        });
    }

    private <H> List<H> ensureHandlerList(MvpEvent.Type<H> type, Object source) {
        Map<Object, List<?>> sourceMap = map.get(type);
        if (sourceMap == null) {
            sourceMap = new HashMap<Object, List<?>>();
            map.put(type, sourceMap);
        }

        // safe, we control the puts.
        @SuppressWarnings("unchecked")
        List<H> handlers = (List<H>) sourceMap.get(source);
        if (handlers == null) {
            handlers = new ArrayList<H>();
            sourceMap.put(source, handlers);
        }

        return handlers;
    }

    private <H> List<H> getDispatchList(MvpEvent.Type<H> type, Object source) {
        List<H> directHandlers = getHandlerList(type, source);
        if (source == null) {
            return directHandlers;
        }

        List<H> globalHandlers = getHandlerList(type, null);

        List<H> rtn = new ArrayList<H>(directHandlers);
        rtn.addAll(globalHandlers);
        return rtn;
    }

    private <H> List<H> getHandlerList(MvpEvent.Type<H> type, Object source) {
        Map<Object, List<?>> sourceMap = map.get(type);
        if (sourceMap == null) {
            return Collections.emptyList();
        }

        // safe, we control the puts.
        @SuppressWarnings("unchecked")
        List<H> handlers = (List<H>) sourceMap.get(source);
        if (handlers == null) {
            return Collections.emptyList();
        }

        return handlers;
    }

    private void handleQueuedAddsAndRemoves() {
        if (deferredDeltas != null) {
            try {
                for (Runnable c : deferredDeltas) {
                    c.run();
                }
            } finally {
                deferredDeltas = null;
            }
        }
    }

    private void prune(MvpEvent.Type<?> type, Object source) {
        Map<Object, List<?>> sourceMap = map.get(type);

        List<?> pruned = sourceMap.remove(source);

        assert pruned != null : "Can't prune what wasn't there";
        assert pruned.isEmpty() : "Pruned unempty list!";

        if (sourceMap.isEmpty()) {
            map.remove(type);
        }
    }
}