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

import org.openide.cookies.OpenCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * <p>
 * A type of action that listens on change in activated nodes selection and
 * its cookies. This action is enabled only when selected nodes has <code>OpenCookie</code>
 * in its cookie set.
 * </p>
 * <p>
 * Whenever a list of activated nodes changes (a new
 * <a href="@org-openide-windows@/org/openide/windows/TopComponent.html">
 * TopComponent</a> is selected or its internal selection changes)
 * action tests if all of selected nodes has <code>OpenCookie</code> in their cookie set.
 * When the action is performed, <code>OpenCookie.open()</code> is called for <code>OpenCookie</code>
 * of every selected <code>Node</code>. This method can perform should perform all
 * operations for opening selected nodes ( e.g. it should create and open new
 * <code>TopComponent</code> ).
 * </p>
 * 
 * @author Son
 * @author Patryk Żywica
 * @see <a href="http://bits.netbeans.org/6.8/javadoc/org-openide-nodes/org/openide/util/actions/CookieAction.html">CookieAction</a>
 */
public final class OpenAction extends CookieAction {

    static private OpenAction action;

    private OpenAction() {
        super();
    }

    /**
     * Gets the singleton object of this Action.
     *
     * @return singleton object of this Action
     */
    static public CookieAction getDefault() {
        if (action == null) {
            action = new OpenAction();
        }
        return action;
    }

    /**
     * For each selected node, this method finds its <code>OpenCookie</code>
     * and runs its <code>OpenCookie.open</code> method.
     *
     * @param activatedNodes array of currently selected nodes
     * @see OpenCookie
     */
    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            OpenCookie cookie = node.getCookie(OpenCookie.class);
            cookie.open();
        }
    }

    /** Get the mode of the action: how strict it should be about
     * cookie support.
     * 
     * @return the mode of the action. Possible values are disjunctions of the <code>MODE_XXX</code>
     * constants. */
    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    /**
     * Gets the localized display name of action from bundle.properties.
     *
     * @return action display name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(OpenAction.class, "CTL_OpenAction.name");
    }

    /** Get the cookies that this action requires. The cookies are disjunctive, i.e. a node
     * must support AT LEAST ONE of the cookies specified by this method.
     * 
     * <p>This action uses only <code>OpenCookie</code> which is Netbeans class
     * for opening objects.</p>
     * 
     * @return a list of cookies required by this action
     * @see <a href="http://bits.netbeans.org/6.8/javadoc/org-openide-nodes/org/openide/cookies/OpenCookie.html">OpenCookie</a>
     */
    @Override
    protected Class[] cookieClasses() {
        return new Class[]{OpenCookie.class};
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
     * Get help context for the action.
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
