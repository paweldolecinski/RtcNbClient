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

import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IResolution;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.IWorkflowAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowResolutionImpl;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkFlowResolutionPrefferedValues implements RtcWorkItemAttributePrefferedValues<RtcWorkFlow> {

    private final RtcWorkFlowInfoImpl workflow;
    private final WorkItemWorkingCopy workingCopy;

    public RtcWorkFlowResolutionPrefferedValues(RtcWorkFlowInfoImpl flow, WorkItemWorkingCopy workingCopy) {
        this.workflow = flow;
        this.workingCopy = workingCopy;
    }

    @Override
    public List<RtcWorkFlow> getPrefferedValues() {
        List<RtcWorkFlow> res = new ArrayList<RtcWorkFlow>();
        if (workingCopy.getWorkflowAction() != null) {
            Identifier<IResolution>[] availableResolutionIds = workflow.getWorkFlowInfo().getResolutionIds(Identifier.create(IWorkflowAction.class, workingCopy.getWorkflowAction()));
            if (availableResolutionIds.length > 0) {
                String[] ids = new String[availableResolutionIds.length];
                for (int i = 0; i < availableResolutionIds.length; i++) {
                    ids[i] = availableResolutionIds[i].getStringIdentifier();
                }
                Arrays.sort(ids);

                for (int i = 0; i < ids.length; i++) {
                    res.add(new RtcWorkFlowResolutionImpl(workflow, Identifier.create(IResolution.class, ids[i])));
                }
            }

        }
        return res;

    }

    @Override
    public void setConstraint(Object constraint) {
    }

    @Override
    public boolean isConstraint() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


