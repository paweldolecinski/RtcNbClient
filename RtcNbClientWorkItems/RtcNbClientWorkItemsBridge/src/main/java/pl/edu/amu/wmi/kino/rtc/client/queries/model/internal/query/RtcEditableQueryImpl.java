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
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.SelectClause;
import com.ibm.team.workitem.common.expression.Statement;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.expression.variables.WorkItemTypeVariable;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.WorkItemTypes;
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
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryResult;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement.RtcStatementEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement.RtcStatementListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression.RtcCreateEditableStatementVisitor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression.RtcEditableStatementImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcStatementCreationException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 * @see RtcEditableQuery
 * @author Patryk Żywica
 */
public class RtcEditableQueryImpl implements RtcEditableQuery, RtcStatementListener {

    private String editableName;
    private String name;
    private RtcEditableStatementImpl statement;
    private IQueryDescriptor query;
    private ActiveProjectAreaImpl area;
    private boolean modified = false;
    private IQueryClient qClient;
    private RtcQueriesManagerImpl manager;
    private Contributor contributor;
    private EventSourceSupport<RtcQueryEvent> eventSource = new EventSourceSupport<RtcQueryEvent>();

    //TODO : bikol : change to package
    /**
     * DO NOT USE
     * @return
     */
//    public Expression getExpression() {
//        if (query != null) {
//            return query.getExpression();
//        } else {
//            return new Term();
//        }
//
//    }
//
//    /**
//     * DO NOT USE
//     * @return
//     */
//    public RtcActiveProjectArea getArea() {
//        return area;
//    }
//////////////////////////////
    ////////////////////////////////

    /*package*/ RtcEditableQueryImpl(IQueryDescriptor query, RtcQueriesManagerImpl manager) {
        this.manager = manager;
        this.area = manager.getActiveProjectArea();
        if (query == null) {
            query = createNewQueryDescriptor();
            this.modified = true;
        }
        this.query = query;
        this.name = this.query.getName();
        this.editableName = name;
        this.contributor = area.getLookup().lookup(ContributorManager.class).getLoggedInContributor();
    }

    /*package*/ RtcEditableQueryImpl(RtcQueriesManagerImpl manager) {
        this(null, manager);
    }

    private IQueryDescriptor createNewQueryDescriptor() {
        Statement s = null;
        try {
            Term hiddenTerm = new Term(Operator.AND);
            hiddenTerm.setInternal(true);
            IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
            IAuditableClient aClient = (IAuditableClient) area.getITeamRepository().getClientLibrary(IAuditableClient.class);
            IQueryableAttribute projectAreaAttribute = factory.findAttribute(area.getProjectArea().getIProcessArea(), IWorkItem.PROJECT_AREA_PROPERTY, aClient, null);
            IQueryableAttribute workItemTypeAttribute = factory.findAttribute(area.getProjectArea().getIProcessArea(), IWorkItem.TYPE_PROPERTY, aClient, null);
            hiddenTerm.add(new AttributeExpression(projectAreaAttribute, AttributeOperation.EQUALS, area.getProjectArea().getIProcessArea()));
            hiddenTerm.add(new VariableAttributeExpression(workItemTypeAttribute, AttributeOperation.EQUALS, new WorkItemTypeVariable(WorkItemTypes.TYPE_CATEGORY)));

            Term rootTerm = new Term(Operator.AND);

            s = new Statement(
                    new SelectClause(IWorkItem.ITEM_TYPE),
                    new Term(Operator.AND, new Expression[]{hiddenTerm, rootTerm}));
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.WARNING, NbBundle.getMessage(RtcEditableQueryImpl.class, "NewQueryDescriptorCreation.error"), ex);
            s = new Statement(new SelectClause(IWorkItem.ITEM_TYPE), new Term(Operator.AND));
        } finally {
            return getQueryClient().createQuery(
                    area.getProjectArea().getIProcessArea(),
                    QueryTypes.WORK_ITEM_QUERY,
                    NbBundle.getMessage(RtcEditableQueryImpl.class, "UnsavedQuery.name"),
                    s);
        }
    }

    public IQueryDescriptor getQueryDescriptor() {
        return query;
    }

    /**
     * @return unique ID of this query
     */
    @Override
    public String getQueryIdentifier() {
        return query.getItemId().getUuidValue();
    }

    /**
     * @see RtcEditableQuery
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEditableName() {
        return editableName;
    }

    @Override
    public void setEditableName(String name) {
        this.editableName = name;
        modified = true;
        fireEvent(RtcQueryEvent.EDITABLE_NAME_CHANGED);
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void save() throws RtcSaveException {
        assert (!EventQueue.isDispatchThread());
        if (getEditableName().equals("")) {
            throw new RtcSaveException(NbBundle.getMessage(RtcEditableQueryImpl.class, "EmptyQueryName.msg"), null);
        }
        IQueryDescriptor newQuery;
        newQuery = (IQueryDescriptor) query.getWorkingCopy();
        try {
            getQueryClient().getWorkingCopyManager().connect(newQuery, null);
            newQuery.setName(getEditableName());
            try {
                newQuery.setExpression(getStatement().createStatement());
            } catch (RtcStatementCreationException ex) {
                throw new RtcSaveException(ex.getLocalizedMessage(), ex);
            }
            try {
                query = getQueryClient().save(newQuery, null);
                name = editableName;
                modified = false;
                fireEvent(RtcQueryEvent.QUERY_SAVED);
            } catch (TeamRepositoryException ex) {
                throw new RtcSaveException(ex.getLocalizedMessage(), ex);
            } finally {
                getQueryClient().getWorkingCopyManager().disconnect(newQuery);
            }
        } catch (TeamRepositoryException ex) {
            throw new RtcSaveException(ex.getLocalizedMessage(), ex);
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
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return new ArrayList<RtcQueryResult>();
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return new ArrayList<RtcQueryResult>();
        }
    }

    @Override
    public void discardChanges() {
        assert (!EventQueue.isDispatchThread());
        editableName = name;
        if (statement != null) {
            statement.removeListener(this);
            statement = null;
//        try {
//            createStatement();
        }
        modified = false;
//            fireEvent(RtcQueryEvent.CHANGES_DISCARDED);
//        } catch (RtcStatementCreationException ex) {
//            RtcLogger.getLogger().log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "UnableToDiscardChanges.error"));
//        }
    }

    @Override
    public void statementChanged(RtcStatementEvent event) {
        modified = true;
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
            //ex.printStackTrace();
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new RtcStatementCreationException(
                    NbBundle.getMessage(RtcEditableQueryImpl.class, "InternalStatementCreation.error"),
                    ex);
        }
    }

    @Override
    public RtcQueriesManager getQueriesManager() {
        return manager;
    }

    private IQueryClient getQueryClient() {
        if (qClient == null) {
            qClient = (IQueryClient) area.getITeamRepository().getClientLibrary(IQueryClient.class);
        }
        return qClient;
    }
    /* package */

    synchronized void synchronizeWithServer(IQueryDescriptor newDescriptor) {
        boolean fireS = false, fireN = false;
        if (!isModified()) {
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
        } else {
            if (!name.equals(newDescriptor.getName())) {
                name = newDescriptor.getName();
                fireEvent(RtcQueryEvent.NAME_CHANGED);
            }
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.INFO, NbBundle.getMessage(RtcEditableQueryImpl.class, "ModifiedQuerySynchronization.msg"));
        }
    }

    @Override
    public void addColumn(RtcQueryColumn column) {
        try {
            getStatement().addColumn(column);
            fireEvent(RtcQueryEvent.COLUMN_ADDED);
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
        }
    }

    @Override
    public void removeColumn(RtcQueryColumn column) {
        try {
            getStatement().removeColumn(column);
            fireEvent(RtcQueryEvent.COLUMN_ADDED);
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
        }
    }

    @Override
    public RtcQueryColumn[] getColumns() {
        try {
            return getStatement().getColumns();
        } catch (RtcStatementCreationException ex) {
            RtcLogger.getLogger(RtcEditableQueryImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcEditableQueryImpl.class, "ColumnCreation.error"), ex);
            return new RtcQueryColumn[]{};
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
