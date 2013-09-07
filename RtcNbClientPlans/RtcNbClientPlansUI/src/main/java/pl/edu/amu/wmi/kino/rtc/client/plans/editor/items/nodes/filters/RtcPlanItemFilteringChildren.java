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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.filters;

import java.util.HashMap;
import java.util.List;

import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.RtcAbstractNode;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.utilities.RtcFilterNodeUtilities;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcPlanItemFilteringChildren extends ChildFactory<RtcPlanItem> {

    private Node root;
    private HashMap<RtcPlanItem, Node> map;
    List<RtcPlanItem> planItems;

    RtcPlanItemFilteringChildren(Node root, HashMap<RtcPlanItem, Node> map, List<RtcPlanItem> planItems) {
        this.root = root;
        this.map = map;
        this.planItems = planItems;
    }

    @Override
    protected boolean createKeys(List<RtcPlanItem> toPopulate) {
        List<RtcPlanItem> list =
                RtcFilterNodeUtilities.getPlanItems(root.getChildren().getNodes());

        for(RtcPlanItem planItem : list) {
            if(planItems.contains(planItem))
                toPopulate.add(planItem);
        }

       // //System.out.println("FilteringChildren: tworze dzieci " + toPopulate.size());

        return true;
    }

    @Override
    protected Node createNodeForKey(RtcPlanItem key) {

        Node node = map.get(key);
        
        if(!node.isLeaf() && node instanceof RtcAbstractNode) {
            Children ch = Children.create(new RtcPlanItemFilteringChildren(
                node, map, planItems), false);
            
           ((RtcAbstractNode) node).setNewChildren(ch);
        }

        return node;
    }
}
