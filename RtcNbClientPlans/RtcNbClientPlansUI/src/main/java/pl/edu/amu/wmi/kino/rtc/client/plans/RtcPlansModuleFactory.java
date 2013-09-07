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

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcProjectAreaDependentModuleFactory;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcProjectAreaDependentModuleFactory.class, path = "Rtc/Modules/ProjectAreaNodeFactories")
public class RtcPlansModuleFactory implements RtcProjectAreaDependentModuleFactory {

    @Override
    public Node[] createModuleNodes(ActiveProjectArea rtcActiveProjectArea) {
        RtcPlansManager manager = rtcActiveProjectArea.getLookup().lookup(RtcPlansManager.class);
        if (manager != null) {
            InstanceContent ic = new InstanceContent();
            ic.add(manager);
            ic.add(rtcActiveProjectArea);
            AbstractNode node = new RtcPlansModuleNode(ic);
            return new Node[]{node};
        }
        throw new IllegalStateException("There is no RtcIterationsManagerFactory in lookup");
    }
}
