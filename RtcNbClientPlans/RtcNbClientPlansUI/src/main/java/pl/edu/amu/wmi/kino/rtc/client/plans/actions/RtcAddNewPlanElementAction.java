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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcAddNewPlanElementAction extends AbstractAction implements Presenter.Popup, PopupMenuListener, ContextAwareAction, HelpCtx.Provider {

    private static final long serialVersionUID = 23534645775675634L;
    private static final String WIT_PROP = "RtcAddNewPlanElementAction.WorkItem_Type";
    private JMenu menu;
    private Lookup lookup;

    /**
     *
     */
    public RtcAddNewPlanElementAction() {
        putValue(NAME, NbBundle.getMessage(RtcAddNewPlanElementAction.class, "CTL_NewWorkItemAction"));
    }

    private RtcAddNewPlanElementAction(Lookup actionContext) {
        putValue(NAME, NbBundle.getMessage(RtcAddNewPlanElementAction.class, "CTL_NewWorkItemAction"));
        this.lookup = actionContext;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        JMenuItem source = (JMenuItem) evt.getSource();
        final RtcWorkItemType type = (RtcWorkItemType) source.getClientProperty(WIT_PROP);
        if (type != null) {

            SwingWorker<RtcPlanWorkItem, Void> planWorker = new SwingWorker<RtcPlanWorkItem, Void>() {

                @Override
                protected RtcPlanWorkItem doInBackground() {
                    RtcPlan plan = lookup.lookup(RtcPlan.class);

                    if (plan != null) {
                        RtcPlanItemsManager planItemsManager = plan.getPlanItemsManager();
                        RtcPlansManager plansManager = plan.getPlansManager();

                        RtcWorkItem wi = plansManager.getActiveProjectArea().getLookup().lookup(RtcWorkItemsManager.class).createWorkItem(plan.getOwner(), type);
                        //System.out.println("New Work itemmmmmmmmmmmmmm: "+wi.getType().getDisplayName() + " - "+wi.getOwner().getName());
                        //wi.setValue(null, null); //attribute from group mode
                        return planItemsManager.addNewWorkItem(wi);
                    }
                    return null;
                }
            };
            planWorker.execute();
        }
    }

    @Override
    public JMenuItem getPopupPresenter() {
        if (menu == null) {
            menu = new UpdatingMenu(this);
            menu.getPopupMenu().addPopupMenuListener(this);
        }
        return menu;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        fillSubMenu();
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        menu.removeAll();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    private void fillSubMenu() {
        RtcPlan plan = lookup.lookup(RtcPlan.class);

        if (plan != null) {
            RtcPlanItemAttributePossibleValues values = plan.getPlansManager().getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.type").getLookup().lookup(RtcPlanItemAttributePossibleValues.class);

            for (Object v : values.getPossibleValues()) {
                if (v instanceof RtcWorkItemType) {
                    RtcWorkItemType type = (RtcWorkItemType) v;
                    JMenuItem jmi = null;

                    jmi = new JMenuItem(type.getDisplayName());

                    jmi.putClientProperty(WIT_PROP, type);
                    jmi.addActionListener(this);
                    menu.add(jmi);
                }
            }
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        RtcAddNewPlanElementAction action = new RtcAddNewPlanElementAction(actionContext);
        return action;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    private static class UpdatingMenu extends JMenu implements DynamicMenuContent {

		private static final long serialVersionUID = 8250523138559311423L;
		private final JComponent[] content = new JComponent[]{this};

        public UpdatingMenu(Action action) {
            super(action);
        }

        public JComponent[] getMenuPresenters() {
            setEnabled(true);
            return content;
        }

        public JComponent[] synchMenuPresenters(JComponent[] items) {
            return getMenuPresenters();
        }
    }
}
