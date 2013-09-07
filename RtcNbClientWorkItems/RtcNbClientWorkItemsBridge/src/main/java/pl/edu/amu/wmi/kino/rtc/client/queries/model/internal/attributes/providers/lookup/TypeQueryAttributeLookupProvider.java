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
import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcTypePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemTypeImpl;

public class TypeQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectArea area;

    public TypeQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new TypePossibleValues(area));
    }
}

class TypePossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;

    public TypePossibleValues(ActiveProjectArea area) {
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public TypeValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcTypePossibleValues pv = new RtcTypePossibleValues(area);
        ArrayList<TypeValueImpl> valimpl = new ArrayList<TypeValueImpl>();
        for (RtcWorkItemType type : pv.getPossibleValues()) {
            if (type instanceof RtcWorkItemTypeImpl) {
                RtcWorkItemTypeImpl impl = (RtcWorkItemTypeImpl) type;
                valimpl.add(new TypeValueImpl(impl));
            }
        }
        return valimpl.toArray(new TypeValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof TypeValueImpl) {
            return ((TypeValueImpl) value).getRtcType().getIcon();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof TypeValueImpl) {
            return ((TypeValueImpl) value).getRtcType().getDisplayName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }
//TODO : bikol : sprawdzic co jest przechowywane na serwerze

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
//        if (obj instanceof IWorkItemType) {
//            IWorkItemType handle = (IWorkItemType) obj;
//            for (TypeValueImpl val : getValues()) {
//                if (val.getRtcType().getType().getIdentifier().equals(handle.getIdentifier())) {
//                    return val;
//                }
//            }
//        }
        if (obj instanceof String) {
            String handle = (String) obj;
            for (TypeValueImpl val : getValues()) {
                if (val.getRtcType().getType().getIdentifier().equals(handle)) {
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

class TypeValueImpl extends RtcQueryAttributeValueImpl {

    private RtcWorkItemTypeImpl wi;

    public TypeValueImpl(RtcWorkItemTypeImpl wi) {
        super(wi.getType().getIdentifier());
        this.wi = wi;
    }

    public RtcWorkItemTypeImpl getRtcType() {
        return wi;
    }
}
