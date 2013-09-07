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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcPlanItemViewModeEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public interface RtcPlanItemViewMode extends EventSource<RtcPlanItemViewModeEvent> {

    /**
     * @return
     */
    public abstract String getDisplayName();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemAttribute[] getColumns();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemFilter[] getFilters();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemGrouping getGrouping();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemSorting getSorting();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcBarType getBarType();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemViewStyle getViewStyle();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract boolean isEditable();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract boolean isModified();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract Lookup getLookup();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanItemViewModeManager getManager();

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract void setDisplayName(String name);

    /**
     * @since 0.2.1.4
     * @param attribute
     */
    public abstract void addColumn(RtcPlanItemAttribute attribute);

    /**
     * @since 0.2.1.4
     * @param attribute
     * @param position
     */
    public abstract void addColumn(RtcPlanItemAttribute attribute, int position);

    /**
     * @since 0.2.1.4
     * @param attribute
     */
    public abstract void removeColumn(RtcPlanItemAttribute attribute);

    /**
     * @since 0.2.1.4
     */
    public abstract void removeColumns();

    /**
     * @since 0.2.1.4
     * @param filter
     */
    public abstract void addFilter(RtcPlanItemFilter filter);

    /**
     * @since 0.2.1.4
     * @param filter
     */
    public abstract void removeFilter(RtcPlanItemFilter filter);

    /**
     * @since 0.2.1.4
     */
    public abstract void removeFilters();

    /**
     * @since 0.2.1.4
     * @param grouping
     */
    public abstract void setGrouping(RtcPlanItemGrouping grouping);

    /**
     * @since 0.2.1.4
     * @param sorting
     */
    public abstract void setSorting(RtcPlanItemSorting sorting);

    /**
     * @since 0.2.1.4
     * @param barType
     */
    public abstract void setBarType(RtcBarType barType);

    /**
     * @since 0.2.1.4
     * @param viewStyle
     */
    public abstract void setViewStyle(RtcPlanItemViewStyle viewStyle);

    /**
     * @since 0.2.1.4
     * @throws RtcSaveException
     */
    public abstract void save() throws RtcSaveException;

    /**
     * @since 0.2.1.4
     */
    public abstract void discardChanges();

    public enum RtcBarType {

        PROGRESS_BAR,
        LOAD_BAR,
        NONE;
    }

    /**
     * @since 0.2.1.4
     * @author Patryk Żywica
     */
    public enum RtcPlanItemViewModeEvent {

        STYLE_CHANGED,
        GROUPING_CHANGED,
        SORTING_CHANGED,
        BARTYPE_CHANGED,
        NAME_CHANGED,
        VIEWMODE_SAVED,
        CHANGES_DISCARDED,
        FILTER_ADDED,
        FILTER_REMOVED,
        COLUMN_ADDED,
        COLUMN_REMOVED,}
}
