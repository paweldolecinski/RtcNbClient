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
import java.util.Date;
import java.util.Random;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcContributorAbsence;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyRtcContributorAbsence extends RtcContributorAbsence {

    private String id;
    private Date start, end;
    private Contributor owner;
    private RtcPlanItem parent;
    private final RtcPlan plan;
    private EventSourceSupport<RtcPlanItemEvent> eventSource = new EventSourceSupport<RtcPlanItemEvent>();

    public DummyRtcContributorAbsence(Contributor owner, RtcPlanItem parent, RtcPlan plan) {
        this.owner = owner;
        this.parent = parent;
        Random rand = new Random();
        Integer.toHexString(rand.nextInt());
        start = new Date(System.currentTimeMillis() - rand.nextInt(10000000));
        end = new Date(System.currentTimeMillis() + rand.nextInt(10000000));
        this.plan = plan;
    }

    @Override
    public RtcPlanItemType getPlanItemType() {
        return RtcPlanItemType.ABSENCE;
    }

    @Override
    public RtcPlanItem[] getChildItems() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return new RtcPlanItem[]{};
    }

    @Override
    public <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return (T[]) Array.newInstance(clazz, 0);
    }

    @Override
    public String getPlanItemIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return "absence" + hashCode();
    }

    @Override
    public Date getStartDate() {
        return start;
    }

    @Override
    public Date getEndDate() {
        return end;
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
        return null;
    }

    @Override
    public <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value) throws RtcIllegalPlanAttributeValue {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        throw new RtcIllegalPlanAttributeValue();
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
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
