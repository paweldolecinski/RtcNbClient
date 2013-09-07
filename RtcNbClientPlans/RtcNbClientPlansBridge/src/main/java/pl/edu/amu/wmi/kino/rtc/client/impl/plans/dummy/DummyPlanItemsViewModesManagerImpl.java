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

import java.util.HashMap;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ProjectAreaDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
//@ServiceProvider(service = ProjectAreaDependentManagerFactory.class, path = "Rtc/ProjectAreaDependentManagerFactories")
public class DummyPlanItemsViewModesManagerImpl implements RtcPlanItemViewModeManager, ProjectAreaDependentManagerFactory<RtcPlanItemViewModeManager> {

    private HashMap<ActiveRepository, DummyPlanItemsViewModesManagerImpl> mgrs =
            new HashMap<ActiveRepository, DummyPlanItemsViewModesManagerImpl>();
    private RtcPlanItemViewMode[] viewModes = new RtcPlanItemViewMode[]{
        new DummyPlanViewModeImpl(this, "view mode1"),
        new DummyPlanViewModeImpl(this, "view mode2")};
    private RtcPlanItemSorting[] sortings1 = new RtcPlanItemSorting[]{
        DummyPlanItemSortingImpl.SORTING2,
        DummyPlanItemSortingImpl.SORTING3,
        DummyPlanItemSortingImpl.SORTING4,};
    private RtcPlanItemSorting[] sortings2 = new RtcPlanItemSorting[]{
        DummyPlanItemSortingImpl.SORTING1,
        DummyPlanItemSortingImpl.SORTING2,
        DummyPlanItemSortingImpl.SORTING3,
        DummyPlanItemSortingImpl.SORTING4,};
    private RtcPlanItemGrouping[] groupings1 = new RtcPlanItemGrouping[]{
        DummyPlanItemGroupingImpl.GROUPING1,
        DummyPlanItemGroupingImpl.GROUPING2,
        DummyPlanItemGroupingImpl.GROUPING3,
        DummyPlanItemGroupingImpl.GROUPING4,};
    private RtcPlanItemGrouping[] groupings2 = new RtcPlanItemGrouping[]{
        DummyPlanItemGroupingImpl.GROUPING2,
        DummyPlanItemGroupingImpl.GROUPING3,
        DummyPlanItemGroupingImpl.GROUPING4,};
    private RtcPlanItemFilter[] filters1 = new RtcPlanItemFilter[]{
        DummyPlanItemFilterImpl.FILTER1,
        DummyPlanItemFilterImpl.FILTER2,
        DummyPlanItemFilterImpl.FILTER3,
        DummyPlanItemFilterImpl.FILTER4,};
    private RtcPlanItemFilter[] filters2 = new RtcPlanItemFilter[]{
        DummyPlanItemFilterImpl.FILTER2,
        DummyPlanItemFilterImpl.FILTER3,
        DummyPlanItemFilterImpl.FILTER4,};
    private EventSourceSupport<RtcPlanItemViewModeManagerEvent> eventSource = new EventSourceSupport<RtcPlanItemViewModeManagerEvent>();

    @Override
    public RtcPlanItemViewMode[] getViewModes(RtcPlanType planType) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return viewModes;
    }

    @Override
    public RtcPlanItemSorting[] getSortings(RtcPlanItemViewMode viewMode) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if (viewMode == viewModes[0]) {
            return sortings1;
        } else {
            return sortings2;
        }
    }

    @Override
    public RtcPlanItemGrouping[] getGrouping(RtcPlanItemViewMode viewMode) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if (viewMode == viewModes[0]) {
            return groupings1;
        } else {
            return groupings2;
        }
    }

    @Override
    public RtcPlanItemFilter[] getFilters(RtcPlanItemViewMode viewMode) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if (viewMode == viewModes[0]) {
            return filters1;
        } else {
            return filters2;
        }
    }

    @Override
    public RtcPlanItemViewMode getDefaultViewMode(RtcPlanType planType) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return getViewModes(planType)[0];
    }

    @Override
    public Class<RtcPlanItemViewModeManager> getManagerType() {
        return RtcPlanItemViewModeManager.class;
    }

    @Override
    public RtcPlanItemViewModeManager getManager(ActiveProjectArea area) {
        return new DummyPlanItemsViewModesManagerImpl();
    }

    @Override
    public String getManagerNamePrefix() {
        return "dummy plan item view mode manager";
    }

    @Override
    public String getManagerIdPrefix() {
        return "dummy plan item view mode manager";
    }

    public final void removeListener(EventListener<RtcPlanItemViewModeManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemViewModeManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemViewModeManagerEvent> listener) {
        eventSource.addListener(listener);
    }
}
