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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.checker;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.openide.util.ImageUtilities;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemCheckerUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;

/**
 *
 * @author Patryk Żywica
 */
class RtcPlanItemCheckerPopupComponent extends JLabel {

    private static final long serialVersionUID = 235252351L;

    RtcPlanItemCheckerPopupComponent(RtcPlanItem planItem, RtcPlanItemChecker[] checkers) {
        super(getIconFor(planItem, checkers));
        String tip = "";
        for (RtcPlanItemChecker ch : checkers) {
            for (RtcPlanItemProblem p : ch.getProblems()) {
                if (p.getPlanItem().equals(planItem)) {
                    tip += "<p>" + p.getMessage() + "</p>";
                }
            }
            setToolTipText("<html>" + tip + "</html>");
        }
    }

    RtcPlanItemCheckerPopupComponent(RtcPlanItemChecker[] checkers) {
        super();
        RtcPlanItem planItem = null;
        RtcPlanItemProblem.RtcSeverity maxSeverity = null;
        String tip = "";
        for (RtcPlanItemChecker ch : checkers) {
            for (RtcPlanItemProblem p : ch.getProblems()) {
                if (planItem != null && maxSeverity != null) {
                    if (RtcPlanItemCheckerUtilities.compareSeverities(maxSeverity, p.getSeverity()) > 0) {
                        planItem = p.getPlanItem();
                        maxSeverity = p.getSeverity();
                    }
                } else {
                    planItem = p.getPlanItem();
                    maxSeverity = p.getSeverity();
                }

                tip += "<p>" + p.getMessage() + "</p>";
            }
        }
        if (planItem != null) {
            setIcon(getIconFor(planItem, checkers));
        } else {
            setIcon(null);
        }

        setToolTipText("<html>" + tip + "</html>");
    }

    private static ImageIcon getIconFor(RtcPlanItem planItem, RtcPlanItemChecker[] checkers) {
        RtcPlanItemProblem.RtcSeverity max =
                RtcPlanItemCheckerUtilities.getHighestPlanItemProblemSeverity(planItem, checkers);
        switch (max) {
            case FATAL_ERROR:
            case ERROR:
                return ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/checker/resources/checkError.gif", false);
            case WARNING:
                return ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/checker/resources/checkWarning.gif", false);
            case NONE:
            case INFO:
            default:
                return null;
        }
    }
}
