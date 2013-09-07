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
package pl.edu.amu.wmi.kino.rtc.client.favorites.api;

/**
 * Shall be thrown by getReference in case of problems during
 * retrieval of resource or creation of reference.
 * <p>
 * If resourceProvider believes
 * that the problem is not temporary (for example - connection has been succesful,
 * but the resource of the given Id has not been found), markForDelete shall be set
 * true, as a hint for favorites service to delete the entry.
 * </p>
 */
public class UnableToGetTheReferenceException extends Exception {

	private static final long serialVersionUID = -6301280790398423838L;
	private final boolean deleteFavoritesEntry;

    public UnableToGetTheReferenceException(String refName, boolean markForDelete) {
        super("reference " + refName + " could not be retrieved");
        deleteFavoritesEntry = markForDelete;
    }

    public boolean getDeleteFavoritesEntry() {
        return deleteFavoritesEntry;
    }
}
