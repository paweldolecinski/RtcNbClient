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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process;

import com.ibm.team.process.common.IDevelopmentLineHandle;
import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.IDeliverable;
import com.ibm.team.workitem.common.model.IDeliverableHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;

/**
 * @see ProcessManager
 * @author Pawel Dolecinski
 * @author Patryk Żywica
 */
public class ProcessManagerImpl implements ProcessManager {

    private ActiveProjectAreaImpl area;
    private ProjectAreaImpl projectArea;
    private Map<String, TeamAreaImpl> teamAreas = new HashMap<String, TeamAreaImpl>(8);
    private Map<String, DevelopmentLineImpl> developmentLines = new HashMap<String, DevelopmentLineImpl>(2);
    private DevelopmentLineImpl[] developmentLinesCache;
    private Map<String, IterationImpl> iterations = new HashMap<String, IterationImpl>(8);
    private Map<IterationImpl, IterationImpl[]> iterationsCache;
    private Map<DevelopmentLineImpl, IterationImpl[]> devLineIterationsCache;
    private Map<String, DeliverableImpl> deliverables = new HashMap<String, DeliverableImpl>(2);
    private DeliverableImpl[] deliverablesCache;
    private EventSourceSupport<ProcessManager.RtcProcessEvent> eventSource = new EventSourceSupport<RtcProcessEvent>();

    /*package*/ ProcessManagerImpl(ActiveProjectAreaImpl area) {
        this.area = area;
        this.projectArea = area.getProjectArea();
    }

    public ActiveProjectAreaImpl getActiveProjectArea() {
        return area;
    }

    @Override
    synchronized public void synchronizeWithServer() {
        throw new UnsupportedOperationException();
    }

    public ProjectAreaImpl getProjectArea() {
        return projectArea;
    }

    @Override
    public TeamAreaImpl[] getTeamAreas() {
        LinkedList<TeamAreaImpl> tmp = new LinkedList<TeamAreaImpl>();
        for (Object team : projectArea.getIProcessArea().getTeamAreas()) {
            if (team instanceof ITeamAreaHandle) {
                tmp.add(findTeamArea((ITeamAreaHandle) team));
            }
        }
        return tmp.toArray(new TeamAreaImpl[]{});
    }

    @Override
    public TeamAreaImpl[] getTeamAreas(TeamArea teamArea) {

        if (teamArea instanceof TeamAreaImpl) {
            TeamAreaImpl ta = (TeamAreaImpl) teamArea;
            LinkedList<TeamAreaImpl> tmp = new LinkedList<TeamAreaImpl>();
            for (Object team : projectArea.getIProcessArea().getTeamAreaHierarchy().getChildren(ta.getIProcessArea())) {
                if (team instanceof ITeamAreaHandle) {
                    tmp.add(findTeamArea((ITeamAreaHandle) team));
                }
            }
            return tmp.toArray(new TeamAreaImpl[]{});
        } else {
            throw new IllegalArgumentException();
        }
    }

    public TeamAreaImpl findTeamArea(ITeamAreaHandle teamArea) {
        TeamAreaImpl impl = new TeamAreaImpl(teamArea);
        teamAreas.put(teamArea.getItemId().getUuidValue(), impl);
        return impl;
    }

    @Override
    public ContributorImpl[] getMembers(ProcessArea pArea) {
        if (pArea instanceof AbstractProcessAreaImpl) {
            AbstractProcessAreaImpl areaImpl = (AbstractProcessAreaImpl) pArea;
            ContributorManager contributorManager = area.getLookup().lookup(ContributorManager.class);
            if (contributorManager instanceof ContributorManagerImpl) {
                ContributorManagerImpl cm = (ContributorManagerImpl) contributorManager;
                IContributorHandle[] m = areaImpl.getIProcessArea().getMembers();
                ArrayList<ContributorImpl> members = new ArrayList<ContributorImpl>(m.length);
                for (IContributorHandle iContributorHandle : m) {
                    members.add(cm.findContributor(iContributorHandle));
                }
                return members.toArray(new ContributorImpl[]{});
            } else {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public DevelopmentLineImpl[] getDevelopmentLines() {
        if (developmentLinesCache == null) {
            IDevelopmentLineHandle[] tab = projectArea.getIProcessArea().getDevelopmentLines();
            ArrayList<DevelopmentLineImpl> result = new ArrayList<DevelopmentLineImpl>(tab.length);
            for (IDevelopmentLineHandle h : tab) {
                result.add(findDevelopmentLine(h));
            }
            developmentLinesCache = result.toArray(new DevelopmentLineImpl[]{});
        }
        return developmentLinesCache;
    }

    public DevelopmentLineImpl getMainDevelopmentLine() {
        return findDevelopmentLine(projectArea.getIProcessArea().getProjectDevelopmentLine());
    }

    public IterationImpl getCurrentIteration(DevelopmentLine developmentLine) {
        if (developmentLine instanceof DevelopmentLineImpl) {
            DevelopmentLineImpl dev = (DevelopmentLineImpl) developmentLine;
            return findIteration(dev.getIDevelopmentLine().getCurrentIteration());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public IterationImpl[] getIterations(DevelopmentLine developmentLine) {
        if (developmentLine instanceof DevelopmentLineImpl) {
            DevelopmentLineImpl dev = (DevelopmentLineImpl) developmentLine;
            if (!devLineIterationsCache.containsKey(dev)) {
                IIterationHandle[] tab = dev.getIDevelopmentLine().getIterations();
                ArrayList<IterationImpl> result = new ArrayList<IterationImpl>(tab.length);
                for (IIterationHandle h : tab) {
                    result.add(findIteration(h));
                }
                devLineIterationsCache.put(dev, result.toArray(new IterationImpl[]{}));
            }
            return devLineIterationsCache.get(dev);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public IterationImpl[] getIterations(Iteration iteration) {
        if (iteration instanceof IterationImpl) {
            IterationImpl iter = (IterationImpl) iteration;
            if (!iterationsCache.containsKey(iter)) {
                IIterationHandle[] tab = iter.getIIteration().getChildren();
                ArrayList<IterationImpl> result = new ArrayList<IterationImpl>(tab.length);
                for (IIterationHandle h : tab) {
                    result.add(findIteration(h));
                }
                iterationsCache.put(iter, result.toArray(new IterationImpl[]{}));
            }
            return iterationsCache.get(iter);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public DevelopmentLineImpl findDevelopmentLine(IDevelopmentLineHandle devHandle) {
        DevelopmentLineImpl impl = new DevelopmentLineImpl(devHandle);
        developmentLines.put(devHandle.getItemId().getUuidValue(), impl);
        return impl;
    }

    public IterationImpl findIteration(IIterationHandle iterationHandle) {
        if (iterationHandle != null) {
            IterationImpl impl = new IterationImpl(iterationHandle);
            iterations.put(iterationHandle.getItemId().getUuidValue(), impl);
            return impl;
        } else {
            return null;
        }
    }

    /**
     * 
     * @return
     */
    public DeliverableImpl[] getDeliverables() {
        if (deliverablesCache == null) {
            IWorkItemCommon workItemCommon = (IWorkItemCommon) area.getITeamRepository().getClientLibrary(IWorkItemCommon.class);
            try {
                List<IDeliverable> findDeliverables = workItemCommon.findDeliverablesByProjectArea(
                        area.getProjectArea().getIProcessArea(), true, IDeliverable.DEFAULT_PROFILE, null);
                ArrayList<DeliverableImpl> list = new ArrayList<DeliverableImpl>(findDeliverables.size());
                for (IDeliverable iDeliverable : findDeliverables) {
                    list.add(findDeliverable(iDeliverable));
                }
                deliverablesCache = list.toArray(new DeliverableImpl[]{});
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(ProcessManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                throw new IllegalStateException(ex);
            }
        }
        return deliverablesCache;
    }

    public DeliverableImpl findDeliverable(IDeliverableHandle handle) {
        if (handle == null) {
            return null;
        } else {
            if (!deliverables.containsKey(handle.getItemId().getUuidValue())) {
                DeliverableImpl impl = new DeliverableImpl(handle);
                deliverables.put(handle.getItemId().getUuidValue(), impl);
            }
            return deliverables.get(handle.getItemId().getUuidValue());
        }
    }

    public final void removeListener(EventListener<RtcProcessEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcProcessEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcProcessEvent> listener) {
        eventSource.addListener(listener);
    }
}
