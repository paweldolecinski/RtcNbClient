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

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDeliverableImpl;

import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkItemDeliverableAttribute extends RtcWorkItemIAttribute<RtcDeliverable> {

    public RtcWorkItemDeliverableAttribute(Class valueType, ToStringProvider stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    protected void getValueInvoked() {

        if (!(value != null && value instanceof RtcDeliverable)) {
            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            value = (RtcDeliverable) wi.getValue(this);
        }
    }

    @Override
    protected void setValueInvoked(RtcDeliverable value) {

        if (value instanceof RtcDeliverableImpl) {
            getLookup().lookup(RtcWorkItem.class).setValue(this, ((RtcDeliverableImpl) value).getDeliverable().getItemHandle());
        }
    }

    @Override
    public String toString() {
        return getLookup().lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public Type getAttributeType() {
        return Type.DELIVERABLE;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<RtcDeliverable> {

        @Override
        public String toString(RtcDeliverable value) {
            return value.getName();
        }
    }

    public class WorkingCopyDeliverableListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
