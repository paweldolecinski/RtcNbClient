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
package pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.folders;

import java.util.ArrayList;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildren;
import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.RtcRootReferenceChildren;
import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.actions.RtcDeleteFolderAction;

import com.ibm.team.reports.common.IFolderDescriptor;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcRootFoldertReference implements RtcReportReference {

    private final IFolderDescriptor folder;
    private final ActiveProjectArea projectArea;

    public RtcRootFoldertReference(IFolderDescriptor folder, ActiveProjectArea projectArea) {
        this.folder = folder;
        this.projectArea = projectArea;
    }

    public String getDisplayName() {
        return folder.getName();
    }

    public String getShortDescription() {
        return folder.getDescription();
    }

    public String getIconBaseWithExtension() {
        return "pl/edu/amu/wmi/kino/rtc/client/reports/icons/folder.gif";
    }

    public RtcReportActionReference[] getActions() {
        ArrayList<RtcReportActionReference> actions = new ArrayList<RtcReportActionReference>();

//        RtcRefreshAction refreshAction = new RtcRefreshAction(folder);
        RtcDeleteFolderAction deletehAction = new RtcDeleteFolderAction(folder, projectArea);
//        actions.add(refreshAction);
        //TODO : layer.xml
        actions.add(deletehAction);
        return actions.toArray(new RtcReportActionReference[actions.size()]);
    }

    public RtcReportActionReference getDefaultAction() {
        return null;
    }

    public RtcReportReferenceChildren getChildrenFactory() {
        return new RtcRootReferenceChildren(folder, projectArea);
    }
}
