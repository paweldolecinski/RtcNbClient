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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.progress;

import org.openide.util.NbBundle;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 * @author Pawel Dolecinski
 */
public class RtcProgressUtilities {

    public static double getUnitsBehind(RtcProgressInfo progress) {
        return progress.getDoneUnits() - progress.getPlannedUnits();
    }

    public static int getQualityOfPlanning(RtcProgressInfo progress) {
        if (progress.getPlannedItemsCount() < progress.getEstimatedItemsCount()) {
            return 100;
        }

        return Math.round((100 * (progress.getEstimatedItemsCount() / (float) progress.getPlannedItemsCount())));
    }

    public static String getTextQualityOfPlanning(RtcProgressInfo progress) {
        final int value = getQualityOfPlanning(progress);

        if (value < 33) {
            return NbBundle.getMessage(RtcProgressUtilities.class, "ProgressItems_QUALITY_POOR");
        }
        if (value >= 33 && value < 66) {
            return NbBundle.getMessage(RtcProgressUtilities.class, "ProgressItems_QUALITY_FAIR");
        }

        if (value >= 66 && value < 95) {
            return NbBundle.getMessage(RtcProgressUtilities.class, "ProgressItems_QUALITY_GOOD");
        }
        if (value >= 95) {
            return NbBundle.getMessage(RtcProgressUtilities.class, "ProgressItems_QUALITY_EXCELLENT");
        }
        return NbBundle.getMessage(RtcProgressUtilities.class, "ProgressItems_QUALITY_UNKNOWN");
    }

    public static long getLeftUnits(RtcProgressInfo progress) {
        return (long) (progress.getPlannedUnits() - progress.getDoneUnits());
    }

    public static String formatUnits(double value) {
        final StringBuilder text = new StringBuilder();

        if (value == 0) {
            if (value > 0) {
                text.append('<');
                text.append(1);
            } else {
                text.append(0);
            }
        } else if (value > 999) {
            text.append('>');
            text.append(999);
        } else {
            text.append(value);
        }

        return text.toString();
    }

    private RtcProgressUtilities() {
    }
}
