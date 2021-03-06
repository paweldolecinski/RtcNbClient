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
import com.ibm.team.workitem.common.model.IApprovalState;
import com.ibm.team.workitem.common.model.WorkItemApprovals;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;

public class ApprovalStateAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectArea area;

    public ApprovalStateAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new ApprovalStatePossibleValues());
    }
}

class ApprovalStatePossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public ApprovalStatePossibleValues() {
    }

    @Override
    public ApprovalStateValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        List<IApprovalState> states = WorkItemApprovals.getStates();
        ArrayList<ApprovalStateValueImpl> valimpl = new ArrayList<ApprovalStateValueImpl>();
        for (IApprovalState state : states) {
            valimpl.add(new ApprovalStateValueImpl(state));
        }
        return valimpl.toArray(new ApprovalStateValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof ApprovalStateValueImpl) {
            return ((ApprovalStateValueImpl) value).getApprovalState().getDisplayName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        //TODO : bikol : sprawdzic co jest na serwerze string czy apprtype
        if (obj instanceof String) {
            String handle = (String) obj;
            for (ApprovalStateValueImpl val : getValues()) {
                if (val.getApprovalState().getIdentifier().equals(handle)) {
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

class ApprovalStateValueImpl extends RtcQueryAttributeValueImpl {

    private IApprovalState wi;

    public ApprovalStateValueImpl(IApprovalState wi) {
        super(wi.getIdentifier());
        this.wi = wi;
    }

    public IApprovalState getApprovalState() {
        return wi;
    }
}
