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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 * Implementations of this abstract class will be responsible for managing process
 * sets.
 * @since 0.3.0.1
 * @author Patryk Żywica
 */
public interface ProcessManager extends EventSource<ProcessManager.RtcProcessEvent> {

    public Contributor[] getMembers(ProcessArea area);

    /**
     *
     * @since 0.3.0.1
     * @return array of top level team areas defined in this project area
     */
    public abstract TeamArea[] getTeamAreas();

    /**
     *
     * @return list of TA children.
     */
    abstract public TeamArea[] getTeamAreas(TeamArea teamArea);

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @return array of all development lines registered in this manager.
     */
    public abstract DevelopmentLine[] getDevelopmentLines();

    /**
     * 
     * @since 0.3.0.1
     * @return 
     */
    public abstract DevelopmentLine getMainDevelopmentLine();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.3.0.1
     * @return
     */
    public abstract Iteration getCurrentIteration(DevelopmentLine developmentLine);
    
    public abstract Iteration[] getIterations(DevelopmentLine developmentLine);
    
    public abstract Iteration[] getIterations(Iteration iteration);
    
    public abstract Deliverable[] getDeliverables();

    /**
     * Calling this method should force synchronizing all model objects with corresponding
     * server data.
     * This can be long running operation. Do not call on event dispatch thread.
     * @since 0.3.0.1
     */
    public abstract void synchronizeWithServer();

    /**
     * This enumeration defines all possible events for <code>ProcessManager</code>.
     *
     * @author Pawel Dolecinski
     */
    public enum RtcProcessEvent {

        /**
         * This event should be called when new set was added to manager.
         */
        PROCESS_CHANGED,
        ITERATION_ADDED,
        ITERATION_REMOVED,
        ITERATION_MODIFIED;
    }
}
