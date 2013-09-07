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
package pl.edu.amu.wmi.kino.rtc.client.favorites.nodes;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesEntry;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.UnableToGetTheReferenceException;

/**
 *
 * @author Patryk Żywica
 */
class RtcFavoritesFolderChildFactory extends ChildFactory<RtcFavoritesFolder> implements RtcFavoritesFolder.RtcFavoritesFolderListener {

    private RtcFavoritesFolder folder;
    private boolean isListenerInitialized = false;

    RtcFavoritesFolderChildFactory(RtcFavoritesFolder folder) {
        this.folder = folder;
    }

    @Override
    protected boolean createKeys(List<RtcFavoritesFolder> toPopulate) {
        if (!isListenerInitialized) {
            folder.addRtcFavoritesFolderListener(this);
        }
        toPopulate.addAll(folder);
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcFavoritesFolder key) {
        assert !EventQueue.isDispatchThread();
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        if (key instanceof RtcFavoritesEntry) {
            RtcFavoritesEntry entry = (RtcFavoritesEntry) key;

            Collection<? extends RtcResourceProvider> providers =
                    Lookup.getDefault().lookupAll(RtcResourceProvider.class);
            RtcResourceProvider provider = null;
            for (RtcResourceProvider rp : providers) {
                if (rp.getClass().getName().equals(entry.getProviderId())) {
                    provider = rp;
                    break;
                }
            }
            if (provider == null) {
                //TODO : i18n
                RtcLogger.getLogger(RtcFavoritesFolderChildFactory.class).log(Level.WARNING, "Niemozna znalesc providera");
                return new RtcFavoritesBrokenEntryNode(ic);
            }
            RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
            for (RtcRepositoryConnection rc : manager.getRepositoryConnections()) {
                if (rc.getConnectionID().equals(entry.getconnectionId())) {
                    //TODO : szukamy po connection id, mozna dodac metode do wyszukiwania do api
                    try {
                        return new RtcRemoveFromFavoritesFilterNode(
                                provider.getReference(entry.getResourceId(), rc.getActiveRepository()),
                                ic);
                    } catch (UnableToGetTheReferenceException ex) {
                        if (ex.getDeleteFavoritesEntry()) {
                            RtcLogger.getLogger(RtcFavoritesFolderChildFactory.class).log(Level.SEVERE, "Niemozna znalesc pozycji, zostanie usunieta", ex);
                            //TODO : wyswietlic komunikat z pyttaniem o usuniecie zepsutego favorka
                        } else {
                            RtcLogger.getLogger(RtcFavoritesFolderChildFactory.class).log(Level.WARNING, "Niemozna znalesc pozycji", ex);
                        }
                        return new RtcFavoritesBrokenEntryNode(ic);
                    } catch(Exception ex){
                        RtcLogger.getLogger(RtcFavoritesFolderChildFactory.class).log(Level.WARNING, "Niemozna znalesc pozycji, inny blad", ex);
                    }
                }
            }
            //TODO : trzab to zrefaktoryzowac zeby ten komunikat byl poprawnie
            RtcLogger.getLogger(RtcFavoritesFolderChildFactory.class).log(Level.SEVERE, "Niemozna znalesc polaczenia z repozytorium");
            return new RtcFavoritesBrokenEntryNode(ic);
        } else {
            return new RtcFavoritesFolderNode(ic);
        }
    }

    @Override
    public void fireEvent(RtcFavoritesFolderEvent eventType) {
        switch (eventType) {
            case ChildrenChanged:
                refresh(false);
        }
    }
}
