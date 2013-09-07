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
import java.util.Random;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanImpl implements RtcPlan {

    private String name;
    private ProcessArea owner;
    private Iteration iteration;
    private DummyPlansManagerImpl manager;
    private DummyPlanTypeImpl type;
    private ArrayList<DummyWikiPageImpl> wikis = new ArrayList<DummyWikiPageImpl>();
    private DummyWikiPageImpl overview = new DummyWikiPageImpl("DummyOverView");
    private DummyPlanItemsManagerImpl itemsManager;
    private int sync = 0;
    private RtcPlanItemChecker[] checkers;
    private EventSourceSupport<RtcPlan.RtcPlanEvent> eventSource = new EventSourceSupport<RtcPlanEvent>();

    public DummyPlanImpl(String name, DummyPlansManagerImpl manager, Iteration iter, ProcessArea owner) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        this.manager = manager;
        this.iteration = iter;
        this.owner = owner;
        if (name != null) {
            this.name = name;
        } else {
            this.name = "dummy plan@s" + sync + " #" + hashCode();
        }
        if ((new Random()).nextBoolean()) {
            type = DummyPlanTypeImpl.TYPE1;
        } else {
            type = DummyPlanTypeImpl.TYPE2;
        }
        for (int i = 0; i < 1 + (new Random()).nextInt() % 5; i++) {
            wikis.add(new DummyWikiPageImpl("Wikipage " + i));
        }
        this.itemsManager = new DummyPlanItemsManagerImpl(this);
    }

    @Override
    public DummyPlansManagerImpl getPlansManager() {
        return manager;
    }

    @Override
    public RtcPlanPage[] getPages() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return wikis.toArray(new RtcPlanPage[]{});
    }

    @Override
    public String getPlanIdentifier() {
        return "dummyID#" + hashCode();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iteration getIteration() {
        return iteration;
    }

    @Override
    public RtcPlanType getPlanType() {
        return type;
    }

    @Override
    public void addPage(RtcPlanPage page) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        wikis.add((DummyWikiPageImpl) page);
        fireEvent(RtcPlanEvent.PAGE_ADDED);
    }

    @Override
    public void removePage(RtcPlanPage page) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        wikis.remove(page);
        fireEvent(RtcPlanEvent.PAGE_REMOVED);
    }

    @Override
    public RtcPlanPage getOverviewPage() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return overview;
    }

    @Override
    public ProcessArea getOwner() {
        return owner;
    }

    @Override
    public void synchronizeWithServer() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        sync++;
        this.name = "dummy plan@s" + sync + " #" + hashCode();
        fireEvent(RtcPlanEvent.PLAN_SYNCHRONIZED_WITH_SERVER);
    }

    @Override
    public void save() throws RtcPlanSaveException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        sync++;
        this.name = "dummy plan@s" + sync + " #" + hashCode();
        fireEvent(RtcPlanEvent.PLAN_SAVED);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        fireEvent(RtcPlanEvent.NAME_CHANGED);
    }

    @Override
    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
        fireEvent(RtcPlanEvent.ITERATION_CHANGED);
    }

    @Override
    public void setPlanType(RtcPlanType type) {
        this.type = (DummyPlanTypeImpl) type;
        fireEvent(RtcPlanEvent.TYPE_CHANGED);
    }

    @Override
    public void setOwner(ProcessArea area) {
        this.owner = area;
        fireEvent(RtcPlanEvent.OWNER_CHANGED);
    }

    @Override
    public RtcPlanItemsManager getPlanItemsManager() {
        return itemsManager;
    }

    @Override
    public RtcComplexityComputator getComplexityComputator() {
        return new DummyComplexityComputatorImpl();
    }

    @Override
    public RtcPlanItemChecker[] getPlanItemCheckers() {
        if (checkers == null) {
            checkers = new RtcPlanItemChecker[]{
                new DummyPlanItemCheckerImpl(this),
                new DummyPlanItemCheckerImpl(this)};
        }
        return checkers;
    }

    @Override
    public boolean isReleasePlan() {
        return false;
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    public final void removeListener(EventListener<RtcPlanEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanEvent> listener) {
        eventSource.addListener(listener);
    }
}
