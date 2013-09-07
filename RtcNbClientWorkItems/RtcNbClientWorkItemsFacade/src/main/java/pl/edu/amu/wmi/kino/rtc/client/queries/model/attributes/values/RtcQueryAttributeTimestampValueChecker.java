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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values;

import java.sql.Timestamp;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;

/**
 * @see RtcQueryAttributeValueChecker
 * @author Patryk Żywica
 */
public interface RtcQueryAttributeTimestampValueChecker extends RtcQueryAttributeValueChecker {

    /**
     *
     * @param <code>Timestamp</code> from which value should be created
     * @return value created from given timestamp. Never null.
     * @throws IllegalArgumentException when given timestamp is not proper for this checker.
     */
    public RtcQueryAttributeValue getValue(Timestamp timespamp) throws IllegalArgumentException;

    /**
     *
     * @param value for which <code>Timestamp</code> representation should be created
     * @return timestamp representation of given value
     * @throws IllegalArgumentException when given value is not proper for this checker
     */
    public Timestamp getTimestampRepresentation(RtcQueryAttributeValue value) throws IllegalArgumentException;

    public Pair<Integer,Unit> getRepresentation(RtcQueryAttributeVariable variable) throws IllegalArgumentException;

    public RtcQueryAttributeVariable getVariable(int time,Unit unit) throws IllegalArgumentException;

    public static enum Unit {

        DAYS_AGO,
        DAYS_FROM_NOW,
        HOURS_AGO,
        HOURS_FROM_NOW,
        MINUTES_AGO,
        MINUTES_FROM_NOW,
        MONTHS_AGO,
        MONTHS_FROM_NOW,
        YEARS_AGO,
        YEARS_FROM_NOW;
    }
}
