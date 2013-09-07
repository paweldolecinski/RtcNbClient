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
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers.RtcGroupRowRenderProvider;

/**
 *
 * @author Michal Wojciechowski
 */
class RtcPlanItemGroupingChildren extends ChildFactory<Object> {

    private HashMap<RtcPlanItem, Node> map;
    private List<? extends Object> objects;
    private RtcPlanItemSorting sorting;

    public RtcPlanItemGroupingChildren(HashMap<RtcPlanItem, Node> map, List<? extends Object> objects, RtcPlanItemSorting sorting) {
        this.map = map;
        this.objects = objects;
        this.sorting = sorting;
        
    }

    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        toPopulate.addAll(objects);

        ////System.out.println("GroupingChildren: tworze dzieci " + toPopulate.size());
        return true;
    }

    @Override
    protected Node createNodeForKey(Object object) {

        Node n = null;
        InstanceContent ic = new InstanceContent();
        AbstractLookup lk = new AbstractLookup(ic);

        if(object instanceof RtcPlanItemGroup) {

            RtcPlanItemGroup group = (RtcPlanItemGroup) object;

            n = new RtcPlanItemGroupingNode(group, map, lk, sorting);
            RtcGroupRowRenderProvider p = new RtcGroupRowRenderProvider(group);
            ic.add(p);
            ic.add(group);

        } else if(object instanceof RtcPlanItem) {
            
            RtcPlanItem planItem = (RtcPlanItem) object;
            n = map.get(planItem);
        }
        return n;
    }
}
