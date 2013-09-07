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
import org.openide.util.NbBundle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.panels.RtcAddExpressionPanel;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression.RtcExpressionEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpressionFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

/**
 *
 * @author Patryk Żywica
 * @author Michal Wiktorowski
 */
public class TermWidget extends Widget implements RtcEditableExpression.RtcExpressionListener {

    private final RtcEditableTermExpression term;
    private LayerWidget subExpressionsLayer, toolbarLayer, topLayer;
    private LabelWidget fillLayer, operatorDesc;
    private ComboBoxModel comboBoxModel;
    private RtcQuery query;
    JComboBox comboBox;

    public TermWidget(Scene scene,RtcQuery query, final RtcEditableTermExpression term) {
        super(scene);
        this.query=query;
        this.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 10));
        this.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1, Color.decode("#FBFBFF"), Color.decode("#B7CADE")));

        operatorDesc = new LabelWidget(scene, NbBundle.getMessage(TermWidget.class, "TermOperator.name") + ":");
        operatorDesc.setAlignment(LabelWidget.Alignment.LEFT);
        operatorDesc.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        operatorDesc.setForeground(Color.decode("#194C7F"));
        operatorDesc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
//TODO : michal : names and,or have to be localized
        comboBoxModel = new DefaultComboBoxModel(term.getOperator().values());

        topLayer = new LayerWidget(scene);
        topLayer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
        topLayer.setCheckClipping(true);


        subExpressionsLayer = new LayerWidget(scene);
        subExpressionsLayer.setLayout(new GalleryLayout(10));

        subExpressionsLayer.setBorder(BorderFactory.createEmptyBorder(1, 8));

        toolbarLayer = new LayerWidget(scene);
        toolbarLayer.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 4));
        toolbarLayer.setBorder(BorderFactory.createRoundedBorder(10, 10, 1, 1, Color.decode("#E0E8F1"), Color.WHITE));


        comboBox = new JComboBox(comboBoxModel);
        comboBox.setSelectedItem(term.getOperator());
        comboBox.setUI(new RtcComboBoxUI());
        comboBox.setBackground(Color.decode("#E0E8F1"));
        comboBox.setForeground(Color.decode("#194C7F"));
        comboBox.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        comboBox.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        comboBox.setRenderer(new ListCellRenderer() {

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

        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                term.setOperator((RtcEditableTermExpression.RtcTermOperator) comboBox.getSelectedItem());
            }
        });

        JButton removeButton = new JButton(NbBundle.getMessage(TermWidget.class, "Delete.name"));
        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/remove_all.gif"))); // NOI18N
        removeButton.setContentAreaFilled(false);
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                RequestProcessor.getDefault().post(new SubexpressionDeleteRunable(TermWidget.this.term));
            }
        });

        JButton addButton = new JButton(NbBundle.getMessage(TermWidget.class, "AddSubexpressionButton.name"));
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/add.gif"))); // NOI18N
        addButton.setContentAreaFilled(false);

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RtcAddExpressionPanel panel = new RtcAddExpressionPanel(TermWidget.this.query.getQueriesManager().getQueryAttributeManager());
                DialogDescriptor desc = new DialogDescriptor(panel, NbBundle.getMessage(TermWidget.class, "AddSubexpressionDialog.name"));
                desc.setHelpCtx(new HelpCtx(TermWidget.class));
                Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
                dialog.setVisible(true);
                if (desc.getValue().equals(DialogDescriptor.OK_OPTION)) {
                    Node[] selected = panel.getExplorerManager().getSelectedNodes();
                    RequestProcessor.getDefault().post(new SubexpressionAddRunnable(selected, TermWidget.this.term));
                }
            }
        });

        //TODO : bikol : localise this string 
        JButton addGroupButton = new JButton(NbBundle.getMessage(TermWidget.class, "AddGroup.name"));
        addGroupButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/queries/editor/resources/add_group.gif"))); // NOI18N
        addGroupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RequestProcessor.getDefault().post(new GroupAddRunnalbe(TermWidget.this.term));
            }
        });
        addGroupButton.setContentAreaFilled(false);

        fillLayer = new LabelWidget(scene);
        fillLayer.setOpaque(false);

        toolbarLayer.addChild(operatorDesc);
        toolbarLayer.addChild(new ComponentWidget(scene, comboBox));
        toolbarLayer.addChild(fillLayer, 1000);
        toolbarLayer.addChild(new ComponentWidget(scene, addGroupButton));
        toolbarLayer.addChild(new ComponentWidget(scene, addButton));
        toolbarLayer.addChild(new ComponentWidget(scene, removeButton));


        topLayer.addChild(toolbarLayer);
        topLayer.addChild(subExpressionsLayer);

        this.addChild(topLayer);
        this.term = term;
    }

    @Override
    protected void notifyAdded() {
        super.notifyAdded();
        refresh();
        term.addListener(this);
    }

    @Override
    protected void notifyRemoved() {
        super.notifyRemoved();
        term.removeListener(this);
    }

    private void refresh() {
        assert (EventQueue.isDispatchThread());
//        //System.out.println("refresh termo "+hashCode());
        ExpressionWidgetFactory factory = Lookup.getDefault().lookup(ExpressionWidgetFactory.class);
        subExpressionsLayer.removeChildren();
        for (RtcEditableExpression ex : term.getSubExpressions()) {
            //createExpressionWidget dolacza nowego listenera do ex.
            Widget[] ws = factory.createExpressionWidget(ex,query, this.getScene());
            for (Widget w : ws) {
                subExpressionsLayer.addChild(w);
                w.revalidate();
            }

        }
        subExpressionsLayer.revalidate();
        this.revalidate();
        this.getScene().validate();
    }

    @Override
    public void expressionChanged(RtcExpressionEvent e) {
        if (!RtcExpressionEvent.SUBEXPRESSION_CHANGED.equals(e)) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
        }
    }
}

class SubexpressionAddRunnable implements Runnable {

    private Node[] selected;
    private RtcEditableTermExpression term;

    public SubexpressionAddRunnable(Node[] selected, RtcEditableTermExpression term) {
        this.selected = selected;
        this.term = term;
    }

    @Override
    public void run() {
        RtcEditableExpressionFactory factory =
                Lookup.getDefault().lookup(RtcEditableExpressionFactory.class);
        List<RtcEditableExpression> exs = new LinkedList<RtcEditableExpression>();
        for (Node n : selected) {
            RtcQueryAttribute attr = n.getLookup().lookup(RtcQueryAttribute.class);
            if (attr != null) {
                exs.add(factory.createAttributeExpression(attr, term));
            } else {
                throw new IllegalStateException(NbBundle.getMessage(TermWidget.class, "TermWidgetExeption")); //popr
            }
            
        }
        term.addSubExpressions(exs.toArray(new RtcEditableExpression[]{}));
    }
}

class GroupAddRunnalbe implements Runnable {

    private RtcEditableTermExpression parent;

    public GroupAddRunnalbe(RtcEditableTermExpression parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        RtcEditableExpressionFactory factory =
                Lookup.getDefault().lookup(RtcEditableExpressionFactory.class);
        parent.addSubExpression(factory.createTermExpression(parent, RtcEditableTermExpression.RtcTermOperator.AND));
    }
}

class SubexpressionDeleteRunable implements Runnable {

    private RtcEditableExpression term;

    public SubexpressionDeleteRunable(RtcEditableExpression term) {
        this.term = term;
    }

    @Override
    public void run() {
        if (term.getParent() != null) {
            term.getParent().removeSubExpression(term);
        } else {
            if (term instanceof RtcEditableTermExpression) {
                RtcEditableTermExpression t = (RtcEditableTermExpression) term;
                t.removeSubExpressions(t.getSubExpressions());
            }
        }
    }
}
