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

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.workitem.common.model.IComment;
import com.ibm.team.workitem.common.model.IComments;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcComment;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcComments;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcCommentsImpl implements RtcComments{

    private final IWorkItem wi;
	private final ActiveProjectAreaImpl area;

    public RtcCommentsImpl(IWorkItem wi, ActiveProjectAreaImpl area) {
        this.wi = wi;
		this.area = area;
    }

    @Override
    public RtcComment createComment(String s) {
        IComments comments = wi.getComments();
        return new RtcCommentImpl(comments.createComment(
                ((ITeamRepository) wi.getOrigin()).loggedInContributor(),
                XMLString.createFromPlainText(s)), area);
    }

    @Override
    public List<RtcComment> getComments() {
        List<RtcComment> list = new ArrayList<RtcComment>();
        for (IComment object : wi.getComments().getContents()) {
            list.add(new RtcCommentImpl(object, area));
        }
        return list;
    }

    public void setComments(List<RtcComment> comments)
    {
        IComments comments1 = wi.getComments();
    }
}
