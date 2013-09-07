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
import com.ibm.team.workitem.common.model.Identifier;
import java.awt.Image;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcEnumerationPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcLiteralImpl;

/**
 *
 * @author michu
 */
public class RtcWorkItemEnumerationAttribute extends RtcWorkItemIAttribute<RtcLiteral> {

    public RtcWorkItemEnumerationAttribute(Class<RtcLiteral> valueType, ToStringProvider<RtcLiteral> stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        super(valueType, stringProvider, iAttribute, wi, aditionalContextContent);
        getValueInvoked();
    }

    @Override
    protected final void getValueInvoked() {

        if (!(value != null && value instanceof RtcLiteral)) {
            RtcWorkItem wi = getLookup().lookup(RtcWorkItem.class);
            value = (RtcLiteral) wi.getValue(this);
        }
    }

    @Override
    protected void setValueInvoked(RtcLiteral value) {

        List<RtcLiteral> possibleValues =
                getLookup().lookup(RtcEnumerationPossibleValues.class).getPossibleValues();

        for (RtcLiteral iLiteral : possibleValues) {
            if (iLiteral.equals(value)) {
                Identifier identifier = ((RtcLiteralImpl) value).getLiteral().getIdentifier2();
                getLookup().lookup(RtcWorkItem.class).setValue(this, identifier);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return getLookup().lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public Type getAttributeType() {
        return Type.ENUMERATION;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProviderImpl();
    }

    public static IconProvider getIconProvider() {
        return new ToStringProviderImpl();
    }

    public static class ToStringProviderImpl implements ToStringProvider<RtcLiteral>, IconProvider<RtcLiteral> {

        @Override
        public String toString(RtcLiteral value) {
            return value.getName();
        }

        @Override
        public Image getIcon(RtcLiteral value) {
            return value.getIcon();
        }
    }

    public class WorkingCopyEnumerationListener extends WorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
