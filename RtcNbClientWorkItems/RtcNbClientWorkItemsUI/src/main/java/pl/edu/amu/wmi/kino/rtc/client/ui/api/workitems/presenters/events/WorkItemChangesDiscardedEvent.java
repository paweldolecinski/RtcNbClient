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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventBus;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent.Type;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events.WorkItemChangesDiscardedEvent.ChangesDiscardedHandler;

/**
 *
 * @author Patryk Żywica
 */
public class WorkItemChangesDiscardedEvent extends MvpEvent<ChangesDiscardedHandler> {

    private static Type<ChangesDiscardedHandler> TYPE = new Type<ChangesDiscardedHandler>();

    @Override
    public Type<ChangesDiscardedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChangesDiscardedHandler handler) {
        handler.changesDiscarded();
    }

    public static HandlerRegistration register(ChangesDiscardedHandler h, Object source) {
        return Lookup.getDefault().lookup(EventBus.class).addHandlerToSource(TYPE, source, h);
    }

    public interface ChangesDiscardedHandler extends EventHandler {

        void changesDiscarded();
    }
}
