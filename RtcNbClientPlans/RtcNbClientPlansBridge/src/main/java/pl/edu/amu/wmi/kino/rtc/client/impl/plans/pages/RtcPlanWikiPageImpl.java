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

import com.ibm.team.apt.common.IIterationPlanRecord;
import com.ibm.team.apt.internal.client.IterationPlanData;
import com.ibm.team.apt.internal.client.wiki.ResolvedWikiPage;
import com.ibm.team.apt.internal.client.wiki.ResolvedWikiPageAttachment;
import com.ibm.team.apt.internal.client.wiki.WikiClient;
import com.ibm.team.apt.internal.client.wiki.WikiManager;
import com.ibm.team.apt.internal.common.wiki.IWikiPage;
import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContent;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor.ProgressMonitor;

/**
 * 
 * @author Pawel Dolecinski
 */
public class RtcPlanWikiPageImpl implements RtcPlanPage {

	private Lookup lookup;
	private InstanceContent ic = new InstanceContent();
	private IWikiPage page;
	private String name;
	private String content = "";
	private List<RtcPlanPageAttachment> attachments;
	private ResolvedWikiPage resolvedPage;
	private EventSourceSupport<RtcPlanPageEvent> eventSource = new EventSourceSupport<RtcPlanPageEvent>();

	/**
	 * 
	 * @param wikiPage
	 * @param resolvedPage
	 */
	public RtcPlanWikiPageImpl(IWikiPage wikiPage, ResolvedWikiPage resolvedPage) {
		this.page = (IWikiPage) wikiPage.getWorkingCopy();
		lookup = new AbstractLookup(ic);
		this.resolvedPage = resolvedPage;
	}

	/**
	 * 
	 * @param name
	 */
	public RtcPlanWikiPageImpl(String name) {
		this.name = name;
		lookup = new AbstractLookup(ic);
	}

	@Override
	public String getName() {
		if (name == null) {
			name = IIterationPlanRecord.OVERVIEW_PAGE_ID.equals(page
					.getWikiID()) ? IterationPlanData.OVERVIEW_PAGE_NAME : page
					.getLabel();
		}
		return name;
	}

	@Override
	public void setName(String name) {
		if (page != null) {
			page.setName(name);
		}
		this.name = name;
		fireEvent(RtcPlanPageEvent.NAME_CHANGED);
	}

	@Override
	public Lookup getLookup() {
		return lookup;
	}

	@Override
	public String getPageContent() {

		if (page != null) {
			XMLString result = XMLString.EMPTY;
			IContent cont = page.getContent();
			if (cont != null) {
				InputStream input = null;
				try {
					input = ((ITeamRepository) page.getOrigin())
							.contentManager().retrieveContentStream(cont,
									new ProgressMonitor());
					{
						InputStreamReader reader = null;
						try {
							reader = new InputStreamReader(input,
									cont.getCharacterEncoding());
							char[] buffer = new char[1024];
							int read;
							StringBuilder xmlString = new StringBuilder();
							while ((read = reader.read(buffer)) != -1) {
								xmlString.append(buffer, 0, read);
							}
							content = XMLString.createFromXMLText(
									xmlString.toString()).getXMLText();
						} catch (IOException ex) {
							//Exceptions.printStackTrace(ex);
                                                        RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
						} finally {
							try {
								input.close();
							} catch (IOException ex) {
                                                            RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
							}
							try {
								reader.close();
							} catch (IOException ex) {
                                                            //Exceptions.printStackTrace(ex);
                                                            RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
							}
						}
					}
				} catch (TeamRepositoryException ex) {
					//Exceptions.printStackTrace(ex);
                                    RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				} finally {
					try {
						input.close();
					} catch (IOException ex) {
						//Exceptions.printStackTrace(ex);
                                            RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
					}
				}
			}
		}
		return content;
	}

	@Override
	public void setPageContent(String content) {
		this.content = content;
		if (page != null) {
			try {
				IContent storeContent = ((ITeamRepository) page.getOrigin())
						.contentManager().storeContent(
								IContent.CONTENT_TYPE_TEXT, content,
								new ProgressMonitor());
				page.setContent(storeContent);
			} catch (TeamRepositoryException ex) {
				//Exceptions.printStackTrace(ex);
                            RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			}
		}
		fireEvent(RtcPlanPageEvent.CONTENT_CHANGED);
	}

	@Override
	public RtcPlanPageAttachment[] getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<RtcPlanPageAttachment>();
			if (resolvedPage != null) {
				for (ResolvedWikiPageAttachment a : resolvedPage
						.getAttachments()) {
					// FIXME
					// attachments.add(new
					// RtcWikiAttachmentImpl(a.getAttachment()));
				}
			}
		}
		return attachments.toArray(new RtcPlanPageAttachment[] {});
	}

	@Override
	public void addAttachment(RtcPlanPageAttachment attachment) {

		attachment.setOwner(this);
		WikiManager manager = WikiClient.getWikiManager(page);
		// FIXME
		// attachment = new
		// RtcWikiAttachmentImpl(manager.saveAttachment(((RtcWikiAttachmentImpl)
		// attachment).getAttachment(), new ProgressMonitor()));
		attachments.add(attachment);
		fireEvent(RtcPlanPageEvent.CONTENT_CHANGED);

	}

	@Override
	public void removeAttachment(RtcPlanPageAttachment attachment) {
		if (attachments.contains(attachment)) {
			attachments.remove(attachment);
			fireEvent(RtcPlanPageEvent.CONTENT_CHANGED);
		}
	}

	@Override
	public void removeAttachments() {
		attachments.clear();
		fireEvent(RtcPlanPageEvent.CONTENT_CHANGED);
	}

	/**
	 * 
	 * @param page
	 */
	public void setPage(IWikiPage page) {
		this.page = (IWikiPage) page.getWorkingCopy();
		page.setName(name);
		try {
			IContent storeContent = ((ITeamRepository) page.getOrigin())
					.contentManager().storeContent(IContent.CONTENT_TYPE_TEXT,
							content, new ProgressMonitor());
			page.setContent(storeContent);
		} catch (TeamRepositoryException ex) {
			//Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanWikiPageImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
	}

	public IWikiPage getPage() {
		return page;
	}

	public final void removeListener(EventListener<RtcPlanPageEvent> listener) {
		eventSource.removeListener(listener);
	}

	public final void fireEvent(RtcPlanPageEvent event) {
		eventSource.fireEvent(event);
	}

	public final void addListener(EventListener<RtcPlanPageEvent> listener) {
		eventSource.addListener(listener);
	}
}
