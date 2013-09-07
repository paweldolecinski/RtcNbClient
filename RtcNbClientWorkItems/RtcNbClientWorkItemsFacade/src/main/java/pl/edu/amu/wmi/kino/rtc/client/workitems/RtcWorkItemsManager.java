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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author Patryk Żywica
 */
public abstract class RtcWorkItemsManager {

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @param iteration for which work items have to be returned.
     * @param owner for which work items have to be returned.
     * @return array of work items assigned for given iteration.
     */
    public abstract RtcWorkItem[] getWorkItemsFor(Iteration iteration, ProcessArea owner);

    /**
     * Returns work item with given id or null if such work item isn't exists.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @param workItemId which is work item server id number
     * @return return plan with given id or null.
     */
    public abstract RtcWorkItem findWorkItem(int workItemId);


    /**
     * 
     * @param workItemId
     * @return
     */
    public abstract RtcWorkItem[] findWorkItems(Integer[] workItemId);
    /**
     * Calling this method should force synchronizing all model objects with corresponding
     * server data.
     *
     * This can be long running operation. Do not call on event dispatch thread.
     */
    public abstract void synchronizeWithServer();

    /**
     * Creates draft of work item. Every attributes of it are empty.
     *
     * This draft is created only locally. You should save it by your self (with reqauried fields setted)
     *
     *
     * @param area
     * @param type 
     * @return empty draft of work item
     */
    public abstract RtcWorkItem createWorkItem(ProcessArea area, RtcWorkItemType type);

    /**
     * not implemented yet
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @return array of all possible plan types
     */
    public abstract RtcWorkItemType[] getWorkItemTypes();

    /**
     * @since 0.2.1.4
     * @return 
     */
    public abstract ActiveProjectArea getActiveProjectArea();
}
