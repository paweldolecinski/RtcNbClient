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
package pl.edu.amu.wmi.kino.netbeans.mvp.event;

import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent.Type;

/**
 * Dispatches {@link MvpEvent}s to interested parties. Eases decoupling by
 * allowing objects to interact without having direct dependencies upon one
 * another, and without requiring event sources to deal with maintaining handler
 * lists. There will typically be one EventBus per application, broadcasting
 * events that may be of general interest.
 *
 * @see SimpleEventBus
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public interface EventBus {

    /**
     * Adds an unfiltered handler to receive events of given type from all sources.
     * <p>
     * It is rare to call this method directly. More typically a {@link MvpEvent}
     * subclass will provide a static <code>register</code> method.
     * <p>
     * A tip: to make a handler de-register itself, the following works:
     * <code><pre>new MyHandler() {
     *  HandlerRegistration reg = MyEvent.register(eventBus, this);
     * 
     *  public void onMyThing(MyEvent event) {
     *    {@literal /}* do your thing *{@literal /}
     *    reg.removeHandler();
     *  }
     * };
     * </pre></code>
     * 
     * @param <H> The type of handler
     * @param type the event type associated with this handler
     * @param handler the handler
     * @return the handler registration, can be stored in order to remove the
     *         handler later
     */
     <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler);

    /**
     * Adds a handler to receive events of given type from the given source.
     * <p>
     * It is rare to call this method directly. More typically a {@link MvpEvent}
     * subclass will provide a static <code>register</code> method.
     *
     * @see EventBus#addHandler(pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent.Type, pl.edu.amu.wmi.kino.netbeans.mvp.event.EventHandler) 
     * @param <H> The type of handler
     * @param type the event type associated with this handler
     * @param source the source associated with this handler
     * @param handler the handler
     * @return the handler registration, can be stored in order to remove the
     *         handler later
     */
    public abstract <H extends EventHandler> HandlerRegistration addHandlerToSource(
            Type<H> type, Object source, H handler);

    /**
     * Fires the event from no source. Only unfiltered handlers will receive it.
     * <p>
     * Any exceptions thrown by handlers will be bundled into a
     * {@link UmbrellaException} and then re-thrown after all handlers have
     * completed. An exception thrown by a handler will not prevent other handlers
     * from executing.
     * </p>
     * @param event the event to fire
     */
    void fireEvent(MvpEvent<?> event);

    /**
     * Fires the given event to the handlers listening to the event's type.
     * <p>
     * Any exceptions thrown by handlers will be bundled into a
     * {@link UmbrellaException} and then re-thrown after all handlers have
     * completed. An exception thrown by a handler will not prevent other handlers
     * from executing.
     * </p>
     * @param event the event to fire
     */
    public abstract void fireEventFromSource(MvpEvent<?> event, Object source);
}