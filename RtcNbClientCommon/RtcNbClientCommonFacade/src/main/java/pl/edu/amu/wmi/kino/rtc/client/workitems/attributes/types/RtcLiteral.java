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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types;

import java.awt.Image;

/**
 * This abstract class represents ILiteral class from RTC API.
 * Literal represents value of enumeration attributes.
 * @author Pawel Dolecinski
 */
abstract public class RtcLiteral implements Comparable<RtcLiteral> {

    public static final RtcLiteral NullValue = new RtcLiteralNullValue();

    /**
     * 
     * @return icon if literal has one or null in another case
     */
    abstract public Image getIcon();

    /**
     * 
     * @return human readable name of literal
     */
    abstract public String getName();

    /**
     * 
     * @return internal literal ID
     */
    abstract public String getId();

    @Override
    public int compareTo(RtcLiteral o) {
        return this.getId().compareTo(o.getId());
    }

    private static class RtcLiteralNullValue extends RtcLiteral {

        public RtcLiteralNullValue() {
        }

        @Override
        public Image getIcon() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getName() {
            return "Unnasigned Enum";
        }

        @Override
        public String getId() {
            return "null";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
