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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.progress;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo.RtcProgressInfoEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public interface RtcProgressInfo extends EventSource<RtcProgressInfoEvent> {

    /**
     *
     * @return
     */
    public abstract double getDoneUnits();

    /**
     *
     * @return
     */
    public abstract double getPlannedUnits();

    /**
     *
     * It may return negative value to indicate that prediction is unsupported
     * by this <code>RtcProgressInfo</code> object.
     * @since 0.2.1.3
     * @return
     */
    public abstract double getExpectedUnits();

    /**
     *
     * @return
     */
    public abstract int getCompletedItemsCount();

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

    public enum RtcProgressInfoEvent {

        COMPLETED_ITEMS_COUNT_CHANGED,
        DONE_UNITS_CHANGED,
        ESTIMATED_ITEMS_COUNT_CHANGED,
        PLANNED_ITEMS_COUNT_CHANGED,
        PLANNED_UNITS_CHANGED,
        EXPECTED_UNITS_CHANGED;
    }
}
