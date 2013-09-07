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

import java.awt.Image;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcWorkFlowResolutionPrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowResolutionImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;

import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.model.IWorkItem;

/**
 *
 * @param <T>
 * @author Pawel Dolecinski
 */
public class RtcWorkItemWorkFlowResolutionAttribute extends RtcWorkItemAttribute<RtcWorkFlow> {

    private boolean writable;
    private final InstanceContent ic= new InstanceContent();
    //this ensures, that I'll have ic instantinated on any jvm.
    //and it's pretty neat ;]
    private Lookup lookup = new AbstractLookup(ic);

    public RtcWorkItemWorkFlowResolutionAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkFlow resolution, RtcWorkItem wi) {
        super(resolution, valueType);
        ic.add(stringProvider);
        ic.add(resolution);
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
     * @param resolution
     * @param workflowInfo workflowinfo that is associeted with work item type
     * @param workItem workItem that is associated with this attribute - can be null
     * @param aditionalContextContent any other context that should be available in lookup
     */
    public RtcWorkItemWorkFlowResolutionAttribute(Class valueType, ToStringProvider stringProvider, RtcWorkFlow resolution, RtcWorkItem workItem, Object... aditionalContextContent) {
        this(valueType, stringProvider, resolution, workItem);
        for (int i = 0; i < aditionalContextContent.length; i++) {
            ic.add(aditionalContextContent[i]);
        }
    }

    @Override
    public String toString() {

        return lookup.lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public Image getIcon() {
        return ((RtcWorkFlow) value).getIcon();
    }

    @Override
    public String getAttributeId() {
        return "internalResolution";
    }

    @Override
    public String getAttributeDisplayName() {
        return "Resolution";
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
            IWorkItem wi = lookup.lookup(IWorkItem.class);
            value = new RtcWorkFlowResolutionImpl(new RtcWorkFlowInfoImpl(wi), wi.getResolution2());
        }
    }

    @Override
    protected void fireEvent(RtcWorkItemAttributeEvents eventType) {
        super.fireEvent(eventType);
    }

    @Override
    protected void setValueInvoked(RtcWorkFlow value) {

        RtcWorkItemImpl wi = lookup.lookup(RtcWorkItemImpl.class);
        wi.setResolution((RtcWorkFlowResolutionImpl) value);
        
    }

    @Override
    public boolean isWritable() {
        //return true;
        return writable;
    }

    @Override
    public Type getAttributeType() {
        return Type.FLOW_RESOLUTION;
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProvider();
    }

    public static class ToStringProvider {

        public String toString(RtcWorkFlow value) {
            return value.getName();
        }
    }

    public class WorkingCopyActionListener implements IWorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
                IWorkItem wi = lookup.lookup(IWorkItem.class);
                value = new RtcWorkFlowResolutionImpl(new RtcWorkFlowInfoImpl(wi), wi.getResolution2());
            }
            if (wce.hasType(WorkingCopyEvent.WORKFLOW_ACTION_CHANGED)) {
                if (!getLookup().lookup(RtcWorkFlowResolutionPrefferedValues.class).getPrefferedValues().isEmpty()) {
                    writable = true;
                    RtcWorkItemWorkFlowResolutionAttribute.this.fireEvent(RtcWorkItemAttributeEvents.WORKFLOW_ACTION_CHANGED);
                }
            }

//            if ((event.hasType(WorkingCopyEvent.SAVED) || event.hasType(WorkingCopyEvent.REVERTED)) && fWorkingCopy != null) {
//				fCachedResolution= fWorkingCopy.getWorkItem().getResolution2();
//			}
//			if (event.hasType(WorkingCopyEvent.WORKFLOW_ACTION_CHANGED)) {
//				if (fWorkingCopy != null && fWorkingCopy.getWorkflowAction() == null && fWorkingCopy.isDirty()) {
//					fWorkingCopy.getWorkItem().setResolution2(fCachedResolution);
//				}
//				updateResolutionValueSet();
//			}
        }
    }
}
