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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.RtcPlanItemAttributesFactory;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.load.RtcLoadInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor.ProgressMonitor;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.pages.RtcPlanWikiPageImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.pages.RtcWikiAttachmentImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.progress.RtcProgressInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.reports.RtcPlanReportImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.types.RtcPlanTypes;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;

import com.ibm.team.apt.api.common.planning.IProgressInformation;
import com.ibm.team.apt.common.IIterationPlanRecord;
import com.ibm.team.apt.common.IIterationPlanRecordHandle;
import com.ibm.team.apt.internal.client.IterationPlanClient;
import com.ibm.team.apt.internal.client.IterationPlanSaveResult;
import com.ibm.team.apt.internal.client.PlanningClientPlugin;
import com.ibm.team.apt.internal.client.teamload.ITeamLoadClient;
import com.ibm.team.apt.internal.client.teamload.LoadItem;
import com.ibm.team.apt.internal.client.teamload.TeamLoadInformation;
import com.ibm.team.apt.internal.common.wiki.IWikiPageAttachment;
import com.ibm.team.process.common.IProcessAreaHandle;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.process.common.ITeamAreaHierarchy;
import com.ibm.team.reports.client.IReportManager;
import com.ibm.team.reports.common.IReportDescriptor;
import com.ibm.team.reports.common.IReportDescriptorHandle;
import com.ibm.team.repository.client.IContentManager;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContent;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.query.IItemQueryPage;
import com.ibm.team.workitem.client.IWorkItemClient;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.AbstractProcessAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.IterationImpl;

/**
 *
 * @author Patryk Żywica
 * @author Pawel Dolecinski
 */
public class RtcPlansManagerImpl implements RtcPlansManager {

    private ActiveProjectAreaImpl area;
    private IterationPlanClient planClient;
    private final Map<Iteration, List<RtcPlanImpl>> toIterationPlans = Collections.synchronizedMap(new HashMap<Iteration, List<RtcPlanImpl>>());
    private final List<RtcPlanImpl> plansList = Collections.synchronizedList(new ArrayList<RtcPlanImpl>());
    private final Map<String, RtcPlanImpl> toRtcPlan = Collections.synchronizedMap(new HashMap<String, RtcPlanImpl>());
    private Map<Iteration, IIterationPlanRecordHandle[]> serverPlans = new HashMap<Iteration, IIterationPlanRecordHandle[]>();
    private EventSourceSupport<RtcPlansManager.RtcPlansEvent> eventSource = new EventSourceSupport<RtcPlansEvent>();

    /**
     * 
     * @param area
     */
    public RtcPlansManagerImpl(ActiveProjectAreaImpl area) {
        this.area = area;
        try {
            this.planClient = (IterationPlanClient) PlanningClientPlugin.getIterationPlanClient(area.getITeamRepository());
        } catch (Throwable ex) {
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, "Problems with RTC API", ex);
        }
    }

    @Override
    public RtcPlan[] getPlansFor(Iteration iteration) {

        //TODO: dolek: do something with hash maps!!!!

        assert (!EventQueue.isDispatchThread());
        if (!toIterationPlans.containsKey(iteration)) {
            toIterationPlans.put(iteration, new ArrayList<RtcPlanImpl>());
            //serverPlans.put(iteration.getId(), new IIterationPlanRecordHandle[] {});
            updatePlans(iteration);
        }
        synchronized (toIterationPlans) {
            return toIterationPlans.get(iteration).toArray(new RtcPlanImpl[]{});
        }
    }

    @Override
    public RtcPlan findPlan(String planUuid) {
        synchronized (toRtcPlan) {
            return toRtcPlan.get(planUuid);
        }
    }

    private void updatePlans(Iteration iteration) {
        //TODO: dolek: make this method faster!
        try {
            IIterationPlanRecordHandle[] oldPlans = serverPlans.get(iteration) == null ? new IIterationPlanRecordHandle[]{} : serverPlans.get(iteration);

            List<IIterationPlanRecordHandle> newPlans = planClient.fetchIterationPlanRecords(getChildProcessAreas(area.getProjectArea().getIProcessArea()), ((IterationImpl) iteration).getIIteration(), new ProgressMonitor());

            serverPlans.remove(iteration);
            serverPlans.put(iteration, newPlans.toArray(new IIterationPlanRecordHandle[]{}));

            List<IIterationPlanRecordHandle> toAdd = new LinkedList<IIterationPlanRecordHandle>();
            List<IIterationPlanRecordHandle> toRemove = new LinkedList<IIterationPlanRecordHandle>();
            IItemManager itemManager = area.getITeamRepository().itemManager();

            //Deretminating plans to add
            for (IIterationPlanRecordHandle np : newPlans) {
                boolean add = true;

                for (IIterationPlanRecordHandle op : oldPlans) {
                    if (op.getItemId().getUuidValue().equals(np.getItemId().getUuidValue())) {
                        add = false;
                        break;
                    }
                }
                if (add == true) {
                    toAdd.add(np);
                }
            }
            if (toAdd.size() > 0) {
                for (IIterationPlanRecordHandle plan : toAdd) {
                    try {
                        IIterationPlanRecord record = (IIterationPlanRecord) itemManager.fetchCompleteItem(plan, IItemManager.DEFAULT, null);
                        RtcPlanImpl q = new RtcPlanImpl(record, (IterationImpl) iteration, this);
                        toIterationPlans.get(iteration).add(q);
                        toRtcPlan.put(plan.getItemId().getUuidValue(), q);
                        plansList.add(q);
                    } catch (Exception ex) {
                        RtcPlanImpl q = new RtcPlanImpl(null, (IterationImpl) iteration, this);
                        toIterationPlans.get(iteration).add(q);
                        toRtcPlan.put(plan.getItemId().getUuidValue(), q);
                        plansList.add(q);
                    }

                }
            }

            //Determinating plans to remove
            for (IIterationPlanRecordHandle oq : oldPlans) {
                boolean remove = true;
                for (IIterationPlanRecordHandle nq : newPlans) {
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
                for (IIterationPlanRecordHandle planRecordHandle : toRemove) {
                    RtcPlan p = findRtcPlan(planRecordHandle);
                    plansList.remove((RtcPlanImpl) p);
                    toIterationPlans.get(iteration).remove((RtcPlanImpl) p);
                    toRtcPlan.remove(planRecordHandle.getItemId().getUuidValue());
                }
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void addPlan(RtcPlan plan) {
        toIterationPlans.get(plan.getIteration()).add((RtcPlanImpl) plan);
        plansList.add((RtcPlanImpl) plan);
        toRtcPlan.put(plan.getPlanIdentifier(), (RtcPlanImpl) plan);
        fireEvent(RtcPlansEvent.PLAN_ADDED);
    }

    @Override
    public void removePlan(RtcPlan plan) {
        if (plan instanceof RtcPlanImpl) {
            try {
                planClient.delete(((RtcPlanImpl) plan).getPlanRecord()); // removes from server
                plansList.remove(plan);
                toRtcPlan.remove(plan.getPlanIdentifier());
                toIterationPlans.get(plan.getIteration()).remove(plan);
                fireEvent(RtcPlansEvent.PLAN_REMOVED);
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }

    @Override
    public void synchronizeWithServer() {

        //TODO: dolek: for future we need better synchronization
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
                DevelopmentLine[] developmentLines = pm.getDevelopmentLines();
                for (int i = 0; i < developmentLines.length; i++) {
                    Iteration[] iterations = pm.getIterations(developmentLines[i]);
                    for (int j = 0; j < iterations.length; j++) {
                        updatePlans(iterations[j]);
                        if (pm.getIterations(iterations[j]).length!=0) {
                            updatePlansForIterations(pm,pm.getIterations(iterations[j]));
                        }
                    }
                }
            }

            private void updatePlansForIterations(ProcessManager pa,Iteration[] children) {
                for (int j = 0; j < children.length; j++) {
                    updatePlans(children[j]);
                    if (pa.getIterations(children[j]).length!=0) {
                        updatePlansForIterations(pa,pa.getIterations(children[j]));
                    }
                }
            }
        });

    }

    @Override
    public RtcPlan createNewPlan(String name, Iteration iteration, ProcessArea owner, RtcPlanType type) {
        //NewIterationPlanData newI = planClient.fetchNewIterationPlanData(new IterationPlanWizardContext(new ArrayList()), null);
        if (iteration instanceof IterationImpl) {
            IIterationPlanRecord record = (IIterationPlanRecord) IIterationPlanRecord.ITEM_TYPE.createItem();
            record.setName(name);
            record.setIteration(((IterationImpl) iteration).getIIteration());
            if (owner instanceof AbstractProcessAreaImpl) {
                record.setOwner(((AbstractProcessAreaImpl) owner).getIProcessArea());
            } else {
                record.setOwner(area.getProjectArea().getIProcessArea());
            }

            record.setPlanType(type.getId());

            try {
                IterationPlanSaveResult create = planClient.create(record, new ProgressMonitor());
                if (create.getStatus().isOK()) {
                    RtcPlanImpl plan = new RtcPlanImpl(record, (IterationImpl) iteration, this);
                    return plan;
                }
            } catch (TeamRepositoryException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
        return null;
    }

    @Override
    public RtcPlanType[] getPlanTypes() {
        List<RtcPlanType> types = new ArrayList<RtcPlanType>();
        types.add(RtcPlanTypes.getType(RtcPlanTypes.ITERATION_PLAN_ID));
        types.add(RtcPlanTypes.getType(RtcPlanTypes.PRODUCT_BACKLOG_PLAN_ID));
        types.add(RtcPlanTypes.getType(RtcPlanTypes.PROJECT_RELEASE_PLAN_ID));
        types.add(RtcPlanTypes.getType(RtcPlanTypes.TEAM_RELEASE_PLAN_ID));
        return types.toArray(new RtcPlanType[]{});

    }

    @Override
    public RtcPlanPage createNewPage(String name) {
        return new RtcPlanWikiPageImpl(name);
    }

    /**
     * The default complexity computator in common situations will use hours unit.
     *
     * @return complexity computator default for project area
     */
    @Override
    public RtcComplexityComputator getDefaultComplexityComputator() {
        return new RtcDefaultComplexityComputator();
    }

    @Override
    public RtcLoadInfo getLoadInfo(Contributor contributor, Iteration iteration) {
        try {
            ITeamLoadClient client = PlanningClientPlugin.getTeamLoadClient(area.getITeamRepository());
            TeamLoadInformation fetchTeamLoadInformation = client.fetchTeamLoadInformation(area.getProjectArea().getIProcessArea(), ((IterationImpl) iteration).getIIteration(), new ProgressMonitor());
            LoadItem loadItem = fetchTeamLoadInformation.getLoadItem(((ContributorImpl) contributor).getIContributor());
            return new RtcLoadInfoImpl(loadItem, contributor, iteration);
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.INFO, "Couldn''t get load of {0}", contributor.getName());
        }
        return null;
    }

    /**
     * Not implemented. Do not use.
     *
     * @param contributor
     * @param iteration
     * @param complexityComputator
     * @return
     */
    @Override
    public RtcProgressInfo getProgressInfo(Contributor contributor, Iteration iteration, RtcComplexityComputator complexityComputator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RtcProgressInfo getProgressInfo(Contributor contributor, RtcPlanItem[] items, RtcComplexityComputator complexityComputator) {
        List<IProgressInformation> progresses = new ArrayList<IProgressInformation>();
        for (RtcPlanItem rtcPlanItem : items) {
            if (rtcPlanItem instanceof RtcPlanWorkItem && rtcPlanItem.getOwner().getUserId().equals(contributor.getUserId())) {
                try {
                    progresses.add(planClient.fetchWorkItemProgress(((RtcWorkItemImpl) ((RtcPlanWorkItem) rtcPlanItem).getWorkItem()).getWorkItem(), new ProgressMonitor()));
                } catch (TeamRepositoryException ex) {
                    RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.INFO, "Couldn''t get progress of {0}", rtcPlanItem.getName());
                }
            }
        }
        return new RtcProgressInfoImpl(progresses.toArray(new IProgressInformation[]{}), complexityComputator);
    }

    @Override
    public RtcProgressInfo getProgressInfo(ProcessArea owner, Iteration iteration, RtcComplexityComputator complexityComputator) {
        List<RtcPlanImpl> get = toIterationPlans.get(iteration);
        if (get != null) {
            for (RtcPlanImpl rtcPlanImpl : get) {
                if (rtcPlanImpl.getOwner().equals(owner)) {
                    return new RtcProgressInfoImpl(rtcPlanImpl, complexityComputator);
                }
            }
        }
        return null;
    }

    @Override
    public RtcProgressInfo getProgressInfo(RtcPlanItem[] items, RtcComplexityComputator complexityComputator) {
        List<IProgressInformation> progresses = new ArrayList<IProgressInformation>();
        for (RtcPlanItem rtcPlanItem : items) {
            if (rtcPlanItem instanceof RtcPlanWorkItem) {
                try {
                    progresses.add(planClient.fetchWorkItemProgress(((RtcWorkItemImpl) ((RtcPlanWorkItem) rtcPlanItem).getWorkItem()).getWorkItem(), new ProgressMonitor()));
                } catch (TeamRepositoryException ex) {
                    RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.INFO, "Couldn''t get progress of {0}", rtcPlanItem.getName());
                }
            }
        }
        return new RtcProgressInfoImpl(progresses.toArray(new IProgressInformation[]{}), complexityComputator);
    }

    @Override
    public RtcPlan[] getRelatedPlans(RtcPlan plan) {
        ArrayList<RtcPlan> res = new ArrayList<RtcPlan>();
        if (plan instanceof RtcPlanImpl) {
            try {
                IItemQueryPage query = planClient.fetchRelatedPlans(((RtcPlanImpl) plan).getPlanRecord(), new ProgressMonitor());
                if (query != null) {
                    IItemManager itemManager = area.getITeamRepository().itemManager();
                    List<IIterationPlanRecord> fetchCompleteItems = itemManager.fetchCompleteItems(query.getItemHandles(), IItemManager.DEFAULT, new ProgressMonitor());
                    for (IIterationPlanRecord iIterationPlanRecord : fetchCompleteItems) {
                        RtcPlan findRtcPlan = findRtcPlan(iIterationPlanRecord);
                        if (findRtcPlan != null) {
                            res.add(findRtcPlan);
                        } else {
                            //Nothing happens. it means that locally are not all plans from server.
                        }
                    }
                }
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, "Couldn't get releated plans", ex);
            }
        }
        return res.toArray(new RtcPlan[]{});
    }

    @Override
    public RtcPlanItemAttribute[] getPlanItemAttributes() {
        RtcPlanItemAttribute[] res = RtcPlanItemAttributesFactory.getAllAttributes(area);
        return res;
    }

    @Override
    public RtcPlanItemAttribute getPlanItemAttribute(String id) {
        RtcPlanItemAttribute res = RtcPlanItemAttributesFactory.getAttribute(area, id);
        return res;
    }

    @Override
    public RtcPlanReport getDefaultReport() {
        IReportManager reportManager = (IReportManager) area.getITeamRepository().getClientLibrary(IReportManager.class);
        RtcPlanReport report = RtcPlanReportImpl.EMPTY;
        try {
            IReportDescriptor reportDescriptor = getReportDescriptor(area.getITeamRepository(), reportManager, area.getProjectArea().getIProcessArea());
            if (reportDescriptor != null) {
                report = new RtcPlanReportImpl(reportDescriptor);
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, "There is not default report", ex);
        } finally {
            return report;
        }
    }

    @Override
    public RtcPlanReport[] getPlanReportsList() {
        List<RtcPlanReport> res = new ArrayList<RtcPlanReport>();
        final ITeamRepository repository = area.getITeamRepository();
        IReportManager reportManager = (IReportManager) repository.getClientLibrary(IReportManager.class);
        List<String> parameters = Arrays.asList(new String[]{"ProjectAreaName", "TeamAreaName", "Interval"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        try {
            List<IReportDescriptor> descriptors = reportManager.fetchReportDescriptors(null, area.getProjectArea().getIProcessArea(), null, parameters, IReportManager.SHARED | IReportManager.EXCLUDE_MICRO, new ProgressMonitor());
            for (IReportDescriptor iReportDescriptor : descriptors) {
                if (iReportDescriptor != null) {
                    res.add(new RtcPlanReportImpl(iReportDescriptor));
                }
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, "Unable to get reports from server.", ex);
        }

        return res.toArray(new RtcPlanReport[]{});
    }

    @Override
    public ActiveProjectArea getActiveProjectArea() {
        return area;
    }

    @Override
    public RtcPlanPageAttachment createNewAttachment(final Contributor creator, final String fileName, final String description) {
        if (creator instanceof ContributorImpl) {
            try {
                final IContentManager contentManager = area.getITeamRepository().contentManager();
                IWorkItemClient client = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
                final long maxAttachmentSize = client.fetchMaxAttachmentSize(new ProgressMonitor());
                File file = new File(fileName);
                if (file.exists()) {
                    long length = file.length();
                    if (length <= maxAttachmentSize) {
                        IWikiPageAttachment attachment = null;
                        attachment = createAttachmentFromFile(file, contentManager);
                        attachment.setCreator(((ContributorImpl) creator).getIContributor());
                        attachment.setDescription(description);
//FIXME
//                        return new RtcWikiAttachmentImpl(attachment);
                    } else {
                        //filesTooBig.add(fileName);
                    }
                }

            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                //Exceptions.printStackTrace(ex);
            }
            return null;
        } else {
            throw new IllegalArgumentException();
        }
    }

    //-------------- internal API ---------------
    //
    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> fetchTopLevelWorkItemsTypes() {
        try {
            return planClient.fetchTopLevelWorkItemTypes(area.getProjectArea().getIProcessArea(), new ProgressMonitor());
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return Collections.EMPTY_LIST;
    }

    // ------ private methods ----------------
    //
    private RtcPlan findRtcPlan(IIterationPlanRecordHandle query) {
        synchronized (toRtcPlan) {
            return toRtcPlan.get(query.getItemId().getUuidValue());

        }
    }

    @SuppressWarnings("unchecked")
    private List<IProcessAreaHandle> getChildProcessAreas(IProjectArea project) {
        List<IProcessAreaHandle> res = new LinkedList<IProcessAreaHandle>();
        ITeamAreaHierarchy hierarchy = project.getTeamAreaHierarchy();
        res.add(project);
        for (ITeamAreaHandle team : (List<ITeamAreaHandle>) (project.getTeamAreas())) {
            res.addAll(getChildTeamAreas(team, hierarchy));
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private List<ITeamAreaHandle> getChildTeamAreas(ITeamAreaHandle teamArea, ITeamAreaHierarchy hierar) {
        List<ITeamAreaHandle> res = new LinkedList<ITeamAreaHandle>();
        res.add(teamArea);
        for (ITeamAreaHandle handle : (Set<ITeamAreaHandle>) hierar.getChildren(teamArea)) {
            res.addAll(getChildTeamAreas(handle, hierar));
        }
        return res;
    }

    private IReportDescriptor getReportDescriptor(
            final ITeamRepository repository, IReportManager reportManager,
            IProjectArea projectArea)
            throws TeamRepositoryException {
        IReportDescriptor report = null;
        IReportDescriptorHandle reportHandle = reportManager.getDefaultReportDescriptor(projectArea,
                new ProgressMonitor());
        if (reportHandle != null) {
            report = (IReportDescriptor) repository.itemManager().fetchCompleteItem(reportHandle, IItemManager.DEFAULT,
                    new ProgressMonitor());
        } else if (reportManager.doesReportDescriptorExist(projectArea,
                "apt.WorkItems", new ProgressMonitor())) { //$NON-NLS-1$
            report = reportManager.getReportDescriptor(
                    "apt.WorkItems", projectArea, new ProgressMonitor()); //$NON-NLS-1$
        } else if (reportManager.doesReportDescriptorExist(projectArea,
                "workitems.OpenWorkItemsByType", new ProgressMonitor())) { //$NON-NLS-1$
            report = reportManager.getReportDescriptor(
                    "workitems.OpenWorkItemsByType", projectArea, new ProgressMonitor()); //$NON-NLS-1$
        }
        return report;
    }

    private IWikiPageAttachment createAttachmentFromFile(File file, final IContentManager contentManager) throws TeamRepositoryException {

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mime = fileNameMap.getContentTypeFor(file.getName());
        InputStream inStream = null;
        String charset = "";
        try {
            inStream = new FileInputStream(file);
            InputStreamReader r = new InputStreamReader(inStream);
            charset = r.getEncoding();
        } catch (FileNotFoundException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            return null;
        }



        IWikiPageAttachment attachment = null;
        try {
            attachment = createAttachmentFromStream(contentManager, inStream, mime, charset);
        } finally {
            try {
                inStream.close();
            } catch (IOException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcPlansManagerImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        if (attachment != null) {
            attachment.setName(file.getName());
        }

        return attachment;
    }

    @SuppressWarnings("deprecation")
    private IWikiPageAttachment createAttachmentFromStream(IContentManager contentManager, InputStream inStream, String mimeType, String charset) throws TeamRepositoryException {

        IContent content = contentManager.storeContent(mimeType, charset, inStream, new ProgressMonitor());
        IWikiPageAttachment attachment = (IWikiPageAttachment) IWikiPageAttachment.ITEM_TYPE.createItem();
        attachment.setContent(content);

        return attachment;
    }

    /**
     *
     */
    public static class RtcDefaultComplexityComputator extends RtcComplexityComputator {

        public RtcDefaultComplexityComputator() {
        }

        @Override
        public String getUnitDisplayName() {
            return "Hour";
        }

        @Override
        public String getUnitShortDisplayName() {
            return "h";
        }

        @Override
        public double computeComplexity(double units) {
            return units / (1000 * 60 * 60);
        }
    }

    public final void removeListener(EventListener<RtcPlansEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlansEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlansEvent> listener) {
        eventSource.addListener(listener);
    }
    
    
}
