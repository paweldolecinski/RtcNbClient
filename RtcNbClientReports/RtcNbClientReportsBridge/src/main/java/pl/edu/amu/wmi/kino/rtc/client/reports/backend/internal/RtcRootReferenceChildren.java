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

import pl.edu.amu.wmi.kino.rtc.client.reports.backend.internal.folders.RtcFoldertReference;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.reports.client.IReportManager;
import com.ibm.team.reports.common.IFolderDescriptor;
import com.ibm.team.reports.common.IReportQueryDescriptor;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildren;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReferenceChildrenListener;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcRootReferenceChildren implements RtcReportReferenceChildren {

    private final IFolderDescriptor folder;
    private final ActiveProjectArea activeProjectArea;

    public RtcRootReferenceChildren(IFolderDescriptor folder, ActiveProjectArea activeProjectArea) {
        this.folder = folder;
        this.activeProjectArea = activeProjectArea;
    }

    @Override
    public RtcReportReference[] getChildren() {
        List<RtcReportReference> list = new ArrayList<RtcReportReference>();
        ITeamRepository repo = ((ActiveProjectAreaImpl)activeProjectArea).getITeamRepository();
        IProjectArea projectArea = ((ActiveProjectAreaImpl)activeProjectArea).getProjectArea().getIProcessArea();

        IReportManager reportManager = (IReportManager) repo.getClientLibrary(IReportManager.class);
        try {

            if (folder.isShared()) {
                List<IFolderDescriptor> rootShared = reportManager.fetchFolderDescriptors(null, projectArea, null, folder, IReportManager.SHARED, null);
                for (IFolderDescriptor r : rootShared) {
                    list.add(new RtcFoldertReference(r, activeProjectArea));
                }
                List<IReportQueryDescriptor> fetchReportDescriptors = reportManager.fetchQueryDescriptors(null,
                        projectArea, null, null, folder, IReportManager.SHARED, null);
                for (IReportQueryDescriptor report : fetchReportDescriptors) {
                    list.add(new RtcReportReferenceImpl(report, folder));
                }
            } else {

                List<IFolderDescriptor> root = reportManager.fetchFolderDescriptors(repo.loggedInContributor(), projectArea, null, folder, IReportManager.PRIVATE, null);
                for (IFolderDescriptor r : root) {
                    list.add(new RtcFoldertReference(r, activeProjectArea));
                }

                List<IReportQueryDescriptor> fetchReportDescriptors = reportManager.fetchQueryDescriptors(repo.loggedInContributor(),
                        projectArea, null, null, folder, IReportManager.PRIVATE, null);
                for (IReportQueryDescriptor report : fetchReportDescriptors) {
                    list.add(new RtcReportReferenceImpl(report, folder));
                }


            }
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcRootReferenceChildren.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return list.toArray(new RtcReportReference[list.size()]);
    }

    @Override
    public void addListener(RtcReportReferenceChildrenListener listener) {
    }

    @Override
    public void removeListener(RtcReportReferenceChildrenListener listener) {
    }
}
