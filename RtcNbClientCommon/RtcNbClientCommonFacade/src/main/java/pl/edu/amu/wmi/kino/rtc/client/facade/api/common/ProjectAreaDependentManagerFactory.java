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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common;

/**
 *
 * @author Patryk Żywica
 */
public interface ProjectAreaDependentManagerFactory<T> extends ManagerFactory{
    /**
     * Returns manager for given <code>RtcActiveRepository</code>.
     * <p>
     * It is has to be guaranteed that for two areas: area1 and area2 such that
     * <code>repo1.equals(repo2)</code> this method will return the same
     * manager object.
     * </p>
     * <p>
     * Manager object for given area can be garbage collected only when there is
     * no other reference to it. After that it can be recreated only in way that
     * guarantee consistence with server.
     * </p>
     *
     * @param area to return  manager for. Must not be <code>null</code>.
     * @return manager for given <code>RtcActiveRepository</code>. Never <code>null</code>.
     */
    T getManager(ActiveProjectArea area);
}
