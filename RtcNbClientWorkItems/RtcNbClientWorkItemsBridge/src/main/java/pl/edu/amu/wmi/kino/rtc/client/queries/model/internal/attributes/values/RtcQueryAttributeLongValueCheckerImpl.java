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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values;

import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeNumericValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryAttributeLongValueCheckerImpl implements RtcQueryAttributeNumericValueChecker, ValueCreator {

    @Override
    public RtcQueryAttributeValue getValue(String value) throws IllegalArgumentException {
        if (value.equals("")) {
            return new LongValueImpl(null);
        } else {
            try {
                long tmp = Long.parseLong(value);
                return new LongValueImpl(tmp);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(
                        NbBundle.getMessage(
                        RtcQueryAttributeLongValueCheckerImpl.class,
                        "NumericValueChecker.error"), ex);
            }
        }
    }

    @Override
    public String getRepresentation(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof LongValueImpl) {
            LongValueImpl v = (LongValueImpl) value;
            Long l = v.getLong();
            if (l != null) {
                return Long.toString(l);
            } else {
                return "";
            }
        }
        if (value instanceof RtcQueryAttributeValueImpl) {
            RtcQueryAttributeValueImpl impl = (RtcQueryAttributeValueImpl) value;
            if (impl.getValue() == null) {
                return "";
            }
        }
        throw new IllegalArgumentException(
                NbBundle.getMessage(
                RtcQueryAttributeLongValueCheckerImpl.class,
                "NumericValueChecker.error"));
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof Long) {
            return new LongValueImpl((Long) obj);
        }
        throw new IllegalArgumentException();
    }
}

class LongValueImpl extends RtcQueryAttributeValueImpl {

    private Long val;

    public LongValueImpl(Long rtcValue) {
        super(rtcValue);
        this.val = rtcValue;
    }

    public Long getLong() {
        return val;
    }
}
