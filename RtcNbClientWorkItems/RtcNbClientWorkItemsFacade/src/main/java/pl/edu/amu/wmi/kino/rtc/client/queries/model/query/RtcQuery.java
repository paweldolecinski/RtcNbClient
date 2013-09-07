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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery.RtcQueryEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * Implementations of this class will be used to represent queries.
 *
 * This is read only class. It means that it hasn't got setters.
 * @see pl.edu.amu.wmi.kino.rtc.client.queries.model.editable.RtcEditableQuery
 * @author Patryk Żywica
 */
public interface RtcQuery extends EventSource<RtcQueryEvent> {

    /**
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param column that will be added to this query. Not null.
     */
    public abstract void addColumn(RtcQueryColumn column);

    /**
     *
     * @param column that will be removed from this query. Not null.
     */
    public abstract void removeColumn(RtcQueryColumn column);

    /**
     * This method returns array of <code>RtcStatementColumn</code>. Each object
     * form that array describes one column that should be present in any result
     * view of this query.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return array containing columns for this query. Never null.
     */
    public abstract RtcQueryColumn[] getColumns();

    /**
     * This method return string identifier of this query.
     *
     * It is guaranteed that returned query ID is unique for all queries in manager.
     * @return unique within manager string identifier of this query.
     */
    public abstract String getQueryIdentifier();

    /**
     *
     * @return contributor that created this query.
     */
    public abstract Contributor getCreator();

    /**
     * This method returns query name that is stored on Jazz server.
     * 
     * @return name of this query.
     */
    public abstract String getName();

    /**
     * Returns query editable name.
     *
     * Editable name is local name of this query. Before any modification of this query
     * it is equal to <code>getName()</code>. But if you modify query name (and you don't
     * invoke <code>save()</code> method) it will return new name instead of <code>getName()</code>
     * that will return old one (name after last invocation of <code>save()</code> or
     * <code>RtcEditableQuery</code> creation in case that <code>save()</code> was not invoked yet).
     *
     * @return editable name of this query
     */
    public abstract String getEditableName();

    /**
     * Returns statement that holds expression for this query.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * Two different invocations of this method may not return the same object.
     * Statement object can change in many cases (e.g. <code>discard()</code> may
     * change it) firing STATEMENT_CHANGED event.
     *
     * @return statement that holds expression for this query, never <code>null</code>.
     * @throws RtcStatementCreationException 
     */
    public abstract RtcEditableStatement getStatement() throws RtcStatementCreationException;

    /**
     * This method returns List of RtcQueryResult.
     *
     * Implementations of this method
     * should return specific implementation of list that support lazy initialisation
     * of RtcQueryResult and associated Work Item (it means that data from server will
     * be fetch only when needed, to avoid unnecessary network traffic).
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return results of this query. Not null, may be empty.
     */
    public abstract List<RtcQueryResult> getQueryResults();

    /**
     * This method should return manager that manages this query.
     * @return manager for this query
     */
    public abstract RtcQueriesManager getQueriesManager();

    /**
     * This method creates copy of this query with given name.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * Given name cannot be empty or null.
     *
     * @param name 
     * @throws RtcSaveException
     */
    public abstract void saveAs(String name) throws RtcSaveException;

    /**
     * Discards all changes since last invocation of <code>save()</code> or RtcQuery
     * creation in case that <code>save()</code> was not invoked yet or is not available.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     */
    public abstract void discardChanges();

    /**
     * This enumeration defines all possible events for <code>RtcQuery</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcQueryEvent {

        /**
         * This event should be called when query column was added to query.
         */
        COLUMN_ADDED,
        /**
         * This event should be called when query column was removed  from query.
         */
        COLUMN_REMOVED,
        /**
         * This event should be called when query name on server was changed.
         */
        NAME_CHANGED,
        /**
         * This event should be called when editable name was changed.
         */
        EDITABLE_NAME_CHANGED,
        /**
         * This event should be called when query was saved.
         */
        QUERY_SAVED,
        /**
         * This event should be called when query's changes was discarded.
         */
        CHANGES_DISCARDED,
        /**
         * This event should be called when query's statement was changed.
         */
        STATEMENT_CHANGED,
        /**
         * This event should be called after query synchronisation with server data.
         * It means that query's statement, name and other properties may be changed.
         */
        QUERY_SYNCHRONIZED_WITH_SERVER;
    }
}
