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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewStyle;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy.DummyPlanItemFilterImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy.DummyPlanItemGroupingImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy.DummyPlanItemSortingImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanItemViewModeImplTest {

    private RtcPlanItemViewModeImpl instance;

    public RtcPlanItemViewModeImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new RtcPlanItemViewModeImplImpl();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of getFilters method, of class RtcPlanItemViewModeImpl.
     */
    @Ignore
    @Test
    public void testGetFilters() {
        RtcPlanItemFilter[] expResult = null;
        RtcPlanItemFilter[] result = instance.getFilters();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGrouping method, of class RtcPlanItemViewModeImpl.
     */
    @Ignore
    @Test
    public void testGetGrouping() {
        RtcPlanItemGrouping expResult = null;
        RtcPlanItemGrouping result = instance.getGrouping();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSorting method, of class RtcPlanItemViewModeImpl.
     */
    @Ignore
    @Test
    public void testGetSorting() {
        RtcPlanItemSorting expResult = null;
        RtcPlanItemSorting result = instance.getSorting();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBarType method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testGetBarType() {
        RtcBarType expResult = RtcBarType.LOAD_BAR;
        RtcBarType result = instance.getBarType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getViewStyle method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testGetViewStyle() {
        RtcPlanItemViewStyle expResult = RtcPlanItemViewStyle.TREE;
        RtcPlanItemViewStyle result = instance.getViewStyle();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEditable method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testIsEditable() {
        boolean expResult = true;
        boolean result = instance.isEditable();
        assertEquals(expResult, result);
    }

    /**
     * Test of isModified method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testIsModified() {
        boolean expResult = false;
        boolean result = instance.isModified();
        assertEquals(expResult, result);

        instance.setDisplayName("dirty name");
        expResult = true;
        result = instance.isModified();
        assertEquals(expResult, result);
    }

    /**
     * Test of removeColumns method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testRemoveColumns() {
        instance.removeColumns();
        assertEquals(0, instance.getColumns().length);
    }

    /**
     * Test of addFilter method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testAddFilter() {
        RtcPlanItemFilter filter = new DummyPlanItemFilterImpl("dummy filter");

        int sizeBefore = instance.getFilters().length;
        instance.addFilter(filter);
        assertEquals(sizeBefore + 1, instance.getFilters().length);
    }

    /**
     * Test of removeFilters method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testRemoveFilters() {

        instance.removeFilters();
        assertEquals(0, instance.getFilters().length);
    }

    /**
     * Test of setGrouping method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testSetGrouping() {
        RtcPlanItemGrouping grouping = new DummyPlanItemGroupingImpl("dummy grouping");

        instance.setGrouping(grouping);
        assertEquals(grouping, instance.getGrouping());
    }

    /**
     * Test of setSorting method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testSetSorting() {
        RtcPlanItemSorting sorting = new DummyPlanItemSortingImpl("dummy sorting");

        instance.setSorting(sorting);
        assertEquals(sorting, instance.getSorting());
    }

    /**
     * Test of setBarType method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testSetBarType() {
        RtcBarType barType = RtcBarType.NONE;

        instance.setBarType(barType);
        assertEquals(barType, instance.getBarType());
    }

    /**
     * Test of setViewStyle method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testSetViewStyle() {
        RtcPlanItemViewStyle viewStyle = RtcPlanItemViewStyle.FLAT;

        instance.setViewStyle(viewStyle);
        assertEquals(viewStyle, instance.getViewStyle());
    }

    /**
     * Test of setDisplayName method, of class RtcPlanItemViewModeImpl.
     */
    @Test
    public void testSetDisplayName() {
        String name = "testName";

        instance.setDisplayName(name);
        assertEquals(name, instance.getDisplayName());
    }

    public class RtcPlanItemViewModeImplImpl extends RtcPlanItemViewModeImpl {

        public RtcPlanItemViewModeImplImpl() {
            name = "Ranked List";
            columns = new ArrayList<RtcPlanItemAttribute>();
            filters = new ArrayList<RtcPlanItemFilter>();
        }

        @Override
        public String getId() {
            return "com.ibm.team.apt.viewmodes.internal.backlog2";
        }

        @Override
        public String getDisplayName() {
            return name;
        }

        @Override
        public RtcPlanItemAttribute[] getColumns() {
            return new RtcPlanItemAttribute[]{};
        }

        @Override
        public RtcPlanItemViewModeManager getManager() {
            return null;
        }

        @Override
        public void save() throws RtcSaveException {
        }

        @Override
        public void discardChanges() {
        }
    }
}
