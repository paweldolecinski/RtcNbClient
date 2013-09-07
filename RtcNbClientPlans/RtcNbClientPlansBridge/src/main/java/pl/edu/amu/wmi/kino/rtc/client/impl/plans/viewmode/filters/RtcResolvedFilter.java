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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.filters;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.workflow.WorkflowInfo;
import com.ibm.team.workitem.common.internal.workflow.WorkflowManager;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcResolvedFilter extends RtcPlanItemFilter {

    private static final String attibuteId = "com.ibm.team.apt.attribute.planitem.state";

    /**
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcResolvedFilter.class, "filter.name.resolved");
    }

    @Override
    public boolean isFiltered(RtcPlanItem planItem) {
        if (planItem instanceof RtcPlanWorkItem) {
            RtcPlansManager plansManager = planItem.getPlan().getPlansManager();
            RtcPlanItemAttribute planItemAttribute = plansManager.getPlanItemAttribute(attibuteId);
            @SuppressWarnings("unchecked")
            Object planAttributeValue = planItem.getPlanAttributeValue(planItemAttribute);
            ITeamRepository repo = ((ActiveProjectAreaImpl) plansManager.getActiveProjectArea()).getITeamRepository();
            IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
            IWorkItem wi = ((RtcWorkItemImpl) ((RtcPlanWorkItem) planItem).getWorkItem()).getWorkItem();
            try {
                IWorkflowInfo iwi = ((WorkflowManager) workItemClient.getWorkflowManager()).getWorkflowInfo(wi, null);
                if (iwi.getStateGroup(wi.getState2()) == WorkflowInfo.CLOSED_STATES) {
                    return true;
                }
            } catch (TeamRepositoryException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcResolvedFilter.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }


        }
        return false;
    }

    @Override
    public RtcPlanItem[] filter(RtcPlanItem[] planItems) {
        List<RtcPlanItem> res = new ArrayList<RtcPlanItem>();
        for (RtcPlanItem rtcPlanItem : planItems) {
            if (!isFiltered(rtcPlanItem)) {
                res.add(rtcPlanItem);
            }
        }

        return res.toArray(new RtcPlanItem[]{});
    }
}
