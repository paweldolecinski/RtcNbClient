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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo.RtcLoadInfoEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyLoadInfoImpl implements RtcLoadInfo {

    private Contributor contributor;
    private double ha, hu, tl;
    private int ic, ie, op;
    private Random rand = new Random();
    private EventSourceSupport<RtcLoadInfo.RtcLoadInfoEvent> eventSource= new EventSourceSupport<RtcLoadInfo.RtcLoadInfoEvent>();

    public DummyLoadInfoImpl(Contributor contributor) {
        this.contributor = contributor;
        ha = (double) rand.nextInt(100) * 0.87686;
        hu = (double) rand.nextInt(100) * 0.674;
        ic = rand.nextInt(100);
        op = rand.nextInt(100);
        tl = (double) rand.nextInt(100) * 0.8565;
        ie = (int) ((float) ic * rand.nextFloat());
    }

    @Override
    public Contributor getContributor() {
        return contributor;
    }

    @Override
    public double getWorkHoursAvailable() {
        return ha;
    }

    @Override
    public double getWorkHoursUsed() {
        return hu;
    }

    @Override
    public int getPlannedItemsCount() {
        return ic;
    }

    @Override
    public int getEstimatedItemsCount() {
        return ie;
    }

    @Override
    public int getOpenItemsCount() {
        return op;
    }

    @Override
    public double getWorkTimeLeft() {
        return tl;
    }

    public final void removeListener(EventListener<RtcLoadInfoEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcLoadInfoEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcLoadInfoEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
