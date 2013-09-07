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

import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcAttributeOperation;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;

/**
 * This class is one of expression types. It represents query attribute based expressions.
 * @author Patryk Żywica
 */
public abstract class RtcEditableAttributeExpression extends RtcEditableExpression {

    private final Set<RtcAttributeExpressionSelectedValueChangeListener> listeners =
            Collections.synchronizedSet(new HashSet<RtcAttributeExpressionSelectedValueChangeListener>());

    /**
     *
     * @return query attribute against which this expression is set.
     */
    public abstract RtcQueryAttribute getQueryAttribute();

    /**
     * To get list of all possible for this expression attribute operations call
     * <code>getQueryAttribute().getAttributeOperations()</code>.
     *
     * @return currently selected attribute operation, never null.
     */
    public abstract RtcAttributeOperation getSelectedAttributeOperation();

    /**
     * To get list of all possible for this expression attribute operations call
     * <code>getQueryAttribute().getAttributeOperations()</code>.
     *
     * This can be long running operation. Do not call on AWT event dispatch thread.
     *
     * @param operation that should be set for this expression. Not null.
     * @throws IllegalArgumentException when given operation is not proper for this expression.
     */
    public abstract void setSelectedAttributeOperation(RtcAttributeOperation operation) throws IllegalArgumentException;

    /**
     *
     * @return array of <code>RtcQueryAttributeValue</code> selected in this expression. Never null. May be empty.
     */
    public abstract RtcQueryAttributeValue[] getSelectedValues();

    /**
     * Use this method to register new listener to this object's selected values.
     *
     * If given listener is actually registered in this object does nothing.
     * @see RtcEditableAttributeExpression#changeSelectedValue(pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue, pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue)
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public void addSelectedValuesChangeListener(RtcAttributeExpressionSelectedValueChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object's selected values.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public void removeSelectedValuesChangeListener(RtcAttributeExpressionSelectedValueChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected void fireSelectedValueChangeEvent(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) {
        synchronized (listeners) {
            for (RtcAttributeExpressionSelectedValueChangeListener l : listeners) {
                RequestProcessor.getDefault().post(new SelectedValueEventRunner(l, oldValue, newValue));
            }
        }
    }

    /**
     * Call this method to add new selected value to this expression.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param val
     * @throws IllegalArgumentException
     */
    public abstract void addSelectedValue(RtcQueryAttributeValue value) throws IllegalArgumentException;

    /**
     * Call this method to remove selected value from this expression.
     * This can be long running operation. Do not call on event dispatch thread.
     * @param val
     */
    public abstract void removeSelectedValue(RtcQueryAttributeValue value);

    /**
     * Call this method to change currently selected value. Calling this method will
     * replace <code>odlValue</code> with <code>newValue</code>.
     *
     * This method is necessary because <code>RtcQueryAttributeValue</code> is constant
     * object. It means that it cannot change.
     *
     * Example: If you want to change selected value e.g. <code>value</code> that represents <code>String</code>
     * you should not internally modify <code>RtcQueryAttribiteValue</code>. You have
     * two options. You can call <code>removeSelectedValue(value)</code> and then call
     * <code>addSelectedValue(newValue)</code>. But in some cases it is not good idea.
     * The second option it to call <code>changeSelectedValue(value, newValue)</code>.
     * Difference between this two options is very significant. Calling
     * <code>removeSelectedValue</code> and <code>addSelectedValue</code> will cause
     * firing <code>SELECTED_VALUE_REMOVED</code> and <code>SELECTED_VALUE_ADDED</code>
     * events. Calling <code>changeSelectedValue</code> fires only one event
     * <code>SELECTED_VALUE_CHANGED</code>.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @see RtcQueryAttributeValue
     *
     * @param oldValue that have to be present in <code>getSelectedValues()</code>. Cannot be null.
     * @param newValue that will replace <code>oldValue</code> in expression. Cannot be null.
     * @throws IllegalArgumentException
     */
    public abstract void changeSelectedValue(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) throws IllegalArgumentException;

    /**
     *
     * @return array of <code>RtcQueryAttributeVariable</code> selected in this expression. Never null. May be empty.
     */
    public abstract RtcQueryAttributeVariable[] getSelectedVariables();

    /**
     * Call this method to add new selected variable to this expression.
     * This can be long running operation. Do not call on event dispatch thread.
     * @param variable that should be added to this expression. Not null.
     * @throws IllegalArgumentException if given variable is not proper for this expression.
     */
    public abstract void addSelectedVariable(RtcQueryAttributeVariable variable) throws IllegalArgumentException;

    /**
     * Call this method to remove selected variable from this expression.
     *
     * If given variable is not selected in this expression does nothing.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param variable that have to be removed from this expression. Not null.
     */
    public abstract void removeSelectedVariable(RtcQueryAttributeVariable variable);

    /**
     * Listener interface for <code>RtcEditableAttributeExpression</code>.
     * If you want to listen for changes in selected values in this
     * <code>RtcExpression</code> you have to implement this interface and pass
     * this implementation to <code>RtcEditableAttributeExpression.addSelectedValuesChangeListener</code> method.
     *
     * @autor Patryk Żywica
     */
    public static interface RtcAttributeExpressionSelectedValueChangeListener {

        /**
         * Calling this method means that <code>oldValue</code> was replaced by <code>newValue</code>.
         *
         * If <code>oldValue</code> is <code>null</code> then it means that
         * <code>newValue</code> was added to expression.
         *
         * If <code>newValue</code> is <code>null</code> then it means that
         * <code>oldValue</code> was removed from expression.
         *
         * @param oldValue previous value, may be null.
         * @param newValue new value, may be null.
         */
        void selectedValueChanged(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue);
    }

    /**
     * Private class for running events.
     */
    private static class SelectedValueEventRunner implements Runnable {

        private RtcAttributeExpressionSelectedValueChangeListener l;
        private RtcQueryAttributeValue oldValue;
        private RtcQueryAttributeValue newValue;

        public SelectedValueEventRunner(RtcAttributeExpressionSelectedValueChangeListener l, RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) {
            this.l = l;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void run() {
            l.selectedValueChanged(oldValue, newValue);
        }
    }
}
