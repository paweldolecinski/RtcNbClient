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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcPasswordAuthenticationInfo;
import java.awt.EventQueue;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnectionEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveRepositoryImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ConnectionsManagerImpl;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.transport.TeamServiceException;
import com.ibm.team.repository.transport.client.AuthenticationException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;


/**
 *
 * @author Patryk Żywica
 */
public class RtcRepositoryConnectionImpl implements RtcRepositoryConnection {

    private String url;
    private String name;
    private RtcAuthenticationInfo rai;
    private String id;
    private boolean autoconnect;
    private ActiveRepository activeRepository;
    private ITeamRepository iTeamRepository;
    private EventSourceSupport<RtcRepositoryConnectionEvent> sourceEvent = new EventSourceSupport<RtcRepositoryConnectionEvent>();
    

    public RtcRepositoryConnectionImpl(String url, String name, RtcAuthenticationInfo rai, boolean autoconnect) {
        this.url = url;
        this.name = name;
        this.rai = rai;
        this.autoconnect = autoconnect;
        Random rand = new Random();
        this.id = "repositoryConnection#" + rand.nextLong() + "/" + (new Date()).toString();
    }

    RtcRepositoryConnectionImpl(RtcBasicConnectionInformationImpl bi) {
        this.url = bi.getUrl();
        this.name = bi.getName();
        this.rai = bi.getRai();
        this.autoconnect = bi.getAutoconnect();
        this.id = bi.getConnectionId();
    }

    @Override
    public void connect() {
        assert !EventQueue.isDispatchThread();
        final RtcRepositoryConnectionImpl repository = this;
        ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ConnectionsManagerImpl.class, "RtcConnectingToRepository.name")
                + " " + getName());
        try {
            ph.start(3);
            ph.progress(NbBundle.getMessage(ConnectionsManagerImpl.class, "RtcLoggingIn.name"), 1);
            iTeamRepository = login();
            fireEvent(RtcRepositoryConnectionEvent.STATUS_CHANGED);
            ph.progress(NbBundle.getMessage(ConnectionsManagerImpl.class, "RtcFillingWithProjectAreas.name"), 2);
            ph.progress(3);
        } catch (AuthenticationException e) {
            //System.out.println("exception z loggerem");
            RtcLogger.getLogger(RtcRepositoryConnectionImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcRepositoryConnectionImpl.class, "WrongUsernameOrPasswordException.name"), e);
        } catch (TeamServiceException e) {
            //System.out.println("exception z loggerem");
            RtcLogger.getLogger(RtcRepositoryConnectionImpl.class).log(Level.SEVERE, NbBundle.getMessage(RtcRepositoryConnectionImpl.class, "WrongHostOrConnectionException.name"), e);
        } catch (TeamRepositoryException e) {
            //System.out.println("exception z loggerem");
            RtcLogger.getLogger(RtcRepositoryConnectionImpl.class).log(Level.WARNING, NbBundle.getMessage(ConnectionsManagerImpl.class, "UnexpectedRepositoryError.name"), e);
        } catch (Exception e) {
            //System.out.println("exception z loggerem");
            RtcLogger.getLogger(RtcRepositoryConnectionImpl.class).log(Level.WARNING, e.getLocalizedMessage(), e);
        } finally {
            ph.finish();
        }
    }
//TODO : tu trzeba zmienić cały login
    private ITeamRepository login() throws TeamRepositoryException {
        assert !EventQueue.isDispatchThread();
        ITeamRepository repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(url);

        repo.registerLoginHandler(new RtcLoginHandler(rai));

        repo.login(null);
        return repo;
    }

    @Override
    public void disconnect() {
        if (iTeamRepository != null) {
            iTeamRepository.logout();
            iTeamRepository = null;
        }
        fireEvent(RtcRepositoryConnectionEvent.STATUS_CHANGED);
    }

    @Override
    public boolean isConnected() {
        if (iTeamRepository == null) {
            return false;
        }
        return iTeamRepository.loggedIn();
    }

    @Override
    public void reconnect() {
        disconnect();
        connect();
    }

    @Override
    public ActiveRepository getActiveRepository() {
        if(!isConnected()){
                return null;
            }
        if (activeRepository == null) {
            activeRepository = new ActiveRepositoryImpl(this);
        }
        return activeRepository;
    }

    @Override
    public Boolean getAutoconnect() {
        return autoconnect;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public RtcAuthenticationInfo getRtcAuthenticationInfo() {
        return rai;
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setAutoconnect(Boolean autoconnect) {
        if (this.autoconnect != autoconnect) {
            this.autoconnect = autoconnect;
            Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class).updateRepository(this);
            fireEvent(RtcRepositoryConnectionEvent.AUTOCONNECT_CHANGED);
        }
    }

    @Override
    public void setName(String name) {
        if (!this.name.equals(name)) {
            this.name = name;
            Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class).updateRepository(this);
            fireEvent(RtcRepositoryConnectionEvent.NAME_CHANGED);
        }
    }

    @Override
    public void setRtcAuthenticationInfo(RtcAuthenticationInfo rai) {
        if (!rai.equals(this.rai)) {
            this.rai = rai;
            Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class).updateRepository(this);
            fireEvent(RtcRepositoryConnectionEvent.AUTHENTICATION_INFO_CHANGED);
        }
    }

    @Override
    public void setUrl(String url) {
        if (!url.equals(this.url)) {
            this.url = url;
            Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class).updateRepository(this);
            fireEvent(RtcRepositoryConnectionEvent.URL_CHANGED);
        }
    }


    public ITeamRepository getTeamRepository() {
        return iTeamRepository;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RtcRepositoryConnectionImpl other = (RtcRepositoryConnectionImpl) obj;
        if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7576;
        hash = 43 * hash + id.hashCode();
        return hash;
    }

    @Override
    public String getConnectionID() {
        return id;
    }

    public final void removeListener(EventListener<RtcRepositoryConnectionEvent> listener) {
        sourceEvent.removeListener(listener);
    }

    public final void fireEvent(RtcRepositoryConnectionEvent event) {
        sourceEvent.fireEvent(event);
    }

    public final void addListener(EventListener<RtcRepositoryConnectionEvent> listener) {
        sourceEvent.addListener(listener);
    }
    
}
