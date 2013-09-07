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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common;

import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import org.openide.util.lookup.InstanceContent.Convertor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ProjectAreaDependentManagerFactory;

/**
 *
 * @author Patryk Żywica
 */
public class RtcProjectAreaDependentLookupConvertorImpl implements Convertor<ProjectAreaDependentManagerFactory, Object> {

    private ActiveProjectAreaImpl activeProjectArea;

    public RtcProjectAreaDependentLookupConvertorImpl(ActiveProjectAreaImpl activeProjectArea) {
        this.activeProjectArea = activeProjectArea;
    }

    @Override
    public Object convert(ProjectAreaDependentManagerFactory obj) {
        return obj.getManager(activeProjectArea);
    }

    @Override
    public Class<? extends Object> type(ProjectAreaDependentManagerFactory obj) {
        return obj.getManagerType();
    }

    @Override
    public String id(ProjectAreaDependentManagerFactory obj) {
        return obj.getManagerIdPrefix() + "_" + activeProjectArea.getID();
    }

    @Override
    public String displayName(ProjectAreaDependentManagerFactory obj) {
        return obj.getManagerNamePrefix() + " " + activeProjectArea.getID();
    }
}
