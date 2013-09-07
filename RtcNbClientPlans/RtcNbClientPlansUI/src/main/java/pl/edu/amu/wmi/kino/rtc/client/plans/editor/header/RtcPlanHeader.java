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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.header;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.SystemAction;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewHeader;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker.RtcPlanItemCheckerEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemCheckerUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem.RtcSeverity;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.RtcShowPlanChecksAction;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.bars.RtcProgressBar;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.checker.RtcPlanItemCheckerPopupFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanHeader extends KinoMultiViewHeader implements HelpCtx.Provider,EventListener<RtcPlanItemCheckerEvent> {

    private Dimension areaDatabasicSize;
    private Dimension iterationDatabasicSize;
    private static final long serialVersionUID = 1L;
    /** Creates new form RtcPlanHeader
     * @param plan
     */
    private RtcPlan myPlan;
    private Color startColor = Color.WHITE;
    private Color endColor = Color.decode("#E0E8F1");
    

    public RtcPlanHeader(final RtcPlan plan) {
        initComponents();
        myPlan = plan;

        SwingWorker planDateWorker = new SwingWorker<Iteration, Void>() {

            @Override
            protected Iteration doInBackground() throws Exception {
                Iteration it = plan.getIteration();
                return it;
            }

            @Override
            protected void done() {
                try {
                    Iteration it = get();
                    if (it.getEndDate() != null && it.getStartDate() != null) {
                        GregorianCalendar kalendarz = new GregorianCalendar();
                        GregorianCalendar kalendarz2 = new GregorianCalendar();
                        kalendarz.setTime(it.getStartDate());
                        kalendarz2.setTime(it.getEndDate());
                        iterationData.setText(it.getName() + " (" + kalendarz.get(Calendar.DATE) + "." + (kalendarz.get(Calendar.MONTH) + 1) + "." + kalendarz.get(Calendar.YEAR) + " - " + kalendarz2.get(Calendar.DATE) + "." + (kalendarz2.get(Calendar.MONTH) + 1) + "." + kalendarz2.get(Calendar.YEAR) + ")");
                    } else {
                        iterationData.setText(it.getName() + " (" + "-" + "." + "-" + "." + "-" + ")");
                    }
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        };
        planDateWorker.execute();

        SwingWorker namesWorker = new SwingWorker<String[], Void>() {

            @Override
            protected String[] doInBackground() throws Exception {
                String[] result = new String[2];
                result[0] = plan.getOwner().getName();
                result[1] = plan.getName();
                return result;
            }

            @Override
            protected void done() {
                try {
                    String names[] = get();
                    areaData.setText(names[0]);
                    headerName.setText(names[1]);
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        };
        namesWorker.execute();

        iconPanel.setLayout(new BorderLayout());



        SwingWorker progressInfoWorker = new SwingWorker<RtcProgressInfo, Void>() {

            @Override
            protected RtcProgressInfo doInBackground() throws Exception {
                RtcProgressInfo p = plan.getPlansManager().getProgressInfo(plan.getOwner(), plan.getIteration(), plan.getComplexityComputator());
                return p;
            }

            @Override
            protected void done() {
                try {
                    RtcProgressInfo p = get();
                    jProgressBarContainer.add(new RtcProgressBar(p, plan.getComplexityComputator(), RtcProgressBar.Layout.HORIZONTAL));
                    closed.setText(Integer.toString(p.getCompletedItemsCount()));
                    opened.setText(Integer.toString(p.getPlannedItemsCount() - p.getCompletedItemsCount()));
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        };
        progressInfoWorker.execute();

        checkerButton.setAction(SystemAction.get(RtcShowPlanChecksAction.class));

        SwingWorker planCheckerWorker = new SwingWorker<RtcPlanItemChecker[], Void>() {

            RtcSeverity rs = RtcSeverity.NONE;

            @Override
            protected RtcPlanItemChecker[] doInBackground() throws Exception {
                RtcPlanItemChecker rpic[] = plan.getPlanItemCheckers();

                RtcPlanItemProblem pr;
                if(rpic!=null){
                    for (int i = 0; i < rpic.length; i++) {
                        
                        pr = rpic[i].getMostSevereProblem();
                        if (pr != null) {
                            if (RtcPlanItemCheckerUtilities.compareSeverities(rs, pr.getSeverity()) > 0) {
                                rs = pr.getSeverity();
                            }
                        }
                    }
                }
                return rpic;
            }

            @Override
            protected void done() {
                    try{
                        RtcPlanItemChecker[] rpic=get();
                        for(int i=0;i<rpic.length;i++){
                            RtcPlanHeader h=returnThis();
                            rpic[i].addListener(h);
                        }
                    }catch(Exception ex){
                        //e.printStackTrace();
                        RtcLogger.getLogger(RtcPlanHeader.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                        
                    }
                    if (rs == RtcSeverity.NONE || rs == RtcSeverity.INFO) {
                        checkerButton.setVisible(false);
                        iconPanel.add(new JLabel(new ImageIcon(plan.getPlanType().getIcon())));
                    } else {
                        checkerButton.setVisible(true);
                        iconPanel.add(RtcPlanItemCheckerPopupFactory.createPlanItemCheckerPopup(plan));
                        if (rs == RtcSeverity.ERROR || rs == RtcSeverity.FATAL_ERROR) {
                            checkerButton.setForeground(Color.RED);
                        }
                    }

            }
        };
        planCheckerWorker.execute();
    }
    private static HashMap<String, RtcPlanHeader> headers = new HashMap<String, RtcPlanHeader>();

    public static JComponent forPlan(RtcPlan plan) {

        //TODO : bikol : handle header unregistration
        //TODO : bikol : resolve hgeader problem
//        if (headers.get(plan.getPlanIdentifier()) == null) {
        headers.put(plan.getPlanIdentifier(), new RtcPlanHeader(plan));
//        }
        return headers.get(plan.getPlanIdentifier());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, this.getHeight(), endColor);
        g2d.setPaint(gradient);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        headerName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        areaData = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        iterationData = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jProgressBarContainer = new javax.swing.JPanel();
        checkerButton = new javax.swing.JButton();
        iconPanel = new javax.swing.JPanel();
        closed = new javax.swing.JLabel();
        opened = new javax.swing.JLabel();

        setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));
        setMaximumSize(new java.awt.Dimension(32767, 75));
        setMinimumSize(new java.awt.Dimension(700, 75));
        setName("Header"); // NOI18N
        setPreferredSize(new java.awt.Dimension(300, 75));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        headerName.setFont(new java.awt.Font("Tahoma", 1, 14));
        headerName.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.headerName.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.jLabel1.text")); // NOI18N

        areaData.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.areaData.text")); // NOI18N
        areaData.setMaximumSize(new java.awt.Dimension(80, 14));
        areaData.setMinimumSize(new java.awt.Dimension(15, 14));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.jLabel3.text")); // NOI18N

        iterationData.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.iterationData.text")); // NOI18N
        iterationData.setMaximumSize(new java.awt.Dimension(100, 14));
        iterationData.setMinimumSize(new java.awt.Dimension(5, 14));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.jLabel5.text")); // NOI18N

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.jLabel6.text")); // NOI18N

        jProgressBarContainer.setPreferredSize(new java.awt.Dimension(300, 17));
        jProgressBarContainer.setLayout(new java.awt.BorderLayout());

        checkerButton.setForeground(new java.awt.Color(128, 128, 0));
        checkerButton.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.checkerButton.text")); // NOI18N
        checkerButton.setBorder(null);
        checkerButton.setContentAreaFilled(false);
        checkerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkerButton.setFocusPainted(false);
        checkerButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        checkerButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        iconPanel.setOpaque(false);

        javax.swing.GroupLayout iconPanelLayout = new javax.swing.GroupLayout(iconPanel);
        iconPanel.setLayout(iconPanelLayout);
        iconPanelLayout.setHorizontalGroup(
            iconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );
        iconPanelLayout.setVerticalGroup(
            iconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        closed.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.closed.text")); // NOI18N

        opened.setText(org.openide.util.NbBundle.getMessage(RtcPlanHeader.class, "RtcPlanHeader.opened.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(areaData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iterationData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opened)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(jProgressBarContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(headerName, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(checkerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(headerName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBarContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(opened, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(closed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(areaData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iterationData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>                        

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (areaDatabasicSize != null && iterationDatabasicSize != null) {
            double defaultSize = jLabel1.getSize().getWidth() + areaData.getSize().getWidth() + jLabel3.getSize().getWidth() + iterationData.getSize().getWidth() + jLabel5.getSize().getWidth() + jLabel6.getSize().getWidth() + jProgressBarContainer.getSize().getWidth() + 50;
            if (evt.getComponent().getSize().getWidth() < defaultSize) {
                double newSize = defaultSize - evt.getComponent().getSize().getWidth();
                areaData.setPreferredSize(new Dimension(Math.max((int) Math.floor(areaData.getPreferredSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(areaData.getPreferredSize().getHeight()), 0)));
                areaData.setMaximumSize(new Dimension(Math.max((int) Math.floor(areaData.getMaximumSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(areaData.getMaximumSize().getHeight()), 0)));
                areaData.setMinimumSize(new Dimension(Math.max((int) Math.floor(areaData.getMinimumSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(areaData.getMinimumSize().getHeight()), 0)));
                
                iterationData.setPreferredSize(new Dimension(Math.max((int) Math.floor(iterationData.getPreferredSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(iterationData.getPreferredSize().getHeight()), 0)));
                iterationData.setMaximumSize(new Dimension(Math.max((int) Math.floor(iterationData.getMaximumSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(iterationData.getMaximumSize().getHeight()), 0)));
                iterationData.setMinimumSize(new Dimension(Math.max((int) Math.floor(iterationData.getMinimumSize().getWidth() - newSize / 2), 0), Math.max((int) Math.floor(iterationData.getMinimumSize().getHeight()), 0)));
            } else {
                double newSize = -defaultSize + evt.getComponent().getSize().getWidth();
                if (areaData.getSize().getWidth() < areaDatabasicSize.getWidth()) {

                    areaData.setPreferredSize(new Dimension(Math.max((int) Math.floor(areaData.getPreferredSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(areaData.getPreferredSize().getHeight()), 0)));
                    areaData.setMaximumSize(new Dimension(Math.max((int) Math.floor(areaData.getMaximumSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(areaData.getMaximumSize().getHeight()), 0)));
                    areaData.setMinimumSize(new Dimension(Math.max((int) Math.floor(areaData.getMinimumSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(areaData.getMinimumSize().getHeight()), 0)));               
                }
                if (iterationData.getSize().getWidth() < iterationDatabasicSize.getWidth()) {
                    iterationData.setPreferredSize(new Dimension(Math.max((int) Math.floor(iterationData.getPreferredSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(iterationData.getPreferredSize().getHeight()), 0)));
                    iterationData.setMaximumSize(new Dimension(Math.max((int) Math.floor(iterationData.getMaximumSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(iterationData.getMaximumSize().getHeight()), 0)));
                    iterationData.setMinimumSize(new Dimension(Math.max((int) Math.floor(iterationData.getMinimumSize().getWidth() + newSize / 2), 0), Math.max((int) Math.floor(iterationData.getMinimumSize().getHeight()), 0)));

                }
            }
        } else {
            iterationDatabasicSize = new Dimension((int) Math.floor(iterationData.getSize().getWidth()), 14);
            areaDatabasicSize = new Dimension((int) Math.floor(areaData.getSize().getWidth()), 14);
            //JOptionPane.showMessageDialog(null, ", "+basicSize.getWidth());
        }

    }//GEN-LAST:event_formComponentResized
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaData;
    private javax.swing.JButton checkerButton;
    private javax.swing.JLabel closed;
    private javax.swing.JLabel headerName;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JLabel iterationData;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jProgressBarContainer;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel opened;
    // End of variables declaration//GEN-END:variables

    //TODO : bikol : implement this to support toolbar update
    @Override
    public boolean isUpdateToolbarSupported() {
        return false;
    }

    @Override
    public void updateToolbar(JComponent toolbar) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void eventFired(RtcPlanItemCheckerEvent event) {
        SwingWorker checkerWorker=new SwingWorker() {
            RtcSeverity rs = RtcSeverity.NONE;
            @Override
            protected Object doInBackground() throws Exception {
                RtcPlanItemProblem pr;
                RtcPlanItemChecker rpic[]=myPlan.getPlanItemCheckers();
                if(rpic!=null){
                    for (int i = 0; i < rpic.length; i++) {
                        pr = rpic[i].getMostSevereProblem();
                        if (pr != null) {
                            if (RtcPlanItemCheckerUtilities.compareSeverities(rs, pr.getSeverity()) > 0) {
                                rs = pr.getSeverity();
                            }
                        }
                    }
                }
                if (rs == RtcSeverity.NONE || rs == RtcSeverity.INFO) {
                    checkerButton.setVisible(false);
                    iconPanel.add(new JLabel(new ImageIcon(myPlan.getPlanType().getIcon())));
                } else {
                    checkerButton.setVisible(true);
                    iconPanel.add(RtcPlanItemCheckerPopupFactory.createPlanItemCheckerPopup(myPlan));
                    if (rs == RtcSeverity.ERROR || rs == RtcSeverity.FATAL_ERROR) {
                        checkerButton.setForeground(Color.RED);
                    }
                }
                
                return null;
            }
        };
        if(event.equals(RtcPlanItemCheckerEvent.MOST_SEVERE_PROBLEM_CHANGED)||
                event.equals(RtcPlanItemCheckerEvent.NEW_PROBLEMS_FOUND)||
                event.equals(RtcPlanItemCheckerEvent.PROBLEM_RESOLVED)){
            checkerWorker.execute();
        }
    }

    private RtcPlanHeader returnThis(){
        return this;
    }

}
