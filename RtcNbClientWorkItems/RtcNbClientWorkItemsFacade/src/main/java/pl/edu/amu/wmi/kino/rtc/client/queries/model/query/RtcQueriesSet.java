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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.query;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * Implementations of this abstract class will be responsible for agregating queries.
 * <p>
 * <code>RtcQueriesSet</code> agregates group of queries that should be shown and managed
 * together.
 * </p><p>
 * This is read only class. It means that in hasn't got setters. It does not mean that
 * values returned by its methods cannot change.
 * </p>
 * 
 * @author Patryk Żywica
 * @see pl.edu.amu.wmi.kino.rtc.client.queries.model.editable.RtcEditableQueriesSets
 */
public interface RtcQueriesSet extends EventSource<RtcQueriesSet.RtcQueriesSetEvent> {

    /**
     * Returns all queries agregated by this set.
     *
     * Returned array may be empty but not null.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return all queries agregated by this set. Not null.
     */
    public abstract RtcQuery[] getQueries();

    /**
     * Returns localized display name of this set.
     *
     * Returned <code>String</code> can be used as e.g. name of node representing this
     * set.
     *
     * @return localized display name of this set
     */
    public abstract String getDisplayName();

    /**
     * This method should return manager that manages this set.
     * @return manager for this set
     */
    public abstract RtcQueriesManager getQueriesManager();

    /**
     * This enumeration defines all possible events for <code>RtcQueriesSet</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcQueriesSetEvent {

        /**
         * This event should be called when new query was added to set.
         */
        QUERY_ADDED,
        /**
         * This event should be called when query was removed from set.
         */
        QUERY_REMOVED,
        /**
         * This event should be called when set name was changed.
         */
        NAME_CHANGED;
    }
}
