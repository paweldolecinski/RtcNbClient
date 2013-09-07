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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDuration;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcDurationImpl extends RtcDuration {

    public static final RtcDuration NULL_VALUE = new RtcDurationImpl();
    /**
     *
     * @param duration
     */
    public RtcDurationImpl(long duration) {
        super(duration);
    }

    /**
     * For internal use.
     * 
     * @param stringDuration
     */
    public RtcDurationImpl(String stringDuration) {
        super(-1);
        duration = convertToLong(stringDuration);
    }

    /**
     * <p>Creats 'Null Value' duration. It means that {@link RtcDuration#getDuration()}
     * will return <code>-1</code>.</p>
     */
    public RtcDurationImpl() {
        super(-1);
    }

    @Override
    public double getDuration() {
        if (duration == -1) {
            return duration;
        }
        return duration / (1000 * 60 * 60);
    }

    @Override
    public String toString() {
        if (duration == -1) {
            return "--";
        }
        return convertToString(duration);
    }

    @Override
    public long getDurationInMillis() {
        return duration;
    }

    @Override
    protected final long convertToLong(String input) {
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
        return result;
    }

     private String convertToString(long miliseconds) {

        String secondsString = NbBundle.getMessage(RtcDurationImpl.class, "DurationSecond");
        String minutesString = NbBundle.getMessage(RtcDurationImpl.class, "DurationMinute");
        String hoursString = NbBundle.getMessage(RtcDurationImpl.class, "DurationHour");
        String daysString = NbBundle.getMessage(RtcDurationImpl.class, "DurationDay");
        String weeksString = NbBundle.getMessage(RtcDurationImpl.class, "DurationWeek");


        Long secs = (miliseconds / 1000);
        Long weeeeks = secs / (hoursPerDay * daysPerWeek * 3600);
        secs = secs - (weeeeks * (hoursPerDay * daysPerWeek * 3600));
        Long d = secs / (hoursPerDay * 3600);
        secs = secs - (d * (hoursPerDay * 3600));
        Long h = secs / 3600;
        secs = secs - (h * 3600);
        Long m = secs / 60;
        secs = secs - (m * 60);

        String result = "";
        if (weeeeks != 0) {
            result += weeeeks + " " + weeksString + " ";
        }
        if (d != 0) {
            result += d + " " + daysString + " ";
        }
        if (h != 0) {
            result += h + " " + hoursString + " ";
        }
        if (m != 0) {
            result += m + " " + minutesString + " ";
        }
        if (secs != 0) {
            result += secs + " " + secondsString;
        }

        return result.trim();
    }

    private static final String seconds  = NbBundle.getMessage(RtcDurationImpl.class, "DurationSecondRegex");
    private static final String minutes  = NbBundle.getMessage(RtcDurationImpl.class, "DurationMinuteRegex");
    private static final String hours  = NbBundle.getMessage(RtcDurationImpl.class, "DurationHourRegex");
    private static final String days  = NbBundle.getMessage(RtcDurationImpl.class, "DurationDayRegex");
    private static final String weeks  = NbBundle.getMessage(RtcDurationImpl.class, "DurationWeekRegex");
    private static final Integer hoursPerDay = 8;
    private static final Integer daysPerWeek = 5;
}
