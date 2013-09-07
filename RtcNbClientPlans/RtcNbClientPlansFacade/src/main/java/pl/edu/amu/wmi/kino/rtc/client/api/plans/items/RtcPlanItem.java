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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem.RtcPlanItemEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * Abstract plan item. This class represents common responsibilities of all plan
 * items.
 *
 * @see RtcContributorAbsence
 * @see RtcPlanWorkItem
 * @author Patryk Żywica
 */
public abstract class RtcPlanItem implements EventSource<RtcPlanItemEvent> {

    private final Set<RtcPlanItemAttributeValueChangeListener> listeners =
            Collections.synchronizedSet(new HashSet<RtcPlanItemAttributeValueChangeListener>());

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param <T>
     * @since 0.2.1.3
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final <T> void addListener(RtcPlanItemAttributeValueChangeListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param <T> 
     * @since 0.2.1.3
     * @param listener to be unregistered from this object. Not null.
     */
    public final <T> void removeListener(RtcPlanItemAttributeValueChangeListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     *
     * @param <T>
     * @since 0.2.1.3
     * @param attribute
     * @param newValue
     * @param oldValue
     */
    protected final <T> void fireAttributeValueChanged(RtcPlanItemAttribute<T> attribute, T newValue, T oldValue) {
        synchronized (listeners) {
            for (RtcPlanItemAttributeValueChangeListener l : listeners) {
                RequestProcessor.getDefault().post(new ValueChangeEventRunner<T>(attribute, newValue, oldValue, l));
            }
        }

    }

    /**
     * Returns this item type.
     * <p>
     * Plan item type describes what kind of item it is.
     * </p>
     *
     * @return type of this plan item.
     */
    public abstract RtcPlanItemType getPlanItemType();

    /**
     * Returns all <code>RtcPlanItem</code>s that are children of this item.
     * <p>
     * Equals to call <code>getPlanItems(RtcPlanItem.class)</code>.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @see RtcPlanItem
     * @return array of all <code>RtcPlanItem</code> that are children of this item.
     */
    public abstract RtcPlanItem[] getChildItems();

    /**
     * Returns all <code>RtcPlanItem</code>s that are children of this item.
     * <p>
     * All returned items are instances of given class.
     * </p><p>
     * Order of returned items is undefined.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @see RtcPlanWorkItem
     * @see RtcContributorAbsence
     * @param <T> template parameter defining method return type. Must be inherited from <code>RtcPlanItem</code>
     * @param clazz wanted class of returned items. Must be inherited from <code>RtcPlanItem</code>.
     * @return array of all <code>RtcPlanItem</code> that are instances of T.
     */
    public abstract <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz);

    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract RtcPlanItem getParentItem();

    /**
     * Returns name of this plan item.
     *
     * Returned item can be presented the to user as main or header name of this
     * plan item.
     *
     * @return name of this plan item
     */
    public abstract String getName();

    /**
     *
     * @return
     */
    public abstract Contributor getOwner();

    /**
     * This method return string identifier of this plan item.
     *
     * It is guaranteed that returned plan item ID is unique for all plans in
     * <code>RtcPlansManager</code>.
     * @return unique within manager string identifier of this plan item.
     */
    public abstract String getPlanItemIdentifier();

    /**
     * Returns value of given attribute for this plan item.
     * 
     * Three cases may occur:
     * <ul>
     * <li>it may return standard value</li>
     * <li>it may return special null value. Each attribute has its own null value that
     * can be fetch through <code>RtcPlanItemAttribute.getNullValue()</code>.
     * This means that this plan item supports this attribute but has no value assigned
     * to it.</li>
     * <li>it may return <code>null</code> to indicate that this attribute is unsupported by
     * this plan item.</li>
     * </ul>
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param <T>
     * @param attribute for which value should be returned.
     * @return returns T value or <code>null</code>.
     */
    public abstract <T> T getPlanAttributeValue(RtcPlanItemAttribute<T> attribute);

    /**
     * Sets given attribute value for this plan item.
     *
     * <p>
     * It may throw an exception when value is improper or illegal for this attribute or
     * plan item.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param <T>
     * @param attribute to set value
     * @param value to be set
     * @throws RtcIllegalPlanAttributeValue when given attribute is illegal
     */
    public abstract <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value)
            throws RtcIllegalPlanAttributeValue;

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract Lookup getLookup();

    /**
     * Returns plan for which item belongs.
     * Every instance of plan item belongs to one plan.
     *
     * @return plan instance
     */
    public abstract RtcPlan getPlan();

    /**
     * This enumeration defines all possible events for <code>RtcPlanItem</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcPlanItemEvent {

        /**
         * This event should be called when plan item type was changed.
         */
        ITEM_TYPE_CHANGED,
        /**
         * This event should be called when child plan item was added to this item.
         */
        CHILD_ITEM_ADDED,
        /**
         * This event should be called when child plan item was removed from this item.
         */
        CHILD_ITEM_REMOVED,
        /**
         * @since 0.2.1.3
         */
        ATTRIBUTE_VALUE_CHANGED;
    }

    /**
     * @since 0.2.1.3
     * @author  Patryk Żywica
     * @param <T>
     */
    public static interface RtcPlanItemAttributeValueChangeListener<T> {

        /**
         * @since 0.2.1.3
         * @param attribute
         * @param newValue
         * @param oldValue
         */
        public void attributeValueChanged(RtcPlanItemAttribute<T> attribute, T newValue, T oldValue);
    }

    /**
     * @since 0.2.1.3
     * @param <T>
     */
    private static class ValueChangeEventRunner<T> implements Runnable {

        private RtcPlanItemAttribute<T> attribute;
        private T newValue, oldValue;
        private RtcPlanItemAttributeValueChangeListener l;

        public ValueChangeEventRunner(RtcPlanItemAttribute<T> attribute, T newValue, T oldValue, RtcPlanItemAttributeValueChangeListener l) {
            this.attribute = attribute;
            this.newValue = newValue;
            this.oldValue = oldValue;
            this.l = l;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            l.attributeValueChanged(attribute, newValue, oldValue);
        }
    }
}
