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

import java.util.Arrays;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApproval;

/**
 *
 * @author Dawid Holewa
 */
public class RtcApprovalsExtendedEditorTreeModel implements TreeModel {

    RtcApproval root;

    public RtcApprovalsExtendedEditorTreeModel(RtcApproval root) {
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        RtcApproval a = (RtcApproval) parent;
        RtcApproval[] approvals = a.getApprovals();
        return approvals[index];
    }

    @Override
    public int getChildCount(Object parent) {
        RtcApproval a = (RtcApproval) parent;
        if (!a.isDescriptor()) {
            return 0;
        } else {
            int res = a.getApprovals().length;
            return res;
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        RtcApproval a = (RtcApproval) node;
        return !a.isDescriptor();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // nie rob nic
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        RtcApproval par = (RtcApproval) parent;
        RtcApproval ch = (RtcApproval) child;
        return Arrays.asList(par.getApprovals()).indexOf(ch);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        //
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        //
    }

}
