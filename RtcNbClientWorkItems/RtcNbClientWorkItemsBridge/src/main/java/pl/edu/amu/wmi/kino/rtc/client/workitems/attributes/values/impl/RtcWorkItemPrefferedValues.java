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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkItemPrefferedValues implements RtcWorkItemAttributePrefferedValues<RtcWorkItem> {

    private ActiveProjectAreaImpl area;

    public RtcWorkItemPrefferedValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public List<RtcWorkItem> getPrefferedValues() {
        IQueryClient qClient = (IQueryClient) area.getITeamRepository().getClientLibrary(IQueryClient.class);
        IAuditableClient aClient = (IAuditableClient) area.getITeamRepository().getClientLibrary(IAuditableClient.class);
        Expression[] exprs = new Expression[3];
        IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);

        IQueryableAttribute qAttr;
        try {
//            qAttr = factory.findAttribute(area.getProjectArea(), "projectArea", aClient, null);
//            exprs[0] = new AttributeExpression(qAttr, AttributeOperation.EQUALS, area.getProjectArea());
//            qAttr = factory.findAttribute(area.getProjectArea(), "workItemType", aClient, null);
//            exprs[1] = new AttributeExpression(qAttr, AttributeOperation.EQUALS, IWorkItem.ITEM_TYPE);
            IQueryableAttribute qAttr1 = factory.findAttribute(area.getProjectArea().getIProcessArea(), "owner", aClient, null);
            IQueryableAttribute qAttr3 = factory.findAttribute(area.getProjectArea().getIProcessArea(), "creator", aClient, null);
            IQueryableAttribute qAttr2 = factory.findAttribute(area.getProjectArea().getIProcessArea(), "resolver", aClient, null);
//            exprs[2] = new Term(Operator.OR, new Expression[]{
//                        new AttributeExpression(qAttr1, AttributeOperation.EQUALS, area.getITeamArea().loggedInContributor()),
//                        new AttributeExpression(qAttr2, AttributeOperation.EQUALS, area.getITeamArea().loggedInContributor()),
//                        new AttributeExpression(qAttr3, AttributeOperation.EQUALS, area.getITeamArea().loggedInContributor())});
            Term root = new Term(Operator.OR, new Expression[]{
                        new AttributeExpression(qAttr1, AttributeOperation.EQUALS, area.getITeamRepository().loggedInContributor()),
                        new AttributeExpression(qAttr2, AttributeOperation.EQUALS, area.getITeamRepository().loggedInContributor()),
                        new AttributeExpression(qAttr3, AttributeOperation.EQUALS, area.getITeamRepository().loggedInContributor())});
            IQueryResult<IResolvedResult<IWorkItem>> results =
                    qClient.getResolvedExpressionResults(area.getProjectArea().getIProcessArea(), root, IWorkItem.SMALL_PROFILE);
            List<RtcWorkItem> wis = new LinkedList<RtcWorkItem>();
            List<Integer> ids = new ArrayList<Integer>();
            while (results.hasNext(null)) {
                ids.add(results.next(null).getItem().getId());
                //wis.add(new RtcWorkItemImpl());
            }
            wis.addAll(Arrays.asList(area.getLookup().lookup(RtcWorkItemsManager.class).findWorkItems(ids.toArray(new Integer[]{}))));
            Collections.sort(wis, new Comparator<RtcWorkItem>() {

                @Override
                public int compare(RtcWorkItem o1, RtcWorkItem o2) {
                    return o2.getId()-o1.getId();
                }
            });
            return wis;
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger().log(Level.WARNING, ex.getLocalizedMessage());
            return new LinkedList<RtcWorkItem>();
        }


    }

    @Override
    public void setConstraint(Object constraint) {
    }

    @Override
    public boolean isConstraint() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
