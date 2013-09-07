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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.reports;

import com.ibm.team.apt.internal.client.IterationPlanData;
import com.ibm.team.apt.internal.ide.core.ResolvedIterationPlanRecord;
import com.ibm.team.process.common.IIteration;
import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.process.common.ITeamArea;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.process.common.ITeamAreaHierarchy;
import com.ibm.team.reports.client.IReportManager;
import com.ibm.team.reports.common.IReportDescriptor;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.transport.TeamContent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.RtcPlanImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor.ProgressMonitor;

/**
 * @see RtcPlanReport
 * @author Pawel Dolecinski
 */
public class RtcPlanReportImpl extends RtcPlanReport {

    /**
     * This only an empty stub of report. Used internaly if server return null
     * instead of report descriptor.
     */
    public static final RtcPlanReport EMPTY = new RtcPlanReportImpl();
    private final IReportDescriptor reportDescriptor;
    private final String name;

    /**
     *
     * @param reportDescriptor Never <code>null</code>.
     */
    public RtcPlanReportImpl(IReportDescriptor reportDescriptor) {
        this.reportDescriptor = reportDescriptor;
        this.name = reportDescriptor.getName();
    }

    private RtcPlanReportImpl() {
        reportDescriptor = null;
        this.name = "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getChartFor(RtcPlan plan) {
        if (reportDescriptor == null || ((RtcPlanImpl) plan).getPlanData() == null) {
            return null;
        }
        BufferedImage res = null;
        try {
            IterationPlanData data = ((RtcPlanImpl) plan).getPlanData();
            ResolvedIterationPlanRecord resolvedIterationPlan = new ResolvedIterationPlanRecord(data);
            final ITeamRepository repository = resolvedIterationPlan.getTeamRepository();
            List<IProcessArea> processAreas = resolvedIterationPlan.getMemberAreas();
            IProjectAreaHandle projectAreaHandle = resolvedIterationPlan.getProjectArea();
            IProjectArea projectArea = (IProjectArea) repository.itemManager().fetchCompleteItem(projectAreaHandle,
                    IItemManager.DEFAULT,
                    new ProgressMonitor());
            ITeamAreaHierarchy teamAreaHierarchy = projectArea.getTeamAreaHierarchy();
            List<String> teamAreaPaths = new ArrayList<String>();
            Iterator<IProcessArea> it = processAreas.iterator();
            while (it.hasNext()) {
                StringBuilder teamAreaPathBuilder = new StringBuilder();
                IProcessArea currentProcessArea = it.next();
                do {
                    CharSequence processName = currentProcessArea.getName();
                    for (int i = processName.length() - 1; i >= 0; i--) {
                        teamAreaPathBuilder.append(processName.charAt(i));
                    }
                    teamAreaPathBuilder.append('/');
                    if (!currentProcessArea.getProjectArea().getItemId().equals(currentProcessArea.getItemId())) {
                        ITeamAreaHandle parent = teamAreaHierarchy.getParent((ITeamArea) currentProcessArea);
                        if (parent == null) {
                            break;
                        }
                        currentProcessArea = (ITeamArea) repository.itemManager().fetchCompleteItem(parent,
                                IItemManager.DEFAULT,
                                new ProgressMonitor());
                    } else {
                        break;
                    }
                } while (true);
                teamAreaPaths.add(teamAreaPathBuilder.reverse().toString());
            }
            String charsetName = Charset.defaultCharset().name();
            String teamAreaParam = ""; //$NON-NLS-1$
            Iterator<String> pathIt = teamAreaPaths.iterator();
            while (pathIt.hasNext()) {
                teamAreaParam += URLEncoder.encode(
                        "'" //$NON-NLS-1$
                        + pathIt.next()
                        + "'", charsetName); //$NON-NLS-1$
                if (pathIt.hasNext()) {
                    teamAreaParam += ","; //$NON-NLS-1$
                }
            }

            String intervalParam = ""; //$NON-NLS-1$
            Iterator<IIteration> iterationIt = resolvedIterationPlan.getIterations().iterator();
            while (iterationIt.hasNext()) {
                intervalParam += URLEncoder.encode("'" //$NON-NLS-1$
                        + iterationIt.next().getName()
                        + "'", charsetName); //$NON-NLS-1$
                if (iterationIt.hasNext()) {
                    intervalParam += ","; //$NON-NLS-1$
                }
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("ProjectAreaName", "{Current Project Area}"); //$NON-NLS-1$ //$NON-NLS-2$
            parameters.put("TeamAreaName", teamAreaParam); //$NON-NLS-1$
            parameters.put("Interval", intervalParam); //$NON-NLS-1$
            try {
                IReportManager reportManager = (IReportManager) repository.getClientLibrary(IReportManager.class);
                TeamContent content = reportManager.getReportImage(
                        reportDescriptor,
                        "chart", parameters, new ProgressMonitor()); //$NON-NLS-1$

                res = ImageIO.read(content.getInputStream());

            } catch (Exception e) {
                RtcLogger.getLogger(RtcPlanReportImpl.class).log(Level.OFF, "Cannot get chart of {0} report for this plan.", getName().toLowerCase()); //$NON-NLS-1$
            }

        } catch (Exception e) {
            RtcLogger.getLogger(RtcPlanReportImpl.class).log(Level.OFF, "Cannot get chart of {0} report for this plan.", getName().toLowerCase()); //$NON-NLS-1$
        }
        finally{
            return res;
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }
}
