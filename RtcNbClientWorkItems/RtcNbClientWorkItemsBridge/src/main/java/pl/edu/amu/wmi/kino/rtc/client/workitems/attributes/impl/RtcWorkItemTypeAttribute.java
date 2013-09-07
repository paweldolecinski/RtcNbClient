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
import java.awt.Image;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author michu
 */
public class RtcWorkItemTypeAttribute extends RtcWorkItemIAttribute<RtcWorkItemType> {

    public RtcWorkItemTypeAttribute(Class<RtcWorkItemType> valueType, ToStringProvider<RtcWorkItemType> stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    protected void getValueInvoked() {

        if (!(value != null && value instanceof RtcWorkItemType)) {
            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            value = wi.getType();
        }
    }

    @Override
    protected void setValueInvoked(RtcWorkItemType value) {
        getLookup().lookup(RtcWorkItem.class).setValue(this, value.getId());
    }

    @Override
    public Type getAttributeType() {
        return Type.TYPE;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static IconProvider getIconProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<RtcWorkItemType>, IconProvider<RtcWorkItemType> {

        @Override
        public String toString(RtcWorkItemType value) {
            return value.getDisplayName();
        }

        @Override
        public Image getIcon(RtcWorkItemType value) {
            return value.getIcon();
        }
    }

    public class WorkingCopyTypeListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
