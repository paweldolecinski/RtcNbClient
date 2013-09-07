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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.StatePair;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcWorkFlowStatePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowStateImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowState;

public class InternalStateQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public InternalStateQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new InternalStatePossibleValues(area));
    }
}

class InternalStatePossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private ActiveProjectAreaImpl area;
    private WorkFlowStateValueImpl[] children = new WorkFlowStateValueImpl[]{};
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public InternalStatePossibleValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public StatePairValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcWorkFlowStatePossibleValues pv = new RtcWorkFlowStatePossibleValues(area);
        ArrayList<StatePairValueImpl> impls = new ArrayList<StatePairValueImpl>();
        for (StatePair dl : pv.getPossibleValues()) {
            impls.add(new StatePairValueImpl(dl));
        }
        return impls.toArray(new StatePairValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkFlowStateValueImpl) {
            Image tmpIcon = ((WorkFlowStateValueImpl) value).getRtcWorkFlowState().getIcon();
            if (tmpIcon == null) {
                return icon;
            } else {
                return tmpIcon;
            }
        } else {
            if (value instanceof StatePairValueImpl) {
                return ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/queries/model/internal/workflow_obj.gif");
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof WorkFlowStateValueImpl) {
            return ((WorkFlowStateValueImpl) value).getRtcWorkFlowState().getName();
        } else {
            if (value instanceof StatePairValueImpl) {
                return ((StatePairValueImpl) value).getStatePair().getWorkFlowName();
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public WorkFlowStateValueImpl[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        assert (!EventQueue.isDispatchThread());
        if (value instanceof StatePairValueImpl) {
            ArrayList<WorkFlowStateValueImpl> chs = new ArrayList<WorkFlowStateValueImpl>();
            StatePair line = ((StatePairValueImpl) value).getStatePair();
            for (RtcWorkFlowState impl : line.getStatesList()) {
                if (impl instanceof RtcWorkFlowStateImpl) {
                    chs.add(new WorkFlowStateValueImpl((RtcWorkFlowStateImpl) impl));
                }
            }
            return chs.toArray(new WorkFlowStateValueImpl[]{});
        } else {
            if (value instanceof WorkFlowStateValueImpl) {
                return children;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof String) {
            String id = (String) obj;
            for (StatePairValueImpl pair : getValues()) {
                for (WorkFlowStateValueImpl stateVal : getChildValues(pair)) {
                    RtcWorkFlowStateImpl state = stateVal.getRtcWorkFlowState();
                    RtcWorkFlowStateImpl stateImpl = (RtcWorkFlowStateImpl) state;
                    if (stateImpl.getIdentifier().getStringIdentifier().equals(id)) {
                        return stateVal;
                    }
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof StatePairValueImpl) {
            return false;
        } else {
            if (value instanceof WorkFlowStateValueImpl) {
                return true;
            }
        }
        throw new IllegalArgumentException();
    }
}

class WorkFlowStateValueImpl extends RtcQueryAttributeValueImpl {

    private RtcWorkFlowStateImpl wi;

    public WorkFlowStateValueImpl(RtcWorkFlowStateImpl wi) {
        super(wi.getIdentifier().getStringIdentifier());
        this.wi = wi;
    }

    public RtcWorkFlowStateImpl getRtcWorkFlowState() {
        return wi;
    }
}

class StatePairValueImpl extends RtcQueryAttributeValueImpl {

    private StatePair wi;

    public StatePairValueImpl(StatePair wi) {
        super(null);
        this.wi = wi;
    }

    public StatePair getStatePair() {
        return wi;
    }
}
