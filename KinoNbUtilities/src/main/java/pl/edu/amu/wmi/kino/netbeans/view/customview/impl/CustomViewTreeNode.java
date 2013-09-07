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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNode;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewTreeNode implements CustomViewNode, NodeListener, CustomViewNodeListener {

    private Node node;
    private CustomViewTreeNode[] children = null;
    private Set<CustomViewNodeListener> listeners =
            Collections.synchronizedSet(new HashSet<CustomViewNodeListener>());
    private boolean isExpanded = false;
    private CustomViewTreeNode parent;
    private Object modelLock;

    public CustomViewTreeNode(CustomViewTreeNode parent, Node node, Object modelLock) {
        this.node = node;
        this.parent = parent;
        this.modelLock = modelLock;
        node.addNodeListener(this);
    }

    public void expand() {
        if (!isExpanded) {
            List<CustomViewTreeNode> tmp = new ArrayList<CustomViewTreeNode>();
            for (Node n : node.getChildren().getNodes()) {
                CustomViewTreeNode newNode = new CustomViewTreeNode(this, n, modelLock);
                newNode.addTreeNodeListener(this);
                tmp.add(newNode);
            }
            synchronized (modelLock) {
                ////System.out.println("expand childrenow:" + tmp.size());
                children = tmp.toArray(new CustomViewTreeNode[]{});
                isExpanded = true;
                for (CustomViewNodeListener l : listeners) {
//                ////System.out.println("dodaje listenert w expandzie");
                    l.childrenInserted(1, getSize());
                }
            }
        }
    }

    public void collapse() {
        if (isExpanded) {
            synchronized (modelLock) {
//            ////System.out.println("collapse childrenow");
                children = null;

                isExpanded = false;
                for (CustomViewNodeListener l : listeners) {
//                ////System.out.println("dodaje listenert w expandzie");
                    l.childrenInserted(1, getSize());
                }
            }
        }
    }

    public boolean isExpanded() {
        synchronized (modelLock) {
            return isExpanded;
        }
    }

    public Node getNode() {
        return node;
    }

    public CustomViewTreeNode[] getChildren() {
        synchronized (modelLock) {
            return children;
        }
    }

    public int getSize() {
        synchronized (modelLock) {
            if (children == null) {
                return 1;
            } else {
                int res = 1;
                for (CustomViewTreeNode n : children) {
                    res += n.getSize();
                }
                return res;
            }
        }
    }

    public CustomViewNode getElementAt(int index) {
        synchronized (modelLock) {
//        ////System.out.println("node get element at : " + index + " rozmiar noda : " + getSize());
            if (index == 0) {
//            ////System.out.println("index 0");
                return this;
            } else {
                if (children.length != 0) {
                    if (index <= children[0].getSize()) {
                        return children[0].getElementAt(index - 1);
                    } else {
                        int tmp = 1;
                        int i = 0;
                        while (index >= tmp) {
//                        ////System.out.println("dodaje childreny o rozmiarze "+children[i].getSize());
                            tmp += children[i].getSize();
                            i++;
                        }
//                    ////System.out.println("koniec iter i=" + i);
                        if (i < 1 || i > children.length) {
                            return null;
                        } else {
                            return children[i - 1].getElementAt(index - (tmp - children[i - 1].getSize()));
                        }
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public void addTreeNodeListener(CustomViewNodeListener l) {
        listeners.add(l);
    }

    public void removeTreeNodeListener(CustomViewNodeListener l) {
        listeners.remove(l);
    }

    private void refreshChildren() {
        synchronized (modelLock) {
            List<CustomViewTreeNode> tmp = new ArrayList<CustomViewTreeNode>();
            for (Node n : node.getChildren().getNodes()) {
                CustomViewTreeNode newNode = new CustomViewTreeNode(this, n, modelLock);
                newNode.addTreeNodeListener(this);
                tmp.add(newNode);
            }
//        ////System.out.println("refresh childrenow:" + tmp.size());
            children = tmp.toArray(new CustomViewTreeNode[]{});
        }
    }

    @Override
    public void childrenAdded(NodeMemberEvent ev) {
        refreshChildren();
        for (CustomViewNodeListener l : listeners) {
            l.childrenInserted(0, getSize() - 1);
        }
    }

    @Override
    public void childrenRemoved(NodeMemberEvent ev) {
        refreshChildren();
        for (CustomViewNodeListener l : listeners) {
            l.childrenInserted(0, getSize() - 1);
        }
    }

    @Override
    public void childrenReordered(NodeReorderEvent ev) {
        refreshChildren();
        for (CustomViewNodeListener l : listeners) {
            l.childrenInserted(0, getSize() - 1);
        }
    }

    @Override
    public void nodeDestroyed(NodeEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        ////System.out.println("property event " + evt.getPropertyName());
        for (CustomViewNodeListener l : listeners) {
            l.childrenInserted(0, getSize() - 1);
        }
    }

    @Override
    public void childrenInserted(int startIndex, int endIndex) {
//        ////System.out.println("ooooo event e nodzie");
        for (CustomViewNodeListener l : listeners) {
            l.childrenInserted(0, getSize() - 1);
        }
    }

    @Override
    public CustomViewNode getParent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return node.isLeaf();
    }
}
