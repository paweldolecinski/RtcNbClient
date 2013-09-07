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
package pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes;
import org.openide.util.NbBundle;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;

import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.ReloadCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet.RtcQueriesSetEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;

/**
 * Used for showing MyQueries node in any explorer view for example in RTC
 * panel in Work Items. Every child of this node will be created from
 * <code>IQueryDescriptor</code> fetched from <code>QueryClient.findPersonalQueries()</code>
 * <p>
 * This node takes its actions from <code>layer.xml</code>. All actions registered in
 * Rtc/Modules/Queries/MyQueriesActions will be shown in <code>MyQueriesNode</code>'s
 * context menu.
 *</p><p>
 * In next version there will be posibility to add actions arranged in tree-like
 * structure.
 *</p>
 * @author Patryk Żywica
 */
public class RtcQueriesSetNode extends AbstractNode implements EventListener<RtcQueriesSet.RtcQueriesSetEvent> {

    private Action[] actions;
    private InstanceContent content;

    public RtcQueriesSetNode(RtcQueriesSet set, InstanceContent content) {
        super(Children.create(new RtcQueriesSetChildFactory(set), true), new AbstractLookup(content));
        this.content = content;
        set.addListener(this);
        content.add(new ReloadCookie() {

            @Override
            public void reload() {
                RtcQueriesSet set = getLookup().lookup(RtcQueriesSet.class);
                set.getQueriesManager().synchronizeWithServer();
            }
        });
    }

    /**
     * Returns all actions registered in layer.xml under
     * Rtc/Modules/QueriesModule/MyQueriesActions directory. Actions are sorted by
     * possition atribute.
     * <p>
     * Action with the lowest position attribute is used as default action for node.
     * AddAction is declared with position equal to 100, so if you declare action with
     * position less then 100 your action will become default one.
     * </p><p>
     * In next version there will be posibility to add actions arranged in tree-like
     * structure.
     *</p>
     * @param context whether to find actions for context meaning or for the
     *   node itself
     * @return a list of actions (may include nulls for separators)
     */
    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            if (getLookup().lookup(RtcEditableQueriesSet.class) != null) {
                List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/EditableQueriesSetActions");
                actions = list.toArray(new Action[]{});
            } else {
                List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/QueriesSetActions");
                actions = list.toArray(new Action[]{});
            }
        }
        return actions;
    }

    @Override
    public void eventFired(RtcQueriesSetEvent event) {
        switch (event) {
            case NAME_CHANGED:
                setDisplayName(getLookup().lookup(RtcQueriesSet.class).getDisplayName());
                break;
            case QUERY_ADDED:
            case QUERY_REMOVED:
                RtcQueriesSet set = getLookup().lookup(RtcQueriesSet.class);
                setChildren(Children.create(new RtcQueriesSetChildFactory(set), true));
                break;
        }
    }

    @Override
    public HelpCtx getHelpCtx(){
        return new HelpCtx(RtcQueriesSetNode.class);
    }
}

class RtcQueriesSetChildFactory extends ChildFactory<RtcQuery> implements EventListener<RtcQueriesSet.RtcQueriesSetEvent> {

    RtcQueriesSet set;

    public RtcQueriesSetChildFactory(RtcQueriesSet set) {
        this.set = set;
        set.addListener(this);
    }

    @Override
    protected Node createNodeForKey(RtcQuery key) {
        RtcQueryNodeFactory factory = Lookup.getDefault().lookup(RtcQueryNodeFactory.class);
        if (factory == null) {
            throw new IllegalStateException(NbBundle.getMessage(RtcQueriesSetNode.class, "CannotFindRtcQueryNode"));
        }
        return factory.createNode(set, key);
    }

    @Override
    protected boolean createKeys(List<RtcQuery> toPopulate) {
        for (RtcQuery q : set.getQueries()) {
            toPopulate.add(q);
        }
        Collections.sort(toPopulate, new Comparator<RtcQuery>() {

            @Override
            public int compare(RtcQuery o1, RtcQuery o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return true;
    }

    @Override
    public void eventFired(RtcQueriesSetEvent event) {
        if (event.equals(RtcQueriesSetEvent.QUERY_ADDED) || event.equals(RtcQueriesSetEvent.QUERY_REMOVED)) {
            refresh(false);
        }
    }
}
