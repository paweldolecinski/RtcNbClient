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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;

/**
 * Class used for representing single query attribute.
 *<p>
 * Each query attribute has it own lookup that may contain additional features
 * available for that query attribute.
 *</p><p>
 * Instances of this class should be obtained from <code>RtcQueryAttributeManager</code>.
 * </p>
 * 
 * @author Patryk Żywica
 * @see RtcQueryAttributeManager
 */
public abstract class RtcQueryAttribute {

    /**
     * Should return localized display name of this query attribute. It should be used to
     * present this attribute to user.
     * @return display name for this query attribute.
     */
    public abstract String getDisplayName();

    /**
     *
     * @return attributeId from jazz server.
     */
    public abstract String getAttributeId();

    /**
     * Returns array of child attributes of this attribute.
     * Any query attribute may have child attributes. For example any contributor
     * query attribute has two child attributes: name and email.
     *
     * @return child attributes of this attribute.
     */
    public abstract RtcQueryAttribute[] getChildren();

    /**
     * 
     * @return type of this attribute.
     * @see RtcQueryAttributeType
     */
    public abstract RtcQueryAttributeType getType();

    /**
     * Each attribute has to provide at least one attribute operation.
     *
     * 
     * @return array of possible attribute operations never null or empty
     * @see RtcAttributeOperation
     */
    public abstract RtcAttributeOperation[] getAttributeOperations();

    /**
     * Returned array  may not contain all possible variables for this query attribute.
     *
     * For example relative date in any <code>Timestamp</code> query attribute is
     * stored as variable, but returning all possible relative date in array is impossible.
     *
     * @return array of standard variables available for this attribute. Not null.
     */
    public abstract RtcQueryAttributeVariable[] getAttributeVariables();

    /**
     * Returns lookup of this query attribute. It may contain many features available
     * for this attribute e.g. possible values, or value checkers.
     * 
     * @see RtcQueryAttributeValueProvider
     * @see RtcQueryAttributeValueChecker
     * @return lookup of this query attribute.
     */
    public abstract Lookup getLookup();

    /**
     * Return parent of this attribute. It may be <code>null</code> if this attribute
     * has no parent.
     *
     * @return parent attribute of this attribute. May be <code>null</code>.
     */
    public abstract RtcQueryAttribute getParent();

    /**
     * Query attribute type describes type of this attribute.
     */
    public enum RtcQueryAttributeType {

        /**
         * Type for all build in standard attributes.
         */
        BUILD_IN,
        /**
         * Type for all custom user or process defined attributes.
         */
        CUSTOM;
    }
}
