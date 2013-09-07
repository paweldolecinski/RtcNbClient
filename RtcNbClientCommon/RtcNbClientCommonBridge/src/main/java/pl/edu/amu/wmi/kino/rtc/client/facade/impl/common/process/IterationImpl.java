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

import com.ibm.team.process.common.IIteration;
import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.Date;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

/**
 *
 * @author Patryk Żywica
 */
public class IterationImpl implements Iteration {

    private final IIteration iteration;

    /* package */ IterationImpl(IIterationHandle iterationHandle) {
        if (iterationHandle.hasFullState()) {
            this.iteration = (IIteration) iterationHandle.getFullState();
        } else {
            if (iterationHandle instanceof IIteration) {
                this.iteration = (IIteration) iterationHandle;
            } else {
                try {
                    this.iteration = (IIteration) ((ITeamRepository) iterationHandle.getOrigin()).itemManager().fetchCompleteItem(iterationHandle, IItemManager.DEFAULT, null);
                } catch (TeamRepositoryException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
    }

    public String getName() {
        return iteration.getLabel();
    }

    public Date getStartDate() {
        return iteration.getStartDate();
    }

    public Date getEndDate() {
        return iteration.getEndDate();
    }

    public boolean isArchived() {
        return iteration.isArchived();
    }
    
    public IIteration getIIteration(){
        return iteration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IterationImpl other = (IterationImpl) obj;
        if (this.iteration != other.iteration && (this.iteration == null || !this.iteration.getItemId().equals(other.iteration.getItemId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.iteration != null ? this.iteration.getItemId().hashCode() : 0);
        return hash;
    }
    
}
