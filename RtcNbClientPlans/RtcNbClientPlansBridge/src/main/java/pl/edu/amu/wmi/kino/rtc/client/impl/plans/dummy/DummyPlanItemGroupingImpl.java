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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemGroupingImpl extends RtcPlanItemGrouping {

    private String name;

    public static RtcPlanItemGrouping GROUPING1 = new DummyPlanItemGroupingImpl("Grouping 1");
    public static RtcPlanItemGrouping GROUPING2 = new DummyPlanItemGroupingImpl("Grouping 2");
    public static RtcPlanItemGrouping GROUPING3 = new DummyPlanItemGroupingImpl("Grouping 3");
    public static RtcPlanItemGrouping GROUPING4 = new DummyPlanItemGroupingImpl("Grouping 4");

    public DummyPlanItemGroupingImpl(String string) {
        this.name = string;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DummyPlanItemGroupingImpl other = (DummyPlanItemGroupingImpl) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.name.hashCode();
        return hash;
    }

    @Override
    public RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return new RtcPlanItemGroup[]{new DummyPlanItemGroup(planItems)};
    }

    @Override
    public RtcBarType[] getPossibleBarTypes() {
        return RtcPlanItemViewMode.RtcBarType.values();
    }
}

class DummyPlanItemGroup implements RtcPlanItemGroup {

    private RtcPlanItem[] items;

    public DummyPlanItemGroup(RtcPlanItem[] items) {
        this.items = items;
    }

    @Override
    public String getGroupLabel() {
        return "group1";
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public RtcPlanItemGroup[] getChildGroups() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return new RtcPlanItemGroup[]{};
    }

    @Override
    public RtcPlanItem[] getPlanItems() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return items;
    }
}
