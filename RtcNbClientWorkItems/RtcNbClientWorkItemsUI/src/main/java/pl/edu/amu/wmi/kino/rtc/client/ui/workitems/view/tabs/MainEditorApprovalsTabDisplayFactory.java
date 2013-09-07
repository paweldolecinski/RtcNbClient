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

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
public class MainEditorApprovalsTabDisplayFactory implements DisplayFactory {

    private static class MainEditorApprovalsTabDisplay extends JPanel
            implements WorkItemTabPresenter.TabDisplay, SwingDisplay {

        private static final long serialVersionUID = 2324354354355L;
        private JPanel approvals = new JPanel(new VerticalLayout());
        private JScrollPane approvalsSP = new JScrollPane(approvals);

        MainEditorApprovalsTabDisplay() {
            super(new BorderLayout(0, 0));
            approvals.setOpaque(true);
            approvals.setBackground(Color.white);
            add(approvalsSP, BorderLayout.CENTER);
        }

        private SwingDisplay convertDisplay(Display d) {
            if (d instanceof SwingDisplay) {
                return (SwingDisplay) d;
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(MainEditorApprovalsTabDisplayFactory.class, "SwingDisplay") + d.getClass().getName()); // TODO nbbundle?
            }
        }

        public void addToSlot(TabSlot slot, Display content) {
            switch (slot) {
                case NONE_SECTION:
                    approvals.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void removeFromSlot(TabSlot slot, Display content) {
            switch (slot) {
                case NONE_SECTION:
                    approvals.remove(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void clearSlot(TabSlot slot) {
            switch (slot) {
                case NONE_SECTION:
                    approvals.removeAll();
                    break;
                default:
                    assert false;
            }
        }

        public void setInSlot(TabSlot slot, Display content) {
            switch (slot) {
                case NONE_SECTION:
                    approvals.removeAll();
                    approvals.add(convertDisplay(content).asComponent());
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
            return displayType.cast(new MainEditorApprovalsTabDisplay());
        }
        return null;
    }
}
