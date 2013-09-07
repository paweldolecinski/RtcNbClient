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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.expression;

import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeVariableImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeImpl;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.ClientEvaluationContext;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.ProgressExpressionVisitor;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.SelectClause;
import com.ibm.team.workitem.common.expression.SortCriteria;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.expression.variables.IEvaluationContext;
import com.ibm.team.workitem.common.expression.variables.RelativeDateVariable;
import com.ibm.team.workitem.common.internal.expression.EditableSortColumn.Direction;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.eclipse.core.runtime.IProgressMonitor;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpressionFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.query.RtcQueryColumnImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl.RtcQueryAttributeRtcObjectValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.variables.TimestampVariableImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcStatementCreationException;

/**
 *
 * @author Patryk Żywica
 */
public class RtcCreateEditableStatementVisitor extends ProgressExpressionVisitor {

    private class CandidateHelper {

        private HashMap<String, List<AttributeExpression>> fAttributeCandidates = new HashMap<String, List<AttributeExpression>>();

        public void addCandidate(AttributeExpression expression) throws TeamRepositoryException {
            addToAttributeCandidates(expression);
        }

        public Collection<List<AttributeExpression>> getAttributeCandidates() {
            return fAttributeCandidates.values();
        }

        private void addToAttributeCandidates(AttributeExpression expression) {
            String key = getAttributeKey(expression);
            List<AttributeExpression> attributeCandidates = fAttributeCandidates.get(key);
            if (attributeCandidates == null) {
                attributeCandidates = new ArrayList<AttributeExpression>(1);
                fAttributeCandidates.put(key, attributeCandidates);
            }
            attributeCandidates.add(expression);
        }

        private String getAttributeKey(AttributeExpression expression) {
            return expression.getAttributeIdentifier() + " " + expression.getOperation().getIdentifier(); //$NON-NLS-1$
        }
    }
    private final IEvaluationContext fContext;
    private RtcEditableStatementImpl fEditableStatement;
    private RtcEditableTermExpressionImpl fCurrentTerm;
//    private List<IQueryableAttribute> fColumns;
    private List<RtcQueryColumn> fSortColumns;
//    private ActiveProjectArea area;
    private RtcEditableExpressionFactoryImpl attributeExpressionFactory;
    private Map<String, Direction> cols = new LinkedHashMap<String, Direction>();
    private Map<String, Direction> sortCols = new LinkedHashMap<String, Direction>();
//    private IItemType iType=IWorkItem.ITEM_TYPE;
    private ActiveProjectAreaImpl area;

    public RtcCreateEditableStatementVisitor(ActiveProjectArea area) {
        //TODO : for future, it can be dangerous if multiple factories will be registered in Lookup
        attributeExpressionFactory = (RtcEditableExpressionFactoryImpl) Lookup.getDefault().lookup(RtcEditableExpressionFactory.class);
        this.area = (ActiveProjectAreaImpl) area;
        fContext = new ClientEvaluationContext(this.area.getProjectArea().getIProcessArea());

//        fColumns = new ArrayList<IQueryableAttribute>();
        fSortColumns = new ArrayList<RtcQueryColumn>();
    }

    public RtcEditableStatementImpl getEditableStatement() throws RtcStatementCreationException {
        if (fEditableStatement == null) {
            if (fCurrentTerm != null) {
                IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
                if (sortCols.isEmpty()) {
                    sortCols.put("id", Direction.DESCENDING);
                }
                fEditableStatement = new RtcEditableStatementImpl(fCurrentTerm, area);
                Iterator<Entry<String, Direction>> it = cols.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Direction> tmp = it.next();
                    IQueryableAttribute qa = null;
                    try {
                        qa = factory.findAttribute(area.getProjectArea().getIProcessArea(), tmp.getKey(), (IAuditableCommon) area.getITeamRepository().getClientLibrary(IAuditableCommon.class), null);
                    } catch (TeamRepositoryException ex) {
                        //Exceptions.printStackTrace(ex);
                        RtcLogger.getLogger(RtcCreateEditableStatementVisitor.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    }
                    String tmpName;
                    if (qa != null) {
                        tmpName = qa.getDisplayName();
                    } else {
                        tmpName = tmp.getKey();
                    }
                    RtcQueryColumnImpl c;
                    if (sortCols.containsKey(tmp.getKey())) {
                        c = new RtcQueryColumnImpl(
                                tmp.getKey(),
                                tmpName,
                                true,
                                true,
                                sortCols.get(tmp.getKey()) == Direction.ASCENDING ? true : false,
                                1);
                    } else {
                        c = new RtcQueryColumnImpl(tmp.getKey(), tmpName);
                    }
                    if (qa != null) {
                        if ("smallString".equals(qa.getAttributeType())
                                || "integer".equals(qa.getAttributeType())
                                || "interval".equals(qa.getAttributeType())
                                || "integer".equals(qa.getAttributeType())
                                || "float".equals(qa.getAttributeType())
                                || "severity".equals(qa.getAttributeType())
                                || "long".equals(qa.getAttributeType())
                                || "type".equals(qa.getAttributeType())
                                || "boolean".equals(qa.getAttributeType())
                                || "priority".equals(qa.getAttributeType())) {
                            c.setSize(15);
                        } else {
                            if ("timestamp".equals(qa.getAttributeType())
                                    || "category".equals(qa.getAttributeType())) {
                                c.setSize(20);
                            } else {
                                if ("contributor".equals(qa.getAttributeType())
                                        || "projectArea".equals(qa.getAttributeType())
                                        || "teamArea".equals(qa.getAttributeType())
                                        || "processArea".equals(qa.getAttributeType())
                                        || "workItem".equals(qa.getAttributeType())
                                        || "tags".equals(qa.getAttributeType())) {
                                    c.setSize(20);
                                } else {
                                    if ("mediumString".equals(qa.getAttributeType())
                                            || "mediumHtml".equals(qa.getAttributeType())) {
                                        c.setSize(60);
                                    } else {
                                        if ("string".equals(qa.getAttributeType())
                                                || "html".equals(qa.getAttributeType())) {
                                            c.setSize(70);
                                        } else {
                                            c.setSize(10);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    fSortColumns.add(c);
                }

                it = sortCols.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Direction> tmp = it.next();
                    IQueryableAttribute qa = null;
                    try {
                        qa = factory.findAttribute(area.getProjectArea().getIProcessArea(), tmp.getKey(), (IAuditableCommon) area.getITeamRepository().getClientLibrary(IAuditableCommon.class), null);
                    } catch (TeamRepositoryException ex) {
                        //Exceptions.printStackTrace(ex);
                        RtcLogger.getLogger(RtcCreateEditableStatementVisitor.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    }
                    String tmpName;
                    if (qa != null) {
                        tmpName = qa.getDisplayName();
                    } else {
                        tmpName = tmp.getKey();
                    }
                    if (!cols.containsKey(tmp.getKey())) {
                        fSortColumns.add(new RtcQueryColumnImpl(
                                tmp.getKey(),
                                tmpName,
                                false,
                                true,
                                tmp.getValue().equals(Direction.ASCENDING),
                                1));
                    }
                }
                for (RtcQueryColumn col : fSortColumns) {
                    fEditableStatement.addColumn(col);
                }
            } else {
                throw new RtcStatementCreationException(
                        NbBundle.getMessage(RtcCreateEditableStatementVisitor.class,
                        "badlyFormedQuery.error.msg"), new NullPointerException());
            }
        }
        return fEditableStatement;
    }

    private IQueryableAttribute resolveAttribute(AttributeExpression expression, IProgressMonitor monitor) throws TeamRepositoryException {
        return expression.getAttribute(fContext, monitor);
    }

    @Override
    public boolean visit(Term term, IProgressMonitor monitor) throws TeamRepositoryException {
        int termSize = term.getExpressions().size();
        if (monitor != null) {
            monitor.beginTask(null, 10 * termSize);
        }
        RtcEditableTermExpressionImpl oldTerm;
        if (fCurrentTerm == null) {
            oldTerm = new RtcEditableTermExpressionImpl(area);
            oldTerm.setOperator(Term.Operator.AND);
        } else {
            oldTerm = fCurrentTerm;
        }

        CandidateHelper helper = new CandidateHelper();

        for (Expression expression : term.getExpressions()) {
            if (expression instanceof AttributeExpression) {
                helper.addCandidate((AttributeExpression) expression);
            }
        }
        ArrayList<RtcEditableAttributeExpressionImpl> editableExpressions = new ArrayList<RtcEditableAttributeExpressionImpl>();

        RtcEditableTermExpressionImpl parentTerm = oldTerm;
        if (helper.getAttributeCandidates().size() != 1) {
            parentTerm = new RtcEditableTermExpressionImpl(oldTerm, area, term.isInternal());
        } else {
            if (helper.getAttributeCandidates().size() == 1) {
                Iterator<List<AttributeExpression>> it = helper.getAttributeCandidates().iterator();
                if (it.next().size() != term.getExpressions().size()) {
                    parentTerm = new RtcEditableTermExpressionImpl(oldTerm, area, term.isInternal());
                }
            } else {
                if (isLogicalRootContainer(oldTerm)) {
                    parentTerm = new RtcEditableTermExpressionImpl(oldTerm, area, term.isInternal());
                } else {
                    if (term.isInternal()) {
                        parentTerm = new RtcEditableTermExpressionImpl(oldTerm, area, term.isInternal());
                    }
                }
            }
        }


        for (List<AttributeExpression> candidateList : helper.getAttributeCandidates()) {
            if (!candidateList.isEmpty()) {

                AttributeExpression master = candidateList.get(0);
                IQueryableAttribute attribute = resolveAttribute(master, null);
                RtcEditableAttributeExpressionImpl editableAttributeExpression = createRtcEditableAttributeExression(attribute, parentTerm);
                editableAttributeExpression.setSelectedAttributeOperation(
                        RtcEditableAttributeExpressionImpl.toRtcAttributeOperation(master.getOperation()));
                ValueCreator vc = null;
                for (AttributeExpression candidate : candidateList) {
                    if (candidate instanceof VariableAttributeExpression) {
                        VariableAttributeExpression var = (VariableAttributeExpression) candidate;
                        if (var.getVariable() instanceof RelativeDateVariable) {
                            editableAttributeExpression.addSelectedVariable(new TimestampVariableImpl((RelativeDateVariable) var.getVariable()));
                        } else {
                            editableAttributeExpression.addSelectedVariable(new RtcQueryAttributeVariableImpl(var.getVariable()));
                        }
                    } else {
                        Object resolvedValue = candidate.getResolvedValue(fContext, monitor);
//                        if(resolvedValue instanceof Identifier){
//                            IWorkItemClient wiClient = (IWorkItemClient) area.getITeamArea().getClientLibrary(IWorkItemClient.class);
//                            IAttribute iAttribute = wiClient.findAttribute(area.getProjectArea(), attribute.getIdentifier(), null);
//                            IEnumeration iEnumeration = wiClient.resolveEnumeration(iAttribute, null);
//                            ILiteral iLiteral = iEnumeration.findEnumerationLiteral((Identifier) resolvedValue);
//                            String resolvedValueName=iLiteral.getName();
                        //check url against null value
//                            Icon resolvedValueIcon=new ImageIcon(iLiteral.getIconURL());
//                            editableAttributeExpression.addSelectedValue(new RtcQueryAttributeValueImpl(resolvedValue,resolvedValueName,resolvedValueIcon));
//                        }else{
                        if (vc == null) {
                            vc = editableAttributeExpression.getQueryAttribute().getLookup().lookup(ValueCreator.class);
                        }
                        if (vc != null) {
                            try {
                                editableAttributeExpression.addSelectedValue(vc.getValueForObject(resolvedValue));
                            } catch (IllegalArgumentException ex) {
                                if (resolvedValue != null) {
                                    //message should be log only if resolved value is not null
                                    RtcLogger.getLogger().log(Level.WARNING, NbBundle.getMessage(RtcCreateEditableStatementVisitor.class, "CannotResolveValue.error"));
                                }
//                                ex.printStackTrace();
                                editableAttributeExpression.addSelectedValue(new RtcQueryAttributeRtcObjectValue(resolvedValue));
                            }
                        } else {
                            editableAttributeExpression.addSelectedValue(new RtcQueryAttributeRtcObjectValue(resolvedValue));
                            //Here we have different situation, we have not vc, is which means somethong is broken, or
                            //given value comes from unsupported value.
//                            //System.out.println(resolvedValue!=null?resolvedValue.toString():"null");
                            RtcLogger.getLogger().log(Level.SEVERE, NbBundle.getMessage(RtcCreateEditableStatementVisitor.class, "CannotResolveValue.error"));
                        }

//                        }

                    }
                }

                editableExpressions.add(editableAttributeExpression);
            }
        }
//        monitor.worked(3 * termSize);
        if (!term.isInternal() && isMultiValueTerm(term, editableExpressions) && !isLogicalRootContainer(oldTerm)) {

            assert (parentTerm == oldTerm);
            if (oldTerm == null) {
                throw new IllegalStateException();
            }
            oldTerm.addSubExpression(editableExpressions.get(0));
            fCurrentTerm = oldTerm;

        } else {
            assert ((parentTerm != oldTerm));
            RtcEditableTermExpressionImpl newTerm = parentTerm;
            newTerm.setOperator(term.getOperator());

            for (RtcEditableExpression editableExpression : editableExpressions) {
                newTerm.addSubExpression(editableExpression);
            }

            oldTerm.addSubExpression(newTerm);

            fCurrentTerm = newTerm;
        }
        // depth first
        for (Expression expression : term.getExpressions()) {
            expression.accept(this, null);

        }

        fCurrentTerm = oldTerm;
        if (monitor != null) {
            monitor.done();
        }
        return false;
    }

    private boolean isLogicalRootContainer(RtcEditableTermExpressionImpl term) {
        if (term == null) {
            return true;
        }
        return (term.getParent() == null);
    }

    @Override
    public boolean visit(SelectClause selectClause, IProgressMonitor monitor) throws TeamRepositoryException {
//        iType = selectClause.getItemType();
        if (selectClause.getColumnIdentifiers().isEmpty()) {
            cols.put("workItemType", null);
            cols.put("id", null);
            cols.put("internalState", null);
            cols.put("internalPriority", null);
            cols.put("internalSeverity", null);
            cols.put("summary", null);
            cols.put("owner", null);
            cols.put("creator", null);
        } else {
            for (String identifier : selectClause.getColumnIdentifiers()) {
                cols.put(identifier, null);
            }
        }
        return super.visit(selectClause, monitor);
    }

    @Override
    public boolean visit(SortCriteria sortCriteria, IProgressMonitor monitor) throws TeamRepositoryException {
        //TODO : for future : add support for sorting index, now it returns 1 for all sort columns
        IQueryableAttribute attribute = sortCriteria.getAttribute(fContext, monitor);
        if (attribute != null) {
            Direction direction = sortCriteria.isAscending() ? Direction.ASCENDING : Direction.DESCENDING;
            sortCols.put(attribute.getIdentifier(), direction);
        }
        return super.visit(sortCriteria, monitor);
    }

    private boolean isMultiValueTerm(Term term, ArrayList<RtcEditableAttributeExpressionImpl> editableExpressions) {
        return editableExpressions.size() == 1 && editableExpressions.get(0).getSelectedValues().length + editableExpressions.get(0).getSelectedVariables().length == term.getExpressions().size();
    }

    private RtcEditableAttributeExpressionImpl createRtcEditableAttributeExression(IQueryableAttribute qAttribute, RtcEditableTermExpressionImpl oldTerm) {
        RtcQueryAttributeImpl queryAttribute =
                RtcQueryAttributeManagerImpl.getFor(area).getQueryAttribute(qAttribute);
        return attributeExpressionFactory.createAttributeExpression(queryAttribute, oldTerm);
    }
}
