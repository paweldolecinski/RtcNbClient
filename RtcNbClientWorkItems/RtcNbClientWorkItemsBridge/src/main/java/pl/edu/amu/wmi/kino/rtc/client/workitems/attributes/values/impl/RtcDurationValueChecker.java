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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributeValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 * This class will check that gives duration has proper value.
 * For example if server language is Polish and someone gives duration
 * time: 2d 3h (2days 3hours) isvalueProper method should return false.
 * Proper value in this case is 2d 3g (2 dni 3 godz)
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcDurationValueChecker implements RtcWorkItemAttributeValueChecker<RtcDurationImpl> {
    //TODO: Get hoursPerDay and daysPerWeek from repository (it's dynamic)

    Integer hoursPerDay = 8;
    Integer daysPerWeek = 5;
    String seconds, minutes, hours, days, weeks;

    public RtcDurationValueChecker() {

        seconds = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationSecondRegex");
        minutes = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinuteRegex");
        hours = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHourRegex");
        days = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationDayRegex");
        weeks = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeekRegex");
    }

    @Override
    public boolean isvalueProper(RtcDurationImpl value) {
        return true;
    }

    public boolean isvalueProper(String value) {
        if (value.matches("(?i)^(([0-9]+((\\.|,)[0-9]+)?\\s*(" + seconds + "|" + minutes + "|" + hours + "|" + days + "|" + weeks + "))\\s*)*$")) {
            return true;
        } else {
            return false;
        }

    }

    public String convertToString(RtcDurationImpl miliseconds) {

        String secondsString = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationSecond");
        String minutesString = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinute");
        String hoursString = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHour");
        String daysString = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationDay");
        String weeksString = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeek");


        Long seconds = (miliseconds.getDurationInMillis() / 1000);
        Long weeks = seconds / (hoursPerDay * daysPerWeek * 3600);
        seconds = seconds - (weeks * (hoursPerDay * daysPerWeek * 3600));
        Long days = seconds / (hoursPerDay * 3600);
        seconds = seconds - (days * (hoursPerDay * 3600));
        Long hours = seconds / 3600;
        seconds = seconds - (hours * 3600);
        Long minutes = seconds / 60;
        seconds = seconds - (minutes * 60);

        String result = "";
        if (weeks != 0) {
            result += weeks + " " + weeksString + " ";
        }
        if (days != 0) {
            result += days + " " + daysString + " ";
        }
        if (hours != 0) {
            result += hours + " " + hoursString + " ";
        }
        if (minutes != 0) {
            result += minutes + " " + minutesString + " ";
        }
        if (seconds != 0) {
            result += seconds + " " + secondsString;
        }

        return result.trim();
    }

    public RtcDurationImpl convertToLong(String input) {
        input = " " + input.replaceAll(",", ".");
        Long result = new Long(0);
        Pattern weeksP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + weeks + ")");
        Matcher weeksM = weeksP.matcher(input);
        while (weeksM.find()) {


            Float weeksF = Float.parseFloat(weeksM.group(1));
            Float m = weeksF * 3600000;
            result += m.longValue() * daysPerWeek * hoursPerDay;
        }
        Pattern daysP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + days + ")");
        Matcher daysM = daysP.matcher(input);
        while (daysM.find()) {

            Float daysF = Float.parseFloat(daysM.group(1));

            Float m = daysF * 3600000;
            result += m.longValue() * hoursPerDay;
        }
        Pattern hoursP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + hours + ")");
        Matcher hoursM = hoursP.matcher(input);
        while (hoursM.find()) {

            Float hoursF = Float.parseFloat(hoursM.group(1));

            Float m = hoursF * 3600000;
            result += m.longValue();
        }
        Pattern minutesP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + minutes + ")");
        Matcher minutesM = minutesP.matcher(input);
        while (minutesM.find()) {

            Float minutesF = Float.parseFloat(minutesM.group(1));
            Float m = minutesF * 60000;
            result += m.longValue();
        }
        Pattern secondsP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + seconds + ")");
        Matcher secondsM = secondsP.matcher(input);
        while (secondsM.find()) {
            Float secondsF = Float.parseFloat(secondsM.group(1));
            Float m = secondsF * 1000;
            result += m.longValue();
        }
        return new RtcDurationImpl(result);
    }
}
