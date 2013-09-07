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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcDeliverablePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDeliverableImpl;

import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.model.IDeliverableHandle;

public class DeliverableQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public DeliverableQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public Lookup createLookup() {
        InstanceContent ic = new InstanceContent();
        ic.add(new DeliverablePossibleValues(area));
        ic.add(new DeliverablePrefferedValues(area));
        return new AbstractLookup(ic);
    }
}

class DeliverablePossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public DeliverablePossibleValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public DeliverableValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcDeliverablePossibleValues pv = new RtcDeliverablePossibleValues(area);
        ArrayList<DeliverableValueImpl> valimpl = new ArrayList<DeliverableValueImpl>();
        for (RtcDeliverable cat : pv.getPossibleValues()) {
            if (cat instanceof RtcDeliverableImpl) {
                RtcDeliverableImpl impl = (RtcDeliverableImpl) cat;
                valimpl.add(new DeliverableValueImpl(impl));
            }
        }
        return valimpl.toArray(new DeliverableValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof DeliverableValueImpl) {
            return ((DeliverableValueImpl) value).getRtcDeliverable().getName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        //TODO : bikol : sprawdzic co jestr na serwerze w delivarable
        if (obj instanceof IDeliverableHandle) {
            IDeliverableHandle handle = (IDeliverableHandle) obj;
            for (DeliverableValueImpl val : getValues()) {
                if (val.getRtcDeliverable().getDeliverable().getItemId().getUuidValue().equals(handle.getItemId().getUuidValue())) {
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

class DeliverablePrefferedValues implements RtcQueryAttributePrefferedValues {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public DeliverablePrefferedValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public RtcQueryAttributeValue[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcDeliverablePossibleValues pv = new RtcDeliverablePossibleValues(area);
        ArrayList<RtcQueryAttributeValueImpl> valimpl = new ArrayList<RtcQueryAttributeValueImpl>();
        for (RtcDeliverable cat : pv.getPossibleValues()) {
            if (cat instanceof RtcDeliverableImpl) {
                RtcDeliverableImpl impl = (RtcDeliverableImpl) cat;
                if (!impl.isArchived()) {
                    valimpl.add(new DeliverableValueImpl(impl));
                }
            }
        }
        return valimpl.toArray(new RtcQueryAttributeValue[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof DeliverableValueImpl) {
            return ((DeliverableValueImpl) value).getRtcDeliverable().getName();
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

class DeliverableValueImpl extends RtcQueryAttributeValueImpl {

    private RtcDeliverableImpl wi;

    public DeliverableValueImpl(RtcDeliverableImpl wi) {
        super(wi.getDeliverable());
        this.wi = wi;
    }

    public RtcDeliverableImpl getRtcDeliverable() {
        return wi;
    }
}
