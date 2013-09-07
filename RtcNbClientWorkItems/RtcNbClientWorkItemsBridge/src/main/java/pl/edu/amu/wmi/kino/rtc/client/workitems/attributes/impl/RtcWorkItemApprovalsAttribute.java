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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovals;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;

import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkingCopyEvent;

/**
 * @author Pawel Dolecinski
 */
public class RtcWorkItemApprovalsAttribute extends RtcWorkItemAttribute<RtcApproval> {

    private final InstanceContent ic;
    //this ensures, that I'll have ic instantinated on any jvm.
    //and it's pretty neat ;]
    private Lookup lookup = new AbstractLookup((ic = new InstanceContent()));

    public RtcWorkItemApprovalsAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkItem wi, RtcApprovals approvals) {
        super((wi == null ? null : approvals.getRoot()), valueType);
        ic.add(stringProvider);
        ic.add(approvals);
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
     * @param approvals
     * @param wi workItemWorkingCopy that is associated with this attribute - can be null
     * @param aditionalContextContent any other context that should be available in lookup
     */
    public RtcWorkItemApprovalsAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkItem wi, RtcApprovals approvals, Object... aditionalContextContent) {
        this(valueType, stringProvider, wi, approvals);
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
        return "approvals";
    }

    @Override
    public String getAttributeDisplayName() {
        return "Approvals";
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
            RtcApprovals a = lookup.lookup(RtcApprovals.class);
            value = a.getRoot();
        }
    }

    @Override
    protected void setValueInvoked(RtcApproval value) {
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public Type getAttributeType() {
        return Type.APPROVALS;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProvider();
    }

    public static class ToStringProvider {

        public String toString(RtcApproval value) {
            return "Approvals";
        }
    }

    public class WorkingCopyApprovalsListener implements IWorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
