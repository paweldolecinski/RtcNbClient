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

/**
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public abstract class RtcComplexityComputator {

    /**
     *
     * @return
     */
    public abstract String getUnitDisplayName();

    /**
     *
     * @return
     */
    public abstract String getUnitShortDisplayName();

    /**
     * Converts given units to units used by complexity
     * i.e milisecunds to hours in hours complexity case.
     * @param units
     * @return
     */
    public abstract double computeComplexity(double units);
}
