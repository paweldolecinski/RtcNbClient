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
package pl.edu.amu.wmi.kino.netbeans.view.customview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.openide.awt.MouseUtils;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewListModel;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNode;
import pl.edu.amu.wmi.kino.netbeans.view.customview.VerticalLayout;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.CustomViewCallbackImpl;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.CustomViewColumnRowRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.CustomViewDefaultExpandColumn;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.CustomViewListModelImpl;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewClassicNodeRowRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewRowRenderProvider;

/**
 *
 * @author Patryk Żywica
 */
public class CustomView extends JScrollPane implements PropertyChangeListener, ListDataListener {

    private Color selectedBackgroundColor = UIManager.getColor("List.selectionBackground");
    private Color selectedForegroundColor = UIManager.getColor("List.selectionForeground");
    private Color backgroundColor = UIManager.getColor("List.background");
    private Color foregroundColor = UIManager.getColor("List.foreground");
    private int INDENT_WIDTH = 15;
    private boolean showPreColumn = true;
    private boolean showPostColumn = true;
    private boolean showExpandColumn = true;
    private int preColumnWidth = 17;
    private int postColumnWidth = 35;
    private static final long serialVersionUID = 12432414234L;
    private transient ExplorerManager manager;
    private JPanel panel;
    private BoxLayout boxLayout;
    private CustomViewListModel model;
    //link between component and customView nodes
    private HashMap<CustomViewNode, JComponent> nodesToComponents =
            new HashMap<CustomViewNode, JComponent>();
    private HashMap<JComponent, CustomViewNode> componentsToNodes =
            new HashMap<JComponent, CustomViewNode>();
    private HashMap<CustomViewNode, CustomViewRenderProvider> nodesToProviders =
            new HashMap<CustomViewNode, CustomViewRenderProvider>();
    private HashMap<CustomViewNode, CustomViewCallback> nodesToCallbacks =
            new HashMap<CustomViewNode, CustomViewCallback>();
    //to handle selection
    private CustomViewNode currentlySelectedNode;
    private boolean rootVisible = true;

    public CustomView() {
        super();
        panel = new JPanel();
        panel.setLayout(new VerticalLayout(0, VerticalLayout.BOTH));
        setViewportView(panel);
        ////System.out.println("Startujemy CustomView:");
        ////System.out.println(selectedBackgroundColor);
        ////System.out.println(selectedForegroundColor);
        ////System.out.println(backgroundColor);
        ////System.out.println(foregroundColor);
    }

    public boolean isRootVisible() {
        return rootVisible;
    }

    public void setRootVisible(boolean rootVisible) {
        this.rootVisible = rootVisible;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        manager = ExplorerManager.find(this);
        ////System.out.println("Znaleziony manager : " + manager.toString());

//        panel.addMouseListener(this);
        panel.addMouseListener(new CustomViewPopupAdapter());
        initialize();

        manager.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        ////System.out.println("----------------PropertyChangeEvent z explorer managera #" + manager.hashCode() + " : " + evt.getPropertyName());
        if (evt.getPropertyName().equals(ExplorerManager.PROP_ROOT_CONTEXT)) {
            initialize();
        }
    }

    /**
     * Inicjuje calosc komponentu, zarowno model jak i panel
     */
    private void initialize() {
        ////System.out.println("Wywolanie inicjalizacji");
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                synchronized (getTreeLock()) {
                    if (model != null) {
                        model.removeListDataListener(CustomView.this);
                    }
                    nodesToCallbacks.clear();
                    nodesToProviders.clear();
                    currentlySelectedNode = null;
                    ////System.out.println("Tworze nowy model");
                    model = new CustomViewListModelImpl(manager.getRootContext());
                    //TODO : bikol : expandsion hack
                    model.getElementAt(0).expand();
                    if (!rootVisible) {
                        CustomViewNode[] tmp = model.getElements();
                        for (CustomViewNode n : tmp) {
                            n.expand();
                        }
                    }
                    ////System.out.println("nowy model utworzony");
                    model.addListDataListener(CustomView.this);
                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                }
            }
        });
    }

    /**
     * Metoda powinna być wywolywana tylko po zmianie calego modelu, badz w wypadku
     * koniecznosci calkowitej resynchronizacji.
     *
     * Aktualizuje zawartosc panelu, na podstawie obecnego modelu.
     */
    private void refresh() {
        assert EventQueue.isDispatchThread();
        synchronized (getTreeLock()) {
            ////System.out.println("Wywolanie refresh calego componentu. Nowych elementow : " + model.getSize());
            panel.removeAll();
            componentsToNodes.clear();
            nodesToComponents.clear();
            ////System.out.println("Przed dodawaniem componentow");
            boolean first = true;
            for (CustomViewNode node : model.getElements()) {

                ////System.out.println("getComponentFor " + node.getNode().getDisplayName());
                if (first && !rootVisible) {
                    ////System.out.println("skip");
                    first = false;
                    continue;
                }
                first = false;
                panel.add(getComponentFor(node));
                ////System.out.println("koniec get Component");
                if (currentlySelectedNode == node) {
                    nodesToProviders.get(node).setSelected(currentlySelectedNode == node);
                }
            }
            ////System.out.println("Przed walidacja");
            panel.validate();
            validate();
            repaint();
            ////System.out.println("Po wywolanej walidacji");
        }
    }

    private JComponent getComponentFor(CustomViewNode node) {
        ////System.out.println("poczatek getCompoenyn");
        if (!nodesToComponents.containsKey(node)) {
            Node n = node.getNode();
            CustomViewRowRenderProvider rrp = n.getLookup().lookup(CustomViewRowRenderProvider.class);
            CustomViewColumnRenderProvider crp = n.getLookup().lookup(CustomViewColumnRenderProvider.class);
            CustomViewColumnProvider cp = null;

            if (rrp == null && crp == null) {
                rrp = new CustomViewClassicNodeRowRenderProvider(n);
            } else {
                if (rrp == null) {
                    cp = findColumnProviderFor(n);
                    if (cp != null) {
                        rrp = new CustomViewColumnRowRenderProvider(crp, cp, n);
                    } else {
                        rrp = new CustomViewClassicNodeRowRenderProvider(n);
                    }
                }
            }

            if (rrp != null) {
                //callback handling
                rrp.setCallback(getCallbackFor(node));

                JPanel tmp = new JPanel(new BorderLayout(0, 0));
                JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                if (showPreColumn && rrp.shouldDisplayPreColumn()) {
                    //TODO : for future : fix resizing of pre and post columns
                    JPanel prePanel = new JPanel(new BorderLayout(0, 0));
                    prePanel.setOpaque(true);
                    prePanel.setBackground(Color.decode("#F9F9F9"));

                    JComponent preCol = rrp.getPreColumn();
                    if (preCol != null) {
                        prePanel.add(preCol);
                        prePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                        prePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                    } else {
                        prePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                        prePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                    }
                    prePanel.setMinimumSize(new Dimension(preColumnWidth, 16));
//                    prePanel.setSize(preColumnWidth, 10);
                    westPanel.add(prePanel);
                }
                if (rrp.isIndentAllowed()) {
                    JPanel indentPanel = new JPanel();
                    indentPanel.setOpaque(false);
                    indentPanel.setMinimumSize(new Dimension(computeIndentFor(node) * INDENT_WIDTH, 1));
                    indentPanel.setPreferredSize(new Dimension(computeIndentFor(node) * INDENT_WIDTH, 1));
                    indentPanel.setMaximumSize(new Dimension(computeIndentFor(node) * INDENT_WIDTH, 1));
                    westPanel.add(indentPanel);
                }
                if (showExpandColumn && rrp.shouldDisplayExpandColumn()) {
                    westPanel.add(new CustomViewDefaultExpandColumn(node));
                }
                tmp.add(westPanel, BorderLayout.WEST);
                tmp.add(rrp.getMainColumn(), BorderLayout.CENTER);
                if (showPostColumn && rrp.shouldDisplayPostColumn()) {
                    JPanel postPanel = new JPanel(new BorderLayout(0, 0));
                    postPanel.setOpaque(true);
                    postPanel.setBackground(Color.ORANGE);
                    postPanel.setPreferredSize(new Dimension(postColumnWidth, 1));
                    JComponent postCol = rrp.getPostColumn();
                    if (postCol != null) {
                        postPanel.add(postCol);
                    }
                    tmp.add(postPanel, BorderLayout.EAST);
                }

//                recursivelyAddMouseListeners(tmp, new InternalSelectionListener(node));

                componentsToNodes.put(tmp, node);
                nodesToProviders.put(node, rrp);
                nodesToComponents.put(node, tmp);
            } else {
                assert false;
                if (crp != null && cp != null) {
                    crp.setCallback(getCallbackFor(node));
                    JLabel tmp = new JLabel("column renderer provider");
                    componentsToNodes.put(tmp, node);
                    nodesToProviders.put(node, rrp);
                    nodesToComponents.put(node, tmp);

//                    throw new UnsupportedOperationException();
                } else {
                }
            }
        }
        ////System.out.println("koniec getComp");
        return nodesToComponents.get(node);
    }

    private void recursivelyAddMouseListeners(Component cmp, MouseListener l) {
        if (cmp instanceof Container) {

            for (Component c : ((Container) cmp).getComponents()) {
                ////System.out.println("sa dzieci");
                recursivelyAddMouseListeners(c, l);
            }
        }
        ////System.out.println("dodaje internal");
        cmp.addMouseListener(l);
        cmp.addFocusListener(new FocusImpl());
    }

    /**
     * Zwraca null jesli niema zadnego column profidera
     * @param node
     * @return
     */
    private CustomViewColumnProvider findColumnProviderFor(Node node) {
        ////System.out.println("poczatek find column prov");
        Node n = node.getParentNode();
        CustomViewColumnProvider cp = null;
        while (n != null) {
            cp = n.getLookup().lookup(CustomViewColumnProvider.class);
            if (cp != null) {
                return cp;
            }
            n = n.getParentNode();
        }
        ////System.out.println("koniec getcol prov");
        return cp;
    }

    private CustomViewNode getNodeFor(JComponent component) {
        return componentsToNodes.get(component);
    }

    private CustomViewCallback getCallbackFor(CustomViewNode node) {
        if (!nodesToCallbacks.containsKey(node)) {
            nodesToCallbacks.put(node, new CustomViewCallbackImpl(node, selectedBackgroundColor, selectedForegroundColor, backgroundColor, foregroundColor));
        }
        return nodesToCallbacks.get(node);
    }

    private int computeIndentFor(CustomViewNode node) {
        ////System.out.println("poczatek wyliczania indentu");
        int res = 0;
        CustomViewNode n = node;
        while (n.getParent() != null) {
            n = n.getParent();
            res++;
        }
        ////System.out.println("Wyliczony indent : " + res);
        return res;
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        ////System.out.println("Interval added");
        refresh();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        ////System.out.println("Interfal removed");
        refresh();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        ////System.out.println("contents Changed");
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    refresh();
                }
            });
        } else {
            refresh();
        }
    }

    private class CustomViewPopupAdapter extends MouseUtils.PopupMouseAdapter {

        @Override
        protected void showPopup(MouseEvent evt) {
            mousePressed(evt);
            ////System.out.println("show popup");
            if (currentlySelectedNode != null) {
                ////System.out.println("show popup nie null current");
                JPopupMenu popup = Utilities.actionsToPopup(currentlySelectedNode.getNode().getActions(true), CustomView.this);
                popup.show(panel, evt.getX(), evt.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Component c = panel.getComponentAt(e.getPoint());
            CustomViewNode node = null;
            if (c instanceof JComponent) {
                node = componentsToNodes.get(c);
            }
            if (node != null) {
                ////System.out.println("Kliknieto w : " + node.getNode().getDisplayName());
            } else {
                ////System.out.println("Kliknieto w puste pole");
            }
            if (node != null && currentlySelectedNode != node) {
                ////System.out.println("Jest zmiana selekcji");
                if (currentlySelectedNode != null) {
                    CustomViewRenderProvider rp = nodesToProviders.get(currentlySelectedNode);
                    //non null check to handle removal of selected node
                    if (rp != null) {
                        rp.setSelected(false);
                    }
                    JComponent comp = nodesToComponents.get(currentlySelectedNode);
                    if (comp != null) {
                        comp.setBackground(backgroundColor);
                        comp.setForeground(foregroundColor);
                    }
                }
                CustomViewRenderProvider rp = nodesToProviders.get(node);
                rp.setSelected(true);
                JComponent comp = nodesToComponents.get(node);
                comp.setBackground(selectedBackgroundColor);
                comp.setForeground(selectedForegroundColor);
                currentlySelectedNode = node;
                try {
                    manager.setSelectedNodes(new Node[]{node.getNode()});
                } catch (PropertyVetoException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } else {
                if (node == null && node != currentlySelectedNode) {
                    CustomViewRenderProvider rp = nodesToProviders.get(currentlySelectedNode);
                    //non null check to handle removal of selected node
                    if (rp != null) {
                        rp.setSelected(false);
                    }
                    JComponent comp = nodesToComponents.get(currentlySelectedNode);
                    if (comp != null) {
                        comp.setBackground(backgroundColor);
                        comp.setForeground(foregroundColor);
                    }
                    currentlySelectedNode = null;
                    try {
                        manager.setSelectedNodes(new Node[]{});
                    } catch (PropertyVetoException ex) {
                        assert false : "not allowed to throw";
                    }
                }

            }
        }
    }

    private class InternalSelectionListener implements MouseListener {

        private CustomViewNode node;

        public InternalSelectionListener(CustomViewNode node) {
            this.node = node;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            ////System.out.println("idzie internal mouse");
            if (node != null && currentlySelectedNode != node) {
                ////System.out.println("Jest zmiana selekcji internal");
                if (currentlySelectedNode != null) {
                    CustomViewRenderProvider rp = nodesToProviders.get(currentlySelectedNode);
                    //non null check to handle removal of selected node
                    if (rp != null) {
                        rp.setSelected(false);
                    }
                    JComponent comp = nodesToComponents.get(currentlySelectedNode);
                    if (comp != null) {
                        comp.setBackground(backgroundColor);
                        comp.setForeground(foregroundColor);
                    }
                }
                //TODO : bikol : fix this null problem
                if (node == null) {
                    ////System.out.println("node jest cholerny null");
                }

                CustomViewRenderProvider rp = nodesToProviders.get(node);
                if (rp != null) {
                    rp.setSelected(true);
                }
                JComponent comp = nodesToComponents.get(node);
                if (comp != null) {
                    comp.setBackground(selectedBackgroundColor);
                    comp.setForeground(selectedForegroundColor);
                }
                currentlySelectedNode = node;
                try {
                    manager.setSelectedNodes(new Node[]{node.getNode()});
                } catch (PropertyVetoException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } else {
                if (node == null && node != currentlySelectedNode) {
                    CustomViewRenderProvider rp = nodesToProviders.get(currentlySelectedNode);
                    //non null check to handle removal of selected node
                    if (rp != null) {
                        rp.setSelected(false);
                    }
                    JComponent comp = nodesToComponents.get(currentlySelectedNode);
                    if (comp != null) {
                        comp.setBackground(backgroundColor);
                        comp.setForeground(foregroundColor);
                    }
                    currentlySelectedNode = null;
                    try {
                        manager.setSelectedNodes(new Node[]{});
                    } catch (PropertyVetoException ex) {
                        assert false : "not allowed to throw";
                    }
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseExited(MouseEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class FocusImpl implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            ////System.out.println("gained");
        }

        @Override
        public void focusLost(FocusEvent e) {
            ////System.out.println("lost");
        }
    }
}
