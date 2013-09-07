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

import com.ibm.team.repository.common.TeamRepositoryException;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemCategory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.AttributesManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.repository.common.UUID;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 * @author Pawel Dolecinski
 * 
 */
public class WorkItemManagerImpl implements RtcWorkItemManager {

    private final ActiveProjectAreaImpl area;
    private IWorkItemClient clientLibrary;
    private Map<UUID, WorkItemImpl> wiMaps = new HashMap<UUID, WorkItemImpl>();
    private Map<RtcWorkItem, WorkItemCopyImpl> wiCopies = new HashMap<RtcWorkItem, WorkItemCopyImpl>();
    private Map<String, WorkItemCategoryImpl> categories = new HashMap<String, WorkItemCategoryImpl>();
    private WorkItemCategoryImpl[] categoriesCache;
    private AttributesManager attrsManager;

    WorkItemManagerImpl(ActiveProjectAreaImpl rtcActiveProjectAreaImpl) {

        this.area = rtcActiveProjectAreaImpl;
        this.attrsManager = area.getLookup().lookup(AttributesManager.class);

        clientLibrary = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #getWorkingCopy
     * (pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem)
     */
    @Override
    public WorkItemCopyImpl getWorkingCopy(RtcWorkItem wi) {
        WorkItemCopyImpl copy = new WorkItemCopyImpl(wi, this);
        wiCopies.put(wi, copy);
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #createWorkItem()
     */
    @Override
    public RtcWorkItemWorkingCopy createWorkItem() {
        WorkItemCopyImpl copy = new WorkItemCopyImpl(this);
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #getWorkItemTypes()
     */
    @Override
    public RtcWorkItemType[] getWorkItemTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #getWorkItemCategories()
     */
    @Override
    public RtcWorkItemCategory[] getWorkItemCategories() {
        if (categoriesCache == null) {
            try {
                List<ICategory> tab = clientLibrary.findCategories(area.getProjectArea().getIProcessArea(), ICategory.DEFAULT_PROFILE, null);
                ArrayList<WorkItemCategoryImpl> result = new ArrayList<WorkItemCategoryImpl>(tab.size());
                for (ICategory c : tab) {
                    result.add(findWorkItemCategory(c));
                }
                categoriesCache = result.toArray(new WorkItemCategoryImpl[]{});
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(WorkItemManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                throw new IllegalStateException(ex);
            }
        }
        return categoriesCache;
    }

    public WorkItemCategoryImpl findWorkItemCategory(ICategoryHandle handle) {
        if (!categories.containsKey(handle.getItemId().getUuidValue())) {
            WorkItemCategoryImpl impl = new WorkItemCategoryImpl(handle);
            categories.put(handle.getItemId().getUuidValue(), impl);
        }
        return categories.get(handle.getItemId().getUuidValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #getBuiltInAttributes()
     */
    @Override
    public RtcWorkItemAttribute<?>[] getBuiltInAttributes() {
        assert (!EventQueue.isDispatchThread());
        return attrsManager.getBuiltInAttributes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager
     * #getAttributes()
     */
    @Override
    public RtcWorkItemAttribute<?>[] getAttributes() {
        assert (!EventQueue.isDispatchThread());
        return attrsManager.getAttributes();
    }

    @Override
    public ActiveProjectArea getProjectArea() {
        return area;
    }

    public RtcWorkItem getWorkItem(IWorkItemHandle workitem) {
        if (wiMaps.containsKey(workitem.getItemId())) {
            return wiMaps.get(workitem.getItemId());
        }
        WorkItemImpl wi = new WorkItemImpl(workitem, this);
        wiMaps.put(workitem.getItemId(), wi);
        return wi;
    }

    public IWorkItemClient getClientLibrary() {
        return clientLibrary;
    }
}
