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

import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;
import pl.edu.amu.wmi.kino.rtc.client.favorites.actions.RtcAddFavoritesFolderAction;
import pl.edu.amu.wmi.kino.rtc.client.favorites.actions.RtcRemoveFavoritesFolderAction;

/**
 *
 * @author Patryk Żywica
 */
class RtcFavoritesFolderNode extends AbstractNode {

    private InstanceContent ic;
    private Action[] actions;

    public RtcFavoritesFolderNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;

        setChildren(Children.create(new RtcFavoritesFolderChildFactory(getFolder()), true));

        setDisplayName(getFolder().getName());
        setShortDescription(getFolder().getPath());
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/favorites/chooser/folder.gif");
    }

    private RtcFavoritesFolder getFolder() {
        return getLookup().lookup(RtcFavoritesFolder.class);
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            actions = new Action[]{
                        context ? (new RtcAddFavoritesFolderAction()).createContextAwareInstance(getLookup()) : new RtcAddFavoritesFolderAction(),
                        context ? (new RtcRemoveFavoritesFolderAction()).createContextAwareInstance(getLookup()) : new RtcRemoveFavoritesFolderAction()
                    };
        }
        return actions;
    }

    @Override
    public HelpCtx getHelpCtx() {
        String s = "usingJazzServer.administering.configuringServer.configuringServerToServerCommunication.editingEntriesFromServerFriendsList";
        return new HelpCtx(s);
    }
}
