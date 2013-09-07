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
import java.util.List;
import java.util.Random;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewStyle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanViewModeImpl implements RtcPlanItemViewMode {

    private boolean modified = false;
    private String name;
    private RtcPlanItemViewModeManager manager;
    private List<RtcPlanItemAttribute> columns = new ArrayList<RtcPlanItemAttribute>();
    private List<RtcPlanItemFilter> filters = new ArrayList<RtcPlanItemFilter>();
    private RtcPlanItemSorting sorting;
    private RtcPlanItemGrouping grouping;
    private RtcPlanItemViewMode.RtcBarType barType;
    private RtcPlanItemViewStyle viewStyle;
    private EventSourceSupport<RtcPlanItemViewModeEvent> eventSource = new EventSourceSupport<RtcPlanItemViewModeEvent>();

    public DummyPlanViewModeImpl(RtcPlanItemViewModeManager manager, String name) {
        this.name = name;
        this.manager = manager;
        Random rand = new Random();


        columns.add(DummyPlanItemAttributesImpl.TYPE_PROPERTY);
        columns.add(DummyPlanItemAttributesImpl.SUMMPARY_ATTRIBUTE);

        columns.add(DummyPlanItemAttributesImpl.DURATION_PROPERTY);
        columns.add(DummyPlanItemAttributesImpl.PRIORITY_ATTRIBUTE);
        columns.add(DummyPlanItemAttributesImpl.ID_ATTRIBUTE);


        switch (rand.nextInt(3)) {
            case 2:
                filters.add(DummyPlanItemFilterImpl.FILTER2);
            case 1:
                filters.add(DummyPlanItemFilterImpl.FILTER3);
            default:
                filters.add(DummyPlanItemFilterImpl.FILTER4);
        }
        switch (rand.nextInt(3)) {
            case 2:
                grouping = DummyPlanItemGroupingImpl.GROUPING2;
                break;
            case 1:
                grouping = DummyPlanItemGroupingImpl.GROUPING3;
                break;
            default:
                grouping = DummyPlanItemGroupingImpl.GROUPING4;
                break;
        }
        switch (rand.nextInt(3)) {
            case 2:
                sorting = DummyPlanItemSortingImpl.SORTING2;
                break;
            case 1:
                sorting = DummyPlanItemSortingImpl.SORTING3;
                break;
            default:
                sorting = DummyPlanItemSortingImpl.SORTING4;
                break;
        }
        switch (rand.nextInt(3)) {
            case 2:
                viewStyle = RtcPlanItemViewStyle.FLAT;
                break;
            case 1:
                viewStyle = RtcPlanItemViewStyle.TASKBOARD;
                break;
            default:
                viewStyle = RtcPlanItemViewStyle.TREE;
                break;
        }
        switch (rand.nextInt(3)) {
            case 2:
                barType = RtcPlanItemViewMode.RtcBarType.LOAD_BAR;
                break;
            case 1:
                barType = RtcPlanItemViewMode.RtcBarType.NONE;
                break;
            default:
                barType = RtcPlanItemViewMode.RtcBarType.PROGRESS_BAR;
                break;
        }
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public RtcPlanItemAttribute[] getColumns() {
        return columns.toArray(new RtcPlanItemAttribute[]{});
    }

    @Override
    public RtcPlanItemFilter[] getFilters() {
        return filters.toArray(new RtcPlanItemFilter[]{});
    }

    @Override
    public RtcBarType getBarType() {
        return barType;
    }

    @Override
    public RtcPlanItemGrouping getGrouping() {
        return grouping;
    }

    @Override
    public RtcPlanItemSorting getSorting() {
        return sorting;
    }

    @Override
    public RtcPlanItemViewStyle getViewStyle() {
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
    public RtcPlanItemViewModeManager getManager() {
        return manager;
    }

    @Override
    public void setDisplayName(String name) {
        this.name = name;
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.NAME_CHANGED);
    }

    @Override
    public void addColumn(RtcPlanItemAttribute attribute) {
        columns.add(attribute);
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.COLUMN_ADDED);
    }

    @Override
    public void addColumn(RtcPlanItemAttribute attribute, int position) {
        columns.add(position, attribute);
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.COLUMN_ADDED);
    }

    @Override
    public void removeColumn(RtcPlanItemAttribute attribute) {
        columns.remove(attribute);
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.COLUMN_REMOVED);
    }

    @Override
    public void removeColumns() {
        columns.clear();
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.COLUMN_REMOVED);
    }

    @Override
    public void addFilter(RtcPlanItemFilter filter) {
        filters.add(filter);
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.FILTER_ADDED);
    }

    @Override
    public void removeFilter(RtcPlanItemFilter filter) {
        filters.remove(filter);
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.FILTER_REMOVED);
    }

    @Override
    public void removeFilters() {
        filters.clear();
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.FILTER_REMOVED);
    }

    @Override
    public void setGrouping(RtcPlanItemGrouping grouping) {
        this.grouping = grouping;
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.GROUPING_CHANGED);
    }

    @Override
    public void setSorting(RtcPlanItemSorting sorting) {
        this.sorting = sorting;
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.SORTING_CHANGED);
    }

    @Override
    public void setBarType(RtcBarType barType) {
        this.barType = barType;
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.BARTYPE_CHANGED);
    }

    @Override
    public void setViewStyle(RtcPlanItemViewStyle viewStyle) {
        this.viewStyle = viewStyle;
        modified = true;
        fireEvent(RtcPlanItemViewModeEvent.STYLE_CHANGED);
    }

    @Override
    public void save() throws RtcSaveException {
        modified = false;
        fireEvent(RtcPlanItemViewModeEvent.VIEWMODE_SAVED);
    }

    @Override
    public void discardChanges() {
        modified = false;
        fireEvent(RtcPlanItemViewModeEvent.CHANGES_DISCARDED);
    }

    @Override
    public boolean isModified() {
        return modified;
    }
//    @Override
//    public String getId() {
//        return name;
//    }

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
