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
package pl.edu.amu.wmi.kino.rtc.client.favorites;

/**
 *
 * @author psychollek
 */
public class RtcFavoritesEntry extends RtcFavoritesFolder{
    private final String providerId;
    private final String resourceId;
    private final String repositoryConnectionId;

    public RtcFavoritesEntry(RtcFavoritesFolder parent,String providerId, String resourceId,String repositoryConnectionId) {
        super(parent,repositoryConnectionId+" "+providerId+" "+resourceId);
        this.providerId = providerId;
        this.resourceId = resourceId;
        this.repositoryConnectionId = repositoryConnectionId;
    }

    @Override
    public RtcFavoritesFolder remove(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean add(RtcFavoritesFolder e) {
        return false;
    }

    @Override
    public RtcFavoritesFolder get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String getPath() {
        String path = "/e/"+getName();
        if(getParent()!=null){
            path=getParent().getPath()+path;
        }
        return path;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getconnectionId() {
        return repositoryConnectionId;
    }


}
