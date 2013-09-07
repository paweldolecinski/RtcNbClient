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

import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.IWorkflowAction;
import java.util.ArrayList;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowAction;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowActionImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowStateImpl;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkFlowActionPrefferedValues implements RtcWorkItemAttributePrefferedValues<RtcWorkFlowAction> {

    private final RtcWorkFlowInfoImpl workflow;
    private RtcWorkFlowStateImpl constraint = null;

    public RtcWorkFlowActionPrefferedValues(RtcWorkFlowInfoImpl flow) {
        this.workflow = flow;
    }

    @Override
    public boolean isConstraint() {
        if (this.constraint != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<RtcWorkFlowAction> getPrefferedValues() {
        List<RtcWorkFlowAction> list = new ArrayList<RtcWorkFlowAction>();
        Identifier<IWorkflowAction>[] actionIds = workflow.getWorkFlowInfo().getActionIds(constraint.getIdentifier());
        for (Identifier<IWorkflowAction> identifier : actionIds) {
            list.add(new RtcWorkFlowActionImpl(workflow.getWorkFlowInfo(), identifier));
        }
        return list;
    }

    @Override
    public void setConstraint(Object constraint) {
        if (constraint instanceof RtcWorkFlowStateImpl) {
            this.constraint = (RtcWorkFlowStateImpl) constraint;
        }
    }
}
