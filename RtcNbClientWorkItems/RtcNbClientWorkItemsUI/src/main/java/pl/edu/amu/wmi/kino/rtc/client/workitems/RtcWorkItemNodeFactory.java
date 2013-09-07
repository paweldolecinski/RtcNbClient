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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import org.openide.nodes.Node;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers.RtcAddToFavoritesFilterNode;

/**
 * This class is meant to provide a way to create nodes from workitems - which
 * then take responsibility for invking any editors, setting properties of the workitem,
 * etc.
 * @author psychollek
 * @author Dawid Holewa
 */
@ServiceProvider(service = RtcWorkItemNodeFactory.class)
public class RtcWorkItemNodeFactory{

    /**
     * This method creates a node for a given workitem, alongside with it's
     * propertysheets and necesary actions.
     * @param workItem which will be basis of the node
     * @param activeProjectArea 
     * @return Node with all properties and actions set
     */

    public Node createNodeForWorkItem(RtcWorkItem workItem, ActiveProjectArea activeProjectArea) {
        WorkItemNode n = new WorkItemNode(workItem, activeProjectArea);
        
        //My intention is to set displayname with html markups, and it should
        //work 
        n.setDisplayName(workItem.getDisplayName());
        String plainText = workItem.getDescription();
        n.setShortDescription((plainText.length() > 128) ? plainText.substring(0, 128) + "..." : plainText);
        //return (new RtcAddToFavoritesFilterNode(n, new RtcWorkItemResourceProvider()));
        return (new RtcAddToFavoritesFilterNode(n));
    }

}
