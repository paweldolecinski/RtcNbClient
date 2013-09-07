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
package pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal;

import java.util.ArrayList;

import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildren;
import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.actions.RtcDeleteReportAction;
import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.actions.RtcOpenReportAction;

import com.ibm.team.reports.common.IFolderDescriptor;
import com.ibm.team.reports.common.IReportQueryDescriptor;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcReportReferenceImpl implements RtcReportReference {

    private IReportQueryDescriptor report;
    private final IFolderDescriptor folder;

    RtcReportReferenceImpl(IReportQueryDescriptor report, IFolderDescriptor folder) {
        this.report = report;
        this.folder = folder;
    }

    @Override
    public String getDisplayName() {
        return report.getName();
    }

    @Override
    public String getShortDescription() {
        return report.getDescription();
    }

    @Override
    public String getIconBaseWithExtension() {
        return "pl/edu/amu/wmi/kino/rtc/client/reports/icons/report.gif";
    }

    @Override
    public RtcReportActionReference[] getActions() {
        ArrayList<RtcReportActionReference> actions = new ArrayList<RtcReportActionReference>();

        RtcOpenReportAction openAction = new RtcOpenReportAction(report, folder);
        //TODO : layer.xml
//        RtcRefreshAction refreshAction = new RtcRefreshAction(folder);
        RtcDeleteReportAction deletehAction = new RtcDeleteReportAction(folder);
        actions.add(openAction);
//        actions.add(refreshAction);
        actions.add(deletehAction);
        return actions.toArray(new RtcReportActionReference[actions.size()]);
    }

    @Override
    public RtcReportActionReference getDefaultAction() {
        return new RtcOpenReportAction(report, folder);
    }

    @Override
    public RtcReportReferenceChildren getChildrenFactory() {
        return null;
    }
}
