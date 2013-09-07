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
package pl.edu.amu.wmi.kino.rtc.client.plans.actions;

import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.BooleanStateAction;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.cookies.RtcTwoStateSwitchCookie;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlannedItemsPageRightPanelSwitchAction extends BooleanStateAction {

    private static final long serialVersionUID = 4531436278L;

    @Override
    public boolean isEnabled() {
        return true;
        //maybe it is because action should listen on lookup
//        return Utilities.actionsGlobalContext().lookup(RtcTwoStateSwitchCookie.class) != null;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        super.actionPerformed(ev);
        RtcTwoStateSwitchCookie stateSwitch = Utilities.actionsGlobalContext().lookup(RtcTwoStateSwitchCookie.class);
        stateSwitch.setEnabled(getBooleanState());
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcPlannedItemsPageRightPanelSwitchAction.class, "PlannedItemsPageRightPanelSwitch.name");
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/sidebar_icon.gif";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}