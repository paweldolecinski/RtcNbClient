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
import javax.swing.BorderFactory;
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
public class MainEditorLinksTabDisplayFactory implements DisplayFactory {

    private static class MainEditorLinksTabDisplay extends JXMultiSplitPane
            implements WorkItemTabPresenter.TabDisplay, SwingDisplay {

        private static final long serialVersionUID = 24354355L;
        private JPanel attachments = new JPanel(new VerticalLayout(3)), subscribers = new JPanel(new VerticalLayout(3));
        private JPanel links = new JPanel(new VerticalLayout(3));
        private JScrollPane attachmentsSP = new JScrollPane(attachments), subscribersSP = new JScrollPane(subscribers);
        private JScrollPane linksSP = new JScrollPane(links);

        MainEditorLinksTabDisplay() {
            String layoutDef = "(COLUMN (ROW weight=0.5 (LEAF name=attachments weight=0.6) (LEAF name=subscribers weight=0.4)) (LEAF weight=0.5 name=links))";
            MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
            getMultiSplitLayout().setModel(modelRoot);
            getMultiSplitLayout().setDividerSize(1);
            getMultiSplitLayout().setLayoutMode(MultiSplitLayout.NO_MIN_SIZE_LAYOUT);
            attachments.setOpaque(true);
            attachments.setBackground(Color.white);
            attachments.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            subscribers.setOpaque(true);
            subscribers.setBackground(Color.white);
            links.setOpaque(true);
            links.setBackground(Color.white);
            add(attachmentsSP, "attachments");
            add(subscribersSP, "subscribers");
            add(linksSP, "links");
        }

        private SwingDisplay convertDisplay(Display d) {
            if (d instanceof SwingDisplay) {
                return (SwingDisplay) d;
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(MainEditorLinksTabDisplayFactory.class, "SwingDisplay") + d.getClass().getName());
            }
        }

        public void addToSlot(TabSlot slot, Display content) {
            switch (slot) {
                case ATTACHMENTS:
                    attachments.add(convertDisplay(content).asComponent());
                    break;
                case SUBSCRIBERS:
                    subscribers.add(convertDisplay(content).asComponent());
                    break;
                case LINKS:
                    links.add(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void removeFromSlot(TabSlot slot, Display content) {
            switch (slot) {
                case ATTACHMENTS:
                    attachments.remove(convertDisplay(content).asComponent());
                    break;
                case SUBSCRIBERS:
                    subscribers.remove(convertDisplay(content).asComponent());
                    break;
                case LINKS:
                    links.remove(convertDisplay(content).asComponent());
                    break;
                default:
                    assert false;
            }
        }

        public void clearSlot(TabSlot slot) {
            switch (slot) {
                case ATTACHMENTS:
                    attachments.removeAll();
                    break;
                case SUBSCRIBERS:
                    subscribers.removeAll();
                    break;
                case LINKS:
                    links.removeAll();
                    break;
                default:
                    assert false;
            }
        }

        public void setInSlot(TabSlot slot, Display content) {
            switch (slot) {
                case ATTACHMENTS:
                    attachments.removeAll();
                    attachments.add(convertDisplay(content).asComponent());
                    break;
                case SUBSCRIBERS:
                    subscribers.removeAll();
                    subscribers.add(convertDisplay(content).asComponent());
                    break;
                case LINKS:
                    links.removeAll();
                    links.add(convertDisplay(content).asComponent());
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
            return displayType.cast(new MainEditorLinksTabDisplay());
        }
        return null;
    }
}
