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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcOwnerGroupMode extends RtcPlanItemGrouping {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcOwnerGroupMode.class, "groupmode.name.owner");
    }

    @Override
    public RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan) {
        List<RtcPlanItemGroup> res = new ArrayList<RtcPlanItemGroup>();
        ProcessManager pm = plan.getPlansManager().getActiveProjectArea().getLookup().lookup(ProcessManager.class);
        Contributor[] members = pm.getMembers(plan.getOwner());

        HashMap<Contributor, ArrayList<RtcPlanItem>> map = new HashMap<Contributor, ArrayList<RtcPlanItem>>();
        for (Contributor rtcContributor : members) {
            map.put(rtcContributor, new ArrayList<RtcPlanItem>());
        }
        
        for (RtcPlanItem item : planItems) {
            if (map.get(item.getOwner()) != null) {
                map.get(item.getOwner()).add(item);
            } else {
                ArrayList<RtcPlanItem> a = new ArrayList<RtcPlanItem>();
                a.add(item);
                map.put(item.getOwner(), a);
            }

        }

        Set<Entry<Contributor, ArrayList<RtcPlanItem>>> entrySet = map.entrySet();
        for (Entry<Contributor, ArrayList<RtcPlanItem>> entry : entrySet) {
            res.add(new RtcPlanItemOwnerGroup(entry.getKey(), entry.getValue().toArray(new RtcPlanItem[]{})));
        }
        return res.toArray(new RtcPlanItemGroup[]{});
    }

    @Override
    public RtcBarType[] getPossibleBarTypes() {
        return new RtcBarType[]{RtcBarType.PROGRESS_BAR, RtcBarType.LOAD_BAR};
    }

    private static class RtcPlanItemOwnerGroup implements RtcPlanItemGroup<Contributor> {

        private final Contributor owner;
        private final RtcPlanItem[] planItems;
        private Lookup lookup;
        private InstanceContent ic = new InstanceContent();

        public RtcPlanItemOwnerGroup(Contributor owner, RtcPlanItem[] planItems) {
            this.owner = owner;
            this.planItems = planItems;
            if (planItems.length > 0 && planItems[0].getPlan() != null) {
                RtcPlansManager plansManager = planItems[0].getPlan().getPlansManager();
                RtcProgressInfo progressInfo = plansManager.getProgressInfo(owner, planItems, planItems[0].getPlan().getComplexityComputator());
                ic.add(planItems[0].getPlan().getComplexityComputator());
                ic.add(progressInfo);
                RtcLoadInfo loadInfo = plansManager.getLoadInfo(owner, planItems[0].getPlan().getIteration());
                ic.add(loadInfo);
            }


            ic.add(owner);
            lookup = new AbstractLookup(ic);
        }

        @Override
        public String getGroupLabel() {
            return owner.getName();
        }

        @Override
        public Lookup getLookup() {
            return lookup;
        }

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        @Override
        public RtcPlanItemGroup[] getChildGroups() {
            return new RtcPlanItemGroup[]{};
        }

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        @Override
        public RtcPlanItem[] getPlanItems() {
            return planItems;
        }
    }
}
