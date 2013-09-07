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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process;

import java.util.Date;

/**
 * This abstract class represents IDeliverable class from RTC API
 * Deliverable this 'Found in' attribute.
 * @author Pawel Dolecinski
 * @author Patryk Żywica
 */
public interface Deliverable {

    /**
     * 
     * @return human readable name of deliverable object
     */
    abstract public String getName();

    /**
     * HTMLDescription this a short description for deliverable object.
     * In RTC calls HTML but this is just a String
     * @return decsiption of deliverable
     */
    abstract public String getHTMLDescription();

    /**
     * 
     * @return date of creation
     */
    abstract public Date getCreationDate();

    /**
     * 
     * @return true if archived or false if isn't
     */
    abstract public boolean isArchived();
}
