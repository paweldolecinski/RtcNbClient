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

/**
 * Represents a query attribute value that changes depending on context.
 * <p>
 * It means that variable is just like a value but its value depends on
 * server context.
 * </p><p>
 * For example currently selected contributor variable can represent currently logged in
 * contributor. Its value will change depending on currently login contributor.
 * </p>
 * 
 * @author Patryk Żywica
 */
public interface RtcQueryAttributeVariable {

    /**
     * Returns display name of this variable. It should be used to
     * present this variable to user.
     * @return display name for this variable.
     */
    String getDisplayName();
}
