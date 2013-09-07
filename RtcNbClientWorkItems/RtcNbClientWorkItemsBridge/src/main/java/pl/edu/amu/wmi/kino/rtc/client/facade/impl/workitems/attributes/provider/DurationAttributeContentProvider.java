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
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.DurationMarker;
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
public class DurationAttributeContentProvider implements
		AttributeContentProvider {

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
	public Lookup createLookup(final IAttribute ia, ActiveProjectAreaImpl area) {
		if (AttributeTypes.DURATION.equals(ia.getAttributeType())) {
			InstanceContent ic = new InstanceContent();
			Lookup lookup = new AbstractLookup(ic);
			final AttributesManager am = area.getLookup().lookup(
					AttributesManager.class);

			ic.add(new DurationMarker() {

				@Override
				public RtcWorkItemAttribute<Long> getAttribute() {
					RtcWorkItemAttribute<Long> attributeById = null;
					try {
						attributeById = am.getAttributeById(
								ia.getIdentifier(), Long.class);
					} catch (UnsupportedAttributeIdException e) {
						// shouldn't happen
						RtcLogger.getLogger().log(Level.SEVERE, "", e);
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
