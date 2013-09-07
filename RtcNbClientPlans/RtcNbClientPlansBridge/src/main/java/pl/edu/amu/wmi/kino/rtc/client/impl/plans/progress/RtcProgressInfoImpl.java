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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.RtcPlanImpl;

import com.ibm.team.apt.api.common.planning.IProgressInformation;
import com.ibm.team.apt.api.common.planning.IProgressInformation.Unit;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 * @since 0.2.1.3
 * @author Pawel Dolecinski
 */
public class RtcProgressInfoImpl implements RtcProgressInfo {

    private List<IProgressInformation> progresses = new ArrayList<IProgressInformation>();
    private final RtcComplexityComputator complexity;
    private EventSourceSupport<RtcProgressInfo.RtcProgressInfoEvent> eventSource = new EventSourceSupport<RtcProgressInfo.RtcProgressInfoEvent>();

    /**
     *
     * @param rtcPlanImpl
     * @param complexity
     */
    public RtcProgressInfoImpl(RtcPlanImpl rtcPlanImpl, RtcComplexityComputator complexity) {
        this(rtcPlanImpl.getPlanData() == null
                ? new IProgressInformation[]{}
                : new IProgressInformation[]{rtcPlanImpl.getPlanData().getProgressInformation()},
                complexity);
    }

    /**
     *
     * @param progresses
     * @param complexity 
     */
    public RtcProgressInfoImpl(IProgressInformation[] progresses, RtcComplexityComputator complexity) {
        assert complexity != null : "Complexity computator cannanot be null";
        this.progresses = Arrays.asList(progresses);
        this.complexity = complexity;
    }

    @Override
    public double getExpectedUnits() {
        double res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getStepsDoneExpected(Unit.COMLEXITY);
        }

        res = complexity.computeComplexity(res);

        return res;
    }

    @Override
    public double getDoneUnits() {
        double res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getStepsDoneDelta(Unit.COMLEXITY);
        }

        res = complexity.computeComplexity(res);

        return res;
    }

    @Override
    public double getPlannedUnits() {
        double res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getSteps(Unit.COMLEXITY);
        }

        res = complexity.computeComplexity(res);

        return res;
    }

    @Override
    public int getCompletedItemsCount() {
        int res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getCloseCount();
        }
        return res;
    }

    @Override
    public int getPlannedItemsCount() {
        int res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getCount();
        }
        return res;
    }

    @Override
    public int getEstimatedItemsCount() {
        int res = 0;
        for (IProgressInformation pr : progresses) {
            res += pr.getEstimatedCount(Unit.COMLEXITY);
        }
        return res;
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
