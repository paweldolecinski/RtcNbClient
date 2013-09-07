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

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;

/**
 *
 * @author Bartosz Zaleski
 */
public class RtcRemovePlanPageAction extends CallableSystemAction {
    private static final long serialVersionUID = 4351435L;

    @Override
    public void performAction() {
        SwingWorker planPageWorker = new SwingWorker<Object,Void>(){
            @Override
            protected Object doInBackground() {
                RtcPlanPage page = Utilities.actionsGlobalContext().lookup(RtcPlanPage.class);
                RtcPlan plan = Utilities.actionsGlobalContext().lookup(RtcPlan.class);
                if (plan == null || page == null) {
                        assert false : "No plan or page in global actions context";
                    }
                    else {
                        int opt = JOptionPane.showConfirmDialog(
                                null,
                                NbBundle.getMessage(RtcRemovePlanPageAction.class, "CTL_RtcRemovePlanPageAction.msg"),
                                NbBundle.getMessage(RtcRemovePlanPageAction.class, "CTL_RtcRemovePlanPageAction.title"),
                                JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_OPTION) {
                            plan.removePage(page);
                        }
                    }
                return null;
            }
        };
        planPageWorker.execute();
         
        
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcRemovePlanPageAction.class, "CTL_RtcRemovePlanPageAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/removePage.gif";
    }
}
