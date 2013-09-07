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
package pl.edu.amu.wmi.kino.rtc.client.connections.tab;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcIndependentModuleFactory;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcRepositoryDependentModuleFactory;

/**
 *
 * @author Patryk Żywica
 */
public class RtcTabRootChildFactory extends ChildFactory<Object> implements EventListener<RtcConnectionsManagerEvent> {

    private boolean listenerInitialized = false;

    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        toPopulate.addAll(Arrays.asList(manager.getActiveProjectAreas()));
        toPopulate.addAll(Lookups.forPath("Rtc/Modules/RepositoryNodeFactories").lookupAll(RtcRepositoryDependentModuleFactory.class));
        toPopulate.addAll(Lookups.forPath("Rtc/Modules/IndependentNodeFactories").lookupAll(RtcIndependentModuleFactory.class));
        if (!listenerInitialized) {
            manager.addListener(this);
        }
        return true;
    }

    @Override
    protected Node[] createNodesForKey(Object key) {
        if (key instanceof ActiveProjectArea) {
            InstanceContent ic = new InstanceContent();
            ic.add(key);
            return new Node[]{new RtcActiveProjectAreaTabNode(ic)};
        }else{
            if(key instanceof RtcRepositoryDependentModuleFactory){
                RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
                RtcRepositoryDependentModuleFactory rcdmf = (RtcRepositoryDependentModuleFactory) key;
                List<Node> nodes = new LinkedList<Node>();
                for(RtcRepositoryConnection rc : manager.getRepositoryConnections()){
                    nodes.addAll(Arrays.asList(rcdmf.createModuleNodes(rc.getActiveRepository())));
                }
                return nodes.toArray(new Node[]{});
            }else{
                if(key instanceof RtcIndependentModuleFactory){
                    RtcIndependentModuleFactory imf = (RtcIndependentModuleFactory) key;
                    return imf.createModuleNodes();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void eventFired(RtcConnectionsManagerEvent event) {
        if (event.equals(RtcConnectionsManagerEvent.ACTIVE_PROJECT_AREA_LIST_CHANGED)) {
            refresh(false);
        }

    }
}
