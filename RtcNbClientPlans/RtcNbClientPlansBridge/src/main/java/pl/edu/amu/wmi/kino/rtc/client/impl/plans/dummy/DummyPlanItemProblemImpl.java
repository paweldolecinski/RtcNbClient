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
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 *
 * @author Patryk Żywica
 */
class DummyPlanItemProblemImpl extends RtcPlanItemProblem {

    private RtcPlanItem planItem;
    private RtcPlanItemChecker checker;
    private RtcSeverity severity;

    DummyPlanItemProblemImpl(RtcPlanItemChecker checker, RtcPlanItem item) {
        this.planItem = item;
        this.checker = checker;
        Random rand = new Random();
        int r = rand.nextInt(10);
        switch (r) {
            case 2:
            case 3:
                severity = RtcSeverity.INFO;
                break;
            case 4:
            case 5:
                severity = RtcSeverity.WARNING;
                break;
            case 6:
            case 7:
                severity = RtcSeverity.ERROR;
                break;
            case 8:
            case 9:
                severity = RtcSeverity.FATAL_ERROR;
                break;
            default:
                severity = RtcSeverity.NONE;
        }

    }

    @Override
    public RtcPlanItem getPlanItem() {
        return planItem;
    }

    @Override
    public RtcPlanItemChecker getPlanItemChecker() {
        return checker;
    }

    @Override
    public RtcSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getMessage() {
        return "Problem with severity <b><font color=\"red\">"+severity.name()+ "</font> </b>occured.";
    }
}
