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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems;

import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;

/**
 * This manager should be used to create main Node for work item.
 * <p/>
 * Main work item node should present to user only most important properties of
 * work item. All presenter properties should be editable via property editors so
 * for example history or attachments should not be present in main node.
 *
 * @author Patryk Żywica
 */
public interface WorkItemNodeManager {
    /**
     * Creates new node for
     * @return
     */
    Node createNode(RtcWorkItem wi);
}
