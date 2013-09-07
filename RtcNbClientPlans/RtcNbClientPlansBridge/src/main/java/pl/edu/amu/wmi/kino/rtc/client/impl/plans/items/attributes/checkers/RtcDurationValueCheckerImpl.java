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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.checkers;


import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.checkers.RtcDurationValueChecker;

/**
 * This class will check that gives duration has proper value.
 * For example if server language is Polish and someone gives duration
 * time: 2d 3h (2days 3hours) isvalueProper method should return false.
 * Proper value in this case is 2d 3g (2 dni 3 godz)
 * @author Pawel Dolecinski
 */
public class RtcDurationValueCheckerImpl implements RtcDurationValueChecker {
    //TODO: Get hoursPerDay and daysPerWeek from repository (it's dynamic)

    Integer hoursPerDay = 8;
    Integer daysPerWeek = 5;
    String seconds, minutes, hours, days, weeks;

    public RtcDurationValueCheckerImpl() {

        seconds = NbBundle.getMessage(RtcDurationValueCheckerImpl.class, "DurationSecondRegex");
        minutes = NbBundle.getMessage(RtcDurationValueCheckerImpl.class, "DurationMinuteRegex");
        hours = NbBundle.getMessage(RtcDurationValueCheckerImpl.class, "DurationHourRegex");
        days = NbBundle.getMessage(RtcDurationValueCheckerImpl.class, "DurationDayRegex");
        weeks = NbBundle.getMessage(RtcDurationValueCheckerImpl.class, "DurationWeekRegex");
    }

    @Override
    public boolean isValueProper(Long value) {
        return true;
    }

    @Override
    public boolean isValueProper(String value) {
        if (value.matches("(?i)^(([0-9]+((\\.|,)[0-9]+)?\\s*(" + seconds + "|" + minutes + "|" + hours + "|" + days + "|" + weeks + "))\\s*)*$")) {
            return true;
        } else {
            return false;
        }

    }
}
