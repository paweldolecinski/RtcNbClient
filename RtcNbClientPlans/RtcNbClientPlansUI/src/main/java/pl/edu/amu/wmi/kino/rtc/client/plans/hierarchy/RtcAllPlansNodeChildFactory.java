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
package pl.edu.amu.wmi.kino.rtc.client.plans.hierarchy;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager.RtcPlansEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;

/**
 *
 * @author Patryk Żywica
 */
public class RtcAllPlansNodeChildFactory extends ChildFactory<DevelopmentLine>
        implements EventListener<RtcPlansEvent> {

    private ActiveProjectArea area;
    private RtcPlansManager manager;

    public RtcAllPlansNodeChildFactory(RtcPlansManager manager, ActiveProjectArea area) {
        this.area = area;
        this.manager = manager;
        this.manager.addListener(this);
    }

    @Override
    protected boolean createKeys(List<DevelopmentLine> list) {
        ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
        list.addAll(Arrays.asList(pm.getDevelopmentLines()));
        return true;
    }

    @Override
    protected Node createNodeForKey(DevelopmentLine t) {
        InstanceContent ic = new InstanceContent();
        ic.add(area);
        ic.add(manager);
        ic.add(t);
        return new RtcDevelopmentLineNode(ic);
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
}
