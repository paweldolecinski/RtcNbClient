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

import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.RenameCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * <p>
 * A type of action that listens on change in activated nodes selection and
 * its cookies. This action is enabled only when selected node has <code>RenameCookie</code>
 * in its cookie set.
 * </p>
 * <p>
 * Whenever a list of activated nodes changes (a new
 * <a href="@org-openide-windows@/org/openide/windows/TopComponent.html">
 * TopComponent</a> is selected or its internal selection changes)
 * action tests if selected node has <code>RenameCookie</code> in their cookie set.
 * Only one node can be selected when this action is active.
 * When the action is performed, <code>RenameCookie.rename()</code> is called for <code>RenameCookie</code>
 * of selected <code>Node</code>. This method can perform should perform all
 * operations for renaming selected nodes ( e.g. it may change name window).
 * </p>
 * 
 * @author Son
 * @author Patryk Żywica
 * @see <a href="http://bits.netbeans.org/6.8/javadoc/org-openide-nodes/org/openide/util/actions/CookieAction.html">CookieAction</a>
 */
public final class RenameAction extends CookieAction {

    static private RenameAction action;

    private RenameAction() {
        super();
    }

    /**
     * Gets the singleton object of this Action.
     *
     * @return singleton object of this Action
     */
    static public CookieAction getDefault() {
        if (action == null) {
            action = new RenameAction();
        }
        return action;
    }

    /**
     * For each selected node, this method finds its <code>RenameCookie</code>
     * and runs its <code>RenameCookie.rename</code> method.
     *
     * @param activatedNodes array of currently selected nodes
     * @see RenameCookie
     */
    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            for (RenameCookie cookie : node.getLookup().lookupAll(RenameCookie.class)) {
                cookie.rename();
            }
        }
    }

    /** Get the mode of the action: how strict it should be about
     * cookie support.
     * @return the mode of the action. Possible values are disjunctions of the <code>MODE_XXX</code>
     * constants. */
    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    /**
     * Gets the localized display name of action from bundle.properties.
     *
     * @return action display name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(RenameAction.class, "CTL_RenameAction.name");
    }

    /** Get the cookies that this action requires. The cookies are disjunctive, i.e. a node
     * must support AT LEAST ONE of the cookies specified by this method.
     *
     * <p>This action uses only <code>RenameCookie</code> which is defined in package
     * <code>pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies</code>
     * @see RenameCookie
     * @return a list of cookies
     */
    @Override
    protected Class[] cookieClasses() {
        return new Class[]{RenameCookie.class};
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
     * Get a help context for the action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Returning true means that each call of this action from Netbeans GUI will be
     * asynchronous(on separate thread).
     * @return always true
     */
    @Override
    protected boolean asynchronous() {
        return true;
    }
}
