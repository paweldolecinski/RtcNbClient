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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.load;

import org.openide.util.NbBundle;

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 * @author Pawel Dolecinski
 */
public class RtcLoadUtilities {

    public static double getHoursOverbooked(RtcLoadInfo load) {
        return load.getWorkHoursAvailable() - load.getWorkHoursUsed();
    }

    public static int getQualityOfPlanning(RtcLoadInfo load) {
        if (load.getOpenItemsCount() < load.getEstimatedItemsCount()) {
            return 100;
        }

        if (load.getOpenItemsCount() == 0) {
            return 100;
        }

        return Math.round((100 * (load.getEstimatedItemsCount() / (float) load.getOpenItemsCount())));
    }

    public static String getTextQualityOfPlanning(RtcLoadInfo load) {
        final int value = getQualityOfPlanning(load);

        if (value < 33) {
            return NbBundle.getMessage(RtcLoadUtilities.class, "LoadItems_QUALITY_POOR");
        }
        if (value >= 33 && value < 66) {
            return NbBundle.getMessage(RtcLoadUtilities.class, "LoadItems_QUALITY_FAIR");
        }

        if (value >= 66 && value < 95) {
            return NbBundle.getMessage(RtcLoadUtilities.class, "LoadItems_QUALITY_GOOD");
        }
        if (value >= 95) {
            return NbBundle.getMessage(RtcLoadUtilities.class, "LoadItems_QUALITY_EXCELLENT");
        }
        return NbBundle.getMessage(RtcLoadUtilities.class, "LoadItems_QUALITY_UNKNOWN");

    }

    public static String formatHours(double value) {
        final StringBuilder text = new StringBuilder();
        long hour = Math.round(value);

        if (hour == 0) {
            if (value > 0) {
                text.append('<');
                text.append(1);
            } else {
                text.append(0);
            }
        } else if (hour > 999) {
                text.append('>');
                text.append(999);
        } else {
            text.append(hour);
        }

        return text.toString();
    }

    private RtcLoadUtilities() {
    }
}
