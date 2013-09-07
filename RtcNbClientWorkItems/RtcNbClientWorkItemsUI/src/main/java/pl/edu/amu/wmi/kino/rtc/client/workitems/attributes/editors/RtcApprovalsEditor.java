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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcApprovalsExtendedPanel;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovals;

/**
 * Approvals editor
 * @author Pawel Dolecinski
 * @author Dawid Holewa
 */
public class RtcApprovalsEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private Lookup context;
    private PropertyEnv env;

    public RtcApprovalsEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
    }

    /**
     * Gets {@link RtcApprovals} object string representation.
     * It is an empty string because the object is too complex to present it as a string.
     * @return empty string
     */
    @Override
    public String getAsText() {
        return "";
    }

    /**
     * Creates new instance of {@link RtcApprovalsExtendedPanel}
     * object which represent custom editor for approvals.
     * @return instance of custom editor
     * @see RtcApprovalsExtendedPanel
     */
    @Override
    public Component getCustomEditor() {
        RtcApproval root = null;

        root = (RtcApproval) getValue();
        return new RtcApprovalsExtendedPanel(this, context, root, env);
    }

    /**
     * Informs the Netbeans platform that the custom editor is exist and we can use it.
     * @return true
     */
    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
}
