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
package pl.edu.amu.wmi.kino.rtc.client.queries.result;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.swing.etable.ETableColumn;
import org.netbeans.swing.etable.ETableColumnModel;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.EditQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.NewQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.PrevQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.RefreshAction;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttributeLabelFactory.LabelProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttributeLabelFactory.LabelSniffer;

//to jest zle
/**
 * This <code>TopComponent</code> is used for showing query results. There can be
 * several ResultsTopComponent objects in whole platform. To obtain it you should
 * call <code>ResultsTopComponent.findInstanceFor()</code>. Also it is obligatory to call
 * <code>refreshResults()</code> after obtaining this <code>TopComponent</code> instance.
 *
 *
 * @author Szymon Sadlo
 * @author Patryk Żywica
 */
@Deprecated
public final class ResultsTopComponent extends TopComponent implements ExplorerManager.Provider {

    private ExplorerManager manager;
    private RtcOutline outlineView;
    private static final String PREFERRED_ID = "ResultsTopComponent";
    private static ResultsManager editorManager = new ResultsManager();
    private static RtcQuery previousQuery = null;
    private InstanceContent ic = new InstanceContent();
    private RtcQuery query;
    private Lookup lookup;
    private JToolBar bar;
    private String[] actualCols;

    public ResultsTopComponent(RtcQuery query) {
        this.query = query;
        manager = new ExplorerManager();
        initComponents();
        outlineView = new RtcOutline();
        outlineView.getOutline().addMouseListener(new MouseListenerImpl(outlineView.getOutline()));
        outlineView.getOutline().setDefaultEditor(Property.class, null);

        outlineView.getOutline().setDefaultRenderer(Property.class, new LabelProviderTableCellRenderer(outlineView.getOutline().getDefaultRenderer(Property.class)));
        outlineView.getOutline().setDefaultRenderer(Object.class, new EmptyTableCellRenderer());

//        outlineView.getOutline().getModel()

        Outline outline = outlineView.getOutline();
        this.removeDefaultColumn(outline);
        outline.setRootVisible(false);
        add(outlineView);

        ic.add(this.query);
        HashMap hm = new HashMap();
        hm.put(query, previousQuery);
        ic.add(hm);
        previousQuery = this.query;

        setDisplayName(NbBundle.getMessage(ResultsTopComponent.class, "ResultTopComponent.name")
                + " " + this.query.getEditableName());
        setFocusable(true);
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        bar = new JToolBar();
        bar.setFloatable(false);
        bar.addSeparator();

        Component newQuery = CallableSystemAction.get(NewQueryAction.class).getToolbarPresenter();
        Component editQuery = CallableSystemAction.get(EditQueryAction.class).getToolbarPresenter();
        Component prevQuery = CallableSystemAction.get(PrevQueryAction.class).getToolbarPresenter();
        Component refresh = CallableSystemAction.get(RefreshAction.class).getToolbarPresenter();

        bar.add(newQuery);
        bar.add(editQuery);
        bar.add(prevQuery);
        bar.addSeparator();
        bar.add(refresh);

        //add(ResultsToolbar.getDefault().getToolbar(), BorderLayout.NORTH);
        add(bar, BorderLayout.NORTH);
        lookup = new AbstractLookup(ic);
        associateLookup(new ProxyLookup(
                ExplorerUtils.createLookup(manager, getActionMap()),
                lookup));
    }

    private void removeDefaultColumn(Outline o) {
        TableColumn nodeColumn = o.getColumnModel().getColumn(0);
        nodeColumn.setHeaderValue("");
        nodeColumn.setWidth(1);
        nodeColumn.setMaxWidth(1);
        nodeColumn.setMinWidth(1);
//        o.getColumnModel().removeColumn(nodeColumn);

    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * <code>refreshResults</code> needs to be called after finding an instance
     * of the <code>ResultsTopComponent</code>. This is needed to run the specified
     * query and fetch the results.
     *
     */
    public void refreshResults() {
        RequestProcessor.getDefault().post(new ShowQueryResultsRunnable(query));
    }

    /**
     * This method is responsible for finding a <code>ResultsTopComponent</code>
     * instance which belongs to given query. Remember to call
     * <code>refreshResults()</code> method after it.
     *
     * @param query
     * @return
     */
    public static pl.edu.amu.wmi.kino.rtc.client.ui.queries.result.ResultsTopComponent findInstanceFor(RtcQuery query) {
        return editorManager.findResults(query);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(ResultsTopComponent.class);
    }

    private class ShowQueryResultsRunnable implements Runnable {

        private RtcQuery query;
        private ArrayList<String> alps;
        private ResultsNode n;
        private RtcQueryColumn[] rtcsc;

        public ShowQueryResultsRunnable(RtcQuery query) {
            this.query = query;
        }

        @Override
        public void run() {
            if (!EventQueue.isDispatchThread()) {
                ProgressHandle ph;
                ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ResultsTopComponent.class, "PreparingResultsWindow.msg"));
                ph.start();
                n = new ResultsNode(query);
                rtcsc = query.getColumns();
                List cols = Arrays.asList(rtcsc);
                Iterator colsIt = cols.iterator();
                alps = new ArrayList();
                while (colsIt.hasNext()) {
                    RtcQueryColumn rsc = (RtcQueryColumn) colsIt.next();
                    alps.add(rsc.getColumnIdentifier());
                    alps.add(rsc.getColumnDisplayName());
                }
                actualCols = alps.toArray(new String[]{});
                EventQueue.invokeLater(this);
                ph.finish();
            } else {
                outlineView.setPropertyColumns(alps.toArray(new String[]{}));
                Outline outline = outlineView.getOutline();
                removeDefaultColumn(outline);
                ETableColumnModel etcm = (ETableColumnModel) outline.getColumnModel();

                //TODO: szymon : change j to proper sort order
                int j = 1;
                for (int i = 1; i < rtcsc.length; i++) {
                    ETableColumn etc = (ETableColumn) etcm.getColumn(i);
                    if (alps.get(i * 2 - 2).equals("id")) {
                        etc.setMaxWidth(20);
                    }
                    if (alps.get(i * 2 - 2).equals("internalSeverity")) {
                        etc.setMaxWidth(20);
                    }
                    if (alps.get(i * 2 - 2).equals("internalPriority")) {
                        etc.setMaxWidth(20);
                    }
                    if (alps.get(i * 2 - 2).equals("workItemType")) {
                        etc.setMaxWidth(20);
                    }
                    if (alps.get(i * 2 - 2).equals("internalState")) {
                        etc.setMaxWidth(85);
                    }
                    if (alps.get(i * 2 - 2).equals("summary")) {
                        etc.setMaxWidth(550);
                        etc.setWidth(475);
                        etc.setMinWidth(400);
                    }
                    if (rtcsc[i].isSortColumn()) {
                        etcm.setColumnSorted(etc, rtcsc[i].isAscending(), j);
                        j++;
                    }
                }

                manager.setRootContext(n);
            }
        }
    }

    private class LabelProviderTableCellRenderer implements TableCellRenderer {

        private TableCellRenderer rend;

        LabelProviderTableCellRenderer(TableCellRenderer defaultRenderer) {
            this.rend = defaultRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        //System.out.println(table.getModel().getClass().getSimpleName());
            Object tmp = table.getModel().getValueAt(row, 0);
//        //System.out.println(Visualizer.findNode(tmp).getClass().getSimpleName());
            LabelSniffer ls = Visualizer.findNode(tmp).getLookup().lookup(LabelSniffer.class);
            try {
                if (ls != null) {
//                    //System.out.println(actualCols[(column-1) * 2]);
                    LabelProvider label;
                    if ((label = ls.findLabelProvider(actualCols[(column - 1) * 2])) != null) {
                        JLabel lab = label.getLabel();
                        lab.setOpaque(isSelected);
                        if (isSelected) {
                            lab.setBackground(table.getSelectionBackground());
                        }
                        return label.getLabel();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return new JLabel("tmp");
//            //System.out.println("bez labela dla "+actualCols[(column-1) * 2]);
//            Component comp = rend.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//            return comp;
        }
    }
}
//
//class LabelProvidersRowModel extends PropertiesRowModel {
//
//    @Override
//    public Class getColumnClass(int column) {
//        return LabelSniffer.class;
//    }
//
//    @Override
//    public Object getValueFor(Object node, int column) {
//        Node n = (Node) node;
//        LabelSniffer ls = n.getLookup().lookup(LabelSniffer.class);
//        return new JLabel("dziala label provider");
////        if(ls!=null){
////            return ls;
////        }else{
////            return super.getValueFor(node,column);
////        }
//    }
//
//    @Override
//    public boolean isCellEditable(Object node, int column) {
//        return false;
//    }
//}

class MouseListenerImpl implements MouseListener {

    private Outline outline;
    private long time;
    private int prevRow;
    private int prevCol;

    public MouseListenerImpl(Outline outline) {
        this.outline = outline;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = outline.rowAtPoint(e.getPoint());
        int col = outline.columnAtPoint(e.getPoint());
        if (prevCol == col && prevRow == row && System.currentTimeMillis() - time < 500) {
            Node node = Visualizer.findNode(outline.getValueAt(row, 0));
            time = 0;
            node.getPreferredAction().actionPerformed(null);
        } else {
            time = System.currentTimeMillis();
        }
        prevRow = row;
        prevCol = col;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}

class EmptyTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
//        JLabel lab = new JLabel(o != null ? o.getClass().getSimpleName() : "null");
        JLabel lab = new JLabel();
        lab.setOpaque(bln);
        if (bln) {
            lab.setBackground(jtable.getSelectionBackground());
        }
        return lab;
    }
}
