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
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeBooleanValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;

public class BooleanAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectArea area;

    public BooleanAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new BooleanValueProvider());
    }
}

class BooleanValueProvider implements RtcQueryAttributeBooleanValueProvider, ValueCreator {

    private RtcQueryAttributeValueImpl trueValue = new BooleanValueImpl(Boolean.TRUE);
    private RtcQueryAttributeValueImpl falseValue = new BooleanValueImpl(Boolean.FALSE);
    private RtcQueryAttributeValueImpl[] values = new RtcQueryAttributeValueImpl[]{trueValue, falseValue};
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private RtcQueryAttributeValueImpl[] children = new RtcQueryAttributeValueImpl[]{};
    private String trueName = NbBundle.getMessage(BooleanValueProvider.class, "BooleanValueProvider.true.name");
    private String falseName = NbBundle.getMessage(BooleanValueProvider.class, "BooleanValueProvider.false.name");

    @Override
    public RtcQueryAttributeValue getTrue() {
        return trueValue;
    }

    @Override
    public RtcQueryAttributeValue getFalse() {
        return falseValue;
    }

    @Override
    public RtcQueryAttributeValue[] getValues() {
        return values;
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value.equals(trueValue)) {
            return trueName;
        } else {
            if (value.equals(falseValue)) {
                return falseName;
            } else {
                throw new IllegalArgumentException(NbBundle.getMessage(BooleanValueProvider.class, "BooleanValueProvider.error"));
            }
        }
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof Boolean) {
            if (obj.equals(Boolean.TRUE)) {
                return getTrue();
            } else {
                return getFalse();
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return true;
    }
}

class BooleanValueImpl extends RtcQueryAttributeValueImpl {

    private Boolean val;

    public BooleanValueImpl(Boolean rtcValue) {
        super(rtcValue);
    }

    public Boolean getBoolean() {
        return val;
    }
}
