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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewStyle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public abstract class RtcPlanItemViewModeImpl implements RtcPlanItemViewMode {

    protected boolean dirty;
    protected String name = "";
    protected List<RtcPlanItemFilter> filters;
    protected RtcPlanItemGrouping grouping;
    protected RtcPlanItemSorting sorting;
    protected RtcBarType barType = RtcBarType.LOAD_BAR;
    protected RtcPlanItemViewStyle viewStyle = RtcPlanItemViewStyle.TREE;
    protected ArrayList<RtcPlanItemAttribute> columns;
    private EventSourceSupport<RtcPlanItemViewModeEvent> eventSource = new EventSourceSupport<RtcPlanItemViewModeEvent>();

    public abstract String getId();

    @Override
    public RtcPlanItemFilter[] getFilters() {
        if (filters == null) {
            Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Filter");
            filters = new ArrayList((Collection<RtcPlanItemFilter>) forPath.lookupAll(RtcPlanItemFilter.class));
        }
        return filters.toArray(new RtcPlanItemFilter[]{});
    }

    @Override
    public RtcPlanItemGrouping getGrouping() {
        if (grouping == null) {
            Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Grouping");
            grouping = forPath.lookupAll(RtcPlanItemGrouping.class).toArray(new RtcPlanItemGrouping[0])[0];
        }
        return grouping;

    }

    @Override
    public RtcPlanItemSorting getSorting() {
        if (sorting == null) {
            Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Sorting");
            sorting = forPath.lookupAll(RtcPlanItemSorting.class).toArray(new RtcPlanItemSorting[0])[0];
        }
        return sorting;
    }

    @Override
    public RtcBarType getBarType() {
        return barType;
    }

    @Override
    public RtcPlanItemViewStyle getViewStyle() {
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Style");
        return viewStyle;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public boolean isModified() {
        return dirty;
    }

    @Override
    public void addColumn(RtcPlanItemAttribute attribute) {
        if (columns != null && columns.add(attribute)) {
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.COLUMN_ADDED);
        }
    }

    @Override
    public void addColumn(RtcPlanItemAttribute attribute, int position) {
        try {
            if (columns == null) {
                return;
            }
            columns.add(position, attribute);
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.COLUMN_ADDED);
        } catch (IndexOutOfBoundsException e) {
            RtcLogger.getLogger(RtcPlanItemViewModeImpl.class).log(Level.INFO, e.getLocalizedMessage(), e);
        }

    }

    @Override
    public void removeColumn(RtcPlanItemAttribute attribute) {
        if (columns != null && columns.remove(attribute)) {
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.COLUMN_REMOVED);
        }
    }

    @Override
    public void removeColumns() {
        if (columns != null && !columns.isEmpty()) {
            columns.clear();
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.COLUMN_REMOVED);
        }
    }

    @Override
    public void addFilter(RtcPlanItemFilter filter) {
        if (filters != null && filters.add(filter)) {
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.FILTER_ADDED);
        }
    }

    @Override
    public void removeFilter(RtcPlanItemFilter filter) {
        if (filters != null && filters.remove(filter)) {
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.FILTER_REMOVED);
        }
    }

    @Override
    public void removeFilters() {
        if (filters != null && !filters.isEmpty()) {
            filters.clear();
            dirty = true;
            fireEvent(RtcPlanItemViewModeEvent.FILTER_REMOVED);
        }
    }

    @Override
    public void setGrouping(RtcPlanItemGrouping grouping) {
        this.grouping = grouping;
        dirty = true;
        fireEvent(RtcPlanItemViewModeEvent.GROUPING_CHANGED);
    }

    @Override
    public void setSorting(RtcPlanItemSorting sorting) {
        this.sorting = sorting;
        dirty = true;
        fireEvent(RtcPlanItemViewModeEvent.SORTING_CHANGED);
    }

    @Override
    public void setBarType(RtcBarType barType) {
        this.barType = barType;
        dirty = true;
        fireEvent(RtcPlanItemViewModeEvent.BARTYPE_CHANGED);
    }

    @Override
    public void setViewStyle(RtcPlanItemViewStyle viewStyle) {
        this.viewStyle = viewStyle;
        dirty = true;
        fireEvent(RtcPlanItemViewModeEvent.STYLE_CHANGED);
    }

    @Override
    public void setDisplayName(String name) {
        this.name = name;
        dirty = true;
        fireEvent(RtcPlanItemViewModeEvent.NAME_CHANGED);
    }

    public final void removeListener(EventListener<RtcPlanItemViewModeEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemViewModeEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemViewModeEvent> listener) {
        eventSource.addListener(listener);
    }
}
