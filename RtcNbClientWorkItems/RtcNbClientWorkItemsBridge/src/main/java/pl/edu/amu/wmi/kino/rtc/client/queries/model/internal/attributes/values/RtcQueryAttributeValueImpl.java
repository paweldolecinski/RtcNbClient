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

import com.ibm.team.repository.common.IItemHandle;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;

/**
 *
 * @author Patryk Żywica
 */
public abstract class RtcQueryAttributeValueImpl implements RtcQueryAttributeValue {

    private Object value;

    public RtcQueryAttributeValueImpl(Object rtcValue) {
        this.value = rtcValue;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcQueryAttributeValueImpl) {
            Object toCompare = ((RtcQueryAttributeValueImpl) obj).getValue();

            if (value instanceof IItemHandle) {
                if (toCompare instanceof IItemHandle) {
                    return ((IItemHandle) value).getItemId().getUuidValue().equals(((IItemHandle) toCompare).getItemId().getUuidValue());
                } else {
                    return false;
                }
            }
            if (value == null) {
                return toCompare == null;
            }
            return value.equals(toCompare);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (value instanceof IItemHandle) {
            return ((IItemHandle) value).getItemId().getUuidValue().hashCode();
        }
        int hash = 3;
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    public static class RtcQueryAttributeRtcObjectValue extends RtcQueryAttributeValueImpl {

        public RtcQueryAttributeRtcObjectValue(Object rtcValue) {
            super(rtcValue);
        }
    }
}
