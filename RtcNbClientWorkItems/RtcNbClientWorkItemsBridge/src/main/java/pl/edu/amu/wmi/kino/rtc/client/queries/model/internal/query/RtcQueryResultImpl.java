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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.query;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.WorkItemManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryResult;
import com.ibm.team.workitem.common.model.IWorkItem;

/**
 * 
 * @author Patryk Żywica
 */
class RtcQueryResultImpl implements RtcQueryResult {

	private IWorkItem workitem;
	private ActiveProjectArea area;
	private double score;

	public RtcQueryResultImpl(IWorkItem workitem, ActiveProjectArea area,
			double score) {
		this.workitem = workitem;
		this.area = area;
		this.score = score;
	}

	@Override
	public RtcWorkItem getWorkItem() {
		RtcWorkItemManager wim = area.getLookup().lookup(
				RtcWorkItemManager.class);
		assert wim instanceof WorkItemManagerImpl : wim.getClass().getName();
		WorkItemManagerImpl wiManager = (WorkItemManagerImpl) wim;
		RtcWorkItem findWorkItem = wiManager.getWorkItem(workitem);
		return findWorkItem;
	}

	@Override
	public double getScore() {
		return score;
	}

	@Override
	public ActiveProjectArea getActiveProjectArea() {
		return area;
	}

}
