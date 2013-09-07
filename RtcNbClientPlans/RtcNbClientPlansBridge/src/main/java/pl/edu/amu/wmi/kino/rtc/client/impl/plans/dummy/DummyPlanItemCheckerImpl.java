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

import java.util.HashMap;
import java.util.Random;

import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemCheckerUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemCheckerImpl implements RtcPlanItemChecker, Runnable {

    private RtcPlan plan;
    private HashMap<RtcPlanItem, RtcPlanItemProblem> problems =
            new HashMap<RtcPlanItem, RtcPlanItemProblem>();
    private EventSourceSupport<RtcPlanItemCheckerEvent> eventSource = new EventSourceSupport<RtcPlanItemCheckerEvent>();

    public DummyPlanItemCheckerImpl(RtcPlan plan) {
        // //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        this.plan = plan;
        Random rand = new Random();
        for (RtcPlanItem item : plan.getPlanItemsManager().getPlanItems()) {
            if (rand.nextInt(100) > 63) {
                problems.put(item, new DummyPlanItemProblemImpl(this, item));
            }
        }
        RequestProcessor.getDefault().post(this, 7000);
    }

    @Override
    public RtcPlanItemProblem[] getProblems() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return problems.values().toArray(new RtcPlanItemProblem[]{});
    }

    @Override
    public RtcPlanItemProblem[] checkPlanItem(RtcPlanItem planItem) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if (problems.containsKey(planItem)) {
            return new RtcPlanItemProblem[]{problems.get(planItem)};

        } else {
            return new RtcPlanItemProblem[]{};
        }
    }

    @Override
    public RtcPlanItemProblem getMostSevereProblem() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        RtcPlanItemProblem highest = null;
        for (RtcPlanItemProblem p : problems.values()) {
            if (highest != null) {
                if (RtcPlanItemCheckerUtilities.compareSeverities(p.getSeverity(), highest.getSeverity()) < 0) {
                    highest = p;
                }
            } else {
                highest = p;
            }
        }
        return highest;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
    }

    @Override
    public void run() {
        problems.clear();
        Random rand = new Random();
        for (RtcPlanItem item : plan.getPlanItemsManager().getPlanItems()) {
            if (rand.nextInt(100) > 63) {
                problems.put(item, new DummyPlanItemProblemImpl(this, item));
            }
        }
        fireEvent(RtcPlanItemCheckerEvent.NEW_PROBLEM_FOUND);
        fireEvent(RtcPlanItemCheckerEvent.MOST_SEVERE_PROBLEM_CHANGED);
        RequestProcessor.getDefault().post(this, 7000);
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
