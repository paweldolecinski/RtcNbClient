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

/**
 * Parent class for all <code>MvpEvent</code>s. It contains basic implementation
 * of methods.
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public abstract class MvpEvent<H extends EventHandler> {


    /**
     * Type class used to register events with the {@link EventBus}.
     * <p>
     * Type is parameterized by the handler type in order to make the addHandler
     * method type safe.
     * </p>
     *
     * @param <H> handler type
     */
    public static class Type<H> {


        private static int nextHashCode;
        private final int index;


        /**
         * Constructor.
         */
        public Type() {
            index = ++nextHashCode;
        }

        // We override hash code to make it as efficient as possible.
        @Override
        public final int hashCode() {
            return index;
        }


        @Override
        public String toString() {
            return "Event type";
        }
    }
    private boolean dead;
    private Object source;


    /**
     * Constructor.
     */
    protected MvpEvent() {
    }


    /**
     * Returns the type used to register this event. Used by event bus to
     * dispatch events to the correct handlers.
     *
     * @return the type
     */
    public abstract Type<H> getAssociatedType();


    /**
     * Returns the source that last fired this event.
     *
     * @return object representing the source of this event
     */
    public Object getSource() {
        assertLive();
        return source;
    }


    /**
     * This is a method used primarily for debugging. It gives a string
     * representation of the event details. This does not override the toString
     * method because the compiler cannot always optimize toString out correctly.
     * Event types should override as desired.
     *
     * @return a string representing the event's specifics.
     */
    public String toDebugString() {
        String name = this.getClass().getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        return "event: " + name + ":";
    }


    /**
     * The toString() for abstract event is overridden to avoid accidently
     * including class literals in the the compiled output. Use {@link MvpEvent}
     * #toDebugString to get more information about the event.
     */
    @Override
    public String toString() {
        return "An event type";
    }


    /**
     * Asserts that the event still should be accessed. All events are considered
     * to be "dead" after their original event bus finishes firing them. An
     * event can be revived by calling {@link MvpEvent#revive()}.
     */
    protected void assertLive() {
        assert (!dead) : "This event has already finished being processed by its original handler manager, so you can no longer access it";
    }


    /**
     * Should only be called by {@link EventBus}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    protected abstract void dispatch(H handler);


    /**
     * Is the event current live?
     *
     * @return whether the event is live
     */
    protected final boolean isLive() {
        return !dead;
    }


    /**
     * Kill the event. After the event has been killed, users cannot really on its
     * values or functions being available.
     */
    protected void kill() {
        dead = true;
        source = null;
    }


    /**
     * Revives the event. Used when recycling event instances.
     */
    protected void revive() {
        dead = false;
        source = null;
    }


    /**
     * Set the source that triggered this event.
     *
     * @param source the source of this event, should only be set by a
     *          {@link EventBus}
     */
    /* package */void setSource(Object source) {
        this.source = source;
    }
}