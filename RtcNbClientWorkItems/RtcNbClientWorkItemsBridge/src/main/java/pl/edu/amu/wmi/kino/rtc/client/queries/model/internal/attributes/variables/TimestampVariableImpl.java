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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.variables;

import com.ibm.team.workitem.common.expression.variables.RelativeDateVariable;
import com.ibm.team.workitem.common.expression.variables.RelativeDateVariable.TimeUnit;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeTimestampValueChecker.Unit;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.RtcQueryAttributeVariableImpl;

/**
 *
 * @author Patryk Żywica
 */
public class TimestampVariableImpl extends RtcQueryAttributeVariableImpl {

    private int time;
    private Unit unit;

    public TimestampVariableImpl(int time, Unit unit) {
        super(new RelativeDateVariable(RtcUnitToTimeUnit(unit), time));
        this.time = time;
        this.unit = unit;
    }

    public TimestampVariableImpl(RelativeDateVariable var){
        super(var);
        this.time=var.getTimeSpan();
        this.unit=timeUnitToRtcUnit(var.getTimeUnit());
    }

    public int getTime() {
        return time;
    }

    public Unit getUnit() {
        return unit;
    }

    private static Unit timeUnitToRtcUnit(TimeUnit unit) {
        switch (unit) {
            case DAYS_AGO:
                return Unit.DAYS_AGO;
            case DAYS_FROM_NOW:
                return Unit.DAYS_FROM_NOW;
            case HOURS_AGO:
                return Unit.HOURS_AGO;
            case HOURS_FROM_NOW:
                return Unit.HOURS_FROM_NOW;
            case MINUTES_AGO:
                return Unit.MINUTES_AGO;
            case MINUTES_FROM_NOW:
                return Unit.MINUTES_FROM_NOW;
            case MONTHS_AGO:
                return Unit.MONTHS_AGO;
            case MONTHS_FROM_NOW:
                return Unit.MONTHS_FROM_NOW;
            case YEARS_AGO:
                return Unit.YEARS_AGO;
            case YEARS_FROM_NOW:
                return Unit.YEARS_FROM_NOW;
            default:
                throw new IllegalArgumentException();
        }
    }

        private static TimeUnit RtcUnitToTimeUnit(Unit unit) {
        switch (unit) {
            case DAYS_AGO:
                return TimeUnit.DAYS_AGO;
            case DAYS_FROM_NOW:
                return TimeUnit.DAYS_FROM_NOW;
            case HOURS_AGO:
                return TimeUnit.HOURS_AGO;
            case HOURS_FROM_NOW:
                return TimeUnit.HOURS_FROM_NOW;
            case MINUTES_AGO:
                return TimeUnit.MINUTES_AGO;
            case MINUTES_FROM_NOW:
                return TimeUnit.MINUTES_FROM_NOW;
            case MONTHS_AGO:
                return TimeUnit.MONTHS_AGO;
            case MONTHS_FROM_NOW:
                return TimeUnit.MONTHS_FROM_NOW;
            case YEARS_AGO:
                return TimeUnit.YEARS_AGO;
            case YEARS_FROM_NOW:
                return TimeUnit.YEARS_FROM_NOW;
            default:
                throw new IllegalArgumentException();
        }
    }
}
