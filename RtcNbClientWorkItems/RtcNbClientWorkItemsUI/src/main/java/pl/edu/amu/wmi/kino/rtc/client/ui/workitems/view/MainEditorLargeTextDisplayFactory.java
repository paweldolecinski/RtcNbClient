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
import java.awt.Dimension;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.LargeTextDisplay;

/**
 * 
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
public class MainEditorLargeTextDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType,
            Lookup lookup) {
        if (displayType.equals(LargeTextDisplay.class)) {
            return displayType.cast(new MainEditorLargeTextDisplay());
        }
        return null;
    }

    private static class MainEditorLargeTextDisplay extends JPanel implements
            LargeTextDisplay, SwingDisplay, DocumentListener {

        private static final long serialVersionUID = 107007277215465050L;
        private JLabel label;
        private JEditorPane jText1;
        private JPanel upperPanel;
        private JLabel icon;// TODO: To musi być ikoną
        private final List<InputHandler<String>> inputHandlers = Collections.synchronizedList(new LinkedList<InputHandler<String>>());

        public MainEditorLargeTextDisplay() {
            super(new BorderLayout(0, 5));
            setOpaque(false);
            label = new JLabel();
            label.setOpaque(false);
            jText1 = new JEditorPane();
            JScrollPane editorScrollPane = new JScrollPane(jText1);
            editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            editorScrollPane.setPreferredSize(new Dimension(150, 260));
            editorScrollPane.setMinimumSize(new Dimension(10, 10));
            jText1.setContentType("text/html");
            jText1.setText("");
            jText1.setBackground(Color.WHITE);
            
            icon = new JLabel();
            icon.setOpaque(false);
//            setInfoStatus(Status.OK, "");
            upperPanel = new JPanel(new BorderLayout());
            upperPanel.setOpaque(false);
            upperPanel.add(label,BorderLayout.WEST);
            upperPanel.add(icon,BorderLayout.EAST);
            
            add(editorScrollPane, BorderLayout.CENTER);
            add(upperPanel, BorderLayout.NORTH);

            jText1.getDocument().addDocumentListener(this);
        }

        @Override
        public <T> HandlerRegistration addInputHandler(Input<T> input,
                final InputHandler<T> h) {
            if (input.equals(LargeTextDisplay.TEXT_INPUT)) {
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
            jText1.setText(text);
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

        public void setLabel(String labelText) {
            this.label.setText(labelText);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            notifyHandlers(jText1.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            notifyHandlers(jText1.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            notifyHandlers(jText1.getText());
        }

        private void notifyHandlers(String value) {
            synchronized (inputHandlers) {
                for (InputHandler<String> h : inputHandlers) {
                    h.valueEntered(value);
                }
            }
        }

        public void setId(String id) {
        }
    }
}
