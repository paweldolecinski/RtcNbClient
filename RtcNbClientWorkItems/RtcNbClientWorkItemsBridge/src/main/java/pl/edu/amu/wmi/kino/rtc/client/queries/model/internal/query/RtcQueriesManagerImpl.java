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

import com.ibm.team.process.common.ITeamArea;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IQueryEvent;
import com.ibm.team.workitem.client.IQueryListener;
import com.ibm.team.workitem.common.query.IQueryDescriptor;
import com.ibm.team.workitem.common.query.QueryTypes;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;

/**
 * @see RtcQueriesManager
 * @author Patryk Żywica
 */
class RtcQueriesManagerImpl extends RtcQueriesManager implements IQueryListener {

    private ActiveProjectAreaImpl area;
    private IQueryClient qClient;
    private RtcEditableQueriesSetImpl personalSet;
    private boolean sharedInitialized = false;
    private List<RtcQueriesSetImpl> sharedSets = new ArrayList<RtcQueriesSetImpl>();
    private ProjectArea projectArea;
    ;
    private HashSet<String> teamAreaSharedSets = new HashSet<String>();
    private boolean projectAreaSharedSet = false;
    private boolean contributorSharedSet = false;
    private boolean listenersInitialized = false;
    private static final RequestProcessor requestProcessor = new RequestProcessor("RtcQueriesManagerImplRequestProcessor");

    public RtcQueriesManagerImpl(ActiveProjectAreaImpl area) {
        this.area = area;
        this.qClient = (IQueryClient) area.getITeamRepository().getClientLibrary(IQueryClient.class);
        this.projectArea = area.getProjectArea();
    }

    private void initListeners() {
        if (!listenersInitialized) {
            qClient.addQueryListener(IQueryEvent.QUERY_ADDED_EVENT_TYPE, this);
            qClient.addQueryListener(IQueryEvent.QUERY_CREATED_EVENT_TYPE, this);
            qClient.addQueryListener(IQueryEvent.QUERY_DELETED_EVENT_TYPE, this);
            listenersInitialized = true;
        }
    }

    RequestProcessor getProcessor() {
        return requestProcessor;
    }

    @Override
    public RtcQueriesSet[] getSharedQueriesSets() {
        assert (!EventQueue.isDispatchThread());
        if (!sharedInitialized) {
            updateSets();
            initListeners();
            sharedInitialized = true;
        }
        return sharedSets.toArray(new RtcQueriesSet[]{});
    }

    private void updateSets() {
        List<IQueryDescriptor> tmp;
        try {
            if (projectAreaSharedSet == false
                    && (tmp = qClient.findSharedQueries(
                    area.getProjectArea().getIProcessArea(),
                    Collections.singletonList(area.getProjectArea().getIProcessArea()),
                    QueryTypes.WORK_ITEM_QUERY,
                    IQueryDescriptor.DEFAULT_PROFILE,
                    null)) != null) {
                sharedSets.add(new RtcQueriesSetImpl(NbBundle.getMessage(RtcQueriesManagerImpl.class, "SharedQueries.predefined.name"), Collections.singletonList(area.getProjectArea().getIProcessArea()), this, qClient));
                projectAreaSharedSet = true;
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcQueriesManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        try {
            if (contributorSharedSet == false
                    && (tmp = qClient.findSharedQueries(
                    area.getProjectArea().getIProcessArea(),
                    Collections.singletonList(area.getITeamRepository().loggedInContributor()),
                    QueryTypes.WORK_ITEM_QUERY,
                    IQueryDescriptor.DEFAULT_PROFILE,
                    null)) != null) {
                sharedSets.add(new RtcQueriesSetImpl(NbBundle.getMessage(RtcQueriesManagerImpl.class, "SharedQueries.individually.name"), Collections.singletonList(area.getITeamRepository().loggedInContributor()), this, qClient));
                contributorSharedSet = true;
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcQueriesManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        //TODO : for future : add support for ITeamAreaHierarchy and delevoplent lines
        ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
        for (Object obj : pm.getTeamAreas()) {
            if (obj instanceof ITeamAreaHandle) {
                ITeamAreaHandle teamAreaHandle = (ITeamAreaHandle) obj;
                try {
                    List list = Collections.singletonList(teamAreaHandle);
                    if (!teamAreaSharedSets.contains(teamAreaHandle.getItemId().getUuidValue())
                            && (tmp = qClient.findSharedQueries(
                            area.getProjectArea().getIProcessArea(),
                            list,
                            QueryTypes.WORK_ITEM_QUERY,
                            IQueryDescriptor.DEFAULT_PROFILE, null)) != null
                            && !tmp.isEmpty()) {
                        ITeamArea teamArea = (ITeamArea) area.getITeamRepository().itemManager().fetchCompleteItem(teamAreaHandle, IItemManager.DEFAULT, null);
                        sharedSets.add(new RtcQueriesSetImpl(teamArea.getName(), list, this, qClient));
                        teamAreaSharedSets.add(teamArea.getItemId().getUuidValue());
                    }
                } catch (TeamRepositoryException ex) {
                    RtcLogger.getLogger(RtcQueriesManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
            }

        }
    }

    @Override
    public RtcEditableQueriesSet getPersonalQueriesSet() {
        if (personalSet == null) {
            personalSet = new RtcEditableQueriesSetImpl(
                    NbBundle.getMessage(RtcEditableQueriesSetImpl.class, "PersonalQueriesSet.name"),
                    this,
                    qClient);
            initListeners();
        }
        return personalSet;
    }

    /*package*/ ActiveProjectAreaImpl getActiveProjectArea() {
        return area;
    }

    @Override
    synchronized public void synchronizeWithServer() {
        //TODO : for future : handle removing shared sets
        if (personalSet != null) {
            personalSet.synchronizeWithServer();
        }
        if (sharedInitialized) {
            for (RtcQueriesSetImpl set : sharedSets) {
                set.synchronizeWithServer();
            }
            updateSets();
        }
    }

    @Override
    public void handleEvents(List list) {
        requestProcessor.post(new Runnable() {

            @Override
            public void run() {
                synchronizeWithServer();
            }
        });

    }

    @Override
    public Pair<RtcQuery, RtcQueriesSet> findQuery(String queryId) {
        for (RtcQuery q : getPersonalQueriesSet().getQueries()) {
            if (q.getQueryIdentifier().equals(queryId)) {
                return new Pair<RtcQuery, RtcQueriesSet>(q, getPersonalQueriesSet());
            }
        }

        for (RtcQueriesSet set : getSharedQueriesSets()) {
            for (RtcQuery q : set.getQueries()) {
                if (q.getQueryIdentifier().equals(queryId)) {
                    return new Pair<RtcQuery, RtcQueriesSet>(q, set);
                }
            }
        }
        return null;
    }

    @Override
    public RtcQueryAttributeManager getQueryAttributeManager() {
        return RtcQueryAttributeManagerImpl.getFor(area);
    }

    @Override
    public ProjectArea getProjectArea() {
        return projectArea;
    }

    @Override
    public ActiveProjectArea getAvtiveProjectArea() {
        return area;
    }
}
