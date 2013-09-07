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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreeModel;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.model.RtcApprovalsExtendedEditorRowModel;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.model.RtcApprovalsExtendenEditorRenderData;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalContributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovals;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.model.RtcApprovalsExtendedEditorTreeModel;

/**
 *
 * @author Dawid Holewa
 */
public class RtcApprovalsExtendedPanel extends javax.swing.JPanel implements VetoableChangeListener {

	private static final long serialVersionUID = -7997929517472139757L;
	RtcApproval root = null;
    List<RtcApprovalType> types = null;
    List<RtcApprovalState> states = null;
    List<Contributor> contributors = null;
    Outline outline1;
    OutlineModel mdl;
    private PropertyEnv env;
    private final Lookup context;
    PropertyEditorSupport editor;

    /** Creates new form RtcApprovalsExtendedPanel */
    public RtcApprovalsExtendedPanel(PropertyEditorSupport editor, Lookup context, RtcApproval root, PropertyEnv env) {
        this.env = env;
        this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        this.env.addVetoableChangeListener(this);
        this.context = context;
        this.root = root;
        this.editor = editor;
        types = context.lookup(RtcApprovals.class).getPossibleTypes();
        states = context.lookup(RtcApprovals.class).getPossibleStates();
        contributors = context.lookup(RtcWorkItemAttributePossibleValues.class).getPossibleValues();
        initComponents();
        initOurComponents();
    }

    private void initOurComponents() {
        TreeModel treeMdl = new RtcApprovalsExtendedEditorTreeModel(root);

        mdl = DefaultOutlineModel.createOutlineModel(treeMdl, new RtcApprovalsExtendedEditorRowModel(root),
                false, org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedEditor.column1.name.text"));

        outline1 = new Outline(mdl);
        outline1.setRootVisible(false);
        outline1.getOutlineModel().getTreePathSupport().clear();
        outline1.setRenderDataProvider(new RtcApprovalsExtendenEditorRenderData());
        outline1.createDefaultColumnsFromModel();

        // List of possible states as ComboBox list for state column.
        JComboBox comboBox = new JComboBox(states.toArray());
        outline1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

        // Listening on selection changes.
        outline1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = outline1.getSelectedRow();
                Object valueAt = outline1.getOutlineModel().getValueAt(index, 0);

                if (valueAt instanceof RtcApprovalDescriptor) {
                    buttonEditApproval.setEnabled(true);
                    buttonAddApprovers.setEnabled(true);
                } else if (valueAt instanceof RtcApproval) {
                    buttonEditApproval.setEnabled(false);
                }

                if (index == -1) {
                    buttonAddApprovers.setEnabled(false);
                    buttonEditApproval.setEnabled(false);
                    buttonRemove.setEnabled(false);
                } else {
                    buttonRemove.setEnabled(true);
                }
            }
        });

        outline1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Connecting outline component witch scrollbar
        jScrollPane1.setViewportView(outline1);

    }

    private void outlineRefresh() {
        // This is a big ugly HACK!!
        // TODO: future: treeModel should use TreeNode objects and listeners on changes.
        initOurComponents();
        //HACK HACK HACK
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void addDescriptors(RtcApprovalDescriptor des) {
        root.addApproval(des);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        buttonAddApproval = new javax.swing.JButton();
        buttonEditApproval = new javax.swing.JButton();
        buttonAddApprovers = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(500, 316));

        buttonAddApproval.setText(org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.buttonAddApproval.text")); // NOI18N
        buttonAddApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddApprovalActionPerformed(evt);
            }
        });

        buttonEditApproval.setText(org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.buttonEditApproval.text")); // NOI18N
        buttonEditApproval.setEnabled(false);
        buttonEditApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditApprovalActionPerformed(evt);
            }
        });

        buttonAddApprovers.setText(org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.buttonAddApprovers.text")); // NOI18N
        buttonAddApprovers.setEnabled(false);
        buttonAddApprovers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddApproversActionPerformed(evt);
            }
        });

        buttonRemove.setText(org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.buttonRemove.text")); // NOI18N
        buttonRemove.setEnabled(false);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonAddApproval, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEditApproval, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAddApprovers)
                    .addComponent(buttonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAddApproval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonEditApproval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonAddApprovers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRemove)))
                .addContainerGap())
        );
    }// </editor-fold>                        

    private void buttonAddApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddApprovalActionPerformed
        RtcApprovalsExtendedApprovalPanel panel = new RtcApprovalsExtendedApprovalPanel(this, types);

        NotifyDescriptor nd = new NotifyDescriptor(panel, org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.addApproval.text"), NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

        if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
            String sub = panel.getSubject();
            RtcApprovalType t = panel.getType();
            Timestamp time = panel.getDue();

            if (sub.length() > 0) {
                RtcApprovalDescriptor des = null;
                if (t != null) {
                    des = context.lookup(RtcApprovals.class).createDescriptor(t, panel.getSubject());
                    root.addApproval(des);
                }
                if (time != null) {
                    des.setDueDate(time);
                }

                outlineRefresh();
            }
        }
}//GEN-LAST:event_buttonAddApprovalActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
//        int index[] = outline1.getSelectedRows();
//        for (int i : index) {
//            if (i >= 0) {
//                Object valueAt = outline1.getOutlineModel().getValueAt(i, 0);
//                if (valueAt instanceof RtcApprovalDescriptor) {
//                    root.remove((RtcApprovalDescriptor) valueAt);
//                } else if (valueAt instanceof RtcApproval) {
//                    context.lookup(RtcApprovals.class).remove((RtcApproval) valueAt);
//                }
//            }
//        }
        outlineRefresh();
}//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonEditApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditApprovalActionPerformed
        RtcApprovalsExtendedApprovalPanel panel = new RtcApprovalsExtendedApprovalPanel(this, types);

        int selectedRow = outline1.getSelectedRow();
        Object valueAt = outline1.getOutlineModel().getValueAt(selectedRow, 0);

        if (valueAt instanceof RtcApprovalDescriptor) {
            RtcApprovalDescriptor value = (RtcApprovalDescriptor) valueAt;
            panel.setSubject(value.getName());
            panel.setType(value.getType());
            Timestamp t = (value.getDueDate());
            if (t != null) {
                panel.setDue(t.toString());
            }

            NotifyDescriptor nd = new NotifyDescriptor(panel, org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.editApproval.text"), NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

            if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {
                value.setName(panel.getSubject());
                value.setType(panel.getType());
                if (panel.getDue() != null) {
                    ((RtcApprovalDescriptor) valueAt).setDueDate(panel.getDue());
                }

                outlineRefresh();
            }
        }
    }//GEN-LAST:event_buttonEditApprovalActionPerformed

    private void buttonAddApproversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddApproversActionPerformed
        int selectedRow = outline1.getSelectedRow();
        Object valueAt = outline1.getOutlineModel().getValueAt(selectedRow, 0);
        if (valueAt instanceof RtcApprovalContributor) {
            valueAt = ((RtcApprovalContributor) valueAt).getDescriptor();
        }

        if (valueAt instanceof RtcApprovalDescriptor) {
            RtcContributorEditorPanel panel = new RtcContributorEditorPanel(this);

            NotifyDescriptor nd = new NotifyDescriptor(panel, org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedPanel.editApproval.text"), NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, NotifyDescriptor.OK_OPTION);

            if (DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION) {


                if (panel.getSelected() != null) {
                    for (Object object : panel.getSelected()) {
                        RtcApprovalDescriptor desc = ((RtcApprovalDescriptor) valueAt);
                        RtcApproval createApproval = desc.createApproval((Contributor) object);
                        desc.addApproval(createApproval);
                    }
                }

                outlineRefresh();
            }
        }
    }//GEN-LAST:event_buttonAddApproversActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddApproval;
    private javax.swing.JButton buttonAddApprovers;
    private javax.swing.JButton buttonEditApproval;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        editor.setValue(root);
    }
}
