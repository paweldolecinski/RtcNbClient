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

import java.awt.Frame;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.windows.WindowManager;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.dialogs.RtcAddPlanDialog;

/**
 *
 * @author michu
 */
public class RtcAddPlanAction extends NodeAction {

    private static RtcAddPlanAction instance;

    /**
     * Gets the singleton object of this Action.
     *
     * @return singleton object of this Action
     */
    public static RtcAddPlanAction getDefault() {
        if (instance == null) {
            instance = new RtcAddPlanAction();
        }
        return instance;
    }
    
    @Override
    protected void performAction(Node[] activatedNodes) {


        for (final Node node : activatedNodes) {

            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

                @Override
                public void run() {
                    Frame f = WindowManager.getDefault().getMainWindow();
                    RtcAddPlanDialog dialog = new RtcAddPlanDialog(f, true, node);
                    dialog.setVisible(true);
                }
            });
        }


    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    /**
     * Get help context for the action.
     *
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcAddPlanAction.class);
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

    @Override
    public String getName() {
        //TODO: fix it
        return "Add new plan";
    }

}
