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
import java.util.LinkedList;
import java.util.Random;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemsManagerImpl implements RtcPlanItemsManager {

    private LinkedList<RtcPlanItem> items = new LinkedList<RtcPlanItem>();
    private DummyPlanItemOrderImpl[] orders;
    private EventSourceSupport<RtcPlanItemsManagerEvent> eventSource = new EventSourceSupport<RtcPlanItemsManagerEvent>();

    /**
     *
     * @param plan
     */
    public DummyPlanItemsManagerImpl(DummyPlanImpl plan) {
        Random rand = new Random();
        int n = rand.nextInt(10);
        for (int i = 0; i < n; i++) {
            if (rand.nextBoolean()) {
                items.add(new DummyPlanWorkItem(plan.getPlansManager().getContributor(), null, plan));
            } else {
                //items.add(new DummyRtcContributorAbsence(plan.getPlansManager().getContributor(),null, plan));
            }
        }

        orders = new DummyPlanItemOrderImpl[]{
            new DummyPlanItemOrderImpl(),
            new DummyPlanItemOrderImpl()};
    }

    @Override
    public RtcPlanItem[] getPlanItems() {
        return items.toArray(new RtcPlanItem[]{});
    }

    @Override
    public <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz) {
        LinkedList<T> tmp = new LinkedList<T>();
        for (RtcPlanItem item : items) {
            if (clazz.isInstance(item)) {
                tmp.add((T) item);
            }
        }
        return tmp.toArray((T[]) Array.newInstance(clazz, 0));
    }

    @Override
    public RtcPlanItem[] getPlanItems(RtcPlanItemType type) {
        LinkedList<RtcPlanItem> tmp = new LinkedList<RtcPlanItem>();
        for (RtcPlanItem item : items) {
            if (item.getPlanItemType().equals(type)) {
                tmp.add(item);
            }
        }
        return tmp.toArray(new RtcPlanItem[]{});
    }

    @Override
    public <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz, RtcPlanItemType type) {
        LinkedList<T> tmp = new LinkedList<T>();
        for (RtcPlanItem item : items) {
            if (clazz.isInstance(item) && item.getPlanItemType().equals(type)) {
                tmp.add((T) item);
            }
        }
        return tmp.toArray((T[]) Array.newInstance(clazz, 0));
    }

    @Override
    public RtcPlanWorkItem addNewWorkItem(RtcWorkItem workitem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void removeListener(EventListener<RtcPlanItemsManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemsManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemsManagerEvent> listener) {
        eventSource.addListener(listener);
    }
}
