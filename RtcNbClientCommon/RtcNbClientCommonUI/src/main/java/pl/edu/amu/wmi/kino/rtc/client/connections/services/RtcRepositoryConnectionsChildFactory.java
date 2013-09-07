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
package pl.edu.amu.wmi.kino.rtc.client.connections.services;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;

/**
 *
 * @author Patryk Żywica
 */
public class RtcRepositoryConnectionsChildFactory extends ChildFactory<RtcRepositoryConnection> implements EventListener<RtcConnectionsManagerEvent> {

    private RtcConnectionsManager manager;

    @Override
    protected boolean createKeys(List<RtcRepositoryConnection> toPopulate) {
        if (manager == null) {
            manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
            manager.addListener(this);
        }
        toPopulate.addAll(Arrays.asList(manager.getRepositoryConnections()));
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcRepositoryConnection key) {
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        return new RtcRepositoryConnectionNode(ic);
    }

    @Override
    public void eventFired(RtcConnectionsManagerEvent event) {
        switch (event) {
            case CONNECTION_LIST_CHANGED:
            case PLATFORM_SHUTDOWN:
            case PLATFORM_STARTUP_COMPLETE:
                refresh(false);
                break;
            case ACTIVE_PROJECT_AREA_LIST_CHANGED:
                break;
        }
    }
}
