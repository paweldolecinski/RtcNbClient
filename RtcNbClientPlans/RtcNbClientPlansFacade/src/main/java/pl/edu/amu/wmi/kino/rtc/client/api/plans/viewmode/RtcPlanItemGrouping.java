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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public abstract class RtcPlanItemGrouping {

    /**
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemViewMode.RtcBarType[] getPossibleBarTypes();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.4
     * @param planItems
     * @return array of plan item groups, could be null if there is defined non-grouping policy
     */
    public abstract RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan );

    /**
     * @since 0.2.1.4
     * @author Patryk Żywica
     */
    public interface RtcPlanItemGroup<T> {

        String getGroupLabel();

        Lookup getLookup();

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        RtcPlanItemGroup[] getChildGroups();

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        RtcPlanItem[] getPlanItems();
    }
}
