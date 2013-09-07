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

import java.util.Random;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
class DummyContributorProgressInfoImpl implements RtcProgressInfo {

    private Contributor contributor;
    private Random rand = new Random();
    private double eu, du, pu;
    private int ci, pi, ei;
    private EventSourceSupport<RtcProgressInfoEvent> eventSource = new EventSourceSupport<RtcProgressInfoEvent>();

    public DummyContributorProgressInfoImpl(Contributor contributor) {
        this.contributor = contributor;
        pu = (double) rand.nextInt(100) * 0.8968563453;
        eu = ((float) pu * rand.nextFloat());
        du = ((float) pu * rand.nextFloat());

        pi = rand.nextInt(100);
        ci = (int) ((float) pi * rand.nextFloat());
        ei = (int) ((float) pi * rand.nextFloat());
    }

    @Override
    public double getExpectedUnits() {
        return eu;
    }

    @Override
    public double getDoneUnits() {
        return du;
    }

    @Override
    public double getPlannedUnits() {
        return pu;
    }

    @Override
    public int getCompletedItemsCount() {
        return ci;
    }

    @Override
    public int getPlannedItemsCount() {
        return pi;
    }

    @Override
    public int getEstimatedItemsCount() {
        return ei;
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
