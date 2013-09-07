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

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;

/**
 *
 * @author Bartosz Zaleski
 */
public class RtcPageRenameAction extends CallableSystemAction {
    private static final long serialVersionUID = 45341435345L;
    @Override
    public void performAction() {

        SwingWorker pageWorker= new SwingWorker<Object,Void>() {

            @Override
            protected Object doInBackground() throws Exception {
                RtcPlanPage page= Utilities.actionsGlobalContext().lookup(RtcPlanPage.class);
                if(page!=null){
                    String str = (String)JOptionPane.showInputDialog(
                            (Component)null,
                            NbBundle.getMessage(RtcPageRenameAction.class, "CTL_RtcPageRename.msg"),
                            NbBundle.getMessage(RtcPageRenameAction.class, "CTL_RtcPageRename.title"),
                            JOptionPane.QUESTION_MESSAGE,
                            (Icon)null,
                            null,
                            page.getName());
                    if(str!=null){
                        page.setName(str);
                    }
                }
                return null;
            }
        };
        pageWorker.execute();
        
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(RtcPageRenameAction.class, "CTL_RtcPageRename");
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
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/renamePage.gif";
    }


}
