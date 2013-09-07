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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalContributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;

import com.ibm.team.workitem.common.model.IApproval;

/**
 * 
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcApprovalContributorImpl implements RtcApprovalContributor {
	private final IApproval approval;
	private final RtcApprovalDescriptor parent;
	private Contributor approver;
	private final ActiveProjectAreaImpl area;

	RtcApprovalContributorImpl(IApproval approval,
			RtcApprovalDescriptor parent, ActiveProjectAreaImpl area) {
		this.approval = approval;
		this.parent = parent;
		this.area = area;

	}

	@Override
	public RtcApprovalState getState() {
		return RtcApprovalsImpl.getState(approval.getStateIdentifier());
	}

	@Override
	public Contributor getApprover() {
		if (approver == null) {
			ContributorManagerImpl lookup = (ContributorManagerImpl) area
					.getLookup().lookup(ContributorManager.class);
			approver = lookup.findContributor(approval.getApprover());
		}

		return approver;
	}

	@Override
	public RtcApprovalDescriptor getDescriptor() {
		return parent;
	}

	public IApproval getAproval() {
		return approval;
	}

	@Override
	public void setState(RtcApprovalState state) {
		approval.setStateIdentifier(state.getIdentifier());
	}

	@Override
	public String toString() {
		return getApprover().getName();
	}

	@Override
	public String getName() {
		return getApprover().getName();
	}

	@Override
	public boolean isDescriptor() {
		return false;
	}

	@Override
	public RtcApproval[] getApprovals() {
		return new RtcApproval[] {};
	}

	@Override
	public RtcApprovalType getType() {
		return RtcApprovalsImpl.CONTRIBUTOR_TYPE;
	}

	@Override
	public Timestamp getDueDate() {
		return null;
	}

	@Override
	public void addApproval(RtcApproval approval) {
	}

	@Override
	public void removeApproval(RtcApproval approval) {
	}

}
