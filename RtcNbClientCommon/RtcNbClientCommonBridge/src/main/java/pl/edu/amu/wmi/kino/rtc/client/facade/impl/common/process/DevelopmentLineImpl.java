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

import com.ibm.team.process.common.IDevelopmentLine;
import com.ibm.team.process.common.IDevelopmentLineHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;

/**
 * Represents pair: development line - iterations list.
 * That list has iterations that directly belongs to
 * development line. If you want to get all iterations
 * you have to recursively get all childs of iterations in list
 * @author Pawel Dolecinski
 * @author Patryk Żywica
 */
public class DevelopmentLineImpl implements DevelopmentLine {

    private final IDevelopmentLine devLine;

    /*package*/ DevelopmentLineImpl(IDevelopmentLineHandle devLineHandle) {
        if (devLineHandle.hasFullState()) {
            this.devLine = (IDevelopmentLine) devLineHandle.getFullState();
        } else {
            if (devLineHandle instanceof IDevelopmentLine) {
                this.devLine = (IDevelopmentLine) devLineHandle;
            } else {
                try {
                    this.devLine = (IDevelopmentLine) ((ITeamRepository) devLineHandle.getOrigin()).itemManager().fetchCompleteItem(devLineHandle, IItemManager.DEFAULT, null);
                } catch (TeamRepositoryException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
    }

    /**
     *
     * @return development line display name
     */
    public String getName() {
        return devLine.getLabel();
    }
    
    public IDevelopmentLine getIDevelopmentLine(){
        return devLine;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DevelopmentLineImpl other = (DevelopmentLineImpl) obj;
        if (this.devLine != other.devLine && (this.devLine == null || !this.devLine.getItemId().equals(other.devLine.getItemId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.devLine != null ? this.devLine.getItemId().hashCode() : 0);
        return hash;
    }
    
}
