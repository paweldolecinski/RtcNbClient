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
package pl.edu.amu.wmi.kino.netbeans.view.customview.impl.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import org.openide.util.ImageUtilities;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewDefaultExpandColumn extends JButton {

    private static final long serialVersionUID = 536164574657L;
    private CustomViewNodeGroup nodeGroup;
    private static Icon collapsedIcon = ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/netbeans/view/customview/resources/treeTriangle.png", false);
    private static Icon expandedIcon = ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/netbeans/view/customview/resources/treeTriangleExpanded.png", false);

    public CustomViewDefaultExpandColumn() {
        super("");
        setContentAreaFilled(false);
        setBorderPainted(false);
        addActionListener(new ActionListenerImpl());
    }

    public CustomViewDefaultExpandColumn(boolean expanded) {
        this();
        if (expanded) {
            setIcon(expandedIcon);
        } else {
            setIcon(collapsedIcon);
        }
    }

    public void setNodeGroup(CustomViewNodeGroup nodeGroup) {
        this.nodeGroup = nodeGroup;
        if (!nodeGroup.isLeaf()) {
            if (nodeGroup.isExpanded()) {
                setIcon(expandedIcon);
            } else {
                setIcon(collapsedIcon);
            }
        }

        if (nodeGroup.isLeaf()) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

    private class ActionListenerImpl implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    if (nodeGroup.isExpanded()) {
                        nodeGroup.collapse();
                        setIcon(collapsedIcon);
                    } else {
                        nodeGroup.expand();
                        setIcon(expandedIcon);
                    }
                }
            });

        }
    }
}
