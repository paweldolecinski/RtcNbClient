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

import java.awt.EventQueue;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;

/**
 *
 * @author Dawid
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcPreferencesStorageImpl.class)
public class RtcPreferencesStorageImpl {

    static private final String repositoriesListKey = "repositoriesList";
    private List<RtcBasicConnectionInformationImpl> repoConnections = new ArrayList<RtcBasicConnectionInformationImpl>();
    private List<String> activeProjectAreas = new ArrayList<String>();
    private Preferences rtc_preferences;

    public RtcPreferencesStorageImpl() {
        load();
    }

    public void load() {
        assert !EventQueue.isDispatchThread();
        rtc_preferences = NbPreferences.forModule(RtcPreferencesStorageImpl.class);
        String repositoriesList = rtc_preferences.get(repositoriesListKey, null);

        if (repositoriesList != null) {
            XMLDecoder decoder = null;
            try {
                ByteArrayInputStream inStream = new ByteArrayInputStream(repositoriesList.getBytes());
                decoder = new XMLDecoder(inStream);
                repoConnections = (List<RtcBasicConnectionInformationImpl>) decoder.readObject();
            } catch (ArrayIndexOutOfBoundsException ex) {
                repoConnections = new LinkedList<RtcBasicConnectionInformationImpl>();
                //TODO : i18n
                RtcLogger.getLogger(RtcPreferencesStorageImpl.class).log(Level.FINE, "Cannot read connection list from NbPreferences. Using empty list instead.", ex);
            } finally {
                decoder.close();
            }
        }else{
            repoConnections = new LinkedList<RtcBasicConnectionInformationImpl>();
            //TODO : i18n
            RtcLogger.getLogger(RtcPreferencesStorageImpl.class).log(Level.FINEST, "There is no connections key in NbPreferences. Using default value.");
        }
    }

    public void save() {
        assert !EventQueue.isDispatchThread();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(outStream);
        encoder.writeObject(repoConnections);
        encoder.close();
        rtc_preferences.put(repositoriesListKey, outStream.toString());
    }

    public RtcRepositoryConnectionImpl[] getRepositories() {
        ArrayList<RtcRepositoryConnectionImpl> toReturn = new ArrayList<RtcRepositoryConnectionImpl>(repoConnections.size());
        for(RtcBasicConnectionInformationImpl basic :  repoConnections){
            if(basic.getConnectionId()==null){
            }
            toReturn.add(RtcBasicConnectionInformationsFactory.getRepositoryConnection(basic));
        }
        return toReturn.toArray(new RtcRepositoryConnectionImpl[]{});
    }

    public void addRepository(RtcRepositoryConnection rc) {
        if (rc instanceof RtcRepositoryConnectionImpl) {
            addRepository(RtcBasicConnectionInformationsFactory.getBasicInformation((RtcRepositoryConnectionImpl) rc));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void addRepository(RtcBasicConnectionInformationImpl b) {
        repoConnections.add((RtcBasicConnectionInformationImpl) b);
        doSave();
    }

    public void removeRepository(RtcRepositoryConnection rc) {
        if (rc instanceof RtcRepositoryConnectionImpl) {
            removeRepository(RtcBasicConnectionInformationsFactory.getBasicInformation((RtcRepositoryConnectionImpl) rc));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void removeRepository(RtcBasicConnectionInformationImpl b) {
        repoConnections.remove(b);
        doSave();
    }

    public void updateRepository(RtcRepositoryConnection b) {
        if (b instanceof RtcRepositoryConnectionImpl) {
            RtcBasicConnectionInformationImpl basic = RtcBasicConnectionInformationsFactory.getBasicInformation((RtcRepositoryConnectionImpl) b);

            basic.setAutoconnect(b.getAutoconnect());
            basic.setName(b.getName());
            basic.setRai(b.getRtcAuthenticationInfo());
            basic.setUrl(b.getUrl());

            doSave();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void doSave() {
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                save();
            }
        });
    }
}
