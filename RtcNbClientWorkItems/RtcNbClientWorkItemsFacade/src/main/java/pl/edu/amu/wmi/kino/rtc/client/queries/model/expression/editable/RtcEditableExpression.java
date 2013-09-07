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

/**
 * Model interface that represents expression that can be used in query.
 *
 * Actual implementation uses Rtc API to communicate with Jazz server.
 * @author Patryk Żywica
 */
public abstract class RtcEditableExpression {

    private final Set<RtcExpressionListener> listeners = Collections.synchronizedSet(new HashSet<RtcExpressionListener>());

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public void addListener(RtcExpressionListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public void removeListener(RtcExpressionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected void fireEvent(final RtcExpressionEvent event) {
        synchronized (listeners) {
            for (RtcExpressionListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(l, event));
            }
        }

    }

    /**
     * This method should return parent <code>RtcTerm</code> of this <code>RtcExpression</code>.
     * If this expression is root <code>RtcTermExpression</code> then this method must return null.
     *
     * @return parent term of this <code>RtcExpression</code>. May be null.
     */
    public abstract RtcEditableTermExpression getParent();

    /**
     * Listener interface for <code>RtcEditableExpression</code>. If you want to listen for
     * changes in this <code>RtcExpression</code> you have to implement this interface
     * and pass this implementation to <code>RtcEditableExpression.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface RtcExpressionListener {

        /**
         * This method is invoked when any change in <code>RtcExpression</code> occurs.
         *
         * @param event type that caused this change
         */
        void expressionChanged(RtcExpressionEvent e);
    }

    /**
     * This enumeration defines all possible events for <code>RtcExpression</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcExpressionEvent {

        /**
         * This event should be called when selected term operator was changed.
         */
        TERM_OPERATOR_CHANGED,
        /**
         * This event should be called when selected attribute operation was changed.
         */
        ATTRIBUTE_OPERATION_CHANGED,
        /**
         * This event should be called when one of subexpressions was changed.
         */
        SUBEXPRESSION_CHANGED,
        /**
         * This event should be called when new subexpression was added to this expression.
         */
        SUBEXPRESSION_ADDED,
        /**
         * This event should be called when subexpression was removed from this expression.
         */
        SUBEXPRESSION_REMOVED,
        /**
         * This event should be called when new selected value was added to this expression.
         */
        SELECTED_VALUE_ADDED,
        /**
         * This event should be called when selected value was removed from this expression.
         */
        SELECTED_VALUE_REMOVED,
        /**
         * This event should be called when new selected variable was added to this expression.
         */
        SELECTED_VARIABLE_ADDED,
        /**
         * This event should be called when selected variable was removed from this expression.
         */
        SELECTED_VARIABLE_REMOVED,
        /**
         * This event should be called when any of selected values was changed.
         */
        SELECTED_VALUE_CHANGED,
    }

    /**
     * Private class for running events.
     */
    private static class EventRunner implements Runnable {

        private RtcExpressionEvent event;
        private RtcExpressionListener listener;

        public EventRunner(RtcExpressionListener listener, RtcExpressionEvent event) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.expressionChanged(event);
        }
    }
}
