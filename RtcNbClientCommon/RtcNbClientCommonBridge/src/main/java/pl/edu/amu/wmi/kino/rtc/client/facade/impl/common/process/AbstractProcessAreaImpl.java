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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process;

import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProcessAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public abstract class AbstractProcessAreaImpl implements ProcessArea {

    private final IProcessArea processArea;
    private EventSourceSupport<ProcessArea.ProcessAreaEvent> eventSupport = new EventSourceSupport<ProcessAreaEvent>();

    /*package*/ AbstractProcessAreaImpl(IProcessAreaHandle processAreaHandle) {
        if (processAreaHandle.hasFullState()) {
            this.processArea = (IProcessArea) processAreaHandle.getFullState();
        } else {
            if (processAreaHandle instanceof IProcessArea) {
                this.processArea = (IProcessArea) processAreaHandle;
            } else {
                try {
                    this.processArea = (IProcessArea) ((ITeamRepository) processAreaHandle.getOrigin()).itemManager().fetchCompleteItem(processAreaHandle, IItemManager.DEFAULT, null);
                } catch (TeamRepositoryException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
    }

    public IProcessArea getIProcessArea() {
        return processArea;
    }

    public String getName() {
        return processArea.getName();
    }

    public boolean isArchived() {
        return processArea.isArchived();
    }

    public String getId() {
        return processArea.getItemId().getUuidValue();
    }

    public final void removeListener(EventListener<ProcessAreaEvent> listener) {
        eventSupport.removeListener(listener);
    }

    public final void fireEvent(ProcessAreaEvent event) {
        eventSupport.fireEvent(event);
    }

    public final void addListener(EventListener<ProcessAreaEvent> listener) {
        eventSupport.addListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractProcessAreaImpl other = (AbstractProcessAreaImpl) obj;
        if (this.processArea != other.processArea && (this.processArea == null || !this.processArea.getItemId().equals(other.processArea.getItemId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.processArea != null ? this.processArea.getItemId().hashCode() : 0);
        return hash;
    }
}
