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

import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNodeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNode;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewTreeModel implements CustomViewNodeListener {

    private Set<CustomViewTreeModelListener> listeners = Collections.synchronizedSet(new HashSet<CustomViewTreeModelListener>());
    private Node  root;
    private CustomViewTreeNode rootTreeNode;

    private Object modelLock;
    public CustomViewTreeModel(Node root,Object modelLock) {
        this.root=root;
        this.modelLock=modelLock;
        rootTreeNode = new CustomViewTreeNode(null,root,modelLock);
        rootTreeNode.addTreeNodeListener(this);
    }

    public void addTreeModelListener(CustomViewTreeModelListener l){
        listeners.add(l);
    }
    public void removeTreeModelListener(CustomViewTreeModelListener l){
        listeners.remove(l);
    }

    public int getSize(){
        return rootTreeNode.getSize();
    }

    public CustomViewNode getElementAt(int index){
        return rootTreeNode.getElementAt(index);
    }

    @Override
    public void childrenInserted(int startIndex, int endIndex) {
        ////System.out.println("++++event tree modelu");
        for(CustomViewTreeModelListener l : listeners){
            l.rowsInserted(new int[]{});
        }
    }

}
