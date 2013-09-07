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
import java.util.ArrayList;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.TeamAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcTeamAreaPossibleValues;

import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;

public class TeamAreaQueryAttributeLookupProvider implements
		RtcQueryAttributeLookupProvider {

	private IQueryableAttribute attribute;
	private ActiveProjectAreaImpl area;

	public TeamAreaQueryAttributeLookupProvider(IQueryableAttribute attribute,
			ActiveProjectArea area) {
		this.attribute = attribute;
		this.area = (ActiveProjectAreaImpl) area;
	}

	@Override
	public Lookup createLookup() {
		InstanceContent ic = new InstanceContent();
		ic.add(new TeamAreaPrefferedValues(area));
		ic.add(new TeamAreaPossibleValues(area));
		return new AbstractLookup(ic);
	}
}

class TeamAreaPrefferedValues implements RtcQueryAttributePrefferedValues {

	private ActiveProjectAreaImpl area;

	public TeamAreaPrefferedValues(ActiveProjectAreaImpl area) {
		this.area = area;
	}

	@Override
	public TeamAreaValueImpl[] getValues() {
		assert (!EventQueue.isDispatchThread());
		RtcTeamAreaPossibleValues pv = new RtcTeamAreaPossibleValues(area);
		ArrayList<TeamAreaValueImpl> impls = new ArrayList<TeamAreaValueImpl>();
		for (TeamArea ta : pv.getPossibleValues()) {
			if (ta instanceof TeamAreaImpl && !ta.isArchived()) {
				TeamAreaImpl tai = (TeamAreaImpl) ta;
				impls.add(new TeamAreaValueImpl(tai));
			}
		}
		return impls.toArray(new TeamAreaValueImpl[] {});
	}

	@Override
	public Image getIconFor(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof TeamAreaValueImpl) {
			TeamAreaImpl team = ((TeamAreaValueImpl) value).getRtcTeamArea();
			if (team.isArchived()) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/teamarea_archived_obj.gif");

			} else {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/teamarea_obj.gif");
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getDisplayName(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof TeamAreaValueImpl) {
			return ((TeamAreaValueImpl) value).getRtcTeamArea().getName();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public TeamAreaValueImpl[] getChildValues(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		assert (!EventQueue.isDispatchThread());
		if (value instanceof TeamAreaValueImpl) {
			ArrayList<TeamAreaValueImpl> children = new ArrayList<TeamAreaValueImpl>();
			TeamAreaImpl tai = ((TeamAreaValueImpl) value).getRtcTeamArea();
			ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
			for (TeamArea impl : pm.getTeamAreas(tai)) {
				if (impl instanceof TeamAreaImpl && !impl.isArchived()) {
					children.add(new TeamAreaValueImpl((TeamAreaImpl) impl));
				}
			}
			return children.toArray(new TeamAreaValueImpl[] {});
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isValueSelectable(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		return true;
	}
}

class TeamAreaPossibleValues implements RtcQueryAttributePossibleValues,
		ValueCreator {

	private ActiveProjectAreaImpl area;

	public TeamAreaPossibleValues(ActiveProjectAreaImpl area) {
		this.area = area;
	}

	@Override
	public TeamAreaValueImpl[] getValues() {
		assert (!EventQueue.isDispatchThread());
		RtcTeamAreaPossibleValues pv = new RtcTeamAreaPossibleValues(area);
		ArrayList<TeamAreaValueImpl> impls = new ArrayList<TeamAreaValueImpl>();
		for (TeamArea ta : pv.getPossibleValues()) {
			if (ta instanceof TeamAreaImpl) {
				TeamAreaImpl tai = (TeamAreaImpl) ta;
				impls.add(new TeamAreaValueImpl(tai));
			}
		}
		return impls.toArray(new TeamAreaValueImpl[] {});
	}

	@Override
	public Image getIconFor(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof TeamAreaValueImpl) {
			TeamAreaImpl team = ((TeamAreaValueImpl) value).getRtcTeamArea();
			if (team.isArchived()) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/teamarea_archived_obj.gif");

			} else {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/teamarea_obj.gif");
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getDisplayName(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof TeamAreaValueImpl) {
			return ((TeamAreaValueImpl) value).getRtcTeamArea().getName();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public TeamAreaValueImpl[] getChildValues(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		assert (!EventQueue.isDispatchThread());
		if (value instanceof TeamAreaValueImpl) {
			ArrayList<TeamAreaValueImpl> children = new ArrayList<TeamAreaValueImpl>();
			TeamAreaImpl tai = ((TeamAreaValueImpl) value).getRtcTeamArea();
			ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
			for (TeamArea impl : pm.getTeamAreas(tai)) {
				if (impl instanceof TeamAreaImpl) {
					children.add(new TeamAreaValueImpl((TeamAreaImpl) impl));
				}
			}
			return children.toArray(new TeamAreaValueImpl[] {});
		}
		throw new IllegalArgumentException();
	}

	@Override
	public RtcQueryAttributeValue getValueForObject(Object obj)
			throws IllegalArgumentException {
		if (obj instanceof ITeamAreaHandle) {
			ITeamAreaHandle handle = (ITeamAreaHandle) obj;
			TeamAreaValueImpl tmp;
			for (TeamAreaValueImpl val : getValues()) {
				tmp = recursiveValueSearch(handle.getItemId().getUuidValue(),
						val);
				if (tmp != null) {
					return tmp;
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private TeamAreaValueImpl recursiveValueSearch(String valToFind,
			TeamAreaValueImpl space) {
		if (space.getRtcTeamArea().getIProcessArea().getItemId().getUuidValue()
				.equals(valToFind)) {
			return space;
		}
		TeamAreaValueImpl tmp;
		for (TeamAreaValueImpl impl : getChildValues(space)) {
			tmp = recursiveValueSearch(valToFind, impl);
			if (tmp != null) {
				return tmp;
			}
		}
		return null;
	}

	@Override
	public boolean isValueSelectable(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		return true;
	}
}

class TeamAreaValueImpl extends RtcQueryAttributeValueImpl {

	private TeamAreaImpl wi;

	public TeamAreaValueImpl(TeamAreaImpl wi) {
		super(wi.getIProcessArea());
		this.wi = wi;
	}

	public TeamAreaImpl getRtcTeamArea() {
		return wi;
	}
}
