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
package pl.edu.amu.wmi.kino.rtc.client.reports.gui;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcProjectAreaDependentModuleFactory;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildren;

/**
 *
 * @author psychollek
 */
@ServiceProvider(service = RtcProjectAreaDependentModuleFactory.class, path = "Rtc/Modules/ProjectAreaNodeFactories")
public class RtcReportsModuleFactory implements RtcProjectAreaDependentModuleFactory {

    public RtcReportsModuleFactory() {
    }

    @Override
    public Node[] createModuleNodes(ActiveProjectArea rtcActiveProjectArea) {
        final RtcReportsRetriever rtcReportsRetriever = Lookup.getDefault().lookup(RtcReportsRetriever.class);
        rtcReportsRetriever.setProjectArea(rtcActiveProjectArea);
        return new Node[]{new RtcReportNode(new RtcReportsRetriever.RtcReportReference() {

                @Override
                public String getDisplayName() {
                    return NbBundle.getMessage(RtcReportsModuleFactory.class, "RtcReportsNode.name");
                }

                @Override
                public String getShortDescription() {
                    return NbBundle.getMessage(RtcReportsModuleFactory.class, "RtcReportsNode.shortDescription");
                }

                @Override
                public String getIconBaseWithExtension() {
                    return "pl/edu/amu/wmi/kino/rtc/client/reports/icons/reports_node.gif";
                }

                @Override
                public RtcReportActionReference[] getActions() {
                    return rtcReportsRetriever.getActions();
                }

                @Override
                public RtcReportActionReference getDefaultAction() {
                    return null;
                }

                @Override
                public RtcReportReferenceChildren getChildrenFactory() {
                    return rtcReportsRetriever.getReports();
                }
            })};
    }
}
