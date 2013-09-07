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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager.RtcPlansEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

/**
 *
 * @author Patryk Żywica
 */
public interface RtcPlansManager extends EventSource<RtcPlansEvent> {

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @param iteration for which plans have to be returned.
     * @return array of plans registered for given iteration.
     */
    public abstract RtcPlan[] getPlansFor(Iteration iteration);

    /**
     * Returns plan with given id or null if such plan isn't registered in this manager.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param planId
     * @return return plan with given id or null.
     */
    public abstract RtcPlan findPlan(String planId);

    /**
     * Probably this method is unnecessary
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param plan
     */
    public abstract void addPlan(RtcPlan plan);

    /**
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param plan
     */
    public abstract void removePlan(RtcPlan plan);

    /**
     * Calling this method should force synchronizing all model objects with corresponding
     * server data.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     */
    public abstract void synchronizeWithServer();

    /**
     * Creates new plan for given iteration, that is owned by given process area, and of given type.
     *
     * Created plan is stored on server, before this method finished.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param name
     * @param iteration
     * @param owner
     * @param type
     * @return newly created plan
     */
    public abstract RtcPlan createNewPlan(
            String name,
            Iteration iteration,
            ProcessArea owner,
            RtcPlanType type);

    /**
     * Creates new plan page that can be assigned to any <code>RtcPlan</code>.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @param name
     * @return new plan page, never null.
     */
    public abstract RtcPlanPage createNewPage(String name);

    /**
     * Will give you list of plans kinds which you can create.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return array of all possible plan types
     */
    public abstract RtcPlanType[] getPlanTypes();

    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract RtcComplexityComputator getDefaultComplexityComputator();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @since 0.2.1.3
     * @param contributor may not be <code>null</code>.
     * @param iteration
     * @return
     */
    public abstract RtcLoadInfo getLoadInfo(Contributor contributor, Iteration iteration);

    /**
     * When complexityComputator is null then managers default one is used.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param contributor may not be <code>null</code>
     * @param iteration
     * @param complexityComputator may be <code>null<code>.
     * @return
     */
    public abstract RtcProgressInfo getProgressInfo(Contributor contributor, Iteration iteration, RtcComplexityComputator complexityComputator);

    /**
     * When complexityComputator is null then managers default one is used.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param contributor may not be <code>null</code>
     * @param items
     * @param complexityComputator may be <code>null<code>.
     * @return
     */
    public abstract RtcProgressInfo getProgressInfo(Contributor contributor, RtcPlanItem[] items, RtcComplexityComputator complexityComputator);

    /**
     * When complexityComputator is null then managers default one is used.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param ownerArea 
     * @param iteration may not be <code>null</code>
     * @param complexityComputator may be <code>null<code>.
     * @return
     */
    public abstract RtcProgressInfo getProgressInfo(ProcessArea ownerArea, Iteration iteration, RtcComplexityComputator complexityComputator);

    /**
     * When complexityComputator is null then managers default one is used.
     * If given array is empty then it return null.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param items may not be <code>null</code>
     * @param complexityComputator may be <code>null<code>.
     * @return
     */
    public abstract RtcProgressInfo getProgressInfo(RtcPlanItem[] items, RtcComplexityComputator complexityComputator);

    /**
     * Returns all plans somehow related to given plan.
     * <p>
     * Definition of "related plan" is up to implementation.
     * </p>
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param plan for which all related plans will be returned. Never <code>null</code>.
     * @return all plans related to given plan. Never <code>null</code>. May be empty.
     */
    public abstract RtcPlan[] getRelatedPlans(RtcPlan plan);

    /**
     * Returns list of all plan item attributes registered to this manager.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @return array of all plan item attributes. Never null.
     */
    public abstract RtcPlanItemAttribute[] getPlanItemAttributes();

    /**
     *
     * Returns <code>null</code> if there is no attribute with given identifier.
     * This can be long running operation. Do not call on event dispatch thread.
     * @param id
     * @return attribute with given id. May be <code>null</code>
     */
    public abstract RtcPlanItemAttribute getPlanItemAttribute(String id);

    /**
     *
     * @since 0.2.1.3
     * @return default report attached to project area. Never null.
     */
    public abstract RtcPlanReport getDefaultReport();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.2.1.3
     * @param plan
     * @return array of all reports available in selected project area. Never null.
     */
    public abstract RtcPlanReport[] getPlanReportsList();

    /**
     * @since 0.2.1.4
     * @return 
     */
    public abstract ActiveProjectArea getActiveProjectArea();

    /**
     * @since 0.2.1.4
     * @param contributor
     * @param fileName
     * @param description
     * @return
     */
    public abstract RtcPlanPageAttachment createNewAttachment(Contributor contributor, String fileName, String description);

    /**
     * This enumeration defines all possible events for <code>RtcPlan</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcPlansEvent {

        /**
         * This event should be called when new plan to manager.
         */
        PLAN_ADDED,
        /**
         * This event should be called when plan was removed from manager.
         */
        PLAN_REMOVED,
        /**
         * This event should be called after all plans synchronization with server data.
         * It means that plans returned for any <code>Iteration</code> can change.
         */
        PLANS_MANAGER_SYNCHRONIZED_WITH_SERVER,
        /**
         * This event should be called when item attribute was added to manager.
         * @since 0.2.1.3
         */
        PLAN_ITEM_ATTRIBITE_ADDED,
        /**
         * This event should be called when item attribute was removed from manager.
         * @since 0.2.1.3
         */
        PLAN_ITEM_ATTRIBUTE_REMOVED;
    }
}
