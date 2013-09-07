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
package pl.edu.amu.wmi.kino.rtc.client.workitems.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Class respresents pair: workflow - states list in workflow
 * @author Pawel Dolecinski
 */
public class StatePair {

    private String workFlowName;
    private List<RtcWorkFlowState> statesList = new ArrayList<RtcWorkFlowState>();

    /**
     * Workflow is a kind of root for states from one family
     * @param workFlowName which is a name of workflow
     */
    public StatePair(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    /**
     * 
     * @return all possible states for associated workflow
     */
    public List<RtcWorkFlowState> getStatesList() {
        return statesList;
    }

    /**
     * 
     * @return workflow name
     */
    public String getWorkFlowName() {
        return workFlowName;
    }

    /**
     * 
     * @param state 
     */
    public void addState(RtcWorkFlowState state)
    {
        statesList.add(state);
    }

}
