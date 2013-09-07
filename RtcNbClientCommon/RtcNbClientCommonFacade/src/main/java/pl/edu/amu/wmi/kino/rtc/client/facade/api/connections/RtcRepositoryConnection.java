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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 *
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
public interface RtcRepositoryConnection extends EventSource<RtcRepositoryConnectionEvent> {


    /**
     * Connect with repository and display progress on progressbar at the bottom of main window.
     * Display also errors if they exist.
     * @param repositoryConnection <code>RtcRepositoryConnectionImpl</code> object that represents the repository to which you want to connect.
     */
    public abstract void connect();

    /**
     * Disconnects from RTC repository
     */
    public abstract void disconnect();

    public abstract Boolean getAutoconnect();

    /**
     * @return the name of repository connection
     */
    public abstract String getName();

    /**
     * @return the RtcAuthInfo of repository connection
     */
    public abstract  RtcAuthenticationInfo getRtcAuthenticationInfo();

    /**
     * @return the url
     */
    public abstract String getUrl();

    /**
     * Gets info about status of connection.
     * @return true if is connected, false otherwise
     */
    public abstract boolean isConnected();

    /**
     * Reconnects to RTC repository
     */
    public abstract void reconnect();


    public abstract void setAutoconnect(Boolean autoconnect);

    /**
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @param rai the RtcAuthenticationInfo to set
     */
    public abstract void setRtcAuthenticationInfo(RtcAuthenticationInfo rai);

    /**
     * Sets url for this connection.
     * @param url the url to set
     */
    public abstract void setUrl(String url);

    /**
     *
     * @return null iff this repository connect is not connected
     */
    public abstract ActiveRepository getActiveRepository();
/**
 * 
 * @return
 */
    public abstract String getConnectionID();

}
