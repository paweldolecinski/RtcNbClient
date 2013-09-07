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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;

import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.ICategoryHandle;

/**
 * TODO: dolek: javadoc
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcCategoryImpl implements RtcCategory {

    private ICategory category;
	private final ActiveProjectAreaImpl area;

    /**
     * @param handle
     */
    public RtcCategoryImpl(ICategoryHandle handle, ActiveProjectAreaImpl area) {
        this.area = area;
		if (handle instanceof ICategory) {
            this.category = (ICategory) handle;
        } else if (handle.hasFullState()) {
            this.category = (ICategory) handle.getFullState();
        } else {
            try {
                this.category = (ICategory) ((ITeamRepository) handle.getOrigin()).itemManager().fetchCompleteItem(handle, IItemManager.DEFAULT, null);
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public String getId() {
        return category.getCategoryId().getInternalRepresentation();
    }

    @Override
    public String getName() {
        return category.getName();
    }

    @Override
    public String getHTMLDescription() {
        return category.getHTMLDescription().getXMLText();
    }

    /**
     *  Intended for internal use
     * @return
     */
    public ICategory getCategory() {
        return category;
    }

    /**
     * TODO: dolek: javadoc
     * @return
     */
    public RtcCategory getValue() {
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
        if (obj instanceof RtcCategory) {
            RtcCategory kv = (RtcCategory) obj;
            return (kv.getId().equals(this.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public List<TeamArea> getAssociatedTeamAreas() {
        List<TeamArea> ret = new ArrayList<TeamArea>();
        ProcessManagerImpl lookup = (ProcessManagerImpl) area.getLookup().lookup(ProcessManager.class);
        for (ITeamAreaHandle iTeamAreaHandle : category.getAssociatedTeamAreas()) {
            ret.add(lookup.findTeamArea(iTeamAreaHandle));
        }
        return ret;
    }
}
