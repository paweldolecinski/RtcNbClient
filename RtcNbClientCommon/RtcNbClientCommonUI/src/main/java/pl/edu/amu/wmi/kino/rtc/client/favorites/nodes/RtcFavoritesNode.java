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

import java.io.IOException;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesServiceImpl;
import pl.edu.amu.wmi.kino.rtc.client.favorites.actions.RtcAddFavoritesFolderAction;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;

/**
 * this class represent's favourites in 
 * @author psychollek
 */
public class RtcFavoritesNode extends AbstractNode implements LookupListener, EventListener<RtcConnectionsManagerEvent> {

    private InstanceContent ic;
    private Action[] actions;
    private Lookup.Result<RtcResourceProvider> result;

    public RtcFavoritesNode(InstanceContent ic) {
        super(
                Children.create(new RtcFavoritesFolderChildFactory(Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class).getRootFolder()), true),
                new AbstractLookup(ic));
        this.ic = ic;
        ic.add(Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class).getRootFolder());
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/favorites/nodes/favorites_node.gif");
        setDisplayName(NbBundle.getMessage(RtcFavoritesNode.class, "RtcFavoritesNode.displayName"));
        setShortDescription(NbBundle.getMessage(RtcFavoritesNode.class, "RtcFavoritesNode.shortDescription"));

        //System.out.println("jest root i dodaje listeney");
        result = Lookup.getDefault().lookupResult(RtcResourceProvider.class);
        result.allInstances();
        result.addLookupListener(this);

        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        manager.addListener(this);
    }

    @Override
    public void destroy() throws IOException {
        super.destroy();
        result = Lookup.getDefault().lookupResult(RtcResourceProvider.class);
        result.allInstances();
        result.removeLookupListener(this);

        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        manager.removeListener(this);
    }

    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            actions = new Action[]{
                        context ? (new RtcAddFavoritesFolderAction()).createContextAwareInstance(getLookup()) : new RtcAddFavoritesFolderAction()};
        }
        return actions;
    }

    /**
     *
     * @return the help context
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcFavoritesNode.class);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setChildren(Children.create(new RtcFavoritesFolderChildFactory(Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class).getRootFolder()), true));
        //System.out.println("lookup event");
    }

    @Override
    public void eventFired(RtcConnectionsManagerEvent event) {
        switch (event) {
            case ACTIVE_PROJECT_AREA_LIST_CHANGED:
            case CONNECTION_LIST_CHANGED:
                setChildren(Children.create(new RtcFavoritesFolderChildFactory(Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class).getRootFolder()), true));
                break;
        }
        //System.out.println("connections event");
    }
}
