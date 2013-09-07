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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.topcomponents.RtcWorkItemTopComponent;

/**
 *
 * @author michu
 */
@ServiceProvider(service=WorkItemsManager.class)
public class WorkItemsManager {

    private Map<String, RtcWorkItemTopComponent> map =
            Collections.synchronizedMap(new HashMap<String, RtcWorkItemTopComponent>());

    public RtcWorkItemTopComponent find(Lookup context) {
        String id = Integer.toString((context.lookup(RtcWorkItem.class)).getId());
        RtcWorkItemTopComponent tc = map.get(id);
        if (tc == null) {
            tc = new RtcWorkItemTopComponent(context);
            map.put(id, tc);
            tc.open();
        }

        return tc;
    }

    public void remove(String itemId) {
        RtcWorkItemTopComponent tc = map.get(itemId);
        if (tc != null) {
            map.remove(itemId);
        }
    }

}
