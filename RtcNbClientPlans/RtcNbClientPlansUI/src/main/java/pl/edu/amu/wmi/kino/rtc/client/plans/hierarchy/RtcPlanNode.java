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
package pl.edu.amu.wmi.kino.rtc.client.plans.hierarchy;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan.RtcPlanEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanNode extends AbstractNode
        implements EventListener<RtcPlanEvent> {

    private Action[] actions;
    private RtcPlan plan;
    private InstanceContent ic;

    public RtcPlanNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        plan = getLookup().lookup(RtcPlan.class);
        this.ic = ic;
        ic.add(new PlanOpennable(
                getLookup().lookup(RtcPlan.class)));

        plan.addListener(this);
    }

    @Override
    public Image getOpenedIcon(int type) {
        ////System.out.println("get opened");
        if (plan.getPlanType().getIcon() == null) {
            ////System.out.println("null");
        }
        return plan.getPlanType().getIcon();
    }

    @Override
    public Image getIcon(int type) {
        if (plan.getPlanType().getIcon() == null) {
            ////System.out.println("null");
        }
        return plan.getPlanType().getIcon();
    }

    @Override
    public String getDisplayName() {
        return plan.getName() + " [" + plan.getIteration().getName() + "]";
    }

    private void setDisplayName() {
        setDisplayName(plan.getName() + " [" + plan.getIteration().getName() + "]");
    }

    @Override
    public Action getPreferredAction() {
        return getActions(true)[0];
    }

    @Override
    public Action[] getActions(boolean bln) {
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/PlansModule/PlanActions");
            actions = list.toArray(new Action[]{});
        }
        return actions;
    }

    @Override
    public String getName() {
        return plan.getName();
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void setName(String s) {
        plan.setName(s);
//        setDisplayName();
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        //TODO: implement node destroy and plan remove mechanism
        RtcPlansManager manager = getLookup().lookup(RtcPlansManager.class);
        manager.removePlan(plan);
        super.destroy();
    }

    private void exposeSaveCookie() {
        if (getLookup().lookup(SaveCookie.class) == null) {
            ic.add(new PlanSaveCookie(plan, ic));
        }
    }

    @Override
    public void eventFired(RtcPlanEvent event) {
        switch (event) {
            case NAME_CHANGED:
            case PLAN_SYNCHRONIZED_WITH_SERVER:
                setDisplayName();
                break;
        }
        exposeSaveCookie();
    }

    public static class PlanSaveCookie implements SaveCookie {

        private RtcPlan plan;
        private InstanceContent ic;

        public PlanSaveCookie(RtcPlan workItem, InstanceContent ic) {
            this.plan = workItem;
            this.ic = ic;
        }

        @Override
        public void save() {
            try {
                plan.save();
                ic.remove(this);
            } catch (RtcPlanSaveException ex) {
                RtcLogger.getLogger(RtcPlanNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcPlanNode.class);
    }
}
