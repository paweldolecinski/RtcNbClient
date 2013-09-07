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

import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryContent;

import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkingCopyEvent;

/**
 * this class shall be used to enclose IAttribute based attributes
 * @author Pawel Dolecinski
 */
public class RtcWorkItemHistoryAttribute extends RtcWorkItemAttribute<RtcHistoryContent> {

    private final InstanceContent ic;
    //this ensures, that I'll have ic instantinated on any jvm.
    //and it's pretty neat ;]
    private Lookup lookup = new AbstractLookup((ic = new InstanceContent()));

    public RtcWorkItemHistoryAttribute(Class<RtcHistoryContent> valueType, ToStringProvider stringProvider, RtcHistoryContent historyContent, RtcWorkItem wi) {
        super((wi == null ? null : historyContent), valueType);
        ic.add(stringProvider);
        ic.add(historyContent);
        ic.add(this);
        if (wi != null) {
            ic.add(wi);
        }
    }

    /**
     * this constructor can accept additional content to it's context - then
     * available in getLookup() of resulting object.
     * @param valueType class of value that will be available through this attribute
     * @param stringProvider stringProvider for the value type
     * @param historyContent 
     * @param wi workItemWorkingCopy that is associated with this attribute - can be null
     * @param aditionalContextContent any other context that should be available in lookup
     */
    public RtcWorkItemHistoryAttribute(Class<RtcHistoryContent> valueType, ToStringProvider stringProvider, RtcHistoryContent historyContent, RtcWorkItem wi, Object... aditionalContextContent) {
        this(valueType, stringProvider, historyContent, wi);
        for (int i = 0; i < aditionalContextContent.length; i++) {
            ic.add(aditionalContextContent[i]);
        }
    }

    @Override
    public String toString() {
        return lookup.lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public String getAttributeId() {
        return "history";
    }

    @Override
    public String getAttributeDisplayName() {
        return "History";
    }

    @Override
    public String getShortDesc() {
        return "";
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    protected void getValueInvoked() {
        if (value == null) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    RtcHistoryContent historyContent = lookup.lookup(RtcHistoryContent.class);
                    value = historyContent;
                }
            }).run();
        }
    }

    @Override
    protected void setValueInvoked(RtcHistoryContent value) {
    }

    @Override
    public boolean isWritable() {

        return false;
    }

    @Override
    public Type getAttributeType() {
        return Type.HISTORY;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProvider();
    }

    public static class ToStringProvider {

        public String toString(RtcHistoryContent value) {
            return "History";
        }
    }

    public class WorkingCopyHistoryListener implements IWorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
