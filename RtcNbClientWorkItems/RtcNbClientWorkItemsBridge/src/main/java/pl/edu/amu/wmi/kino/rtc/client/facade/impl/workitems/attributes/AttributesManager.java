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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes;

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import java.awt.EventQueue;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider.AttributeContentProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.model.NumericAttributeType;
import com.ibm.team.workitem.common.internal.model.WorkItemAttributes;
import com.ibm.team.workitem.common.model.AttributeType;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.Identifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Deliverable;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemCategory;

public class AttributesManager {

    private static final String ATTRIBUTE_CONTENT_LAYER_PATH = "Rtc/Modules/WorkItems/Impl/AttributeContent";
    protected ActiveProjectAreaImpl area;
    private IWorkItemClient clientLibrary;
    private Map<String, RtcWorkItemAttribute<?>> attrs = new HashMap<String, RtcWorkItemAttribute<?>>();

    /*package*/ AttributesManager(ActiveProjectAreaImpl area) {
        this.area = area;
        this.clientLibrary = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
    }

    public RtcWorkItemAttribute<?>[] getBuiltInAttributes() {
        assert (!EventQueue.isDispatchThread());
        RtcWorkItemAttribute<?>[] toReturn;
        List<IAttributeHandle> list;
        IAttributeHandle i;
        IAttribute j;
        Iterator attributes;
        RtcWorkItemAttribute<?> attribute;

        try {
            list = this.clientLibrary.findBuiltInAttributes(this.area.getProjectArea().getIProcessArea(), null);
            toReturn = new RtcWorkItemAttribute<?>[list.size()];

            attributes = list.iterator();
            int k = 0;
            while (attributes.hasNext()) {

                i = (IAttributeHandle) attributes.next();

                if (i.hasFullState()) {
                    attribute = this.getAttributeById(Identifier.create(IAttribute.class,
                            ((IAttribute) i.getFullState()).getIdentifier()));
                } else {
                    if (i instanceof IAttribute) {
                        attribute = this.getAttributeById(Identifier.create(IAttribute.class,
                                ((IAttribute) i).getIdentifier()));
                    } else {
                        attribute = this.getAttributeById(Identifier.create(IAttribute.class,
                                ((IAttribute) ((ITeamRepository) i.getOrigin()).itemManager().fetchCompleteItem(i, IItemManager.DEFAULT, null)).getIdentifier()));
                    }
                }

                toReturn[k] = attribute;
                k++;
            }

        } catch (Exception ex) {
            //TODO: re-throw exception
                    RtcLogger.getLogger(AttributesManager.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            toReturn = new RtcWorkItemAttribute<?>[0];
        }


        return toReturn;
    }

    public RtcWorkItemAttribute<?>[] getAttributes() {
        assert (!EventQueue.isDispatchThread());
        ArrayList<RtcWorkItemAttribute<?>> toReturn;
        List<IAttribute> list;
        Iterator<IAttribute> attributes;
        RtcWorkItemAttribute<?> attribute;

        try {
            list = this.clientLibrary.findAttributes(this.area.getProjectArea().getIProcessArea(), null);
            toReturn = new ArrayList<RtcWorkItemAttribute<?>>(list.size());

            attributes = list.iterator();
            while (attributes.hasNext()) {
                IAttribute i = attributes.next();
                try {
                    attribute = this.getAttributeById(Identifier.create(IAttribute.class, i.getIdentifier()));
                    toReturn.add(attribute);
                } catch (UnsupportedAttributeIdException ex) {
                    RtcLogger.getLogger(AttributesManager.class).log(Level.FINE, ex.getLocalizedMessage(), ex);
                }
            }
        } catch (Exception ex) {
            //TODO: re-throw exception
                    RtcLogger.getLogger(AttributesManager.class).log(Level.FINE, ex.getLocalizedMessage(), ex);
            toReturn = new ArrayList<RtcWorkItemAttribute<?>>(0);
        }

        return toReturn.toArray(
                new RtcWorkItemAttribute<?>[]{});
    }

    protected <T> RtcWorkItemAttribute<T> getAttributeById(String attributeId,
            Class<T> attrClass, IAttribute ia) {

        InstanceContent ic = new InstanceContent();
        List<Lookup> ls = new LinkedList<Lookup>();


        for (AttributeContentProvider f : Lookups.forPath(
                ATTRIBUTE_CONTENT_LAYER_PATH).lookupAll(
                AttributeContentProvider.class)) {
            Lookup l = f.createLookup(ia, area);

            if (l
                    != null) {
                ls.add(l);
            }
        }
        ls.add(new AbstractLookup(ic));
        ProxyLookup pl = new ProxyLookup(ls.toArray(new Lookup[ls.size()]));
        SimpleAttribute<T> attr = new SimpleAttribute<T>(attrClass, ia, pl);
        attrs.put(attributeId, attr);
        return attr;

    }

    public <T> RtcWorkItemAttribute<T> getAttributeById(String attributeId,
            Class<T> attrClass) throws UnsupportedAttributeIdException {
        RtcWorkItemAttribute<T> findInCache = findInCache(attributeId);
        if (findInCache != null) {
            return findInCache;
        }
        try {
            return getAttributeById(attributeId, attrClass,
                    findIAttribute(attributeId));
        } catch (TeamRepositoryException e) {
            throw new UnsupportedAttributeIdException(attributeId, e);
        }

    }

    public RtcWorkItemAttribute<?> getAttributeById(
            Identifier<IAttribute> attributeId)
            throws UnsupportedAttributeIdException {
        if (attributeId == null) {
            throw new UnsupportedAttributeIdException();
        }
        String attributeIdVal = WorkItemAttributes.getAttributeId(attributeId);
        RtcWorkItemAttribute<?> findInCache = findInCache(attributeIdVal);
        if (findInCache != null) {
            return findInCache;
        }

        IAttribute ia;
        try {
            ia = findIAttribute(attributeIdVal);
        } catch (TeamRepositoryException e) {
            throw new UnsupportedAttributeIdException(attributeIdVal, e);
        }

        if (ia == null) {
            throw new UnsupportedAttributeIdException(attributeIdVal);
        }

        String type = ia.getAttributeType();
        AttributeType at = AttributeTypes.getAttributeType(type);

        // enums


        if (AttributeTypes.isEnumerationAttributeType(type)) {
            return getAttributeById(attributeIdVal, RtcLiteral.class, ia);
        } // primitive types
        else if (type.equals(AttributeTypes.FILE_SIZE)
                || type.equals(AttributeTypes.DURATION)) {
            return getAttributeById(attributeIdVal, Long.class, ia);
        } else if (at instanceof NumericAttributeType) {
            Class<?> instanceType = at.getInstanceType();
            return getAttributeById(attributeIdVal, instanceType, ia);


        } else if (AttributeTypes.STRING_TYPES.contains(type)
                || AttributeTypes.HTML_TYPES.contains(type)) {
            return getAttributeById(attributeIdVal, String.class, ia);
        } else if (type.equals(AttributeTypes.TIMESTAMP)) {
            return getAttributeById(attributeIdVal, Date.class, ia);

        } else if (type.equals(AttributeTypes.TYPE)) {
            return getAttributeById(attributeIdVal, RtcWorkItemType.class, ia);
        } else if (type.equals(AttributeTypes.TAG)) {
            return getAttributeById(attributeIdVal, String.class, ia);
        } else if (type.equals(AttributeTypes.TAGS)) {
            return getAttributeById(attributeIdVal, List.class, ia);
        } // items types
        else if (type.equals(AttributeTypes.CONTRIBUTOR)) {
            return getAttributeById(attributeIdVal, Contributor.class, ia);
        } else if (type.equals(AttributeTypes.CATEGORY)) {
            return getAttributeById(attributeIdVal, RtcWorkItemCategory.class, ia);
        } else if (type.equals(AttributeTypes.ITERATION)) {
            return getAttributeById(attributeIdVal, Iteration.class, ia);
        } else if (type.equals(AttributeTypes.PROCESS_AREA)) {
            return getAttributeById(attributeIdVal, ProcessArea.class, ia);
        } else if (type.equals(AttributeTypes.PROJECT_AREA)) {
            return getAttributeById(attributeIdVal, ProjectArea.class, ia);
        } else if (type.equals(AttributeTypes.TEAM_AREA)) {
            return getAttributeById(attributeIdVal, TeamArea.class, ia);
        } else if (type.equals(AttributeTypes.WORK_ITEM)) {
            return getAttributeById(attributeIdVal, RtcWorkItem.class, ia);
        } else if (type.equals(AttributeTypes.DELIVERABLE)) {
            return getAttributeById(attributeIdVal, Deliverable.class, ia);
        } // lists
        else if (AttributeTypes.isListAttributeType(type)) {
            return getAttributeById(attributeIdVal, List.class, ia);
        }

        throw new UnsupportedAttributeIdException(attributeIdVal);
    }

    private <T> RtcWorkItemAttribute<T> findInCache(String attributeId) {
        if (attrs.containsKey(attributeId)) {
            @SuppressWarnings("unchecked")
            RtcWorkItemAttribute<T> toReturn = (RtcWorkItemAttribute<T>) attrs.get(attributeId);
            return toReturn;
        }
        return null;
    }

    private IAttribute findIAttribute(String attributeId)
            throws TeamRepositoryException {
        return clientLibrary.findAttribute(area.getProjectArea().getIProcessArea(), attributeId, null);
    }
}
