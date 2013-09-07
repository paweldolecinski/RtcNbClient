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
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.WorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet;

/**
 * Action that opens work item top component which lets us edit current work item.
 * @author Michał Wojciechowski
 */
public class RtcOpenWorkItemAction extends AbstractAction implements ContextAwareAction, HelpCtx.Provider {

	private static final long serialVersionUID = 6729298840283464217L;
	private Lookup context;

    public RtcOpenWorkItemAction(Lookup context) {
        super(NbBundle.getMessage(RtcOpenWorkItemAction.class, "RtcOpenWorkItemAction.name"));
        this.context = context;
    }

    public static Action createContextAwareAction(Lookup actionContext) {
        RtcOpenWorkItemAction action = new RtcOpenWorkItemAction(actionContext);
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

        if (context.lookup(RtcWorkItemAttributeSet.class) == null
                || context.lookup(RtcWorkItem.class) == null) {
            throw new NullPointerException(NbBundle.getMessage(RtcOpenWorkItemAction.class, "WImissing.exp"));
        }

        Lookup.getDefault().lookup(WorkItemsManager.class).find(context).requestActive();

        /*
        WindowManager wm = WindowManager.getDefault();
        String uniqueId = RtcWorkItemTopComponent.class.getSimpleName()
                + Integer.toString(context.lookup(IWorkItem.class).getId());

        TopComponent tp = wm.findTopComponent(uniqueId);
        if (tp == null) {
            tp = new RtcWorkItemTopComponent(context);
            tp.open();
            tp.requestActive();
        } else {
            tp.requestActive();
        }*/

    }

}
