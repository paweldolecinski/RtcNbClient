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

import java.util.List;

import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildren;

/**
 *
 * @author psychollek
 */
public class RtcReportsChildrenFactory extends ChildFactory<RtcReportsRetriever.RtcReportReference> implements RtcReportsRetriever.RtcReportReferenceChildrenListener {
    
    private final RtcReportsRetriever.RtcReportReferenceChildren rtcReportsChildren;

    public RtcReportsChildrenFactory(RtcReportReferenceChildren children) {
        this.rtcReportsChildren = children;
        rtcReportsChildren.addListener(this);
    }

    @Override
    protected Node createNodeForKey(RtcReportReference key) {
        return new RtcReportNode(key);
    }

    @Override
    protected boolean createKeys(List<RtcReportReference> toPopulate) {
        RtcReportReference[] children = rtcReportsChildren.getChildren();
        for(RtcReportReference child : children){
            toPopulate.add(child);
        }
        return true;
    }

    @Override
    public void fireEvent(Event eventType) {
        switch (eventType) {
            case ChildrenChanged: {
                refresh(true);
                break;
            }
        }
    }

}
