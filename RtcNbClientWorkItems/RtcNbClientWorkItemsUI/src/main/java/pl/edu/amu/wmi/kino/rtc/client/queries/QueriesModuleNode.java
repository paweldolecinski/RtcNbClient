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
package pl.edu.amu.wmi.kino.rtc.client.queries;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes.RtcQueriesSetNode;
import pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes.RtcSharedQueriesNode;

/**
 * Root of Work Items node tree, which provides query management capabilities.
 * <p>
 * It has two children MyQueries and SharedQueries. First is responsible for
 * showing user defined queries. Second is showing other queries like Predefined,
 * team shared etc.
 * </p><p>
 * This node takes its actions from <code>layer.xml</code>. All actions registered in
 * Rtc/Modules/Queries/QueriesNodeActions will be shown in <code>QueriesModuleNode</code>'s
 * context menu.
 * </p><p>
 * In the next version there will be posibility to add actions arranged in tree-like
 * structure.
 * </p>
 * 
 * @author Patryk Żywica
 */
class QueriesModuleNode extends AbstractNode {

    private Action[] actions;
    private InstanceContent ic = new InstanceContent();

    public QueriesModuleNode(RtcQueriesManager manager, InstanceContent ic) {
        super(Children.create(new QueriesModuleNodeChildFactory(manager), true), new AbstractLookup(ic));
        ic.add(manager.getPersonalQueriesSet());
        setName(NbBundle.getMessage(QueriesModuleNode.class, "QueriesModuleNode.name"));
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/resources/workitem.gif");
        setShortDescription(NbBundle.getMessage(QueriesModuleNode.class, "QueriesModuleNode.tooltip"));
    }

    /**
     * Returns all actions registered in layer.xml under
     * Rtc/Modules/QueriesModule/QueriesNodeActions directory. 
     * Actions are sorted by possition atribute.
     * <p>
     * Action with the lowest position attribute is used as default action for node.
     * AddAction is declared with position equal to 100, so if you declare action with
     * position less then 100 your action will become default one.
     *</p><p>
     * In the next version there will be posibility to add actions arranged in tree-like
     * structure.
     *</p>
     *
     * @param context whether to find actions for context meaning or for the
     *   node itself
     * @return a list of actions (may include nulls for separators)
     */
    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/QueriesNodeActions");
            actions = list.toArray(new Action[]{});
        }
        return actions;
    }

    @Override
    public HelpCtx getHelpCtx(){
        return new HelpCtx(QueriesModuleNode.class);
    }
}

class QueriesModuleNodeChildFactory extends ChildFactory {

    private RtcQueriesManager manager;
    private static Object MY = new Object(), SHARED = new Object();

    public QueriesModuleNodeChildFactory(RtcQueriesManager manager) {
        this.manager = manager;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        InstanceContent ic;
        if (key.equals(MY)) {
            ic = new InstanceContent();
            ic.add(manager.getPersonalQueriesSet());
            RtcQueriesSetNode mNode = new RtcQueriesSetNode(manager.getPersonalQueriesSet(), ic);
            mNode.setName(manager.getPersonalQueriesSet().getDisplayName());
            mNode.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/resources/folder.gif");
            mNode.setShortDescription(NbBundle.getMessage(RtcQueriesSetNode.class, "RtcQueriesSetNode.tooltip"));
            return mNode;
        } else {
            if (key.equals(SHARED)) {
                ic = new InstanceContent();
                ic.add(manager);
                RtcSharedQueriesNode sNode = new RtcSharedQueriesNode(manager, ic);
                sNode.setName(NbBundle.getMessage(RtcSharedQueriesNode.class, "SharedQueriesNode.name"));
                sNode.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/resources/folder.gif");
                sNode.setShortDescription(NbBundle.getMessage(RtcSharedQueriesNode.class, "SharedQueriesNode.tooltip"));
                return sNode;
            } else {
                return new AbstractNode(Children.LEAF);
            }
        }
    }

    @Override
    protected boolean createKeys(List toPopulate) {
        toPopulate.add(MY);
        toPopulate.add(SHARED);
        return true;
    }

    
}
