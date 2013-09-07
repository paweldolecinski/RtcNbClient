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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types;

/**
 *
 * @author Pawel Dolecinski
 */
public abstract class RtcDuration {

    /**
     *
     */
    protected String durationValue;
    /**
     * 
     */
    protected long duration;


    /**
     * You can create new RtcDuration object giving new duration value.
     *
     * @param duration value in milisecunds
     */
    public RtcDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Returns duration in hours.
     *
     * @return duration
     */
    public abstract double getDuration();

    /**
     * If you have duration value as a string you can use this setter to
     * set new value of duration.
     * Duration value string should be a in correct format.
     *
     * @param newDuration
     */
    public final void setDuration(String newDuration) {
        durationValue = newDuration;
        duration = convertToLong(durationValue);
    }

    /**
     * Returns duration in milisecunds.
     *
     * @return
     */
    public abstract long getDurationInMillis();

    @Override
    public abstract String toString();

    /**
     * This method converts string value of duration to milisecunds.
     * 
     * @param input
     * @return
     */
    protected abstract long convertToLong(String input);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RtcDuration other = (RtcDuration) obj;
        if ((this.durationValue == null) ? (other.durationValue != null) : !this.durationValue.equals(other.durationValue)) {
            return false;
        }
        if (this.duration != other.duration) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.durationValue != null ? this.durationValue.hashCode() : 0);
        hash = 23 * hash + (int) (this.duration ^ (this.duration >>> 32));
        return hash;
    }

    
}
