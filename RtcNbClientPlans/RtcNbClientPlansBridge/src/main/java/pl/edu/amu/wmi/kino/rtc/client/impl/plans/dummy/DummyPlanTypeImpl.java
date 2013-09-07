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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanTypeImpl extends RtcPlanType {

    public static DummyPlanTypeImpl TYPE1 = new DummyPlanTypeImpl();
    public static DummyPlanTypeImpl TYPE2 = new DummyPlanTypeImpl();

    private Image icon = ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/dummy/iteration_plan.gif");

    @Override
    public Image getIcon() {
        return icon;
    }

    @Override
    public String getId() {
        return "com.ibm.team.apt.plantype.default";
    }

    @Override
    public String getDisplayName() {
        return "Default";
    }

    @Override
    public String getDescription() {
        return "Default plan type";
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }
}
