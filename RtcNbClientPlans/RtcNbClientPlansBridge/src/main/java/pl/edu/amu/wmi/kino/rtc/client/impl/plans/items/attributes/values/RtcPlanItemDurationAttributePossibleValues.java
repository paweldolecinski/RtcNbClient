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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values;

import java.util.Arrays;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDuration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 *
 * @author michu
 */
public class RtcPlanItemDurationAttributePossibleValues implements RtcPlanItemAttributePossibleValues<RtcDuration> {

    private static RtcDuration[] list = {
        new RtcDurationImpl("30 min"),
        new RtcDurationImpl("1 h"),
        new RtcDurationImpl("2 h"),
        new RtcDurationImpl("3 h"),
        new RtcDurationImpl("4 h"),
        new RtcDurationImpl("8 h"),};

    /**
     *
     */
    public RtcPlanItemDurationAttributePossibleValues() {
    }

    @Override
    public List<RtcDuration> getPossibleValues() {
        return Arrays.asList(list);
    }
}
