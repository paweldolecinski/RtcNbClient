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

import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcLiteralImpl;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class EnumerationAttributeContentProvider implements
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
	public Lookup createLookup(IAttribute ia, ActiveProjectAreaImpl area) {
		if (AttributeTypes.isEnumerationAttributeType(ia.getAttributeType())) {
			InstanceContent ic = new InstanceContent();
			Lookup lookup = new AbstractLookup(ic);

			ic.add(new EnumerationPrefferedValues(ia));
			return lookup;
		} else {
			return null;
		}
	}

	/**
	 * @author Paweł Doleciński
	 * 
	 */
	private static class EnumerationPrefferedValues implements PrefferedValues<RtcLiteral> {

		private IAttribute ia;

		public EnumerationPrefferedValues(IAttribute ia) {
			assert ia != null : "EnumerationPrefferedValues: argument in constructor cannnot be null";
			this.ia = ia;
		}

		/*
		 * This method retrieves from rtc server possible values of enumeration
		 * type attributes You will get list of ILiteral but you must remember
		 * that WI gives you from getValue method IIdentifier object. You can
		 * get this object from ILiteral but it hasn't got a human readable
		 * name. Human readable name you will get from ILiteral.getName() and
		 * this should be displayed in editor.
		 * 
		 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.
		 * PrefferedValues#getValues()
		 */
		@Override
		public ValueProvider.Value<RtcLiteral>[] getValues() {
			assert (!EventQueue.isDispatchThread());
			List<ValueProvider.Value<RtcLiteral>> rtcLiterals = new ArrayList<ValueProvider.Value<RtcLiteral>>();
			try {
				ITeamRepository repo = (ITeamRepository) ia.getOrigin();
				IWorkItemCommon workItemCommon = (IWorkItemCommon) repo
						.getClientLibrary(IWorkItemCommon.class);
				// this should give you a list of ILiteral but
				IEnumeration<? extends ILiteral> enumerations = workItemCommon
						.resolveEnumeration(ia, null);
				List<?> items = enumerations.getEnumerationLiterals();

				for (Object iLiteral : items) {
					if (iLiteral instanceof ILiteral)
						rtcLiterals.add(new ValueImpl((ILiteral) iLiteral));
				}
			} catch (TeamRepositoryException ex) {
				RtcLogger.getLogger(EnumerationPrefferedValues.class).log(Level.SEVERE,
						ex.getLocalizedMessage(), ex);
			}
			@SuppressWarnings("unchecked")
			ValueProvider.Value<RtcLiteral>[] r = rtcLiterals
					.toArray(new ValueProvider.Value[rtcLiterals.size()]);
			return r;
		}

		private static class ValueImpl implements
				ValueProvider.Value<RtcLiteral> {
			private final RtcLiteralImpl type;

			private ValueImpl(ILiteral iLiteral) {
				this.type = new RtcLiteralImpl(iLiteral);
			}

			@Override
			public RtcLiteral getValue() {
				return type;
			}

			@Override
			public String getDisplayName() {
				return type.getName();
			}

			@Override
			public Image getIcon() {
				return type.getIcon();
			}

			@Override
			public Value<RtcLiteral>[] getChildren() {
				@SuppressWarnings({ "unchecked" })
				Value<RtcLiteral>[] ret = new Value[] {};
				return ret;
			}

			@Override
			public boolean isSelectable() {
				return true;
			}
		}
	}
}
