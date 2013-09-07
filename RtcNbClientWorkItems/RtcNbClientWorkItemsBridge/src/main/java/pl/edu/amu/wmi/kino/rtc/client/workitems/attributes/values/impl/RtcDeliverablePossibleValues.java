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
import com.ibm.team.workitem.common.model.IDeliverable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDeliverableImpl;

/**
 * 
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcDeliverablePossibleValues implements
		RtcWorkItemAttributePossibleValues<RtcDeliverable> {

	private final ActiveProjectAreaImpl activeProjectArea;

	public RtcDeliverablePossibleValues(
			ActiveProjectAreaImpl activeProjectArea) {
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public List<RtcDeliverable> getPossibleValues() {
		List<RtcDeliverable> list = new ArrayList<RtcDeliverable>();
		IWorkItemCommon workItemCommon = (IWorkItemCommon) activeProjectArea
				.getITeamRepository().getClientLibrary(IWorkItemCommon.class);
		try {
			List<IDeliverable> findDeliverables = workItemCommon
					.findDeliverablesByProjectArea(activeProjectArea
							.getProjectArea().getIProcessArea(), true,
							IDeliverable.DEFAULT_PROFILE, null);
			for (IDeliverable iDeliverable : findDeliverables) {
				list.add(new RtcDeliverableImpl(iDeliverable));
			}
		} catch (TeamRepositoryException ex) {
			RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
		}
		return list;
	}
}
