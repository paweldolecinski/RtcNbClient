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
import pl.edu.amu.wmi.kino.rtc.client.favorites.actions.RtcRemoveFromFavoritesAction;

/**
 *
 * @author Patryk Żywica
 */
public class RtcRemoveFromFavoritesFilterNode extends FilterNode {

    public RtcRemoveFromFavoritesFilterNode(Node node,InstanceContent ic) {
        super(node,node.getChildren(),new ProxyLookup(new AbstractLookup(ic),node.getLookup()));
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList();
        for (Action action : super.getActions(context)) {
            actions.add(action);
        }
        actions.add(context
                ? new RtcRemoveFromFavoritesAction().createContextAwareInstance(getLookup())
                : new RtcRemoveFromFavoritesAction());
        return actions.toArray(new Action[0]);
    }
}
