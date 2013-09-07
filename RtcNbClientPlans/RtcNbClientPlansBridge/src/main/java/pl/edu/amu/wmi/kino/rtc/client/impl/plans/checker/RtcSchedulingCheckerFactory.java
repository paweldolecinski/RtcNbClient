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

import java.util.Hashtable;
import java.util.Map;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcSchedulingCheckerFactory implements RtcPlanItemCheckerFactory {

    Map<String, RtcPlanItemChecker> checkers = new Hashtable<String, RtcPlanItemChecker>();

    @Override
    public RtcPlanItemChecker createChecker(RtcPlan plan) {
        RtcPlanItemChecker checker = checkers.get(plan.getPlanIdentifier());
        if(checker == null){
            checker = new RtcSchedulingChecker(plan);
            checkers.put(plan.getPlanIdentifier(), checker);
        }
        return checker;
    }

}
