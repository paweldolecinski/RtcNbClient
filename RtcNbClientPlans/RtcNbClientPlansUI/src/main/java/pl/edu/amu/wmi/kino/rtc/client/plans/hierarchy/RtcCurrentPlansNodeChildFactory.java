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
import java.util.logging.Level;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager.RtcPlansEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;

/**
 *
 * @author michu
 */
class RtcCurrentPlansNodeChildFactory extends ChildFactory
        implements EventListener<RtcPlansEvent> {

    private RtcPlansManager plansManager;
    private ProcessManager processManager;
    private ActiveProjectArea area;

    public RtcCurrentPlansNodeChildFactory(RtcPlansManager plansManager,
            ActiveProjectArea area, ProcessManager iterationsManager) {
        this.plansManager = plansManager;
        this.processManager = iterationsManager;
        this.area = area;
        this.plansManager.addListener(this);
    }

    @Override
    protected boolean createKeys(List list) {
        try {
            for (DevelopmentLine dl : processManager.getDevelopmentLines()) {
                Iteration[] currentIterations = new Iteration[]{processManager.getCurrentIteration(dl)};
                for (Iteration iter : currentIterations) {
                    list.addAll(Arrays.asList(plansManager.getPlansFor(iter)));
                }
            }
        } catch (Exception ex) {
            //e.printStackTrace();
            RtcLogger.getLogger(RtcCurrentPlansNodeChildFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Object t) {
        InstanceContent ic = new InstanceContent();
        ic.add(t);
        ic.add(area);
        ic.add(plansManager);
        return new RtcPlanNode(ic);
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
