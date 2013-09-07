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

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcAttachment;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcAttachments;

import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkingCopyEvent;

/**
 * @author Pawel Dolecinski
 */
public class RtcAttachmentsAttribute extends RtcWorkItemAttribute<Collection<RtcAttachment>> {

    private final InstanceContent ic;
    //this ensures, that I'll have ic instantinated on any jvm.
    //and it's pretty neat ;]
    private Lookup lookup = new AbstractLookup((ic = new InstanceContent()));

    public RtcAttachmentsAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkItem workItem, RtcAttachments attachments) {
        super((workItem.getAttachments()), valueType);
        ic.add(stringProvider);
        ic.add(attachments);
        ic.add(workItem);
        ic.add(this);
    }

    /**
     * this constructor can accept additional content to it's context - then
     * available in getLookup() of resulting object.
     * @param valueType class of value that will be available through this attribute
     * @param stringProvider stringProvider for the value type
     * @param iAttribute IAttribute that is a base of this attribute
     * @param wi workItemWorkingCopy that is associated with this attribute - can be null
     * @param aditionalContextContent any other context that should be available in lookup
     */
    public RtcAttachmentsAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkItem workItem, RtcAttachments attachments, Object... aditionalContextContent) {
        this(valueType, stringProvider, workItem, attachments);
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
        return "attachments";
    }

    @Override
    public String getAttributeDisplayName() {
        return "Attachments";
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
            RtcWorkItem wc = lookup.lookup(RtcWorkItem.class);
            value = wc.getAttachments();
        }
    }

    @Override
    protected void setValueInvoked(Collection<RtcAttachment> value) {
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public Type getAttributeType() {
        return Type.ATTACHMENTS;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProvider();
    }

    public static class ToStringProvider {

        public String toString(Collection<RtcAttachment> value) {
            return "Attachments";
        }
    }

    public class WorkingCopyAttachmentsListener implements IWorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                getValueInvoked();
            }
        }
    }
}
