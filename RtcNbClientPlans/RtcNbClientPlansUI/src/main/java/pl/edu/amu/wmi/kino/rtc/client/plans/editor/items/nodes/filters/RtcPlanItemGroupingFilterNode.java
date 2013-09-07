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

import java.util.Arrays;
import java.util.HashMap;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnProvider;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers.RtcDefaultViewColumnProvider;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.utilities.RtcFilterNodeUtilities;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcPlanItemGroupingFilterNode extends FilterNode {

    public RtcPlanItemGroupingFilterNode(Node root, RtcPlanItemViewMode viewMode) {
        super(root, createChildren(root, viewMode), createProxyLookup(root, viewMode));

        setDisplayName(viewMode.getGrouping().getDisplayName());
    }

    private static org.openide.nodes.Children createChildren(Node root, RtcPlanItemViewMode viewMode) {
        RtcPlanItemGrouping grouping = viewMode.getGrouping();
        RtcPlanItemSorting sorting = viewMode.getSorting();

        HashMap<RtcPlanItem, Node> map = RtcFilterNodeUtilities.getPlanItemsMap(root);
        RtcPlanItemGroup[] groups = grouping.groupItems(map.keySet().toArray(new RtcPlanItem[]{}), root.getLookup().lookup(RtcPlan.class));

        return org.openide.nodes.Children.create(new RtcPlanItemGroupingChildren(
                map, Arrays.asList(groups), sorting), false);
    }

    private static Lookup createProxyLookup(Node root, RtcPlanItemViewMode viewMode) {

        CustomViewColumnProvider columnProvider =
                new RtcDefaultViewColumnProvider(viewMode.getColumns());

        InstanceContent ic = new InstanceContent();
        ic.add(columnProvider);
        AbstractLookup al = new AbstractLookup(ic);

        ProxyLookup pl = new ProxyLookup(root.getLookup(), al);

        return pl;
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
