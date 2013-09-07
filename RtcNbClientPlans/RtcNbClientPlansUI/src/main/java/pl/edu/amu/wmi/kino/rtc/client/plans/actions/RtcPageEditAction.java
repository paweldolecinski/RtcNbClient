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
import javax.swing.SwingWorker;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.BooleanStateAction;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.cookies.RtcTwoStateEditSwitchCookie;

public class RtcPageEditAction extends BooleanStateAction implements LookupListener {

    private static final long serialVersionUID = 5461343567L;
    private Result<RtcTwoStateEditSwitchCookie> result = Utilities.actionsGlobalContext().lookupResult(RtcTwoStateEditSwitchCookie.class);

    public RtcPageEditAction() {
        if (!result.allInstances().isEmpty()) {
            RtcTwoStateEditSwitchCookie cookie = result.allInstances().iterator().next();
            setBooleanState(cookie.isEditing());
        }
        result.addLookupListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        super.actionPerformed(ev);
        //JOptionPane.showMessageDialog(null,getBooleanState());
        SwingWorker editWorker = new SwingWorker<Object, Void>() {

            @Override
            protected Object doInBackground() throws Exception {
                RtcTwoStateEditSwitchCookie mediator = Utilities.actionsGlobalContext().lookup(RtcTwoStateEditSwitchCookie.class);
                mediator.setEditing(getBooleanState());
                return null;
            }
        };
        editWorker.execute();

    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcPageEditAction.class, "CTL_RtcPageEditAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/plans/actions/resources/editPage.gif";
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        if (!result.allInstances().isEmpty()) {
            RtcTwoStateEditSwitchCookie cookie = result.allInstances().iterator().next();
            setBooleanState(cookie.isEditing());
        }
    }
}
