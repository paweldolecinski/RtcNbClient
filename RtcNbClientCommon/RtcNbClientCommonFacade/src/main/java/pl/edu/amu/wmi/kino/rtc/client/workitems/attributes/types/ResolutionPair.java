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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types;

import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowResolution;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents pair: workflow - resolutions list for workflow
 * @author Pawel Dolecinski
 */
public class ResolutionPair {

    private String workFlowName;
    private ArrayList<RtcWorkFlowResolution> resolutionList = new ArrayList<RtcWorkFlowResolution>();

    /**
     *
     * @param workFlowName
     */
    public ResolutionPair(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    /**
     *
     * @return
     */
    public List<RtcWorkFlowResolution> getStatesList() {
        return resolutionList;
    }

    /**
     *
     * @return
     */
    public String getWorkFlowName() {
        return workFlowName;
    }

    /**
     * 
     * @param resolution
     */
    public void addState(RtcWorkFlowResolution resolution) {
        resolutionList.add(resolution);
    }
}
