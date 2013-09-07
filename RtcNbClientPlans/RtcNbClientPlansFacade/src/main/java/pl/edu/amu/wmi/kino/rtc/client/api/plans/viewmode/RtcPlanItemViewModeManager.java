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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

//TODO : bikol : javadoc about layer.xml registration
/**
 *
 * @since 0.2.1.4
 * @author Patryk Żywica
 */
public interface RtcPlanItemViewModeManager
        extends EventSource<RtcPlanItemViewModeManager.RtcPlanItemViewModeManagerEvent> {

    /**
     * This method returns all view modes registered in this manager for given planType.
     * <p>
     * It may return empty array if there isn't any view mode registered for given planType.
     * </p>
     * <p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param planType
     * @return
     */
    public abstract RtcPlanItemViewMode[] getViewModes(RtcPlanType planType);

    /**
     * This method returns all sortings registered in this manager for given viewMode.
     * <p>
     * It may return empty array if there isn't any sorting registered for given viewMode.
     * </p><p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param viewMode
     * @return
     */
    public abstract RtcPlanItemSorting[] getSortings(RtcPlanItemViewMode viewMode);

    /**
     * This method returns all groupings registered in this manager for given viewMode.
     * <p>
     * It may return empty array if there isn't any grouping registered for given viewMode.
     * </p><p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param viewMode
     * @return
     */
    public abstract RtcPlanItemGrouping[] getGrouping(RtcPlanItemViewMode viewMode);

    /**
     *  This method returns all filters registered in this manager for given viewMode.
     * <p>
     * It may return empty array if there isn't any filter registered for given viewMode.
     * </p><p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param viewMode
     * @return
     */
    public abstract RtcPlanItemFilter[] getFilters(RtcPlanItemViewMode viewMode);

    /**
     * Returns default view mode for given planType.
     * <p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param planType
     * @return default view mode for given planType
     */
    public abstract RtcPlanItemViewMode getDefaultViewMode(RtcPlanType planType);

    /**
     * @since 0.2.1.4
     */
    public enum RtcPlanItemViewModeManagerEvent {

        VIEWMODE_ADDED,
        VIEWMODE_REMOVED,
        SORTING_ADDED,
        SORTING_REMOVED,
        GROUPING_ADDED,
        GROUPING_REMOVED,
        FILTER_ADDED,
        FILTER_REMOVED,
        DEFAULT_VIEWMODE_CHANGED,}
}
