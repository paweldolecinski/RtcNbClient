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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.types;

import org.junit.Test;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import static org.junit.Assert.*;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanTypesTest {

    /**
     *
     */
    public RtcPlanTypesTest() {
    }

    /**
     * Test of getType method, of class RtcPlanTypes.
     */
    @Test
    public void getType_iteration() {
        RtcPlanType type = RtcPlanTypes.getType(RtcPlanTypes.ITERATION_PLAN_ID);
        assertEquals(type.getId(), "com.ibm.team.apt.plantype.default");
        RtcPlanType type2 = RtcPlanTypes.getType(RtcPlanTypes.ITERATION_PLAN_ID);
        assertEquals(type2, type);
    }

    /**
     * Test of getType method, of class RtcPlanTypes.
     */
    @Test
    public void getType_backlog() {
        RtcPlanType type = RtcPlanTypes.getType(RtcPlanTypes.PRODUCT_BACKLOG_PLAN_ID);
        assertEquals(type.getId(), "com.ibm.team.apt.plantype.product.backlog");
        RtcPlanType type2 = RtcPlanTypes.getType(RtcPlanTypes.PRODUCT_BACKLOG_PLAN_ID);
    }

    /**
     * Test of getType method, of class RtcPlanTypes.
     */
    @Test
    public void getType_release1() {
        RtcPlanType type = RtcPlanTypes.getType(RtcPlanTypes.PROJECT_RELEASE_PLAN_ID);
        assertEquals(type.getId(), "com.ibm.team.apt.plantype.release.project");
        RtcPlanType type2 = RtcPlanTypes.getType(RtcPlanTypes.PROJECT_RELEASE_PLAN_ID);
        assertEquals(type2, type);
    }

    /**
     * Test of getType method, of class RtcPlanTypes.
     */
    @Test
    public void getType_release2() {
        RtcPlanType type = RtcPlanTypes.getType(RtcPlanTypes.TEAM_RELEASE_PLAN_ID);
        assertEquals(type.getId(), "com.ibm.team.apt.plantype.release");
        RtcPlanType type2 = RtcPlanTypes.getType(RtcPlanTypes.TEAM_RELEASE_PLAN_ID);
        assertEquals(type2, type);
    }
}
