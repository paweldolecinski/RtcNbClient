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
package pl.edu.amu.wmi.kino.rtc.client.plans.actions.dialogs;

import java.awt.Cursor;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.windows.WindowManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
//import pl.edu.amu.wmi.kino.rtc.client.ui.api.common.advisor.Advisor;

/**
 *
 * @author michu
 */
public class RtcAddPlanDialog extends javax.swing.JDialog
        implements PropertyChangeListener, DocumentListener, HelpCtx.Provider {

	private static final long serialVersionUID = -7922447088989112362L;
	private Node node;
    private Iteration iteration;
    private TeamArea teamArea;
    private RtcPlanType planType;
    private Task task;
    String planName;
    private DefaultMutableTreeNode iterationRootNode =
            new DefaultMutableTreeNode("Iteration root node");
    private DefaultMutableTreeNode teamAreaRootNode =
            new DefaultMutableTreeNode("Team area root node");

    /** Creates new form RtcAddPlanDialog */
    public RtcAddPlanDialog(java.awt.Frame parent, boolean modal, Node node) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);

        this.node = node;

        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();

        nameTextField.getDocument().addDocumentListener(this);

    }

    public void setIteration(Object o) {
        iteration = (Iteration) o;
        IterationTextField.setText(iteration.getName());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    void setTeamArea(Object o) {
        teamArea = (TeamArea) o;
        ownerTextField.setText(teamArea.getName());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (nameTextField.getText().length() > 0) {
            createButton.setEnabled(true);
        } else {
            createButton.setEnabled(false);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (nameTextField.getText().length() > 0) {
            createButton.setEnabled(true);
        } else {
            createButton.setEnabled(false);
        }
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcAddPlanDialog.class);
    }

    class Task extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setProgress(0);

            getPlanTypes();
            setProgress(33);

            getIterations();
            setProgress(66);

            getTeamAreas();
            setProgress(100);

            return null;
        }

        @Override
        protected void done() {
            nameTextField.setEditable(true);
            typeComboBox.setEnabled(true);
            iterationBrowseButton.setEnabled(true);
            ownerBrowseButton.setEnabled(true);
            setCursor(null);
            progressContainer.setVisible(false);
        }
    }

    private void getPlanTypes() {
        RtcPlanType[] planTypes = node.getLookup().lookup(RtcPlansManager.class).getPlanTypes();

        for (RtcPlanType type : planTypes) {
            typeComboBox.addItem(new RtcPlanTypeComboItem(type));
        }
    }

    private class RtcPlanTypeComboItem {

        private RtcPlanType planType;

        public RtcPlanTypeComboItem(RtcPlanType planType) {
            this.planType = planType;
        }

        @Override
        public String toString() {
            return planType.getDisplayName();
        }

        public RtcPlanType getPlanType() {
            return planType;
        }
    }

    private void getTeamAreas() {

        ProjectArea projectArea = node.getLookup().lookup(ActiveProjectArea.class).getProjectArea();
        ProcessManager pm = node.getLookup().lookup(ActiveProjectArea.class).getLookup().lookup(ProcessManager.class);
        TeamArea[] teamAreas = pm.getTeamAreas();

        DefaultMutableTreeNode projectAreaNode = new RtcProjectAreaTreeNode(projectArea);

        for (int i = 0; i < teamAreas.length; i++) {
            DefaultMutableTreeNode teamAreaNode = new DefaultMutableTreeNode(teamAreas[i]);

            TeamArea[] areas = pm.getTeamAreas(teamAreas[i]);
            for (TeamArea a : areas) {
                teamAreaNode.add(new DefaultMutableTreeNode(a));
            }
            teamAreaRootNode.add(teamAreaNode);
        }
        //teamAreaRootNode.add(projectAreaNode);


        if (teamAreaRootNode.getFirstChild() != null) {
            setTeamArea(((DefaultMutableTreeNode) teamAreaRootNode.getFirstChild()).getUserObject());
        } else {
            throw new NullPointerException("There is no RtcTeamArea in lookup");
            //Advisor.displayAdviceAsTooltip(new Advisor.ExceptionInformation(new NullPointerException("There is no RtcTeamArea in lookup")));
        }
    }

    private class RtcProjectAreaTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -9206988258841444915L;
		private ProjectArea projectArea;

        public RtcProjectAreaTreeNode(ProjectArea projectArea) {
            this.projectArea = projectArea;
        }

        @Override
        public Object getUserObject() {
            return projectArea;
        }

        @Override
        public String toString() {
            return projectArea.getName();
        }
    }

    private void getIterations() {
        ActiveProjectArea area = node.getLookup().lookup(ActiveProjectArea.class);

        ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
        DevelopmentLine[] developmentLines = pm.getDevelopmentLines();
        for (int i = 0; i < developmentLines.length; i++) {

            Iteration[] iterations = pm.getIterations(developmentLines[i]);
            for (int j = 0; j < iterations.length; j++) {

                DefaultMutableTreeNode iter =
                        new DefaultMutableTreeNode(iterations[j]);


                if (pm.getIterations(iterations[j]).length!=0) {
                    childs(pm,iter, pm.getIterations(iterations[j]));
                }

                iterationRootNode.add(iter);
            }
        }

        if (node.getLookup().lookup(Iteration.class) != null) {
            setIteration(node.getLookup().lookup(Iteration.class));
        } else if (iterationRootNode.getFirstChild() != null) {
            setIteration(((DefaultMutableTreeNode) iterationRootNode.getFirstChild()).getUserObject());
        } else {
            throw new NullPointerException("There is no RtcIteration in lookup");
            //Advisor.displayAdviceAsTooltip(new Advisor.ExceptionInformation(new NullPointerException("There is no RtcIteration in lookup")));
        }

    }

    private void childs(ProcessManager pm,DefaultMutableTreeNode node, Iteration[] children) {
        for (int j = 0; j < children.length; j++) {

            DefaultMutableTreeNode c = new DefaultMutableTreeNode(children[j]);

            if (pm.getIterations(children[j]).length!=0) {
                childs(pm,c, pm.getIterations(children[j]));
            }

            node.add(c);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        IterationTextField = new javax.swing.JTextField();
        iterationBrowseButton = new javax.swing.JButton();
        ownerTextField = new javax.swing.JTextField();
        ownerBrowseButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        progressContainer = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.title")); // NOI18N
        setResizable(false);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel2.text")); // NOI18N

        nameTextField.setEditable(false);
        nameTextField.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.nameTextField.text")); // NOI18N
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel4.text")); // NOI18N

        IterationTextField.setEditable(false);
        IterationTextField.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.IterationTextField.text")); // NOI18N

        iterationBrowseButton.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.iterationBrowseButton.text")); // NOI18N
        iterationBrowseButton.setEnabled(false);
        iterationBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iterationBrowseButtonActionPerformed(evt);
            }
        });

        ownerTextField.setEditable(false);
        ownerTextField.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.ownerTextField.text")); // NOI18N

        ownerBrowseButton.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.ownerBrowseButton.text")); // NOI18N
        ownerBrowseButton.setEnabled(false);
        ownerBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ownerBrowseButtonActionPerformed(evt);
            }
        });

        jLabel5.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel5.text")); // NOI18N

        typeComboBox.setEnabled(false);
        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(69, 69, 69)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typeComboBox, 0, 355, Short.MAX_VALUE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(IterationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addComponent(ownerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ownerBrowseButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(iterationBrowseButton, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ownerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ownerBrowseButton)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IterationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iterationBrowseButton)
                    .addComponent(jLabel4))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        createButton.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(324, Short.MAX_VALUE)
                .addComponent(createButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        progressBar.setStringPainted(true);

        jLabel6.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout progressContainerLayout = new javax.swing.GroupLayout(progressContainer);
        progressContainer.setLayout(progressContainerLayout);
        progressContainerLayout.setHorizontalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressContainerLayout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
        );
        progressContainerLayout.setVerticalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressContainerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/crt_iteration_plan_wiz.gif"))); // NOI18N
        jLabel1.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel1.text")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText(org.openide.util.NbBundle.getMessage(RtcAddPlanDialog.class, "RtcAddPlanDialog.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progressContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        

    private void iterationBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iterationBrowseButtonActionPerformed

        Frame f = WindowManager.getDefault().getMainWindow();

        RtcAddPlanIterationsDialog dialog = new RtcAddPlanIterationsDialog(f, true, node, this, iterationRootNode);
        dialog.setVisible(true);
    }//GEN-LAST:event_iterationBrowseButtonActionPerformed

    private void ownerBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ownerBrowseButtonActionPerformed

        Frame f = WindowManager.getDefault().getMainWindow();

        RtcAddPlanTeamAreaDialog dialog = new RtcAddPlanTeamAreaDialog(f, true, node, this, teamAreaRootNode);
        dialog.setVisible(true);
    }//GEN-LAST:event_ownerBrowseButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed

        String name = nameTextField.getText();
        RtcPlan createNewPlan = node.getLookup().lookup(RtcPlansManager.class).createNewPlan(name, iteration, teamArea, planType);
        node.getLookup().lookup(RtcPlansManager.class).addPlan(createNewPlan);
        dispose();
    }//GEN-LAST:event_createButtonActionPerformed

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        JComboBox cb = (JComboBox) evt.getSource();
        planType = ((RtcPlanTypeComboItem) cb.getSelectedItem()).getPlanType();
    }//GEN-LAST:event_typeComboBoxActionPerformed

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
    }//GEN-LAST:event_nameTextFieldActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IterationTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton createButton;
    private javax.swing.JButton iterationBrowseButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton ownerBrowseButton;
    private javax.swing.JTextField ownerTextField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JComboBox typeComboBox;
    // End of variables declaration//GEN-END:variables
}
