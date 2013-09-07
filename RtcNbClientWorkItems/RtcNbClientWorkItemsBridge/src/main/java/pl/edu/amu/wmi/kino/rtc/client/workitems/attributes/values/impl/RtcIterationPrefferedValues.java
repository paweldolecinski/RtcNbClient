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
import java.util.Arrays;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;

/**
 * 
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcIterationPrefferedValues implements
		RtcWorkItemAttributePrefferedValues<Iteration> {

	private final ActiveProjectAreaImpl activeProjectArea;

	public RtcIterationPrefferedValues(ActiveProjectAreaImpl activeProjectArea) {
		this.activeProjectArea = activeProjectArea;
	}

	@Override
	public List<Iteration> getPrefferedValues() {

		List<Iteration> ret = new ArrayList<Iteration>();

		ProcessManager pm = activeProjectArea.getLookup().lookup(
				ProcessManager.class);
		for (DevelopmentLine d : pm.getDevelopmentLines()) {
			Iteration[] iterations = pm.getIterations(d);
			ret.addAll(Arrays.asList(iterations));
		}

		return ret;
	}

	@Override
	public void setConstraint(Object constraint) {
	}

	@Override
	public boolean isConstraint() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
