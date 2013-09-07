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
package pl.edu.amu.wmi.kino.netbeans.view.customview.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewListModel;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNode;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewListModelImpl implements CustomViewListModel, CustomViewTreeModelListener {

    private Node rootContext;
    private Set<ListDataListener> listeners = Collections.synchronizedSet(new HashSet<ListDataListener>());
    private CustomViewTreeModel treeModel;
    private Object modelLock = new Object();

    public CustomViewListModelImpl(Node rootContext) {
        this.rootContext = rootContext;
        treeModel = new CustomViewTreeModel(rootContext, modelLock);
        treeModel.addTreeModelListener(this);
    }

    @Override
    public int getSize() {
        return treeModel.getSize();
    }

    @Override
    public CustomViewNode getElementAt(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Negative index not allowed");
        }
//        ////System.out.println("get element at : "+index+" size : "+getSize());
        return treeModel.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void setRootVisible(boolean visible) {
//        treeModel.getElementAt(0).expand();
    }

    @Override
    public void rowsInserted(int[] indexed) {
        ////System.out.println("--------event list modelu");
        for (ListDataListener l : listeners) {
            l.contentsChanged(new ListDataEvent(l, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
        }
    }

    @Override
    public CustomViewNode[] getElements() {
        //TODO : bikol : temporary implementation
        synchronized (modelLock) {
            int n = getSize();
            CustomViewNode[] res = new CustomViewNode[n];
            for (int i = 0; i < n; i++) {
                res[i] = getElementAt(i);
            }
            return res;
        }
    }
}
