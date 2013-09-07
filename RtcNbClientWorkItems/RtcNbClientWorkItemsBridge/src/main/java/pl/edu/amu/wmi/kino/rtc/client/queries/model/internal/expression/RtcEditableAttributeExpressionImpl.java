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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcAttributeOperationImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeVariableImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.model.AttributeOperation;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcAttributeOperation;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression.RtcTermOperator;

/**
 *
 * @author Patryk Żywica
 */
class RtcEditableAttributeExpressionImpl extends RtcEditableAttributeExpression implements RtcEditableExpressionImpl {

    private RtcEditableTermExpressionImpl parent;
    private RtcAttributeOperationImpl operation;
    private final Set<RtcQueryAttributeValueImpl> selectedValues =
            Collections.synchronizedSet(new HashSet<RtcQueryAttributeValueImpl>());
    private RtcQueryAttributeImpl queryAttribute;
    private final Set<RtcQueryAttributeVariableImpl> variables =
            Collections.synchronizedSet(new HashSet<RtcQueryAttributeVariableImpl>());
    private boolean addListeners = false;

    public RtcEditableAttributeExpressionImpl(RtcQueryAttributeImpl queryAttribute, RtcAttributeOperationImpl operation, RtcEditableTermExpressionImpl parent) {
        this.parent = parent;
        this.queryAttribute = queryAttribute;
        assert (operation != null);
        this.operation = operation;
    }

    @Override
    public RtcEditableTermExpressionImpl getParent() {
        return parent;
    }

    /**
     *
     * @return never null
     */
    @Override
    public RtcAttributeOperationImpl getSelectedAttributeOperation() {
        return operation;
    }

    @Override
    public RtcQueryAttributeValueImpl[] getSelectedValues() {
        synchronized (selectedValues) {
            return selectedValues.toArray(new RtcQueryAttributeValueImpl[]{});
        }
    }

    @Override
    public void addSelectedValue(RtcQueryAttributeValue value) throws IllegalArgumentException {
        assert (!EventQueue.isDispatchThread());
        if (selectedValues.add((RtcQueryAttributeValueImpl) value) == false) {
            throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "AddSelectedValue.error"));
        }

        if (addListeners) {
            fireEvent(RtcExpressionEvent.SELECTED_VALUE_ADDED);
            fireSelectedValueChangeEvent(null, value);
        }
    }

    @Override
    public void removeSelectedValue(RtcQueryAttributeValue value) {
        assert (!EventQueue.isDispatchThread());
        if (selectedValues.remove(value) && addListeners) {
            fireEvent(RtcExpressionEvent.SELECTED_VALUE_REMOVED);
            fireSelectedValueChangeEvent(value, null);
        }
    }

    /**
     * @see RtcEditableAttributeExpression
     * @param oldValue
     * @param newValue
     * @throws IllegalArgumentException
     */
    @Override
    public void changeSelectedValue(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) throws IllegalArgumentException {
        if (oldValue.equals(newValue)) {
            return;
        }
        synchronized (selectedValues) {
            if (selectedValues.contains(oldValue)) {
                if (newValue instanceof RtcQueryAttributeValueImpl && oldValue instanceof RtcQueryAttributeValueImpl) {
                    if (selectedValues.add((RtcQueryAttributeValueImpl) newValue)) {
                        selectedValues.remove((RtcQueryAttributeValueImpl) oldValue);
                        fireEvent(RtcExpressionEvent.SELECTED_VALUE_CHANGED);
                        fireSelectedValueChangeEvent(oldValue, newValue);
                    } else {
                        throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "AddSelectedValue.error"));
                    }
                } else {
                    throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "NotProperValueType.error"));
                }
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "oldValueNotSelected.error"));
            }
        }
    }

    @Override
    public void setSelectedAttributeOperation(RtcAttributeOperation operation) throws IllegalArgumentException {
        assert (!EventQueue.isDispatchThread());
        if (operation instanceof RtcAttributeOperationImpl) {
            RtcAttributeOperationImpl o = (RtcAttributeOperationImpl) operation;
            boolean good = false;
            for (RtcAttributeOperationImpl attr : queryAttribute.getAttributeOperations()) {
                if (isGoodOperator(attr, o)) {
                    good = true;
                    break;
                }
            }
            if (good) {
                assert (o != null);
                this.operation = o;
                if (addListeners && parent != null) {
                    fireEvent(RtcExpressionEvent.ATTRIBUTE_OPERATION_CHANGED);
                }
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "NotProperAttributeOperation.error"));
            }
        } else {
            throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "NotProperAttributeOperation type"));
        }
    }

    public static AttributeOperation toAttributeOperation(RtcAttributeOperationImpl operation) {
        return operation.getAttributeOperation();
    }

    public static RtcAttributeOperationImpl toRtcAttributeOperation(AttributeOperation operation) {
        return new RtcAttributeOperationImpl(operation);
    }

    @Override
    public RtcQueryAttributeImpl getQueryAttribute() {
        return queryAttribute;
    }

    /**
     *
     * @return <code>Expression</code> that represents this <code>RtcEditableAttibuteExpression</code>.
     * May be null if there is no selected values.
     */
    @Override
    public Expression createExpression() {
        assert (!EventQueue.isDispatchThread());
        ArrayList<AttributeExpression> aExprs = new ArrayList<AttributeExpression>();
        synchronized (selectedValues) {
            for (RtcQueryAttributeValueImpl val : selectedValues) {
                assert (queryAttribute != null);
                assert (operation != null);
                assert (val != null);
                if (val.getValue() == null) {
                    if (queryAttribute.getQueryableAttribute().hasNullValue()) {
                        aExprs.add(new AttributeExpression(
                                queryAttribute.getQueryableAttribute(),
                                operation.getAttributeOperation(),
                                queryAttribute.getQueryableAttribute().getNullValue()));
                    } else {
                        aExprs.add(new AttributeExpression(
                                queryAttribute.getQueryableAttribute(),
                                operation.getAttributeOperation(),
                                val.getValue()));
                    }
                } else {
                    aExprs.add(new AttributeExpression(
                            queryAttribute.getQueryableAttribute(),
                            operation.getAttributeOperation(),
                            val.getValue()));
                }
            }


            for (RtcQueryAttributeVariableImpl variable : getSelectedVariables()) {
                aExprs.add(new VariableAttributeExpression(
                        queryAttribute.getQueryableAttribute(),
                        operation.getAttributeOperation(),
                        variable.getVariable()));
            }
            if (selectedValues.size() + variables.size() > 0) {
                //we are doing like in eclipse, all (even single value) expressions are wrapped into Term
                return new Term(
                        operation.getTermOperator().equals(RtcTermOperator.AND) ? Term.Operator.AND : Term.Operator.OR,
                        aExprs.toArray(new AttributeExpression[]{}));
            } else {
                return null;
//            if (selectedValues.size() > 0) {
//                return new AttributeExpression(queryAttribute.getQueryableAttribute(), operation.getAttributeOperation(), selectedValues.get(0).getValue());
//            } else {
//                if (variables.size() > 0) {
//                    return new VariableAttributeExpression(queryAttribute.getQueryableAttribute(), operation.getAttributeOperation(), variables.get(0).getVariable());
//                } else {
//                    return null;
//                }
//            }
            }
        }
    }

    private boolean isGoodOperator(RtcAttributeOperationImpl attr1, RtcAttributeOperationImpl attr2) {
        if (attr1 == null || attr2 == null) {
            return false;
        }
        if (attr1.equals(attr2)) {
            return true;
        }
        if (attr1.getAttributeOperation().equals(AttributeOperation.GREATER_OR_EQUALS)
                && attr2.getAttributeOperation().equals(AttributeOperation.AFTER)) {
            return true;
        }
        if (attr2.getAttributeOperation().equals(AttributeOperation.GREATER_OR_EQUALS)
                && attr1.getAttributeOperation().equals(AttributeOperation.AFTER)) {
            return true;
        }
        return false;
    }

    @Override
    public void initExpressionListeners() {
        addListeners = true;
    }

    @Override
    public void addSelectedVariable(RtcQueryAttributeVariable variable) {
        assert (!EventQueue.isDispatchThread());
        if (variable instanceof RtcQueryAttributeVariableImpl) {
            variables.add((RtcQueryAttributeVariableImpl) variable);

            if (addListeners && parent != null) {
                fireEvent(RtcExpressionEvent.SELECTED_VARIABLE_ADDED);
            }
        } else {
            throw new IllegalArgumentException(NbBundle.getMessage(RtcEditableAttributeExpressionImpl.class, "NotProperVariableType.error"));
        }
    }

    @Override
    public void removeSelectedVariable(RtcQueryAttributeVariable variable) {
        assert (!EventQueue.isDispatchThread());
        variables.remove(variable);

        if (addListeners && parent != null) {
            fireEvent(RtcExpressionEvent.SELECTED_VARIABLE_REMOVED);
        }

    }

    @Override
    public RtcQueryAttributeVariableImpl[] getSelectedVariables() {
        synchronized (variables) {
            return variables.toArray(new RtcQueryAttributeVariableImpl[]{});
        }
    }

    @Override
    public void changeParentTerm(RtcEditableTermExpressionImpl parent) {
        this.parent = parent;
    }

    @Override
    public ActiveProjectArea getProjectArea() {
        return parent.getProjectArea();
    }
}
