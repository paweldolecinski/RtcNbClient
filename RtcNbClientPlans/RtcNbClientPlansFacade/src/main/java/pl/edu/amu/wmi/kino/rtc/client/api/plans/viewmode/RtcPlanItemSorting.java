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

import java.util.Comparator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcIllegalOrderException;

/**
 * Implementation of this class will be responsible for sorting plan items.
 * <p>
 * Each implementation may have other ordering algorithm. Changing this order is also
 * possible by calling insertXxx methods.
 * </p>
 * @since 0.2.1.4
 * @author Patryk Żywica
 */
public abstract class RtcPlanItemSorting implements Comparator<RtcPlanItem> {

    /**
     * Returns display name of this order. For example: "Rank", "Planned Time".
     *
     * @return display name of this order. Never <code>null</code> or <code>""</code>.
     */
    public abstract String getDisplayName();

    /**
     * Tries to insert given <code>item</code> somewhere between <code>after</code> and
     * <code>before</code>. Throws <code>RtcIlegalOrderException</code> when it is
     * impossible or illegal.
     * <p>
     * If given <code>item</code> is already ordered by this order it will be reordered
     * to new position. If <code>after</code> or <code>before</code> are not yet
     * ordered by this order then this method will throw <code>IllegalArgumentException</code>
     * </p><p>
     * If <code>after</code> (or <code>before</code>) parameter is <code>null</code> then
     * given <code>item</code> will be inserted between beginning of the order and
     * <code>before</code> item (or between <code>after</code> item and the end of
     * the order). Calling with both arguments set to <code>null</code> causes
     * inserting given <code>item</code> somewhere in the order.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @see RtcPlanItem
     * @param item that have to be inserted. Never <code>null</code>.
     * @param after given item will be inserted after this item. May be <code>null</code>.
     * @param before given item will be inserted before this item. May be <code>null</code>.
     * @throws RtcIllegalOrderException when it is impossible or illegal to insert item.
     * @throws IllegalArgumentException when given arguments does not meet requirements
     * described above.
     */
    public abstract void insertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before)
            throws RtcIllegalOrderException;

    /**
     * Tries to insert given <code>item</code> exactly before <code>before</code>
     * parameter. Throws <code>RtcIlegalOrderException</code> when it is
     * impossible or illegal.
     * <p>
     * If given <code>item</code> is already ordered by this order it will be reordered
     * to new position. If <code>before</code> is not yet
     * ordered by this order then this method will throw <code>IllegalArgumentException</code>
     * </p><p>
     * If <code>before</code> parameter is <code>null</code> then
     * given <code>item</code> will be inserted at the end of
     * the order.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @see RtcPlanItem
     * @param item that have to be inserted. Never <code>null</code>.
     * @param before given item will be inserted before this item. May be <code>null</code>.
     * @throws RtcIllegalOrderException when it is impossible or illegal to insert item.
     * @throws IllegalArgumentException when given arguments does not meet requirements
     * described above.
     */
    public abstract void insertBefore(RtcPlanItem item, RtcPlanItem before)
            throws RtcIllegalOrderException;

    /**
     * Tries to insert given <code>item</code> exactly after <code>after</code>
     * parameter. Throws <code>RtcIlegalOrderException</code> when it is
     * impossible or illegal.
     * <p>
     * If given <code>item</code> is already ordered by this order it will be reordered
     * to new position. If <code>after</code> is not yet
     * ordered by this order then this method will throw <code>IllegalArgumentException</code>
     * </p><p>
     * If <code>after</code> parameter is <code>null</code> then
     * given <code>item</code> will be inserted at the beginning of
     * the order.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @see RtcPlanItem
     * @param item that have to be inserted. Never <code>null</code>.
     * @param after given item will be inserted after this item. May be <code>null</code>.
     * @throws RtcIllegalOrderException when it is impossible or illegal to insert item.
     * @throws IllegalArgumentException when given arguments does not meet requirements
     * described above.
     */
    public abstract void insertAfter(RtcPlanItem item, RtcPlanItem after)
            throws RtcIllegalOrderException;
    /**
     *
     * @param item
     * @param after
     * @param before
     * @return
     */
    public abstract boolean canInsertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before);

    /**
     * 
     * @param item
     * @param before
     * @return
     */
    public abstract boolean canInsertBefore(RtcPlanItem item, RtcPlanItem before);

    /**
     *
     * @param item
     * @param after
     * @return
     */
    public abstract boolean canInsertAfter(RtcPlanItem item, RtcPlanItem after);
    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @param item1
     * @param item2
     * @return
     */
    @Override
    public abstract int compare(RtcPlanItem item1,RtcPlanItem item2);
}
