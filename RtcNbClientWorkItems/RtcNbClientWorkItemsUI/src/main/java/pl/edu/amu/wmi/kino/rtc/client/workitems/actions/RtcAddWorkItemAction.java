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
package pl.edu.amu.wmi.kino.rtc.client.workitems.actions;



import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;

/**
 * Action that opens work item top component which lets us create a new workitem.
 * @author Tomasz Adamski
 */
public class RtcAddWorkItemAction extends AbstractAction {

	private static final long serialVersionUID = 5416892930784756733L;
	private static AbstractAction action;

    public static AbstractAction getDefault() {
        if (action == null) {
            action = new RtcAddWorkItemAction();
        }
        return action;
    }

    private RtcAddWorkItemAction() {
        super(NbBundle.getMessage(RtcAddWorkItemAction.class, "RtcAddWorkItemAction.name"));
        //TODO: Remove after implentation of actionPerformed method
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException(NbBundle.getMessage(RtcAddWorkItemAction.class, "NotSupported.exp"));
    }
}
