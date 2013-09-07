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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyProgressInfoImpl implements RtcProgressInfo {

    private EventSourceSupport<RtcProgressInfoEvent> eventSource = new EventSourceSupport<RtcProgressInfoEvent>();

    @Override
    public double getDoneUnits() {
        return 10.322;
    }

    @Override
    public double getPlannedUnits() {
        return 50.6;
    }

    @Override
    public int getCompletedItemsCount() {
        return 12;
    }

    @Override
    public int getPlannedItemsCount() {
        return 40;
    }

    @Override
    public int getEstimatedItemsCount() {
        return 30;
    }

    @Override
    public double getExpectedUnits() {
        return 8.543;
    }

    public final void removeListener(EventListener<RtcProgressInfoEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcProgressInfoEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcProgressInfoEvent> listener) {
        eventSource.addListener(listener);
    }
}
