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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;

/**
 * This class is used to manage add query attributes for one project area. It supports
 * listening on changes in attributes list.
 * @author Patryk Żywica
 */
public abstract class RtcQueryAttributeManager {

    private final Set<RtcQueryAttributeManagerListener> listeners = Collections.synchronizedSet(new HashSet<RtcQueryAttributeManagerListener>());

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(RtcQueryAttributeManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(RtcQueryAttributeManagerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(RtcQueryAttributeManagerEvent event) {
        synchronized (listeners) {
            for (RtcQueryAttributeManagerListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(l, event));
            }
        }

    }

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return array of all query attributes registered in this manager.
     */
    public abstract RtcQueryAttribute[] getQueryAttributes();

    /**
     * Listener interface for <code>RtcQueryAttributeManager</code>. If you want to listen for
     * changes in this <code>RtcQueryAttributeManager</code> you have to implement this interface
     * and pass this implementation to <code>RtcStatement.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface RtcQueryAttributeManagerListener {

        public void attributesChanged(RtcQueryAttributeManagerEvent event);
    }

    /**
     * This enumeration defines all possible events for <code>RtcStatement</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcQueryAttributeManagerEvent {

        /**
         * This event should be called when statement was modified in any way.
         * Also when e.g. rootTerm was modified.
         */
        QUERY_ATTRIBUTE_ADDED,
        QUERY_ATTRIBUTE_REMOVED;
    }

    private static class EventRunner implements Runnable {

        private RtcQueryAttributeManagerEvent event;
        private RtcQueryAttributeManagerListener listener;

        public EventRunner(RtcQueryAttributeManagerListener listener, RtcQueryAttributeManagerEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            listener.attributesChanged(event);
        }
    }
}
