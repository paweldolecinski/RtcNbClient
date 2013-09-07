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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.bars;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcComplexityComputator;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import org.openide.util.NbBundle;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcProgressBar extends RtcBar {

	private static final long serialVersionUID = 1636155804182268673L;
	private RtcProgressInfo progressInfo;
    private final RtcComplexityComputator complexityComputator;

    /**
     * 
     * @param progressInfo
     * @param complexityComputator
     * @param layout
     */
    public RtcProgressBar(RtcProgressInfo progressInfo, RtcComplexityComputator complexityComputator, Layout layout) {
        this.complexityComputator = complexityComputator;
        this.layout = layout;
        this.progressInfo = progressInfo;

        calculateData();
        initialize();
        fillText();
    }

    private void fillText() {

        String progressText = "<html>"
                + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.ProgressLabel") + toHours(progressInfo.getDoneUnits()) + "/" + toHours(progressInfo.getPlannedUnits());
        if (progressInfo.getExpectedUnits() > progressInfo.getDoneUnits()) {
            progressText += " | <font color=#FF0000>+" + toHours(progressInfo.getExpectedUnits() - progressInfo.getDoneUnits()) + "</font> ";
        }
        if (progressInfo.getExpectedUnits() < 0) {
            progressText += " | <font color=#008000>-" + toHours(-progressInfo.getExpectedUnits()) + "</font>";
        }
        progressText += complexityComputator.getUnitShortDisplayName() + "</html>";

        progressLabel.setText(progressText);
        estimateLabel.setText(NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.Estimated") + ": " + (int) (estimatePercent * 100) + "%");


        String toolTip;
        toolTip = "<html><body><strong>" + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.ReportProgress") + "</strong><br/>";
        toolTip += complexityComputator.getUnitDisplayName() + " " + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.NumberOfWorkedHours", toHours(progressInfo.getDoneUnits()), toHours(progressInfo.getPlannedUnits()));
        toolTip += "<br/><br/>";
        toolTip += NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.EstimatedItemCount") + ": " + (int) (estimatePercent * 100) + "%";
        toolTip += "<br/>";
        toolTip += NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.NumberOfCompletedItems", progressInfo.getCompletedItemsCount(), progressInfo.getPlannedItemsCount());
        toolTip += " (" + (int) (completedItemsPercent * 100) + "%)";

        toolTip += "</body></html>";


        setToolTipText(toolTip);
    }

    private void calculateData() {
        greenBar = (double) progressInfo.getDoneUnits() / progressInfo.getPlannedUnits();
        if (progressInfo.getExpectedUnits() > progressInfo.getDoneUnits()) {
            redBar = (double) (progressInfo.getExpectedUnits() - progressInfo.getDoneUnits()) / progressInfo.getPlannedUnits();
        }
        if (progressInfo.getExpectedUnits() < 0) {
            lightBar = (double) -progressInfo.getExpectedUnits() / progressInfo.getPlannedUnits();
        }
        completedItemsPercent = (double) progressInfo.getCompletedItemsCount() / progressInfo.getPlannedItemsCount();
        estimatePercent = (double) progressInfo.getEstimatedItemsCount() / progressInfo.getPlannedItemsCount();
    }
}
