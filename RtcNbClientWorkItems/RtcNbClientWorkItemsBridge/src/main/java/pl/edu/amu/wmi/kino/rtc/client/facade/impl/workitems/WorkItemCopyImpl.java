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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IDetailedStatus;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemOperation;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.client.internal.WorkItemWorkingCopyImpl;
import com.ibm.team.workitem.client.internal.WorkItemWorkingCopyManager;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.WorkItemReferences;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.SimpleAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.references.WorkItemReferencesImpl;

/**
 * @author Paweł Doleciński
 * 
 */
public class WorkItemCopyImpl extends AbstractWorkItem implements
        RtcWorkItemWorkingCopy {

    protected Map<SimpleAttribute<?>, Object> changedAttributes = new HashMap<SimpleAttribute<?>, Object>();
    private WorkItemWorkingCopyManager copyManager;
    private WorkItemWorkingCopy workingCopy;
    protected WorkItemImpl rwi;
    protected WorkItemReferences references;

    /**
     * Create empty work item
     */
    public WorkItemCopyImpl(WorkItemManagerImpl manager) {
        super(manager);
    }

    /**
     * Create working copy for given {@link RtcWorkItem}
     * 
     * @param rwi
     * @param workitemManager
     */
    protected WorkItemCopyImpl(RtcWorkItem wi, WorkItemManagerImpl manager) {
        super(manager);
        assert wi instanceof WorkItemImpl;
        this.rwi = (WorkItemImpl) wi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy
     * #setValue(pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.
     * RtcWorkItemAttribute, java.lang.Object)
     */
    @Override
    public <T> void setValue(RtcWorkItemAttribute<T> attr, T val) {
        if (attr instanceof SimpleAttribute<?>) {
            changedAttributes.put((SimpleAttribute<?>) attr, val);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy
     * #save()
     */
    @Override
    public void save() {
        try {
            (new WorkItemOperation("", IWorkItem.FULL_PROFILE) {

                @Override
                protected void execute(WorkItemWorkingCopy workingCopy,
                        IProgressMonitor monitor)
                        throws TeamRepositoryException {
                    final IWorkItem workItem = workingCopy.getWorkItem();
                    for (Map.Entry<SimpleAttribute<?>, Object> entry : changedAttributes.entrySet()) {
                        IAttribute attr = entry.getKey().getIAttribute();
                        workItem.setValue(attr, entry.getValue());
                    }

                }
            }).run(getIWorkItem(), null);
        } catch (TeamRepositoryException e) {
            RtcLogger.getLogger(WorkItemCopyImpl.class).log(Level.SEVERE,
                    "Exception while saving", e);
        }
        if (workingCopy != null && workingCopy.isDirty()) {
            IDetailedStatus save = workingCopy.save(null);
            if (save.isOK()) {
                copyManager.disconnect(workingCopy.getWorkItem());
                ((WorkItemWorkingCopyImpl) workingCopy).saved(save);
                workingCopy = null;
            }
        }
        eventSource.fireEvent(RtcWorkItemEvent.WORK_ITEM_SAVED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy
     * #update()
     */
    @Override
    public void update() {
        if (workingCopy == null) {
            getWorkingCopy();
        }
        copyManager.refresh((IWorkItemHandle) workingCopy.getWorkItem().getItemHandle());
        //FIXME merge with local changes
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy
     * #discardChanges()
     */
    @Override
    public void discardChanges() {
        changedAttributes.clear();
        if (workingCopy != null) {
            copyManager.refresh((IWorkItemHandle) workingCopy.getWorkItem().getItemHandle());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy
     * #isDirty()
     */
    @Override
    public boolean isDirty() {
        if (workingCopy != null) {
            return !changedAttributes.isEmpty() || workingCopy.isDirty();
        }
        return !changedAttributes.isEmpty();
    }

    @Override
    public IWorkItem getIWorkItem() {
        return rwi.getIWorkItem();
    }

    private void getWorkingCopy() {
        try {
            IWorkItemClient wiclient = workitemManager.getClientLibrary();
            // first I have to connect to rwi and then i can get working copy.
            copyManager = (WorkItemWorkingCopyManager) wiclient.getWorkItemWorkingCopyManager();
            copyManager.connect(getIWorkItem(), IWorkItem.FULL_PROFILE, null);
            workingCopy = wiclient.getWorkItemWorkingCopyManager().getWorkingCopy(getIWorkItem());
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(WorkItemCopyImpl.class).log(Level.SEVERE,
                    ex.getLocalizedMessage(), ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // Hash code of WIWC cannot change, beacuse any HashMap containers will
        // break up.
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getIWorkItem() == null) ? 0 : getIWorkItem().getItemId().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RtcWorkItemWorkingCopy)) {
            return false;
        }
        WorkItemCopyImpl other = (WorkItemCopyImpl) obj;
        if (getIWorkItem() == null) {
            if (other.getIWorkItem() != null) {
                return false;
            }
        } else if (!getIWorkItem().getItemId().equals(
                other.getIWorkItem().getItemId())) {
            return false;
        }
        return true;
    }

    @Override
    public <T> T getValue(RtcWorkItemAttribute<T> attr) {

        if (rwi == null) {
            return attr.getDefaultValue();
        }
        if (changedAttributes.containsKey(attr)) {
            return attr.getValueType().cast(changedAttributes.get(attr));
        }
        return rwi.getValue(attr);
    }

    public WorkItemReferences getReferences() {
        if (references == null) {
            if (workingCopy == null) {
                getWorkingCopy();
            }
            references = new WorkItemReferencesImpl(
                    workitemManager.getProjectArea(), workingCopy);
        }
        return references;
    }
}
