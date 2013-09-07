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
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
/**
 *
 * @author Bartosz Zaleski
 */
public class RtcAddPlanPageAction extends CallableSystemAction {
    private static final long serialVersionUID = 324153534L;

    @Override
    public void performAction(){
       SwingWorker planWorker= new SwingWorker<Object,Void>(){
            @Override
            protected Object doInBackground(){
                Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
                RtcPlan plan=Utilities.actionsGlobalContext().lookup(RtcPlan.class);
                    RtcPlanPage page;
                    String name=JOptionPane.showInputDialog( NbBundle.getMessage(RtcAddPlanPageAction.class, "CTL_AddWikiPageAction.msg"));
                    if(plan!=null){
                        page=plan.getPlansManager().createNewPage(name);
                        page.setName(name);
                        plan.addPage(page);

                    }
                    return null;
            }
       };
       planWorker.execute();
       
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/createPage.gif";
    }


    @Override
    public String getName() {
        return NbBundle.getMessage(RtcAddPlanPageAction.class, "CTL_AddWikiPageAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

}
