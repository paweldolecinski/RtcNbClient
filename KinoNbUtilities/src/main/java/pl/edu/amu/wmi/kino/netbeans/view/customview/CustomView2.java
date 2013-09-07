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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.openide.awt.MouseUtils;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.NodePopupFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.CustomViewColumnRowRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.table.CustomViewDefaultExpandColumn;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.table.CustomViewNodeGroup;
import pl.edu.amu.wmi.kino.netbeans.view.customview.impl.table.CustomViewTableModel;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewClassicNodeRowRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewRowRenderProvider;

/**
 *
 * @author Patryk Żywica
 */
public class CustomView2 extends JScrollPane implements TableCellRenderer, TableCellEditor, ListSelectionListener {

    private static final long serialVersionUID = 4353451435345L;
    private ExplorerManager manager;
    private CustomViewTableModel model;
    private JTable table = new JTable();
    private int INDENT_WIDTH = 15;
    private boolean showPreColumn = true;
    private boolean showPostColumn = true;
    private boolean showExpandColumn = true;
    private int preColumnWidth = 17;
    private int postColumnWidth = 35;
    private boolean allowPopup = true;
    private Set<CellEditorListener> cellEditorListeners = new CopyOnWriteArraySet<CellEditorListener>();
    private TablePopupFactory popupFactory = new TablePopupFactory();
    private TableSelectionListener managerListener = null;
    /** weak variation of the listener for property change on the explorer manager */
    private PropertyChangeListener wlpc;
    /** weak variation of the listener for vetoable change on the explorer manager */
    private VetoableChangeListener wlvc;
    /** Listener on keystroke to invoke default action */
    private ActionListener defaultTreeActionListener;
    //renderer support
    private JPanel rendererPanel = new JPanel(new BorderLayout(0, 0));
    private JPanel rendererWestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private JPanel rendererPrePanel = new JPanel(new BorderLayout(0, 0));
    private JPanel rendererIndentPanel = new JPanel();
    private JPanel rendererPostPanel = new JPanel(new BorderLayout(0, 0));
    //will be used only for rendering
    private CustomViewDefaultExpandColumn defaultRendererExpandedExpandColumn = new CustomViewDefaultExpandColumn(true);
    private CustomViewDefaultExpandColumn defaultRendererCollapsedExpandColumn = new CustomViewDefaultExpandColumn(false);
    //end of renderer support
    //editor support
    private CustomViewRowRenderProvider editingRRP;
    private JPanel editorPanel = new JPanel(new BorderLayout(0, 0));
    private JPanel editorWestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private JPanel editorPrePanel = new JPanel(new BorderLayout(0, 0));
    private JPanel editorIndentPanel = new JPanel();
    private JPanel editorPostPanel = new JPanel(new BorderLayout(0, 0));
    private CustomViewDefaultExpandColumn defaultEditorExpandColumn = new CustomViewDefaultExpandColumn();

    public CustomView2() {
        super();
        table.setDefaultRenderer(CustomViewNodeGroup.class, this);
        table.setDefaultEditor(CustomViewNodeGroup.class, this);
        setViewportView(table);

        getActionMap().put("org.openide.actions.PopupAction", new PopupAction());
        table.addMouseListener(new CustomViewPopupListener());

        defaultTreeActionListener = new DefaultTreeAction(table);
        table.registerKeyboardAction(
                defaultTreeActionListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

        //preconfiguration of renderer panels
        rendererIndentPanel.setOpaque(false);
        rendererPrePanel.setOpaque(false);
        rendererPostPanel.setOpaque(false);
        rendererWestPanel.setOpaque(false);
        rendererPanel.setOpaque(true);

        //preconfiguration of editor panels
        editorIndentPanel.setOpaque(false);
        editorPrePanel.setOpaque(false);
        editorPostPanel.setOpaque(false);
        editorWestPanel.setOpaque(false);
        editorPanel.setOpaque(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        lookupExplorerManager();

        model = new CustomViewTableModel(manager.getRootContext());
        table.setModel(model);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().removeListSelectionListener(managerListener);
        manager.removePropertyChangeListener(wlpc);
        manager.removeVetoableChangeListener(wlvc);
        manager = null;
    }

    /** Registers in the tree of components.
     */
    private void lookupExplorerManager() {
        // Enter key in the tree

        if (managerListener == null) {
            managerListener = new TableSelectionListener();
        }

        ExplorerManager newManager = ExplorerManager.find(this);
        if (newManager != manager) {
            if (manager != null) {
                manager.removeVetoableChangeListener(wlvc);
                manager.removePropertyChangeListener(wlpc);
            }

            manager = newManager;

            manager.addVetoableChangeListener(wlvc = WeakListeners.vetoableChange(managerListener, manager));
            manager.addPropertyChangeListener(wlpc = WeakListeners.propertyChange(managerListener, manager));
        }

        synchronizeRootContext();
        synchronizeSelectedNodes(true);

        // Sometimes the listener is registered twice and we get the
        // selection events twice. Removing the listener before adding it
        // should be a safe fix.
        table.getSelectionModel().removeListSelectionListener(managerListener);
        table.getSelectionModel().addListSelectionListener(managerListener);
    }
    private HashMap<CustomViewRowRenderProvider, CustomViewCallback> rrpToCallback =
            new HashMap<CustomViewRowRenderProvider, CustomViewCallback>(10);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Node n = ((CustomViewNodeGroup) value).getMainNode();

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
            if (!rrpToCallback.containsKey(rrp)) {
                createCallbackFor((CustomViewNodeGroup) value, rrp);
            }
            rrp.setCallback(rrpToCallback.get(rrp));

            //selection handling
            rrp.setExpanded(((CustomViewNodeGroup) value).isExpanded());
            rrp.setFocused(hasFocus);
            rrp.setSelected(isSelected);

            if (isSelected) {
                rendererPanel.setBackground(table.getSelectionBackground());
                rendererPanel.setForeground(table.getSelectionForeground());
            } else {
                rendererPanel.setBackground(table.getBackground());
                rendererPanel.setForeground(table.getForeground());
            }
            rendererPanel.removeAll();
            rendererWestPanel.removeAll();
            if (showPreColumn && rrp.shouldDisplayPreColumn()) {
                //TODO : for future : fix resizing of pre and post columns
                rendererPrePanel.removeAll();

                JComponent preCol = rrp.getPreColumn();
                if (preCol != null) {
                    rendererPrePanel.add(preCol);
                    rendererPrePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                    rendererPrePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                } else {
                    rendererPrePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                    rendererPrePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                }
                rendererPrePanel.setMinimumSize(new Dimension(preColumnWidth, 16));
//                    prePanel.setSize(preColumnWidth, 10);
                rendererWestPanel.add(rendererPrePanel);
            }
            if (rrp.isIndentAllowed()) {
                rendererIndentPanel.setMinimumSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                rendererIndentPanel.setPreferredSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                rendererIndentPanel.setMaximumSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                rendererWestPanel.add(rendererIndentPanel);
            }
            if (showExpandColumn && rrp.shouldDisplayExpandColumn()) {
                if (((CustomViewNodeGroup) value).isExpanded()) {
                    rendererWestPanel.add(defaultRendererExpandedExpandColumn);
                } else {
                    rendererWestPanel.add(defaultRendererCollapsedExpandColumn);
                }
            }
            rendererPanel.add(rendererWestPanel, BorderLayout.WEST);
            rendererPanel.add(rrp.getMainColumn(), BorderLayout.CENTER);
            if (showPostColumn && rrp.shouldDisplayPostColumn()) {
                rendererPostPanel.removeAll();
                rendererPostPanel.setPreferredSize(new Dimension(postColumnWidth, 1));
                JComponent postCol = rrp.getPostColumn();
                if (postCol != null) {
                    rendererPostPanel.add(postCol);
                }
                rendererPanel.add(rendererPostPanel, BorderLayout.EAST);
            }
            if (table.getRowHeight(row) != rendererPanel.getPreferredSize().height) {
                table.setRowHeight(row, rendererPanel.getPreferredSize().height);
            }
            return rendererPanel;
        }
        else {
            throw new IllegalStateException("Row render provider should not be null");
        }
    }

    private int computeIndentFor(CustomViewNodeGroup node) {
        int res = 0;
        CustomViewNodeGroup n = node;
        while (n.getParent() != null) {
            n = n.getParent();
            res++;
        }
        return res - 1;
    }

    private CustomViewCallback createCallbackFor(CustomViewNodeGroup group, CustomViewRowRenderProvider rrp) {
        rrpToCallback.put(rrp, new CustomViewCallbackImpl(group));
        return rrpToCallback.get(rrp);
        //TODO
    }

    /**
     * Zwraca null jesli niema zadnego column providera
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

    /**
     * Used by editor to listen on selection changes.
     * @param e
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        //System.out.println("idzie event z selection listenera: " + table.getEditingRow() + " selected:" + table.getSelectedRows());
        boolean isSelected = false;
        for (int r : table.getSelectedRows()) {
            if (r == table.getEditingRow()) {
                isSelected = true;
                break;
            }
        }

        //System.out.println(isSelected);
        if (editingRRP != null) {
            editingRRP.setSelected(isSelected);
        }
        if (isSelected) {
            editorPanel.setBackground(table.getSelectionBackground());
            editorPanel.setForeground(table.getSelectionForeground());
        } else {
            editorPanel.setBackground(table.getBackground());
            editorPanel.setForeground(table.getForeground());
        }
        //System.out.println("koniec eventu");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //System.out.println("getTableEditor isSelected: " + isSelected);
//        isSelected = true;
        Node n = ((CustomViewNodeGroup) value).getMainNode();
        if (editingRRP == null) {
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
            editingRRP = rrp;
        }
        if (editingRRP != null) {
            //callback handling
            if (!rrpToCallback.containsKey(editingRRP)) {
                createCallbackFor((CustomViewNodeGroup) value, editingRRP);
            }
            editingRRP.setCallback(rrpToCallback.get(editingRRP));

            //selection handling
            editingRRP.setExpanded(((CustomViewNodeGroup) value).isExpanded());
            editingRRP.setFocused(true);
            editingRRP.setSelected(isSelected);
            if (isSelected) {
                editorPanel.setBackground(table.getSelectionBackground());
                editorPanel.setForeground(table.getSelectionForeground());
            } else {
                editorPanel.setBackground(table.getBackground());
                editorPanel.setForeground(table.getForeground());
            }
            editorPanel.removeAll();
            editorWestPanel.removeAll();
            if (showPreColumn && editingRRP.shouldDisplayPreColumn()) {
                //TODO : for future : fix resizing of pre and post columns
                editorPrePanel.removeAll();

                JComponent preCol = editingRRP.getPreColumn();
                if (preCol != null) {
                    editorPrePanel.add(preCol);
                    editorPrePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                    editorPrePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                } else {
                    editorPrePanel.setPreferredSize(new Dimension(preColumnWidth, 16));
                    editorPrePanel.setMaximumSize(new Dimension(preColumnWidth, 16));
                }
                editorPrePanel.setMinimumSize(new Dimension(preColumnWidth, 16));
//                    prePanel.setSize(preColumnWidth, 10);
                editorWestPanel.add(editorPrePanel);
            }
            if (editingRRP.isIndentAllowed()) {
                editorIndentPanel.setMinimumSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                editorIndentPanel.setPreferredSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                editorIndentPanel.setMaximumSize(new Dimension(computeIndentFor((CustomViewNodeGroup) value) * INDENT_WIDTH, 1));
                editorWestPanel.add(editorIndentPanel);
            }
            if (showExpandColumn && editingRRP.shouldDisplayExpandColumn()) {
                defaultEditorExpandColumn.setNodeGroup((CustomViewNodeGroup) value);
                editorWestPanel.add(defaultEditorExpandColumn);
            }
            editorPanel.add(editorWestPanel, BorderLayout.WEST);
            editorPanel.add(editingRRP.getMainColumn(), BorderLayout.CENTER);
            if (showPostColumn && editingRRP.shouldDisplayPostColumn()) {
                editorPostPanel.removeAll();
                editorPostPanel.setPreferredSize(new Dimension(postColumnWidth, 1));
                JComponent postCol = editingRRP.getPostColumn();
                if (postCol != null) {
                    editorPostPanel.add(postCol);
                }
                editorPanel.add(editorPostPanel, BorderLayout.EAST);
            }
            if (table.getRowHeight(row) != editorPanel.getPreferredSize().height) {
                table.setRowHeight(row, editorPanel.getPreferredSize().height);
            }
            return editorPanel;
        } else {
            throw new IllegalStateException("Row render provider should not be null");
        }
    }

    @Override
    public Object getCellEditorValue() {
        //System.out.println("get cell editor value");
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        //System.out.println("isCellEditable");
        table.getSelectionModel().addListSelectionListener(this);
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        //System.out.println("shouldSelectCell");
        //TODO : bikol : pass selected to callback
        //TODO : hack : 
//        editorPanel.setBackground(table.getSelectionBackground());
//        editorPanel.setForeground(table.getSelectionForeground());
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        //System.out.println("stop edit");
        table.getSelectionModel().removeListSelectionListener(this);
        editingRRP = null;
        for (CellEditorListener l : cellEditorListeners) {
            l.editingStopped(new ChangeEvent(table));
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        editingRRP = null;
        //System.out.println("cancel edit");
        table.getSelectionModel().removeListSelectionListener(this);
        for (CellEditorListener l : cellEditorListeners) {
            l.editingCanceled(new ChangeEvent(table));
        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        cellEditorListeners.add(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        cellEditorListeners.add(l);
    }

    public boolean isPopupAllowed() {
        return allowPopup;
    }

    private Node getNodeFromRow(int row) {
        //System.out.println("!!poczatek getNodeFromRow"+System.currentTimeMillis());
        Node n = ((CustomViewNodeGroup) table.getValueAt(row, 0)).getMainNode();
        //System.out.println("!!koniec getNodeFromRow");
        return n;
    }

    /**
     * Find relevant actions and call the factory to create a popup.
     */
    private JPopupMenu createPopup(Point p) {
        int[] selRows = table.getSelectedRows();
        ArrayList<Node> al = new ArrayList<Node>(selRows.length);
        for (int i = 0; i < selRows.length; i++) {
            Node n = getNodeFromRow(selRows[i]);
            if (n != null) {
                al.add(n);
            }
        }
        Node[] arr = al.toArray(new Node[al.size()]);
        if (arr.length == 0) {
            // hack to show something even when no rows are selected
            arr = new Node[]{manager.getRootContext()};
        }
        p = SwingUtilities.convertPoint(this, p, table);
        int column = table.columnAtPoint(p);
        int row = table.rowAtPoint(p);
        return popupFactory.createPopupMenu(row, column, arr, table);
    }

    /**
     * Shows popup menu invoked on the table.
     */
    void showPopup(int xpos, int ypos, final JPopupMenu popup) {
        if ((popup != null) && (popup.getSubElements().length > 0)) {
            final PopupMenuListener p = new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    popup.removePopupMenuListener(this);
                    table.requestFocus();
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            };
            popup.addPopupMenuListener(p);
            popup.show(this, xpos, ypos);
        }
    }

    /** Synchronize the root context from the manager of this Explorer.
     */
    final void synchronizeRootContext() {
        if (null != table) {
            table.setModel(new CustomViewTableModel(manager.getRootContext()));
        }
    }

    /** Synchronize the selected nodes from the manager of this Explorer.
     */
    final void synchronizeSelectedNodes(boolean scroll, Node... nodes) {
        if (!needToSynchronize()) {
            return;
        }
//        expandSelection();
        table.invalidate();
        invalidate();
        validate();
        Node[] arr = manager.getSelectedNodes();
        table.getSelectionModel().clearSelection();
        int size = table.getRowCount();
        int firstSelection = -1;
        for (int i = 0; i < size; i++) {
            Node n = getNodeFromRow(i);
            for (int j = 0; j < arr.length; j++) {
                if ((n != null) && (n.equals(arr[j]))) {
                    table.getSelectionModel().addSelectionInterval(i, i);
                    if (firstSelection == -1) {
                        firstSelection = i;
                    }
                }
            }
        }
        if (scroll && (firstSelection >= 0)) {
            JViewport v = getViewport();
            if (v != null) {
                Rectangle rect = table.getCellRect(firstSelection, 0, true);
                if (v.getExtentSize().height > rect.height) {
                    rect.height = v.getExtentSize().height;
                }
                int ho = table.getSize().height;
                if (ho > 0) {
                    if (rect.y + rect.height > ho) {
                        rect.height = ho - rect.y;
                        if (rect.height <= 0) {
                            rect.height = 40;
                        }
                    }
                }
                v.setViewPosition(new Point()); // strange line - but without
                // it the next one is wrong
                table.scrollRectToVisible(rect);
            }
        }
    }

    private boolean needToSynchronize() {
        boolean doSync = false;
        Node[] arr = manager.getSelectedNodes();
        if (table.getSelectedRows().length != arr.length) {
            doSync = true;
        } else if (arr.length > 0) {
            List<Node> nodes = Arrays.asList(arr);
            for (int idx : table.getSelectedRows()) {
                Node n = getNodeFromRow(idx);
                if (n == null || !nodes.contains(n)) {
                    doSync = true;
                    break;
                }
            }
        }
        return doSync;
    }

    /**
     * Called when selection in tree is changed.
     */
    final private void callSelectionChanged(Node[] nodes) {
        manager.removePropertyChangeListener(wlpc);
        manager.removeVetoableChangeListener(wlvc);
        //System.out.println("callSelectionChanged przed try");
        try {
            //System.out.println("przed blokiem w try "+System.currentTimeMillis());
            manager.setSelectedNodes(nodes);
            //System.out.println("po bloku w try");
        } catch (PropertyVetoException e) {
            //System.out.println("w catch");
            synchronizeSelectedNodes(false);
        } finally {
            // to be sure not to add them twice!
            manager.removePropertyChangeListener(wlpc);
            manager.removeVetoableChangeListener(wlvc);
            manager.addPropertyChangeListener(wlpc);
            manager.addVetoableChangeListener(wlvc);
        }
    }

    /**
     * Check if selection of the nodes could break
     * the selection mode set in the ListSelectionModel.
     * @param nodes the nodes for selection
     * @return true if the selection mode is broken
     */
    private boolean isSelectionModeBroken(Node[] nodes) {

        // if nodes are empty or single then everthing is ok
        // or if discontiguous selection then everthing ok
        if (nodes.length <= 1 || table.getSelectionModel().getSelectionMode()
                == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
            return false;
        }

        // if many nodes

        // breaks single selection mode
        if (table.getSelectionModel().getSelectionMode()
                == ListSelectionModel.SINGLE_SELECTION) {
            return true;
        }

        // check the contiguous selection mode

        // check selection's rows

        // all is ok
        return false;
    }

    /** Returns the point at which the popup menu is to be shown. May return null.
     * @return the point or null
     */
    private Point getPositionForPopup() {
        int i = table.getSelectionModel().getLeadSelectionIndex();
        if (i < 0) {
            return null;
        }
        int j = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        if (j < 0) {
            j = 0;
        }

        Rectangle rect = table.getCellRect(i, j, true);
        if (rect == null) {
            return null;
        }

        Point p = new Point(rect.x + rect.width / 3,
                rect.y + rect.height / 2);

        // bugfix #36984, convert point by TableView.this
        p = SwingUtilities.convertPoint(table, p, CustomView2.this);

        return p;
    }

    /** Invokes default action.
     */
    private class DefaultTreeAction implements ActionListener {

        private JTable outline;

        DefaultTreeAction(JTable outline) {
            this.outline = outline;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (outline.getSelectedColumn() != 0) {
                return;
            }

            Node[] nodes = manager.getSelectedNodes();

            if (nodes.length == 1) {
                Action a = nodes[0].getPreferredAction();

                if (a != null) {
                    if (a.isEnabled()) {
                        a.actionPerformed(new ActionEvent(nodes[0], ActionEvent.ACTION_PERFORMED, "")); // NOI18N
                    } else {
                        Logger.getLogger(JTable.class.getName()).info("Action " + a + " on node " + nodes[0] + " is disabled");
                    }
                }
            }
        }
    }

    /**
     * Listener attached to the explorer manager and also to the
     * changes in the table selection.
     */
    private class TableSelectionListener implements VetoableChangeListener, ListSelectionListener, PropertyChangeListener {

        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (manager == null) {
                return; // the tree view has been removed before the event got delivered
            }
            if (evt.getPropertyName().equals(ExplorerManager.PROP_ROOT_CONTEXT)) {
                synchronizeRootContext();
            }
            if (evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES)) {
                synchronizeSelectedNodes(true);
            }
        }

        public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
            //System.out.println("poczatek value changed");
            int selectedRows[] = table.getSelectedRows();
            ArrayList<Node> selectedNodes = new ArrayList<Node>(selectedRows.length);
            for (int i = 0; i < selectedRows.length; i++) {
                Node n = getNodeFromRow(selectedRows[i]);
                if (n != null) {
                    selectedNodes.add(n);
                }
            }
            //System.out.println("value changed przed call selection");
            callSelectionChanged(selectedNodes.toArray(new Node[selectedNodes.size()]));
            //System.out.println("koniec value changed");
        }

        public void vetoableChange(java.beans.PropertyChangeEvent evt) throws java.beans.PropertyVetoException {
            //System.out.println("TreeListener.vetoableChange pocztek");
            if (evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES)) {
                // issue 11928 check if selecetion mode will be broken
                Node[] nodes = (Node[]) evt.getNewValue();
                if (isSelectionModeBroken(nodes)) {
                    throw new PropertyVetoException("selection mode " + " broken by " + Arrays.asList(nodes), evt); // NOI18N
                }
                //System.out.println("TreeListener.vetoableChange koniec");
            }
        }
    }

    private static class TablePopupFactory extends NodePopupFactory {

        public TablePopupFactory() {
        }

        @Override
        public JPopupMenu createPopupMenu(int row, int column, Node[] selectedNodes, Component component) {
            if (component instanceof JTable) {
                JTable et = (JTable) component;
                int modelRowIndex = et.convertColumnIndexToModel(column);
                setShowQuickFilter(modelRowIndex != 0);
            }
            return super.createPopupMenu(row, column, selectedNodes, component);
        }
    }

    class CustomViewCallbackImpl implements CustomViewCallback {

        private CustomViewNodeGroup group;

        public CustomViewCallbackImpl(CustomViewNodeGroup group) {
            this.group = group;
        }

        @Override
        public void expand() {
            group.expand();
        }

        @Override
        public void collapse() {
            group.collapse();
        }

        @Override
        public Color getSelectedBackgroundColor() {
            return table.getSelectionBackground();
        }

        @Override
        public Color getSelectedForegroundColor() {
            return table.getSelectionForeground();
        }

        @Override
        public Color getBackgroundColor() {
            return table.getBackground();
        }

        @Override
        public Color getForegroundColor() {
            return table.getForeground();
        }
    }

    /**
     * Action registered in the component's action map.
     */
    private class PopupAction extends javax.swing.AbstractAction implements Runnable {

		private static final long serialVersionUID = 2787296219035505687L;

		public void actionPerformed(ActionEvent evt) {
            SwingUtilities.invokeLater(this);
        }

        public void run() {
            Point p = getPositionForPopup();
            if (p == null) {
                return;
            }
            if (isPopupAllowed()) {
                JPopupMenu pop = createPopup(p);
                showPopup(p.x, p.y, pop);
            }
        }
    };

    private class CustomViewPopupListener extends MouseUtils.PopupMouseAdapter {

        @Override
        protected void showPopup(MouseEvent e) {

            int selRow = table.rowAtPoint(e.getPoint());

            if (selRow != -1) {
                if (!table.getSelectionModel().isSelectedIndex(selRow)) {
                    table.getSelectionModel().clearSelection();
                    table.getSelectionModel().setSelectionInterval(selRow, selRow);
                }
            } else {
                table.getSelectionModel().clearSelection();
            }
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), CustomView2.this);
            if (isPopupAllowed()) {
                JPopupMenu pop = createPopup(p);
                CustomView2.this.showPopup(p.x, p.y, pop);
                e.consume();
            }
        }
    }
}
