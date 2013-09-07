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
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.workflow.WorkflowManager;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowInfo;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkFlowInfoImpl implements RtcWorkFlowInfo {

    private IWorkItem wi;
    private IWorkItemClient workItemClient;
    private IWorkflowInfo flow = null;

    public RtcWorkFlowInfoImpl(IWorkItem wi) {
        this.wi = wi;
    }

    public RtcWorkFlowInfoImpl(IWorkflowInfo flow) {
        this.flow = flow;
    }

    public IWorkflowInfo getWorkFlowInfo() {
        if (flow != null) {
            return flow;
        } else if (wi != null) {
            try {
                flow = ((WorkflowManager) getWorkItemClient().getWorkflowManager()).getWorkflowInfo(wi, null);
                return flow;
            } catch (TeamRepositoryException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return null;
    }

    private IWorkItemClient getWorkItemClient() {
        if (workItemClient == null) {
            workItemClient = (IWorkItemClient) ((ITeamRepository) wi.getOrigin()).getClientLibrary(IWorkItemClient.class);
        }
        return workItemClient;
    }
}
