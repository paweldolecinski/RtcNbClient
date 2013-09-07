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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider;

import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.HtmlMarker;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.SmallTextMarker;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.AttributesManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.UnsupportedAttributeIdException;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class HtmlAttributeContentProvider implements AttributeContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider
	 * .WorkItemAttributeContentProvider
	 * #createLookup(com.ibm.team.workitem.common.model.IAttribute,
	 * pl.edu.amu.wmi.kino.rtc.client.impl.connections.ActiveProjectAreaImpl)
	 */
	@Override
	public Lookup createLookup(final IAttribute ia,
			ActiveProjectAreaImpl area) {
		String type = ia.getAttributeType();
		InstanceContent ic = new InstanceContent();
		Lookup lookup = new AbstractLookup(ic);
		final AttributesManager am = area.getLookup().lookup(
				AttributesManager.class);
		if (AttributeTypes.MEDIUM_HTML.contains(type)) {

			ic.add(new HtmlMarker() {

				@Override
				public RtcWorkItemAttribute<String> getAttribute() {
					RtcWorkItemAttribute<String> attributeById = null;
					try {
						attributeById = am.getAttributeById(ia.getIdentifier(),
								String.class);
					} catch (UnsupportedAttributeIdException ex) {
						// shouldn't happen
						RtcLogger.getLogger(HtmlAttributeContentProvider.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
					}
					return attributeById;
				}
			});

			ic.add(new SmallTextMarker() {

				@Override
				public RtcWorkItemAttribute<String> getAttribute() {
					RtcWorkItemAttribute<String> attributeById = null;
					try {
						attributeById = am.getAttributeById(ia.getIdentifier(),
								String.class);
					} catch (UnsupportedAttributeIdException ex) {
						// shouldn't happen
						RtcLogger.getLogger(HtmlAttributeContentProvider.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
					}
					return attributeById;
				}
			});
			return lookup;
		} else if (AttributeTypes.LARGE_HTML.contains(type)) {
			ic.add(new HtmlMarker() {

				@Override
				public RtcWorkItemAttribute<String> getAttribute() {
					RtcWorkItemAttribute<String> attributeById = null;
					try {
						attributeById = am.getAttributeById(ia.getIdentifier(),
								String.class);
					} catch (UnsupportedAttributeIdException ex) {
						// shouldn't happen
						RtcLogger.getLogger(HtmlAttributeContentProvider.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
					}
					return attributeById;
				}
			});
			return lookup;
		} else {
			return null;
		}
	}

}
