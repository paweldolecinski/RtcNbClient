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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems;

/**
 *
 * @author Patryk Żywica
 */
public enum RtcWorkItemViewTarget {
    /**
     * Profile used to show/edit attribute in full work item editor (TopComponent).
     */
    MAIN,
    /**
     * Profile used to show/edit attribute in properties view.
     */
    PROPERTIES,
    /**
     * Profile used to show/edit attribute in preview (tooltip) window.
     */
    PREVIEW,
    /**
     * Profile used to show/edit attribute in plans editor
     */
    PLANS,
    /**
     * Profile used to show/edit attribute in query results window.
     */
    QUERIES,

}
