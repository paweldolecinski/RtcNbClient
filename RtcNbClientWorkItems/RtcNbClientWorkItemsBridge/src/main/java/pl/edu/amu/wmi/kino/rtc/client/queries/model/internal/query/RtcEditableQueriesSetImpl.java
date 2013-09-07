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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.query;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.query.IQueryDescriptor;
import com.ibm.team.workitem.common.query.QueryTypes;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;

/**
 * This class is used for personal queries sets.
 * @author Patryk Żywica
 */
class RtcEditableQueriesSetImpl implements RtcEditableQueriesSet {

    private IQueryClient queryClient;
    private ActiveProjectAreaImpl area;
    private final List<RtcEditableQueryImpl> queryList = Collections.synchronizedList(new ArrayList<RtcEditableQueryImpl>());
    private final Map<String, RtcEditableQueryImpl> toRtcQuery = Collections.synchronizedMap(new HashMap<String, RtcEditableQueryImpl>());
    private List<IQueryDescriptor> serverQueries = new ArrayList<IQueryDescriptor>();
    private String name;
    private boolean installed = false;
    private RtcQueriesManagerImpl manager;
    private EventSourceSupport<RtcQueriesSet.RtcQueriesSetEvent> eventSource = new EventSourceSupport<RtcQueriesSetEvent>();

    /*package*/ RtcEditableQueriesSetImpl(String name, RtcQueriesManagerImpl manager, IQueryClient queryClient) {
        this.queryClient = queryClient;
        this.name = name;
        this.manager = manager;
        this.area = manager.getActiveProjectArea();

    }

    @Override
    public RtcEditableQueryImpl createEditableQuery() {
        assert (!EventQueue.isDispatchThread());
        return new RtcEditableQueryImpl(manager);
    }

    @Override
    public void removeQuery(RtcQuery query) {
        assert (!EventQueue.isDispatchThread());
        if (query instanceof RtcEditableQueryImpl) {
            RtcEditableQueryImpl q = (RtcEditableQueryImpl) query;
            try {
                queryClient.delete(q.getQueryDescriptor(), null);
                toRtcQuery.remove(query.getQueryIdentifier());
                queryList.remove(query);
                //apropertiate event should be called from QueryClient listener
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(RtcEditableQueriesSetImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }

    @Override
    public RtcEditableQueryImpl[] getQueries() {
        assert !EventQueue.isDispatchThread();
        if (installed == false) {
            updateQueries();
            installed = true;
        }
        synchronized (queryList) {
            return queryList.toArray(new RtcEditableQueryImpl[]{});
        }
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    /**
     * this method is implemented in very inefficient way, but it should be enougth
     * for less then 1000 queries. This method have n^2 complexity.
     */
    private synchronized void updateQueries() {
        try {
            List<IQueryDescriptor> oldQueries = serverQueries;

            List<IQueryDescriptor> newQueries = queryClient.findPersonalQueries(
                    area.getProjectArea().getIProcessArea(),
                    area.getITeamRepository().loggedInContributor(),
                    QueryTypes.WORK_ITEM_QUERY,
                    IQueryDescriptor.DEFAULT_PROFILE,
                    null);
            this.serverQueries = newQueries;
            List<IQueryDescriptor> toAdd = new LinkedList<IQueryDescriptor>();
            List<IQueryDescriptor> toRemove = new LinkedList<IQueryDescriptor>();

            //Deretminating queries to add
            for (IQueryDescriptor nq : newQueries) {
                boolean add = true;
                for (IQueryDescriptor oq : oldQueries) {
                    if (oq.getItemId().getUuidValue().equals(nq.getItemId().getUuidValue())) {
                        add = false;
                        //all old queries that will stay in queries set have to be synchronized
                        this.findRtcQuery(oq).synchronizeWithServer(nq);
                        break;
                    }
                }
                if (add == true) {
                    toAdd.add(nq);
                }
            }
            if (toAdd.size() > 0) {
                for (IQueryDescriptor query : toAdd) {
                    RtcEditableQueryImpl q = new RtcEditableQueryImpl(query, manager);
                    toRtcQuery.put(query.getItemId().getUuidValue(), q);
                    queryList.add(q);
                }
                fireEvent(RtcQueriesSetEvent.QUERY_ADDED);
            }

            //Determinating queries to remove
            for (IQueryDescriptor oq : oldQueries) {
                boolean remove = true;
                for (IQueryDescriptor nq : newQueries) {
                    if (oq.getItemId().getUuidValue().equals(nq.getItemId().getUuidValue())) {
                        remove = false;
                        break;
                    }
                }
                if (remove == true) {
                    toRemove.add(oq);
                }
            }
            if (toRemove.size() > 0) {
                for (IQueryDescriptor query : toRemove) {
                    queryList.remove(findRtcQuery(query));
                    toRtcQuery.remove(query.getItemId().getUuidValue());
                }
                fireEvent(RtcQueriesSetEvent.QUERY_REMOVED);
            }
            assert (newQueries.size() == queryList.size());
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcEditableQueriesSetImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    private RtcEditableQueryImpl findRtcQuery(IQueryDescriptor query) {
        synchronized (toRtcQuery) {
            return toRtcQuery.get(query.getItemId().getUuidValue());
        }
    }

    /* package */ void synchronizeWithServer() {
        if (installed) {
            manager.getProcessor().post(new Runnable() {

                @Override
                public void run() {
                    updateQueries();
                }
            });
        }
    }

    @Override
    public RtcQueriesManager getQueriesManager() {
        return manager;
    }

    public final void removeListener(EventListener<RtcQueriesSetEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcQueriesSetEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcQueriesSetEvent> listener) {
        eventSource.addListener(listener);
    }

}
