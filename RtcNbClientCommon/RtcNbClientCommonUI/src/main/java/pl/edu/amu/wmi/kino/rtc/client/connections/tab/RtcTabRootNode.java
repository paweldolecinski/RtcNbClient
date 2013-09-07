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

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;

/**
 * A root node of <code>RtcTabTopComponent</code>. This class is listening for changes
 * in <code>RtcActiveProjectAreas</code>.
 * @author Michał Wojciechowski
 * @see RtcActiveProjectAreas
 */
public class RtcTabRootNode extends AbstractNode{

    public RtcTabRootNode() {
        super(Children.create(new RtcTabRootChildFactory(), true));
        //TODO : i18n
        setName("RtcActiveProjectAreasNode");
    }

    @Override
    public HelpCtx getHelpCtx() {
        String s = "usingJazzServer.administering.configuringServer.configuringServerToServerCommunication";
        return new HelpCtx(s);
    }
}
