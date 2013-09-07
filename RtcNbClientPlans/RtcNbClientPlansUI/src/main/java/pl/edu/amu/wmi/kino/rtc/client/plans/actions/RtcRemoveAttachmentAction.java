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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.RtcPageEditingPanel;

public final class RtcRemoveAttachmentAction extends CallableSystemAction {

    private static final long serialVersionUID = 1001002881001L;

    @Override
    public void performAction() {
        
        SwingWorker pageWorker = new SwingWorker<Object, Void>() {

            @Override
            protected Object doInBackground() throws Exception {
                RtcPlanPage page = Utilities.actionsGlobalContext().lookup(RtcPlanPage.class);
                String[][] temp;
                RtcPageEditingPanel pane = Utilities.actionsGlobalContext().lookup(RtcPageEditingPanel.class);
                temp = pane.getAttachmentPane().removeAttachmentEntry();
                for (int i = 0; i < temp.length; i++) {
                    for (int j = page.getAttachments().length - 1; j > 0; j--) {
                        //TODO : b4lzak : tu jest to zrobione paskudnie, trzeba sie zastranowić jak inaczej
                        // dolek: temp[i][1].equals(String.valueOf(page.getAttachments()[j].getContent().getLength()))
                        // usunalem to bo temp[i][1] teraz da ci wartosc w kilobajtach z dopiskiem KB a getContent().getLength() da ci wartosc w bajtach. trza to zamieniac :)
                        if (temp[i][0].equals(page.getAttachments()[j].getName())) {
                            page.removeAttachment(page.getAttachments()[j]);
                        }
                    }
                }
                return null;
            }
        };
        int opt=JOptionPane.showConfirmDialog(null, "Do you really want to remove this attachment?");
        if(opt==JOptionPane.YES_OPTION){
            pageWorker.execute();
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcAddAttachmentAction.class, "CTL_RtcAddAttachmentAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/removePage.gif";
    }
}
