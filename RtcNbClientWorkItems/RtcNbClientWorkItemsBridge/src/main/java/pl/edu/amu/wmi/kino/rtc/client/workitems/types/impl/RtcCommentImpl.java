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

import com.ibm.team.workitem.common.model.IComment;
import java.util.Date;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcComment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

/**
 *
 * @author dolek
 */
class RtcCommentImpl implements RtcComment{
    private final IComment comment;
	private final ActiveProjectAreaImpl area;

    public RtcCommentImpl(IComment comment,ActiveProjectAreaImpl area) {
        this.comment = comment;
		this.area = area;
    }

    @Override
    public Contributor getCreator() {
    	ContributorManagerImpl lookup = (ContributorManagerImpl) area.getLookup().lookup(ContributorManager.class);
        return lookup.findContributor(comment.getCreator());
    }

    @Override
    public Date getCreationDate() {
        return comment.getCreationDate();
    }

    @Override
    public String getContent() {
        return comment.getHTMLContent().getXMLText();
    }

}
