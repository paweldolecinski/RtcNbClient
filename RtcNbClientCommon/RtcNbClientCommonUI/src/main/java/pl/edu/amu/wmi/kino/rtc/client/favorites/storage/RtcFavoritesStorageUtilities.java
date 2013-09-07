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

import java.util.ArrayList;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesEntry;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;

/**
 *
 * @author Patryk Żywica
 */
public class RtcFavoritesStorageUtilities {

    public static RtcFavoritesFolder createFavoritesFolderFromStorage(RtcFavoritesFolder parent,RtcStorageFolder storage) {
        RtcFavoritesFolder folder = new RtcFavoritesFolder(parent, storage.getName());
        if(storage instanceof RtcStorageEntry){
            RtcStorageEntry e = (RtcStorageEntry) storage;
            RtcFavoritesEntry entry  = new RtcFavoritesEntry(
                    parent,
                    e.getProviderId(),
                    e.getResourceId(),
                    e.getConnectionId());

            folder=entry;
        }
        for(RtcStorageFolder ch : storage.getChildren()){
            folder.add(createFavoritesFolderFromStorage(folder, ch));
        }
        return folder;
    }

    public static RtcStorageFolder createStorageFolderFromFavorites(RtcFavoritesFolder folder) {
        RtcStorageFolder storage = new RtcStorageFolder();
        if (folder instanceof RtcFavoritesEntry) {
            RtcFavoritesEntry e = (RtcFavoritesEntry) folder;
            RtcStorageEntry s = new RtcStorageEntry();
            s.setConnectionId(e.getconnectionId());
            s.setProviderId(e.getProviderId());
            s.setResourceId(e.getResourceId());
            storage = s;
        }
        storage.setName(folder.getName());
        List<RtcStorageFolder> children = new ArrayList<RtcStorageFolder>(folder.size());
        for(RtcFavoritesFolder f : folder){
            children.add(createStorageFolderFromFavorites(f));
        }
        storage.setChildren(children);
        return storage;
    }

    private RtcFavoritesStorageUtilities() {
    }

}
