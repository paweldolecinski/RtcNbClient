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
package pl.edu.amu.wmi.kino.rtc.client.queries.actions;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Patryk Żywica
 */
public class AddListAction extends AbstractAction implements Presenter.Popup {

	private static final long serialVersionUID = 856317525570005196L;
	private static AbstractAction action;

    static public AbstractAction getDefault() {
        if (action == null) {
            action = new AddListAction();
        }
        return action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //no action
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(NbBundle.getMessage(AddListAction.class, "CTL_AddListAction.name"));
        Action[] actions = null;
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/AddActions");
            actions = list.toArray(new Action[]{});
        }
        for (Action a : actions) {
            menu.add(a);
        }
        return menu;
    }
}
