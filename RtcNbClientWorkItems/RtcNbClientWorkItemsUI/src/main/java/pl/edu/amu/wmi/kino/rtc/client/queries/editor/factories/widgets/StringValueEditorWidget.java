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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.factories.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;

/**
 *
 * @author Patryk Żywica
 *
 * TODO: Patryk , set some kind of group to provide better response for TAB key
 */

public class StringValueEditorWidget extends Widget implements DocumentListener, RtcEditableAttributeExpression.RtcAttributeExpressionSelectedValueChangeListener {

    private RtcEditableAttributeExpression expression;
    /**
     * this field covers value that is actually stored in expression
     */
    private RtcQueryAttributeValue currentValue;
    /**
     * this field covers string that is actually stored in expression. it may be different
     * from this shown in textField, but it have the same value.
     */
    private String currentText;
    private RtcQueryAttributeValueChecker checker;
    private JTextField textField;
    private boolean updateSynchronization = false;

    public StringValueEditorWidget(Scene scene, RtcEditableAttributeExpression expression, RtcQueryAttributeValue value, RtcQueryAttributeValueChecker checker) {
        super(scene);
        this.expression = expression;
        this.currentValue = value;
        try {
            this.currentText = checker.getRepresentation(currentValue);
        } catch (IllegalArgumentException ex) {
            this.currentText = NbBundle.getMessage(StringValueEditorWidget.class, "UnresolvedValue.name");
        }

        this.checker = checker;
        setLayout(LayoutFactory.createHorizontalFlowLayout());

        textField = new JTextField(currentText);
        textField.setColumns(25);
        addChild(new ComponentWidget(scene, textField));

        JButton removeButton = new JButton(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/delete10.gif", false));
        removeButton.setPreferredSize(new Dimension(20, 20));
        removeButton.setContentAreaFilled(false);
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        StringValueEditorWidget.this.expression.removeSelectedValue(currentValue);
                    }
                });
            }
        });
        this.addChild(new ComponentWidget(scene, removeButton));
        expression.addSelectedValuesChangeListener(this);
    }

    @Override
    protected void notifyAdded() {
        try {
            currentText = checker.getRepresentation(currentValue);
        } catch (IllegalArgumentException ex) {
            this.currentText = NbBundle.getMessage(StringValueEditorWidget.class, "UnresolvedValue.name");
        }
        if (!textField.getText().equals(currentText)) {
            textField.setText(currentText);
        }
        textField.getDocument().addDocumentListener(this);
    }

    @Override
    protected void notifyRemoved() {
        textField.getDocument().removeDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (updateSynchronization != true) {
            textFieldUpdate(e);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (updateSynchronization != true) {
            textFieldUpdate(e);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
//
    }

    private void textFieldUpdate(DocumentEvent e) {
        synchronized (this) {
            String newName;
            try {
                newName = e.getDocument().getText(0, e.getDocument().getLength());
                RtcQueryAttributeValue v = checker.getValue(newName);
                newName = checker.getRepresentation(v);
                if (!newName.equals(currentText)) {
                    if (!textField.getBackground().equals(Color.white)) {
                        textField.setBackground(Color.white);
                    }
                    try {
                        //TODO : for future : do not call this method on AWT event thread
                        expression.changeSelectedValue(currentValue, v);
                        currentText = newName;
                    } catch (IllegalArgumentException ex) {
                        RtcLogger.getLogger(StringValueEditorWidget.class)
                                .log(Level.WARNING, ex.getLocalizedMessage(), ex);
                        textField.setBackground(Color.pink);
                    }
                } else {
                    if (!textField.getBackground().equals(Color.white)) {
                        textField.setBackground(Color.white);
                    }
                }
            } catch (BadLocationException ex) {
                RtcLogger.getLogger(StringValueEditorWidget.class)
                        .log(Level.WARNING, ex.getLocalizedMessage(), ex);
            } catch (IllegalArgumentException ex) {
                textField.setBackground(Color.pink);
            }
        }
    }

    @Override
    public void selectedValueChanged(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) {
        synchronized (this) {
            if (currentValue.equals(oldValue)) {
                if (newValue != null) {
                    currentValue = newValue;
                    String tmp;
                    try {
                        tmp = checker.getRepresentation(newValue);
                    } catch (IllegalArgumentException ex) {
                        tmp = NbBundle.getMessage(StringValueEditorWidget.class, "UnresolvedValue.name");
                    }
                    if (!currentText.equals(tmp)) {
                        currentText = tmp;
                        EventQueue.invokeLater(new NameChanger(tmp));
                    }
                } else {
                    Widget p = this.getParentWidget();
                    p.removeChild(this);
                    p.revalidate();
                    this.getScene().validate();
                }
            }
        }
    }

    private class NameChanger implements Runnable {

        private String newName;

        public NameChanger(String newName) {
            this.newName = newName;
        }

        @Override
        public void run() {
            updateSynchronization = true;
            textField.setText(newName);
            updateSynchronization = false;
        }
    }
}
