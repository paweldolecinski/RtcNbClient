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

import java.util.List;
import javax.swing.Action;
import org.openide.util.Utilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.ReloadCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;

/**
 * Used for showing Shared Queries node in any explorer view for example in RTC
 * panel in Work Items. Currently there are only two children for this node:
 * IndividuallySharedQueries and PredefinedQueries. 
 * <p>
 * In future there will be
 * posibility to create child for any other type of shared queries (e.g. Team
 * shared queries).
 * </p>
 * <p>
 * This node takes its actions from <code>layer.xml</code>. All actions registered in
 * Rtc/Modules/QueriesModule/SharedQueriesActions will be shown in <code>SharedQueriesNode</code>'s
 * context menu.
 * </p>
 * @author Patryk Żywica
 */
public class RtcSharedQueriesNode extends AbstractNode {

    private Action[] actions;

    public RtcSharedQueriesNode(RtcQueriesManager manager, InstanceContent content) {

        super(Children.create(new SharedQueriesChildFactory(manager), true), new AbstractLookup(content));
        content.add(new ReloadCookie() {

            @Override
            public void reload() {
                setChildren(
                        Children.create(
                        new SharedQueriesChildFactory(getLookup().lookup(RtcQueriesManager.class)), true));
            }
        });
    }

    /**
     * Returns all actions registered in layer.xml under
     * Rtc/Modules/QueriesModule/SharedQueriesActions directory. Actions are sorted by
     * possition atribute.
     * <p>
     * Action with the lowest position attribute is used as default action for node.
     * AddAction is declared with position equal to 100, so if you declare action with
     * position less then 100 your action will become default one.
     * </p><p>
     * In future version there will be posibility to add actions arranged in tree-like
     * structure.
     * </p>
     * @param context whether to find actions for context meaning or for the
     *   node itself
     * @return a list of actions (may include nulls for separators)
     */
    @Override
    public Action[] getActions(boolean bln) {
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/SharedQueriesActions");
            actions = list.toArray(new Action[]{});
        }
        return actions;
    }

    @Override
    public HelpCtx getHelpCtx(){
        return new HelpCtx(RtcSharedQueriesNode.class);
    }
}

class SharedQueriesChildFactory extends ChildFactory<RtcQueriesSet> {

    RtcQueriesManager manager;

    public SharedQueriesChildFactory(RtcQueriesManager manager) {
        this.manager = manager;
    }

    @Override
    protected Node createNodeForKey(RtcQueriesSet key) {
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        AbstractNode node = new RtcQueriesSetNode(key, ic);
        node.setName(key.getDisplayName());
        node.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/resources/folder.gif");
        return node;
    }

    @Override
    protected boolean createKeys(List<RtcQueriesSet> toPopulate) {
        for (RtcQueriesSet set : manager.getSharedQueriesSets()) {
            toPopulate.add(set);
        }
        return true;
    }
}
