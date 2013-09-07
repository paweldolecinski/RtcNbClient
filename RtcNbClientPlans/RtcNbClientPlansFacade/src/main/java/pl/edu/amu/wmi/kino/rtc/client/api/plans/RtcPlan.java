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
package pl.edu.amu.wmi.kino.rtc.client.api.plans;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 *
 * @author Patryk Żywica
 */
public interface RtcPlan extends EventSource<RtcPlan.RtcPlanEvent> {

    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract RtcComplexityComputator getComplexityComputator();

    /**
     *
     * @return
     */
    //TODO : bikol : javadoc
    public abstract RtcPlansManager getPlansManager();

    /**
     * All plan pages of this plan except of Overview page are returned by this method.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return all user defined plan pages of this plan. May be empty. Never <code>null</code>.
     * @see RtcPlanPage
     */
    public abstract RtcPlanPage[] getPages();

    /**
     * This method return string identifier of this plan.
     *
     * It is guaranteed that returned plan ID is unique for all plans in manager.
     *
     * @return unique within manager string identifier of this plan.
     */
    public abstract String getPlanIdentifier();

    /**
     *
     * @return plan name.
     */
    public abstract String getName();
    //TODO : bikol : javadoc

    /**
     *
     * @param name
     */
    public abstract void setName(String name);

    /**
     * This method returns iteration that this plan is assigned to.
     *
     * It can be any iteration registered in <code>RtcIterationManager</code>.
     *
     * @return <code>Iteration</code> that this plan is assigned to. Never null.
     * @see pl.edu.amu.wmi.kino.rtc.client.process.RtcIterationsManager
     * @see Iteration
     */
    public abstract Iteration getIteration();
    //TODO : bikol : javadoc

    /**
     *
     * @param iteration
     */
    public abstract void setIteration(Iteration iteration);

    /**
     * Currently unsupported and not implemented.
     *
     * For future use.
     * @return
     * @see RtcPlanType
     */
    public abstract RtcPlanType getPlanType();
//TODO : bikol : javadoc

    /**
     *
     * @param type
     */
    public abstract void setPlanType(RtcPlanType type);

    /**
     * Adds new page to this plan.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param page that will be added to plan.
     */
    public abstract void addPage(RtcPlanPage page);

    /**
     * Removes given page from plan.
     *
     * If given page is not registered in this plan does nothing.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param page to be removed from plan.
     */
    public abstract void removePage(RtcPlanPage page);

    /**
     * This method returns first plan page - the overview page.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return overview plan page.
     */
    public abstract RtcPlanPage getOverviewPage();

    /**
     * This method returns owner of this plan.
     *
     * Any plan can be own by Team or Project Area.
     *
     * @return owner of this plan. Never null.
     */
    public abstract ProcessArea getOwner();
    //TODO : bikol : javadoc

    /**
     *
     * @param area
     */
    public abstract void setOwner(ProcessArea area);

    /**
     * @since 0.2.1.4
     * @return true if plan type is project release or team release plan
     */
    public abstract boolean isReleasePlan();

    /**
     *
     *
     * @since 0.2.1.3
     * @return
     */
    //TODO : bikol : should be moved to PlanItemManager
    public abstract RtcPlanItemChecker[] getPlanItemCheckers();

    /**
     * Calling this method should force synchronizing all model objects with corresponding
     * server data. It also synchronizes <code>RtcPlanItemsManager</code>.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     */
    public abstract void synchronizeWithServer();
//TODO : bikol : javadoc
/**
 * This can be long running operation. Do not call on event dispatch thread.
 * @throws RtcPlanSaveException
 */
    public abstract void save() throws RtcPlanSaveException;

    /**
     * Returns <code>RtcPlanItemsManager</code> that is responsible for managing
     * <code>RtcPlanItem</code>s assigned to this plan.
     *
     * @return plan items manager for this plan.
     */
    public abstract RtcPlanItemsManager getPlanItemsManager();

    /**
     *
     * @return
     */
    public abstract Lookup getLookup();

    /**
     * This enumeration defines all possible events for <code>RtcPlan</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcPlanEvent {

        /**
         * This event should be called when plan page was added to plan.
         */
        PAGE_ADDED,
        /**
         * This event should be called when plan page was removed from plan.
         */
        PAGE_REMOVED,
        /**
         * This event should be called when plan name was changed.
         */
        NAME_CHANGED,
        /**
         *
         */
        //TODO : bikol : javadoc
        OWNER_CHANGED,
        /**
         *
         */
        ITERATION_CHANGED,
        /**
         * 
         */
        TYPE_CHANGED,
        /**
         * This event should be called when plan was saved.
         */
        PLAN_SAVED,
        /**
         * This event should be called when plan's changes was discarded.
         */
        CHANGES_DISCARDED,
        /**
         * This event should be called after plan synchronization with server data.
         * It means that plan's name and other properties may be changed.
         */
        PLAN_SYNCHRONIZED_WITH_SERVER;
    }
}
