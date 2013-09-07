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
package pl.edu.amu.wmi.kino.rtc.client.favorites.storage;

import java.awt.EventQueue;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service=RtcFavoritesStorageImpl.class)
public class RtcFavoritesStorageImpl {

    private Preferences rtc_preferences;
    private RtcStorageFolder rootFolder;
    static private final String favoritesListKey = "favoritesList";

    private void load() {
        assert !EventQueue.isDispatchThread();
        rtc_preferences = NbPreferences.forModule(RtcFavoritesStorageImpl.class);
        String repositoriesList = rtc_preferences.get(favoritesListKey, null);

        if (repositoriesList != null) {
            XMLDecoder decoder = null;
            try {
                ByteArrayInputStream inStream = new ByteArrayInputStream(repositoriesList.getBytes());
                decoder = new XMLDecoder(inStream);
                rootFolder = (RtcStorageFolder) decoder.readObject();
            } catch (ArrayIndexOutOfBoundsException ex) {
                rootFolder = createNewRootStorageFolder();
                //TODO : i18n
                RtcLogger.getLogger(RtcFavoritesStorageImpl.class).log(Level.FINE, "Cannot read favourites list from NbPreferences. Using empty list instead.", ex);
            } finally {
                decoder.close();
                if (rootFolder == null) {
                    rootFolder = createNewRootStorageFolder();
                    //TODO : i18n
                    RtcLogger.getLogger(RtcFavoritesStorageImpl.class).log(Level.FINE, "Cannot read favourites list from NbPreferences. Using empty list instead.");
                }
            }
        } else {
            rootFolder = createNewRootStorageFolder();
            //TODO : i18n
            RtcLogger.getLogger(RtcFavoritesStorageImpl.class).log(Level.FINEST, "There is no favourites key in NbPreferences. Using default value.");
        }
    }

    public RtcStorageFolder getRootFolder() {
        if (rootFolder == null) {
            load();
        }
        return rootFolder;
    }

    public void setRootFolder(RtcStorageFolder f){
        rootFolder=f;
    }

    public void save() {
        assert !EventQueue.isDispatchThread();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(outStream);

        encoder.writeObject(rootFolder);
        encoder.close();
        rtc_preferences.put(favoritesListKey, outStream.toString());
    }

    private RtcStorageFolder createNewRootStorageFolder(){
        RtcStorageFolder s = new RtcStorageFolder();
        s.setChildren(new ArrayList<RtcStorageFolder>(4));
        //TODO : i18n
        s.setName("Favorites");
        return s;
    }
}
