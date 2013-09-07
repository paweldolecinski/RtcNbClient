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
package pl.edu.amu.wmi.kino.netbeans.view.customview.impl.table;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.openide.nodes.Node;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewTableModel implements TableModel {

    private CustomViewNodeGroup rootGroup;
    private Set<TableModelListener> listeners = Collections.synchronizedSet(new HashSet<TableModelListener>(5));

    private Object modelUpdateLock=new Object();

    public CustomViewTableModel(Node rootNode) {
        rootGroup = new CustomViewNodeGroup(new CustomViewRootNodeGroupParent(rootNode), rootNode,modelUpdateLock);
    }

    @Override
    public int getRowCount() {
        return rootGroup.getGroupSize();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "CustomViewMainColumn";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CustomViewNodeGroup.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public CustomViewNodeGroup getValueAt(int rowIndex, int columnIndex) {
        return rootGroup.getNodeAt(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    protected void fireEvent() {
        synchronized (listeners) {
            TableModelEvent e = new TableModelEvent(this);
            for (TableModelListener l : listeners) {
                l.tableChanged(e);
            }
        }
    }

    /**
     * this class is used to obtain set modified event from root group
     */
    class CustomViewRootNodeGroupParent extends CustomViewNodeGroup {

        public CustomViewRootNodeGroupParent(Node rootNode) {
            super(null, rootNode, modelUpdateLock);
        }

        @Override
        protected void setModified() {
            super.setModified();
            fireEvent();
        }
    }
}
