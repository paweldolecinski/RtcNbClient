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

/**
 * Implementations should manage all work item layouts.
 *
 * <p>All layout object should be shared, so that there is exactly one layout object
 * per work item layout defined on server. Returned object must be immutable.</p>
 *
 *
 * @author Patryk Żywica
 */
public interface RtcWorkItemLayoutManager {
/**
 * Returns layout for given work item and view target. Returned object may be
 * shared between many work item objects.
 *
 * <p>Layout manager should choose proper layout for given work item form layout pool
 * and return it.</p>
 * @param wi used to determine proper layout object
 * @param target used to describe which layout choose for given work item.
 * @return
 */
    public abstract RtcWorkItemLayout getLayout(RtcWorkItem wi, RtcWorkItemViewTarget target);
}
