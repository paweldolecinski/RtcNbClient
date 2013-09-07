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

import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression;

/**
 *
 * @author Patryk Żywica
 */
class RtcEditableTermExpressionImpl extends RtcEditableTermExpression implements RtcEditableExpression.RtcExpressionListener, RtcEditableExpressionImpl {

    private RtcEditableTermExpressionImpl parent;
    private final List<RtcEditableExpression> subExpressions =
            Collections.synchronizedList(new LinkedList<RtcEditableExpression>());
    private Operator operator;
    private ActiveProjectArea area;
    private boolean addListeners = false;
    private RtcEditableTermExpressionImpl oldParent;
    private boolean hidden = false;

    public RtcEditableTermExpressionImpl(RtcEditableTermExpressionImpl parent, ActiveProjectArea area, boolean hidden) {
        this.parent = parent;
        this.area = area;
        this.hidden = hidden;
    }

    public RtcEditableTermExpressionImpl(RtcEditableTermExpressionImpl parent, ActiveProjectArea area) {
        this(parent, area, false);
    }

    public RtcEditableTermExpressionImpl(ActiveProjectArea area) {
        this(null, area, false);
    }

    @Override
    public RtcTermOperator getOperator() {
        return toRtcTermOperator(operator);
    }

    @Override
    public void setOperator(RtcTermOperator op) {
        this.operator = toOperator(op);

        if (addListeners) {
            fireEvent(RtcExpressionEvent.TERM_OPERATOR_CHANGED);
        }
    }

    public void setOperator(Operator op) {
        this.operator = op;
        if (addListeners) {
            fireEvent(RtcExpressionEvent.TERM_OPERATOR_CHANGED);
        }
    }

    @Override
    public RtcEditableTermExpressionImpl getParent() {
        return parent;
    }

    @Override
    public void addSubExpression(RtcEditableExpression ex) {
        assert (!EventQueue.isDispatchThread());
        subExpressions.add(ex);
        if (addListeners) {
            ex.addListener(this);
            if (ex instanceof RtcEditableExpressionImpl) {
                ((RtcEditableExpressionImpl) ex).initExpressionListeners();
            }
        }
        if (addListeners) {
            fireEvent(RtcExpressionEvent.SUBEXPRESSION_ADDED);
        }
    }

    @Override
    public void addSubExpressions(RtcEditableExpression[] exs) {
        assert (!EventQueue.isDispatchThread());
        for (RtcEditableExpression ex : exs) {
            subExpressions.add(ex);
            if (addListeners) {
                ex.addListener(this);
                if (ex instanceof RtcEditableExpressionImpl) {
                    ((RtcEditableExpressionImpl) ex).initExpressionListeners();
                }
            }
        }
        if (addListeners) {
            fireEvent(RtcExpressionEvent.SUBEXPRESSION_ADDED);
        }
    }

    @Override
    public void removeSubExpression(RtcEditableExpression ex) {
        assert (!EventQueue.isDispatchThread());
        if (subExpressions.remove(ex)) {
            ex.removeListener(this);

            if (addListeners) {
                fireEvent(RtcExpressionEvent.SUBEXPRESSION_REMOVED);
            }
        }
    }

    @Override
    public void removeSubExpressions(RtcEditableExpression[] exs) {
        assert (!EventQueue.isDispatchThread());
        for (RtcEditableExpression ex : exs) {
            if (subExpressions.remove(ex)) {
                ex.removeListener(this);
            }
        }
        if (addListeners) {
            fireEvent(RtcExpressionEvent.SUBEXPRESSION_REMOVED);
        }
    }

    @Override
    public RtcEditableExpression[] getSubExpressions() {
        synchronized (subExpressions) {
            return subExpressions.toArray(new RtcEditableExpression[]{});
        }
    }

    @Override
    public Expression createExpression() {
        assert (!EventQueue.isDispatchThread());
        ArrayList<Expression> exprs = new ArrayList<Expression>();
        for (RtcEditableExpression ex : getSubExpressions()) {
            if (ex instanceof RtcEditableExpressionImpl) {
                Expression newEx = ((RtcEditableExpressionImpl) ex).createExpression();
                if (newEx != null) {
                    exprs.add(newEx);
                }
            } else {
                throw new IllegalStateException("Invalid subExpression");
            }
        }
        if (exprs.size() > 1 || isHidden()) {
            Term t = new Term(getOperator().equals(RtcTermOperator.AND) ? Term.Operator.AND : Term.Operator.OR, exprs.toArray(new Expression[]{}));
            if (isHidden()) {
                t.setInternal(hidden);
            }
            return t;
        } else {
            if (exprs.size() > 0) {
                if (exprs.get(0) instanceof Term) {
                    Term ter = (Term) exprs.get(0);
                    if (isHidden()) {
                        ter.setInternal(isHidden());
                    }
                    return ter;
                } else {
                    Term t = new Term(getOperator().equals(RtcTermOperator.AND) ? Term.Operator.AND : Term.Operator.OR, exprs.toArray(new Expression[]{}));
                    if (isHidden()) {
                        t.setInternal(hidden);
                    }
                    return t;
                }
            } else {
                return null;
            }
        }

    }

    public static RtcTermOperator toRtcTermOperator(Operator operator) {
        switch (operator) {
            case AND:
                return RtcTermOperator.AND;
            case OR:
                return RtcTermOperator.OR;
        }
        return RtcTermOperator.AND;
    }

    public static Operator toOperator(RtcTermOperator rtcTermOperator) {
        switch (rtcTermOperator) {
            case AND:
                return Operator.AND;
            case OR:
                return Operator.OR;
        }
        return Operator.AND;
    }


    @Override
    public void initExpressionListeners() {
        addListeners = true;
        for (RtcEditableExpression expr : getSubExpressions()) {
            if (expr instanceof RtcEditableExpressionImpl) {
                ((RtcEditableExpressionImpl) expr).initExpressionListeners();
            }
            expr.addListener(this);
        }
    }

    /* package */ void setAdjustedRoot(boolean adjusted) {
        if (adjusted) {
            oldParent = parent;
            parent = null;
        } else {
            parent = oldParent;
            oldParent = null;
        }
    }

    public boolean isHidden() {
        return hidden;
    }

//    /*package*/ void subexpressionChangeNofity() {
//        JOptionPane.showMessageDialog(null, "change notify");
//        fireEvent(RtcExpressionEvent.SUBEXPRESSION_CHANGED);
//    }
    @Override
    public void expressionChanged(RtcExpressionEvent e) {
        fireEvent(RtcEditableExpression.RtcExpressionEvent.SUBEXPRESSION_CHANGED);
    }

    @Override
    public void changeParentTerm(RtcEditableTermExpressionImpl parent) {
        this.parent = parent;
        this.oldParent = parent;
    }

    @Override
    public ActiveProjectArea getProjectArea() {
        return area;
    }
}
