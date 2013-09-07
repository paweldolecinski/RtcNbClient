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

/**
 *
 * @author Bartosz Zaleski 
 */
public class RtcSmartCardAuthenticationInfo implements  RtcAuthenticationInfo{

    private static final long serialVersionUID = 17121986181219860L;

    private String alias;

    public RtcSmartCardAuthenticationInfo() {
        //for serialization purpouse
    }

    public RtcSmartCardAuthenticationInfo(String alias){
        this.alias = alias;
    }

    public RtcSmartCardAuthenticationInfo(RtcAuthenticationInfo rai){
        if(rai instanceof RtcSmartCardAuthenticationInfo){
            this.alias = ((RtcSmartCardAuthenticationInfo)rai).getAlias();
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    
    

}
