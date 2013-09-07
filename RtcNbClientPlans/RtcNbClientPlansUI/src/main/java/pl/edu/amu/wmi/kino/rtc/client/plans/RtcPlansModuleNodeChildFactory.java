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
package pl.edu.amu.wmi.kino.rtc.client.plans;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager.RtcPlansEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.plans.RtcPlansModuleNodeChildFactory.Type;
import pl.edu.amu.wmi.kino.rtc.client.plans.hierarchy.RtcAllPlansNode;
import pl.edu.amu.wmi.kino.rtc.client.plans.hierarchy.RtcCurrentPlansNode;

/**
 *
 * @author Patryk Żywica
 */
class RtcPlansModuleNodeChildFactory extends ChildFactory<Type> implements EventListener<RtcPlansEvent> {

    private ActiveProjectArea area;
    private RtcPlansManager plansManager;
    private ProcessManager iterationsManager;

    public RtcPlansModuleNodeChildFactory(RtcPlansManager manager, ActiveProjectArea rtcActiveProjectArea) {
        this.area = rtcActiveProjectArea;
        this.plansManager = manager;
        this.iterationsManager = area.getLookup().lookup(ProcessManager.class);
        manager.addListener(this);
    }

    @Override
    protected boolean createKeys(List<Type> list) {
        list.addAll(Arrays.asList(Type.values()));
        return true;
    }

    @Override
    protected Node createNodeForKey(Type t) {
        AbstractNode node;
        InstanceContent ic = new InstanceContent();
        switch (t) {
            case ALL:
                ic.add(area);
                ic.add(plansManager);
                node = new RtcAllPlansNode(ic);
                node.setDisplayName(NbBundle.getMessage(RtcPlansModuleNodeChildFactory.class, "AllPlansNode.name"));
                break;
            case CURRENT:
                ic.add(area);
                ic.add(plansManager);
                ic.add(iterationsManager);
                node = new RtcCurrentPlansNode(ic);
                node.setDisplayName(NbBundle.getMessage(RtcPlansModuleNodeChildFactory.class, "CurrentPlansNode.name"));
                break;
            default:
                node = new AbstractNode(Children.LEAF);
                break;
        }

        return node;
    }

    @Override
    public void eventFired(RtcPlansEvent event) {
        switch (event) {
            case PLANS_MANAGER_SYNCHRONIZED_WITH_SERVER:
            case PLAN_ADDED:
            case PLAN_REMOVED:
                refresh(true);
                break;
        }
    }

    enum Type {

        ALL,
        CURRENT;
    }
}
