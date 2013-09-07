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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.factories.enumeration;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;

/**
 *
 * @author Patryk Żywica
 */
class RtcQueryAttributeValueChildFactory extends ChildFactory<RtcQueryAttributeValue> {

    private RtcQueryAttributeValue value;
    private RtcQueryAttributeValueProvider vals;
    private RtcEditableAttributeExpression expression;

    public RtcQueryAttributeValueChildFactory(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider vals, RtcEditableAttributeExpression ex) {
        this.value = value;
        this.vals = vals;
        this.expression = ex;
    }

    @Override
    protected boolean createKeys(List<RtcQueryAttributeValue> toPopulate) {
        toPopulate.addAll(Arrays.asList(vals.getChildValues(value)));
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcQueryAttributeValue key) {
        boolean selected = false;
        for (RtcQueryAttributeValue val : expression.getSelectedValues()) {
            if (key.equals(val)) {
                selected = true;
                break;
            }
        }
        RtcQueryAttributeValueChildFactory fac = new RtcQueryAttributeValueChildFactory(key, vals, expression);
        InstanceContent ic = new InstanceContent();
        ic.add(vals);
        ic.add(key);
        ic.add(new CheckableNodeImpl(key, vals, expression, selected));
        AbstractNode node =
                new RtcQueryAttributeValueNode(Children.create(fac, true), vals.getIconFor(key), ic);
        node.setDisplayName(vals.getDisplayName(key));
        return node;
    }
}
