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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public class RtcPlanItemCheckerUtilities {

    public static RtcPlanItemProblem.RtcSeverity getHighestPlanItemProblemSeverity(RtcPlan plan) {
        RtcPlanItemProblem.RtcSeverity higest = RtcPlanItemProblem.RtcSeverity.NONE;
        RtcPlanItemChecker[] chcks = plan.getPlanItemCheckers();
        for (RtcPlanItemChecker c : chcks) {
            RtcPlanItemProblem p = c.getMostSevereProblem();
            if (p != null) {
                if (compareSeverities(higest, p.getSeverity()) > 0) {
                    higest = p.getSeverity();
                }
            }
        }
        return higest;
    }

    public static RtcPlanItemProblem.RtcSeverity getHighestPlanItemProblemSeverity(RtcPlanItemChecker[] checkers) {
        RtcPlanItemProblem.RtcSeverity higest = RtcPlanItemProblem.RtcSeverity.NONE;
        RtcPlanItemChecker[] chcks = checkers;
        for (RtcPlanItemChecker c : chcks) {
            RtcPlanItemProblem p = c.getMostSevereProblem();
            if (p != null) {
                if (compareSeverities(higest, p.getSeverity()) > 0) {
                    higest = p.getSeverity();
                }
            }
        }
        return higest;
    }

    public static RtcPlanItemProblem.RtcSeverity getHighestPlanItemProblemSeverity(RtcPlanItem planItem, RtcPlanItemChecker[] checkers) {
        RtcPlanItemProblem.RtcSeverity higest = RtcPlanItemProblem.RtcSeverity.NONE;
        RtcPlanItemChecker[] chcks = checkers;
        for (RtcPlanItemChecker c : chcks) {
            RtcPlanItemProblem[] problems = c.checkPlanItem(planItem);
            if (problems != null) {
                for (RtcPlanItemProblem p : problems) {
                    if (compareSeverities(higest, p.getSeverity()) > 0) {
                        higest = p.getSeverity();
                    }
                }

            }
        }
        return higest;
    }

    /**
     * If severity2 is higher then severity1 then returns positive integer.
     * If severity1 is higher then severity2 then returns negative integer.
     * If severity1 equals to severity2 then returns 0.
     * @param severity1
     * @param severity2
     * @return
     */
    public static int compareSeverities(RtcPlanItemProblem.RtcSeverity severity1, RtcPlanItemProblem.RtcSeverity severity2) {
        if(severity1 == null)
            return 1;
        if(severity2 == null)
            return -1;
        switch (severity1) {
            case NONE:
                switch (severity2) {
                    case NONE:
                        return 0;
                    case INFO:
                        return 1;
                    case WARNING:
                        return 2;
                    case ERROR:
                        return 3;
                    case FATAL_ERROR:
                        return 4;
                }
            case INFO:
                switch (severity2) {
                    case NONE:
                        return -1;
                    case INFO:
                        return 0;
                    case WARNING:
                        return 1;
                    case ERROR:
                        return 2;
                    case FATAL_ERROR:
                        return 3;
                }
            case WARNING:
                switch (severity2) {
                    case NONE:
                        return -2;
                    case INFO:
                        return -1;
                    case WARNING:
                        return 0;
                    case ERROR:
                        return 1;
                    case FATAL_ERROR:
                        return 2;
                }
            case ERROR:
                switch (severity2) {
                    case NONE:
                        return -3;
                    case INFO:
                        return -2;
                    case WARNING:
                        return -1;
                    case ERROR:
                        return 0;
                    case FATAL_ERROR:
                        return 1;
                }
            case FATAL_ERROR:
                switch (severity2) {
                    case NONE:
                        return -4;
                    case INFO:
                        return -3;
                    case WARNING:
                        return -2;
                    case ERROR:
                        return -1;
                    case FATAL_ERROR:
                        return 0;
                }
            default:
                assert false;
                return 0;
        }
    }

    private RtcPlanItemCheckerUtilities() {
    }
}
