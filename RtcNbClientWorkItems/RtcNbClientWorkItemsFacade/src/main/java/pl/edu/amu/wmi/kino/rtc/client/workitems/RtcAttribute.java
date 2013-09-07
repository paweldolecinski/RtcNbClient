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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import org.openide.util.Lookup;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public abstract class RtcAttribute {

    /**
     * This method shall return lookup associated with a given attribute.
     * Contents of this lookup may vary, but shall be more or less consistent
     * across one attribute type.
     * Capabilities initially thought to be exposed are : "Possible values",
     * "Preffersed values"
     * @return lookup containing capabilities of the attribute type.
     */
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }
}
