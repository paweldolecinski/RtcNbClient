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

import com.ibm.team.workitem.common.model.IApprovalDescriptor;
import com.ibm.team.workitem.common.model.IApprovals;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.WorkItemApprovals;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovals;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcApprovalsImpl extends RtcApprovals {

    public static final RtcApprovalType APPROVAL_TYPE = new RtcApprovalTypeImpl(WorkItemApprovals.APPROVAL_TYPE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_generic.gif");
    public static final RtcApprovalType REVIEW_TYPE = new RtcApprovalTypeImpl(WorkItemApprovals.REVIEW_TYPE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_review.gif");
    public static final RtcApprovalType VERIFICATION_TYPE = new RtcApprovalTypeImpl(WorkItemApprovals.VERIFICATION_TYPE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_verification.gif");
    public static final RtcApprovalType CONTRIBUTOR_TYPE = new RtcApprovalTypeImpl(null, null);
    public static final RtcApprovalState APPROVED_STATE = new RtcApprovalStateImpl(WorkItemApprovals.APPROVED_STATE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_approved.gif");
    public static final RtcApprovalState PENDING_STATE = new RtcApprovalStateImpl(WorkItemApprovals.PENDING_STATE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_pending.gif");
    public static final RtcApprovalState REJECTED_STATE = new RtcApprovalStateImpl(WorkItemApprovals.REJECTED_STATE, "pl/edu/amu/wmi/kino/rtc/client/workitems/icons/appr_rejected.gif");
    private static List<RtcApprovalType> types = new ArrayList<RtcApprovalType>();
    private static Map<String, RtcApprovalType> typesMap = new HashMap<String, RtcApprovalType>();
    private static List<RtcApprovalState> states = new ArrayList<RtcApprovalState>();
    private static Map<String, RtcApprovalState> statesMap = new HashMap<String, RtcApprovalState>();

    static {
        types.add(APPROVAL_TYPE);
        types.add(REVIEW_TYPE);
        types.add(VERIFICATION_TYPE);
        types.add(CONTRIBUTOR_TYPE);
        typesMap.put(APPROVAL_TYPE.getIdentifier(), APPROVAL_TYPE);
        typesMap.put(REVIEW_TYPE.getIdentifier(), REVIEW_TYPE);
        typesMap.put(VERIFICATION_TYPE.getIdentifier(), VERIFICATION_TYPE);
        typesMap.put(CONTRIBUTOR_TYPE.getIdentifier(), CONTRIBUTOR_TYPE);
        states.add(APPROVED_STATE);
        states.add(PENDING_STATE);
        states.add(REJECTED_STATE);
        statesMap.put(APPROVED_STATE.getIdentifier(), APPROVED_STATE);
        statesMap.put(PENDING_STATE.getIdentifier(), PENDING_STATE);
        statesMap.put(REJECTED_STATE.getIdentifier(), REJECTED_STATE);
    }
    private final IWorkItem wi;
    private IApprovals approvals;
	private ActiveProjectAreaImpl area;

    public RtcApprovalsImpl(IWorkItem wi, ActiveProjectAreaImpl area) {
        this.wi = wi;
		this.area = area;
    }

    @Override
    public RtcApproval getRoot() {
        if (approvals == null) {
            injectIApprovals();
        }
        RtcApproval root = new RtcApprovalImpl(approvals, area);
        return root;
    }

    @Override
    public RtcApprovalDescriptor createDescriptor(RtcApprovalType type, String name) {
        if (approvals == null) {
            injectIApprovals();
        }
        IApprovalDescriptor newDescriptor = approvals.createDescriptor(type.getIdentifier(), name);
        return new RtcApprovalDescriptorImpl(newDescriptor, approvals, area);
    }

    @Override
    public List<RtcApprovalType> getPossibleTypes() {
        return Collections.unmodifiableList(types);
    }

    @Override
    public List<RtcApprovalState> getPossibleStates() {
        return Collections.unmodifiableList(states);
    }

    static public RtcApprovalType getType(String typeId) {
        return typesMap.get(typeId);
    }

    static public RtcApprovalState getState(String stateId) {
        return statesMap.get(stateId);
    }

    private void injectIApprovals() {
        approvals = wi.getApprovals();
    }

    private static class RtcApprovalImpl implements RtcApproval {

        private final IApprovals approvals;
        private ArrayList<RtcApproval> approvalsList = new ArrayList<RtcApproval>();
		private final ActiveProjectAreaImpl area2;

        private RtcApprovalImpl(IApprovals approvals, ActiveProjectAreaImpl area) {
            this.approvals = approvals;
			area2 = area;
        }

        @Override
        public String getName() {
            return "RtcApprovalRoot"; //NOI18L
        }

        @Override
        public boolean isDescriptor() {
            return true;
        }

        @Override
        public RtcApproval[] getApprovals() {
            if (approvalsList.isEmpty()) {
                for (IApprovalDescriptor iDesc : approvals.getDescriptors()) {
                    approvalsList.add(new RtcApprovalDescriptorImpl(iDesc, approvals, area2));
                }
            }
            return approvalsList.toArray(new RtcApproval[]{});
        }

        @Override
        public RtcApprovalState getState() {
            return APPROVED_STATE;
        }

        @Override
        public RtcApprovalType getType() {
            return APPROVAL_TYPE;
        }

        @Override
        public Timestamp getDueDate() {
            return null;
        }

        @Override
        public void addApproval(RtcApproval approval) {
            if (approval instanceof RtcApprovalDescriptor) {
                approvalsList.add(((RtcApprovalDescriptor) approval));
                approvals.add(((RtcApprovalDescriptorImpl) approval).getDescriptor());
            }
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
            if (!(obj instanceof RtcApprovalImpl)) {
                return false;
            }
            final RtcApprovalImpl other = (RtcApprovalImpl) obj;
            if (this.approvalsList != other.approvalsList && (this.approvalsList == null || !this.approvalsList.equals(other.approvalsList))) {
                return false;
            }

            return true;
        }

        @Override
        public void removeApproval(RtcApproval approval) {
            if (approval instanceof RtcApprovalDescriptor) {
                approvalsList.remove(approval);
                approvals.remove(((RtcApprovalDescriptorImpl) approval).getDescriptor());
            }
        }
    }
}
