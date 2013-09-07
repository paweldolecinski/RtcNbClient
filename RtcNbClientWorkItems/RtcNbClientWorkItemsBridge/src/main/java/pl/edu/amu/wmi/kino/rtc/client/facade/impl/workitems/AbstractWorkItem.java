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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.SimpleAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemTypeImpl;

import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.internal.util.DelegatingItemList;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IDeliverableHandle;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.Identifier;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;

public abstract class AbstractWorkItem implements RtcWorkItem {

    protected WorkItemManagerImpl workitemManager;
    protected EventSourceSupport<RtcWorkItem.RtcWorkItemEvent> eventSource = new EventSourceSupport<RtcWorkItemEvent>();
    protected ProcessManagerImpl processManager;
    protected ContributorManagerImpl contributorManager;

    /*package*/
    AbstractWorkItem(WorkItemManagerImpl manager) {
        this.workitemManager = manager;
        processManager = (ProcessManagerImpl) manager.getProjectArea().getLookup().lookup(ProcessManager.class);
        contributorManager = (ContributorManagerImpl) manager.getProjectArea().getLookup().lookup(ContributorManager.class);
    }

    protected ActiveProjectArea getArea() {
        return workitemManager.getProjectArea();
    }

    @Override
    public RtcWorkItemManager getManager() {
        return workitemManager;
    }

    protected EventSourceSupport<RtcWorkItem.RtcWorkItemEvent> getEventSource() {
        return eventSource;
    }

    /**
     * 
     * @return
     */
    protected IAuditableCommon getAuditableClient() {
        return workitemManager.getClientLibrary().getAuditableCommon();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem#hasAttribute
     * (
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute)
     */
    @Override
    public <T> boolean hasAttribute(RtcWorkItemAttribute<T> attr) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem#getValue
     * (pl
     * .edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute)
     */
    @Override
    public <T> T getValue(RtcWorkItemAttribute<T> attribute) {

        assert attribute instanceof SimpleAttribute;
        IAttribute attr = ((SimpleAttribute<T>) attribute).getIAttribute();
        if (attr == null) {
            return null;
        }
        Object value = getIWorkItem().getValue(attr);
        Class<T> clazz = attribute.getValueType();
        Object ret = null;
        if (AttributeTypes.DURATION.equals(attr.getAttributeType())) {
            ret = value;
        } // Enums
        else if (AttributeTypes.TYPE.equals(attr.getAttributeType())) {
            ret = new RtcWorkItemTypeImpl(getIWorkItem());
        } else if (value instanceof ICategoryHandle) {
            ret = workitemManager.findWorkItemCategory((ICategoryHandle) value);
        } else if (value instanceof Identifier) {
            Identifier<?> identifier = (Identifier<?>) value;
            @SuppressWarnings("unchecked")
            PrefferedValues<RtcLiteral> lookup = attribute.getLookup().lookup(
                    PrefferedValues.class);
            if (lookup == null) {
                return null;
            }
            ValueProvider.Value<RtcLiteral>[] possibleValues = lookup.getValues();

            for (ValueProvider.Value<RtcLiteral> val : possibleValues) {
                RtcLiteral iLiteral = val.getValue();
                if (iLiteral.getId().equals((identifier).getStringIdentifier())) {
                    ret = iLiteral;
                }
            }
        } // Deliverable
        else if (AttributeTypes.DELIVERABLE.equals(attr.getAttributeType())) {
            ret = processManager.findDeliverable((IDeliverableHandle) value);
        } // Iteration
        else if (AttributeTypes.ITERATION.equals(attr.getAttributeType())) {
            ret = processManager.findIteration((IIterationHandle) value);
        } else if (AttributeTypes.CONTRIBUTOR.equals(attr.getAttributeType())) {
            ret = contributorManager.findContributor((IContributorHandle) value);
        } // Items types like contributors, project and team areas, etc.
        else if (value instanceof DelegatingItemList) {
            List<ContributorImpl> res = new ArrayList<ContributorImpl>();
            @SuppressWarnings("unchecked")
            DelegatingItemList<IItemHandle> list = (DelegatingItemList<IItemHandle>) getIWorkItem().getValue(attr);
            for (IItemHandle object : list) {
                if (object instanceof IContributorHandle) {
                    res.add(contributorManager.findContributor((IContributorHandle) object));
                }
            }
            ret = res;
        } // Standard types like int, string, timestamp, etc.
        else if (value instanceof Timestamp) {
            ret = new Date(((Timestamp) value).getTime());
        } else if (value != null) {
            ret = value;
        }
        // FIXME !!! class cast exception !!!
        try {
            return clazz.cast(ret);
        } catch (ClassCastException ex) {
            //System.out.println("jest class cast ex dla atrybutu "
            //        + attr.getAttributeType() + " | "
            //        + attribute.getDisplayName());
            RtcLogger.getLogger(AbstractWorkItem.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            return null;
        }

    }

    public abstract IWorkItem getIWorkItem();

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.api.common.RtcEventSource#addListener(
     * pl.edu.amu.wmi.kino.rtc.client.api.common.EventListener)
     */
    @Override
    public void addListener(EventListener<RtcWorkItemEvent> listener) {
        eventSource.addListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.api.common.RtcEventSource#removeListener
     * (pl.edu.amu.wmi.kino.rtc.client.api.common.EventListener)
     */
    @Override
    public void removeListener(EventListener<RtcWorkItemEvent> listener) {
        eventSource.removeListener(listener);
    }

    private final void fireEvent(RtcWorkItemEvent event) {
        eventSource.fireEvent(event);
    }
}
