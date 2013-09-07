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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public abstract class RtcPlanItemProblem {

    /**
     * 
     * @return
     */
    public abstract RtcPlanItem getPlanItem();

    /**
     *
     * @return
     */
    public abstract RtcPlanItemChecker getPlanItemChecker();

    /**
     *
     * @return
     */
    public abstract RtcSeverity getSeverity();

    /**
     * HTML syntax allowed, cannot starts or ends with <html> tags.
     * @return
     */
    public abstract String getMessage();

    /**
     * Severity tells you how important is problem.
     * This is information only for user but if problem has fatal error severity
     * then plan item with this problem couldn't be saved.
     */
    public enum RtcSeverity {

        /**
         * Problems with this severity shouldn't appears for users.
         */
        NONE,
        /**
         * Problems with this severity inform users about common
         * and not important things (i.e task on you are working belongs to someone else)
         */
        INFO,
        /**
         *
         */
        WARNING,
        /**
         *
         */
        ERROR,
        /**
         * Problems which should blocks saving operation. Fatal error means for example
         * bad value of time remaing which should be lower then estimation.
         */
        FATAL_ERROR;
    }
}
