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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LayerWidget;
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
 */
public class StringMultiValueEditorWidget extends LayerWidget implements RtcEditableAttributeExpression.RtcAttributeExpressionSelectedValueChangeListener {

    private RtcEditableAttributeExpression expression;
    private RtcQueryAttributeValueChecker checker;
    private LayerWidget valuesLayer;

    public StringMultiValueEditorWidget(Scene scene, RtcEditableAttributeExpression expression, RtcQueryAttributeValueChecker checker) {
        super(scene);
        this.expression = expression;
        this.checker = checker;
        valuesLayer = new LayerWidget(scene);
        valuesLayer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 5));
        this.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 2));
        this.addChild(valuesLayer);

        JButton button = new JButton(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/add.gif", false));
        button.addActionListener(new AddButtonActionListener(expression, checker));
        Widget add = new ComponentWidget(scene, button);
        this.addChild(add);
    }

    @Override
    protected void notifyAdded() {
        valuesLayer.removeChildren();

        for (RtcQueryAttributeValue val : expression.getSelectedValues()) {
            StringValueEditorWidget w = new StringValueEditorWidget(getScene(), expression, val, checker);
            valuesLayer.addChild(w);
            w.revalidate();
        }
        valuesLayer.revalidate();
        this.getScene().validate();
        expression.addSelectedValuesChangeListener(this);
        if (expression.getSelectedValues().length == 0) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    //we want to add new value, with empty value
                    try {
                        StringMultiValueEditorWidget.this.expression.addSelectedValue(
                                StringMultiValueEditorWidget.this.checker.getValue(""));
                    } catch (IllegalArgumentException ex) {
                        RtcLogger.getLogger(StringMultiValueEditorWidget.class)
                                .log(Level.WARNING, NbBundle.getMessage(StringMultiValueEditorWidget.class, "UnableToAddValue.error"), ex);
                    }
                }
            });
        }

    }

    @Override
    protected void notifyRemoved() {
        expression.removeSelectedValuesChangeListener(this);
    }

    @Override
    public void selectedValueChanged(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) {
        if (oldValue == null) {
            final StringValueEditorWidget w = new StringValueEditorWidget(this.getScene(), expression, newValue, checker);
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    valuesLayer.addChild(w);
                    w.revalidate();
                    valuesLayer.revalidate();
                    StringMultiValueEditorWidget.this.revalidate();
                    StringMultiValueEditorWidget.this.getScene().validate();
                }
            });

        }
    }
}

class AddButtonActionListener implements ActionListener, Runnable {

    private RtcEditableAttributeExpression expression;
    private RtcQueryAttributeValueChecker checker;

    public AddButtonActionListener(RtcEditableAttributeExpression expression, RtcQueryAttributeValueChecker checker) {
        this.expression = expression;
        this.checker = checker;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(this);
    }

    @Override
    public void run() {
        //we want to add new value, with empty value
        try {
            expression.addSelectedValue(checker.getValue(""));
        } catch (IllegalArgumentException ex) {
            RtcLogger.getLogger(StringMultiValueEditorWidget.class)
                    .log(Level.WARNING, NbBundle.getMessage(StringMultiValueEditorWidget.class, "UnableToAddValue.error"), ex);
        }
    }
}
