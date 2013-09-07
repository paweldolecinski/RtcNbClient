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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items;

import com.ibm.team.apt.internal.client.PlanItem;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.workflow.WorkflowInfo;
import com.ibm.team.workitem.common.internal.workflow.WorkflowManager;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem.RtcPlanItemEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.RtcPlansManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanWorkItemImpl extends RtcPlanWorkItem {

    private PlanItem planItem; //cannot be used until rtc api doesn't work :(
    private String name;
    private Contributor owner;
    private RtcWorkItem workItem;
    private RtcPlan plan;
    private RtcPlanItem parent;
    private List<RtcPlanItem> children;
    private EventSourceSupport<RtcPlanItem.RtcPlanItemEvent> eventSource = new EventSourceSupport<RtcPlanItemEvent>();

    RtcPlanWorkItemImpl(PlanItem planItem, RtcPlan plan) {
        this.planItem = planItem;
        this.plan = plan;
        //this.workItem = planItem.getWorkItem(true);
    }

    RtcPlanWorkItemImpl(RtcWorkItem workItem, RtcPlan plan) {
        this.workItem = workItem;
        this.plan = plan;
        this.name = workItem.getDisplayName();
        this.owner = workItem.getOwner();
    }

    @Override
    public RtcWorkItem getWorkItem() {
        return workItem;
    }

    @Override
    public boolean isResolved() {
        ITeamRepository repo = ((ActiveProjectAreaImpl) plan.getPlansManager().getActiveProjectArea()).getITeamRepository();
        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
        IWorkItem wi = ((RtcWorkItemImpl) workItem).getWorkItem();
        try {
            IWorkflowInfo iwi = ((WorkflowManager) workItemClient.getWorkflowManager()).getWorkflowInfo(wi, null);
            if (iwi.getStateGroup(wi.getState2()) == WorkflowInfo.CLOSED_STATES) {
                return true;
            }
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanWorkItemImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    @Override
    public RtcPlanItemType getPlanItemType() {
        if (((RtcPlansManagerImpl) plan.getPlansManager()).fetchTopLevelWorkItemsTypes().contains(workItem.getType().getId())) {
            return RtcPlanItemType.NON_EXECUTABLE;
        }
        return RtcPlanItemType.EXECUTABLE;
    }

    @Override
    public RtcPlanItem[] getChildItems() {
        if (children == null) {
            children = new ArrayList<RtcPlanItem>();
            RtcWorkItem[] childs = workItem.getChildren();
            for (RtcWorkItem child : childs) {
                children.add(new RtcPlanWorkItemImpl(child, plan));
            }
        }
        return children.toArray(new RtcPlanItem[]{});
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz) {
        return (T[]) new RtcPlanItem[]{};
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Contributor getOwner() {
        return owner;
    }

    @Override
    public String getPlanItemIdentifier() {
        return Integer.toString(workItem.getId());
    }

    @Override
    public RtcPlanItem getParentItem() {
        if (parent == null) {
            RtcWorkItem parent1 = workItem.getParent();
            parent = parent1 != null ? new RtcPlanWorkItemImpl(parent1, plan) : null;
        }
        return parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPlanAttributeValue(RtcPlanItemAttribute<T> attribute) {
        if (attribute != null) {
            if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.complexity")) {
            } else if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.progress")) {
                return (T) new RtcProgressAttribute(this).getProgressText();
            } else if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.state")) {
                Object value = workItem.getWorkFlowAction();
                if (value != null) {
                    return (T) value;
                }
            } else {

                Object value = workItem.getValue(attribute);

                if (value != null) {
                    return (T) value;
                }
            }
            return attribute.getNullValue();
        }
        return null;
    }

    @Override
    public <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value) throws RtcIllegalPlanAttributeValue {
        if (attribute != null) {
            if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.complexity")) {
            } else if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.progress")) {
                //Shouldn't happens, progress is read only
            } else if (attribute.getAttributeIdentifier().equals("com.ibm.team.apt.attribute.planitem.state")) {
            } else {
                workItem.setValue(attribute, value);
            }
        }
        dirty = true;
        fireAttributeValueChanged(attribute, value, value);
        fireEvent(RtcPlanItemEvent.ATTRIBUTE_VALUE_CHANGED);
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
    }

    private static class RtcProgressAttribute {

        private final RtcPlanWorkItemImpl planItem;

        private RtcProgressAttribute(RtcPlanWorkItemImpl planItem) {
            this.planItem = planItem;
        }

        String getProgressText() {
            if (planItem.getPlanItemType() == RtcPlanItemType.NON_EXECUTABLE) {
                return getValue().getDoneUnits() + "/" + getValue().getPlannedUnits() + " h";
            }
            return "--";
        }

        private RtcProgressInfo getValue() {
            RtcProgressInfo progressInfo = planItem.getPlan().getPlansManager().getProgressInfo(planItem.getChildItems(RtcPlanWorkItem.class), planItem.getPlan().getComplexityComputator());
            return progressInfo;
        }
    }

    public final void removeListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.addListener(listener);
    }
}
