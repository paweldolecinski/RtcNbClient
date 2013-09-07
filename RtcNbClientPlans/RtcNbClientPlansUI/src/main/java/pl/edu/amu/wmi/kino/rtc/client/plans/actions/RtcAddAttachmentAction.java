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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.RtcPageEditingPanel;

/**
 * 
 * @author Bartosz Zaleski
 * @author Pawel Dolecinski
 */
public final class RtcAddAttachmentAction extends CallableSystemAction {

    private static final long serialVersionUID = 5410026881001L;

    @Override
    public void performAction() {
        RtcShowAttachmentsTableAction action = SystemAction.get(RtcShowAttachmentsTableAction.class);
        if (!action.getBooleanState()) {
            action.actionPerformed(null);
        }
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int mode = fc.showDialog(null, "Select");

        if (mode == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            List<String> fileNames = new ArrayList<String>(files.length);
            for (File name : files) {
                fileNames.add(name.getPath());
            }
            SwingWorker attWorker = new AddAttachmentsSwingWorker(fileNames);
            attWorker.execute();
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
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/addAttachment.gif";
    }

    private static class AddAttachmentsSwingWorker extends SwingWorker<List<RtcPlanPageAttachment>, Void> {

        private final List<String> fileNames;

        public AddAttachmentsSwingWorker(List<String> names) {
            this.fileNames = names;
        }

        @Override
        protected List<RtcPlanPageAttachment> doInBackground() throws Exception {
            RtcPlanPage page = Utilities.actionsGlobalContext().lookup(RtcPlanPage.class);
            RtcPlan plan = Utilities.actionsGlobalContext().lookup(RtcPlan.class);
            List<RtcPlanPageAttachment> files = new ArrayList<RtcPlanPageAttachment>();
            
            //RtcContent rc = RtcContentUtilities.convertToContent(file);

            //TODO : bikol :
            throw new UnsupportedOperationException();
//            RtcContributor creator = plan.getPlansManager().getActiveProjectArea().getLoggedInContributor();
//            for (String string : fileNames) {
//                RtcPlanPageAttachment a = plan.getPlansManager().createNewAttachment(creator, string, " ");
//                page.addAttachment(a);
//                files.add(a);
//            }
//            return files;
        }

        @Override
        protected void done() {
            try {
                List<RtcPlanPageAttachment> files = get();
                RtcPageEditingPanel pane = Utilities.actionsGlobalContext().lookup(RtcPageEditingPanel.class);
                for (RtcPlanPageAttachment a : files) {
                    long bytes = a.getContent().getLength();
                    long size = bytes / 1024;
                    if (bytes != 0) {
                        size = Math.max(1, size);
                    }
                    String length = size + " KB";
                    String desc = a.getDescription() != null ? a.getDescription() : NbBundle.getMessage(RtcAddAttachmentAction.class, "Attachment_UNKNOWN_DESCRIPTION");

                    String[] rowData = {" "+a.getName(), " "+desc, " "+a.getCreator().getName(), " " + length, " "+a.getContent().getContentType()};
                    ((DefaultTableModel) (pane.getAttachmentPane().getAttachmentContent().getModel())).addRow(rowData);
                }
                pane.getAttachmentPane().getAttachmentContent().doLayout();

            } catch (InterruptedException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAddAttachmentAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            } catch (ExecutionException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAddAttachmentAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }

        }
    }
}
