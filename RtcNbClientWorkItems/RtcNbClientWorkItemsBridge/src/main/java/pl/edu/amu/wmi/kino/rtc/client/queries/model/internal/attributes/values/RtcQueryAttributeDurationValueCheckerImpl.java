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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values;

import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeDurationValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcDurationValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryAttributeDurationValueCheckerImpl implements RtcQueryAttributeDurationValueChecker, ValueCreator {

    private String[] durTable = new String[4];
    private RtcDurationValueChecker valueChecker;

    public RtcQueryAttributeDurationValueCheckerImpl() {
        valueChecker = new RtcDurationValueChecker();

        durTable[0] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinutesShort");
        durTable[1] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHourShort");
        durTable[2] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationDayShort");
        durTable[3] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeekShort");
    }

    @Override
    public RtcQueryAttributeValue getValue(String value)
            throws IllegalArgumentException {
        if (value.equals("")) {
            return new DurationValueImpl(null);
        } else {
            if (valueChecker.isvalueProper(value)) {
                return new DurationValueImpl(valueChecker.convertToLong(value).getDurationInMillis());
            }
        }
        throw new IllegalArgumentException(
                NbBundle.getMessage(
                RtcQueryAttributeDurationValueCheckerImpl.class,
                "DurationValueChecker.error"));
    }

    @Override
    public String getRepresentation(RtcQueryAttributeValue value)
            throws IllegalArgumentException {
        if (value instanceof DurationValueImpl) {
            Long obj = ((DurationValueImpl) value).getLong();
            if (obj != null) {
                return valueChecker.convertToString(new RtcDurationImpl(obj));
            } else {
                return "";
            }
        }
        if (value instanceof RtcQueryAttributeValueImpl) {
            RtcQueryAttributeValueImpl impl = (RtcQueryAttributeValueImpl) value;
            if (impl.getValue() == null) {
                //for duration it should be empty string
                return "";
            }
        }
        throw new IllegalArgumentException(
                NbBundle.getMessage(
                RtcQueryAttributeDurationValueCheckerImpl.class,
                "DurationValueChecker.error"));

    }

    private Long setAsText(String s) throws IllegalArgumentException {
        s = changeString(s);
        long tempTime = 0;

        if (!s.isEmpty()) {
            if (valueChecker.isvalueProper(s)) {
                String tempNumber;
                int i = 0;
                while (i < s.length()) {
                    if (!s.substring(i, i + 1).matches("[0-9.]")) {
                        tempNumber = s.substring(0, i);
                        if (s.substring(i, i + 1).equals(durTable[0])) {
                            if (tempNumber.matches("[0-9]+.[0-9]+")) {
                                tempTime += (long) ((Float.valueOf(tempNumber).floatValue()) * 60000);
                            } else {
                                tempTime += Integer.parseInt(tempNumber) * 60000;
                            }
                            s = s.substring(i + 1);
                        } else if (s.substring(i, i + 1).equals(durTable[1])) {
                            if (tempNumber.matches("[0-9]+.[0-9]+")) {
                                tempTime += (long) ((Float.valueOf(tempNumber).floatValue()) * 3600000);
                            } else {
                                tempTime += Integer.parseInt(tempNumber) * 3600000;
                            }
                            s = s.substring(i + 1);
                        } else if (s.substring(i, i + 1).equals(durTable[2])) {
                            if (tempNumber.matches("[0-9]+.[0-9]+")) {
                                tempTime += (long) ((Float.valueOf(tempNumber).floatValue()) * 28800000);
                            } else {
                                tempTime += Integer.parseInt(tempNumber) * 28800000;
                            }
                            s = s.substring(i + 1);
                        } else if (s.substring(i, i + 1).equals(durTable[3])) {
                            if (tempNumber.matches("[0-9]+.[0-9]+")) {
                                tempTime += (long) ((Float.valueOf(tempNumber).floatValue()) * 144000000);
                            } else {
                                tempTime += Integer.parseInt(tempNumber) * 144000000;
                            }
                            s = s.substring(i + 1);
                        }
                        if (s.isEmpty()) {
                            break;
                        }
                        i = 0;
                    } else {
                        i++;
                    }
                }
                return tempTime;
            } else if (s.matches("[0-9]+.[0-9]+")) {
                tempTime += (long) ((Float.valueOf(s).floatValue()) * 3600000);
                return tempTime;
            } else if (s.matches("[0-9]+")) {
                tempTime += Integer.parseInt(s) * 3600000;
                return tempTime;
            } else {
                throw new IllegalArgumentException(
                        NbBundle.getMessage(
                        RtcQueryAttributeDurationValueCheckerImpl.class,
                        "DurationValueChecker.error"));
            }
        } else {
            throw new IllegalArgumentException(
                    NbBundle.getMessage(
                    RtcQueryAttributeDurationValueCheckerImpl.class,
                    "DurationValueChecker.error"));
        }
    }

    @SuppressWarnings("unused")
	private String getAsText(Long value) {
        int tempNumber = 0;
        String result = "";
        tempNumber = (int) (value / 144000000);
        if (tempNumber > 0) {
            result += tempNumber + " " + durTable[3];
            value -= 144000000 * tempNumber;
            if (value > 0) {
                result += " ";
            }
        }
        tempNumber = (int) (value / 28800000);
        if (tempNumber > 0) {
            result += tempNumber + " " + durTable[2];
            value -= 28800000 * tempNumber;
            if (value > 0) {
                result += " ";
            }
        }
        tempNumber = (int) (value / 3600000);
        if (tempNumber > 0) {
            result += tempNumber + " " + durTable[1];
            value -= 3600000 * tempNumber;
            if (value > 0) {
                result += " ";
            }
        }
        if (value > 0) {
            tempNumber = (int) (value / 60000);
            value -= (int) (60000 * tempNumber);
            if (value > 0) {
                float tempNumberF = (float) (value) / 60000;
                value -= (int) (60000 * tempNumberF);
                tempNumberF += tempNumber;
                result += tempNumberF + " " + durTable[0];
            } else {
                result += tempNumber + " " + durTable[0];
                value -= 60000 * tempNumber;
            }
        }
        return result;
    }

    private String changeString(String s) {

        String[] durTable2 = new String[11];
        durTable2[0] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinutesLong");
        durTable2[1] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinutesMid");
        durTable2[2] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationMinutesShort");
        durTable2[3] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHourLong");
        durTable2[4] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHourMid");
        durTable2[5] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationHourShort");
        durTable2[6] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationDayLong");
        durTable2[7] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationDayShort");
        durTable2[8] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeekLong");
        durTable2[9] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeekMid");
        durTable2[10] = NbBundle.getMessage(RtcDurationValueChecker.class, "DurationWeekShort");

        s = s.replace(durTable2[0], durTable2[2]);
        s = s.replace(durTable2[1], durTable2[2]);
        s = s.replace(durTable2[3], durTable2[5]);
        s = s.replace(durTable2[4], durTable2[5]);
        s = s.replace(durTable2[6], durTable2[7]);
        s = s.replace(durTable2[8], durTable2[10]);
        s = s.replace(durTable2[9], durTable2[10]);
        s = s.trim();
        s = s.replace(" ", "");
        s = s.replace(",", ".");

        return s;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof Long) {
            return new DurationValueImpl((Long) obj);
        }
        throw new IllegalArgumentException();
    }
}

class DurationValueImpl extends RtcQueryAttributeValueImpl {

    private Long val;

    public DurationValueImpl(Long rtcValue) {
        super(rtcValue);
        this.val = rtcValue;
    }

    public Long getLong() {
        return val;
    }
}
