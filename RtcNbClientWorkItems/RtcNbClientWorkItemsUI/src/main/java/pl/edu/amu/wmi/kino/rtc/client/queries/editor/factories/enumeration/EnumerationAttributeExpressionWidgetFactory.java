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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.factories.enumeration;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.ScrollPaneConstants;

import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.queries.editor.api.AbstractAttributeExpressionWidgetFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.api.AttributeExpressionWidgetFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = AttributeExpressionWidgetFactory.class, path = "Rtc/Modules/QueriesModule/AttributeExpressionWidgetFactories")
public class EnumerationAttributeExpressionWidgetFactory extends AbstractAttributeExpressionWidgetFactory {

    NodeExplorerManagerProviderPanel panel;

    @Override
    public boolean canCreate(RtcEditableAttributeExpression expression,RtcQuery query) {
        if (expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePossibleValues.class) != null
                || expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePrefferedValues.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Widget createDescription(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        RtcQueryAttributePossibleValues poss =
                expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePossibleValues.class);
        RtcQueryAttributePrefferedValues pref =
                expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePrefferedValues.class);
        //NodeExplorerManagerProviderPanel panel;
        ValueProviderNode rootNode;
        if (poss != null && pref != null) {
            rootNode = new ValueProviderNode(pref, expression);
        } else {
            if (poss != null) {
                rootNode = new ValueProviderNode(poss, expression);
            } else {
                if (pref != null) {
                    rootNode = new ValueProviderNode(pref, expression);
                } else {
                    throw new IllegalStateException(NbBundle.getMessage(EnumerationAttributeExpressionWidgetFactory.class, "CancreateReturnsTrue.exp"));
                }
            }
        }

        panel = new NodeExplorerManagerProviderPanel(rootNode);

        Widget widget = new LayerWidget(scene);
        OutlineView ov = new OutlineView();

//        ExpandNodeHack list = new ExpandNodeHack(ov, panel.getExplorerManager());

        ov.getOutline().setRootVisible(false);
        ov.getOutline().setShowGrid(false);
        ov.getOutline().setShowHorizontalLines(false);
        ov.getOutline().setShowVerticalLines(false);
        ov.getOutline().setTableHeader(null);
        Dimension dim = new Dimension(200, 220);
        ov.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ov.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        ov.setWheelScrollingEnabled(true);
        panel.setLayout(new BorderLayout());
        panel.add(ov, BorderLayout.CENTER);
        ComponentWidget comp = new ComponentWidget(scene, panel);
        widget.setLayout(LayoutFactory.createOverlayLayout());
        widget.setPreferredSize(dim);
        widget.addChild(comp);
        //TODO : hack : this hack to expand all nodes
        RequestProcessor.getDefault().post(new ExpandNodeHack(ov, panel.getExplorerManager()), 1000);
        return widget;
    }

    @Override
    public Widget[] createFooter(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        RtcQueryAttributePossibleValues poss =
                expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePossibleValues.class);
        RtcQueryAttributePrefferedValues pref =
                expression.getQueryAttribute().getLookup().lookup(RtcQueryAttributePrefferedValues.class);
        if (poss != null && pref != null) {
            //TODO : I18N
            JButton button = new JButton("Advanced");
            button.addActionListener(new PossibleValuesActionListener(expression, poss));
            ComponentWidget comp = new ComponentWidget(scene, button);
            return new Widget[]{comp};
        } else {
            return new Widget[]{};
        }
    }

    @Override
    public boolean canCreateValueWidgets(RtcEditableAttributeExpression expression,RtcQuery query) {
        return false;
    }

    @Override
    public boolean canCreateVariableWidgets(RtcEditableAttributeExpression expression,RtcQuery query) {
        return false;
    }

    class PossibleValuesActionListener implements ActionListener {

        private RtcQueryAttributePossibleValues possible;
        private RtcEditableAttributeExpression expression;
        private PossibleValuesSelectionPanel pvsp;
        private DialogDescriptor desc;

        public PossibleValuesActionListener(RtcEditableAttributeExpression expression, RtcQueryAttributePossibleValues possible) {
            this.possible = possible;
            this.expression = expression;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pvsp = new PossibleValuesSelectionPanel(possible, expression);
            desc = new DialogDescriptor(pvsp, NbBundle.getMessage(EnumerationAttributeExpressionWidgetFactory.class, "PossibleValuesWindow.name"));
            Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
            dialog.setResizable(false);
            dialog.setVisible(true);
            if (desc.getValue().equals(DialogDescriptor.OK_OPTION)) {
                RequestProcessor.getDefault().post(new SelectionRunnable());
            }
        }

        private class SelectionRunnable implements Runnable {

            @Override
            public void run() {
                HashMap<RtcQueryAttributeValue, Boolean> sv = pvsp.getSelectedValues();
                RtcQueryAttributeValue[] vals = expression.getSelectedValues();
                for (RtcQueryAttributeValue val : sv.keySet()) {
                    //TODO: hack z try-catch bo if nie działa tak jak chciałem
                    //if (!contains(vals, val)) {
                    try {
                        if (sv.get(val) == true) {
                            expression.addSelectedValue(val);
                        } else {
                            expression.removeSelectedValue(val);
                        }
                    } catch (IllegalArgumentException ex) {
                        RtcLogger.getLogger(EnumerationAttributeExpressionWidgetFactory.class)
                                .log(Level.WARNING, ex.getLocalizedMessage(), ex);
                    }
                    //}
                }
                ////System.out.println(ContributorSelectedValuesMap.getMap());
                ContributorSelectedValuesMap.getMap().clear();
                expression = pvsp.getExpression();
            }

            /**
             * Check if <code>RtcQueryAttributeValue[]</code> contains
             * <code>RtcQueryAttributeValue</code>.
             * 
             * @return <code>True</code> if <code>RtcQueryAttributeValue[]</code>
             * contains <code>RtcQueryAttributeValue</code>. If not it returns
             * <code>False</code>
             */
            private boolean contains(RtcQueryAttributeValue[] vals, RtcQueryAttributeValue val) {
                for (RtcQueryAttributeValue v : vals) {
                    if (v.equals(val)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}

class ExpandNodeHack implements Runnable {

    private OutlineView ov;
    private ExplorerManager manager;

    public ExpandNodeHack(OutlineView ov, ExplorerManager manager) {
        this.ov = ov;
        this.manager = manager;
    }

    @Override
    public void run() {
        if (EventQueue.isDispatchThread()) {
            for (Node ch : manager.getRootContext().getChildren().getNodes()) {
                expand(ch,ov);
            }
        } else {
            EventQueue.invokeLater(this);
        }
    }
    private void expand(Node n,OutlineView ov){
        ov.expandNode(n);
        for(Node ch : n.getChildren().getNodes()){
            expand(ch, ov);
        }
    }
}

class NodeExplorerManagerProviderPanel extends JComponent implements ExplorerManager.Provider {

	private static final long serialVersionUID = -887188317196722666L;
	private Node root;
    private ExplorerManager manager = new ExplorerManager();

    public NodeExplorerManagerProviderPanel(Node root) {
        this.root = root;
        manager.setRootContext(root);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}
