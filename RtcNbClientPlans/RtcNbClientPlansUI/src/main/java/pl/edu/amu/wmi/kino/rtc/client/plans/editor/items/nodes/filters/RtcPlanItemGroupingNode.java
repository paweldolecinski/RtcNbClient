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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.RtcAddNewPlanElementAction;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.RtcAbstractNode;

/**
 *
 * @author Michal Wojciechowski
 */
class RtcPlanItemGroupingNode extends RtcAbstractNode {

    private RtcPlanItemGroup group;
    HashMap<RtcPlanItem, Node> map;

    public RtcPlanItemGroupingNode(RtcPlanItemGroup group, HashMap<RtcPlanItem, Node> map, Lookup lookup, RtcPlanItemSorting sorting) {
        super(Children.LEAF, lookup);

        this.group = group;
        this.map = map;

        List<Object> list = new ArrayList<Object>();
        List<RtcPlanItem> itemsList = Arrays.asList(group.getPlanItems());
        Collections.sort(itemsList, sorting);
        list.addAll(itemsList);
        list.addAll(Arrays.asList(group.getChildGroups()));

        setChildren(Children.create(new RtcPlanItemGroupingChildren(map, list, sorting), false));
        setDisplayName(group.getGroupLabel());
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>();
        actions.add((new RtcAddNewPlanElementAction().createContextAwareInstance(getLookup())));
        Action[] tmp = super.getActions(context);
        for (int i = 0; i < tmp.length; i++) {
            actions.add(tmp[i]);
        }
        return actions.toArray(new Action[0]);
    }
}
