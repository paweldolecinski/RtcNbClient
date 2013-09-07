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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl;

import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.model.IAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcDurationValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkItemDurationAttribute extends RtcWorkItemIAttribute<RtcDurationImpl> {

    /**
     * 
     * @param valueType
     * @param stringProvider
     * @param iAttribute
     * @param wi
     * @param aditionalContextContent
     */
    public RtcWorkItemDurationAttribute(Class<RtcDurationImpl> valueType, ToStringProvider<RtcDurationImpl> stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    protected final void getValueInvoked() {

        if (!(value != null && value instanceof RtcDurationImpl)) {

            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            RtcDurationImpl v = (RtcDurationImpl) wi.getValue(this);
            value = v;
        }
    }

    @Override
    protected void setValueInvoked(RtcDurationImpl value) {
        RtcDurationValueChecker valueChecker = getLookup().lookup(RtcDurationValueChecker.class);
        if (value instanceof RtcDurationImpl && valueChecker.isvalueProper(value)) {
            RtcDurationImpl v = value;

            getLookup().lookup(RtcWorkItem.class).setValue(this, v);
        }
    }

    @Override
    public String toString() {
        return getLookup().lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public Type getAttributeType() {
        return Type.DURATION;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<RtcDurationImpl> {

        @Override
        public String toString(RtcDurationImpl value) {
            return value.toString();
        }
    }

    public class WorkingCopyDurationListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
