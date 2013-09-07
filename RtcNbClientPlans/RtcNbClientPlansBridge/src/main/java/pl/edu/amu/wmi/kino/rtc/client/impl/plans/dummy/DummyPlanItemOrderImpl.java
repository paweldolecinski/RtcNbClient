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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcIllegalOrderException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcPlanItemOrder;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanItemOrderImpl implements RtcPlanItemOrder {

    private EventSourceSupport<RtcPlanItemOrderEvent> eventSource = new EventSourceSupport<RtcPlanItemOrderEvent>();
    @Override
    public RtcPlanItem[] orderItems(RtcPlanItem[] items) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        List<RtcPlanItem> tmp = new ArrayList<RtcPlanItem>(Arrays.asList(items));
        Collections.shuffle(tmp);
        return tmp.toArray(new RtcPlanItem[]{});
    }

    @Override
    public String getDisplayName() {
        return "dummyOrder#" + hashCode() % 1000;
    }

    @Override
    public void insertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if ((new Random()).nextInt(100) == 0) {
            throw new RtcIllegalOrderException();
        }
        if( (new Random()).nextInt(100)==0){
            throw new IllegalArgumentException("after paremeter not included in this order");
        }
        fireEvent(RtcPlanItemOrderEvent.ORDER_CHANGED);
    }

    @Override
    public void insertBefore(RtcPlanItem item, RtcPlanItem before) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if ((new Random()).nextInt(100) == 0) {
            throw new RtcIllegalOrderException();
        }
        if( (new Random()).nextInt(100)==0){
            throw new IllegalArgumentException("before paremeter not included in this order");
        }
        fireEvent(RtcPlanItemOrderEvent.ORDER_CHANGED);
    }

    @Override
    public void insertAfter(RtcPlanItem item, RtcPlanItem after) throws RtcIllegalOrderException {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        if ((new Random()).nextInt(100) == 0) {
            throw new RtcIllegalOrderException();
        }
        if( (new Random()).nextInt(100)==0){
            throw new IllegalArgumentException("after paremeter not included in this order");
        }
        fireEvent(RtcPlanItemOrderEvent.ORDER_CHANGED);
    }

    public final void removeListener(EventListener<RtcPlanItemOrderEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemOrderEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemOrderEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
