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
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.openide.util.NbBundle;
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

/**
 * More info about how to use JXMultiSplitPane at 
 * http://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html#resources
 * @author Patryk Żywica
 */
public class MainEditorOverviewTabDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(WorkItemTabPresenter.TabDisplay.class)) {
            return displayType.cast(new MainEditorOverviewTabDisplay());
        }
        return null;
    }

    private static class MainEditorOverviewTabDisplay extends JXMultiSplitPane implements WorkItemTabPresenter.TabDisplay, SwingDisplay {

        private static final long serialVersionUID = 2324354355L;
        private JPanel details = new JPanel(new VerticalLayout(3));
        private JPanel quickinfo = new JPanel(new VerticalLayout(3));
        private JPanel discussion = new JPanel(new VerticalLayout(3));
        private JPanel description = new JPanel(new VerticalLayout(3));
        private JScrollPane detailsSP = new JScrollPane(details);
        private JScrollPane quickinfoSP = new JScrollPane(quickinfo);
        private JScrollPane descriptionSP = new JScrollPane(description);
        private JScrollPane discussionSP = new JScrollPane(discussion);

        MainEditorOverviewTabDisplay() {
            String layoutDef = "(ROW (COLUMN weight=0.4 (LEAF name=details weight=1.0) (LEAF name=quickinfo weight=0.0)) (COLUMN weight=0.6 (LEAF name=description weight=1.0) (LEAF name=discussion weight=0.0)))";
            MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
            getMultiSplitLayout().setModel(modelRoot);
            getMultiSplitLayout().setDividerSize(1);
            getMultiSplitLayout().setLayoutMode(MultiSplitLayout.NO_MIN_SIZE_LAYOUT);
            quickinfo.setOpaque(true);
            quickinfo.setBackground(Color.WHITE);
            details.setOpaque(true);
            details.setBackground(Color.WHITE);
            details.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            discussion.setOpaque(true);
            discussion.setBackground(Color.WHITE);
            description.setOpaque(true);
            description.setBackground(Color.WHITE);
            description.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            add(detailsSP, "details");
            add(quickinfoSP, "quickinfo");
            add(descriptionSP, "description");
            add(discussionSP, "discussion");
        }

        private SwingDisplay convertDisplay(Display d) {
            if (d instanceof SwingDisplay) {
                return (SwingDisplay) d;
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(MainEditorOverviewTabDisplayFactory.class, "SwingDisplay") + d.getClass().getName()); //TODO bundle?
            }
        }

        @Override
        public void addToSlot(TabSlot slot, Display content) {
            switch (slot) {
                case DETAILS:
                    details.add(convertDisplay(content).asComponent());
                    break;
                case QUICKINFO:
                    quickinfo.add(convertDisplay(content).asComponent());
                    break;
                case DESCRIPTION:
                    description.add(convertDisplay(content).asComponent());
                    break;
                case DISCUSSION:
                    discussion.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        @Override
        public void removeFromSlot(TabSlot slot, Display content) {
            switch (slot) {
                case DETAILS:
                    details.remove(convertDisplay(content).asComponent());
                    break;
                case QUICKINFO:
                    quickinfo.remove(convertDisplay(content).asComponent());
                    break;
                case DESCRIPTION:
                    description.remove(convertDisplay(content).asComponent());
                    break;
                case DISCUSSION:
                    discussion.remove(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        @Override
        public void clearSlot(TabSlot slot) {
            switch (slot) {
                case DETAILS:
                    details.removeAll();
                    break;
                case QUICKINFO:
                    quickinfo.removeAll();
                    break;
                case DESCRIPTION:
                    description.removeAll();
                    break;
                case DISCUSSION:
                    discussion.removeAll();
                    break;
                default:
                    assert false;
            }
        }

        @Override
        public void setInSlot(TabSlot slot, Display content) {
            switch (slot) {
                case DETAILS:
                    details.removeAll();
                    details.add(convertDisplay(content).asComponent());
                    break;
                case QUICKINFO:
                    quickinfo.removeAll();
                    quickinfo.add(convertDisplay(content).asComponent());
                    break;
                case DESCRIPTION:
                    description.removeAll();
                    description.add(convertDisplay(content).asComponent());
                    break;
                case DISCUSSION:
                    discussion.removeAll();
                    discussion.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        @Override
        public JComponent asComponent() {
            return this;
        }
    }
}
