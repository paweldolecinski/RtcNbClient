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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.ldap.ManageReferralControl;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcIterationGroupMode extends RtcPlanItemGrouping {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcIterationGroupMode.class, "groupmode.name.iteration");
    }

    @Override
    public RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan) {
        List<RtcPlanItemGroup> res = new ArrayList<RtcPlanItemGroup>();

        HashMap<Iteration, ArrayList<RtcPlanItem>> map = new HashMap<Iteration, ArrayList<RtcPlanItem>>();

        List<Iteration> iters = new ArrayList<Iteration>();
        ProcessManager pm = plan.getPlansManager().getActiveProjectArea().getLookup().lookup(ProcessManager.class);
        iters.add(plan.getIteration());
        iters.addAll(Arrays.asList(pm.getIterations(plan.getIteration())));
        for (Iteration i : iters) {
            map.put(i, new ArrayList<RtcPlanItem>());
        }

        for (RtcPlanItem item : planItems) {
            if (map.get(item.getPlan().getIteration()) != null) {
                map.get(item.getPlan().getIteration()).add(item);
            } else {
                ArrayList<RtcPlanItem> a = new ArrayList<RtcPlanItem>();
                a.add(item);
                map.put((Iteration) item.getPlan().getIteration(), a);
            }

        }

        Set<Entry<Iteration, ArrayList<RtcPlanItem>>> entrySet = map.entrySet();
        for (Entry<Iteration, ArrayList<RtcPlanItem>> entry : entrySet) {
            res.add(new RtcPlanItemIterationGroup(entry.getKey(), entry.getValue().toArray(new RtcPlanItem[]{})));
        }
        return res.toArray(new RtcPlanItemGroup[]{});
    }

    @Override
    public RtcBarType[] getPossibleBarTypes() {
        return new RtcBarType[]{RtcBarType.PROGRESS_BAR};
    }

    private static class RtcPlanItemIterationGroup implements RtcPlanItemGroup<Iteration> {

        private final Iteration iteration;
        private final RtcPlanItem[] planItems;
        private Lookup lookup;
        private InstanceContent ic = new InstanceContent();

        public RtcPlanItemIterationGroup(Iteration iteration, RtcPlanItem[] planItems) {
            this.iteration = iteration;
            this.planItems = planItems;
            ic.add(iteration);
            lookup = new AbstractLookup(ic);
        }

        @Override
        public String getGroupLabel() {
            return iteration.getName();
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
