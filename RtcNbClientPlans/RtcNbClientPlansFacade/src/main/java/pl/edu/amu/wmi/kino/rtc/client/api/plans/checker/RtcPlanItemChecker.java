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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.checker;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker.RtcPlanItemCheckerEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public interface RtcPlanItemChecker extends EventSource<RtcPlanItemCheckerEvent> {

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @return
     */
    public abstract RtcPlanItemProblem[] getProblems();

    /**
     *
     * 
     * If no problem for given planItem was wound by this checker it will
     * return <code>null</code>.
     * This can be long running operation. Do not call on event dispatch thread.
     * @param planItem
     * @return
     */
    public abstract RtcPlanItemProblem[] checkPlanItem(RtcPlanItem planItem);

    /**
     *
     * If no problem was wound by this checker it will return <code>null</code>.
     * This can be long running operation. Do not call on event dispatch thread.
     * @return may be null.
     */
    public abstract RtcPlanItemProblem getMostSevereProblem();

    public abstract RtcPlan getPlan();

    public enum RtcPlanItemCheckerEvent {

        NEW_PROBLEM_FOUND,
        NEW_PROBLEMS_FOUND,
        PROBLEM_RESOLVED,
        MOST_SEVERE_PROBLEM_CHANGED,}
}
