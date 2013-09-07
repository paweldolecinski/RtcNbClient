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
package pl.edu.amu.wmi.kino.rtc.client.plans.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;

/**
 * <p>
 * A type of action that listens on change in activated nodes selection
 * and allows its subclasses to simply change their enabled state and
 * handle action invocation requests.
 * </p>
 * 
 * @author Michal Wojciechowski
 */
public class RefreshAction extends NodeAction {

    private static RefreshAction instance;

    /**
     * Gets the singleton object of this Action.
     * 
     * @return singleton object of this Action
     */
    public static RefreshAction getDefault() {
        if (instance == null) {
            instance = new RefreshAction();
        }
        return instance;
    }

    /**
     * For each selected node, this method finds proper object in lookup
     * (<code>RtcPlansManager</code> or <code>RtcPlan</code>) and runs its
     * <code>RtcPlansManager.synchronizeWithServer</code> or
     * <code>RtcPlan.synchronizeWithServer</code> method.
     *
     * @param nodes array of currently selected nodes
     */
    @Override
    protected void performAction(Node[] nodes) {
        for (Node node : nodes) {
            if (node.getLookup().lookup(RtcPlan.class) != null) {
                node.getLookup().lookup(RtcPlan.class).synchronizeWithServer();
            } else {
                if (node.getLookup().lookup(RtcPlansManager.class) != null) {
                    node.getLookup().lookup(RtcPlansManager.class).synchronizeWithServer();
                }
            }
        }
    }

    /**
     * Returning true if proper domain object
     * (<code>RtcPlansManager</code> or <code>RtcPlan</code>)
     * exists in lookup.
     *
     * @param nodes
     * @return always true
     */
    @Override
    protected boolean enable(Node[] nodes) {
        for (Node node : nodes) {
            if (node.getLookup().lookup(RtcPlan.class) != null) {
                return true;
            }
            if (node.getLookup().lookup(RtcPlansManager.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the localized display name of action from bundle.properties.
     *
     * @return action display name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(RefreshAction.class, "RefreshAction.name");
    }

    /**
     * Get help context for the action.
     *
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
}
