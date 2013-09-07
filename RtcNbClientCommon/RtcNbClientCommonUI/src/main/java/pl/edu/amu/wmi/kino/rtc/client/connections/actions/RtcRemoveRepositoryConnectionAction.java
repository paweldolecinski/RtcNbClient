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
package pl.edu.amu.wmi.kino.rtc.client.connections.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;

/**
 *
 * @author Patryk Żywica
 */
public class RtcRemoveRepositoryConnectionAction extends NodeAction {

    @Override
    protected boolean enable(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            if (n.getLookup().lookup(RtcRepositoryConnection.class) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        for (Node n : activatedNodes) {
            RtcRepositoryConnection conn = activatedNodes[0].getLookup().lookup(RtcRepositoryConnection.class);
            manager.removeRepositoryConnection(conn);
        }
    }
    @Override
    public String getName() {
        return NbBundle.getMessage(RtcRemoveRepositoryConnectionAction.class,
                "RtcRemoveRepositoryConnectionAction.name");
    }
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcRemoveRepositoryConnectionAction.class);
    }
        @Override
    protected boolean asynchronous() {
        return true;
    }
}
