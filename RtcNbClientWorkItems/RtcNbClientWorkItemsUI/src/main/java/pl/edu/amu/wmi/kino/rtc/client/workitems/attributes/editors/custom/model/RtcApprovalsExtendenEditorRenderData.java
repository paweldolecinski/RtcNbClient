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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.model;

import org.netbeans.swing.outline.RenderDataProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcApprovalsExtendenEditorRenderData implements RenderDataProvider {

    @Override
    public java.awt.Color getBackground(Object o) {
        return null;
    }

    @Override
    public String getDisplayName(Object o) {
        if (o instanceof RtcApproval) {
            String state = ((RtcApproval) o).getName();
            return (state != null) ? state : "--";
        }
        return "";
    }

    @Override
    public java.awt.Color getForeground(Object o) {

        return null;
    }

    @Override
    public javax.swing.Icon getIcon(Object o) {
        if (o instanceof RtcApproval) {
            RtcApprovalType type = ((RtcApproval) o).getType();
            return type.getIcon();
        }
        return null;
    }

    @Override
    public String getTooltipText(Object o) {
        return "";
    }

    @Override
    public boolean isHtmlDisplayName(Object o) {
        return false;
    }
}
