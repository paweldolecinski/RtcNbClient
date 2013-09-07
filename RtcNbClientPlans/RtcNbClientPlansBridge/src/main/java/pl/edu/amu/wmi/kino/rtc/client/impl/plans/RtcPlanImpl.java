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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlanType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.RtcPlansManagerImpl.RtcDefaultComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.checker.RtcPlanItemCheckerFactory;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.RtcPlanItemsManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor.ProgressMonitor;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.pages.RtcPlanWikiPageImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.progress.RtcComplexityComputatorImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.types.RtcPlanTypes;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

import com.ibm.team.apt.common.IIterationPlanRecord;
import com.ibm.team.apt.internal.client.IterationPlanClient;
import com.ibm.team.apt.internal.client.IterationPlanData;
import com.ibm.team.apt.internal.client.IterationPlanSaveResult;
import com.ibm.team.apt.internal.client.PlanSaveResult;
import com.ibm.team.apt.internal.client.PlanningClientPlugin;
import com.ibm.team.apt.internal.client.wiki.ResolvedWikiPage;
import com.ibm.team.apt.internal.client.wiki.WikiClient;
import com.ibm.team.apt.internal.client.wiki.WikiManager;
import com.ibm.team.apt.internal.common.duration.IComplexityAttribute;
import com.ibm.team.apt.internal.common.util.ItemList;
import com.ibm.team.apt.internal.common.wiki.IWikiPage;
import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProcessAreaHandle;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.common.IReconcileReport;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan.RtcPlanEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.AbstractProcessAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.IterationImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProjectAreaManagerImpl;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanImpl implements RtcPlan {

    private IIterationPlanRecord planRecord;
    private RtcPlansManagerImpl manager;
    private Future<IterationPlanData> planDataFuture;
    private IterationPlanClient planClient;
    private IterationImpl iteration;
    private List<RtcPlanPage> pages = new LinkedList<RtcPlanPage>();
    private RtcPlanPage overviewPage;
    private ProcessArea owner;
    private RtcPlanType type;
    private String name;
    private RtcPlanItemsManager itemsManager;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Lookup lookup;
    private InstanceContent ic;
    private WikiManager wikiManager;
    private EventSourceSupport<RtcPlan.RtcPlanEvent> eventSource = new EventSourceSupport<RtcPlan.RtcPlanEvent>();

    /**
     *
     * @param planRecord
     * @param iteration
     * @param manager
     */
    public RtcPlanImpl(final IIterationPlanRecord planRecord, IterationImpl iteration, RtcPlansManagerImpl manager) {
        this.ic = new InstanceContent();
        lookup = new AbstractLookup(ic);
        this.planRecord = planRecord;
        this.manager = manager;
        this.iteration = iteration;
        planClient = (IterationPlanClient) PlanningClientPlugin.getIterationPlanClient(((ActiveProjectAreaImpl) manager.getActiveProjectArea()).getITeamRepository());
        wikiManager = WikiClient.getWikiManager(planClient.getTeamRepository());
        Callable<IterationPlanData> task = new Callable<IterationPlanData>() {

            @Override
            public IterationPlanData call() {
                try {
                    return planClient.fetchIterationPlanData(planRecord, new ProgressMonitor());
                } catch (Exception e) {
                    return null;
                }


            }
        };
        planDataFuture = executor.submit(task);
    }

    @Override
    public RtcPlansManagerImpl getPlansManager() {
        return manager;
    }

    @Override
    public RtcPlanPage[] getPages() {
        try {
            if (pages.isEmpty() && planDataFuture.get() != null) {
                ItemList<IWikiPage> wikiPages = planDataFuture.get().getWikiPages();

                for (IWikiPage iWikiPage : wikiPages) {
                    if (IIterationPlanRecord.OVERVIEW_PAGE_ID.equals(iWikiPage.getWikiID())) {
                        continue;
                    }
                    ResolvedWikiPage page = wikiManager.findResolvedPageUsingOwner(planRecord, iWikiPage.getWikiID(), new ProgressMonitor());

                    pages.add(new RtcPlanWikiPageImpl(iWikiPage, page));
                }
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            //Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (ExecutionException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return pages.toArray(new RtcPlanPage[pages.size()]);
    }

    @Override
    public String getPlanIdentifier() {
        if (planRecord != null) {
            return planRecord.getItemId().getUuidValue();
        } else {
            return "error";
        }
    }

    @Override
    public String getName() {
        if (name == null) {
            name = planRecord.getName();
        }
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
        fireEvent(RtcPlanEvent.NAME_CHANGED);
    }

    @Override
    public Iteration getIteration() {
        return iteration;
    }

    /**
     *
     * @param iteration
     */
    @Override
    public void setIteration(Iteration iteration) {
        if (iteration instanceof IterationImpl) {
            this.iteration = (IterationImpl) iteration;
            fireEvent(RtcPlanEvent.ITERATION_CHANGED);
        }
    }

    @Override
    public RtcPlanType getPlanType() {
        if (type == null) {
            type = RtcPlanTypes.getType(planRecord.getPlanType());
        }
        return type;
    }

    /**
     *
     * @param type
     */
    @Override
    public void setPlanType(RtcPlanType type) {
        this.type = type;
        fireEvent(RtcPlanEvent.TYPE_CHANGED);
    }

    @Override
    public void addPage(RtcPlanPage page) {
        if (pages.add(page)) {
            fireEvent(RtcPlanEvent.PAGE_ADDED);
        }
    }

    @Override
    public void removePage(RtcPlanPage page) {
        if (pages.remove(page)) {
            fireEvent(RtcPlanEvent.PAGE_REMOVED);
        }
    }

    @Override
    public RtcPlanPage getOverviewPage() {
        try {
            if (overviewPage == null && planDataFuture.get() != null) {
                ItemList<IWikiPage> wikiPages = planDataFuture.get().getWikiPages();
                for (IWikiPage iWikiPage : wikiPages) {
                    if (IIterationPlanRecord.OVERVIEW_PAGE_ID.equals(iWikiPage.getWikiID())) {
                        ResolvedWikiPage page = wikiManager.findResolvedPageUsingOwner(planRecord, iWikiPage.getWikiID(), new ProgressMonitor());
                        overviewPage = new RtcPlanWikiPageImpl(iWikiPage, page);
                        break;
                    }
                }
            }
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (ExecutionException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } finally {
            return overviewPage != null ? overviewPage : new RtcPlanWikiPageImpl("Overview"); //NON-I18N
        }
    }

    @Override
    public ProcessArea getOwner() {
        if (owner == null) {
            IProcessAreaHandle processArea = planRecord.getOwner();


            if (processArea instanceof ITeamAreaHandle) {
                ProcessManager processManager = manager.getActiveProjectArea().getLookup().lookup(ProcessManager.class);
                if (processManager instanceof ProcessManagerImpl) {
                    ProcessManagerImpl pm = (ProcessManagerImpl) processManager;
                    ITeamAreaHandle teamArea = (ITeamAreaHandle) processArea;
                    owner = pm.findTeamArea(teamArea);
                } else {
                    throw new IllegalStateException();
                }
            } else if (processArea instanceof IProjectAreaHandle) {
                ProjectAreaManager projectAreaManager = manager.getActiveProjectArea().getLookup().lookup(ProjectAreaManager.class);
                if (projectAreaManager instanceof ProjectAreaManagerImpl) {
                    ProjectAreaManagerImpl pam = (ProjectAreaManagerImpl) projectAreaManager;
                    IProjectAreaHandle projectArea = (IProjectAreaHandle) processArea;
                    owner = pam.findProjectArea(projectArea);
                } else {
                    throw new IllegalStateException();
                }
            } else {
                IItemManager itemManager = PlanningClientPlugin.getTeamRepository(processArea).itemManager();
                IProcessArea sharedProcessArea = (IProcessArea) itemManager.getSharedItemIfKnown(processArea);
                if (sharedProcessArea != null) {
                    IProjectAreaHandle projectArea = sharedProcessArea.getProjectArea();
                    if (projectArea != null) {
                        ProjectAreaManager projectAreaManager = manager.getActiveProjectArea().getLookup().lookup(ProjectAreaManager.class);
                        if (projectAreaManager instanceof ProjectAreaManagerImpl) {
                            ProjectAreaManagerImpl pam = (ProjectAreaManagerImpl) projectAreaManager;
                            owner = pam.findProjectArea(projectArea);
                        } else {
                            throw new IllegalStateException();
                        }
                    }
                }
            }
        }
        return owner;
    }

    /**
     *
     * @param area
     */
    @Override
    public void setOwner(ProcessArea area) {
        this.owner = area;
        fireEvent(RtcPlanEvent.OWNER_CHANGED);
    }

    @Override
    public boolean isReleasePlan() {
        String id = getPlanType().getId();
        return RtcPlanTypes.TEAM_RELEASE_PLAN_ID.equals(id) || RtcPlanTypes.PROJECT_RELEASE_PLAN_ID.equals(id);
    }

    @Override
    public void synchronizeWithServer() {
        try {
            if (planDataFuture.get() != null) {
                planDataFuture.get().refresh(new IReconcileReport[]{});
                planDataFuture.get().overwriteRecord(new ProgressMonitor());
            }
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (ExecutionException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void save() throws RtcPlanSaveException {
        try {
            // TODO: adding new and removes old pages from planData using pages list

            if (planRecord != null) {
                IIterationPlanRecord workingCopy = (IIterationPlanRecord) planRecord.getWorkingCopy();
                workingCopy.setName(name);
                workingCopy.setIteration(iteration.getIIteration());
                if (owner instanceof AbstractProcessAreaImpl) {
                    workingCopy.setOwner(((AbstractProcessAreaImpl)owner).getIProcessArea());
                }
                workingCopy.setPlanType(type.getId());
                IterationPlanSaveResult save = planClient.save(workingCopy, new ProgressMonitor());
                planRecord = save.getPlanRecord();
                try {
                    //after pages modyfiactions
                    PlanSaveResult dataSave = planDataFuture.get().save(true, new ProgressMonitor());
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
            //adding new page
//                    WikiManager wikiManager = WikiClient.getWikiManager(planRecord);
////TODO: new page name should be in method args
//        final String name = page.getName();
//        String id = WikiCommon.createWikiId(name);
//
//        try {
//            int appendix = 0;
//            while (wikiManager.findPageUsingOwner(planRecord, id, new ProgressMonitor()) != null) {
//                id = id + (++appendix);
//            }
//            final IWikiPage wikiPage = wikiManager.createPageUsingOwner(planRecord, id, name, new ProgressMonitor());
//            final String pageId = id;
//
//            ((RtcPlanWikiPageImpl)page).setPage(wikiPage);



        } catch (IOException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public RtcPlanItemsManager getPlanItemsManager() {
        if (itemsManager == null) {

//                ResolvedIterationPlan iterationPlan = planClient.fetchIterationPlan(planRecord, new ProgressMonitor());
//
//                if (planDataFuture.get() != null) {
//                    iterationPlan = planDataFuture.get().getIterationPlan();
//                }

            itemsManager = new RtcPlanItemsManagerImpl(this, null, null);

        }
        return itemsManager;
    }

    @Override
    public RtcComplexityComputator getComplexityComputator() {
        try {
            if (isReleasePlan() && planDataFuture.get() != null) {
                IComplexityAttribute complexityAttribute = planDataFuture.get().getComplexityAttribute();
                return (complexityAttribute != null)
                        ? new RtcComplexityComputatorImpl(complexityAttribute)
                        : new RtcDefaultComplexityComputator();

            } else {
                return new RtcDefaultComplexityComputator();
            }
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (ExecutionException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public RtcPlanItemChecker[] getPlanItemCheckers() {
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemCheckers/PlanItemCheckerRegistration/" + getPlanType().getId());
        Collection<? extends RtcPlanItemCheckerFactory> lookupAll = forPath.lookupAll(RtcPlanItemCheckerFactory.class);
        List<RtcPlanItemChecker> result = new ArrayList<RtcPlanItemChecker>(lookupAll.size());
        ////System.out.println("Checkers size: " + lookupAll.size());
        for (RtcPlanItemCheckerFactory f : lookupAll) {
            result.add(f.createChecker(this));
        }
        return result.toArray(new RtcPlanItemChecker[]{});
    }

    /**
     *
     * @return
     */
    @Override
    public Lookup getLookup() {
        return lookup;
    }

    /**
     *
     * @return
     */
    protected IIterationPlanRecord getPlanRecord() {
        return planRecord;
    }

    /**
     *
     * @return
     */
    public IterationPlanData getPlanData() {
        try {
            return planDataFuture.get();
        } catch (InterruptedException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } catch (ExecutionException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    public final void removeListener(EventListener<RtcPlanEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanEvent> listener) {
        eventSource.addListener(listener);
    }
}
