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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;

/**
 *
 * @author Patryk Żywica
 */
class ContributorProviderNode extends AbstractNode {

    public ContributorProviderNode(RtcQueryAttributeValueProvider vals, RtcEditableAttributeExpression expression) {
        super(Children.create(new ContributorProviderChildFactory(vals, expression, null), true));
    }

    public ContributorProviderNode(RtcQueryAttributeValueProvider vals, RtcEditableAttributeExpression expression, Boolean selected) {
        super(Children.create(new ContributorProviderChildFactory(vals, expression, selected), true));
    }
}

class ContributorProviderChildFactory extends ChildFactory<RtcQueryAttributeValue> {

    private RtcQueryAttributeValueProvider vals;
    private RtcEditableAttributeExpression expression;
    private Boolean selected = null;
    private ArrayList<RtcQueryAttributeValue> list;

    public ContributorProviderChildFactory(RtcQueryAttributeValueProvider vals, RtcEditableAttributeExpression expression) {
        this.vals = vals;
        this.expression = expression;
    }

    public ContributorProviderChildFactory(RtcQueryAttributeValueProvider vals, RtcEditableAttributeExpression expression, Boolean selected) {
        this.vals = vals;
        this.expression = expression;
        this.selected = selected;
    }

    @Override
    protected boolean createKeys(List<RtcQueryAttributeValue> toPopulate) {
        toPopulate.addAll(Arrays.asList(vals.getValues()));

        Set<RtcQueryAttributeValue> toAdd = new HashSet<RtcQueryAttributeValue>();
        toAdd.addAll(Arrays.asList(vals.getValues()));
        RtcQueryAttributePossibleValues pVals;
        if ((pVals = expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePossibleValues.class)) != null && pVals != vals) {
            for (RtcQueryAttributeValue val : intersection(
                    new LinkedHashSet<RtcQueryAttributeValue>(Arrays.asList(pVals.getValues())),
                    new LinkedHashSet<RtcQueryAttributeValue>(Arrays.asList(expression.getSelectedValues())))) {
                if (!toAdd.contains(val)) {
                    toPopulate.add(val);
                }
            }
        }
        list = null;
        //TODO : bikol : fi double values in editor
        //TODO : bikol : add support for selected values in child values
        return true;
    }

    private <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new LinkedHashSet<T>();
        for (T x : setA) {
            if (setB.contains(x)) {
                tmp.add(x);
            }
        }
        return tmp;
    }

    @Override
    protected Node createNodeForKey(RtcQueryAttributeValue key) {
        boolean sel = false;
        if (selected == null) {
            for (RtcQueryAttributeValue val : expression.getSelectedValues()) {
                if (key.equals(val)) {
                    sel = true;
                    break;
                }
            }
        } else {
            sel = selected;
        }
        ContributorSelectedValuesMap.getMap().put(key, sel);
        RtcQueryAttributeValueChildFactory fac = new RtcQueryAttributeValueChildFactory(key, vals, expression);
        InstanceContent ic = new InstanceContent();
        ic.add(vals);
        ic.add(key);
        ic.add(new ContributorCheckableNodeImpl(key, vals, expression, sel));
        AbstractNode node = new RtcQueryAttributeValueNode(Children.create(fac, true), vals.getIconFor(key), ic);
        node.setDisplayName(vals.getDisplayName(key));
        return node;
    }
}
