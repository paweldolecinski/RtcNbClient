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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values;

import java.awt.Image;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;

/**
 * Super interface for all value providers.
 *
 * Instances of this classes may be registered in query attribute's lookup to expose
 * new query feature.
 *
 * @see RtcQueryAttributePossibleValues
 * @see RtcQueryAttributePrefferedValues
 * @author Patryk Żywica
 */
public interface RtcQueryAttributeValueProvider {

    /**
     * This can be long running operation. Do not call on AWT event dispatch thread.
     * @return array of values provided by this provider
     */
    RtcQueryAttributeValue[] getValues();

    /**
     * Returns whatever this value can be selected or not.
     *
     * @param value to check for select-ability.
     * @return <code>true</code> if given value can be selected.
     * @throws IllegalArgumentException
     */
    boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException;

    /**
     * This method return icon for given value. It will always return non-null <code>Image</code>.
     * Returned image may be empty 1x1 transparent image.
     * @param value to get icon for
     * @return icon for given value. Never null.
     * @throws IllegalArgumentException
     */
    Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException;

    /**
     *
     * @param value to get display name for
     * @return display name  for given value
     * @throws IllegalArgumentException
     */
    String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException;

    /**
     * This can be long running operation. Do not call on AWT event dispatch thread.
     * 
     * @param value to get child values for.
     * @return array of children valid for given value. Not null, may be empty.
     * @throws IllegalArgumentException
     */
    RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException;
}
