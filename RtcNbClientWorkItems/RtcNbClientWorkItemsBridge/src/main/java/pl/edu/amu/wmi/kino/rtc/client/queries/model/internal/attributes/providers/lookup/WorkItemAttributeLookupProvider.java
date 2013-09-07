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

import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcWorkItemPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcWorkItemPrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;

public class WorkItemAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectArea area;

    public WorkItemAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = area;
    }

    @Override
    public Lookup createLookup() {
        RtcQueryAttributePrefferedValues vals = new WorkItemPrefferedValues(area);
        RtcQueryAttributePossibleValues vals2 = new WorkItemPossibleValues(area);
        InstanceContent ic = new InstanceContent();
        ic.add(vals);
        ic.add(vals2);
        return new AbstractLookup(ic);
    }
}

class WorkItemPrefferedValues implements RtcQueryAttributePrefferedValues {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;

    public WorkItemPrefferedValues(ActiveProjectArea area) {
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public RtcQueryAttributeValue[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcWorkItemPrefferedValues poss = new RtcWorkItemPrefferedValues(area);
        ArrayList<RtcQueryAttributeValueImpl> valimpl =
                new ArrayList<RtcQueryAttributeValueImpl>();
        for (RtcWorkItem w : poss.getPrefferedValues()) {
            if (w instanceof RtcWorkItemImpl) {
                RtcWorkItemImpl wi = (RtcWorkItemImpl) w;
                valimpl.add(new WorkItemValueImpl(wi));
            }
        }
        return valimpl.toArray(new RtcQueryAttributeValue[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkItemValueImpl) {
            return ((WorkItemValueImpl) value).getRtcWorkItem().getIcon();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkItemValueImpl) {
            return ((WorkItemValueImpl) value).getRtcWorkItem().getDisplayName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return true;
    }
}

class WorkItemPossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;

    public WorkItemPossibleValues(ActiveProjectArea area) {
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public WorkItemValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcWorkItemPossibleValues poss = new RtcWorkItemPossibleValues(area);
        ArrayList<WorkItemValueImpl> valimpl =
                new ArrayList<WorkItemValueImpl>();
        for (RtcWorkItem w : poss.getPossibleValues()) {
            if (w instanceof RtcWorkItemImpl) {
                RtcWorkItemImpl wi = (RtcWorkItemImpl) w;
                valimpl.add(new WorkItemValueImpl(wi));
            }
        }
        return valimpl.toArray(new WorkItemValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkItemValueImpl) {
            return ((WorkItemValueImpl) value).getRtcWorkItem().getIcon();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkItemValueImpl) {
            return ((WorkItemValueImpl) value).getRtcWorkItem().getDisplayName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof IWorkItemHandle) {
            IWorkItemHandle handle = (IWorkItemHandle) obj;
            for (WorkItemValueImpl val : getValues()) {
                if (val.getRtcWorkItem().getWorkItem().getItemId().getUuidValue().equals(handle.getItemId().getUuidValue())) {
                    return val;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return true;
    }
}

class WorkItemValueImpl extends RtcQueryAttributeValueImpl {

    private RtcWorkItemImpl wi;

    public WorkItemValueImpl(RtcWorkItemImpl wi) {
        super(wi.getWorkItem());
        this.wi = wi;
    }

    public RtcWorkItemImpl getRtcWorkItem() {
        return wi;
    }
}
