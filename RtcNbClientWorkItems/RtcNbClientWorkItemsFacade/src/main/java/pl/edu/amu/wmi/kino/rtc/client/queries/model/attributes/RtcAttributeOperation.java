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

import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression;

/**
 * Implementations of this interface will represent attribute operations.
 * @author Patryk Żywica
 */
public interface RtcAttributeOperation {

    /**
     * <p>
     * This method should return term operator (AND or OR) that should connect all
     * selected values of <code>RtcAttributeExpression</code> with attribute operation
     * set to this <code>RtcAttributeOperation</code>.
     *</p><p>
     * For example attribute operation for equality in most cases should return OR because if
     * we select two values it is impossible that tested value will be equal to both
     * of them. Attribute operation for difference in most cases should return AND
     * because is we select two values we will expect that tested value should be different
     * from both selected values.
     *</p>
     *
     * @return term operator specific to this <code>RtcAttributeOperation</code>
     */
    public RtcEditableTermExpression.RtcTermOperator getTermOperator();

    /**
     * Should return localized display name for this operation
     *
     * @return display name of this operation
     */
    public String getDisplayName();
}
