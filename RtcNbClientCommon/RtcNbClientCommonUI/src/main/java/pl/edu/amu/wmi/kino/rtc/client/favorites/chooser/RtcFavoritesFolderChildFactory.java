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
package pl.edu.amu.wmi.kino.rtc.client.favorites.chooser;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesEntry;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;

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
        for (RtcFavoritesFolder f : folder) {
            if (!(f instanceof RtcFavoritesEntry)) {
                toPopulate.add(f);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcFavoritesFolder key) {
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        if (key instanceof RtcFavoritesEntry) {
            throw new IllegalArgumentException();
        } else {
            return new RtcFavoritesFolderNode(ic);
        }
    }

    @Override
    public void fireEvent(RtcFavoritesFolderEvent eventType) {
        switch (eventType) {
            case ChildrenChanged:
                refresh(true);
        }
    }
}
