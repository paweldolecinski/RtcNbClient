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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view.tabs;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter.TabSlot;
import org.openide.util.NbBundle;
/**
 * More info about how to use JXMultiSplitPane at 
 * http://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html#resources
 * @author Patryk Żywica
 */
public class MainEditorCustomTabDisplayFactory implements DisplayFactory {

    private static class MainEditorCustomTabDisplay extends JXMultiSplitPane
            implements WorkItemTabPresenter.TabDisplay, SwingDisplay {
        private static final long serialVersionUID = 23415435435L;

        private JPanel right = new JPanel(new VerticalLayout()), left = new JPanel(new VerticalLayout());
        private JScrollPane rightSP = new JScrollPane(right), leftSP = new JScrollPane(left);

        MainEditorCustomTabDisplay() {
            String layoutDef = "(ROW (LEAF name=left weight=0.5) (LEAF name=right weight=0.5))";
            MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
            getMultiSplitLayout().setModel(modelRoot);
            getMultiSplitLayout().setDividerSize(1);
            getMultiSplitLayout().setLayoutMode(MultiSplitLayout.NO_MIN_SIZE_LAYOUT);
            right.setOpaque(true);
            right.setBackground(Color.white);
            left.setOpaque(true);
            left.setBackground(Color.white);
            
            add(rightSP, "right");
            add(leftSP, "left");
        }

        private SwingDisplay convertDisplay(Display d) {
            if (d instanceof SwingDisplay) {
                return (SwingDisplay) d;
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(MainEditorCustomTabDisplayFactory.class, "SwingDisplay") + d.getClass().getName());
            }
        }

        public void addToSlot(TabSlot slot, Display content) {
            switch (slot) {
                case LEFT:
                    left.add(convertDisplay(content).asComponent());
                    break;
                case RIGHT:
                    right.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void removeFromSlot(TabSlot slot, Display content) {
            switch (slot) {
                case LEFT:
                    left.remove(convertDisplay(content).asComponent());
                    break;
                case RIGHT:
                    right.remove(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void clearSlot(TabSlot slot) {
            switch (slot) {
                case LEFT:
                    left.removeAll();
                    break;
                case RIGHT:
                    right.removeAll();
                    break;
                default:
                    assert false;
            }
        }

        public void setInSlot(TabSlot slot, Display content) {
            switch (slot) {
                case LEFT:
                    left.removeAll();
                    left.add(convertDisplay(content).asComponent());
                    break;
                case RIGHT:
                    right.removeAll();
                    right.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public JComponent asComponent() {
            return this;
        }
    }

    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(WorkItemTabPresenter.TabDisplay.class)) {
            return displayType.cast(new MainEditorCustomTabDisplay());
        }
        return null;
    }
}
