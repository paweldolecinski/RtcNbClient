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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
//TODO: javadoc: lack of author

/**
 * A root node of <code>RtcConnectionsManagerImpl</code>.
 * This class listens for changes in <code>RtcConnectionsManagerImpl</code>.
 */
//TODO : i18n
@ServicesTabNodeRegistration(displayName = "RTC Repositories", //displayName="#RtcRepositoryConnectionsNode.name"
iconResource = "pl/edu/amu/wmi/kino/rtc/client/connections/services/folder_repo.gif",
name = "RtcRepositoryConnections", shortDescription = "Rational team concert repositories", //shortDescription="#RtcRepositoryConnectionsNode.description"
position = 700)
public class RtcRepositoryConnectionsNode extends AbstractNode implements EventListener<RtcConnectionsManagerEvent> {

    private boolean passive = true;
    ArrayList<Action> actionsList = new ArrayList<Action>();

    public RtcRepositoryConnectionsNode() {
        super(Children.create(new RtcRepositoryConnectionsChildFactory(), true));
        RtcConnectionsManager connectionsManager = Lookup.getDefault().lookup(RtcConnectionsManager.class);

        setDisplayName(NbBundle.getMessage(RtcRepositoryConnectionsNode.class, "RtcRepositoryConnectionsNode.name"));
        setShortDescription(NbBundle.getMessage(RtcRepositoryConnectionsNode.class, "RtcRepositoryConnectionsNode.tooltip"));
        if (connectionsManager.isTeamPlatformActive()) {
            activate();
        } else {
            passivate();
        }
    }

        @Override
    public Action[] getActions(boolean b) {
        List<Action> actions = new LinkedList<Action>();
        actions.addAll(Utilities.actionsForPath("Rtc/Modules/CoreModule/Nodes/ServicesActions"));
        actions.addAll(Arrays.asList(super.getActions(b)));
        return actions.toArray(new Action[]{});
    }

    private synchronized void passivate() {
        RtcConnectionsManager rtcRepositoryConnections = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        rtcRepositoryConnections.removeListener(this);
        //TODO: substitute this for a not enable icon - proper icon is set in activate()
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/folder_repo.gif");
        passive = true;
    }

    private synchronized void activate() {
        RtcConnectionsManager rtcRepositoryConnections = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        rtcRepositoryConnections.addListener(this);
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/folder_repo.gif");
        passive = false;
    }

    @Override
    public void eventFired(RtcConnectionsManagerEvent type) {
        switch (type) {
            case PLATFORM_STARTUP_COMPLETE: {
                activate();
                break;
            }
            case PLATFORM_SHUTDOWN: {
                passivate();
                break;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        RtcConnectionsManager connectionsManager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        connectionsManager.removeListener(this);
    }

    /**
     *
     * @return the help context
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcRepositoryConnectionsNode.class);
    }
}
