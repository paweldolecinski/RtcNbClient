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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemFilterImpl extends RtcPlanItemFilter {

    private String name;
    public static RtcPlanItemFilter FILTER1 = new DummyPlanItemFilterImpl("Filter 1");
    public static RtcPlanItemFilter FILTER2 = new DummyPlanItemFilterImpl("Filter 2");
    public static RtcPlanItemFilter FILTER3 = new DummyPlanItemFilterImpl("Filter 3");
    public static RtcPlanItemFilter FILTER4 = new DummyPlanItemFilterImpl("Filter 4");

    public DummyPlanItemFilterImpl(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DummyPlanItemFilterImpl other = (DummyPlanItemFilterImpl) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.name.hashCode();
        return hash;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public boolean isFiltered(RtcPlanItem planItem) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return false;
    }

    @Override
    public RtcPlanItem[] filter(RtcPlanItem[] planItems) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return planItems;
    }
}
