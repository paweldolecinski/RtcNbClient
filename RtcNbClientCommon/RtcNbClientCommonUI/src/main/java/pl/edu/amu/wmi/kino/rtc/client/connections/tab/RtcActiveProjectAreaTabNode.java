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

import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;

/**
 * Class represents a <code>RtcProjectArea</code> object in {@link RtcTabTopComponent}
 * @author Tomasz Adamski (tomasz.adamski@gmail.com)
 * @author Michał Wojciechowski
 */
public class RtcActiveProjectAreaTabNode extends AbstractNode {

    private InstanceContent ic;

    public RtcActiveProjectAreaTabNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        setChildren(Children.create(new RtcActiveProjectAreaTabChildFactory(getActiveProjectArea()), true));
        setShortDescription(NbBundle.getMessage(RtcActiveProjectAreaTabNode.class, "RtcActiveProjectAreaNode.tooltip"));
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/projectarea.gif");
        setName(getActiveProjectArea().getProjectArea().getName()
                + " [" + getActiveProjectArea().getRepositoryConnection().getName() + "]");
    }

    @Override
    public Action[] getActions(boolean b) {
        List<Action> actions = new LinkedList<Action>();
        actions.addAll(Utilities.actionsForPath("Rtc/Modules/CoreModule/Nodes/ActiveProjectAreaActions"));
        return actions.toArray(new Action[]{});
    }
    /**
     *
     * @return the help context
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcActiveProjectAreaTabNode.class);
    }

    private ActiveProjectArea getActiveProjectArea() {
        return getLookup().lookup(ActiveProjectArea.class);
    }
}
