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
import com.ibm.team.reports.common.IReportQueryDescriptor;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributorHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.HtmlBrowser;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcOpenReportAction implements RtcReportActionReference {

    private final IReportQueryDescriptor report;
    private final IFolderDescriptor folder;

    public RtcOpenReportAction(IReportQueryDescriptor report, IFolderDescriptor folder) {
        this.report = report;
        this.folder = folder;
    }

    public String getDisplayName() {
        //TODO: I18N
        return "Open";
    }

    public String getIconBaseWithExtension() {
        return "pl/edu/amu/wmi/kino/rtc/client/reports/icons/folder.gif";
    }

    public void performAction() {
        final RequestProcessor rp = new RequestProcessor("Open report");
        ProgressHandle progress = ProgressHandleFactory.createHandle("Report opening....", new Cancellable() {
            public boolean cancel() {
                rp.stop();
                return true;
            }
        });
        rp.post(new RunnableImpl(progress));

    }

    private static URL displayReport(IReportQueryDescriptor queryDescriptor,
            IContributorHandle contributor) throws MalformedURLException {
        String repoURI = ((ITeamRepository) queryDescriptor.getOrigin()).getRepositoryURI();
        if (!repoURI.endsWith("/")) { //$NON-NLS-1$
            repoURI += '/';
        }
        StringBuffer urlSuffix = new StringBuffer();
        try {
            urlSuffix.append("service/com.ibm.team.reports.service.internal.IReportViewerService?__queryUUID="); //$NON-NLS-1$
            urlSuffix.append(queryDescriptor.getItemId().getUuidValue());
            urlSuffix.append("&__ownerUUID="); //$NON-NLS-1$
            urlSuffix.append(contributor.getItemId().getUuidValue());
//            if (Util.showArchived()) {
//                urlSuffix.append("&__showArchived=true"); //$NON-NLS-1$
//            }

        } catch (Exception ex) {
            RtcLogger.getLogger(RtcOpenReportAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return new URL(repoURI
                + urlSuffix.toString());
    }

    private class RunnableImpl implements Runnable {

        private final ProgressHandle progress;

        public RunnableImpl(ProgressHandle progress) {
            this.progress = progress;
        }

        public void run() {
            try {
                //TODO: I18N
                progress.start(100);
                progress.progress("Getting report URL", 33); //TODO: I18N
                URL url = displayReport(report, folder.getOwner());
                progress.progress("Opening browser", 66); //TODO: I18N
                HtmlBrowser.URLDisplayer.getDefault().showURL(url);
                progress.finish();
            } catch (MalformedURLException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcOpenReportAction.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }
    }
}
