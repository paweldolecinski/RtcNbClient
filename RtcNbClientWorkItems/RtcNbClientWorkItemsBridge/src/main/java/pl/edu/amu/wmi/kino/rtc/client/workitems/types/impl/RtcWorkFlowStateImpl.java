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

import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.Identifier;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowState;

/**
 * 
 * @author Pawel Dolecinski
 */
public class RtcWorkFlowStateImpl extends RtcWorkFlowState implements RtcWorkFlow{

    private final RtcWorkFlowInfoImpl workflow;
    private final Identifier<IState> state;

    /**
     * 
     * @param workflowInfo
     * @param state
     */
    public RtcWorkFlowStateImpl(RtcWorkFlowInfoImpl workflowInfo, Identifier<IState> state) {
        this.workflow = workflowInfo;
        this.state = state;
    }

    @Override
    public String getName() {
        return workflow.getWorkFlowInfo().getStateName(state);
    }

    @Override
    public int getStateGroup() {
        return workflow.getWorkFlowInfo().getStateGroup(state);
    }

    @Override
    public Image getIcon() {
        URL stateIconName = workflow.getWorkFlowInfo().getStateIconName(state);
        if (stateIconName != null) {
            return ImageUtilities.icon2Image(new ImageIcon(stateIconName));
        }
        return null;
    }

    /**
     * Intended for internal use
     * @return
     */
    public Identifier<IState> getIdentifier() {
        return state;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * 
     * @return
     */
    public RtcWorkFlowState getValue() {
        return this;
    }

    /**
     * 
     * @return
     */
    public String getKey() {
        return getName();
    }

    @Override
    public String getId() {
        return state.getStringIdentifier();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcWorkFlow) {
            RtcWorkFlow kv = (RtcWorkFlow) obj;
            return (kv.getId().equals(this.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.toString() != null ? this.toString().hashCode() : 0);
        return hash;
    }
}
