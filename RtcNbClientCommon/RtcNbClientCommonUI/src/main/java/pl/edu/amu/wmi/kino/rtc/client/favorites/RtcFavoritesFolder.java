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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder.RtcFavoritesFolderListener.RtcFavoritesFolderEvent;
import pl.edu.amu.wmi.kino.rtc.client.favorites.exceptions.NameAlreadyInUseException;

/**
 *
 * @author psychollek
 */
public class RtcFavoritesFolder extends AbstractList<RtcFavoritesFolder> {

    private final Set<RtcFavoritesFolderListener> listeners = Collections.synchronizedSet(new HashSet<RtcFavoritesFolderListener>(2));
    private final ArrayList<RtcFavoritesFolder> folders =
            new ArrayList<RtcFavoritesFolder>(2);
    private final HashSet<String> names = new HashSet<String>(2);
    private RtcFavoritesFolder parent = null;
    private final String name;

    public RtcFavoritesFolder(RtcFavoritesFolder parent,String name) {
        this.name = name;
        this.parent=parent;
    }

    @Override
    public RtcFavoritesFolder remove(int index) {
        RtcFavoritesFolder f = folders.remove(index);
        names.remove(f.getName());
        fireEvent(RtcFavoritesFolderEvent.ChildrenChanged);
        return f;
    }

    @Override
    public boolean add(RtcFavoritesFolder e) throws NameAlreadyInUseException {
        if (names.contains(e.getName())) {
            throw new NameAlreadyInUseException(e.getName());
        }
        if(!e.getParent().equals(this)){
            throw new  IllegalArgumentException("Invalid parent in given folder");
        }
        boolean toReturn = folders.add(e);
        if (toReturn) {
            names.add(e.getName());
            fireEvent(RtcFavoritesFolderEvent.ChildrenChanged);
        }
        return toReturn;
    }

    @Override
    public RtcFavoritesFolder get(int index) {
        return folders.get(index);
    }

    @Override
    public int size() {
        return folders.size();
    }

    public RtcFavoritesFolder getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        String path = "/f/" + getName();
        if (getParent() != null) {
            path = getParent().getPath() + path;
        }
        return path;
    }
//TODO : bikol : dojsc dlaczego to niedziala, moze wina java7 ? idzie wyjatek z childrena przy usowaniu folderu z childrenami
//    @Override
//    public boolean equals(Object o) {
//        if (o == null) {
//            return false;
//        }
//        if (o instanceof RtcFavoritesFolder && getPath().equals(((RtcFavoritesFolder) o).getPath())) {
//            return true;
//        }
//        return false;
//    }
    int hash = -666;
//
    @Override
    public int hashCode() {
        if (hash == -666) {
            hash = 7;
            hash = 61 * hash + getPath().hashCode();
        }
        return 666;
    }

    protected final void fireEvent(RtcFavoritesFolderEvent eventType) {
        for (Iterator<RtcFavoritesFolderListener> it = listeners.iterator(); it.hasNext();) {
            RtcFavoritesFolderListener listener = it.next();
            listener.fireEvent(eventType);
        }
    }

    public final void addRtcFavoritesFolderListener(RtcFavoritesFolderListener listener) {
        listeners.add(listener);
    }

    public final void removeRtcFavoritesFolderListener(RtcFavoritesFolderListener listener) {
        listeners.remove(listener);
    }

    public interface RtcFavoritesFolderListener {

        public void fireEvent(RtcFavoritesFolderEvent eventType);

        public enum RtcFavoritesFolderEvent {

            ChildrenChanged;
        }
    }
}
