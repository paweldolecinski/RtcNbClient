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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.BooleanStateAction;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.RtcPageEditingPanel;

public final class RtcShowAttachmentsTableAction extends BooleanStateAction{
    private static final long serialVersionUID = 10021006698L;

    public RtcShowAttachmentsTableAction() {
        
        setBooleanState(false);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {//trzeba zmieniac przy resizei ogolnie -  dodać nasluch
        super.actionPerformed(ev);

        setIcon(new ImageIcon(iconResource()));
        SwingWorker buttonWorker=new SwingWorker<RtcPageEditingPanel,Void>() {
            @Override
            protected RtcPageEditingPanel doInBackground() throws Exception {
                RtcPageEditingPanel pane=Utilities.actionsGlobalContext().lookup(RtcPageEditingPanel.class);
                GridBagConstraints c=((GridBagLayout)pane.getLayout()).getConstraints(pane.getAttachmentPane());
                if(c.weighty<0.02){
                    c.weighty=0.3;
                    ((GridBagLayout)pane.getLayout()).setConstraints(pane.getAttachmentPane(), c);
                }
                else{
                    c.weighty=0.005;
                    ((GridBagLayout)pane.getLayout()).setConstraints(pane.getAttachmentPane(), c);
                }
                return pane;
            }
            
            @Override
            protected void done() {
                try{
                    RtcPageEditingPanel pane=get();
                    pane.revalidate();
                    pane.getAttachmentPane().doLayout();
                    pane.getTextEditor().doLayout();
                    pane.doLayout();
                }catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcShowAttachmentsTableAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcShowAttachmentsTableAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        };
        buttonWorker.execute();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcShowAttachmentsTableAction.class, "CTL_RtcShowAttachmentsTableAction");
    }
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        if(getBooleanState()){
            return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/collapse.gif";
        }
        else{
            return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/expand.gif";
        }
    }



}
