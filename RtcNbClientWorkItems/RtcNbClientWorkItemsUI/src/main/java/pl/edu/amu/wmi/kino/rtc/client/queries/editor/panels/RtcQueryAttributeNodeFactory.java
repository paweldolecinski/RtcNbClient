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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.panels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcQueryAttributeNodeFactory.class)
public class RtcQueryAttributeNodeFactory {

    public Node getRootNode(RtcQueryAttributeManager manager) {
        AbstractNode n = new AbstractNode(Children.create(new RtcQueryAttributeRootChildFactory(manager), true));

        return n;
    }

    public Node createNode(RtcQueryAttribute attribute) {
        InstanceContent ic = new InstanceContent();
        ic.add(attribute);
        AbstractNode n = new AbstractNode(
                Children.create(new RtcQueryAttributeChildFactory(attribute), true),
                new AbstractLookup(ic));
        n.setDisplayName(attribute.getDisplayName());
        switch (attribute.getType()) {
            case BUILD_IN:
                n.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/build_in_attribute.png");
                break;
            default:
                n.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/custom_attribute.png");
        }

        return n;
    }
}

class RtcQueryAttributeChildFactory extends ChildFactory<RtcQueryAttribute> {

    RtcQueryAttribute attribute;

    RtcQueryAttributeChildFactory(RtcQueryAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    protected Node createNodeForKey(RtcQueryAttribute key) {
        return Lookup.getDefault().lookup(RtcQueryAttributeNodeFactory.class).createNode(key);
    }

    @Override
    protected boolean createKeys(List<RtcQueryAttribute> toPopulate) {
        RtcQueryAttribute[] attrs = attribute.getChildren();
        for (RtcQueryAttribute child : attrs) {
            toPopulate.add(child);
        }
        Collections.sort(toPopulate, new Comparator<RtcQueryAttribute>() {

            @Override
            public int compare(RtcQueryAttribute o1, RtcQueryAttribute o2) {
                return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
            }
        });
        return true;
    }
}

class RtcQueryAttributeRootChildFactory extends ChildFactory<RtcQueryAttribute> {

    RtcQueryAttributeManager manager;

    public RtcQueryAttributeRootChildFactory(RtcQueryAttributeManager manager) {
        this.manager = manager;
    }

    @Override
    protected Node createNodeForKey(RtcQueryAttribute key) {
        return Lookup.getDefault().lookup(RtcQueryAttributeNodeFactory.class).createNode(key);
    }

    @Override
    protected boolean createKeys(List<RtcQueryAttribute> toPopulate) {
        RtcQueryAttribute[] attrs = manager.getQueryAttributes();
        for (RtcQueryAttribute attr : attrs) {
            toPopulate.add(attr);
        }
        Collections.sort(toPopulate, new Comparator<RtcQueryAttribute>() {

            @Override
            public int compare(RtcQueryAttribute o1, RtcQueryAttribute o2) {
                return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
            }
        });
        return true;
    }
}
