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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;

/**
 * Implementations of this abstract class will be responsible for managing queries
 * sets.
 *
 * @author Patryk Żywica
 */
public abstract class RtcQueriesManager {

    private final Set<RtcQueriesListener> listeners = Collections.synchronizedSet(new HashSet<RtcQueriesListener>());

    /**
     * Use this method to regtister new listener to this object.
     *
     * If given listener is actualy registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(RtcQueriesListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(RtcQueriesListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(RtcQueriesEvent event) {
        synchronized (listeners) {
            for (RtcQueriesListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(event, l));
            }
        }
    }

    /**
     * This method returns array of <code>RtcQueriesSet</code>s, each set represents one
     * shared queries group. Returned array may be empty.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return array of <code>RtcQueriesSet</code>s, never null,may be empty.
     */
    public abstract RtcQueriesSet[] getSharedQueriesSets();

    /**
     *
     * @return project area for this manager.
     */
    public abstract ProjectArea getProjectArea();

    /**
     * 
     * @return
     */
    public abstract ActiveProjectArea getAvtiveProjectArea();

    /**
     * This method returns <code>RtcEditableQueriesSet</code>, that represents all personal
     * queries on RTC server.
     *
     * @return <code>RtcEditableQueriesSet</code>, never null.
     */
    public abstract RtcEditableQueriesSet getPersonalQueriesSet();

    /**
     * Use this method to get query with specific queryId. There can be only one query
     * in whole manager with the same queryId.
     * <p>
     * Returned <code>RtcQuery</code> may implement also <code>RtcEditableQuery</code> if
     * given queryId is identifier of editable query.
     * </p><p>
     * Returned <code>RtcQueriesSet</code> may implement also <code>RtcEditableQueriesSet</code> if
     * given queryId is belongs to editable set.
     * </p>
     *
     * @param queryId of query that manager will search for
     * @return pair of <code>RtcQuery<code> and <code>RtcQueriesSet</code> or null if such query was not found in this manager.
     */
    public abstract Pair<RtcQuery, RtcQueriesSet> findQuery(String queryId);

    /**
     *
     * @return query attribute manager for this expression
     */
    public abstract RtcQueryAttributeManager getQueryAttributeManager();

    /**
     * Calling this method should force synchronising all model objects with corresponding
     * server data.
     */
    public abstract void synchronizeWithServer();

    /**
     * Listener interface for <code>RtcQueriesManager</code>. If you want to listen for
     * changes in this <code>RtcQueriesManager</code> you have to implement this interface
     * and pass this implementation to <code>RtcQueriesManager.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface RtcQueriesListener {

        public void queriesChanged(RtcQueriesEvent event);
    }

    /**
     * This enumeration defines all possible events for <code>RtcQueriesManager</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcQueriesEvent {

        /**
         * This event should be called when new set was added to manager.
         */
        QUERIES_SET_ADDED,
        /**
         * This event should be called when set was removed from manager.
         */
        QUERIES_SET_REMOVED;
    }

    static class EventRunner implements Runnable {

        private RtcQueriesEvent event;
        private RtcQueriesListener listener;

        public EventRunner(RtcQueriesEvent event, RtcQueriesListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.queriesChanged(event);
        }
    }
}
