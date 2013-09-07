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
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.IterationImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcIterationPossibleValues;

import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;

public class IterationQueryAttributeLookupProvider implements
		RtcQueryAttributeLookupProvider {

	private IQueryableAttribute attribute;
	private ActiveProjectAreaImpl area;

	public IterationQueryAttributeLookupProvider(IQueryableAttribute attribute,
			ActiveProjectArea area) {
		this.attribute = attribute;
		this.area = (ActiveProjectAreaImpl) area;
	}

	@Override
	public Lookup createLookup() {
		InstanceContent ic = new InstanceContent();
		ic.add(new IterationPossibleValues(area));
		ic.add(new IterationPrefferedValues(area));
		return new AbstractLookup(ic);
	}
}

class IterationPrefferedValues implements RtcQueryAttributePrefferedValues {

	private ActiveProjectAreaImpl area;

	public IterationPrefferedValues(ActiveProjectAreaImpl area) {
		this.area = area;
	}

	@Override
	public RtcQueryAttributeValue[] getValues() {
		assert (!EventQueue.isDispatchThread());
		RtcIterationPossibleValues pv = new RtcIterationPossibleValues(area);
		ArrayList<RtcQueryAttributeValueImpl> impls2 = new ArrayList<RtcQueryAttributeValueImpl>();
		for (DevelopmentLine dl : pv.getPossibleValues()) {
			impls2.add(new DevelopmentLineValueImpl(dl));
		}
		return impls2.toArray(new RtcQueryAttributeValue[] {});
	}

	@Override
	public Image getIconFor(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof IterationValueImpl) {
			IterationImpl iter = ((IterationValueImpl) value).getRtcIteration();
			if (iter.isArchived()) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/iteration_archived_obj.gif");

			} else {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/iteration_obj.gif");
			}
		} else {
			if (value instanceof DevelopmentLineValueImpl) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/project_devline_obj.gif");
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getDisplayName(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof IterationValueImpl) {
			return ((IterationValueImpl) value).getRtcIteration().getName();
		} else {
			if (value instanceof DevelopmentLineValueImpl) {
				return ((DevelopmentLineValueImpl) value).getLine().getName();
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		assert (!EventQueue.isDispatchThread());
		ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
		if (value instanceof DevelopmentLineValueImpl) {
			ArrayList<RtcQueryAttributeValue> chs = new ArrayList<RtcQueryAttributeValue>();
			DevelopmentLine line = ((DevelopmentLineValueImpl) value).getLine();

			for (Iteration impl : pm.getIterations(line)) {
				if (impl instanceof IterationImpl) {
					if (!impl.isArchived()) {
						chs.add(new IterationValueImpl((IterationImpl) impl));
					}
				}
			}
			return chs.toArray(new RtcQueryAttributeValue[] {});
		} else {
			ArrayList<RtcQueryAttributeValue> chs = new ArrayList<RtcQueryAttributeValue>();
			if (value instanceof IterationValueImpl) {
				IterationValueImpl impl = (IterationValueImpl) value;
				for (Iteration it : pm.getIterations(impl.getRtcIteration())) {
					if (it instanceof IterationImpl) {
						if (!it.isArchived()) {
							chs.add(new IterationValueImpl((IterationImpl) it));
						}
					}
				}
				return chs.toArray(new RtcQueryAttributeValue[] {});
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isValueSelectable(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof DevelopmentLineValueImpl) {
			return false;
		} else {
			if (value instanceof IterationValueImpl) {
				return true;
			}
		}
		throw new IllegalArgumentException();
	}
}

class IterationPossibleValues implements RtcQueryAttributePossibleValues,
		ValueCreator {

	private ActiveProjectAreaImpl area;

	public IterationPossibleValues(ActiveProjectAreaImpl area) {
		this.area = area;
	}

	@Override
	public DevelopmentLineValueImpl[] getValues() {
		assert (!EventQueue.isDispatchThread());
		RtcIterationPossibleValues pv = new RtcIterationPossibleValues(area);
		ArrayList<DevelopmentLineValueImpl> impls2 = new ArrayList<DevelopmentLineValueImpl>();
		for (DevelopmentLine dl : pv.getPossibleValues()) {
			impls2.add(new DevelopmentLineValueImpl(dl));
		}
		return impls2.toArray(new DevelopmentLineValueImpl[] {});
	}

	@Override
	public Image getIconFor(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof IterationValueImpl) {
			IterationImpl iter = ((IterationValueImpl) value).getRtcIteration();
			if (iter.isArchived()) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/iteration_archived_obj.gif");

			} else {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/iteration_obj.gif");
			}
		} else {
			if (value instanceof DevelopmentLineValueImpl) {
				return ImageUtilities
						.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/project_devline_obj.gif");
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getDisplayName(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof IterationValueImpl) {
			return ((IterationValueImpl) value).getRtcIteration().getName();
		} else {
			if (value instanceof DevelopmentLineValueImpl) {
				return ((DevelopmentLineValueImpl) value).getLine().getName();
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public IterationValueImpl[] getChildValues(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		assert (!EventQueue.isDispatchThread());
		ProcessManager pm = area.getLookup().lookup(ProcessManager.class);
		if (value instanceof DevelopmentLineValueImpl) {
			ArrayList<IterationValueImpl> chs = new ArrayList<IterationValueImpl>();
			DevelopmentLine line = ((DevelopmentLineValueImpl) value).getLine();
			for (Iteration impl : pm.getIterations(line)) {
				if (impl instanceof IterationImpl) {
					chs.add(new IterationValueImpl((IterationImpl) impl));
				}
			}
			return chs.toArray(new IterationValueImpl[] {});
		} else {
			if (value instanceof IterationValueImpl) {
				ArrayList<IterationValueImpl> chs = new ArrayList<IterationValueImpl>();
				IterationImpl impl = ((IterationValueImpl) value)
						.getRtcIteration();
				for (Iteration it : pm.getIterations(impl)) {
					if (it instanceof IterationImpl) {
						if (!it.isArchived()) {
							chs.add(new IterationValueImpl((IterationImpl) it));
						}
					}
				}
				return chs.toArray(new IterationValueImpl[] {});
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public RtcQueryAttributeValue getValueForObject(Object obj)
			throws IllegalArgumentException {
		if (obj instanceof IIterationHandle) {
			IIterationHandle handle = (IIterationHandle) obj;
			for (DevelopmentLineValueImpl val : getValues()) {
				for (IterationValueImpl iter : getChildValues(val)) {
					if (iter.getRtcIteration().getIIteration().getItemId()
							.getUuidValue()
							.equals(handle.getItemId().getUuidValue())) {
						return iter;
					}
				}
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isValueSelectable(RtcQueryAttributeValue value)
			throws IllegalArgumentException {
		if (value instanceof DevelopmentLineValueImpl) {
			return false;
		} else {
			if (value instanceof IterationValueImpl) {
				return true;
			}
		}
		throw new IllegalArgumentException();
	}
}

class IterationValueImpl extends RtcQueryAttributeValueImpl {

	private IterationImpl wi;

	public IterationValueImpl(IterationImpl wi) {
		super(wi.getIIteration());
		this.wi = wi;
	}

	public IterationImpl getRtcIteration() {
		return wi;
	}
}

class DevelopmentLineValueImpl extends RtcQueryAttributeValueImpl {

	private DevelopmentLine val;

	public DevelopmentLineValueImpl(DevelopmentLine rtcValue) {
		super(null);
		this.val = rtcValue;
	}

	public DevelopmentLine getLine() {
		return val;
	}
}
