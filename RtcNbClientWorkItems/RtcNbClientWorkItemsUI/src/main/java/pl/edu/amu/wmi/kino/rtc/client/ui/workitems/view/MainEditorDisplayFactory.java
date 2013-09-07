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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.cookies.SaveCookie;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewCloseHandler;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewHeader;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewTopComponentFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler.OptionChooser;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter.WorkItemSlot;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Patryk Żywica
 */
public class MainEditorDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(WorkItemPresenter.WorkItemDisplay.class)) {
            return displayType.cast(new MainEditorDisplay());
        }
        return null;
    }

    private static class MainEditorDisplay implements WorkItemPresenter.WorkItemDisplay, SwingDisplay {

        private static final long serialVersionUID = 34543135453589L;
        private TopComponent tc;
        private MVGroup group;
        private MVHeader header;
        private MVCloseHandler closeHandler;
        private InstanceContent ic = new InstanceContent();
        private SaveCookie saveCookie;
        private List<OptionChooseHandler> optionHandlers = Collections.synchronizedList(new LinkedList<OptionChooseHandler>());
        private String title;
        private boolean modified = false;

        /* package */
        MainEditorDisplay() {
            group = new MVGroup();
            header = new MVHeader();
            closeHandler = new MVCloseHandler();
            tc = KinoMultiViewTopComponentFactory.createTopComponent(
                    new KinoMultiViewGroup[]{group},
                    header,
                    closeHandler,
                    KinoMultiViewTopComponentFactory.KinoMultiViewTopComponentTabPosition.TOP,
                    new AbstractLookup(ic), "usingRationalTeamConcert.EditingWorkItems"); //TODO: monia: helpCtx
        }

        @Override
        public JComponent asComponent() {
            return tc;
        }

        @Override
        public void addToSlot(WorkItemSlot slot, Display content) {
            switch (slot) {
                case HEADER:
                    if (content instanceof SwingDisplay) {
                        header.add(((SwingDisplay) content).asComponent(), BorderLayout.CENTER);
                    } else {
                        throw new IllegalArgumentException(NbBundle.getMessage(MainEditorDisplayFactory.class, "SwingDisplay") + content.getClass().getName());
                    }
                    break;
            }
        }

        @Override
        public void removeFromSlot(WorkItemSlot slot, Display content) {
            throw new UnsupportedOperationException(NbBundle.getMessage(MainEditorDisplayFactory.class, "NotSupported"));
        }

        @Override
        public void clearSlot(WorkItemSlot slot) {
            switch (slot) {
                case HEADER:
                    header.removeAll();
                    break;
            }
        }

        @Override
        public void setInSlot(WorkItemSlot slot, Display content) {
            switch (slot) {
                case HEADER:
                    if (content instanceof SwingDisplay) {
                        header.removeAll();
                        header.add(((SwingDisplay) content).asComponent());
                    } else {
                        throw new IllegalArgumentException(NbBundle.getMessage(MainEditorDisplayFactory.class, "SwingDisplay") + content.getClass().getName());
                    }
                    break;
            }
        }

        @Override
        public void showDialog(String title, String msg, String[] options, OptionChooseHandler handler) {
            DialogDescriptor desc = new DialogDescriptor(
                    new JLabel(msg),
                    title, true,
                    options,
                    options[0],
                    DialogDescriptor.DEFAULT_ALIGN,
                    HelpCtx.DEFAULT_HELP,
                    null);
            desc.setClosingOptions(null);
            Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
            dialog.setVisible(true);
            for (int i = 0; i < options.length; i++) {
                if (desc.getValue().equals(options[i])) {
                    handler.optionChosen(i);
                    return;
                }

            }
            handler.optionChosen(-1);
        }

        @Override
        public void closeDisplay() {
            tc.close();
        }

        @Override
        public void open() {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    tc.open();
                    tc.requestActive();
                }
            });
        }

        @Override
        public void addTab(String title, Display content) {
            if (content instanceof SwingDisplay) {
                MVDescription desc = new MVDescription(title, ((SwingDisplay) content).asComponent());
                group.addDescription(desc);
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(MainEditorDisplayFactory.class, "SwingDisplay") + content.getClass().getName());
            }
        }

        @Override
        public void removeTab(Display content) {
            throw new UnsupportedOperationException(NbBundle.getMessage(MainEditorDisplayFactory.class, "NotSupported"));
        }

        @Override
        public void removeTabs() {
            group.clear();
        }

        public void setTitle(final String title) {
            this.title = title;
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    if (modified) {
                        tc.setHtmlDisplayName("<html><b>" + title + "</b></html>");
                    } else {
                        tc.setHtmlDisplayName(title);
                    }
                }
            });

        }

        public void setModified(boolean modified) {
            this.modified = modified;
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    if (MainEditorDisplayFactory.MainEditorDisplay.this.modified) {
                        if (saveCookie == null) {
                            saveCookie = new SaveCookie() {

                                public void save() throws IOException {
                                    notifyHandlers(SAVED);
                                }
                            };
                        }
                        ic.add(saveCookie);
                        tc.setHtmlDisplayName("<html><b>" + title + "</b></html>");
                    } else {
                        ic.remove(saveCookie);
                        tc.setHtmlDisplayName(title);
                    }
                }
            });
        }

        public HandlerRegistration addOptionHandler(OptionChooser source, final OptionChooseHandler h) {
            if (source.equals(MODIFIED_OPTION)) {
                optionHandlers.add(h);
                return new HandlerRegistration(new Runnable() {

                    public void run() {
                        optionHandlers.remove(h);
                    }
                });
            }
            return null;
        }

        private void notifyHandlers(int option) {
            synchronized (optionHandlers) {
                for (OptionChooseHandler h : optionHandlers) {
                    h.optionChosen(option);
                }
            }
        }

        private class MVGroup extends KinoMultiViewGroup {

            private List<KinoMultiViewDescription> descs = new LinkedList<KinoMultiViewDescription>();

            @Override
            public KinoMultiViewDescription[] getDescriptions() {
                return descs.toArray(new KinoMultiViewDescription[]{});
            }

            void clear() {
                descs.clear();
                fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_REMOVED);
            }

            void addDescription(KinoMultiViewDescription desc) {
                descs.add(desc);
                fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_ADDED);
            }
        }

        private class MVHeader extends KinoMultiViewHeader {

            private static final long serialVersionUID = 68987613452L;

            public MVHeader() {
                super();
                setLayout(new BorderLayout());
            }

            @Override
            public boolean isUpdateToolbarSupported() {
                return false;
            }

            @Override
            public void updateToolbar(JComponent toolbar) {
                throw new UnsupportedOperationException(NbBundle.getMessage(MainEditorDisplayFactory.class, "NotSupported"));
            }
        }

        private class MVCloseHandler implements KinoMultiViewCloseHandler {

            @Override
            public boolean canClose() {
                return true;
            }

            @Override
            public void close() {
            }
        }

        private class MVDescription extends KinoMultiViewDescription {

            private MVElement element;
            private String name;

            MVDescription(String name, JComponent comp) {
                this.element = new MVElement(comp);
                this.name = name;
            }

            @Override
            public KinoMultiViewElement createElement() {
                return element;
            }

            @Override
            public String getDisplayName() {
                return name;
            }
        }

        private class MVElement extends KinoMultiViewElement {

            private JComponent comp;

            MVElement(JComponent comp) {
                this.comp = comp;
            }

            @Override
            protected JComponent createInnerComponent() {
                return comp;
            }

            @Override
            public JComponent getToolbarRepresentation() {
                return new JLabel();
            }
        }
    }
    
}