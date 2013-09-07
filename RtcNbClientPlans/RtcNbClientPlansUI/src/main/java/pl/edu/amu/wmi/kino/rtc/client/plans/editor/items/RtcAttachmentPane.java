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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContentUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.toolbars.RtcAttachmentsToolbar;

/**
 *
 * @author Bartosz Zaleski <b4lzak@gmail.com>
 */
public class RtcAttachmentPane extends javax.swing.JPanel {

    private static final long serialVersionUID = 20669810359734L;
    private JScrollPane tablePanel;
    private JTable attTable;
    private JToolBar attToolbar;
    private boolean isEdited;

    public RtcAttachmentPane(RtcPlanPageAttachment[] ra) {
        initComponents();
        Object content[][];
        if(ra!=null){
            content = new Object[ra.length][5];
            for (int i = 0; i < ra.length; i++) {
                content[i][0] = " "+ra[i].getName();
                content[i][1] = " "+ nonNull(ra[i].getDescription(), NbBundle.getMessage(RtcAttachmentPane.class, "Attachment_UNKNOWN_DESCRIPTION"));
                content[i][2] = " "+ (ra[i].isPredefined() ? NbBundle.getMessage(RtcAttachmentPane.class, "Attachment_PREDEFINED_CREATOR") : ra[i].getCreator().getName());
                long bytes = ra[i].getContent().getLength();
                long size = bytes / 1024;
                if (bytes != 0) {
                    size = Math.max(1, size);
                }
                content[i][3] = " "+ size + " KB";
                content[i][4] = " "+ ra[i].getContent().getContentType();
            }
        }
        else{
            content=new Object[1][5];
            content[1][0]=" ";
            content[1][1]=" ";
            content[1][2]=" ";
            content[1][3]=" ";
            content[1][4]=" ";
        }
        String names[] = new String[5];
        names[0] = "Name";
        names[1] = "Description";
        names[2] = "Creator";
        names[3] = "Size";
        names[4] = "Type";
        DefaultTableModel model = new DefaultTableModel(content, names) {
            private static final long serialVersionUID = 33015097357534L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attTable = new JTable(model);
        attTable.setRowSelectionAllowed(true);
        attTable.setColumnSelectionAllowed(false);
        attTable.setDragEnabled(false);
        attTable.getTableHeader().setReorderingAllowed(false);
        attTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        attToolbar = new RtcAttachmentsToolbar();
        tablePanel = new JScrollPane(attTable);
        tablePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tablePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        attTable.setFillsViewportHeight(true);
        tablePanel.setPreferredSize(new Dimension(1, 0));
        this.setLayout(new BorderLayout());
        //add(attToolbar,BorderLayout.PAGE_START);
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        this.add(attToolbar, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.insets = new Insets(3, 3, 0, 3);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Manage this page's attachments", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
        this.add(tablePanel, c);
        //add(tablePanel,BorderLayout.CENTER);

        attTable.addMouseListener(new MouseAdapter() {
            private int row;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    row = target.getSelectedRow();
                    if (row != -1) {
                        RequestProcessor.getDefault().post(new Runnable() {

                            @Override
                            public void run() {
                                RtcPlanPageAttachment toOpen = null;
                                try {
                                    RtcPlanPage plan = Utilities.actionsGlobalContext().lookup(RtcPlanPage.class);
                                    toOpen = plan.getAttachments()[row];
                                } finally {
                                    ProgressHandle ph = ProgressHandleFactory.createHandle("OpeningFileProgressBar.name");
                                    try {
                                        ph.start();
                                        ph.switchToIndeterminate();
                                        Desktop.getDesktop().open(RtcContentUtilities.convertToFile(toOpen.getContent()));
                                        ph.finish();
                                    } catch (IOException ex) {
                                        //RtcLogger.getLogger().warning(ex.getLocalizedMessage());
                                        RtcLogger.getLogger(RtcAttachmentPane.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                                    } finally {
                                        ph.finish();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void setAttachmentContent(JTable table) {
        throw new UnsupportedOperationException("Strona nie ma metody zwracającej jej attachmenty");
    }

    public JScrollPane getScrollPane() {
        return tablePanel;
    }

    public JToolBar getToolbar() {
        return attToolbar;
    }

    public JTable getAttachmentContent() {
        return attTable;
    }

    public String[][] removeAttachmentEntry() {
        DefaultTableModel model = (DefaultTableModel) attTable.getModel();
        int numRows = attTable.getSelectedRows().length;
        String[][] result = new String[numRows][2];
        for (int i = 0; i < numRows; i++) {
            result[i][0] = (String) attTable.getValueAt(attTable.getSelectedRow(), 0);
            result[i][1] = (String) attTable.getValueAt(attTable.getSelectedRow(), 3);
            model.removeRow(attTable.getSelectedRow());
        }
        return result;
    }

    private String nonNull(String str, String dflt) {
        return str != null ? str : dflt;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
    }// </editor-fold>                        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
