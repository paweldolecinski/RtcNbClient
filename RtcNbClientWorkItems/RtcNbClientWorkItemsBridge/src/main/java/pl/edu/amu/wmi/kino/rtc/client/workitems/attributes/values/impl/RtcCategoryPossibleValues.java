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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.ICategory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcCategoryImpl;

/**
 *
 * @author dolek
 */
@Deprecated
public class RtcCategoryPossibleValues implements RtcWorkItemAttributePossibleValues<RtcCategory> {

    private final ActiveProjectAreaImpl activeProjectArea;

    public RtcCategoryPossibleValues(ActiveProjectAreaImpl activeProjectArea) {
        this.activeProjectArea = activeProjectArea;
    }

    @Override
    public List<RtcCategory> getPossibleValues() {
        List<RtcCategory> list = new ArrayList<RtcCategory>();
        IWorkItemCommon workItemCommon = (IWorkItemCommon) activeProjectArea.getITeamRepository().getClientLibrary(IWorkItemCommon.class);
        try {
            List<ICategory> findCategories = workItemCommon.findCategories(activeProjectArea.getProjectArea().getIProcessArea(), ICategory.DEFAULT_PROFILE, null);
            for (ICategory iCategory : findCategories) {
                list.add(new RtcCategoryImpl(iCategory, activeProjectArea));
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
        }
        return list;
    }
}
