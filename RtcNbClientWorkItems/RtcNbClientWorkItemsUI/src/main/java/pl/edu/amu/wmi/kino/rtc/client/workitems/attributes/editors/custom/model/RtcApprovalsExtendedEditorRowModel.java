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

import java.sql.Timestamp;

import org.netbeans.swing.outline.RowModel;

import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalContributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcApprovalsExtendedPanel;

/**
 *
 * @author Dawid Holewa
 */
public class RtcApprovalsExtendedEditorRowModel implements RowModel {

    RtcApproval app;

    public RtcApprovalsExtendedEditorRowModel(RtcApproval a) {
       app = a;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Timestamp.class;
            default:
                assert false;
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 2; // 3 jak odkomentujemy switche nizej
    }

    @Override
    public String getColumnName(int column) {
        String[] columns = {
            //            org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedEditor.column1.name.text"),
            org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedEditor.column2.name.text"),
            org.openide.util.NbBundle.getMessage(RtcApprovalsExtendedPanel.class, "RtcApprovalsExtendedEditor.column3.name.text")
        };
        return columns[column];
    }

    @Override
    public Object getValueFor(Object node, int column) {

        if (node instanceof RtcApprovalDescriptor) {
            switch (column) {
                case 0:
                    String state = ((RtcApprovalDescriptor) node).getState().getDisplayName();
                    return (state != null) ? state : "--";
                case 1:
                    Timestamp date = ((RtcApprovalDescriptor) node).getDueDate();

                    return date;
            }
        } else if (node instanceof RtcApproval) {
            switch (column) {
                case 0:
                    String state = ((RtcApproval) node).getState().getDisplayName();
                    return (state != null) ? state : "--";
                case 1:
                    return null;
            }
        }
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (node instanceof RtcApproval) {
            switch (column) {
                case 0:
                    return true;
                case 1:
                    return false;
                default:
                    return false;
            }
        }

        return false;


    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        if (column == 0 && node instanceof RtcApproval) {
            RtcApprovalContributor approv = (RtcApprovalContributor) node;
            approv.setState((RtcApprovalState) value);
        }

    }
}
