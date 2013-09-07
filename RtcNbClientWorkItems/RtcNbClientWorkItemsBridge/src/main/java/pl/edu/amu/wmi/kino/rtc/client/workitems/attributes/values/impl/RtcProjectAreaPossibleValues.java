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

import java.util.Arrays;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

/**
 * 
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcProjectAreaPossibleValues implements
		RtcWorkItemAttributePossibleValues<ProjectArea> {

	private final ActiveProjectAreaImpl activeProjectArea;

	public RtcProjectAreaPossibleValues(ActiveProjectAreaImpl activeProjectArea) {
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public List<ProjectArea> getPossibleValues() {
		// IProcessItemService service = (IProcessItemService)
		// activeProjectArea.getITeamRepository().getClientLibrary(IProcessItemService.class);
		ProjectAreaManager pm = activeProjectArea.getLookup().lookup(
				ProjectAreaManager.class);
		ProjectArea[] projectAreas = pm.getProjectAreas();
		return Arrays.asList(projectAreas);

	}
}
