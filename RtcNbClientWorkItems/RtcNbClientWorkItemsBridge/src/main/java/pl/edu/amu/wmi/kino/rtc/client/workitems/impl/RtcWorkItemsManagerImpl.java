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
package pl.edu.amu.wmi.kino.rtc.client.workitems.impl;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkItemsManagerImpl extends RtcWorkItemsManager {

    private final ActiveProjectAreaImpl area;
    private IWorkItemClient clientLibrary;
    private final Map<Integer, RtcWorkItem> toRtcWorkItem = Collections.synchronizedMap(new HashMap<Integer, RtcWorkItem>());

    RtcWorkItemsManagerImpl(ActiveProjectAreaImpl rtcActiveProjectAreaImpl) {
        this.area = rtcActiveProjectAreaImpl;
        clientLibrary = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);

    }

    @Override
    public RtcWorkItem[] getWorkItemsFor(Iteration iteration, ProcessArea owner) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param workItemId
     * @return
     */
    @Override
    public RtcWorkItem findWorkItem(int workItemId) {
        synchronized (toRtcWorkItem) {
            RtcWorkItem wi = toRtcWorkItem.get(workItemId);
            if (wi != null) {
                return wi;
            }
            try {
                IWorkItem findWorkItemById = clientLibrary.findWorkItemById(workItemId, IWorkItem.SMALL_PROFILE, null);
                RtcWorkItemImpl rtcWorkItemImpl = new RtcWorkItemImpl(findWorkItemById, area);
                toRtcWorkItem.put(workItemId, rtcWorkItemImpl);
                return rtcWorkItemImpl;
            } catch (TeamRepositoryException ex) {
                Exceptions.printStackTrace(ex);
            }

        }
        return null;
    }

    /**
     *
     * @param workItemIds
     * @return
     */
    @Override
    public RtcWorkItem[] findWorkItems(Integer[] workItemIds) {
        List<RtcWorkItem> res = new ArrayList<RtcWorkItem>();
        for (int i = 0; i < workItemIds.length; i++) {
            res.add(findWorkItem(workItemIds[i]));
        }
        return res.toArray(new RtcWorkItem[]{});
    }

    /**
     *
     * @param teamArea
     * @param type
     * @return
     */
    @Override
    public RtcWorkItem createWorkItem(ProcessArea teamArea, RtcWorkItemType type) {
        return new RtcWorkItemImpl(teamArea, type, clientLibrary, this.area);
    }

    @Override
    public RtcWorkItemType[] getWorkItemTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void synchronizeWithServer() {
    }

    @Override
    public ActiveProjectArea getActiveProjectArea() {
        return area;
    }
}
