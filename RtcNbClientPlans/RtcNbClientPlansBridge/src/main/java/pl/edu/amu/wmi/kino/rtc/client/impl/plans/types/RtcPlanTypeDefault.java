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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.types;

import java.awt.Image;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;

/**
 *
 * @author Pawel Dolecinski
 */
class RtcPlanTypeDefault extends RtcPlanType {

    private final String planType;

    RtcPlanTypeDefault(String planType) {
        this.planType = planType;
    }

    @Override
    public String getId() {
        return planType;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcPlanTypeDefault.class, planType + ".name");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(RtcPlanTypeDefault.class, planType + ".name");
    }

    @Override
    public Image getIcon() {
        Image icon = ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/types/iteration_plan.gif");
        return icon;
    }

    @Override
    public Lookup getLookup() {
        //TODO: empty lookup
        return Lookup.EMPTY;
    }
}
