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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.SmallTextDisplay;

/**
 *
 * @author Patryk Żywica
 */
public class MainEditorSmallTextDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(SmallTextDisplay.class)) {
            return displayType.cast(new MainEditorSmallTextDisplay());
        }
        return null;
    }

    private static class MainEditorSmallTextDisplay extends JPanel implements SmallTextDisplay, SwingDisplay, DocumentListener {

        private static final long serialVersionUID = 4862764037165050L;
        private final List<InputHandler<String>> inputHandlers =
                Collections.synchronizedList(new LinkedList<InputHandler<String>>());
        private JTextField jTextField1;
        private JPanel jPanelWarning;
        private JLabel icon;
        private JLabel label;

        MainEditorSmallTextDisplay() {
            super(new GridBagLayout());
            setOpaque(false);
            jTextField1 = new JTextField();
            icon = new JLabel();
            icon.setOpaque(false);
            label = new JLabel();
            label.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            leftPanel.setPreferredSize(new Dimension(175, 20));
            leftPanel.add(label, BorderLayout.WEST);
            leftPanel.add(icon, BorderLayout.EAST);
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.add(jTextField1,BorderLayout.CENTER);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            add(leftPanel, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.5;
            add(rightPanel, c);

            jTextField1.getDocument().addDocumentListener(this);
        }

        @Override
        public <T> HandlerRegistration addInputHandler(Input<T> input, final InputHandler<T> h) {
            if (input.equals(SmallTextDisplay.TEXT_INPUT)) {
                inputHandlers.add((InputHandler<String>) h);
                return new HandlerRegistration(new Runnable() {

                    @Override
                    public void run() {
                        inputHandlers.remove(h);
                    }
                });
            }
            return null;
        }

        @Override
        public void setText(String text) {
            jTextField1.setText(text);
        }

        @Override
        public void setInfoStatus(Status status, String text) {
            switch (status) {
                case OK:
                    icon.setIcon(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconInfo.png", true));
                    break;
                case WARNING:
                    icon.setIcon(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconWarning.png", false));
                    break;
                case ERROR:
                    icon.setIcon(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconError.png", false));
                    break;
            }

            icon.setToolTipText(text);

        }

        @Override
        public JComponent asComponent() {
            return this;
        }

        @Override
        public void setHint(String hint) {
            icon.setToolTipText(hint);
        }

        @Override
        public void setLabel(String l) {
            label.setText(l);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            notifyHandlers(jTextField1.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            notifyHandlers(jTextField1.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            notifyHandlers(jTextField1.getText());
        }

        private void notifyHandlers(String value) {
            synchronized (inputHandlers) {
                for (InputHandler<String> h : inputHandlers) {
                    h.valueEntered(value);
                }
            }
        }

        @Override
        public void setId(String id) {
        }
    }
}
