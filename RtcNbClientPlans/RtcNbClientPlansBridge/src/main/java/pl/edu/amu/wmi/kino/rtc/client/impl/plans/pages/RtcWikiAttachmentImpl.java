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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.pages;

import java.util.Arrays;

import org.openide.util.Exceptions;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor.ProgressMonitor;

import com.ibm.team.apt.internal.client.PlanningClientPlugin;
import com.ibm.team.apt.internal.common.wiki.IWikiPage;
import com.ibm.team.apt.internal.common.wiki.IWikiPageAttachment;
import com.ibm.team.apt.internal.common.wiki.IWikiPageHandle;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 * 
 * @author Pawel Dolecinski
 */
public class RtcWikiAttachmentImpl extends RtcPlanPageAttachment {

	private final IWikiPageAttachment attachment;
	private String name;
	private Contributor creator;
	private String description;
	private final RtcWikiContentImpl content;
	private RtcPlanPage owner;

	/**
	 * 
	 * @param attachment
	 */
	public RtcWikiAttachmentImpl(IWikiPageAttachment attachment,
			Contributor creator) {
		this.attachment = attachment;
		this.name = attachment.getName();
		this.description = attachment.getDescription();
		this.creator = creator;
		this.content = new RtcWikiContentImpl(attachment,
				attachment.getContent());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Contributor getCreator() {
		return creator;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public RtcContent getContent() {
		return content;
	}

	@Override
	public boolean isPredefined() {
		IItemManager manager = PlanningClientPlugin.getTeamRepository(
				attachment).itemManager();
		IWikiPageHandle ownerHandle = attachment.getOwner();
		IWikiPage ownerrr;
		try {
			ownerrr = (IWikiPage) manager.fetchPartialItem(ownerHandle,
					IItemManager.DEFAULT,
					Arrays.asList(IWikiPage.OWNER_PROPERTY),
					new ProgressMonitor());
			return ownerrr.getOwner() == null; // null attachments are owned by
												// the null page which has no
												// owner
		} catch (TeamRepositoryException ex) {
			//Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcWikiAttachmentImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public IWikiPageAttachment getAttachment() {
		return attachment;
	}

	@Override
	public void setOwner(RtcPlanPage page) {
		owner = page;
		attachment.setOwner(((RtcPlanWikiPageImpl) page).getPage());
	}
}
