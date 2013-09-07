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

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanTypes {

    /**
     *
     */
    public static final String TEAM_RELEASE_PLAN_ID = "com.ibm.team.apt.plantype.release"; //$NON-L18N
    /**
     *
     */
    public static final String PROJECT_RELEASE_PLAN_ID = "com.ibm.team.apt.plantype.release.project"; //$NON-NON-L18N
    /**
     *
     */
    public static final String PRODUCT_BACKLOG_PLAN_ID = "com.ibm.team.apt.plantype.product.backlog"; //$NON-NON-L18N
    /**
     *
     */
    public static final String ITERATION_PLAN_ID = "com.ibm.team.apt.plantype.default"; //$NON-NLS-1$
    private static final WeakHashMap<String, WeakReference<RtcPlanType>> TYPES =
            new WeakHashMap<String, WeakReference<RtcPlanType>>();

    /**
     * 
     * @param planType
     * @return
     */
    public static RtcPlanType getType(String planType) {
        RtcPlanType data = new RtcPlanTypeDefault(ITERATION_PLAN_ID);
        if (TEAM_RELEASE_PLAN_ID.equals(planType)) {
            data = new RtcPlanTypeRelease(planType);
        }
        if (PROJECT_RELEASE_PLAN_ID.equals(planType)) {
            data = new RtcPlanTypeReleaseProject(planType);
        }
        if (PRODUCT_BACKLOG_PLAN_ID.equals(planType)) {
            data = new RtcPlanTypeProductBacklog(planType);
        }
        WeakReference<RtcPlanType> ref = TYPES.get(data.getId());
        RtcPlanType result = (ref != null) ? ref.get() : null;

        if (result == null) {
            TYPES.put(data.getId(), new WeakReference<RtcPlanType>(data));
            result = data;
        }
        return result;
    }

}
