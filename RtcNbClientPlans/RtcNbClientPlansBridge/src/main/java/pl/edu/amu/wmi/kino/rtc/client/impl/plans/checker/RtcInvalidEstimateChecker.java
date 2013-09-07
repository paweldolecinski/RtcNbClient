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

import com.ibm.team.apt.internal.client.PlanItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.RtcPlanWorkItemImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcInvalidEstimateChecker implements RtcPlanItemChecker {

    private final RtcPlan plan;
    private final RtcPlansManager manager;
    private RtcPlanItemProblem mostSevere;
    private List<RtcPlanItemProblem> problems;
    private EventSourceSupport<RtcPlanItemCheckerEvent> eventSource = new EventSourceSupport<RtcPlanItemCheckerEvent>();

    /**
     * 
     * @param plan
     */
    public RtcInvalidEstimateChecker(RtcPlan plan) {
        this.plan = plan;
        this.manager = plan.getPlansManager();
    }

    @Override
    public RtcPlanItemProblem[] getProblems() {
        if (problems == null) {
            problems = new ArrayList<RtcPlanItemProblem>();
            for (RtcPlanItem item : plan.getPlanItemsManager().getPlanItems()) {
                problems.addAll(Arrays.asList(checkPlanItem(item)));
            }
        }
        return problems.toArray(new RtcPlanItemProblem[]{});
    }

    @Override
    public RtcPlanItemProblem[] checkPlanItem(RtcPlanItem planItem) {
        List<RtcPlanItemProblem> res = new ArrayList<RtcPlanItemProblem>();

        if (planItem instanceof RtcPlanWorkItem && !((RtcPlanWorkItem) planItem).isResolved()) {
            String message = "";
            RtcPlanItemProblemImpl problem;
            @SuppressWarnings("unchecked")
            RtcDurationImpl timeSpent = (RtcDurationImpl) planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.TIMESPENT.getId()));

            boolean isInvalidTimeRemaining = false;//com.ibm.team.apt.client.PlanDuration.isEncodedInvalidTimeRemaining(timeSpent);

            if (!isInvalidTimeRemaining && timeSpent != null) {
                @SuppressWarnings("unchecked")
                RtcDurationImpl estimate = (RtcDurationImpl) planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.ESTIMATE.getId()));
                isInvalidTimeRemaining = estimate == null || estimate.getDuration() < timeSpent.getDuration();
            }

            if (isInvalidTimeRemaining) {
                message = NbBundle.getMessage(RtcInvalidEstimateChecker.class, "problem.invalidEstimate_invalidTimeRemaining"); //$NON-NLS-1$

                if (planItem instanceof RtcPlanWorkItemImpl && ((RtcPlanWorkItemImpl) planItem).isDirty()) {
                    problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.WARNING, message);
                    if (mostSevere == null) {
                        mostSevere = problem;
                        fireEvent(RtcPlanItemCheckerEvent.MOST_SEVERE_PROBLEM_CHANGED);
                    }
                    res.add(problem);
                } else {
                    problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.FATAL_ERROR, message);
                    if (mostSevere == null || mostSevere.getSeverity().equals(RtcPlanItemProblem.RtcSeverity.WARNING)) {
                        mostSevere = problem;
                        fireEvent(RtcPlanItemCheckerEvent.MOST_SEVERE_PROBLEM_CHANGED);
                    }
                    res.add(problem);
                }

            }

        }
        return res.toArray(new RtcPlanItemProblem[]{});
    }

    @Override
    public RtcPlanItemProblem getMostSevereProblem() {
        RequestProcessor.getDefault().execute(new Runnable() {

            @Override
            public void run() {
                getProblems();
            }
        });

        return mostSevere;
    }

    /**
     * 
     * @return
     */
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
