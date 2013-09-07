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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyProcessArea implements ProcessArea {

    private EventSourceSupport<ProcessArea.ProcessAreaEvent> eventSource = new EventSourceSupport<ProcessAreaEvent>();

    @Override
    public String getId() {
        return "Process Area ID";
    }

    @Override
    public String getName() {
        return "Dummy Process Area";
    }

    @Override
    public boolean isArchived() {
        return false;
    }

    public final void removeListener(EventListener<ProcessAreaEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(ProcessAreaEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<ProcessAreaEvent> listener) {
        eventSource.addListener(listener);
    }
}
