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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager.ContributorManagerEvent;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public interface ContributorManager extends EventSource<ContributorManagerEvent> {

    /**
     * Use this method to get licences possessed by contributor.
     * @return all licenses of given contributor
     */
    public abstract ContributorLicense[] getContributorLicences(Contributor contributor);
    
    /**
     * 
     * @return current logged in contributor
     */
    public abstract Contributor getLoggedInContributor();
    /**
     * 
     * @return all non archieved contributors
     */
    public abstract Contributor[] getContributors();
/**
     * 
     * @return all contributors on server
     */
    public abstract Contributor[] getAllContributors();
    /**
     * Calling this method should force synchronizing all model objects with corresponding
     * server data.
     */
    public abstract void synchronizeWithServer();


    /**
     * This enumeration defines all possible events for <code>RtcPlan</code>.
     *
     * @author Patryk Żywica
     */
    public enum ContributorManagerEvent {

        /**
         * This event should be called when new contributor to manager.
         */
        CONTRIBUTOR_ADDED,
        /**
         * This event should be called when contributor was removed from manager.
         */
        CONTRIBUTOR_REMOVED,
        /**
         * This event should be called after all plans synchronization with server data.
         */
        CONTRIBUTOR_MANAGER_SYNCHRONIZED_WITH_SERVER;
    }
}
