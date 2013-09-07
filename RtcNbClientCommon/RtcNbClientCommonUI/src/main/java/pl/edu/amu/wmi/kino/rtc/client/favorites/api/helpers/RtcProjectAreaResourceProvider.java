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
package pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.UnableToGetTheReferenceException;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;

/**
 *
 * @author Patryk Żywica
 */
public abstract class RtcProjectAreaResourceProvider implements RtcResourceProvider {


    private final String SPLIT_PATTERN = "\\\\\\\\~\\\\\\\\";
    private final String SPLITER = "\\\\~\\\\";
    public abstract Node getReferenceFromProjectArea(String resourceId, ActiveProjectArea activeProjectArea)
            throws UnableToGetTheReferenceException;

    public abstract String getProjectAreaResourceId(ActiveProjectArea area,Lookup context);

    @Override
    public Node getReference(String resourceId, ActiveRepository activeRepository) throws UnableToGetTheReferenceException {
        ProjectAreaManager manager = activeRepository.getLookup().lookup(ProjectAreaManager.class);
        String[] tmp =resourceId.split(SPLIT_PATTERN, 2);

        String paId = tmp[0];
        String otherId=tmp[1];

        ProjectArea a = manager.findProjectArea(paId);
        if(a==null){
            throw new UnableToGetTheReferenceException(resourceId, true);
        }
        RtcConnectionsManager connManager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        if(connManager.isActiveProjectArea(activeRepository.getRepositoryConnection(), a)){
            return getReferenceFromProjectArea(
                    otherId,
                    connManager.getActiveProjactArea(activeRepository.getRepositoryConnection(), a));
        }else{
            //we wont have any favorites from not active project areas
            throw new UnableToGetTheReferenceException(resourceId, false);
        }
    }

    @Override
    public String getResourceId(Lookup context) {
        ActiveProjectArea area = context.lookup(ActiveProjectArea.class);
        String paId = area.getProjectArea().getId();
        return paId + SPLITER+getProjectAreaResourceId(area, context);
    }

}
