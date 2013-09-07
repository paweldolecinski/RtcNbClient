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

import javax.swing.JButton;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;

import pl.edu.amu.wmi.kino.rtc.client.plans.actions.dialogs.RtcPlanChecksDialog;

/**
 * 
 * @author Bartosz Zaleski <b4lzak@gmail.com>
 */
public class RtcShowPlanChecksAction extends CallableSystemAction {
    private static final long serialVersionUID = 32415545643534L;
    
    @Override
    public void performAction() {
        
        JButton closeButton=new JButton("Close");
        Object options[]=new Object[3];
        options[0]=SystemAction.get(RtcOpenProblematicWorkItemAction.class).getToolbarPresenter();
        options[2]=closeButton;
        options[1]=SystemAction.get(RtcSelectProblematicWorkItemAction.class).getToolbarPresenter();
        RtcPlanChecksDialog myDialog=new RtcPlanChecksDialog();
        DialogDescriptor dd = new DialogDescriptor(myDialog,
                "title",
                false,
                options,
                closeButton,
                DialogDescriptor.BOTTOM_ALIGN,
                HelpCtx.DEFAULT_HELP,
                null);
        DialogDisplayer.getDefault().notify(dd);

    }


    @Override
    public String getName() {
        return NbBundle.getMessage(RtcShowPlanChecksAction.class, "CTL_RtcShowPlanChecks");
    }

    @Override
    protected String iconResource() {
        return super.iconResource();
    }


    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
