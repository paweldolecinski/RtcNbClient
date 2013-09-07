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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.query;

/**
 *
 * @author Patryk Żywica
 */
public interface RtcQueryColumn {

//    /**
//     * Returns <code>RtcWorkItemAttribute</code> corresponding to this column.
//     *
//     * If you need to get display name for this column you can call
//     * <code>getWorkItemAttribute().getAttributeDisplayName</code>.
//     * @return <code>RtcWorkItemAttribute</code> corresponding to this column. Never <code>null</code>.
//     */
//    public RtcWorkItemAttribute getWorkItemAttribute();
    /**
     * Returned identifier should exactly point to <code>LabelProvoder</code>s
     * <code>String</code> argument.
     *
     * @return identifier of this query.
     */
    public String getColumnIdentifier();

    /**
     * 
     * @return display name of this column.
     */
    public String getColumnDisplayName();
    /**
     *
     * @return <code>true</code> if this column should be visible to user, <code>false</code> if not.
     */
    public boolean isVisible();

    /**
     *
     * @return true if this column should be used for sort.
     */
    public boolean isSortColumn();
    /**
     * E.g. if we have identifier column with sort index equals to 1 and 
     * work item type column with sort index set to 2, we have to sort values 
     * firstly by identifier and then by work item type.
     *
     * Value -1 means that this sort column is not sort column.
     *
     * @return integer that means this column index used for sorting.
     */
    public int getSortIndex();

    /**
     *Calling this method have sense only when <code>isSortColumn()</code> method
     * return <code>true</code>.
     *
     * @return <code>true</code> if this column should be sorted in ascending order, <code>false</code> otherwise.
     */
    public boolean isAscending();
    
    public int getSize();
}
