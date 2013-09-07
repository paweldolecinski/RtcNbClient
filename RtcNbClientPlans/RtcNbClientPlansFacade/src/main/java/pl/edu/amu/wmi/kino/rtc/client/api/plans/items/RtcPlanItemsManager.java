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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.items;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcPlanItemOrder;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;

/**
 * Implementations of this class will be responsible for managing plan items
 * assigned to <code>RtcPlan</code>.
 * <p>
 * Each plan may have it own manager, but it is not a rule. For example if two plans
 * let say PlanA and PlanB describes the same time period and will always have
 * the same plan items assigned to them, they can share the same manager object.
 * It means that <code>planA.getPlanItemsManager() == planB.getPlanItemsManager()</code>
 * equals to <code>true</code>.
 * </p><p>
 * Each <code>RtcPlanItemsManager</code> have two basic responsibilities:
 *  <ul>
 *      <li>fetching all items registered in this manager</li>
 *      <li>fetching all defined plan items orders</li>
 *  </ul>
 * </p>
 * @see RtcPlanItemOrder
 * @see RtcPlanItem
 * @author Patryk Żywica
 */
public interface RtcPlanItemsManager extends EventSource<RtcPlanItemsManager.RtcPlanItemsManagerEvent> {

    /**
     * Returns all <code>RtcPlanItem</code>s registered in this manager.
     * <p>
     * Equals to call <code>getPlanItems(RtcPlanItem.class,null)</code>.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * @see RtcPlanItem
     * @return array of all <code>RtcPlanItem</code> registered in this manager.
     */
    public abstract RtcPlanItem[] getPlanItems();

    /**
     * Returns all <code>RtcPlanItem</code>s of given class registered in this manager.
     * <p>
     * All returned items are instances of given class.
     * </p><p>
     * Equals to call <code>getPlanItems(clazz,null)</code>.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * @see RtcPlanWorkItem
     * @see RtcContributorAbsence
     * @see RtcPlanItem
     * @param <T> template parameter defining method return type. Must be inherited from <code>RtcPlanItem</code>
     * @param clazz wanted class of returned items. Must be inherited from <code>RtcPlanItem</code>.
     * @return array of all <code>RtcPlanItem</code> that are instances of T.
     */
    public abstract <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz);

    /**
     * Returns all <code>RtcPlanItem</code>s of given type registered in this manager.
     * <p>
     * All returned items have given type. If given type is <code>null</code> then any type is possible.
     * </p><p>
     * Equals to call <code>getPlanItems(RtcPlanItem.class,type)</code>.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * @see RtcPlanItem
     * @see RtcPlanItemType
     * @param type wanted type of returned items.
     * @return array of all <code>RtcPlanItem</code> that have <code>type</code> type.
     */
    public abstract RtcPlanItem[] getPlanItems(RtcPlanItemType type);

    /**
     * Returns all <code>RtcPlanItem</code>s of given class and type registered in this manager.
     * <p>
     * All returned items are instances of given class and have given type.
     * If given type is <code>null</code> then any type is possible.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * @see RtcPlanWorkItem
     * @see RtcContributorAbsence
     * @see RtcPlanItem
     * @see RtcPlanItemType
     * @param <T> template parameter defining method return type. Must be inherited from <code>RtcPlanItem</code>
     * @param clazz wanted class of returned items. Must be inherited from <code>RtcPlanItem</code>. Never <code>null</code>.
     * @param type wanted type of returned items. May be <code>null</code>.
     * @return array of all <code>RtcPlanItem</code> that are instances of T and have <code>type</code> type.
     */
    public abstract <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz, RtcPlanItemType type);

    /**
     * 
     * @param workitem
     * @return
     */
    public abstract RtcPlanWorkItem addNewWorkItem(RtcWorkItem workitem);

    /**
     * This enumeration defines all possible events for <code>RtcPlanItemsManager</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcPlanItemsManagerEvent {

        /**
         * This event should be called when plan item was added to manager.
         */
        ITEM_ADDED,
        /**
         * This event should be called when plan item was removed from manager.
         */
        ITEM_REMOVED,
        /**
         * This event should be called when item order was added to manager.
         */
        ITEM_ORDER_ADDED,
        /**
         * This event should be called when item order was removed from manager.
         */
        ITEM_ORDER_REMOVED,}
}
