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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.load.RtcLoadInfo;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcLoadBar extends RtcBar {

	private static final long serialVersionUID = -5523558311670693575L;
	private RtcLoadInfo loadInfo;

    public RtcLoadBar(RtcLoadInfo loadInfo, Layout layout) {
        this.layout = layout;
        this.loadInfo = loadInfo;

        calculateData();
        initialize();
        fillText();
    }

    private void fillText() {
    /*
        String progressText = "<html>"
                + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.ProgressLabel", toHours(loadInfo.getDoneUnits()), toHours(loadInfo.getPlannedUnits()));
        if (loadInfo.getExpectedUnits() > loadInfo.getDoneUnits()) {
            progressText += " | <font color=#FF0000>+" + toHours(loadInfo.getExpectedUnits() - loadInfo.getDoneUnits()) + "</font> " + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.ShortHours");
        }
        if (loadInfo.getExpectedUnits() < 0) {
            progressText += " | <font color=#008000>-" + toHours(-loadInfo.getExpectedUnits()) + "</font> h";
        }
        progressText += "</html>";

        progressLabel.setText(progressText);
        estimateLabel.setText(NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.Estimated") + ": " + (int) (estimatePercent * 100) + "%");

        String toolTip;
        toolTip = "<html><body><strong>" + NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.ReportProgress") + "</strong><br/>";
        toolTip += NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.NumberOfWorkedHours", toHours(loadInfo.getDoneUnits()), toHours(loadInfo.getPlannedUnits()));
        toolTip += "<br/><br/>";
        toolTip += NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.EstimatedItemCount") + ": " + (int) (estimatePercent * 100) + "%";
        toolTip += "<br/>";
        toolTip += NbBundle.getMessage(RtcProgressBar.class, "RtcProgressBar.NumberOfCompletedItems", loadInfo.getCompletedItemsCount(), loadInfo.getPlannedItemsCount());
        toolTip += " (" + (int) (completedPercent * 100) + "%)";

        toolTip += "</body></html>";


        setToolTipText(toolTip);*/
    }

    private void calculateData() {
        /*
        greenBar = (double) loadInfo.getWorkHoursUsed() / loadInfo.getWorkHoursAvailable();
        if (loadInfo.get() > loadInfo.getDoneUnits()) {
            redBar = (double) (loadInfo.getExpectedUnits() - loadInfo.getDoneUnits()) / loadInfo.getPlannedUnits();
        }
        if (loadInfo.getExpectedUnits() < 0) {
            lightBar = (double) -loadInfo.getExpectedUnits() / loadInfo.getPlannedUnits();
        }

        estimatePercent = (double) loadInfo.getEstimatedItemsCount() / loadInfo.getPlannedItemsCount();
        completedPercent = (double) loadInfo.getDoneUnits() / loadInfo.getPlannedUnits();
         * 
         */
    }

}
