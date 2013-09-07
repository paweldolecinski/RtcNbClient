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
package pl.edu.amu.wmi.kino.rtc.client.favorites;

import java.awt.Dialog;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcFavoritesService;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;
import pl.edu.amu.wmi.kino.rtc.client.favorites.chooser.RtcFavoritesFolderChooser;
import pl.edu.amu.wmi.kino.rtc.client.favorites.storage.RtcFavoritesStorageImpl;
import pl.edu.amu.wmi.kino.rtc.client.favorites.storage.RtcFavoritesStorageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.favorites.storage.RtcStorageFolder;

/**
 *
 * @author psychollek
 */
@ServiceProviders({
    @ServiceProvider(service = RtcFavoritesService.class),
    @ServiceProvider(service = RtcFavoritesServiceImpl.class)
})
public class RtcFavoritesServiceImpl implements RtcFavoritesService {

    private RtcFavoritesFolder rootFolder;

    @Override
    public void addToFavorites(RtcResourceProvider provider, String resourceId, ActiveRepository repository) {
        //wyswietlic okienko z drzewkiem
        RtcFavoritesFolderChooser chooser = new RtcFavoritesFolderChooser();
        //TODO : i18n
        DialogDescriptor desc = new DialogDescriptor(chooser, "Add to favorites");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
        dialog.setVisible(true);

        if (desc.getValue().equals(DialogDescriptor.OK_OPTION)) {
            RtcFavoritesFolder result = chooser.getSelectedFolder();
            if (result != null) {
                result.add(new RtcFavoritesEntry(
                        result,
                        provider.getClass().getName(),
                        resourceId,
                        repository.getRepositoryConnection().getConnectionID()));
            }
        }
    }

    public RtcFavoritesFolder getRootFolder() {
        if(rootFolder==null){
            RtcStorageFolder storage=Lookup.getDefault().lookup(RtcFavoritesStorageImpl.class).getRootFolder();
            rootFolder = RtcFavoritesStorageUtilities.createFavoritesFolderFromStorage(null, storage);
        }
        return rootFolder;
    }

    public void save(){
        RtcStorageFolder storage = RtcFavoritesStorageUtilities.createStorageFolderFromFavorites(rootFolder);
        RtcFavoritesStorageImpl fs = Lookup.getDefault().lookup(RtcFavoritesStorageImpl.class);
        fs.setRootFolder(storage);
        fs.save();
    }
}
