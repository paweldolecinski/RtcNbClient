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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemTypeImpl;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItemType;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class TypeAttributeContentProvider implements AttributeContentProvider {

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
	public Lookup createLookup(IAttribute ia, ActiveProjectAreaImpl area) {
		if (AttributeTypes.TYPE.equals(ia.getAttributeType())) {
			InstanceContent ic = new InstanceContent();
			Lookup lookup = new AbstractLookup(ic);
			ic.add(new TypePrefferedValues(area));
			return lookup;
		} else {
			return null;
		}
	}

	private static class TypePrefferedValues implements PrefferedValues<RtcWorkItemType> {

		private ActiveProjectAreaImpl area;

		public TypePrefferedValues(ActiveProjectAreaImpl area) {
			this.area = area;

		}

		@Override
		public ValueProvider.Value<RtcWorkItemType>[] getValues() {
			List<ValueProvider.Value<RtcWorkItemType>> types = new ArrayList<ValueProvider.Value<RtcWorkItemType>>();
			try {
				ITeamRepository repo = area.getITeamRepository();
				IWorkItemClient wiClient = (IWorkItemClient) repo
						.getClientLibrary(IWorkItemClient.class);
				List<IWorkItemType> tmp = wiClient.findWorkItemTypes(area
						.getProjectArea().getIProcessArea(), null);

				for (IWorkItemType rtcWorkItemType : tmp) {
					types.add(new ValueImpl(rtcWorkItemType));
				}
			} catch (TeamRepositoryException ex) {
				RtcLogger.getLogger(TypePrefferedValues.class)
                                                        .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			}
			@SuppressWarnings("unchecked")
			ValueProvider.Value<RtcWorkItemType>[] r = types
					.toArray(new ValueProvider.Value[types.size()]);
			return r;
		}

		private final class ValueImpl implements
				ValueProvider.Value<RtcWorkItemType> {
			private final RtcWorkItemTypeImpl type;

			private ValueImpl(IWorkItemType type) {
				this.type = new RtcWorkItemTypeImpl(type);
			}

			@Override
			public RtcWorkItemTypeImpl getValue() {
				return type;
			}

			@Override
			public String getDisplayName() {
				return type.getDisplayName();
			}

			@Override
			public Image getIcon() {
				return type.getIcon();
			}

			@Override
			public Value<RtcWorkItemType>[] getChildren() {
				@SuppressWarnings({ "unchecked" })
				Value<RtcWorkItemType>[] ret = new Value[] {};
				return ret;
			}

			@Override
			public boolean isSelectable() {
				return true;
			}
		}

	}
}
