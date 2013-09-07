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

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryDescriptor;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.query.QueryTypes;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement.RtcStatementEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement.RtcStatementListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression.RtcCreateEditableStatementVisitor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression.RtcEditableStatementImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryResult;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcStatementCreationException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 * @see RtcQuery
 * @author Patryk Żywica
 */
public class RtcQueryImpl implements RtcQuery, RtcStatementListener {

    private String editableName;
    private String name;
    private RtcEditableStatementImpl statement;
    private IQueryDescriptor query;
    private ActiveProjectAreaImpl area;
    private IQueryClient qClient;
    private RtcQueriesManagerImpl manager;
    private Contributor contributor;
    private EventSourceSupport<RtcQuery.RtcQueryEvent> eventSource = new EventSourceSupport<RtcQueryEvent>();

    /*package*/ RtcQueryImpl(IQueryDescriptor query, RtcQueriesManagerImpl manager) {
        assert (query != null);
        this.query = query;
        this.area = manager.getActiveProjectArea();
        this.manager = manager;
        this.name = query.getName();
        this.editableName = name;
        this.contributor = area.getLookup().lookup(ContributorManager.class).getLoggedInContributor();
    }

    /**
     * @return unique ID of this query
     */
    @Override
    public String getQueryIdentifier() {
        return query.getItemId().getUuidValue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEditableName() {
        return editableName;
    }

    /**
     * @see RtcQuery
     * @return
     */
    @Override
    public synchronized RtcEditableStatementImpl getStatement() throws RtcStatementCreationException {
        assert (!EventQueue.isDispatchThread());
        if (statement == null) {
            createStatement();
        }
        return statement;
    }

    /**
     * TODO : for future : this method should be changed to return new implementation
     * of list, that supports lazy results initialisation. This list implementation should
     * be used in editable and non editable query.
     * @return
     */
    @Override
    public List<RtcQueryResult> getQueryResults() {
        assert (!EventQueue.isDispatchThread());
        try {
            Expression expressionToRun = getStatement().createStatement();
            IQueryResult<IResolvedResult<IWorkItem>> results =
                    getQueryClient().getResolvedExpressionResults(area.getProjectArea().getIProcessArea(), expressionToRun, IWorkItem.FULL_PROFILE);
            List<RtcQueryResult> returnList = new ArrayList<RtcQueryResult>(results.getResultSize(null).getTotalAvailable());
            
            while (results.hasNext(null)) {
                IResolvedResult<IWorkItem> tmp = results.next(null);
                returnList.add(new RtcQueryResultImpl(
                        tmp.getItem(),
                        area,
                        tmp.getScore()));
            }
            return returnList;
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcQueryImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return new ArrayList<RtcQueryResult>();
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcQueryImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return new ArrayList<RtcQueryResult>();
        }
    }

    @Override
    public void statementChanged(RtcStatementEvent event) {
        fireEvent(RtcQueryEvent.STATEMENT_CHANGED);
    }

    private synchronized void createStatement() throws RtcStatementCreationException {
        try {
            RtcCreateEditableStatementVisitor visitor = new RtcCreateEditableStatementVisitor(area);
            query.getExpression().accept(visitor, null);
            statement = visitor.getEditableStatement();
            statement.initExpressionListener();
            statement.addListener(this);
        } catch (TeamRepositoryException ex) {
            throw new RtcStatementCreationException(ex.getLocalizedMessage(), ex);
        } catch (Exception ex) {
            throw new RtcStatementCreationException(
                    NbBundle.getMessage(RtcEditableQueryImpl.class, "InternalStatementCreation.error"),
                    ex);
        }
    }

    @Override
    public RtcQueriesManager getQueriesManager() {
        return manager;
    }
    /* package */

    void synchronizeWithServer(IQueryDescriptor newDescriptor) {
        boolean fireS = false, fireN = false;
        if (!name.equals(newDescriptor.getName())) {
            fireN = true;
        }
        name = newDescriptor.getName();
        editableName = newDescriptor.getName();
        query = newDescriptor;
        if (statement != null) {
            statement = null;
            fireS = true;
        }
        if (fireS) {
            fireEvent(RtcQueryEvent.QUERY_SYNCHRONIZED_WITH_SERVER);
        } else {
            if (fireN) {
                fireEvent(RtcQueryEvent.NAME_CHANGED);
            }
        }
    }

    private IQueryClient getQueryClient() {
        if (qClient == null) {
            qClient = (IQueryClient) area.getITeamRepository().getClientLibrary(IQueryClient.class);
        }
        return qClient;
    }

    @Override
    public RtcQueryColumn[] getColumns() {
        try {
            return getStatement().getColumns();
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
            return new RtcQueryColumn[]{};
        }
    }

    @Override
    public void saveAs(String name) throws RtcSaveException {
        assert (!EventQueue.isDispatchThread());
        if (getEditableName().equals("")) {
            throw new RtcSaveException(NbBundle.getMessage(RtcEditableQueryImpl.class, "EmptyQueryName.msg"), null);
        }
        IQueryDescriptor newQuery = null;
        try {
            newQuery = getQueryClient().createQuery(
                    area.getProjectArea().getIProcessArea(),
                    QueryTypes.WORK_ITEM_QUERY,
                    name,
                    getStatement().createStatement());

            getQueryClient().getWorkingCopyManager().connect(newQuery, null);
            getQueryClient().save(newQuery, null);
//                fireEvent(RtcQueryEvent.);
        } catch (TeamRepositoryException ex) {
            throw new RtcSaveException(ex.getLocalizedMessage(), ex);
        } catch (RtcStatementCreationException ex) {
            throw new RtcSaveException(ex.getLocalizedMessage(), ex);
        } finally {
            if (newQuery != null) {
                getQueryClient().getWorkingCopyManager().disconnect(newQuery);
            }
        }
    }

    @Override
    public void addColumn(RtcQueryColumn column) {
        try {
            getStatement().addColumn(column);
            fireEvent(RtcQueryEvent.COLUMN_ADDED);
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
        }
    }

    @Override
    public void removeColumn(RtcQueryColumn column) {
        try {
            getStatement().removeColumn(column);
            fireEvent(RtcQueryEvent.COLUMN_ADDED);
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
        }
    }

    @Override
    public void discardChanges() {
        assert (!EventQueue.isDispatchThread());
        editableName = name;
        if (statement != null) {
            statement.removeListener(this);
            statement = null;
        }
    }

    @Override
    public Contributor getCreator() {
        return contributor;
    }

    public final void removeListener(EventListener<RtcQueryEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcQueryEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcQueryEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
