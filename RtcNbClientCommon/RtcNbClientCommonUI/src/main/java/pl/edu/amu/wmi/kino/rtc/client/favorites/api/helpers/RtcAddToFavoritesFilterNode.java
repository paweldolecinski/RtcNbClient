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

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;

/**
 * this class is a filter node to add the add to favorites action - you propably
 * want to use this instead of directly adding the action to the node, and then
 * not use it in provider - so the action addtoFavorites is not present there.
 * @author psychollek
 */
public class RtcAddToFavoritesFilterNode extends FilterNode{

    private RtcAddToFavoritesFilterNode(Node orginal,InstanceContent ic,Object... content) {
        super(orginal,orginal.getChildren(),new ProxyLookup(new AbstractLookup(ic),orginal.getLookup()));
        for (int i = 0; i < content.length; i++) {
            ic.add(content[i]);
        }
    }

    /**
     * Should be used if your provider needs projectArea in context, but the
     * original node doesn't have it in it's lookup.
     * 
     * @param orginal node that will be filtered.
     * @param projectArea project area that will be addted to the lookup
     * @param provider will be added to the lookup
     */
    public RtcAddToFavoritesFilterNode(Node orginal,ActiveProjectArea projectArea,RtcResourceProvider provider) {
        this(orginal,new InstanceContent(),projectArea,provider);
    }

    /**
     * Use this constructor if you already have anything necessary for provider to work in
     * orginal node lookup, and want to add only the provider and apropriate action.
     * @param orginal node that will be filtered.
     * @param provider will be added to the lookup.
     */
    public RtcAddToFavoritesFilterNode(Node orginal,RtcResourceProvider provider){
        this(orginal,new InstanceContent(),provider);
    }

    /**
     * use this constructor if you need to pass some special objects to the lookup
     * and you don't want to put them in orginals lookup, remember, that for
     * addToFavorites action be enabled, provider have to be in either aditionalContent
     * or in orginal node lookup, as well as any objects needed by provider.
     * @param orginal
     * @param aditionalContent
     */
    public RtcAddToFavoritesFilterNode(Node orginal,Object... aditionalContent){
        this(orginal,new InstanceContent(),aditionalContent);
    }


    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList();
        for(Action action : super.getActions(context)){
            actions.add(action);
        }
        actions.add(context ?
                        new RtcAddToFavoritesAction().createContextAwareInstance(getLookup())
                        : new RtcAddToFavoritesAction());
        return actions.toArray(new Action[0]);
    }


}
