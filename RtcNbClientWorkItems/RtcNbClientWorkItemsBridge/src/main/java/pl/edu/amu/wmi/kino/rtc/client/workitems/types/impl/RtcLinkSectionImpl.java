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

import com.ibm.team.links.common.IReference;
import com.ibm.team.links.common.registry.IEndPointDescriptor;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLink;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLinkSection;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcLinkSectionImpl extends RtcLinkSection {

    private final IEndPointDescriptor endPointDescriptor;
    private final IWorkItemReferences references;

    public RtcLinkSectionImpl(IEndPointDescriptor endPointDescriptor, IWorkItemReferences references) {
        this.endPointDescriptor = endPointDescriptor;
        this.references = references;
    }

    public IEndPointDescriptor getEndPointDescriptor() {
        return endPointDescriptor;
    }

    @Override
    public String getName() {
        return endPointDescriptor.getDisplayName();
    }

    @Override
    public List<RtcLink> getLinksInSection(RtcLinkSection section) {
        List<RtcLink> list = new ArrayList<RtcLink>();
        List<IReference> attachments = references.getReferences(((RtcLinkSectionImpl) section).getEndPointDescriptor());
        for (Iterator<IReference> it = attachments.iterator(); it.hasNext();) {
            list.add(new RtcLinkImpl(it.next()));

        }
        return list;
    }
}
