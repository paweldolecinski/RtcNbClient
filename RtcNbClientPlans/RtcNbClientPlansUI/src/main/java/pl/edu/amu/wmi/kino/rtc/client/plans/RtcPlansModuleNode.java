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
package pl.edu.amu.wmi.kino.rtc.client.plans;

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
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager.RtcPlansEvent;

/**
 *
 * @author Michal Wojciechowski
 * @author Patryk Żywica
 */
public class RtcPlansModuleNode extends AbstractNode implements EventListener<RtcPlansEvent> {

    private Action[] actions;

    public RtcPlansModuleNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        setChildren(Children.create(new RtcPlansModuleNodeChildFactory(
                getLookup().lookup(RtcPlansManager.class),
                getLookup().lookup(ActiveProjectArea.class)), true));
        setDisplayName(NbBundle.getMessage(RtcPlansModuleFactory.class, "PlansModuleNode.name"));
        getLookup().lookup(RtcPlansManager.class).addListener(this);
        setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/plans/hierarchy/planning_domain.gif");
    }

    @Override
    public Action[] getActions(boolean bln) {
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/PlansModule/PlansNodeActions");
            actions = list.toArray(new Action[]{});
        }
        return actions;
    }

    @Override
    public void eventFired(RtcPlansEvent event) {
        //TODO : implement
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcPlansModuleNode.class);
    }
}
