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

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IWorkItemType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemTypeImpl;

/**
 * 
 * @author dolek
 */
@Deprecated
public class RtcTypePossibleValues implements
		RtcWorkItemAttributePossibleValues<RtcWorkItemType> {

	private final ActiveProjectAreaImpl activeProjectArea;

	public RtcTypePossibleValues(ActiveProjectAreaImpl activeProjectArea) {
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public List<RtcWorkItemType> getPossibleValues() {
		List<RtcWorkItemType> types = new ArrayList<RtcWorkItemType>();
		try {
			ITeamRepository repo = activeProjectArea.getITeamRepository();
			IWorkItemClient wiClient = (IWorkItemClient) repo
					.getClientLibrary(IWorkItemClient.class);
			List<IWorkItemType> tmp = wiClient.findWorkItemTypes(
					activeProjectArea.getProjectArea().getIProcessArea(), null);

			for (IWorkItemType rtcWorkItemType : tmp) {
				types.add(new RtcWorkItemTypeImpl(rtcWorkItemType));
			}
		} catch (TeamRepositoryException ex) {
			RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
		}
		return types;
	}
}
