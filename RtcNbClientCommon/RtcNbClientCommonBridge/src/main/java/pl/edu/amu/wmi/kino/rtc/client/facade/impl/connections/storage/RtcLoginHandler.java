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

import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.login.SSLCertificateLoginInfo;
import com.ibm.team.repository.client.login.SmartCardLoginInfo;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcPasswordAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSSLCertificateAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSmartCardAuthenticationInfo;

/**
 *
 * @author Bartosz Zaleski 
 */
public class RtcLoginHandler implements ILoginHandler2{

    private RtcAuthenticationInfo rai;

    public static final int USERNAME_PASSWORD = 1;

    public static final int SSL_CERTIFICATE = 2;

    public static final int SMART_CARD = 3;

    private int authType;

    public RtcLoginHandler(RtcAuthenticationInfo rai) {
        this.rai = rai;
        if (this.rai instanceof RtcPasswordAuthenticationInfo){
            authType = USERNAME_PASSWORD;
        }
        if (this.rai instanceof RtcSSLCertificateAuthenticationInfo){
            authType = SSL_CERTIFICATE;
        }
        if (this.rai instanceof RtcSmartCardAuthenticationInfo){
            authType = SMART_CARD;
        }
    }

    @Override
    public ILoginInfo2 challenge(ITeamRepository itr) {
        //To trzeba będzie rozbudować sensownie dla obsługi błędów autentykacji
        switch(authType){
            case USERNAME_PASSWORD:
                return new UsernameAndPasswordLoginInfo(((RtcPasswordAuthenticationInfo)rai).getUserName(), ((RtcPasswordAuthenticationInfo)rai).getPassword());
            case SSL_CERTIFICATE:
                return new SSLCertificateLoginInfo(((RtcSSLCertificateAuthenticationInfo)rai).getCertificateLocation(), ((RtcSSLCertificateAuthenticationInfo)rai).getPassword());
            case SMART_CARD:
                return new SmartCardLoginInfo(((RtcSmartCardAuthenticationInfo)rai).getAlias());
            default: return null;
        }
    }
    
}
