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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.variables.IAttributeVariable;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.AttributeTypes;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryAttributeImpl extends RtcQueryAttribute {

    private IQueryableAttribute attribute;
    private RtcAttributeOperationImpl[] attributeOperations;
    private RtcQueryAttributeImpl[] children;
    private final RtcQueryAttributeLookupProvider lookupProvider;
    private InstanceContent content;
    private Lookup lookup;
    private ActiveProjectAreaImpl area;

    public RtcQueryAttributeImpl(IQueryableAttribute attribute, RtcQueryAttributeImpl[] children, RtcQueryAttributeLookupProvider lookupProvider, ActiveProjectAreaImpl area) {
        this.attribute = attribute;
        this.children = children;
        this.area = area;
        this.lookupProvider = lookupProvider;
    }

    public RtcQueryAttributeImpl(IQueryableAttribute attribute, RtcQueryAttributeLookupProvider lookupProvider, ActiveProjectAreaImpl area) {
        this(attribute, new RtcQueryAttributeImpl[]{}, lookupProvider, area);
    }

    @Override
    public RtcAttributeOperationImpl[] getAttributeOperations() {
        if (attributeOperations == null) {

            List<AttributeOperation> operations = attribute.getOperators();
            if (operations.isEmpty()) {
                operations = new LinkedList<AttributeOperation>();
                if (attribute.getAttributeType().equals(AttributeTypes.LARGE_HTML)
                        || attribute.getAttributeType().equals(AttributeTypes.LARGE_STRING)) {
                    operations.add(AttributeOperation.CONTAINS);
                    operations.add(AttributeOperation.NOT_CONTAINS);
                } else {
                    operations.add(AttributeOperation.EXISTS);
                }

            }
            List<RtcAttributeOperationImpl> result = new ArrayList<RtcAttributeOperationImpl>();
            for (AttributeOperation o : operations) {
                result.add(new RtcAttributeOperationImpl(o));
            }
            attributeOperations = result.toArray(new RtcAttributeOperationImpl[]{});
        }
        return attributeOperations;
    }

    public IQueryableAttribute getQueryableAttribute() {
        return attribute;
    }

    @Override
    public String getDisplayName() {
        return attribute.getDisplayName();
    }

    @Override
    public RtcQueryAttributeImpl[] getChildren() {
        return children;
    }

    @Override
    public RtcQueryAttributeType getType() {
        //TODO : for future : maybe we need to add more types
        if (!attribute.isStateExtension()) {
            return RtcQueryAttributeType.BUILD_IN;
        } else {
            return RtcQueryAttributeType.CUSTOM;
        }
    }

    @Override
    public Lookup getLookup() {
        synchronized (lookupProvider) {
            if (lookup == null) {
                content = new InstanceContent();
                lookup = new ProxyLookup(new AbstractLookup(content), lookupProvider.createLookup());
            }
        }
        return lookup;
    }

    @Override
    public RtcQueryAttributeVariableImpl[] getAttributeVariables() {
        ArrayList<RtcQueryAttributeVariableImpl> result = new ArrayList<RtcQueryAttributeVariableImpl>();
        for (IAttributeVariable variable : attribute.getVariables()) {
            result.add(new RtcQueryAttributeVariableImpl(variable));
        }
        return result.toArray(new RtcQueryAttributeVariableImpl[]{});
    }

    @Override
    public RtcQueryAttribute getParent() {
        return attribute.getParent() == null ? RtcQueryAttributeManagerImpl.getFor(area).getQueryAttribute(attribute.getParent()) : null;
    }

    @Override
    public String getAttributeId() {
        return attribute.getIdentifier();
    }
}
