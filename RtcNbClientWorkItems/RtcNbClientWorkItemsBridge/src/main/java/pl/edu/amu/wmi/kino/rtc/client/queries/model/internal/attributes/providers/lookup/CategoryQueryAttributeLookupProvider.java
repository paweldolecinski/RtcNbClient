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
import com.ibm.team.workitem.common.model.ICategoryHandle;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcCategoryPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcCategoryImpl;

public class CategoryQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public CategoryQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new CategoryPossibleValues(area));
    }
}

class CategoryPossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public CategoryPossibleValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public CategoryValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcCategoryPossibleValues pv = new RtcCategoryPossibleValues(area);
        ArrayList<CategoryValueImpl> valimpl = new ArrayList<CategoryValueImpl>();
        for (RtcCategory cat : pv.getPossibleValues()) {
            if (cat instanceof RtcCategoryImpl) {
                RtcCategoryImpl impl = (RtcCategoryImpl) cat;
                valimpl.add(new CategoryValueImpl(impl));
            }
        }
        return valimpl.toArray(new CategoryValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof CategoryValueImpl) {
            return ((CategoryValueImpl) value).getRtcCategory().getName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof ICategoryHandle) {
            ICategoryHandle handle = (ICategoryHandle) obj;
            for (CategoryValueImpl val : getValues()) {
                if (val.getRtcCategory().getCategory().getItemId().getUuidValue().equals(handle.getItemId().getUuidValue())) {
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

class CategoryValueImpl extends RtcQueryAttributeValueImpl {

    private RtcCategoryImpl wi;

    public CategoryValueImpl(RtcCategoryImpl wi) {
        super(wi.getCategory());
        this.wi = wi;
    }

    public RtcCategoryImpl getRtcCategory() {
        return wi;
    }
}
