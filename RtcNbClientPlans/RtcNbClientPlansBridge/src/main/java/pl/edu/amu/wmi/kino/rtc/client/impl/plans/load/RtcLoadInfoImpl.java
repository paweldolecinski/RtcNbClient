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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.load;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.IterationImpl;

import com.ibm.team.apt.internal.client.teamload.LoadItem;

/**
 * 
 * @since 0.2.1.3
 * @author Pawel Dolecinski
 */
public class RtcLoadInfoImpl implements RtcLoadInfo {

	private final ContributorImpl contributor;
	private final IterationImpl iteration;
	private LoadItem loadItem;
	private EventSourceSupport<RtcLoadInfoEvent> eventSource = new EventSourceSupport<RtcLoadInfoEvent>();

	/**
	 * 
	 * @param loadItem
	 * @param contributor
	 * @param iteration
	 */
	public RtcLoadInfoImpl(LoadItem loadItem, Contributor contributor,
			Iteration iteration) {
		assert contributor instanceof ContributorImpl;
		this.contributor = (ContributorImpl) contributor;
		this.iteration = (IterationImpl) iteration;
		this.loadItem = loadItem;
	}

	@Override
	public Contributor getContributor() {
		return contributor;
	}

	@Override
	public double getWorkHoursAvailable() {
		return 0L;
	}

	@Override
	public double getWorkHoursUsed() {
		return loadItem.getSumOfEstimates() - loadItem.getWorkTimeLeft();
	}

	@Override
	public int getPlannedItemsCount() {
		return loadItem.getEstimatedItems().size();
	}

	@Override
	public int getEstimatedItemsCount() {
		return loadItem.getEstimatedItems().size();
	}

	@Override
	public int getOpenItemsCount() {
		return loadItem.getOpenItems().size();
	}

	@Override
	public double getWorkTimeLeft() {
		return loadItem.getWorkTimeLeft();
	}

	public final void removeListener(EventListener<RtcLoadInfoEvent> listener) {
		eventSource.removeListener(listener);
	}

	public final void fireEvent(RtcLoadInfoEvent event) {
		eventSource.fireEvent(event);
	}

	public final void addListener(EventListener<RtcLoadInfoEvent> listener) {
		eventSource.addListener(listener);
	}
}
