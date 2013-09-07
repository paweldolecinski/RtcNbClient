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
import javax.swing.Action;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.WorkItemEditorManager;

/**
 * Action that opens work item top component which lets us edit current work item.
 * @author Michał Wojciechowski
 */
public class DevOpenWorkItemAction extends AbstractAction implements ContextAwareAction, HelpCtx.Provider {

	private static final long serialVersionUID = 6729298840283464217L;
	private Lookup context;

    public DevOpenWorkItemAction(Lookup context) {
        super("Dev"+NbBundle.getMessage(DevOpenWorkItemAction.class, "RtcOpenWorkItemAction.name"));
        this.context = context;
    }

    public static Action createContextAwareAction(Lookup actionContext) {
        DevOpenWorkItemAction action = new DevOpenWorkItemAction(actionContext);
        return action;
    }
/**
 *
 * @return the default help context
 */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return createContextAwareAction(actionContext);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (context.lookup(RtcWorkItem.class) == null) {
            throw new NullPointerException(NbBundle.getMessage(DevOpenWorkItemAction.class, "WImissing.exp"));
        }

        WorkItemEditorManager rwiem = Lookup.getDefault().lookup(WorkItemEditorManager.class);
        rwiem.openEditor(context.lookup(RtcWorkItem.class));
    }

}
