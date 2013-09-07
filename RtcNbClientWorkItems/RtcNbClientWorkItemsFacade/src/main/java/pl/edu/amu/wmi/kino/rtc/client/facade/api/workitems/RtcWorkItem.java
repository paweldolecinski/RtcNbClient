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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem.RtcWorkItemEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.WorkItemReferences;

/**
 * 
 * @author Patryk Żywica
 */
public interface RtcWorkItem extends EventSource<RtcWorkItemEvent> {

    public abstract <T> T getValue(RtcWorkItemAttribute<T> attr);

    public abstract <T> boolean hasAttribute(RtcWorkItemAttribute<T> attr);

    public abstract RtcWorkItemManager getManager();

    public abstract WorkItemReferences getReferences();

    public static enum RtcWorkItemEvent {

        WORK_ITEM_CHANGED, WORK_ITEM_SAVED, WORK_ITEM_CHANGES_DISCARDED,}
}
