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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.IterationImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.TeamAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.RtcPlanImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;

import com.ibm.team.apt.internal.client.IterationPlanData;
import com.ibm.team.apt.internal.client.ResolvedIterationPlan;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.ITeamRepository;
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
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.expression.variables.WorkItemTypeVariable;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.WorkItemTypes;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanItemsManagerImpl implements RtcPlanItemsManager {

    private LinkedList<RtcPlanItem> items = null;
    private final ResolvedIterationPlan iterationPlan;
    private final RtcPlanImpl plan;
    private final IterationPlanData planData;
    private EventSourceSupport<RtcPlanItemsManager.RtcPlanItemsManagerEvent> eventSource = new EventSourceSupport<RtcPlanItemsManager.RtcPlanItemsManagerEvent>();

    /**
     *
     * @param plan
     * @param planData
     * @param iterationPlan
     */
    public RtcPlanItemsManagerImpl(RtcPlan plan, IterationPlanData planData, ResolvedIterationPlan iterationPlan) {
        this.plan = (RtcPlanImpl) plan;
        this.planData = planData;
        this.iterationPlan = iterationPlan;

    }

    @Override
    public RtcPlanItem[] getPlanItems() {
        if (items == null) {
            fetchItems();
        }
        return items.toArray(new RtcPlanItem[]{});
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz) {
        if (items == null) {
            fetchItems();
        }
        LinkedList<T> res = new LinkedList<T>();
        for (RtcPlanItem item : items) {
            if (clazz.isInstance(item)) {
                res.add((T) item);
            }
        }
        return res.toArray((T[]) Array.newInstance(clazz, 0));
    }

    @Override
    public RtcPlanItem[] getPlanItems(RtcPlanItemType type) {
        if (items == null) {
            fetchItems();
        }
        LinkedList<RtcPlanItem> res = new LinkedList<RtcPlanItem>();
        for (RtcPlanItem item : items) {
            if (item.getPlanItemType().equals(type)) {
                res.add(item);
            }
        }
        return items.toArray(new RtcPlanItem[]{});
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RtcPlanItem> T[] getPlanItems(Class<T> clazz, RtcPlanItemType type) {
        if (items == null) {
            fetchItems();
        }
        LinkedList<T> res = new LinkedList<T>();
        for (RtcPlanItem item : items) {
            if (clazz.isInstance(item) && item.getPlanItemType().equals(type)) {
                res.add((T) item);
            }
        }
        return res.toArray((T[]) Array.newInstance(clazz, 0));
    }

    private void fetchItems() {
        items = new LinkedList<RtcPlanItem>();

        if (plan != null) {
            ProcessArea owner = plan.getOwner();
            IProjectAreaHandle area = null;
            if (owner instanceof ProjectAreaImpl) {
                area = ((ProjectAreaImpl) owner).getIProcessArea();
            } else if (owner instanceof TeamAreaImpl) {
                area = ((TeamAreaImpl) owner).getIProcessArea().getProjectArea();
            }
            ITeamRepository repo = (ITeamRepository) area.getOrigin();
            IterationImpl iteration = (IterationImpl) plan.getIteration();


            try {
                IQueryClient qClient = (IQueryClient) repo.getClientLibrary(IQueryClient.class);
                IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
                IAuditableClient aClient = (IAuditableClient) repo.getClientLibrary(IAuditableClient.class);


                //this is hidden expression part, it is responsible for selecting project area and item type( workITem)
                Term hiddenTerm = new Term(Operator.AND);
                hiddenTerm.setInternal(true);

                IQueryableAttribute projectAreaAttribute = factory.findAttribute(area, IWorkItem.PROJECT_AREA_PROPERTY, aClient, null);
                IQueryableAttribute workItemTypeAttribute = factory.findAttribute(area, IWorkItem.TYPE_PROPERTY, aClient, null);
                hiddenTerm.add(new AttributeExpression(projectAreaAttribute, AttributeOperation.EQUALS, area));
                hiddenTerm.add(new VariableAttributeExpression(workItemTypeAttribute, AttributeOperation.EQUALS, new WorkItemTypeVariable(WorkItemTypes.TYPE_CATEGORY)));

                //this is main expression part, here we can query about any workItem attiribute
                //or even more any QueryableAttribute
                Term mainTerm = new Term(Term.Operator.AND);


                IQueryableAttribute iterationAttribute = factory.findAttribute(area, IWorkItem.TARGET_PROPERTY, aClient, null);
                mainTerm.add(new AttributeExpression(iterationAttribute, AttributeOperation.EQUALS, iteration.getIIteration()));
                if (owner instanceof TeamAreaImpl) {
                    IQueryableAttribute teamAttribute = factory.findAttribute(area, "teamArea", aClient, null);
                    mainTerm.add(new AttributeExpression(teamAttribute, AttributeOperation.EQUALS, ((TeamAreaImpl) owner).getIProcessArea()));
                }
                IQueryableAttribute typeAttribute = factory.findAttribute(area, IWorkItem.TYPE_PROPERTY, aClient, null);
                if (plan.isReleasePlan()) {
                    List<String> fetchTopLevelWorkItemsTypes = plan.getPlansManager().fetchTopLevelWorkItemsTypes();
                    for (String type : fetchTopLevelWorkItemsTypes) {
                        mainTerm.add(new AttributeExpression(typeAttribute, AttributeOperation.EQUALS, type));
                    }

                }
                IQueryableAttribute parentAttribute = factory.findAttribute(area, "link:com.ibm.team.workitem.linktype.parentworkitem:target", aClient, null);
                mainTerm.add(new AttributeExpression(parentAttribute, AttributeOperation.LINK_NOT_EXISTS));

                Expression rootExpression = new Term(Operator.AND, new Expression[]{hiddenTerm, mainTerm});


                IQueryResult<IResolvedResult<IWorkItem>> results = qClient.getResolvedExpressionResults(area, rootExpression, IWorkItem.FULL_PROFILE);
                ////System.out.println("Size: " + results.getResultSize(null).getTotal());
                RtcWorkItemsManager manager = plan.getPlansManager().getActiveProjectArea().getLookup().lookup(RtcWorkItemsManager.class);
                while (results.hasNext(null)) {
                    IWorkItem r = results.next(null).getItem();

                    items.add(new RtcPlanWorkItemImpl(manager.findWorkItem(r.getId()), plan));
                    //System.out.println("Work Item #" + r.getId() + " : " + r.getHTMLSummary().getPlainText());


                }
            } catch (TeamRepositoryException ex) {
                //ex.printStackTrace();
                RtcLogger.getLogger(RtcPlanItemsManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
//            Collection<PlanItem> allPlanItems = plan.getAllPlanItems();
//            for (PlanItem planItem : allPlanItems) {
//                items.add(new RtcPlanWorkItemImpl(planItem));
//            }
        }
//        Collection<OutOfOfficeItem> absences = plan.getAbsences();
//        for (OutOfOfficeItem absence : absences) {
//            items.add(new RtcContributorAbsenceImpl(absence));
//        }
    }

    @Override
    public RtcPlanWorkItem addNewWorkItem(RtcWorkItem workitem) {
        RtcPlanWorkItemImpl rtcPlanWorkItemImpl = new RtcPlanWorkItemImpl(workitem, plan);
        items.add(rtcPlanWorkItemImpl);
        fireEvent(RtcPlanItemsManagerEvent.ITEM_ADDED);
        return rtcPlanWorkItemImpl;
    }

    public final void removeListener(EventListener<RtcPlanItemsManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemsManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemsManagerEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
