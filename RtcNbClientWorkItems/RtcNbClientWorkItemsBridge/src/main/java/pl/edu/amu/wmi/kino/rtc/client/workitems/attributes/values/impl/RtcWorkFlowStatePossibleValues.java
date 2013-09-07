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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.ICombinedWorkflowInfos;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.StatePair;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowStateImpl;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkFlowStatePossibleValues implements RtcWorkItemAttributePossibleValues<StatePair>{
    private final ActiveProjectAreaImpl activeProjectArea;

    /**
     * 
     * @param activeProjectArea
     */
    public RtcWorkFlowStatePossibleValues(ActiveProjectAreaImpl activeProjectArea) {
        this.activeProjectArea = activeProjectArea;
    }

    @Override
    public List<StatePair> getPossibleValues() {
        List<StatePair> list = new ArrayList<StatePair>();
        IWorkItemClient clientLibrary = (IWorkItemClient) activeProjectArea.getITeamRepository().getClientLibrary(IWorkItemClient.class);
        try {
            ICombinedWorkflowInfos flows = clientLibrary.findCombinedWorkflowInfos(activeProjectArea.getProjectArea().getIProcessArea(), null);
            IWorkflowInfo[] workflowInfos = flows.getWorkflowInfos();
            for (IWorkflowInfo flow : workflowInfos) {
                StatePair pair = new StatePair(flow.getName());
                for (Identifier<IState> state : flow.getAllStateIds()) {
                    pair.addState(new RtcWorkFlowStateImpl(new RtcWorkFlowInfoImpl(flow), state));
                }
                list.add(pair);
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
        }
        finally
        {
            return list;
        }
        
    }

}
