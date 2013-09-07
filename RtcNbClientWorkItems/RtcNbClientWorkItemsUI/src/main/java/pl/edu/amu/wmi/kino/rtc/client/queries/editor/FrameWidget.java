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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcAttributeOperation;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;

/**
 *
 * @author Patryk Żywica
 * @author Michal Wiktorowski
 */
public class FrameWidget extends Widget {

    private RtcEditableAttributeExpression expression;
    
    private Widget detailsWidget, toolbar, leftToolbar, rightToolbar, footer, lowerToolbar;
    private LabelWidget emptyFillLayer, displayName;
    private ArrayList<Widget> rightToolbarWidgets = new ArrayList<Widget>();
    private WidgetAction mouseHover = ActionFactory.createHoverAction(new MouseController());

    private ComponentWidget attributeComboBoxComponentWidget;
    private JToggleButton deleteBtn;
    private DefaultComboBoxModel attributeComboBoxModel;
    private JComboBox attributeComboBox;
    
    private boolean expanded = true, mouseNotOver = true, animatedResizing = true;

    public FrameWidget(Scene scene, RtcEditableAttributeExpression expr) {
        super(scene);
        this.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
        this.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1,
                Color.WHITE, Color.decode("#B7CADE")));
        this.expression = expr;

        displayName = new LabelWidget(scene, (expression).getQueryAttribute().getDisplayName() + ":");
        displayName.setAlignment(LabelWidget.Alignment.LEFT);
        displayName.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        displayName.setForeground(Color.decode("#194C7F"));
        displayName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

        deleteBtn = new JToggleButton(
                new javax.swing.ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/remove.gif"))); // NOI18N
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestProcessor.getDefault().post(new AtributeDeleteRunable(expression));
            }
        });

        // <editor-fold defaultstate="collapsed" desc="toolbar">
        leftToolbar = new Widget(scene);
        leftToolbar.setLayout(LayoutFactory.createHorizontalFlowLayout(
                LayoutFactory.SerialAlignment.CENTER, 5));

        rightToolbar = new Widget(scene);
        rightToolbar.setLayout(LayoutFactory.createHorizontalFlowLayout(
                LayoutFactory.SerialAlignment.CENTER, 5));

        toolbar = new Widget(scene);
        toolbar.setLayout(LayoutFactory.createHorizontalFlowLayout(
                LayoutFactory.SerialAlignment.CENTER, 5));
        toolbar.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1,
                Color.decode("#E0E8F1"), Color.white));

        lowerToolbar = new Widget(scene);
        lowerToolbar.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 5));
        lowerToolbar.setBorder(BorderFactory.createRoundedBorder(1, 1, 1, 1,
                Color.decode("#EBF5F6"), Color.white));


        emptyFillLayer = new LabelWidget(scene);
        emptyFillLayer.setOpaque(false);
        toolbar.addChild(leftToolbar);
        toolbar.addChild(emptyFillLayer, 1000);
        toolbar.addChild(rightToolbar);
        toolbar.getActions().addAction(new MousOverAction());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="attributeComboBox">
        attributeComboBoxModel = new DefaultComboBoxModel();
        attributeComboBox = new JComboBox(attributeComboBoxModel);
        for (RtcAttributeOperation attributeOperation : expression.getQueryAttribute().getAttributeOperations()) {
            attributeComboBoxModel.addElement(attributeOperation.getDisplayName());
        }
        attributeComboBox.setSelectedItem(expression.getSelectedAttributeOperation().getDisplayName());

        attributeComboBox.setBackground(Color.decode("#E0E8F1"));
        attributeComboBox.setForeground(Color.decode("#194C7F"));
        attributeComboBox.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        attributeComboBox.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        attributeComboBox.setUI(new RtcComboBoxUI());
        attributeComboBox.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final JLabel renderer = new JLabel(value.toString());
                if (isSelected && cellHasFocus) {
                    renderer.setOpaque(true);
                    renderer.setBackground(Color.decode("#E6F0F1"));
                }
                if (isSelected && !cellHasFocus) {
                    renderer.setOpaque(true);
                    renderer.setBackground(Color.decode("#DCE6F1"));
                }
                if (!isSelected) {
                    renderer.setOpaque(true);
                    renderer.setBackground(Color.decode("#E6F0F1"));
                }
                return renderer;
            }
        });


        attributeComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RequestProcessor.getDefault().post(
                        new OperationChangeImpl(expression,
                        expression.getQueryAttribute().
                        getAttributeOperations()[attributeComboBox.getSelectedIndex()]));
            }
        });
        attributeComboBoxComponentWidget = new ComponentWidget(scene, attributeComboBox);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Variables">
                for (final RtcQueryAttributeVariable variable : expression.getQueryAttribute().getAttributeVariables()) {
                    final JCheckBox cb = new JCheckBox(variable.getDisplayName());

                    for (RtcQueryAttributeVariable vv : expression.getSelectedVariables()) {
                        if (variable.getDisplayName().equals(vv.getDisplayName())) {
                            cb.setSelected(true);
                        }
                    }
                    cb.setBorderPainted(false);
                    cb.setContentAreaFilled(false);
                    cb.setForeground(Color.decode("#194C7F"));
                    cb.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!cb.isSelected()) {
                                RequestProcessor.getDefault().post(new VariableSetSelectedImpl(expression, variable));
                            } else {
                                RequestProcessor.getDefault().post(new VariableRemoveSelectedImpl(expression, variable));

                            }
                        }
                    });
                    ComponentWidget c = new ComponentWidget(scene, cb);
                    addToLowerToolbar(c);
                }
                // </editor-fold>


        detailsWidget = new Widget(scene);
        detailsWidget.setLayout(LayoutFactory.createOverlayLayout());

        footer = new Widget(scene);
        footer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.RIGHT_BOTTOM, 1));
        footer.setBorder(BorderFactory.createRoundedBorder(10, 10, 4,4, Color.decode("#F2F8F9"), Color.white));

        addToTolbar(displayName, true);
        addToTolbar(attributeComboBoxComponentWidget, true);
        addToTolbar(new ComponentWidget(scene, deleteBtn), false);
      
        this.addChild(toolbar);
        this.addChild(lowerToolbar);
        this.addChild(detailsWidget);
        this.addChild(footer);



        this.expand();
        this.revalidate();
        this.getActions().addAction(mouseHover);
        this.getScene().getActions().addAction(mouseHover);
        this.getScene().revalidate();
    }

    @Override
    protected void notifyAdded() {
        super.notifyAdded();
        this.expand();
    }

    private String traceName(RtcQueryAttribute a, String tmp) {
        tmp = a.getDisplayName();
        if (a.getParent() != null) {
            tmp = traceName(a.getParent(), tmp) + ">" + tmp;
        }
        return tmp;
    }

    public void addToTolbar(Widget widget, Boolean Left) {
        if (Left) {
            leftToolbar.addChild(widget);
            leftToolbar.revalidate();
        } else {
            rightToolbarWidgets.addAll(rightToolbar.getChildren());
            rightToolbar.removeChildren();
            rightToolbar.addChild(widget);
            rightToolbar.addChildren(rightToolbarWidgets);
            for (Widget w : rightToolbarWidgets) {
                w.revalidate();
            }
            rightToolbar.revalidate();
        }
        widget.revalidate();
        this.revalidate();
    }

    public void addToDescription(Widget widget) {

        detailsWidget.addChild(widget);
        detailsWidget.setCheckClipping(true);
        widget.revalidate();
        detailsWidget.revalidate();
        this.revalidate();
    }

    public void addToFooter(Widget widget) {
        footer.addChild(widget);
        footer.revalidate();
        footer.getScene().revalidate();
        footer.setCheckClipping(true);
    }

    public void addToLowerToolbar(Widget widget) {
        lowerToolbar.addChild(widget);
        lowerToolbar.revalidate();
        lowerToolbar.getScene().revalidate();
//        lowerToolbar.setCheckClipping(true);
    }

    private void collapse() {
        if (!expanded) {
            return;
        }
        expanded = false;
        if (!animatedResizing) {
            detailsWidget.setPreferredBounds(new Rectangle());
        } else {
            getScene().getSceneAnimator().animatePreferredBounds(detailsWidget, new Rectangle());
        }
        changeComponentsVisiblity(detailsWidget);
    }

    private void expand() {
        if (expanded) {
            return;
        }
        expanded = true;
        if (!animatedResizing) {
            detailsWidget.setPreferredBounds(null);
        } else {
            getScene().getSceneAnimator().animatePreferredBounds(detailsWidget, null);
        }
        changeComponentsVisiblity(detailsWidget);
    }

    private void changeComponentsVisiblity(Widget widget) {
        for (Widget w : widget.getChildren()) {
            if (w instanceof ComponentWidget) {
                ((ComponentWidget) w).setComponentVisible(!((ComponentWidget) w).isComponentVisible());
            } else if (w.getChildren() == null) {
                break;
            } else {
                changeComponentsVisiblity(w);
            }
        }
        widget.getScene().validate();

    }

    public void expandHandle() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
        displayName.getScene().revalidate();
    }

    void mouseOver() {
        if (!mouseNotOver) {
            return;
        }
        mouseNotOver = false;
        this.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1,
                Color.WHITE, Color.decode("#7184DE")));
    }

    void mouseOff() {
        if (mouseNotOver) {
            return;
        }
        mouseNotOver = true;
        this.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1,
                Color.WHITE, Color.decode("#B7CADE")));

    }

    private class MousOverAction extends WidgetAction.Adapter {

        @Override
        public State mouseReleased(Widget widget, WidgetMouseEvent event) {
            if (event.isControlDown()) {
                ((FrameWidget) widget.getParentWidget()).expandHandle();
            }
            return State.CONSUMED;
        }
    }

    private static class OperationChangeImpl implements Runnable {

        private RtcEditableAttributeExpression expression;
        private RtcAttributeOperation op;

        public OperationChangeImpl(RtcEditableAttributeExpression expression, RtcAttributeOperation op) {
            this.expression = expression;
            this.op = op;
        }

        @Override
        public void run() {
            expression.setSelectedAttributeOperation(op);
        }
    }

    private static class VariableSetSelectedImpl implements Runnable {

        private RtcEditableAttributeExpression expression;
        private RtcQueryAttributeVariable variable;

        public VariableSetSelectedImpl(RtcEditableAttributeExpression expression, RtcQueryAttributeVariable variable) {
            this.expression = expression;
            this.variable = variable;
        }

        @Override
        public void run() {
            expression.addSelectedVariable(variable);
        }
    }
    private static class VariableRemoveSelectedImpl implements Runnable {

        private RtcEditableAttributeExpression expression;
        private RtcQueryAttributeVariable variable;

        public VariableRemoveSelectedImpl(RtcEditableAttributeExpression expression, RtcQueryAttributeVariable variable) {
            this.expression = expression;
            this.variable = variable;
        }

        @Override
        public void run() {
            expression.removeSelectedVariable(variable);
        }
    }
}

class AtributeDeleteRunable implements Runnable {

    private RtcEditableExpression term;

    public AtributeDeleteRunable(RtcEditableExpression term) {
        this.term = term;
    }

    @Override
    public void run() {
        if (term.getParent() != null) {
            term.getParent().removeSubExpression(term);
        } else {
            assert (false);
        }

    }
}

