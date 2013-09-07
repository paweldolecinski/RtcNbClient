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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcStatementCreationException;

/**
 * Implementations of this class will be used to represent editable queries.
 *
 * @see pl.edu.amu.wmi.kino.rtc.client.queries.model.RtcQuery
 * @author Patryk Żywica
 */
public interface RtcEditableQuery extends RtcQuery {

    /**
     *
     * This can be long running operation. Do not call on event dispatch thread.
     *
     * @see RtcQuery
     * @return
     */
    @Override
    public abstract RtcEditableStatement getStatement() throws RtcStatementCreationException;

    /**
     * Returns true if there are changes to this local object that are not saved on the
     * server, false otherwise.
     *
     * @return <code>true</code> if query is locally modified (it means
     * that not all changes are saved to Jazz server), <code>false</code> otherwise.
     */
    public abstract boolean isModified();

    /**
     * Saves all changes to this local query object to Jazz server.
     * <p>
     * If saving fails for any reason it throws <code>RtcSaveException<code>.
     * This method should be implemented in way that guarantee that if exception is thrown
     * no changes will be stored on Jazz server and if exception is not thrown then all
     * changes must be stored on Jazz server.
     * </p>
     * <p>
     * This can be long running operation. Do not call on event dispatch thread.
     * </p>
     * @throws RtcSaveException
     */
    public abstract void save() throws RtcSaveException;

    /**
     * Sets new editable name.
     *
     * You have to remember that this method will not affect to query name on Jazz server.
     * If you wand to so you have to invoke <code>save()</code> method.
     * @param new editable name
     */
    public abstract void setEditableName(String name);
}
