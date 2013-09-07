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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.load;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo.RtcLoadInfoEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 *
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public interface RtcLoadInfo extends EventSource<RtcLoadInfoEvent> {

    /**
     *
     * @return
     */
    public abstract Contributor getContributor();

    /**
     *
     * @return
     */
    public abstract double getWorkHoursAvailable();

    /**
     * 
     * @return
     */
    public abstract double getWorkHoursUsed();

    /**
     *
     * @return
     */
    public abstract int getPlannedItemsCount();

    /**
     *
     * @return
     */
    public abstract int getEstimatedItemsCount();

    /**
     *
     * @return
     */
    public abstract int getOpenItemsCount();

    /**
     *
     * @return
     */
    public abstract double getWorkTimeLeft();

//TODO : bikol : think about using property change listeners here
    public enum RtcLoadInfoEvent {

        PLANNED_ITEMS_COUNT_CHANGED,
        ESTIMATED_ITEMS_COUNT_CHANGED,
        WORK_HOURS_AVAILABLE_CHANGED,
        WORK_HOURS_USED_CHANGED;
    }
}
