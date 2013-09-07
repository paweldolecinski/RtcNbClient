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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.approvals.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;

import com.ibm.team.workitem.common.model.IApproval;
import com.ibm.team.workitem.common.model.IApprovalDescriptor;
import com.ibm.team.workitem.common.model.IApprovals;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcApprovalDescriptorImpl implements RtcApprovalDescriptor {

    private final IApprovalDescriptor descriptor;
    private final IApprovals approvals;
    private List<RtcApproval> approvalsList = new ArrayList<RtcApproval>();
	private final ActiveProjectAreaImpl area;

    RtcApprovalDescriptorImpl(IApprovalDescriptor descriptor, IApprovals approvals, ActiveProjectAreaImpl area ) {
        this.descriptor = descriptor;
        this.approvals = approvals;
		this.area = area;
    }

    @Override
    public String getName() {
        return descriptor.getName();
    }

    @Override
    public RtcApprovalType getType() {
        return RtcApprovalsImpl.getType(descriptor.getTypeIdentifier());
    }

    @Override
    public Timestamp getDueDate() {
        return descriptor.getDueDate();
    }

    @Override
    public RtcApprovalState getState() {
        return RtcApprovalsImpl.getState(descriptor.getCumulativeStateIdentifier());
    }

    public IApprovalDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public void setType(RtcApprovalType type) {
        descriptor.setTypeIdentifier(type.getIdentifier());
    }

    @Override
    public void setName(String name) {
        descriptor.setName(name);
    }

    @Override
    public void setDueDate(Timestamp date) {
        descriptor.setDueDate(date);
    }

    @Override
    public void addApproval(RtcApproval approval) {
        approvalsList.add((RtcApprovalContributorImpl) approval);
        approvals.add(((RtcApprovalContributorImpl) approval).getAproval());
    }

    @Override
    public RtcApproval createApproval(Contributor contributor) {
        IApproval newApproval = approvals.createApproval(descriptor, ((ContributorImpl) contributor).getIContributor());
        return new RtcApprovalContributorImpl(newApproval, this, area);
    }

    @Override
    public RtcApproval[] getApprovals() {
        if (approvalsList.isEmpty()) {
            List<IApproval> contents = approvals.getContents(descriptor);
            //System.out.println("Size: "+contents.size());
            for (IApproval a : contents) {
                RtcApprovalContributorImpl rtcApprovalContributorImpl = new RtcApprovalContributorImpl(a, this, area);
                approvalsList.add(rtcApprovalContributorImpl);
                //System.out.println("Added: "+rtcApprovalContributorImpl.getName());
            }
        }
        return approvalsList.toArray(new RtcApproval[]{});
    }

    @Override
    public void removeApproval(RtcApproval approval) {
        approvalsList.remove(approval);
        approvals.remove(((RtcApprovalContributorImpl) approval).getAproval());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isDescriptor() {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.approvalsList != null ? this.approvalsList.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!(obj instanceof RtcApprovalDescriptorImpl)) {
            return false;
        }
        final RtcApprovalDescriptorImpl other = (RtcApprovalDescriptorImpl) obj;
        if (this.approvalsList != other.approvalsList && (this.approvalsList == null || !this.approvalsList.equals(other.approvalsList))) {
            return false;
        }

        return true;
    }
}
