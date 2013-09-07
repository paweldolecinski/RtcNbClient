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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanItemProblemImpl extends RtcPlanItemProblem {

    private final RtcSeverity severity;
    private final String message;
    private final RtcPlanItem item;
    private final RtcPlanItemChecker checker;

    /**
     *
     * @param item
     * @param checker
     * @param severity
     * @param message
     */
    public RtcPlanItemProblemImpl(RtcPlanItem item, RtcPlanItemChecker checker, RtcSeverity severity, String message) {
        this.item = item;
        this.checker = checker;
        this.severity = severity;
        this.message = message;
    }

    /**
     *
     * @return
     */
    @Override
    public RtcPlanItem getPlanItem() {
        return item;
    }

    /**
     *
     * @return
     */
    @Override
    public RtcPlanItemChecker getPlanItemChecker() {
        return checker;
    }

    /**
     *
     * @return
     */
    @Override
    public RtcSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
