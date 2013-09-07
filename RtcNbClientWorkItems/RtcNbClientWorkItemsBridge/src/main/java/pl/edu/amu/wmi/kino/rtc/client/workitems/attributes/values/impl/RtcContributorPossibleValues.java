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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * 
 * @author dolek
 */
@Deprecated
public class RtcContributorPossibleValues implements
		RtcWorkItemAttributePossibleValues<Contributor> {

	private final ActiveProjectAreaImpl activeProjectArea;

	public RtcContributorPossibleValues(ActiveProjectAreaImpl activeProjectArea) {
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public List<Contributor> getPossibleValues() {
		List<Contributor> all = new ArrayList<Contributor>();
		try {
			List<IContributorHandle> tmp = activeProjectArea
					.getITeamRepository().contributorManager()
					.fetchAllContributors(null);
			ContributorManagerImpl lookup = (ContributorManagerImpl) activeProjectArea
					.getLookup().lookup(ContributorManager.class);
			for (IContributorHandle iContributorHandle : tmp) {

				all.add(lookup.findContributor(iContributorHandle));
			}
		} catch (TeamRepositoryException ex) {
			RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
		}
		return all;
	}
}
