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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcCategoryImpl;

import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkItemCategoryAttribute extends RtcWorkItemIAttribute<RtcCategory> {

    public RtcWorkItemCategoryAttribute(Class valueType, ToStringProvider stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    protected void getValueInvoked() {

        if (!(value != null && value instanceof RtcCategory)) {

            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            value = wi.getCategory();
        }
    }

    @Override
    protected void setValueInvoked(RtcCategory value) {

        if (value instanceof RtcCategoryImpl) {
            getLookup().lookup(RtcWorkItem.class).setValue(this, ((RtcCategoryImpl)value).getCategory().getItemHandle());
        }
    }

    @Override
    public Type getAttributeType() {
        return Type.CATEGORY;
    }

    @Override
    public String toString() {
        return getLookup().lookup(ToStringProvider.class).toString(value);
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<RtcCategory> {

        @Override
        public String toString(RtcCategory value) {

            return value.getName();
        }

    }

    public class WorkingCopyCategoryListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
