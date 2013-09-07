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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.items;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;

/**
 * Each plan should have its own RtcPlanWorkItem for each work item in this plan.
 * It means that if two different plans use the same work item (e.g. id 999) then
 * first plan should have its own planItem object returning RtcWorkItem id 999 as
 * well as second do.
 * @author Patryk Żywica
 */
public abstract class RtcPlanWorkItem extends RtcPlanItem {

    /**
     *
     */
    protected boolean dirty = false;

    /**
     *
     * @return
     */
    public abstract RtcWorkItem getWorkItem();

    /**
     * Gives information about state of plan item.
     * Resolved means that work item releated with plan item is closed states group.
     *
     * @since 0.2.1.4
     * @return true if resolved or false if not
     */
    public abstract boolean isResolved();

    /**
     *
     * @return
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     *
     */
    public enum RtcPlanWorkItemState {
    }
}
