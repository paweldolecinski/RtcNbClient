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
package pl.edu.amu.wmi.kino.rtc.client.queries.actions;

import java.awt.EventQueue;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.QueryEditorTopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQuery;

/**
 * <p>
 * A type of action that listens on change in activated nodes selection. 
 * This action is enabled only when selected node has <code>RtcEditableQueriesSet</code>
 * in its lookup.
 * </p>
 * <p>
 * Whenever a list of activated nodes changes (a new
 * <a href="@org-openide-windows@/org/openide/windows/TopComponent.html">
 * TopComponent</a> is selected or its internal selection changes)
 * action tests if selected node has  <code>RtcEditableQueriesSet</code> in their lookup.
 * Only one node can be selected when this action is active.
 * </p>
 *
 * 
 * @author Patryk Żywica
 * @see #performAction(org.openide.nodes.Node[])
 * @see <a href="http://bits.netbeans.org/6.8/javadoc/org-openide-nodes/org/openide/util/actions/CookieAction.html">CookieAction</a>
 */
public final class AddQueryAction extends NodeAction {

    static private AddQueryAction action;

    private AddQueryAction() {
        super();
    }

    /**
     * Gets the singleton object of this Action.
     *
     * @return singleton object of this Action
     */
    static public NodeAction getDefault() {
        if (action == null) {
            action = new AddQueryAction();
        }
        return action;
    }

    /**
     * For each selected node, this method finds its <code>AddQueryCookie</code>
     * and runs its <code>AddQueryCookie.add</code> method.
     *
     *@param activatedNodes array of currently selected nodes
     */
    @Override
    protected void performAction(Node[] activatedNodes) {
//        JOptionPane.showMessageDialog(null, "there will be add query dialog here");
        Node node = activatedNodes[0];
        RtcEditableQueriesSet queriesSet = node.getLookup().lookup(RtcEditableQueriesSet.class);
        RtcEditableQuery query = queriesSet.createEditableQuery();
        EventQueue.invokeLater(new EditorRunner(query, queriesSet));
    }

    /**
     * Gets the localized display name for this action from bundle.properties.
     *
     * @return localized action display name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(AddQueryAction.class, "CTL_AddQueryAction.name");
    }

    /**
     * This method configures the object, giving it icon, etc.
     */
    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE); 
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Returning true means that each call of this action from Netbeans GUI will be
     * asynchronous(on separate thread).
     * 
     * @return always true
     */
    @Override
    protected boolean asynchronous() {
        return true;
    }

    /**
     * This method checks if there is selected only one node and if it has
     * <code>RtcEditableQueriesSet</code> in its lookup.
     * 
     * @param activatedNodes currently selected nodes
     * @return true if there is selected exacly one node and it has  <code>RtcEditableQueriesSet</code>, false otherwidse.
     * @see pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet
     */
    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes.length == 1
                && activatedNodes[0].getLookup().lookup(RtcEditableQueriesSet.class) != null) {
            return true;
        } else {
            return false;
        }
    }

    private class EditorRunner implements Runnable {

        private RtcEditableQuery query;
        private RtcEditableQueriesSet set;

        public EditorRunner(RtcEditableQuery query, RtcEditableQueriesSet set) {
            this.query = query;
            this.set = set;
        }

        @Override
        public void run() {
            TopComponent tc = QueryEditorTopComponent.findInstanceFor(query, set);
            tc.open();
            tc.requestActive();
        }
    }
}
