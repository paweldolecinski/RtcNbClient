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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.filters;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;
import static junit.framework.Assert.*;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcTopLevelTypesFilterTest {

    private static RtcTopLevelTypesFilter filter;
    private static RtcPlanItem[] planItems;

    /**
     *
     */
    public RtcTopLevelTypesFilterTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        filter = new RtcTopLevelTypesFilter();
        planItems = new RtcPlanItem[]{new RtcPlanItemMockup(true), new RtcPlanItemMockup(false),
                    new RtcPlanItemMockup(true), new RtcPlanItemMockup(false), };
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        filter = null;
        planItems = null;
    }

    /**
     * Test of isFiltered method, of class RtcTopLevelTypesFilter.
     */
    @Test
    public void testIsFiltered() {
        boolean expected = true;
        RtcPlanItemMockup rtcPlanItemMockup = new RtcPlanItemMockup(true);
        boolean filtered = filter.isFiltered(rtcPlanItemMockup);
        assertEquals(expected, filtered);
    }

    /**
     * Test of filter method, of class RtcTopLevelTypesFilter.
     */
    @Test
    public void testFilter() {
        RtcPlanItem[] filtered = filter.filter(planItems);
        assertEquals(2, filtered.length);
    }

    private static class RtcPlanItemMockup extends RtcPlanWorkItem {

        private final boolean exec;

        public RtcPlanItemMockup(boolean exec) {
            this.exec = exec;
        }

        @Override
        public RtcPlanItemType getPlanItemType() {
            if (exec == true) {
                return RtcPlanItemType.EXECUTABLE;
            } else {
                return RtcPlanItemType.NON_EXECUTABLE;
            }
        }

        @Override
        public RtcPlanItem[] getChildItems() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RtcPlanItem getParentItem() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Contributor getOwner() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getPlanItemIdentifier() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T getPlanAttributeValue(RtcPlanItemAttribute<T> attribute) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value) throws RtcIllegalPlanAttributeValue {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Lookup getLookup() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RtcPlan getPlan() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RtcWorkItem getWorkItem() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isResolved() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addListener(EventListener<RtcPlanItemEvent> listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeListener(EventListener<RtcPlanItemEvent> listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
