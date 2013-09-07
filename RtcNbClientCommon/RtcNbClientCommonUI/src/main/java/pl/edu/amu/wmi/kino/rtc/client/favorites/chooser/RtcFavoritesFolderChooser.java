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

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesServiceImpl;

/**
 *
 * @author Patryk Żywica
 */
public class RtcFavoritesFolderChooser extends TopComponent implements ExplorerManager.Provider {

    private static final long serialVersionUID = 4216564576754675L;
    private ExplorerManager explorerManager = new ExplorerManager();

    public RtcFavoritesFolderChooser() {
//        super(new BorderLayout());
        setLayout(new BorderLayout());
        RtcFavoritesServiceImpl service = Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class);

        InstanceContent ic = new InstanceContent();
        ic.add(service.getRootFolder());
        associateLookup(ExplorerUtils.createLookup(explorerManager, getActionMap()));
        explorerManager.setRootContext(new RtcFavoritesFolderNode(ic));

        BeanTreeView btv = new BeanTreeView();
        btv.setRootVisible(true);
        add(btv, BorderLayout.CENTER);


    }

    public RtcFavoritesFolder getSelectedFolder() {
        List<RtcFavoritesFolder> folders = new LinkedList<RtcFavoritesFolder>();
        for (Node n : getExplorerManager().getSelectedNodes()) {
            RtcFavoritesFolder f = n.getLookup().lookup(RtcFavoritesFolder.class);
            if (f != null) {
                folders.add(f);
            }
        }
        if (folders.size() > 1 || folders.isEmpty()) {
            return null;
        } else {
            return folders.get(0);
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
}
