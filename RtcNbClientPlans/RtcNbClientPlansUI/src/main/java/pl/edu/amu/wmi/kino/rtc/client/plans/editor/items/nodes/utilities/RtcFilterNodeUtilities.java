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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcFilterNodeUtilities {

    public static HashMap<RtcPlanItem, Node> getPlanItemsMap(Node root) {

        HashMap<RtcPlanItem, Node> map = new HashMap<RtcPlanItem, Node>();
        //root.getChildren();
        
        ////System.out.println("NU: root " + root.getClass().getSimpleName() + ", jego dzieci: " + root.getChildren().getNodesCount(true));

        root = new FilterNode(root);
        for (Node node : root.getChildren().getNodes()) {
            ////System.out.println("NU: dziecko " + node.getClass().getSimpleName() + ", jego dzieci: " + node.getChildren().getNodesCount(true) + " czy jest lisciem: " + node.isLeaf());
            node = new FilterNode(node);

            RtcPlanItem planItem = node.getLookup().lookup(RtcPlanItem.class);
            if (planItem != null) {
                ////System.out.println("NU: dziecko posiada RtcPlanItem");
                map.put(planItem, node);
            }

            
            
            if (node.getChildren().getNodesCount() > 0) {
                ////System.out.println("NU: wchodzę w rekurencję ");
                HashMap<RtcPlanItem, Node> map1 = getPlanItemsMap(node);
                ////System.out.println("NU: dostalem nową mape " + map1.size());
                map.putAll(map1);
            }
        }
        return map;
    }

    public static List<RtcPlanItem> getPlanItems(Node[] nodes) {
        List<RtcPlanItem> list = new LinkedList<RtcPlanItem>();
        for (Node node : nodes) {
            RtcPlanItem planItem = node.getLookup().lookup(RtcPlanItem.class);
            if (planItem != null) {
                list.add(planItem);
            }
        }
        return list;
    }

    private RtcFilterNodeUtilities() {
    }
}
