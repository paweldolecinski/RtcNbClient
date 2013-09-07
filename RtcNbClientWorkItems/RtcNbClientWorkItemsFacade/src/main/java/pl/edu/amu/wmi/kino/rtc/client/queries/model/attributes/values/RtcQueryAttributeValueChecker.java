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

import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;

/**
 * Super interface for all value checkers.
 *
 * Instances of this classes may be registered in query attribute's lookup to expose
 * new query feature.
 *
 * @see RtcQueryAttributeBooleanValueProvider
 * @see RtcQueryAttributeDurationValueChecker
 * @see RtcQueryAttributeNumericValueChecker
 * @see RtcQueryAttributeStringValueChecker
 * @see RtcQueryAttributeTimestampValueChecker
 * @author Patryk Żywica
 */
public interface RtcQueryAttributeValueChecker {

    /**
     *
     * @param string from which value should be created
     * @return value created from given string. Never null.
     * @throws IllegalArgumentException when given string is not proper for this checker.
     */
    RtcQueryAttributeValue getValue(String string) throws IllegalArgumentException;

    /**
     *
     * @param value for which representation should be created
     * @return string representation of given value
     * @throws IllegalArgumentException when given value is not proper for this checker
     */
    String getRepresentation(RtcQueryAttributeValue value) throws IllegalArgumentException;
}
