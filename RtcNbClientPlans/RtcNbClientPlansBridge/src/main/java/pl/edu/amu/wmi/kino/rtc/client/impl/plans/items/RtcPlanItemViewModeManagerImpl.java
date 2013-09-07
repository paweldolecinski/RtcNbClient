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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeFactory;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.RtcPlanItemViewModeImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanItemViewModeManagerImpl implements RtcPlanItemViewModeManager {

    private final ActiveProjectAreaImpl area;
    private EventSourceSupport<RtcPlanItemViewModeManagerEvent> eventSource = new EventSourceSupport<RtcPlanItemViewModeManagerEvent>();

    RtcPlanItemViewModeManagerImpl(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public RtcPlanItemViewMode[] getViewModes(RtcPlanType planType) {
        String planId = planType.getId();
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/PlanTypeViewModeRegistration/" + planId);
        Collection<? extends RtcPlanItemViewModeFactory> lookupAll = forPath.lookupAll(RtcPlanItemViewModeFactory.class);
        List<RtcPlanItemViewMode> result = new ArrayList<RtcPlanItemViewMode>(lookupAll.size());
        for (RtcPlanItemViewModeFactory f : lookupAll) {
            result.add(f.createViewMode(this));
        }
        return result.toArray(new RtcPlanItemViewMode[]{});
    }

    @Override
    public RtcPlanItemSorting[] getSortings(RtcPlanItemViewMode viewMode) {
        RtcPlanItemViewModeImpl vm = (RtcPlanItemViewModeImpl) viewMode;
        String id = vm.getId();

        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + id + "/Sorting");
        Collection<? extends RtcPlanItemSorting> lookupAll = forPath.lookupAll(RtcPlanItemSorting.class);

        return lookupAll.toArray(new RtcPlanItemSorting[]{});
    }

    @Override
    public RtcPlanItemGrouping[] getGrouping(RtcPlanItemViewMode viewMode) {
        RtcPlanItemViewModeImpl vm = (RtcPlanItemViewModeImpl) viewMode;
        String id = vm.getId();

        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + id + "/Grouping");
        Collection<? extends RtcPlanItemGrouping> lookupAll = forPath.lookupAll(RtcPlanItemGrouping.class);

        return lookupAll.toArray(new RtcPlanItemGrouping[]{});
    }

    @Override
    public RtcPlanItemFilter[] getFilters(RtcPlanItemViewMode viewMode) {
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/Filter");
        Collection<? extends RtcPlanItemFilter> lookupAll = forPath.lookupAll(RtcPlanItemFilter.class);

        return lookupAll.toArray(new RtcPlanItemFilter[]{});
    }

    @Override
    public RtcPlanItemViewMode getDefaultViewMode(RtcPlanType planType) {
        String planId = planType.getId();
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/PlanTypeViewModeRegistration/" + planId);
        RtcPlanItemViewModeFactory lookup = forPath.lookupAll(RtcPlanItemViewModeFactory.class).toArray(new RtcPlanItemViewModeFactory[0])[0];
        if (lookup != null) {
            return lookup.createViewMode(this);
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public ActiveProjectAreaImpl getProjectArea() {
        return area;
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
