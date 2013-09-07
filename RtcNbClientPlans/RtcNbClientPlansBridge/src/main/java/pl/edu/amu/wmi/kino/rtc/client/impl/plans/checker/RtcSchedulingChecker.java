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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcSchedulingChecker implements RtcPlanItemChecker {

    private final RtcPlan plan;
    private final RtcPlansManager manager;
    private RtcPlanItemProblem mostSevere;
    private List<RtcPlanItemProblem> problems;
    private EventSourceSupport<RtcPlanItemCheckerEvent> eventSource = new EventSourceSupport<RtcPlanItemCheckerEvent>();

    /**
     * 
     * @param plan
     */
    public RtcSchedulingChecker(RtcPlan plan) {
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
            fireEvent(RtcPlanItemCheckerEvent.NEW_PROBLEMS_FOUND);
        }
        return problems.toArray(new RtcPlanItemProblem[]{});
    }

    @Override
    public RtcPlanItemProblem[] checkPlanItem(RtcPlanItem planItem) {
        List<RtcPlanItemProblem> res = new ArrayList<RtcPlanItemProblem>();
        RtcPlanItemProblemImpl problem;
        if (planItem instanceof RtcPlanWorkItem && !((RtcPlanWorkItem) planItem).isResolved()) {
            String message = "";
            @SuppressWarnings("unchecked")
            Timestamp dueDate = (Timestamp) planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.DUE_DATE.getId()));
            if (dueDate != null && dueDate.before(new Timestamp(System.currentTimeMillis()))) {
                message = NbBundle.getMessage(RtcSchedulingChecker.class, "problem.scheduling_pastDueDate", new Date(dueDate.getTime())); //$NON-NLS-1$
                //$NON-NLS-1$
                problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.WARNING, message);
                if (mostSevere == null) {
                    mostSevere = problem;
                }
                ////System.out.println("Problem: " + problem.getMessage());
                res.add(problem);
            }
            if (planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.ESTIMATE.getId())) == null
                    && !planItem.getPlanItemType().equals(RtcPlanItemType.NON_EXECUTABLE)) {
                message = NbBundle.getMessage(RtcSchedulingChecker.class, "problem.scheduling_noestimate"); //$NON-NLS-1$
                problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.WARNING, message);
                if (mostSevere == null) {
                    mostSevere = problem;
                }
                res.add(problem);
            }
            @SuppressWarnings("unchecked")
            Timestamp scheduledTime = new Timestamp(System.currentTimeMillis() + ((RtcDurationImpl) planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.ESTIMATE.getId()))).getDurationInMillis());
//        Timespan scheduledTime = worktimeScheduler.calculateTimeSpan(new Timestamp(System.currentTimeMillis()), (Long)planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.ESTIMATE.getId())), true);
//        Timestamp scheduledTime = (Timestamp) planItem.getPlanAttributeValue(manager.getPlanItemAttribute(PlanItem.SCHEDULED_TIME.getId()));
            if (scheduledTime != null) {

                if (dueDate != null && dueDate.before(scheduledTime)) {
                    message = NbBundle.getMessage(RtcSchedulingChecker.class, "problem.scheduling_pastDueDate", new Date(dueDate.getTime()).toString(), new Date(scheduledTime.getTime()).toString()); //$NON-NLS-1$
                    problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.WARNING, message);
                    if (mostSevere == null) {
                        mostSevere = problem;
                    }
                    ////System.out.println("Problem: " + problem.getMessage());
                    res.add(problem);
                }

                Iteration plannedFor = planItem.getPlan().getIteration();
                if (plannedFor != null) {
                    Date planEnd = plannedFor.getEndDate();
                    if (planEnd != null && planEnd.before(scheduledTime)) {
                        message = NbBundle.getMessage(RtcSchedulingChecker.class, "problem.scheduling_afterIterationEnd", new Date(scheduledTime.getTime()).toString(), planEnd.toString()); //$NON-NLS-1$
                        problem = new RtcPlanItemProblemImpl(planItem, this, RtcPlanItemProblem.RtcSeverity.WARNING, message);
                        if (mostSevere == null) {
                            mostSevere = problem;
                        }
                        res.add(problem);
                    }
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
