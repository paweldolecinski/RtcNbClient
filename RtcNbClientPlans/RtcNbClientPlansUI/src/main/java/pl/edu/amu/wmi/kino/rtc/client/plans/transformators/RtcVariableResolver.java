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
package pl.edu.amu.wmi.kino.rtc.client.plans.transformators;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.IItemReferenceDetector.Reference;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;

/**
 *
 * @author michu
 */
public class RtcVariableResolver implements IVariableResolver {

    private RtcPlan plan;
    //managers
    private RtcWorkItemsManager workItemsManager;
    private RtcPlansManager plansManager;
    private RtcPlanItemsManager planItemManager;
    //plan page - needed for plan page attachments
    private RtcPlanPage planPage;

    public RtcVariableResolver(RtcPlan plan, RtcPlanPage planPage) {
        this.plan = plan;
        this.planPage = planPage;

        this.plansManager = plan.getPlansManager();
        this.workItemsManager =
                this.plansManager.getActiveProjectArea().getLookup().lookup(RtcWorkItemsManager.class);
    }

    @Override
    public String revolve(Reference context, String variable) {

        /*
        RtcWorkItem workItem = workItemsManager.findWorkItem(Integer.parseInt(variable));
        if(workItem != null) {
            return workItem.getDisplayName();
        }

        RtcPlan plan = plansManager.findPlan(variable);
        if(plan != null) {
            return plan.getName();
        */
   
        return null;
    }
}


