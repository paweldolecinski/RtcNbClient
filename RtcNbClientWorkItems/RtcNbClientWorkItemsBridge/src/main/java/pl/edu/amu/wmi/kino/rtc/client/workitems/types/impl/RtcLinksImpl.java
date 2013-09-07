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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl;

import com.ibm.team.links.common.registry.IEndPointDescriptor;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.WorkItemEndPoints;
import com.ibm.team.workitem.common.model.WorkItemLinkTypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLinkSection;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLinks;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcLinksImpl implements RtcLinks {

    private IWorkItemReferences references;
    private final WorkItemWorkingCopy wc;
    private boolean dirty = false;

    public RtcLinksImpl(WorkItemWorkingCopy workingCopy) {
        this.wc = workingCopy;
        this.references = workingCopy.getReferences();
    }

    @Override
    public List<RtcLinkSection> getLinkSections() {
        List<RtcLinkSection> list = new ArrayList<RtcLinkSection>();
        Set<IEndPointDescriptor> endPoints = getEndPoints();
        for (Iterator<IEndPointDescriptor> it = endPoints.iterator(); it.hasNext();) {
            list.add(new RtcLinkSectionImpl(it.next(), references));
        }
        return list;
    }

    private Set<IEndPointDescriptor> getEndPoints() {

        List<IEndPointDescriptor> descriptors = references.getTypes();
        Set<IEndPointDescriptor> visible = new HashSet<IEndPointDescriptor>();
        for (Iterator<IEndPointDescriptor> iter = descriptors.iterator(); iter.hasNext();) {
            IEndPointDescriptor descriptor = iter.next();
            if (descriptor.getLinkType().isInternal()) {
                continue;
            }

            if (!WorkItemLinkTypes.isSymmetric(descriptor)) {
                visible.add(descriptor);
            } else {
                visible.add(descriptor.getLinkType().getTargetEndPointDescriptor());
            }

        }
        visible.remove(WorkItemEndPoints.ATTACHMENT);

        return visible;
    }
}
