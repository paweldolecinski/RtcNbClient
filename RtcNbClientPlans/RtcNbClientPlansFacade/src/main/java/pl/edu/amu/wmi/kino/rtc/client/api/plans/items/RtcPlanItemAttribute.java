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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.items;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttribute;

/**
 * @param <T> 
 * @since 0.2.1.3
 * @author Patryk Żywica
 */
public abstract class RtcPlanItemAttribute<T> extends RtcAttribute{
/**
 * Returns special null value for this attribute. It will be used to indicate
 * unassigned or empty value of this attribute.
 *
 * <p>It may not be <code>null</code> value. Object returned by this method may not
 * change during this attribute life cycle.</p>
 *
 * @since 0.2.1.3
 * @return value that will represent empty, unassigned value of this attribute. May not be null.
 */
    public abstract T getNullValue();
    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract String getAttributeName();
    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract String getAttributeIdentifier();
    /**
     * @since 0.2.1.3
     * @return
     */
    public abstract Class<T> getValueType();

    /**
     * 
     * @return
     */
    public abstract boolean isReadOnly();
}
