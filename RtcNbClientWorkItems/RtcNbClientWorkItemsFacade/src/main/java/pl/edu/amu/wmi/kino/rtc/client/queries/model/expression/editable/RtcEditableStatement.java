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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;

/**
 *
 * @author Patryk Żywica
 */
public abstract class RtcEditableStatement {

    private final Set<RtcStatementListener> listeners = Collections.synchronizedSet(new HashSet<RtcStatementListener>());

    /**
     * Use this method to regtister new listener to this object.
     *
     * If given listener is actualy registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(RtcStatementListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(RtcStatementListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(RtcStatementEvent event) {
        synchronized (listeners) {
            for (RtcStatementListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(l, event));
            }
        }
    }

    public abstract RtcQueryAttributeManager getQueryAttributeManager();

    /**
     * This method returns root term for expression holded by this statement.
     *
     * The root term always exists, even in empty or just created query, so this
     * method never return null.
     * @return root term for expression holded by this statement. Never null.
     */
    public abstract RtcEditableTermExpression getRootTerm();

    /**
     * Listener interface for <code>RtcStatement</code>. If you want to listen for
     * changes in this <code>RtcStatement</code> you have to implement this interface
     * and pass this implementation to <code>RtcStatement.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface RtcStatementListener {

        public void statementChanged(RtcStatementEvent event);
    }

    /**
     * This enumeration defines all possible events for <code>RtcStatement</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcStatementEvent {

        /**
         * This event should be called every time when contained root term was changed.
         */
        ROOT_TERM_CHANGED,
    }

    private static class EventRunner implements Runnable {

        private RtcStatementEvent event;
        private RtcStatementListener listener;

        public EventRunner(RtcStatementListener listener, RtcStatementEvent event) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.statementChanged(event);
        }
    }
}
