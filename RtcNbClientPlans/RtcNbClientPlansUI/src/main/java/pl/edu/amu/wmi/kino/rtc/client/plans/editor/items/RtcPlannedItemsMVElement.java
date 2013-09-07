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
import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.ProxyLookup;

import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomView2;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager.RtcPlanItemsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.RtcPlannedItemsPageRightPanelSwitchAction;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.cookies.RtcTwoStateSwitchCookie;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.RtcPlanItemsRootNodeFactory;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.viewmode.RtcViewModeEditor;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.viewmode.RtcViewModeEditorCallback;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlannedItemsMVElement extends KinoMultiViewElement{

    private ExplorerManager manager;
    private RtcPlan plan;
    private RtcItemsJPanel mainPanel;
    private RtcItemsPageRightPanel rigthPanel;
    private boolean rightPanelAdded;
    private JToolBar toolbar;
    private JPanel viewModeEditor;
    private JPanel centerPanel;
    private RtcPlanItemViewModeManager viewModeManager;
    private Lookup proxyLookup;

    public RtcPlannedItemsMVElement(RtcPlan plan) {
        this.plan = plan;
        ic.add(plan);
        manager = new ExplorerManager();
//        ic.add(manager);

        viewModeManager = plan.getPlansManager().getActiveProjectArea().getLookup().
                lookup(RtcPlanItemViewModeManager.class);
        mainPanel = new RtcItemsJPanel(plan, manager);
        mainPanel.setLookup(getLookup());

    }

    @Override
    public Lookup getLookup() {
        if (proxyLookup == null) {
            proxyLookup = new ProxyLookup(super.getLookup(), ExplorerUtils.createLookup(manager, mainPanel.getActionMap()));
        }
        return proxyLookup;
    }

    @Override
    public JComponent createInnerComponent() {


        mainPanel.setLayout(new BorderLayout());

        rigthPanel = new RtcItemsPageRightPanel(plan, ic);


        rigthPanel.setLookup(getLookup());

        CustomView2 view = new CustomView2();
//        view.setRootVisible(false);

        centerPanel = new JPanel(new BorderLayout());
        //TODO : in future Sensitible LAbel will be unnecessary then uncoment this line and coment rest of them
//        centerPanel.add(view,BorderLayout.CENTER);
        //to coment
        JPanel superLabelHack = new JPanel(new BorderLayout());
        superLabelHack.add(view, BorderLayout.CENTER);
        //create view mode sensitible JLabel that will show name of currently selected view mode
        superLabelHack.add(new ViewModeSensitibleLabel(manager, view), BorderLayout.NORTH);
        centerPanel.add(superLabelHack, BorderLayout.CENTER);
        //end of future comment


        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(rigthPanel, BorderLayout.EAST);
        rightPanelAdded = true;

        ic.add(new RtcViewModeEditorCallback() {

            private final Object lock = new Object();
            private boolean openned = false;

            @Override
            public void openEditor() {
                synchronized (lock) {
                    if (viewModeEditor == null) {
                        viewModeEditor = new RtcViewModeEditor(this,getLookup());
                    }
                    if (!openned) {
                        centerPanel.add(viewModeEditor, BorderLayout.NORTH);
                        centerPanel.validate();
                        openned = true;
                    }
                }
            }

            @Override
            public void closeEditor() {
                synchronized (lock) {
                    if (viewModeEditor != null && openned) {
                        centerPanel.remove(viewModeEditor);
                        centerPanel.validate();
                        openned = false;
                    }
                }
            }

            @Override
            public boolean isOpenned() {
                synchronized (lock) {
                    return openned;
                }
            }
        });
        ic.add(new RtcTwoStateSwitchCookie() {

            @Override
            public boolean isEnabled() {
                return rightPanelAdded;
            }

            @Override
            public void setEnabled(boolean value) {
                if (value && !rightPanelAdded) {
                    mainPanel.add(rigthPanel, BorderLayout.EAST);
                    mainPanel.validate();
                    rightPanelAdded = true;
                } else {
                    if (!value && rightPanelAdded) {
                        mainPanel.remove(rigthPanel);
                        mainPanel.validate();
                        rightPanelAdded = false;
                    }
                }
            }
        });
        return mainPanel;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        if (toolbar == null) {
            toolbar = new JToolBar();
            toolbar.add(SystemAction.get(RtcPlannedItemsPageRightPanelSwitchAction.class).getToolbarPresenter());
        }
        return toolbar;
    }
}

class RtcItemsJPanel extends JPanel implements ExplorerManager.Provider,EventListener<RtcPlanItemsManagerEvent> {

    private static final long serialVersionUID = 123456765432L;
    private ExplorerManager manager;
    private Result<RtcPlanItemViewMode> result;
    private RtcPlanItemViewModeListener listener;
    private RtcPlan plan;
    public RtcItemsJPanel(RtcPlan plan, ExplorerManager manager) {
        this.manager = manager;
        this.plan=plan;
        listener = new RtcPlanItemViewModeListener(RtcPlanItemsRootNodeFactory.createPlanItemsRootNode(plan), manager);

    }
    //TODO : bikol : hack to refresh customView after adding new plan Item
        @Override
    public void eventFired(RtcPlanItemsManagerEvent event) {
           // JOptionPane.showMessageDialog(null, "custom View powinno sie odswierzyc");
//            result.removeLookupListener(listener);
            Node n = RtcPlanItemsRootNodeFactory.createPlanItemsRootNode(plan);
            listener.updateNode(n);
//            manager.setRootContext(listener.createRootNode(n,result.allInstances().iterator().next()));
//        listener = new RtcPlanItemViewModeListener(n, manager);
//        listener.eventFired(RtcPlanItemViewMode.RtcPlanItemViewModeEvent.FILTER_ADDED);
//        result.addLookupListener(listener);

    }

    //TODO : bikol : hack, it should be fixed in other way

    public void setLookup(Lookup lookup) {
        result = lookup.lookupResult(RtcPlanItemViewMode.class);
        result.allInstances();
    }

    @Override
    public void addNotify() {
        result.addLookupListener(listener);
        plan.getPlanItemsManager().addListener(this);
        super.addNotify();
    }

    @Override
    public void removeNotify() {
        result.removeLookupListener(listener);
        plan.getPlanItemsManager().removeListener(this);
        super.removeNotify();
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}

class ViewModeSensitibleLabel extends JLabel implements LookupListener, Runnable {

    private static final long serialVersionUID = 3242342351L;
    private Result<RtcPlanItemViewMode> result;
    private ExplorerManager manager;
    private CustomView2 view;

    ViewModeSensitibleLabel(ExplorerManager manager, CustomView2 view) {
        super();
        result = Utilities.actionsGlobalContext().lookupResult(RtcPlanItemViewMode.class);
        this.manager = manager;
        this.view = view;
        result.addLookupListener(this);
        run();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        EventQueue.invokeLater(this);

    }

    @Override
    public void run() {

        for (RtcPlanItemViewMode m : result.allInstances()) {
            setText(m.getDisplayName());
        }

    }
}
