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

import java.util.LinkedList;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.TeamAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;

import com.ibm.team.process.common.ITeamArea;
import com.ibm.team.repository.common.IContributorHandle;

/**
 * This class gives a contributors for given team areas. If list of team areas
 * is empty this class will gives you all not archived users
 * 
 * @author Pawel DOlecinski
 */
@Deprecated
public class RtcContributorPrefferedValues implements
		RtcWorkItemAttributePrefferedValues<Contributor> {

	private List<TeamArea> teamAreas;
	private final ActiveProjectAreaImpl activeProjectArea;

	/**
	 * 
	 * @param teamAreas
	 *            - could be empty but not null. List of ITeamAreaHandle or
	 *            ITeamArea objects
	 * @param activeProjectArea
	 *            cannot be null
	 */
	public RtcContributorPrefferedValues(List<TeamArea> teamAreas,
			ActiveProjectAreaImpl activeProjectArea) {
		assert teamAreas != null : "List of team ares cannot be null but could be empty.";
		assert activeProjectArea != null;

		this.teamAreas = teamAreas;
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public void setConstraint(Object teamAreas) {
		this.teamAreas = (List) teamAreas;

	}

	@Override
	public List<Contributor> getPrefferedValues() {

		ContributorManagerImpl cm = (ContributorManagerImpl) activeProjectArea
				.getLookup().lookup(ContributorManager.class);
		if (teamAreas.size() == 0) {
			LinkedList<Contributor> arrayList = new LinkedList<Contributor>();
			ContributorImpl[] contributors = cm.getContributors();
			for (int i = 0; i < contributors.length; i++) {
				arrayList.add(contributors[i]);
			}
			return arrayList;

		} else {
			List<Contributor> users = new LinkedList<Contributor>();
			for (TeamArea iTeamArea : teamAreas) {
				ITeamArea ta;
				if (iTeamArea instanceof TeamAreaImpl) {
					ta = ((TeamAreaImpl) iTeamArea).getIProcessArea();
					for (IContributorHandle contribhandle : ta.getMembers()) {
						users.add(cm.findContributor(contribhandle));
					}
				}
			}
			return users;
		}

	}

	@Override
	public boolean isConstraint() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
