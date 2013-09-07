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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.RequestProcessor;
import org.openide.util.WeakListeners;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewNodeGroup implements NodeListener {

    private CustomViewNodeGroup parent;
    private Node mainNode;
    private boolean expanded = false;
    private ArrayList<CustomViewNodeGroup> children = new ArrayList<CustomViewNodeGroup>(10);
    private int size;
    private HashMap<Integer, CustomViewNodeGroup> cache = new HashMap<Integer, CustomViewNodeGroup>(5);
    private boolean modified = true;
    private final Object updateLock;

    public CustomViewNodeGroup(CustomViewNodeGroup parent, Node mainNode, Object lock) {
        this.parent = parent;
        this.mainNode = mainNode;
        this.updateLock = lock;
    }

    public Node getMainNode() {
        return mainNode;
    }

    public CustomViewNodeGroup getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return mainNode.isLeaf();
    }

    public boolean isExpanded() {
        synchronized (updateLock) {
            return expanded;
        }
    }

    public int getGroupSize() {
        synchronized (updateLock) {
            if (isModified()) {
                update();
            }
            return size;
        }
    }

    public CustomViewNodeGroup getNodeAt(int index) {
        synchronized (updateLock) {
            if (isModified()) {
                update();
            }
            if (index < getGroupSize()) {
                if (!cache.containsKey(index)) {
                    if (index == 0) {
                        cache.put(index, this);
                    } else {
                        int tmp = 0, i = 0;
                        int index2 = index - 1;
                        while (tmp <= index2 && i < children.size()) {
                            tmp += children.get(i).getGroupSize();
                            i++;
                        }
                        if (i <= children.size()) {
                            CustomViewNodeGroup gr = children.get(i - 1);
                            cache.put(index, gr.getNodeAt(index2 - (tmp - gr.getGroupSize())));
                        } else {
                            //this place should never be reached
                            throw new IllegalStateException();
                        }
                    }
                }
                return cache.get(index);
            } else {
                throw new IllegalArgumentException("Requested index : " + index + " is greater then group size : " + getGroupSize());
            }
        }
    }

    public void expand() {
        synchronized (updateLock) {
            if (!expanded) {
                for (Node n : mainNode.getChildren().getNodes()) {
                    children.add(new CustomViewNodeGroup(this, n, updateLock));
                }
                expanded = true;
                mainNode.addNodeListener(WeakListeners.create(NodeListener.class, this, mainNode));
                setModified();
            }
        }
    }

    public void collapse() {
        synchronized (updateLock) {
            if (expanded) {
                children.clear();
                expanded = false;
                mainNode.removeNodeListener(WeakListeners.create(NodeListener.class, this, mainNode));
                setModified();
            }
        }
    }

    private void update() {
        synchronized (updateLock) {
            if (isModified()) {
                int tmp = 0;
                for (CustomViewNodeGroup g : children) {
                    tmp += g.getGroupSize();
                }
                size = tmp + 1;

                cache.clear();
                modified = false;
            }
        }
    }

    protected void setModified() {
        synchronized (updateLock) {
            modified = true;
            if (getParent() != null) {
                getParent().setModified();
            }
        }
    }

    /**
     * Aplies only to size and cache. Does not aply to children.
     * @return true iff group was modified and cache or size needs to be updated
     */
    private boolean isModified() {
        synchronized (updateLock) {
            return modified;
        }
    }

    private void childrenChange() {
        //TODO : fix synchronization of get children
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run(){
            synchronized ( updateLock) {
                children.clear();
                if (expanded) {
                    for (Node n : mainNode.getChildren().getNodes()) {
                        children.add(new CustomViewNodeGroup(CustomViewNodeGroup.this, n, updateLock));
                    }
                }
                setModified();
            }

            }
        });
    }

    @Override
    public void childrenAdded(NodeMemberEvent ev) {
        childrenChange();
    }

    @Override
    public void childrenRemoved(NodeMemberEvent ev) {
        childrenChange();
    }

    @Override
    public void childrenReordered(NodeReorderEvent ev) {
        childrenChange();
    }

    @Override
    public void nodeDestroyed(NodeEvent ev) {
        children = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //
    }
}
