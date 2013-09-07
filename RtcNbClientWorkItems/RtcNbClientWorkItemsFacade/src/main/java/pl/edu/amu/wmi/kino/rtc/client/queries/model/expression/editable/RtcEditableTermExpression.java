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


/**
 * This class is one of expression types. It represents term based expressions, that
 * can aggregate other expression.
 * @author Patryk Żywica
 */
public abstract class RtcEditableTermExpression extends RtcEditableExpression {

    /**
     * This method adds given expression to this term expression.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param ex
     */
    public abstract void addSubExpression(RtcEditableExpression ex);

    /**
     * This method adds given array of expressions to this term expression.
     *
     * Fires only one SUBEXPRESSION_ADDED event
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param ex
     */
    public abstract void addSubExpressions(RtcEditableExpression[] ex);

    /**
     * This method removes given expression from this term expression.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param ex
     */
    public abstract void removeSubExpression(RtcEditableExpression ex);

    /**
     * This method removes given expressions from this term expression.
     *
     * Fires only one SUBEXPRESSION_REMOVED event.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param ex
     */
    public abstract void removeSubExpressions(RtcEditableExpression[] ex);

    /**
     * @see RtcTermOperator
     * @param op operator that should be set for this term expression.
     */
    public abstract void setOperator(RtcTermOperator op);

    /**
     *
     * @return currently selected term operator
     */
    public abstract RtcTermOperator getOperator();

    /**
     *
     * @return array of all expression aggregated by this term expression.
     */
    public abstract RtcEditableExpression[] getSubExpressions();



    /**
     * This enumeration represents term operator.
     */
    public enum RtcTermOperator {

        /**
         * And operator.
         */
        AND,
        /**
         * Or operator
         */
        OR;
    }
}
