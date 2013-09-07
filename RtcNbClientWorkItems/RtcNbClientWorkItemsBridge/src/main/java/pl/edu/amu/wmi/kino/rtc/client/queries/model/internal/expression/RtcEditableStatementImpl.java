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

import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeManagerImpl;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.SelectClause;
import com.ibm.team.workitem.common.expression.SortCriteria;
import com.ibm.team.workitem.common.expression.Statement;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.util.ArrayList;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression.RtcExpressionEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression.RtcExpressionListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression.RtcTermOperator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.query.RtcQueryColumnImpl;

/**
 * @see RtcEditableStatement
 * @author Patryk Żywica
 */
public class RtcEditableStatementImpl extends RtcEditableStatement implements RtcExpressionListener {

    private RtcEditableTermExpressionImpl rootTerm, adjustedRootTerm;
    private ArrayList<RtcQueryColumn> columns = new ArrayList<RtcQueryColumn>();
    private ActiveProjectAreaImpl area;


    /* package */ RtcEditableStatementImpl(RtcEditableTermExpressionImpl term, ActiveProjectAreaImpl area) {
        this.rootTerm = term;
        this.area = area;
        this.adjustedRootTerm = adjustRootTerm(term);
    }

    public Statement createStatement() {
        SelectClause selectClause = new SelectClause(IWorkItem.ITEM_TYPE);
        ArrayList<SortCriteria> sort = new ArrayList<SortCriteria>();
        for (RtcQueryColumn col : columns) {
            if (col instanceof RtcQueryColumnImpl) {
                //TODO : bikol : sort by sort index here
                if (col.isSortColumn()) {
                    sort.add(new SortCriteria(((RtcQueryColumnImpl) col).getColumnIdentifier(), col.isAscending()));
                }
                selectClause.addColumnIdentifier(((RtcQueryColumnImpl) col).getColumnIdentifier());
            } else {
                throw new IllegalArgumentException("Invalid statement column type");
            }
        }
        Expression rootExp = rootTerm.createExpression();
        assert (rootExp != null);
        if (rootExp instanceof Term) {
            Term rootT = (Term) rootExp;
            if (rootT.isInternal()) {
                rootExp = new Term(Term.Operator.AND, new Expression[]{rootT});
            }
        }
        return new Statement(
                selectClause,
                rootExp,
                sort.toArray(new SortCriteria[]{}));
    }

    /**
     * never null
     * @return
     */
    @Override
    public RtcEditableTermExpressionImpl getRootTerm() {
        return adjustedRootTerm;
    }

    @Override
    public void expressionChanged(RtcExpressionEvent e) {
        fireEvent(RtcStatementEvent.ROOT_TERM_CHANGED);
    }

    /**
     * this method is used to implement methods in Query
     * @return
     */
    public RtcQueryColumn[] getColumns() {
        return columns.toArray(new RtcQueryColumn[]{});
    }

    /**
     * this method is used to implement methods in Query
     * @param col
     */
    public void addColumn(RtcQueryColumn col) {
        columns.add(col);
    }

    /**
     * this method is used to implement methods in Query
     * @param col
     */
    public void removeColumn(RtcQueryColumn col) {
        columns.remove(col);
    }

    @Override
    public RtcQueryAttributeManagerImpl getQueryAttributeManager() {
        return RtcQueryAttributeManagerImpl.getFor(area);
    }

    public void initExpressionListener() {

        rootTerm.initExpressionListeners();
        rootTerm.addListener(this);

    }

    private RtcEditableTermExpressionImpl adjustRootTerm(RtcEditableTermExpressionImpl term) {
        int hiddenTerms = 0;

        RtcEditableTermExpressionImpl termToAdjust;
        if (term.getSubExpressions().length == 1 && term.getSubExpressions()[0] instanceof RtcEditableTermExpressionImpl && !((RtcEditableTermExpressionImpl) term.getSubExpressions()[0]).isHidden()) {
            termToAdjust = (RtcEditableTermExpressionImpl) term.getSubExpressions()[0];
        } else {
            termToAdjust = term;
        }

        ArrayList<RtcEditableTermExpressionImpl> rootCandidates = new ArrayList<RtcEditableTermExpressionImpl>();
        ArrayList<RtcEditableAttributeExpressionImpl> attributeCandidates = new ArrayList<RtcEditableAttributeExpressionImpl>();
        for (RtcEditableExpression child : termToAdjust.getSubExpressions()) {
            if (child instanceof RtcEditableTermExpressionImpl) {
                RtcEditableTermExpressionImpl candidate = (RtcEditableTermExpressionImpl) child;
                if (!candidate.isHidden()) {
                    rootCandidates.add(candidate);
                } else {
                    hiddenTerms++;
                }
            } else {
                if (child instanceof RtcEditableAttributeExpressionImpl) {
                    //in this case will have to add it to separeate candidate list
                    RtcEditableAttributeExpressionImpl can = (RtcEditableAttributeExpressionImpl) child;
                    attributeCandidates.add(can);
                }
            }
        }
        //it is for predefined queries in RTC, if all
        if (attributeCandidates.size() == termToAdjust.getSubExpressions().length) {
            termToAdjust.setAdjustedRoot(true);
            return termToAdjust;
        }
        if (hiddenTerms > 0 && !attributeCandidates.isEmpty()) {
            //create new root Term candidate with all attributes
            RtcEditableTermExpressionImpl candidate = new RtcEditableTermExpressionImpl(termToAdjust, area);
            candidate.setOperator(termToAdjust.getOperator());
            for (RtcEditableAttributeExpressionImpl ex : attributeCandidates) {
                termToAdjust.removeSubExpression(ex);
                ex.changeParentTerm(candidate);
                candidate.addSubExpression(ex);
            }
            termToAdjust.addSubExpression(candidate);
            rootCandidates.add(candidate);
        }
        if (hiddenTerms > 0) {
            if (rootCandidates.size() == 1 && termToAdjust.getSubExpressions().length - hiddenTerms == 1) {
                RtcEditableTermExpressionImpl t = rootCandidates.get(0);
                t.setAdjustedRoot(true);
                return t;
            }
            if (rootCandidates.isEmpty() && termToAdjust.getSubExpressions().length - hiddenTerms == 0) {
                //in this case it means that we have only hidden terms, so we have to add new one as root
                RtcEditableTermExpressionImpl newRoot = new RtcEditableTermExpressionImpl(termToAdjust, area);
                termToAdjust.addSubExpression(newRoot);
                newRoot.setOperator(RtcTermOperator.AND);
                newRoot.setAdjustedRoot(true);
                return newRoot;
            }
        }
        termToAdjust.setAdjustedRoot(true);
        return termToAdjust;
    }
}
