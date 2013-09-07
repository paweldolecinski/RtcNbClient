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

import java.awt.Image;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;

/**
 *
 * @author dolek
 */
class DummyPlanReportImpl extends RtcPlanReport{

    public DummyPlanReportImpl() {
    }

    @Override
    public String getName() {
        return "Dummy burndown chart";
    }

    @Override
    public Image getChartFor(RtcPlan plan) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/dummy/plan_chart.png");
    }

    @Override
    public Lookup getLookup() {
        return  Lookup.EMPTY;
    }

}
