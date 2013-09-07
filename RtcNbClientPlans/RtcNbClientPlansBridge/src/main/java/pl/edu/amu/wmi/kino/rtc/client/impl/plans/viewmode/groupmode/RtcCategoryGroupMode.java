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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcCategoryGroupMode extends RtcPlanItemGrouping {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcCategoryGroupMode.class, "groupmode.name.category");
    }

    @Override
    public RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan) {
        List<RtcPlanItemGroup> res = new ArrayList<RtcPlanItemGroup>();
        RtcPlansManager plansManager = plan.getPlansManager();
        RtcPlanItemAttribute planItemAttribute = plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.filedAgainst");
        HashMap<RtcCategory, ArrayList<RtcPlanItem>> map = new HashMap<RtcCategory, ArrayList<RtcPlanItem>>();
        List<RtcCategory> possibleValues = planItemAttribute.getLookup().lookup(RtcPlanItemAttributePossibleValues.class).getPossibleValues();
        for (RtcCategory rtcContributor : possibleValues) {
            map.put(rtcContributor, new ArrayList<RtcPlanItem>());
        }

        for (RtcPlanItem item : planItems) {
            if (map.get(item.getPlanAttributeValue(planItemAttribute)) != null) {
                map.get(item.getPlanAttributeValue(planItemAttribute)).add(item);
            } else {
                ArrayList<RtcPlanItem> a = new ArrayList<RtcPlanItem>();
                a.add(item);
                map.put((RtcCategory) item.getPlanAttributeValue(planItemAttribute), a);
            }

        }

        Set<Entry<RtcCategory, ArrayList<RtcPlanItem>>> entrySet = map.entrySet();
        for (Entry<RtcCategory, ArrayList<RtcPlanItem>> entry : entrySet) {
            res.add(new RtcPlanItemCategoryGroup(entry.getKey(), entry.getValue().toArray(new RtcPlanItem[]{})));
        }
        return res.toArray(new RtcPlanItemGroup[]{});
    }

    @Override
    public RtcBarType[] getPossibleBarTypes() {
        return new RtcBarType[]{RtcBarType.PROGRESS_BAR};
    }

    private static class RtcPlanItemCategoryGroup implements RtcPlanItemGroup<RtcCategory> {

        private final RtcCategory category;
        private final RtcPlanItem[] planItems;
        private Lookup lookup;
        private InstanceContent ic = new InstanceContent();

        public RtcPlanItemCategoryGroup(RtcCategory category, RtcPlanItem[] planItems) {
            this.category = category;
            this.planItems = planItems;
            ic.add(this.category);
            lookup = new AbstractLookup(ic);
        }


        @Override
        public String getGroupLabel() {
            return category.getName();
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
