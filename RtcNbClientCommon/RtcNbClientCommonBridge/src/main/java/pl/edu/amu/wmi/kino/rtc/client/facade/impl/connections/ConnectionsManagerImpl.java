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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections;

import com.ibm.team.apt.common.APTCommonPlugin;
import com.ibm.team.apt.internal.client.PlanningClientPlugin;
import com.ibm.team.apt.internal.ide.core.PlanningCorePlugin;
import com.ibm.team.apt.internal.service.APTServicePlugin;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.SmartCardLoginInfo;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage.RtcRepositoryConnectionImpl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.eclipse.osgi.framework.internal.core.Constants;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;
import org.openide.util.lookup.ServiceProvider;
import org.osgi.framework.BundleActivator;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage.RtcPreferencesStorageImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnectionEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage.RtcBasicConnectionInformationImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage.RtcBasicConnectionInformationsFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProjectAreaImpl;

/**
 *
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
@ServiceProvider(service = RtcConnectionsManager.class)
public class ConnectionsManagerImpl implements RtcConnectionsManager, EventListener<RtcRepositoryConnectionEvent> {

    private EventSourceSupport<RtcConnectionsManagerEvent> eventSource = new EventSourceSupport<RtcConnectionsManagerEvent>();
    private Map<ProjectArea, ActiveProjectAreaImpl> activeAreas =
            new HashMap<ProjectArea, ActiveProjectAreaImpl>(6);
    private Task startupTask;
    private Set<String> doneAutoconnect = new HashSet<String>(6);

    public ConnectionsManagerImpl() {


        if (!TeamPlatform.isStarted()) {
            Runnable rtcStartupProcess = new Runnable() {

                @Override
                public void run() {
                    ProgressHandle progress = ProgressHandleFactory.createHandle("RTC platform startup");
                    progress.setInitialDelay(1);
                    progress.start(100);
                    //TODO : i18n
                    progress.progress("Starting rtc platform", 10);
                    System.gc();

                    // Create a configuration property map.
                    Map<String, Object> configMap = new HashMap<String, Object>();
                    // Export the host provided service interface package.
                    configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
                            "host.service.command; version=1.0.0");
                    // Create host activator;
                    APTCommonPlugin m_activator = new APTCommonPlugin();
                    PlanningClientPlugin planningClientPlugin = new PlanningClientPlugin();
                    APTServicePlugin aPTServicePlugin = new APTServicePlugin();
                    PlanningCorePlugin planningCorePlugin = new PlanningCorePlugin();

                    List<BundleActivator> list = new ArrayList<BundleActivator>();
                    //list.add(activator);
                    list.add(m_activator);
                    list.add(planningClientPlugin);
                    list.add(aPTServicePlugin);
                    list.add(planningCorePlugin);
                    configMap.put("org.osgi.framework.storage.clean", "onFirstInit");
                    configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);

                    try {
                        // Now create an instance of the framework with
                        // our configuration properties.
                        Felix m_felix = new Felix(configMap);
                        // Now start Felix instance.
                        m_felix.start();
                    } catch (Exception ex) {
                        RtcLogger.getLogger(ConnectionsManagerImpl.class).log(Level.SEVERE, "Could not create framework: " + ex.getLocalizedMessage(), ex);
                        //System.err.println("Could not create framework: " + ex);
                        //ex.printStackTrace();
                    }
                    TeamPlatform.startup();
                    progress.progress("Starup complete, restoring connections", 80);

                    fireEvent(RtcConnectionsManagerEvent.PLATFORM_STARTUP_COMPLETE);
                    //TODO : i18n
                    progress.progress("Rtc platform startup complete", 100);
                    progress.finish();
                }
            };
            //new Thread(rtcStartupProcess,"rtcStartupProcess").start();
            startupTask = RequestProcessor.getDefault().post(rtcStartupProcess);
        }
    }

    @Override
    public void addRepositoryConnection(String url, String name, RtcAuthenticationInfo rai, boolean connectAtStart) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        RtcRepositoryConnectionImpl rc;
        rc = createRepositoryConnection(url, name, rai, connectAtStart);
        addRepositoryConnection(rc);
    }

    @Override
    public void addRepositoryConnection(RtcRepositoryConnection repositoryConnection) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);
        preferences.addRepository(repositoryConnection);
        fireEvent(RtcConnectionsManagerEvent.CONNECTION_LIST_CHANGED);
    }

    @Override
    public RtcRepositoryConnectionImpl createRepositoryConnection(
            String url, String name, RtcAuthenticationInfo rai, boolean autoconnect) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        return new RtcRepositoryConnectionImpl(url, name, rai, autoconnect);

    }

    @Override
    public RtcRepositoryConnectionImpl[] getRepositoryConnections() {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);
        for (RtcRepositoryConnectionImpl rc : preferences.getRepositories()) {
            rc.addListener(this);
            if (!doneAutoconnect.contains(rc.getConnectionID())) {
                if (rc.getAutoconnect()) {
                    RequestProcessor.getDefault().post(new RtcConnectRunnable(rc));
                }
                doneAutoconnect.add(rc.getConnectionID());
            }
        }
        return preferences.getRepositories();
    }

    @Override
    public void removeRepositoryConnection(RtcRepositoryConnection repositoryConnection) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        if (repositoryConnection instanceof RtcRepositoryConnectionImpl) {
            final RtcRepositoryConnectionImpl rtcRepositoryConnection = (RtcRepositoryConnectionImpl) repositoryConnection;
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    rtcRepositoryConnection.disconnect();
                    for (ActiveProjectAreaImpl projectArea : getActiveProjectAreas(rtcRepositoryConnection)) {
                        deactivateProjectArea(projectArea);
                    }
                    doneAutoconnect.remove(rtcRepositoryConnection.getConnectionID());
                    Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class).removeRepository(rtcRepositoryConnection);
                    fireEvent(RtcConnectionsManagerEvent.CONNECTION_LIST_CHANGED);
                    fireEvent(RtcConnectionsManagerEvent.ACTIVE_PROJECT_AREA_LIST_CHANGED);
                }
            });
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if there alredy exists connection with given url
     * @param url connection url which have to be check
     * @return true if exist or false if not exist
     */
    public boolean verifyConnectionURL(String url) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        for (RtcRepositoryConnectionImpl rpc : getRepositoryConnections()) {
            if (rpc.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ActiveProjectAreaImpl[] getActiveProjectAreas() {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        List<ActiveProjectArea> toReturn = new LinkedList<ActiveProjectArea>();
        for (RtcRepositoryConnectionImpl rc : getRepositoryConnections()) {
            toReturn.addAll(Arrays.asList(getActiveProjectAreas(rc)));
        }
        return toReturn.toArray(new ActiveProjectAreaImpl[]{});
    }

    @Override
    public ActiveProjectAreaImpl[] getActiveProjectAreas(RtcRepositoryConnection repositoryConnection) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        if (repositoryConnection instanceof RtcRepositoryConnectionImpl) {
            if (repositoryConnection.isConnected()) {
//                RtcBasicConnectionInformationImpl info = RtcBasicConnectionInformationsFactory.getBasicInformation((RtcRepositoryConnectionImpl) repositoryConnection);
//                List<String> autoloads = info.getAutoloadProjectAreas();
//
//                List<RtcActiveProjectAreaImpl> toReturn = new ArrayList<RtcActiveProjectAreaImpl>(autoloads.size());
//
//                ProjectAreaManager mgr = repositoryConnection.getActiveRepository().getLookup().lookup(ProjectAreaManager.class);
//                for (String tmp : autoloads) {
//                    ProjectArea area = mgr.findProjectArea(tmp);
//                    if (!activeAreas.containsKey(area)) {
//                        activateProjectArea(repositoryConnection, area);
//                    }
//                    toReturn.add(activeAreas.get(area));
//                }
//                return toReturn.toArray(new ActiveProjectAreaImpl[]{});
                List<ActiveProjectAreaImpl> toReturn = new ArrayList<ActiveProjectAreaImpl>(4);
                for (ActiveProjectAreaImpl a : activeAreas.values()) {
                    if (a.getRepositoryConnection().equals(repositoryConnection)) {
                        toReturn.add(a);
                    }
                }
                return toReturn.toArray(new ActiveProjectAreaImpl[]{});
            } else {
                return new ActiveProjectAreaImpl[]{};
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ActiveProjectArea getActiveProjactArea(RtcRepositoryConnection rc, ProjectArea area) {
        return activeAreas.get(area);
    }

    @Override
    public ActiveProjectAreaImpl activateProjectArea(RtcRepositoryConnection repositoryConnection, ProjectArea projectArea) {
        assert !EventQueue.isDispatchThread();
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        if (projectArea instanceof ProjectAreaImpl) {
            if (!repositoryConnection.isConnected()) {
                repositoryConnection.connect();
            }
            ActiveProjectAreaImpl area = new ActiveProjectAreaImpl(repositoryConnection, (ProjectAreaImpl) projectArea);
            if (!activeAreas.containsKey(projectArea)) {
                activeAreas.put(projectArea, area);
                RtcBasicConnectionInformationImpl info = RtcBasicConnectionInformationsFactory.getBasicInformation((RtcRepositoryConnectionImpl) repositoryConnection);
                info.addAutoloadProjectArea(((ProjectAreaImpl) projectArea).getIProcessArea().getItemId().getUuidValue());
                RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);
                preferences.updateRepository(repositoryConnection);
                fireEvent(RtcConnectionsManagerEvent.ACTIVE_PROJECT_AREA_LIST_CHANGED);
            }
            return activeAreas.get(projectArea);
        } else {
            throw new IllegalArgumentException();
        }

    }

    @Override
    public void deactivateProjectArea(ActiveProjectArea activeProjectArea) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        if (activeProjectArea instanceof ActiveProjectAreaImpl) {
            ActiveProjectAreaImpl area = (ActiveProjectAreaImpl) activeProjectArea;

            activeAreas.remove(activeProjectArea.getProjectArea());
            RtcBasicConnectionInformationImpl info = RtcBasicConnectionInformationsFactory.getBasicInformation(area.getRepositoryConnection());
            info.removeAutoloadProjectArea(area.getProjectArea().getIProcessArea().getItemId().getUuidValue());
            RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);
            preferences.updateRepository(activeProjectArea.getRepositoryConnection());
            fireEvent(RtcConnectionsManagerEvent.ACTIVE_PROJECT_AREA_LIST_CHANGED);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isTeamPlatformActive() {
        return TeamPlatform.isStarted();
    }

    @Override
    public boolean isActiveProjectArea(RtcRepositoryConnection repositoryConnection, ProjectArea projectArea) {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        return activeAreas.containsKey(projectArea);
    }

    @Override
    public void shutdown() {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        final RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);

        // for each repository connection from all connections we get list of project areas to check which one is active
        // and when is active we add it to our storage (preferences file)
        for (RtcRepositoryConnectionImpl repo : getRepositoryConnections()) {
            //System.out.println("shutdown dla " + repo.getName() + "is connected " + repo.isConnected());
            List<String> uuids = new LinkedList<String>();

            for (ActiveProjectAreaImpl apa : getActiveProjectAreas(repo)) {
                //System.out.println("dodaje do uuids " + apa.getProjectArea().getName());
                uuids.add(apa.getProjectArea().getId());
            }
            RtcBasicConnectionInformationImpl basic = RtcBasicConnectionInformationsFactory.getBasicInformation(repo);
            basic.setAutoloadProjectAreas(uuids);
            preferences.updateRepository(repo);
        }
        TeamPlatform.shutdown();
        fireEvent(RtcConnectionsManagerEvent.PLATFORM_SHUTDOWN);
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }

    @Override
    public void refreshConnections() {
        if (!startupTask.isFinished()) {
            startupTask.waitFinished();
        }
        RtcPreferencesStorageImpl preferences = Lookup.getDefault().lookup(RtcPreferencesStorageImpl.class);
        preferences.load();
        fireEvent(RtcConnectionsManagerEvent.CONNECTION_LIST_CHANGED);
    }

    @Override
    public void eventFired(RtcRepositoryConnectionEvent event) {
        fireEvent(RtcConnectionsManagerEvent.ACTIVE_PROJECT_AREA_LIST_CHANGED);
    }

    @Override
    public String[] getSmardCardAliases() {
        SmartCardLoginInfo info = new SmartCardLoginInfo();
        return info.getAliases().toArray(new String[]{});
    }

    public final void removeListener(EventListener<RtcConnectionsManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcConnectionsManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcConnectionsManagerEvent> listener) {
        eventSource.addListener(listener);
    }

    private static class RtcConnectRunnable implements Runnable {

        private RtcRepositoryConnectionImpl rc;

        public RtcConnectRunnable(RtcRepositoryConnectionImpl rc) {
            this.rc = rc;
        }

        @Override
        public void run() {
            //System.out.println("idze connect dla" + rc.getName());
            rc.connect();
            RtcConnectionsManager connManager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
            ActiveRepository r = rc.getActiveRepository();
            try {
                int c = 0;
                while (c < 150 && !rc.isConnected()) {
                    Thread.sleep(10);
                    c--;
                    //System.out.println("rc.isConnected " + rc.isConnected());
                }
                //System.out.println("koniec odliczania po " + c);
            } catch (InterruptedException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(ConnectionsManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            //System.out.println("rc.isConnected " + rc.isConnected());
            if (r != null) {
                ProjectAreaManager m = r.getLookup().lookup(ProjectAreaManager.class);
                RtcBasicConnectionInformationImpl basic = RtcBasicConnectionInformationsFactory.getBasicInformation(rc);
                //System.out.println("przed iteracja po auoload " + basic.getAutoloadProjectAreas().size());
                for (String pa : basic.getAutoloadProjectAreas()) {
                    ProjectArea area = m.findProjectArea(pa);
                    //System.out.println("jest autoload");
                    if (area != null) {
                        connManager.activateProjectArea(rc, area);
                    }
                }
            }
        }
    }
}
