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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.WorkItemNodeManager;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenterManager;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service=WorkItemNodeManager.class)
public class WorkItemNodeManagerImpl implements WorkItemNodeManager{

    @Override
    public Node createNode(RtcWorkItem wi) {
        WorkItemPresenterManager pm = wi.getManager().getProjectArea().getLookup().lookup(WorkItemPresenterManager.class);
        return pm.createNodePresenter(wi, RtcWorkItemViewTarget.QUERIES);
    }

}
