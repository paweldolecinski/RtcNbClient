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
package pl.edu.amu.wmi.kino.rtc.client.workitems.approvals;

import java.sql.Timestamp;

/**
 *
 * @author Pawel Dolecinski
 */
public interface RtcApproval {

    /**
     * For descriptors will return descriptor summary and for contributors firts and last name of contributor
     * @return the name of approval
     */
    public String getName();

    /**
     * @see RtcApprovalDescriptor
     * @return true if approval is instance of descriptor
     */
    boolean isDescriptor();

    /**
     * If approval doesn't has childs this methos will return empty list
     * @return list of approval children, never null
     */
    public RtcApproval[] getApprovals();

    /**
     * 
     * @param approval
     */
    public void addApproval(RtcApproval approval);

    /**
     * 
     * @param approval
     */
    public void removeApproval(RtcApproval approval);
    /**
     * State of approval. For contributors could be set, for descriptors it will be compute.
     * Possible values: @see RtcApprovalState
     * @return
     */
    public RtcApprovalState getState();

    /**
     * Type of approval.
     * Possible values: @see RtcApprovalType
     * 
     * @return type of approval
     */
    public RtcApprovalType getType();

    /**
     * 
     * @return due date for approval if exists or null
     */
    public Timestamp getDueDate();
}
