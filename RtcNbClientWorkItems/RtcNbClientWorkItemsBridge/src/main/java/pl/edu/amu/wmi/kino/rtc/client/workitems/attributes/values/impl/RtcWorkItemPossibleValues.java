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
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkItemPossibleValues implements RtcWorkItemAttributePossibleValues<RtcWorkItem> {

    private ActiveProjectAreaImpl area;

    public RtcWorkItemPossibleValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public List<RtcWorkItem> getPossibleValues() {
        IQueryClient qClient = (IQueryClient) area.getITeamRepository().getClientLibrary(IQueryClient.class);

        try {
            IQueryResult<IResolvedResult<IWorkItem>> results =
                    qClient.getResolvedExpressionResults(area.getProjectArea().getIProcessArea(), new Term(), IWorkItem.FULL_PROFILE);
            List<RtcWorkItem> wis = new LinkedList<RtcWorkItem>();
            List<Integer> ids = new ArrayList<Integer>();
            while (results.hasNext(null)) {
                ids.add(results.next(null).getItem().getId());
                //wis.add(new RtcWorkItemImpl());
            }
            wis.addAll(Arrays.asList(area.getLookup().lookup(RtcWorkItemsManager.class).findWorkItems(ids.toArray(new Integer[]{}))));
            Collections.sort(wis, new Comparator<RtcWorkItem>() {

                @Override
                public int compare(RtcWorkItem o1, RtcWorkItem o2) {
                    return o2.getId() - o1.getId();
                }
            });
            return wis;
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger().log(Level.WARNING, ex.getLocalizedMessage());
            return new LinkedList<RtcWorkItem>();
        }
    }
}
