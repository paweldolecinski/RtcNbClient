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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.internal.WorkItemClient;
import com.ibm.team.workitem.common.internal.rcp.dto.ChangeLogDTO;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.ItemProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryContent;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryItem;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcHistoryContentImpl implements RtcHistoryContent {

    private List<ChangeLogDTO> computeWorkItemHistory;
    private final IWorkItem wi;
    private WorkItemClient workItemClient;
    private List<RtcHistoryItem> historyItems = new ArrayList<RtcHistoryItem>();

    public RtcHistoryContentImpl(IWorkItem wi) {
        this.wi = wi;
    }

    @Override
    public void computeHistory() {
        historyItems.clear();
        try {
            computeWorkItemHistory = getWorkItemClient().computeWorkItemHistory(wi, null);
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcHistoryContentImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        for (Iterator<ChangeLogDTO> it1 = computeWorkItemHistory.iterator(); it1.hasNext();) {
            ChangeLogDTO changeLogDTO = it1.next();
            try {
                IContributor modifiedBy = workItemClient.getAuditableCommon().resolveAuditable(changeLogDTO.getModifiedBy(), ItemProfile.CONTRIBUTOR_DEFAULT, null);
                historyItems.add(new RtcHistoryItem(changeLogDTO.getModifiedDate(), modifiedBy.getName(), changeLogDTO.getHtmlContent(), false));
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(RtcHistoryContentImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
    }

    public List<RtcHistoryItem> getHistory() {
        return historyItems;
    }

    private WorkItemClient getWorkItemClient() {
        if (workItemClient == null) {
            workItemClient = (WorkItemClient) ((ITeamRepository) wi.getOrigin()).getClientLibrary(IWorkItemClient.class);
        }
        return workItemClient;
    }
}
