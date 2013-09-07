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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.connections;

import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;

/**
 *
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
public interface RtcConnectionsManager extends EventSource<RtcConnectionsManagerEvent> {

    /**
     * Adds new <code>RtcRepositoryConnection</code> to list and properties file and connects automaticly witch this repository.
     * @param url server url to which you want to connect
     * @param name repository name, that name is displayed on the list of connections to repositories
     * @param username login name of the repository user
     * @param password repository user password
     * @param connectAtStart information about connecting to the repository at the start of program
     */
    public abstract void addRepositoryConnection(String url, String name, RtcAuthenticationInfo rai, boolean connectAtStart);

    /**
     * Adds new <code>RtcRepositoryConnection</code> to list and properties file and connects automaticly witch this repository.
     * @param repositoryConnection repository to add
     */
    public abstract void addRepositoryConnection(RtcRepositoryConnection repositoryConnection);

    /**
     * Creates new <code>RtcRepositoryConnection</code> object.
     * @param url server url to which you want to connect
     * @param name repository name, that name is displayed on the list of connections to repositories
     * @param rai information about the type of authentication used in this connection
     * @param connectAtStart information about connecting to the repository at the start of program
     * @return {@link RtcRepositoryConnection}
     */
    public abstract RtcRepositoryConnection createRepositoryConnection(String url, String name, RtcAuthenticationInfo rai, boolean connectAtStart);

    /**
     * Gets list of added <code>RtcConnectionsManager</code>.
     * @return List<RtcConnectionsManager>
     * @see List
     * @see RtcConnectionsManager
     */
    public abstract RtcRepositoryConnection[] getRepositoryConnections();

    /**
     * @param rtcRepositoryConnection 
     */
    public abstract void removeRepositoryConnection(RtcRepositoryConnection rtcRepositoryConnection);

    /**
     * Checks if there alredy exists connection with given url
     * @param url connection url which have to be check
     * @return true if exist or false if not exist
     */
    public abstract boolean verifyConnectionURL(String url);

    public abstract boolean isTeamPlatformActive();

    public abstract void shutdown();

    public abstract ActiveProjectArea[] getActiveProjectAreas();

    public abstract ActiveProjectArea[] getActiveProjectAreas(RtcRepositoryConnection repositoryConnection);

    public abstract ActiveProjectArea activateProjectArea(RtcRepositoryConnection repositoryConnection, ProjectArea projectArea);

    public abstract ActiveProjectArea getActiveProjactArea(RtcRepositoryConnection rc, ProjectArea area);

    public abstract void deactivateProjectArea(ActiveProjectArea activeProjectArea);

    public abstract boolean isActiveProjectArea(RtcRepositoryConnection repositoryConnection, ProjectArea projectArea);

    public abstract void refreshConnections();

    public abstract String[] getSmardCardAliases();
}
