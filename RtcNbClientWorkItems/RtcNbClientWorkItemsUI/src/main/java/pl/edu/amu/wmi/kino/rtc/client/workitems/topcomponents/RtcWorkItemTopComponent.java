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
package pl.edu.amu.wmi.kino.rtc.client.workitems.topcomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.WorkItemNode;
import pl.edu.amu.wmi.kino.rtc.client.workitems.WorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcWorkItemApprovalsLayout;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcWorkItemHeaderLayout;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcWorkItemHistoryLayout;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcWorkItemLinksLayout;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcWorkItemOverviewLayout;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 * Top component which displays work item.
 */
public class RtcWorkItemTopComponent extends TopComponent {

    private String UNIQUE_ID = "RtcWorkItemTopComponent";
    private RtcWorkItem workItem;
    private RtcWorkItemAttributeSet set;
    private Lookup context;
    private String niceName;
    WorkItemNode node;

    public RtcWorkItemTopComponent(Lookup context) {
        initComponents();
        this.context = context;

        SetBasicTopComponentValues();
        CreateTabsFromAttributeSet();
        node = this.context.lookup(WorkItemNode.class);
        if (node == null) {
            throw new NullPointerException(NbBundle.getMessage(RtcWorkItemTopComponent.class, "WorkItemNode.exp"));
        }
        setActivatedNodes(new Node[]{node});
        setFocusable(true);
    }

    private void CreateTabsFromAttributeSet() {
        set = (RtcWorkItemAttributeSet) context.lookup(RtcWorkItemAttributeSet.class);

        for (final RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory cat : set) {

            JPanel panel = null;

            if (cat.getLayout().equals(RtcWorkItemAttributeSet.CategoryLayout.HEADER)) {
                // internalHeader always has only one section
                panel = new RtcWorkItemHeaderLayout(this, cat.get(0), workItem, context);
                jHeaderPanel.add(panel);
            } else if (cat.getLayout().equals(RtcWorkItemAttributeSet.CategoryLayout.OVERVIEW)) {
                panel = new RtcWorkItemOverviewLayout(cat);
                jTabbedPane.addTab(cat.getDisplayName(), panel);
            } else {

                if (cat.getLayout().equals(RtcWorkItemAttributeSet.CategoryLayout.APPROVALS)) {
                    jTabbedPane.addTab(cat.getDisplayName(), new RtcWorkItemApprovalsLayout(cat.get(0)));
                } else if (cat.getLayout().equals(RtcWorkItemAttributeSet.CategoryLayout.LINKS)) {
                    jTabbedPane.addTab(cat.getDisplayName(), new RtcWorkItemLinksLayout(cat));
                } else if (cat.getLayout().equals(RtcWorkItemAttributeSet.CategoryLayout.HISTORY)) {
                    jTabbedPane.addTab(cat.getDisplayName(), new RtcWorkItemHistoryLayout(cat.get(0)));
                }
            }
            if (panel != null) {
                //panel.setFocusable(true);
            }
        }
    }

    private void SetBasicTopComponentValues() {
        workItem = context.lookup(RtcWorkItem.class);
        UNIQUE_ID += Integer.toString(workItem.getId());

        niceName = getNiceWorkItemName();

        setName(niceName);
        setToolTipText(niceName);
    }

    private String getNiceWorkItemName() {

        if (niceName != null) {
            return niceName;
        }

        workItem = context.lookup(RtcWorkItem.class);
        String fullText = workItem.getDisplayName();
        String shortName =
                Integer.toString(workItem.getId())
                + ": "
                + (fullText.length() > 30 ? fullText.substring(0, 30) + "..." : fullText);
        return shortName;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jHeaderPanel = new javax.swing.JPanel();

        setBackground(java.awt.Color.white);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jTabbedPane.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane.setAlignmentX(0.0F);
        jTabbedPane.setAlignmentY(0.0F);
        add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        jHeaderPanel.setAlignmentY(0.0F);
        jHeaderPanel.setMinimumSize(new java.awt.Dimension(10, 20));
        jHeaderPanel.setLayout(new java.awt.BorderLayout());
        add(jHeaderPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jHeaderPanel;
    private javax.swing.JTabbedPane jTabbedPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public boolean canClose() {

        if (node.isModified()) {
            Confirmation message = new NotifyDescriptor.Confirmation(
                    NbBundle.getMessage(RtcWorkItemTopComponent.class, "RtcWorkItemTopComponent.unsaved.changed.text", getNiceWorkItemName()),
                    NotifyDescriptor.YES_NO_CANCEL_OPTION,
                    NotifyDescriptor.QUESTION_MESSAGE);

            Object result = DialogDisplayer.getDefault().notify(message);

            if (NotifyDescriptor.YES_OPTION.equals(result)) {
                SaveCookie cookie = context.lookup(SaveCookie.class);
                try {
                    cookie.save();
                } catch (IOException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcWorkItemTopComponent.class)
                            .log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
                workItemSaved();
                return true;
            } else if (NotifyDescriptor.NO_OPTION.equals(result)) {
                    node.dropChanges();
                return true;
            } else {
                return false;
            }

        } else {
            return true;
        }
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        Lookup.getDefault().lookup(WorkItemsManager.class).remove(Integer.toString(workItem.getId()));
        fireEvent(TopComponentEvents.COMPONENT_CLOSED);
    }

    @Override
    protected String preferredID() {
        return UNIQUE_ID;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcWorkItemTopComponent.class);
    }

    public void workItemModified() {
        /*EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {*/
                setHtmlDisplayName("<html><b>" + getNiceWorkItemName() + "</html></b>");
            /*}
        });*/
        
    }

    public void workItemSaved() {
        /*EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {*/
                setHtmlDisplayName("<html>" + getNiceWorkItemName() + "</b>");
            /*}
        });*/
        
    }
    private List<TopComponentListener> listeners = new ArrayList<TopComponentListener>();

    private synchronized void fireEvent(TopComponentEvents eventType) {
        for (Iterator<TopComponentListener> it = listeners.iterator(); it.hasNext();) {
            it.next().eventFired(eventType);
        }
    }

    public synchronized void addListener(TopComponentListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(TopComponentListener listener) {
        listeners.remove(listener);
    }

    public static interface TopComponentListener {

        public void eventFired(TopComponentEvents eventType);
    }

    public static enum TopComponentEvents {

        COMPONENT_CLOSED
    }
}
