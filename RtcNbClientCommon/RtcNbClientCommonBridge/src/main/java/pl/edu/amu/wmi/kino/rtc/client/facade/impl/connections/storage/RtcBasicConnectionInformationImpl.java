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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;

/**
 * Class which represents basic information about RtcRepositoryConnection
 * such as:
 * <ul>
 * <li>url - server url to which you want to connect</li>
 * <li>name -  repository name, that name is displayed on the list of connections to repositorie</li>
 * <li>rai - authentication info for this connection info
 * <li>autoconnect - information about connecting to the repository at the start of program</li>
 * </ul>
 * It's used to serialization.
 */
public class RtcBasicConnectionInformationImpl implements Serializable {

    private static final long serialVersionUID = 8989980L;
    private String url;
    private String name;
    private RtcAuthenticationInfo rai;
    private String connectionId;
    private Boolean autoconnect;
    private List<String> autoloadProjectAreas;

    public RtcBasicConnectionInformationImpl() {
        //for serialization reasons
    }

    RtcBasicConnectionInformationImpl(RtcRepositoryConnection rc) {
        url = rc.getUrl();
        name = rc.getName();
        rai = rc.getRtcAuthenticationInfo();

        autoconnect = rc.getAutoconnect();
        if (rc.getConnectionID() == null) {
            //System.out.println("jest null w konstruktorze basic connection");
        }
        connectionId = rc.getConnectionID();
        autoloadProjectAreas = new ArrayList<String>(4);
    }

    public void addAutoloadProjectArea(String uuid) {
        if (!autoloadProjectAreas.contains(uuid)) {
            autoloadProjectAreas.add(uuid);
        }
    }

    public void removeAutoloadProjectArea(String uuid) {
        if (autoloadProjectAreas.contains(uuid)) {
            autoloadProjectAreas.remove(uuid);
        }
    }

    public List<String> getAutoloadProjectAreas() {
        return autoloadProjectAreas;
    }

    public void setAutoloadProjectAreas(List<String> auto) {
        autoloadProjectAreas = auto;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Rai
     */
    public RtcAuthenticationInfo getRai() {
        return rai;
    }

    /**
     * @return the autoconnect
     */
    public Boolean getAutoconnect() {
        return autoconnect;
    }

    /**
     * @
     * Set url connection.
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @
     * Set name connection.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @
     * Set rai of the connection.
     * @param rai the RtcAuthenticationInfo to set
     */
    public void setRai(RtcAuthenticationInfo rai) {
        this.rai = rai;
    }

    /**
     *
     * @param autoconnect the autoconnect to set
     */
    public void setAutoconnect(Boolean autoconnect) {
        this.autoconnect = autoconnect;
    }

    public boolean isAutoloadProjectArea(String uuid) {
        for (String apa : autoloadProjectAreas) {
            if (apa.equalsIgnoreCase(uuid)) {
                return true;
            }
        }
        return false;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RtcBasicConnectionInformationImpl other = (RtcBasicConnectionInformationImpl) obj;

        if (!connectionId.equals(other.connectionId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 757346;
        hash = 41 * hash + connectionId.hashCode();
        return hash;
    }
}
