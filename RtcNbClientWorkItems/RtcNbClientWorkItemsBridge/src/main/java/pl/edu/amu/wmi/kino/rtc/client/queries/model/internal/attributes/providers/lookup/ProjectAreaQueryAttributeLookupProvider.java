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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcProjectAreaPossibleValues;

import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;

public class ProjectAreaQueryAttributeLookupProvider implements
		RtcQueryAttributeLookupProvider {

	private IQueryableAttribute attribute;
	private ActiveProjectAreaImpl area;

	public ProjectAreaQueryAttributeLookupProvider(
			IQueryableAttribute attribute, ActiveProjectArea area) {
		this.attribute = attribute;
		this.area = (ActiveProjectAreaImpl) area;
	}

	@Override
	public Lookup createLookup() {
		return Lookups.singleton(new ProjectAreaPossibleValues(area));
	}
}

class ProjectAreaPossibleValues implements RtcQueryAttributePossibleValues,
		ValueCreator {

	private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[] {};
	private ActiveProjectAreaImpl area;
	private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

	public ProjectAreaPossibleValues(ActiveProjectArea area) {
		this.area = (ActiveProjectAreaImpl) area;
	}

	@Override
	public ProjectAreaValueImpl[] getValues() {
		assert (!EventQueue.isDispatchThread());
		RtcProjectAreaPossibleValues pv = new RtcProjectAreaPossibleValues(area);
		ArrayList<ProjectAreaValueImpl> valimpl = new ArrayList<ProjectAreaValueImpl>();
		for (ProjectArea type : pv.getPossibleValues()) {
			if (type instanceof ProjectAreaImpl) {
				ProjectAreaImpl impl = (ProjectAreaImpl) type;
				valimpl.add(new ProjectAreaValueImpl(impl));
			}
		}
		return valimpl.toArray(new ProjectAreaValueImpl[] {});
	}

	@Override
	public Image getIconFor(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		return icon;
	}

	@Override
	public String getDisplayName(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof ProjectAreaValueImpl) {
			return ((ProjectAreaValueImpl) value).getRtcProjectArea().getName();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		return children;
	}

	@Override
	public RtcQueryAttributeValue getValueForObject(Object obj)
			throws IllegalArgumentException {
		if (obj instanceof IProjectAreaHandle) {
			IProjectAreaHandle handle = (IProjectAreaHandle) obj;
			for (ProjectAreaValueImpl val : getValues()) {
				if (val.getRtcProjectArea().getIProcessArea().getItemId()
						.getUuidValue()
						.equals(handle.getItemId().getUuidValue())) {
					return val;
				}
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isValueSelectable(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		return true;
	}
}

class ProjectAreaValueImpl extends RtcQueryAttributeValueImpl {

	private ProjectAreaImpl wi;

	public ProjectAreaValueImpl(ProjectAreaImpl wi) {
		super(wi.getIProcessArea());
		this.wi = wi;
	}

	public ProjectAreaImpl getRtcProjectArea() {
		return wi;
	}
}
