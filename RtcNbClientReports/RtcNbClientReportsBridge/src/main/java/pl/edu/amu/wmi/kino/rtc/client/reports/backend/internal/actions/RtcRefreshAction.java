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
package pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.actions;

import com.ibm.team.reports.common.IFolderDescriptor;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcRefreshAction implements RtcReportActionReference {
    private final IFolderDescriptor folder;

    public RtcRefreshAction(IFolderDescriptor folder) {
        this.folder = folder;

    }

    public String getDisplayName() {
        //TODO: I18N
        return "Refresh";
    }

    public String getIconBaseWithExtension() {
        return "pl/edu/amu/wmi/kino/rtc/client/reports/icons/refresh.gif";
    }

    public void performAction() {
        
    }

}
