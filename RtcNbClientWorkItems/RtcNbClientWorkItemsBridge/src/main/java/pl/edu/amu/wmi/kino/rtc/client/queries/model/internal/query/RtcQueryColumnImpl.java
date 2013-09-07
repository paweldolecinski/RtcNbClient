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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.query;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryColumnImpl implements RtcQueryColumn {

    private boolean visible, sort, asc;
    private String id;
    private int sortIndex;
    private String name;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RtcQueryColumnImpl(String id,String name) {
        this(id,name, true, false, false, 0);
    }

    public RtcQueryColumnImpl(String id, String name, boolean isVisible, boolean isSortColumn, boolean isAscending, int sortIndex) {
        this.visible = isVisible;
        this.sort = isSortColumn;
        this.asc = isAscending;
        this.id = id;
        this.sortIndex = sortIndex;
        this.name=name;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean isSortColumn() {
        return sort;
    }

    @Override
    public boolean isAscending() {
        return asc;
    }

    @Override
    public int getSortIndex() {
        return sortIndex;
    }

    @Override
    public String getColumnIdentifier() {
        return id;
    }

    @Override
    public String getColumnDisplayName() {
//        return i
        return name;
    }
}
