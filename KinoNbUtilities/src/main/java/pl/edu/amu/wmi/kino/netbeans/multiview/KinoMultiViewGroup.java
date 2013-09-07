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
package pl.edu.amu.wmi.kino.netbeans.multiview;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;
//TODO : bikol : change javadoc

/**
 * Interface for multi view descriptions.
 * 
 * Each description will be shown in multiview topcomponent.
 *
 *
 * @author Patryk Żywica
 */
public abstract class KinoMultiViewGroup {

    private final Set<KinoMultiViewGroupListener> listeners = Collections.synchronizedSet(new HashSet<KinoMultiViewGroupListener>());
    private static final RequestProcessor requestProcessor = new RequestProcessor("KinoMultiViewGroup");

    public abstract KinoMultiViewDescription[] getDescriptions();

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(KinoMultiViewGroupListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(KinoMultiViewGroupListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(KinoMultiViewGroupEvent event) {
        synchronized (listeners) {
            for (KinoMultiViewGroupListener l : listeners) {
                requestProcessor.post(new EventRunner(event, l));
            }
        }
    }

    /**
     * Listener interface for <code>KinoMultiViewGroup</code>.
     * If you want to listen for changes in this <code>KinoMultiViewGroup</code>
     * you have to implement this interface and pass this implementation to
     * <code>KinoMultiViewGroup.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface KinoMultiViewGroupListener {

        public void eventFired(KinoMultiViewGroupEvent event);
    }

    /**
     * This enumeration defines all possible events for <code>KinoMultiViewGroup</code>.
     *
     * @author Patryk Żywica
     */
    public enum KinoMultiViewGroupEvent {

        DESCRIPTION_ADDED,
        DESCRIPTION_REMOVED,
        DESCRIPTION_ORDER_CHANGED;
    }

    private static class EventRunner implements Runnable {

        private KinoMultiViewGroupEvent event;
        private KinoMultiViewGroupListener listener;

        public EventRunner(KinoMultiViewGroupEvent event, KinoMultiViewGroupListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.eventFired(event);
        }
    }
}
