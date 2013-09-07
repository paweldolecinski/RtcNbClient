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
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcEnumerationPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcLiteralImpl;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.internal.WorkItemClient;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.Identifier;

public class EnumerationQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public EnumerationQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.area = (ActiveProjectAreaImpl) area;
        this.attribute = attribute;
    }

    @Override
    public Lookup createLookup() {
        WorkItemClient wiClient = (WorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
        IAttribute iAttribute;
        try {
            iAttribute = wiClient.findAttribute(area.getProjectArea().getIProcessArea(), attribute.getIdentifier(), null);
            return Lookups.singleton(new EnumerationPossibleValues(area, iAttribute));
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(EnumerationQueryAttributeLookupProvider.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return Lookup.EMPTY;
        }
    }
}

class EnumerationPossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectArea area;
    private IAttribute iAttribute;

    public EnumerationPossibleValues(ActiveProjectArea area, IAttribute attribute) {
        this.area = area;
        this.iAttribute = attribute;
    }

    @Override
    public EnumerationValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        ArrayList<EnumerationValueImpl> valimpl =
                new ArrayList<EnumerationValueImpl>();
        RtcEnumerationPossibleValues poss = new RtcEnumerationPossibleValues(iAttribute);

        for (RtcLiteral l : poss.getPossibleValues()) {
            if (l instanceof RtcLiteralImpl) {
                RtcLiteralImpl li = (RtcLiteralImpl) l;
                valimpl.add(new EnumerationValueImpl(li));
            }
        }
        return valimpl.toArray(new EnumerationValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof EnumerationValueImpl) {
            Image icon = ((EnumerationValueImpl) value).getRtcLiteral().getIcon();
            if (icon != null) {
                return ((EnumerationValueImpl) value).getRtcLiteral().getIcon();
            } else {
                return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof EnumerationValueImpl) {
            return ((EnumerationValueImpl) value).getRtcLiteral().getName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
//        if (obj instanceof ILiteral) {
//            ILiteral handle = (ILiteral) obj;
//            for (EnumerationValueImpl val : getValues()) {
//                if (val.getRtcLiteral().getLiteral().getIdentifier2().getStringIdentifier().equals(handle.getIdentifier2().getStringIdentifier())) {
//                    return val;
//                }
//            }
//        }
        if (obj instanceof Identifier) {
            Identifier handle = (Identifier) obj;
            for (EnumerationValueImpl val : getValues()) {
                if (val.getRtcLiteral().getLiteral().getIdentifier2().getStringIdentifier().equals(handle.getStringIdentifier())) {
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

class EnumerationValueImpl extends RtcQueryAttributeValueImpl {

    private RtcLiteralImpl l;

    public EnumerationValueImpl(RtcLiteralImpl l) {
        super(l.getLiteral().getIdentifier2());
        this.l = l;
    }

    public RtcLiteralImpl getRtcLiteral() {
        return l;
    }
}
