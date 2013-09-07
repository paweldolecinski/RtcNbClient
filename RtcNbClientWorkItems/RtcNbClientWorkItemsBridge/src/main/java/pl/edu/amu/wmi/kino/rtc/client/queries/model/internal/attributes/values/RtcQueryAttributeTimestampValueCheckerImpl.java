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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeTimestampValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.variables.TimestampVariableImpl;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryAttributeTimestampValueCheckerImpl implements RtcQueryAttributeTimestampValueChecker, ValueCreator {

    @Override
    public RtcQueryAttributeValueImpl getValue(Timestamp value) {
        return new TimestampValueImpl(value);
    }

    /**
     * It may return null if epmty value is selected
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Timestamp getTimestampRepresentation(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof TimestampValueImpl) {
            return ((TimestampValueImpl) value).getTimestamp();
        }
        throw new IllegalArgumentException(NbBundle.getMessage(RtcQueryAttributeTimestampValueCheckerImpl.class, "TimestampValueChecker.error"));
    }

    /**
     * Try to use getValue(Timestamp) instead this method
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public RtcQueryAttributeValue getValue(String value) throws IllegalArgumentException {
        if (value.equals("")) {
            return new TimestampValueImpl(null);
        } else {
            try {
                Date d = DateFormat.getInstance().parse(value);
                return new TimestampValueImpl(new Timestamp(d.getTime()));
            } catch (ParseException ex) {
                throw new IllegalArgumentException(NbBundle.getMessage(RtcQueryAttributeTimestampValueCheckerImpl.class, "TimestampValueChecker.error"), ex);
            }
        }
    }

    /**
     * Try to use String getRepresentation
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public String getRepresentation(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof TimestampValueImpl) {
            Timestamp ts = ((TimestampValueImpl) value).getTimestamp();
            if (ts != null) {
                return DateFormat.getInstance().format(new Date(ts.getTime()));
            } else {
                return "";
            }
        }
        if (value instanceof RtcQueryAttributeValueImpl) {
            RtcQueryAttributeValueImpl impl = (RtcQueryAttributeValueImpl) value;
            if (impl.getValue() == null) {
                return "";
            }
        }
        throw new IllegalArgumentException(NbBundle.getMessage(RtcQueryAttributeTimestampValueCheckerImpl.class, "TimestampValueChecker.error"));
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof Timestamp) {
            return new TimestampValueImpl((Timestamp) obj);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Pair<Integer, Unit> getRepresentation(RtcQueryAttributeVariable variable) throws IllegalArgumentException {
        if(variable instanceof TimestampVariableImpl){
            return new Pair<Integer, Unit>(
                    ((TimestampVariableImpl)variable).getTime(),
                    ((TimestampVariableImpl)variable).getUnit());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeVariable getVariable(int time, Unit unit) throws IllegalArgumentException {
        return new TimestampVariableImpl(time, unit);
    }
}

class TimestampValueImpl extends RtcQueryAttributeValueImpl {

    private Timestamp ts;

    public TimestampValueImpl(Timestamp ts) {
        super(ts);
        this.ts = ts;
    }

    public Timestamp getTimestamp() {
        return ts;
    }
}

