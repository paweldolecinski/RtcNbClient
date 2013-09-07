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

import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression.RtcTermOperator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcAttributeOperationImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeImpl;
import java.awt.EventQueue;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpressionFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression;

/**
 * @see RtcEditableExpressionFactory
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcEditableExpressionFactory.class)
public class RtcEditableExpressionFactoryImpl implements RtcEditableExpressionFactory {

    @Override
    public RtcEditableAttributeExpressionImpl createAttributeExpression(
            RtcQueryAttribute attribute,
            RtcEditableTermExpression parent) {
        assert (!EventQueue.isDispatchThread());
        if (attribute instanceof RtcQueryAttributeImpl
                && parent instanceof RtcEditableTermExpressionImpl
                && attribute.getAttributeOperations()[0] instanceof RtcAttributeOperationImpl) {
            return new RtcEditableAttributeExpressionImpl(
                    (RtcQueryAttributeImpl) attribute,
                    (RtcAttributeOperationImpl) attribute.getAttributeOperations()[0],
                    (RtcEditableTermExpressionImpl) parent);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public RtcEditableTermExpressionImpl createTermExpression(RtcEditableExpression parent, RtcTermOperator operation) {
        if (parent instanceof RtcEditableTermExpressionImpl) {
            RtcEditableTermExpressionImpl impl = (RtcEditableTermExpressionImpl) parent;
            RtcEditableTermExpressionImpl term = new RtcEditableTermExpressionImpl(impl, impl.getProjectArea());
            term.setOperator(operation);
            return term;
        }
        throw new IllegalArgumentException();
    }
}
