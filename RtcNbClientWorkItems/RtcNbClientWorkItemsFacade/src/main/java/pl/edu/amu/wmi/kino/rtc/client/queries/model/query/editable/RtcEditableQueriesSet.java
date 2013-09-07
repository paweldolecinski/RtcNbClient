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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

/**
 * @see RtcQueriesSet
 * @author Patryk Żywica
 */
public interface RtcEditableQueriesSet extends RtcQueriesSet {

    /**
     * Returns new RtcEditableQuery.
     * <p>
     * Returned query is not saved on Jazz server or even in <code>RtcEditableQueriesSet</code>
     * unless you call <code>RtcEditableQuery.save()</code> method.
     * </p>
     * <p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @return new query that may be added to this queries set.
     */
    public abstract RtcEditableQuery createEditableQuery();

    /**
     * Removes given query from Jazz server and <code>RtcEditableQueriesSet</code>.
     * <p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @param query that should be removed.
     */
    public abstract void removeQuery(RtcQuery query);

}
