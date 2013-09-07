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
public class RtcPasswordAuthenticationInfo implements RtcAuthenticationInfo{
    private static final long serialVersionUID = 5554450971215465050L;

    private String userName;
    private String password;

    public RtcPasswordAuthenticationInfo() {
        //For serialization
    }

    public RtcPasswordAuthenticationInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public RtcPasswordAuthenticationInfo(RtcAuthenticationInfo rai){
        if(rai instanceof RtcPasswordAuthenticationInfo){
            this.userName = ((RtcPasswordAuthenticationInfo)rai).getUserName();
            this.password = ((RtcPasswordAuthenticationInfo)rai).getPassword();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
