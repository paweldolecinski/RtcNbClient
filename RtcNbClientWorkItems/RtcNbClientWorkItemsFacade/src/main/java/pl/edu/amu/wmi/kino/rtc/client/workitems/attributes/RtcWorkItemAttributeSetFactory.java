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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes;

import org.openide.util.Lookup;

/**
 *
 * @author psychollek
 * @author Pawel Dolecinski
 */
public interface  RtcWorkItemAttributeSetFactory {

    /**
     * this method inspects the context for objects it needs.
     * @param context - context from the information about attributes should be retrieved
     * @return true if objects necesary for creation of Attribute set are present
     */
    public boolean canCreateFromContext(Lookup context);

    /**
     * this method creates RtcWorkItemAttributeSet from context provided. It can return null
     * if canCreateFromContext(context) = false.
     * @param context
     * @return set of attributes created from context or null if canCreateFromContext(context) = false;
     */
    public RtcWorkItemAttributeSet createAttributeSet(Lookup context);
}
