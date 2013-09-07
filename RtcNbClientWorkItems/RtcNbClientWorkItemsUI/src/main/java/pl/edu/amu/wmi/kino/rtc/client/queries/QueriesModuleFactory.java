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
package pl.edu.amu.wmi.kino.rtc.client.queries;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcProjectAreaDependentModuleFactory;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManagerFactory;

/**
 * This implementation of RtcProjectAreaDependentModuleFactory simply creates WorkItems node.
 * <p>
 * It is registered in layer.xml in Rtc/Modules/ProjectAreaNodeFactories as QueriesModuleFactory.
 *</p>
 * @author Patryk Żywica
 */

public class QueriesModuleFactory implements RtcProjectAreaDependentModuleFactory {

    static QueriesModuleFactory factory;

    private QueriesModuleFactory() {
    }

    /**
     *
     * @return default singleton QueriesModuleFactory.
     */
    public static QueriesModuleFactory getDefault() {
        if (factory == null) {
            factory = new QueriesModuleFactory();
        }
        return factory;
    }

    /**
     * Returns nodes that will provide access to Queries Module functionality.
     *
     * @param area ActiveProjectArea object that will be used to provide ProjectArea
     * and TeamRepository.
     * @return nodes for Queries Module
     */
    @Override
    public Node[] createModuleNodes(ActiveProjectArea area) {
        InstanceContent ic = new InstanceContent();

        RtcQueriesManagerFactory f =
                Lookup.getDefault().lookup(RtcQueriesManagerFactory.class);
        if (f == null) {
            throw new IllegalStateException("No Rtc Queries Manager Factory found in lookup");
        }
        RtcQueriesManager manager = f.getManager(area);
        ic.add(manager);
        return new Node[]{new QueriesModuleNode(manager, ic)};
    }
}
