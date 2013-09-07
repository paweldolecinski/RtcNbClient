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
package pl.edu.amu.wmi.kino.rtc.client.ui.queries.result;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.explorer.view.Visualizer;
import org.openide.nodes.Node;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;

/**
 *
 * @author Patryk Żywica
 */
public class ResultsTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static final long serialVersionUID = 7581543L;
    private static final RequestProcessor rp = new RequestProcessor(ResultsTopComponent.class);
    private static final Map<String, ResultsTopComponent> tcs = Collections.synchronizedMap(new HashMap<String, ResultsTopComponent>(4));
    private ExplorerManager manager = new ExplorerManager();
    private OutlineView view;
    private RtcQuery query;

    public ResultsTopComponent(RtcQuery query) {
        this.query = query;
        setDisplayName("Workitems: " + query.getName());
        associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
        setLayout(new BorderLayout());
        view = new OutlineView("");
        view.getOutline().setRootVisible(false);

        OpenWIListenerImpl listener = new OpenWIListenerImpl(view.getOutline());
        view.getOutline().addMouseListener(listener);
        view.getOutline().addKeyListener(listener);
        add(view, BorderLayout.CENTER);
        setFocusable(true);
    }

    /**
     * This method is responsible for finding a <code>ResultsTopComponent</code>
     * instance which belongs to given query. Remember to call
     * <code>refreshResults()</code> method after obtaining TopComponent.
     *
     * @param query
     * @return
     */
    public static ResultsTopComponent findInstanceFor(RtcQuery query) {
        ResultsTopComponent tc = tcs.get(query.getQueryIdentifier());
        if (tc == null) {
            tc = new ResultsTopComponent(query);
            tcs.put(query.getQueryIdentifier(), tc);
        }
        return tc;
    }

    public void refreshResults() {
        manager.setRootContext(new ResultsNode(query));
        rp.post(new Runnable() {

            private String[] cols;
            private RtcQueryColumn[] c;
            public void run() {
                if (!EventQueue.isDispatchThread()) {
                    c = query.getColumns();
                    cols = new String[c.length * 2];
                    for (int i = 0; i < cols.length; i += 2) {
                        cols[i] = c[i / 2].getColumnIdentifier();
                        cols[i + 1] = c[i / 2].getColumnDisplayName();
                    }
                    EventQueue.invokeLater(this);
                } else {
                    view.setPropertyColumns(cols);
                    view.getOutline().getColumnModel().getColumn(0).setMaxWidth(0);
                    view.getOutline().getColumnModel().getColumn(0).setWidth(0);
                    view.getOutline().getColumnModel().getColumn(0).setResizable(false);
                    int total=0;
                    for(RtcQueryColumn cc:c){
                        total+=cc.getSize();
                    }
                    for (int i = 0; i < c.length; i++) {
                        view.getOutline().getColumnModel().getColumn(i+1).setWidth((int)( ((double)c[i].getSize()/(double)total)*(double)view.getOutline().getWidth() ));
                        view.getOutline().getColumnModel().getColumn(i+1).setPreferredWidth((int)( ((double)c[i].getSize()/(double)total)*(double)view.getOutline().getWidth() ));
                        
                        System.out.println((int)( ((double)c[i].getSize()/(double)total)*(double)view.getOutline().getWidth() )+"/"+view.getOutline().getWidth());
                    }
                }
            }
        });
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        tcs.remove(query.getQueryIdentifier());
    }

    @Override
    protected String preferredID() {
        return "QueryResultsTopComponent:" + query.getQueryIdentifier();
    }

    private static class OpenWIListenerImpl extends MouseAdapter implements KeyListener {

        private Outline outline;
        private long time;
        private int prevRow;
        private int prevCol;

        OpenWIListenerImpl(Outline outline) {
            this.outline = outline;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = outline.rowAtPoint(e.getPoint());
            int col = outline.columnAtPoint(e.getPoint());
            if (prevCol == col && prevRow == row && System.currentTimeMillis() - time < 500) {
                openWI(row);
                time = 0;
            } else {
                time = System.currentTimeMillis();
            }
            prevRow = row;
            prevCol = col;
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                outline.getSelectedRow();
                openWI(outline.getSelectedRow());
            }
        }

        private void openWI(int row) {
            Node node = Visualizer.findNode(outline.getValueAt(row, 0));
            node.getPreferredAction().actionPerformed(null);
        }
    }
}
