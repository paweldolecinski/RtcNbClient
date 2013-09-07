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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public final class EventSourceSupport<T> {

    private final Set<EventListener<T>> listeners = Collections.synchronizedSet(new HashSet<EventListener<T>>(4));
    private static final RequestProcessor requestProcessor = new RequestProcessor(EventSourceSupport.class);

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(EventListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    public final void fireEvent(T event) {
        synchronized (listeners) {
            for (EventListener<T> l : listeners) {
                requestProcessor.post(new EventRunner<T>(event, l));
            }
        }
    }

    private static class EventRunner<T> implements Runnable {

        private T event;
        private EventListener<T> listener;

        public EventRunner(T event, EventListener<T> listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.eventFired(event);
        }
    }
}
