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
package pl.edu.amu.wmi.kino.rtc.client.connections.api;

import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
//TODO: javadoc: lack of author
/**
 * This interface is implemented by class which have to connect nodes from
 * others modules to the active area of project.
 * Implemented method returns these nodes that have to be connected to
 * area of project which is given as a parameter.
 *
 * Should be registered in Rtc/Modules/ProjectAreaNodeFactories
 */
public interface RtcProjectAreaDependentModuleFactory {

    Node[] createModuleNodes(ActiveProjectArea rtcActiveProjectArea);
}
