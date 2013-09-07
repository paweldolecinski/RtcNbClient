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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;

/**
 * Provides ability to store references to various objects (resources)
 * in a convinient place. 
 * <p>
 * When invoking this service, one only has control over
 * what should be persisted, not how or where. 
 * </p>
 * <p>
 * For example:<br/>
 * when invoking addtoFavorites(workItemProvider,"10001",myProjectArea) - you specify,
 * that you want to persist "10001" from myProjectArea, and by implementing the
 * ResourceProvider to get workItems from passed projectArea, it will create
 * reference to the 10001 workItem in favorites.
 * </p><p>
 * On the side of the service is to ask user where he would like to store
 * the workItem (for example in some sort of folder structure if given provider
 * supports it), and how to persist information
 * about "10001" being connected to projectArea and recreated by workItemProvider.
 * </p>
 *
 * @author psychollek
 */
public interface RtcFavoritesService {

    /**
     * Stores the resource in favorites - it provides the user with
     * any necesary dialogs, etc. you can invoke this from AWT thread.
     * 
     * @param provider provider which will be used for restoring resource.
     * @param resourceId Id which will be later used by the provider to restore the resurce.
     * @param activeRepository repository connection from which the resource originates.
     */
    public void addToFavorites(RtcResourceProvider provider,
            String resourceId, ActiveRepository activeRepository);
}
