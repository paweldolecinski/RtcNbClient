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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 * Not implemented yet. Do not use.
 *
 * @author Pawel Dolecinski
 */
public class RtcEnumerationHierarchyChecker implements RtcPlanItemChecker {

    private final RtcPlan plan;
    private EventSourceSupport<RtcPlanItemCheckerEvent> eventSource = new EventSourceSupport<RtcPlanItemCheckerEvent>();

    /**
     * Not implemented yet. Do not use.
     *
     * @param plan
     */
    public RtcEnumerationHierarchyChecker(RtcPlan plan) {
        this.plan = plan;
    }

    @Override
    public RtcPlanItemProblem[] getProblems() {
        List<RtcPlanItemProblem> res = new ArrayList<RtcPlanItemProblem>();
        for (RtcPlanItem item : plan.getPlanItemsManager().getPlanItems()) {
            res.addAll(Arrays.asList(checkPlanItem(item)));
        }
        return res.toArray(new RtcPlanItemProblem[]{});
    }

    @Override
    public RtcPlanItemProblem[] checkPlanItem(RtcPlanItem planItem) {
        return new RtcPlanItemProblem[]{};
    }

    @Override
    public RtcPlanItemProblem getMostSevereProblem() {
        return null;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
    }

    public final void removeListener(EventListener<RtcPlanItemCheckerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemCheckerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemCheckerEvent> listener) {
        eventSource.addListener(listener);
    }
}
