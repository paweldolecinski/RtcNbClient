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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcIllegalOrderException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemSortingImpl extends RtcPlanItemSorting {

    private String name;
    public static RtcPlanItemSorting SORTING1 = new DummyPlanItemSortingImpl("Sorting 1");
    public static RtcPlanItemSorting SORTING2 = new DummyPlanItemSortingImpl("Sorting 2");
    public static RtcPlanItemSorting SORTING3 = new DummyPlanItemSortingImpl("Sorting 3");
    public static RtcPlanItemSorting SORTING4 = new DummyPlanItemSortingImpl("Sorting 4");

    public DummyPlanItemSortingImpl(String name) {
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
        final DummyPlanItemSortingImpl other = (DummyPlanItemSortingImpl) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.name.hashCode();
        return hash;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void insertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertBefore(RtcPlanItem item, RtcPlanItem before) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertAfter(RtcPlanItem item, RtcPlanItem after) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertBefore(RtcPlanItem item, RtcPlanItem before) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertAfter(RtcPlanItem item, RtcPlanItem after) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compare(RtcPlanItem o1, RtcPlanItem o2) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
