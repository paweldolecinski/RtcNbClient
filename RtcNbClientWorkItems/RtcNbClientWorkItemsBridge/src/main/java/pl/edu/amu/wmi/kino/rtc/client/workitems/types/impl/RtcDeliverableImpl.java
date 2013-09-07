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

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IDeliverable;
import com.ibm.team.workitem.common.model.IDeliverableHandle;
import java.sql.Timestamp;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;

/**
 * TODO: dolek: javadoc
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcDeliverableImpl extends RtcDeliverable {

    private IDeliverable deliverable = null;

    /**
     * TODO: dolek: javadoc
     * @param handle
     */
    public RtcDeliverableImpl(IDeliverableHandle handle) {
        if(handle == null)
            return;
        
        if (handle instanceof IDeliverable) {
            this.deliverable = (IDeliverable) handle;
        } else if (handle.hasFullState()) {
            this.deliverable = (IDeliverable) handle.getFullState();
        } else if(handle != null) {
            try {
                this.deliverable = (IDeliverable) ((ITeamRepository) handle.getOrigin()).itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, null);
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public String getItemId() {
        return (deliverable != null) ?
            deliverable.getItemId().getUuidValue() : "";
    }

    @Override
    public String getName() {
        return (deliverable != null) ?
            deliverable.getName() : "Unassigned";
    }

    @Override
    public String getHTMLDescription() {
        return (deliverable != null) ?
            deliverable.getHTMLDescription().getXMLText() : "";
    }

    @Override
    public Timestamp getCreationDate() {
        return (deliverable != null) ?
            deliverable.getCreationDate() : null;
    }

    @Override
    public boolean isArchived() {
        return (deliverable != null) ?
            deliverable.isArchived() : false;
    }

    /**
     * Intended for internal use
     * @return
     */
    public IDeliverable getDeliverable() {
        return (deliverable != null) ?
            deliverable : null;
    }

    /**
     * TODO: dolek: javadoc
     * @return
     */
    public RtcDeliverable getValue() {
        return this;
    }

    /**
     * TODO: dolek: javadoc
     * @return
     */
    public String getKey() {
        return getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcDeliverable) {
            RtcDeliverable kv = (RtcDeliverable) obj;
            return (kv.getItemId().equals(this.getItemId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.getItemId() != null ? this.getItemId().hashCode() : 0);
        return hash;
    }
}
