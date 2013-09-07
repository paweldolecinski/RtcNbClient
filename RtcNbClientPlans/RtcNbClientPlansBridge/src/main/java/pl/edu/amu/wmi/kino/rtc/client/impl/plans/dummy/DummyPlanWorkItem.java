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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanWorkItem extends RtcPlanWorkItem {

    private RtcPlanItemType type;
    private String id;
    private ArrayList<RtcPlanItem> children = new ArrayList<RtcPlanItem>();
    private Contributor owner;
    private RtcPlanItem parent;
    private HashMap<String, Object> values =
            new HashMap<String, Object>();
    private final RtcPlan plan;
    private EventSourceSupport<RtcPlanItem.RtcPlanItemEvent> eventSource = new EventSourceSupport<RtcPlanItemEvent>();

    public DummyPlanWorkItem(Contributor owner, RtcPlanItem parent, RtcPlan plan) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        this.owner = owner;
        this.parent = parent;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            type = RtcPlanItemType.EXECUTABLE;
        } else {
            type = RtcPlanItemType.NON_EXECUTABLE;
        }
        id = Integer.toString(rand.nextInt(1000));
        int n = rand.nextInt(3);
        if (rand.nextBoolean()) {
            n = 0;
        }
        for (int i = 0; i < n; i++) {
            if (rand.nextBoolean()) {
                children.add(new DummyPlanWorkItem(owner, this, plan));
            } else {
                //children.add(new DummyRtcContributorAbsence(owner, this, plan));
            }
        }

        /*
        if (rand.nextBoolean()) {
        values.put(DummyPlanItemAttributesImpl.DESCRIPTION_ATTRIBUTE.getAttributeIdentifier(), "Description " + DummyPlanItemAttributesImpl.DESCRIPTION_ATTRIBUTE.getNullValue());
        } else {
        values.put(DummyPlanItemAttributesImpl.DESCRIPTION_ATTRIBUTE.getAttributeIdentifier(), "Description of " + getName());
        }*/

        values.put(DummyPlanItemAttributesImpl.PRIORITY_ATTRIBUTE.getAttributeIdentifier(), DummyPlanItemAttributesImpl.PRIORITY_ATTRIBUTE.getNullValue());
        values.put(DummyPlanItemAttributesImpl.TYPE_PROPERTY.getAttributeIdentifier(), DummyPlanItemAttributesImpl.TYPE_PROPERTY.getNullValue());

        values.put(DummyPlanItemAttributesImpl.DURATION_PROPERTY.getAttributeIdentifier(), DummyPlanItemAttributesImpl.DURATION_PROPERTY.getNullValue());

        values.put(DummyPlanItemAttributesImpl.ID_ATTRIBUTE.getAttributeIdentifier(), new Integer(hashCode() % 1000000000));
        values.put(DummyPlanItemAttributesImpl.SUMMPARY_ATTRIBUTE.getAttributeIdentifier(), "Summary of " + getName());

        if (rand.nextBoolean()) {
            values.put(DummyPlanItemAttributesImpl.SOME_ATTRIBUTE.getAttributeIdentifier(), 666);
        } else {
            values.put(DummyPlanItemAttributesImpl.SOME_ATTRIBUTE.getAttributeIdentifier(), new Integer((hashCode() * 235) % 23563463));
        }
        this.plan = plan;
    }

    @Override
    public RtcWorkItem getWorkItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RtcPlanItemType getPlanItemType() {
        return type;
    }

    @Override
    public RtcPlanItem[] getChildItems() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return children.toArray(new RtcPlanItem[]{});
    }

    @Override
    public <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        LinkedList<T> tmp = new LinkedList<T>();
        for (RtcPlanItem item : children) {
            if (clazz.isInstance(item)) {
                tmp.add((T) item);
            }
        }
        return tmp.toArray((T[]) Array.newInstance(clazz, 0));
    }

    @Override
    public String getPlanItemIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return "name" + id;
    }

    @Override
    public Contributor getOwner() {
        return owner;
    }

    @Override
    public RtcPlanItem getParentItem() {
        return parent;
    }

    @Override
    public <T> T getPlanAttributeValue(RtcPlanItemAttribute<T> attribute) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return (T) values.get(attribute.getAttributeIdentifier());
    }

    @Override
    public <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value) throws RtcIllegalPlanAttributeValue {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        Random rand = new Random();
        if (rand.nextInt(100) > 90) {
            throw new RtcIllegalPlanAttributeValue();
        }
        T oldValue = (T) values.get(attribute.getAttributeIdentifier());
        values.put(attribute.getAttributeIdentifier(), value);
        fireEvent(RtcPlanItemEvent.ATTRIBUTE_VALUE_CHANGED);
        fireAttributeValueChanged(attribute, value, oldValue);
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
    }

    @Override
    public boolean isResolved() {
        return false;
    }

    public final void removeListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.addListener(listener);
    }
}
