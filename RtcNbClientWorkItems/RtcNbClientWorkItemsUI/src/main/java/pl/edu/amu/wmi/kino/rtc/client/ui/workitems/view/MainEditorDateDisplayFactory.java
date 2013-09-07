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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.ImageUtilities;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.DateDisplay;

/**
 * 
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
public class MainEditorDateDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType,
            Lookup lookup) {
        if (displayType.equals(DateDisplay.class)) {
            return displayType.cast(new MainEditorDateDisplay());
        }
        return null;
    }

    private static class MainEditorDateDisplay extends JPanel implements
            DateDisplay, SwingDisplay {

        private static final long serialVersionUID = 86181918599165050L;
        private JLabel label;
        private JFormattedTextField jTextArea1;
        private final Map<Input<?>, List<InputHandler<?>>> inputHandlers = Collections.synchronizedMap(new HashMap<Input<?>, List<InputHandler<?>>>());
        private JLabel icon;

        public MainEditorDateDisplay() {
            super(new GridBagLayout());
            setOpaque(false);
            icon = new JLabel();
            label = new JLabel();
            icon.setOpaque(false);
            label.setOpaque(false);
            jTextArea1 = new JFormattedTextField(
                    DateFormat.getDateInstance(DateFormat.SHORT));
            jTextArea1.setEditable(true);
            jTextArea1.setColumns(10);

            GridBagConstraints c = new GridBagConstraints();
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            leftPanel.setPreferredSize(new Dimension(175, 20));
            leftPanel.add(label, BorderLayout.WEST);
            leftPanel.add(icon, BorderLayout.EAST);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            add(leftPanel, c);
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.add(jTextArea1, BorderLayout.CENTER);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.5;
            add(rightPanel, c);
            jTextArea1.addPropertyChangeListener("value",
                    new PropertyChangeListener() {

                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            Object source = evt.getSource();
                            if (source == jTextArea1) {
                                Date date = (Date) jTextArea1.getValue();
                                List<InputHandler<?>> list = inputHandlers.get(DateDisplay.DATE_INPUT);
                                for (InputHandler<?> i : list) {
                                    ((InputHandler<Date>) i).valueEntered(date);
                                }
                            }

                        }
                    });

        }

        @Override
        public void setDate(Date date) {
            if (date != null) {
                jTextArea1.setText(DateFormat.getDateInstance().format(date));
            }
        }

        @Override
        public <T> HandlerRegistration addInputHandler(Input<T> source,
                final InputHandler<T> h) {
            if (DateDisplay.DATE_INPUT.equals(source)) {
                if (!inputHandlers.containsKey(source)) {
                    inputHandlers.put(source,
                            Collections.synchronizedList(new LinkedList<InputHandler<?>>()));
                }
                final List<InputHandler<?>> list = inputHandlers.get(source);

                list.add(h);
                return new HandlerRegistration(new Runnable() {

                    @Override
                    public void run() {
                        list.remove(h);
                    }
                });
            } else {
                return null;
            }
        }

        @Override
        public JComponent asComponent() {
            return this;
        }

        @Override
        public void setLabel(String labelText) {
            this.label.setText(labelText);
        }

        @Override
        public void setInfoStatus(Status status, String text) {
            switch (status) {
                case OK:
                    icon.setIcon(ImageUtilities.loadImageIcon(
                            "pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconInfo.png",
                            true));
                    break;
                case WARNING:
                    icon.setIcon(ImageUtilities.loadImageIcon(
                            "pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconWarning.png",
                            false));
                    break;
                case ERROR:
                    icon.setIcon(ImageUtilities.loadImageIcon(
                            "pl/edu/amu/wmi/kino/rtc/client/ui/workitems/presenter/attributes/iconError.png",
                            false));
                    break;
            }

            icon.setToolTipText(text);
        }

        @Override
        public void setId(String id) {
        }
    }
}
