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

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;
import pl.edu.amu.wmi.kino.rtc.client.favorites.actions.RtcRemoveFromFavoritesAction;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers.RtcAddToFavoritesAction;

/**
 * this filter node removes any RtcAddToFavoritesAction returned by the source node
 * and adds RtcRemoveFromFavoritesAction to the node 
 * @author psychollek
 */
public class RtcFavoritesFilterNode extends FilterNode{

    public RtcFavoritesFilterNode(Node orginal,RtcFavoritesFolder folder) {
        this(orginal, folder , new InstanceContent());
    }

    private RtcFavoritesFilterNode(Node orginal,RtcFavoritesFolder folder,InstanceContent ic) {
        super(orginal,orginal.getChildren(), new ProxyLookup(orginal.getLookup(),new AbstractLookup(ic)));
        ic.add(folder);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList();
        for(Action action : super.getActions(context)){
            if(!(action instanceof RtcAddToFavoritesAction)){
                actions.add(action);
            }
        }
        if(context){
            actions.add((new RtcRemoveFromFavoritesAction()).createContextAwareInstance(getLookup()));
        }
        return actions.toArray(new Action[0]);
    }

}
