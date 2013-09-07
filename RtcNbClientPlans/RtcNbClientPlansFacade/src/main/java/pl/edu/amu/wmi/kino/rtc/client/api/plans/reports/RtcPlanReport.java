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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.reports;

import java.awt.Image;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;

/**
 *
 * @author Pawel Dolecinski
 */
public abstract class RtcPlanReport {

    /**
     * 
     * @return name of report
     */
    public abstract String getName();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @param plan
     * @return image representing chart for given plan or null if chart is not specified
     */
    public abstract Image getChartFor(RtcPlan plan);

    /**
     * Default you will get empty implementation of Lookup.
     * Probably this method is only for internal use.
     *
     * @since 0.2.1.4
     * @return lookup for report
     */
    public abstract Lookup getLookup();
}
