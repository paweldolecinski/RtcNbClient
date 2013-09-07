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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes;

import com.ibm.team.workitem.common.model.AttributeOperation;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcAttributeOperation;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression.RtcTermOperator;

/**
 * @see RtcAttributeOperation
 * @author Patryk Żywica
 */
public class RtcAttributeOperationImpl implements RtcAttributeOperation {

    private AttributeOperation operation;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcAttributeOperationImpl) {
            RtcAttributeOperationImpl oper = (RtcAttributeOperationImpl) obj;
            return operation.getIdentifier().equals(oper.getAttributeOperation().getIdentifier());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.operation != null ? this.operation.hashCode() : 0);
        return hash;
    }

    public RtcAttributeOperationImpl(AttributeOperation operation) {
        this.operation = operation;
    }

    @Override
    public RtcTermOperator getTermOperator() {
        return operation.isNegation() ? RtcTermOperator.AND : RtcTermOperator.OR;
    }

    @Override
    public String getDisplayName() {
        return operation.getDisplayName();
    }

    public AttributeOperation getAttributeOperation() {
        return operation;
    }
}
