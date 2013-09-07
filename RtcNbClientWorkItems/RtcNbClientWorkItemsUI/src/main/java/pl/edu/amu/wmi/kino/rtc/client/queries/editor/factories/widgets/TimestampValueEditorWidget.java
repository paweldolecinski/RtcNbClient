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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXDatePicker;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeTimestampValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeTimestampValueChecker.Unit;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 *
 * @author Patryk Żywica
 * @author Michal Wiktorowski
 */
public class TimestampValueEditorWidget extends Widget {
    //TODO : for future :  add listener on selected values in expression

    private LayerWidget durationDateLayer, radioButtonLayer, dateCalendarLayer;
    private JTextField durationTimeTextBox;
    private JComboBox durationPropertyComboBox;
    private JRadioButton calendarWidgetTrigger, durationWidgetTrigger;
    private ComponentWidget timePropertyComboBoxComponentWidget,
            timeTextComponentWidget,
            calendarInplaceEditorComponentWidget;
    private JXDatePicker picker;
    private RtcEditableAttributeExpression expression;
    private RtcQueryAttributeValue value;
    private RtcQueryAttributeVariable variable;
    private RtcQueryAttributeTimestampValueChecker ch;
    private ActionListener calendarListener;
    private RtcQueryAttributeTimestampValueChecker.Unit unit=Unit.values()[0];
    private int time = 0;

    public TimestampValueEditorWidget(RtcEditableAttributeExpression expression, RtcQueryAttributeValue value, Scene scene) {
        super(scene);
        this.expression = expression;
        this.value = value;
        init();
    }

    public TimestampValueEditorWidget(RtcEditableAttributeExpression expression, RtcQueryAttributeVariable variable, Scene scene) {
        super(scene);
        this.expression = expression;
        this.variable = variable;
        init();
    }

    private void init() {
        ch = expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributeTimestampValueChecker.class);
        picker = new JXDatePicker();
        calendarListener = new ActionListererImpl(picker, ch, value, expression);

        picker.setPreferredSize(new Dimension(180,20));
        
        this.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 10));

/////////////////////////////////////////
////global part

        calendarWidgetTrigger = new JRadioButton(NbBundle.getMessage(TimestampValueEditorWidget.class, "SpecificDate.name"));
        durationWidgetTrigger = new JRadioButton(NbBundle.getMessage(TimestampValueEditorWidget.class, "RelativeDate.name"));

        calendarWidgetTrigger.setContentAreaFilled(false);
        durationWidgetTrigger.setContentAreaFilled(false);

        ButtonGroup gr = new ButtonGroup();
        gr.add(calendarWidgetTrigger);
        gr.add(durationWidgetTrigger);
        radioButtonLayer = new LayerWidget(getScene());
        radioButtonLayer.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));
        radioButtonLayer.addChild(new ComponentWidget(getScene(), calendarWidgetTrigger));
        radioButtonLayer.addChild(new ComponentWidget(getScene(), durationWidgetTrigger));

        calendarWidgetTrigger.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showCalendarWidget();
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        if (variable != null) {
                            expression.removeSelectedVariable(variable);
                        }
                        if (value != null) {
                            expression.addSelectedValue(value);
                        }
                    }
                });
            }
        });
        durationWidgetTrigger.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showDurationWidget();
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        if (variable != null) {
                            expression.addSelectedVariable(variable);
                        }
                        if (value != null) {
                            expression.removeSelectedValue(value);
                        }
                    }
                });
            }
        });


////relative time part
        DefaultComboBoxModel cModel = new DefaultComboBoxModel(RtcQueryAttributeTimestampValueChecker.Unit.values());


        durationTimeTextBox = new JTextField();
        durationPropertyComboBox = new JComboBox(cModel);


        if (variable != null) {
            Pair<Integer, RtcQueryAttributeTimestampValueChecker.Unit> tmp = ch.getRepresentation(variable);
            durationPropertyComboBox.setSelectedItem(tmp.getSecond());
            durationTimeTextBox.setText(tmp.getFirst().toString());
            time = tmp.getFirst();
            unit = tmp.getSecond();
        }




        durationTimeTextBox.setPreferredSize(new Dimension(40, 20));
        durationTimeTextBox.setMaximumSize(new Dimension(60, 20));
        durationTimeTextBox.getDocument().addDocumentListener(new DocumentListener() {

            private Color prev;

            @Override
            public void insertUpdate(DocumentEvent e) {
                commit();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                commit();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                commit();
            }

            private void commit() {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            time = Integer.parseInt(durationTimeTextBox.getText());
                            durationTimeTextBox.setBackground(prev);
                            expression.removeSelectedVariable(variable);
                            variable = ch.getVariable(time, unit);
                            expression.addSelectedVariable(variable);
                        } catch (NumberFormatException ex) {
                            prev = durationTimeTextBox.getBackground();
                            durationTimeTextBox.setBackground(Color.pink);
                        }
                    }
                });

            }
        });
        durationPropertyComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        if (!durationPropertyComboBox.getSelectedItem().equals(unit)) {
                            unit = (Unit) durationPropertyComboBox.getSelectedItem();
                            if (variable != null) {
                                expression.removeSelectedVariable(variable);
                            }
                            variable = ch.getVariable(time, unit);
                            expression.addSelectedVariable(variable);
                        }
                    }
                });

            }
        });
        timeTextComponentWidget = new ComponentWidget(getScene(), durationTimeTextBox);


        timePropertyComboBoxComponentWidget = new ComponentWidget(getScene(), durationPropertyComboBox);
        durationDateLayer = new LayerWidget(getScene());
        durationDateLayer.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));
        durationDateLayer.addChild(timeTextComponentWidget);
        durationDateLayer.addChild(timePropertyComboBoxComponentWidget);

////calendar time part
        dateCalendarLayer = new LayerWidget(getScene());
        dateCalendarLayer.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));

        
        try {
            Date sel = new Date(ch.getTimestampRepresentation(value).getTime());
            picker.setDate(sel);
        } catch (Exception ex) {
            //
        }
        picker.getEditor().addFocusListener(new FocusListenerImpl(picker));
        picker.addActionListener(calendarListener);

        calendarInplaceEditorComponentWidget = new ComponentWidget(getScene(), picker);
        dateCalendarLayer.addChild(calendarInplaceEditorComponentWidget);
////////////////////////////////////////



        if (variable != null) {
            showDurationWidget();
            durationWidgetTrigger.setSelected(true);
        } else {
            showCalendarWidget();
            calendarWidgetTrigger.setSelected(true);
        }

        this.addChild(durationDateLayer);
        this.addChild(dateCalendarLayer);
        this.addChild(radioButtonLayer);
        this.setBorder(BorderFactory.createEmptyBorder(5));
    }

//    @Override
//    protected void notifyRemoved() {
//        this.removeChild(durationDateLayer);
//        this.removeChild(dateCalendarLayer);
//        this.removeChild(radioButtonLayer);
//    }

    private void showCalendarWidget() {
        durationTimeTextBox.setVisible(false);
        durationPropertyComboBox.setVisible(false);
        durationDateLayer.setVisible(false);

        dateCalendarLayer.setVisible(true);
        calendarInplaceEditorComponentWidget.setComponentVisible(true);

        durationDateLayer.getScene().validate();

    }

    private void showDurationWidget() {
        durationTimeTextBox.setVisible(true);
        durationPropertyComboBox.setVisible(true);
        durationDateLayer.setVisible(true);

        dateCalendarLayer.setVisible(false);
        calendarInplaceEditorComponentWidget.setComponentVisible(false);

        durationDateLayer.getScene().validate();

    }

    private static class FocusListenerImpl implements FocusListener {

        private JXDatePicker picker;

        public FocusListenerImpl(JXDatePicker picker) {
            this.picker = picker;
        }

        private void commit(FocusEvent e) {
            try {
                picker.commitEdit();
            } catch (ParseException ex) {
//                Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(TimestampValueEditorWidget.class)
                        .log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            commit(e);
        }
    }

    private static class ActionListererImpl implements ActionListener {

        private RtcQueryAttributeTimestampValueChecker ch;
        private RtcQueryAttributeValue val;
        private RtcEditableAttributeExpression ex;
        private JXDatePicker picker;

        public ActionListererImpl(JXDatePicker picker, RtcQueryAttributeTimestampValueChecker ch, RtcQueryAttributeValue val, RtcEditableAttributeExpression ex) {
            this.ch = ch;
            this.val = val;
            this.ex = ex;
            this.picker = picker;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RequestProcessor.getDefault().post(new Runnable() {

                public void run() {
                    RtcQueryAttributeValue oldVal = val;
                    Date date = picker.getDate();
                    if (date != null) {
                        val = ch.getValue(new Timestamp(date.getTime()));
                        if (oldVal != null) {
                            ex.changeSelectedValue(oldVal, val);
                        } else {
                            ex.addSelectedValue(val);
                        }
                    }
                }
            });
        }
    }
}
