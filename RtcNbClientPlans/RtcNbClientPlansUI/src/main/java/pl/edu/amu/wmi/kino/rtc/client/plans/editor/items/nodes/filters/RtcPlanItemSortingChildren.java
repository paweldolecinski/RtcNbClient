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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.RtcAbstractNode;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.utilities.RtcFilterNodeUtilities;



/**
 *
 * @author Michal Wojciechowski
 */
public class RtcPlanItemSortingChildren extends ChildFactory<Object> {

    private Node root;
    private HashMap<RtcPlanItem, Node> planItemMap;
    private HashMap<RtcPlanItemGroup, Node> groupMap = new HashMap<RtcPlanItemGroup, Node>();
    private RtcPlanItemSorting sorting;

    RtcPlanItemSortingChildren(Node root, HashMap<RtcPlanItem, Node> map, RtcPlanItemSorting sorting) {
        this.root = root;
        this.planItemMap = map;
        this.sorting = sorting;
    }

    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        List<RtcPlanItem> planItems =
                RtcFilterNodeUtilities.getPlanItems(root.getChildren().getNodes());

        Collections.sort(planItems, sorting);
        toPopulate.addAll(planItems);

        for(Node n : root.getChildren().getNodes()) {
            if(n instanceof RtcPlanItemGroupingNode) {
                //System.out.println("RR: Ciekawe, grupa ma podgrupę");
                RtcPlanItemGroup g = n.getLookup().lookup(RtcPlanItemGroup.class);
                groupMap.put(g, n);
                toPopulate.add(g);
            }
        }

        return true;
    }

    @Override
    protected Node createNodeForKey(Object object) {

        Node n = null;

        if(object instanceof RtcPlanItemGroup) {

            RtcPlanItemGroup group = (RtcPlanItemGroup) object;
            n = groupMap.get(group);

            Children c = Children.create(new RtcPlanItemSortingChildren(n, planItemMap, sorting), false);
            ((RtcAbstractNode) n).setNewChildren(c);
                    

        } else if(object instanceof RtcPlanItem) {
            
            RtcPlanItem planItem = (RtcPlanItem) object;
            n = planItemMap.get(planItem);
        }

        return new FilterNode(n);
    }
}