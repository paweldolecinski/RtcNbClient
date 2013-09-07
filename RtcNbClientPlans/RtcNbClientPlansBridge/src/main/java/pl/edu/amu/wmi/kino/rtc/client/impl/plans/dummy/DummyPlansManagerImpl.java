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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.apt.internal.client.IIterationPlanClient;
import com.ibm.team.apt.internal.client.IterationPlanClient;

/**
 * 
 * @author Patryk Żywica
 */
public class DummyPlansManagerImpl implements RtcPlansManager {

	private ActiveProjectAreaImpl area;
	private IterationPlanClient planClient;
	private HashMap<Iteration, DummyPlanImpl[]> plans = new HashMap<Iteration, DummyPlanImpl[]>();
	private RtcPlanType[] planTypes;
	private RtcPlanItemAttribute[] attributes = new RtcPlanItemAttribute[] {
			DummyPlanItemAttributesImpl.DESCRIPTION_ATTRIBUTE,
			DummyPlanItemAttributesImpl.ID_ATTRIBUTE,
			DummyPlanItemAttributesImpl.SUMMPARY_ATTRIBUTE,
			DummyPlanItemAttributesImpl.PRIORITY_ATTRIBUTE,
			DummyPlanItemAttributesImpl.TYPE_PROPERTY,
			DummyPlanItemAttributesImpl.DURATION_PROPERTY,
			DummyPlanItemAttributesImpl.SOME_ATTRIBUTE };
	private EventSourceSupport<RtcPlansManager.RtcPlansEvent> eventSource = new EventSourceSupport<RtcPlansEvent>();

	public DummyPlansManagerImpl(ActiveProjectAreaImpl area) {
		this.area = area;
	}

	@Override
	public RtcPlan[] getPlansFor(Iteration iteration) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		if (plans.get(iteration) == null) {
			ArrayList<DummyPlanImpl> tmp = new ArrayList<DummyPlanImpl>();
			for (int i = 0; i < 1 + (new Random()).nextInt(5); i++) {
				tmp.add(new DummyPlanImpl(null, this, iteration,
						new DummyProcessArea()));
			}
			plans.put(iteration, tmp.toArray(new DummyPlanImpl[] {}));
		}
		return plans.get(iteration);
	}

	@Override
	public RtcPlan findPlan(String planId) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		for (DummyPlanImpl[] plany : plans.values()) {
			for (DummyPlanImpl p : plany) {
				if (p.getPlanIdentifier().equals(planId)) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public void synchronizeWithServer() {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		plans.clear();
		fireEvent(RtcPlansEvent.PLANS_MANAGER_SYNCHRONIZED_WITH_SERVER);
	}

	@Override
	public RtcPlan createNewPlan(String name, Iteration iteration,
			ProcessArea owner, RtcPlanType type) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		DummyPlanImpl p = new DummyPlanImpl(name, this, iteration,
				new DummyProcessArea());
		return p;
	}

	@Override
	public RtcPlanType[] getPlanTypes() {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		if (planTypes == null) {
			planTypes = new DummyPlanTypeImpl[] { DummyPlanTypeImpl.TYPE1,
					DummyPlanTypeImpl.TYPE2 };
		}
		return planTypes;
	}

	private IterationPlanClient getPlanClient() {
		if (planClient == null) {
			planClient = (IterationPlanClient) area.getITeamRepository()
					.getClientLibrary(IIterationPlanClient.class);
		}
		return planClient;
	}

	@Override
	public RtcPlanPage createNewPage(String name) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyWikiPageImpl("new Dummy wiki page");
	}

	@Override
	public void addPlan(RtcPlan plan) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		List<DummyPlanImpl> tmp = new LinkedList(Arrays.asList(plans.get(plan
				.getIteration())));
		tmp.add((DummyPlanImpl) plan);
		plans.put(plan.getIteration(), tmp.toArray(new DummyPlanImpl[] {}));
		fireEvent(RtcPlansEvent.PLAN_ADDED);
	}

	@Override
	public void removePlan(RtcPlan plan) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		List<DummyPlanImpl> tmp = new LinkedList(Arrays.asList(plans.get(plan
				.getIteration())));
		tmp.remove((DummyPlanImpl) plan);
		plans.put(plan.getIteration(), tmp.toArray(new DummyPlanImpl[] {}));
		fireEvent(RtcPlansEvent.PLAN_REMOVED);
	}

	Contributor getContributor() {
		ContributorManager lookup = area.getLookup().lookup(
				ContributorManager.class);
		return lookup.getLoggedInContributor();
	}

	@Override
	public RtcComplexityComputator getDefaultComplexityComputator() {
		return new DummyComplexityComputatorImpl();
	}

	@Override
	public RtcLoadInfo getLoadInfo(Contributor contributor, Iteration iteration) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyLoadInfoImpl(contributor);
	}

	@Override
	public RtcProgressInfo getProgressInfo(Contributor contributor,
			Iteration iteration, RtcComplexityComputator complexityComputator) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyContributorProgressInfoImpl(contributor);
	}

	@Override
	public RtcProgressInfo getProgressInfo(Contributor contributor,
			RtcPlanItem[] items, RtcComplexityComputator complexityComputator) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyContributorProgressInfoImpl(contributor);
	}

	@Override
	public RtcProgressInfo getProgressInfo(RtcPlanItem[] items,
			RtcComplexityComputator complexityComputator) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyProgressInfoImpl();
	}

	@Override
	public RtcPlan[] getRelatedPlans(RtcPlan plan) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return getPlansFor(plan.getIteration());
	}

	@Override
	public RtcProgressInfo getProgressInfo(ProcessArea ownerArea,
			Iteration iteration, RtcComplexityComputator complexityComputator) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new DummyProgressInfoImpl();
	}

	@Override
	public RtcPlanItemAttribute[] getPlanItemAttributes() {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return attributes;
	}

	@Override
	public RtcPlanReport getDefaultReport() {
		return new DummyPlanReportImpl();
	}

	@Override
	public RtcPlanReport[] getPlanReportsList() {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		return new RtcPlanReport[] { new DummyPlanReportImpl(),
				new DummyPlanReportImpl(), new DummyPlanReportImpl(),
				new DummyPlanReportImpl() };
	}

	@Override
	public RtcPlanItemAttribute getPlanItemAttribute(String id) {
		// assert !EventQueue.isDispatchThread() :
		// "This method should not be called form event dispatch thread.";
		for (RtcPlanItemAttribute a : getPlanItemAttributes()) {
			if (a.getAttributeIdentifier().equals(id)) {
				return a;
			}
		}
		return null;
	}

	@Override
	public ActiveProjectArea getActiveProjectArea() {
		return area;
	}

	@Override
	public RtcPlanPageAttachment createNewAttachment(Contributor contributor,
			String name, String description) {
		return new DummyPlanPageAttachmentImpl(contributor, name, description);
	}

	public final void removeListener(EventListener<RtcPlansEvent> listener) {
		eventSource.removeListener(listener);
	}

	public final void fireEvent(RtcPlansEvent event) {
		eventSource.fireEvent(event);
	}

	public final void addListener(EventListener<RtcPlansEvent> listener) {
		eventSource.addListener(listener);
	}
}
