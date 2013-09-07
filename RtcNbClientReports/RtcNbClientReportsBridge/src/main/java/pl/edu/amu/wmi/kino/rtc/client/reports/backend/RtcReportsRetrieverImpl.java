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
package pl.edu.amu.wmi.kino.rtc.client.reports.backend;

import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.folders.RtcRootFoldertReference;
import com.ibm.team.reports.client.IReportManager;
import com.ibm.team.reports.common.IFolderDescriptor;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever;

/**
 * this class is an only backend for accessing reports. every access should be
 * done, by creatng an instance of this class and traversing through it's methods
 * - this will be the case at least until report generation/execution will be
 * supported directly by editors of some sort.
 * @author Adam Kędziora
 */
@ServiceProvider(service = RtcReportsRetriever.class)
public class RtcReportsRetrieverImpl implements RtcReportsRetriever {

    private ActiveProjectArea projectArea = null;

    /**
     * This method returns topmost report references factory. In orginal Eclipse
     * client those are : "My Reports", "Shared Reports", "Report Templates".
     * @return topmost references factory, not null.
     */
    @Override
    public RtcReportReferenceChildren getReports() {
        return new RtcReportReferenceChildren() {

            @Override
            public RtcReportReference[] getChildren() {
                List<RtcReportReference> childs = new ArrayList<RtcReportReference>();
                try {
                    IReportManager reportManager = (IReportManager) ((ActiveProjectAreaImpl)projectArea).getITeamRepository().getClientLibrary(IReportManager.class);
                    List<IFolderDescriptor> folders = reportManager.fetchRootFolderDescriptors(null, null, null, IReportManager.SYSTEM, null);
                    for (IFolderDescriptor folder : folders) {
                        childs.add(new RtcRootFoldertReference(folder, projectArea));
                    }


                } catch (TeamRepositoryException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcReportsRetrieverImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
                return childs.toArray(new RtcReportReference[childs.size()]);
            }

            @Override
            public void addListener(RtcReportReferenceChildrenListener listener) {
            }

            @Override
            public void removeListener(RtcReportReferenceChildrenListener listener) {
            }
        };
    }

    /**
     * This method will be called to get actions for popup menu of the main node.
     * results of this method will not be cached. do not return null, use
     * empty Array instead. Support lazy initialization if possible.
     * @return actions to use, not null.
     */
    @Override
    public RtcReportActionReference[] getActions() {
        return new RtcReportActionReference[0];
    }

    @Override
    public void setProjectArea(ActiveProjectArea projectArea) {
        this.projectArea = projectArea;
    }
}
