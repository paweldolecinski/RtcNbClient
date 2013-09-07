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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnRenderProvider;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers.RtcDefaultViewColumnRenderProvider;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanItemsRootChildren extends ChildFactory<RtcPlanItem> {

    private RtcPlan plan;

    public RtcPlanItemsRootChildren(RtcPlan plan) {
        this.plan = plan;
    }

    @Override
    protected boolean createKeys(List<RtcPlanItem> toPopulate) {
        toPopulate.addAll(Arrays.asList(plan.getPlanItemsManager().getPlanItems()));
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcPlanItem key) {
        InstanceContent ic = new InstanceContent();
        ic.add(key);
        ic.add(plan);

        RtcPlanItemNode node = new RtcPlanItemNode(ic);
        CustomViewColumnRenderProvider p = new RtcDefaultViewColumnRenderProvider(node);
        
        ic.add(p);
        
        return node;
    }
}
