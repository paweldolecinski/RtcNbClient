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
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnectionEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreasManagerEvent;

/**
 *
 * @author Patryk Żywica
 */
class RtcRepositoryConnectionChildFactory extends ChildFactory<ProjectArea> implements EventListener<RtcRepositoryConnectionEvent> {

    private ProjectAreaManager manager;
    private RtcRepositoryConnection repositoryConnection;

    public RtcRepositoryConnectionChildFactory(RtcRepositoryConnection connection) {
        this.repositoryConnection = connection;
    }

    @Override
    protected boolean createKeys(List<ProjectArea> toPopulate) {
        if(manager==null){
            manager = repositoryConnection.getActiveRepository().getLookup().lookup(ProjectAreaManager.class);
            repositoryConnection.addListener(this);
            if(manager!=null){
                manager.addListener(new RtcProjectAreasManagerListener());
            }
        }
        if (repositoryConnection.isConnected()) {
            if (manager != null) {
                toPopulate.addAll(Arrays.asList(manager.getProjectAreas()));
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ProjectArea key) {
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        ic.add(repositoryConnection);
        ic.add(manager);
        return new RtcProjectAreaNode(ic);
    }

    @Override
    public void eventFired(RtcRepositoryConnectionEvent event) {
        switch (event) {
            case STATUS_CHANGED:
                refresh(false);
                break;
            case NAME_CHANGED:
                break;
        }
    }

    private class RtcProjectAreasManagerListener implements EventListener<ProjectAreasManagerEvent>{

        @Override
        public void eventFired(ProjectAreasManagerEvent event) {
            switch(event){
                case PROJECT_AREAS_LIST_CHANGED:
                    refresh(false);
                    break;
            }
        }

    }
}
