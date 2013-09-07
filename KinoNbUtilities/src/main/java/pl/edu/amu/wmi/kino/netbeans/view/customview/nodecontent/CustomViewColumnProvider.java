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
package pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Patryk Żywica
 */
public abstract class CustomViewColumnProvider {

    private final Set<CustomViewColumnProviderListener> listeners = Collections.synchronizedSet(new HashSet<CustomViewColumnProviderListener>());

    public abstract String[] getColumns();

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(CustomViewColumnProviderListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(CustomViewColumnProviderListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(CustomViewColumnProviderEvent event) {
        synchronized (listeners) {
            for (CustomViewColumnProviderListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(event, l));
            }
        }
    }

    public interface CustomViewColumnProviderListener {

        void eventFired(CustomViewColumnProviderEvent event);
    }

    public enum CustomViewColumnProviderEvent {

        COLUMN_ADDED,
        COLUMN_REMOVED,
        COLUMNS_CHANGED;
    }

    private static class EventRunner implements Runnable {

        private CustomViewColumnProviderEvent event;
        private CustomViewColumnProviderListener listener;

        public EventRunner(CustomViewColumnProviderEvent event, CustomViewColumnProviderListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.eventFired(event);
        }
    }
}
