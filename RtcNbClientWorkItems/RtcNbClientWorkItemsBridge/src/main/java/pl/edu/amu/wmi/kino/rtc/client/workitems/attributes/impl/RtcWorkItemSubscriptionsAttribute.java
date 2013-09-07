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

import java.util.Collection;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.internal.util.DelegatingItemList;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkItemSubscriptionsAttribute extends RtcWorkItemIAttribute<Collection<Contributor>> {

    @SuppressWarnings("unchecked")
    public RtcWorkItemSubscriptionsAttribute(Class valueType, ToStringProvider<Collection<Contributor>> stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void getValueInvoked() {

        if (value == null || (value instanceof DelegatingItemList)) {
            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            List<Contributor> res = (List<Contributor>) wi.getValue(this);
            value = res;
        }
    }

    @Override
    protected void setValueInvoked(Collection<Contributor> value) {
        getLookup().lookup(RtcWorkItem.class).setValue(this, value);
    }

    @Override
    public Type getAttributeType() {
        return Type.SUBSCRIPTIONS;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<Collection<Contributor>> {

        @Override
        public String toString(Collection<Contributor> value) {
            return value.toString();
        }
    }

    public class WorkingCopySubscriptionsListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
