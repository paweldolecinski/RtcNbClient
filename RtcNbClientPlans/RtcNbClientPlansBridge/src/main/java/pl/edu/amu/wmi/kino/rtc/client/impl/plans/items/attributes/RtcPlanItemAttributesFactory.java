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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.internal.model.WorkItem;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.awt.Image;
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.checkers.RtcDurationValueCheckerImpl;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values.RtcPlanItemCategoryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values.RtcPlanItemDurationAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values.RtcPlanItemEnumerationAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values.RtcPlanItemTypeAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDuration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcPlanItemAttributesFactory {

    private static WeakHashMap<String, WeakReference<RtcPlanItemAttribute>> ATTRIBUTES =
            new WeakHashMap<String, WeakReference<RtcPlanItemAttribute>>();
    private static final Map<String, String> planItem2WorkItem = new HashMap<String, String>();

    static {
        planItem2WorkItem.put("com.ibm.team.apt.attribute.summary", IWorkItem.SUMMARY_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.owner", IWorkItem.OWNER_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.sequenceValue", WorkItem.SEQUENCE_VALUE_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.id", IWorkItem.ID_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.description", IWorkItem.DESCRIPTION_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.creator", IWorkItem.CREATOR_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.filedAgainst", IWorkItem.CATEGORY_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.plannedFor", IWorkItem.TARGET_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.tags", IWorkItem.TAGS_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.state", IWorkItem.STATE_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.priority", IWorkItem.PRIORITY_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.severity", IWorkItem.SEVERITY_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.estimate", IWorkItem.DURATION_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.originalEstimate", IWorkItem.DURATION_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.correctedEstimate", WorkItem.CORRECTED_ESTIMATE_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.timeSpent", WorkItem.TIME_SPENT_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.dueDate", IWorkItem.DUE_DATE_PROPERTY); //$NON-NLS-1$
        planItem2WorkItem.put("com.ibm.team.apt.attribute.planitem.type", IWorkItem.TYPE_PROPERTY); //$NON-NLS-1$
//<attribute id="com.ibm.team.apt.attribute.planitem.complexity" implementation="com.ibm.team.apt.client.ComplexityAttribute" name="%attribute.name.complexity" type="INTEGER" />
//<attribute id="com.ibm.team.apt.attribute.planitem.progress" implementation="com.ibm.team.apt.client.ProgressAttribute" name="%attribute.name.progress" type="STRING" readOnly="true" />


    }

    public static RtcPlanItemAttribute[] getAllAttributes(ActiveProjectAreaImpl area) {
        List<RtcPlanItemAttribute> res = new ArrayList<RtcPlanItemAttribute>();
        IWorkItemClient wic = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
        try {
            List<IAttribute> findAttributes = wic.findAttributes(area.getProjectArea().getIProcessArea(), null);
            for (IAttribute a : findAttributes) {
                RtcPlanItemAttribute attribute = getAttribute(area, a.getIdentifier());
                res.add(attribute);
            }
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcPlanItemAttributesFactory.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return res.toArray(new RtcPlanItemAttribute[res.size()]);
    }

    public static RtcPlanItemAttribute getAttribute(ActiveProjectAreaImpl area, String id) {

        WeakReference<RtcPlanItemAttribute> ref = ATTRIBUTES.get(id);
        RtcPlanItemAttribute result = (ref != null) ? ref.get() : null;
        if (result == null) {
            RtcPlanItemAttribute data = null;
            String workItemAttrId = planItem2WorkItem.get(id);
            if (id.equals("com.ibm.team.apt.attribute.planitem.complexity")) {
                data = new RtcPlanItemComplexityAttribute();
            } else if (id.equals("com.ibm.team.apt.attribute.planitem.progress")) {
                data = new RtcPlanItemProgressAttribute();
            } else if (id.equals("com.ibm.team.apt.attribute.planitem.state")) {
                data = new RtcPlanItemStateAttribute();
            } else if (workItemAttrId != null) {
                try {
                    IWorkItemClient wic = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
                    IAttribute attribute = wic.findAttribute(area.getProjectArea().getIProcessArea(), workItemAttrId, null);
                    if (attribute != null) {
                        data = getAttribute(attribute, area);
                    }
                } catch (TeamRepositoryException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPlanItemAttributesFactory.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
            }
            if (data != null) {
                ATTRIBUTES.put(data.getAttributeIdentifier(), new WeakReference<RtcPlanItemAttribute>(data));
                result = data;
            }
        }
        return result;

    }

    private static RtcPlanItemAttribute getAttribute(IAttribute attribute, ActiveProjectAreaImpl area) {
        String type = attribute.getAttributeType();

        if (AttributeTypes.isEnumerationAttributeType(type)) {
            return new RtcPlanItemEnumerationAttribute(attribute);

        } else {

            if (type.equals(AttributeTypes.LONG)
                    || type.equals(AttributeTypes.DURATION)) {
                return new RtcPlanItemDurationAttribute(attribute);

            } else if (type.equals(AttributeTypes.SMALL_STRING)
                    || type.equals(AttributeTypes.MEDIUM_STRING)
                    || type.equals(AttributeTypes.LARGE_STRING)
                    || type.equals(AttributeTypes.MEDIUM_HTML)
                    || type.equals(AttributeTypes.LARGE_HTML)
                    || AttributeTypes.HTML_TYPES.contains(type)
                    || AttributeTypes.STRING_TYPES.contains(type)) {
                return new RtcPlanItemStringAttribute(attribute);

            } else if (type.equals(AttributeTypes.TIMESTAMP)) {
                return new RtcPlanItemTimestampAttribute(attribute);

            } else if (type.equals(AttributeTypes.TYPE)) {
                return new RtcPlanItemTypeAttribute(attribute, area);
            } else if (type.equals(AttributeTypes.CATEGORY)) {
                return new RtcPlanItemCategoryAttribute(attribute, area);
            }

            return null;
        }
    }

    private RtcPlanItemAttributesFactory() {
    }

    private static abstract class RtcPlanItemDefaultAttribute<T> extends RtcPlanItemAttribute<T> {

        private final IAttribute attr;
        protected InstanceContent ic;
        private Lookup lookup;

        public RtcPlanItemDefaultAttribute(IAttribute attr) {
            this.attr = attr;
            this.ic = new InstanceContent();
            this.ic.add(attr);
            this.lookup = new AbstractLookup(ic);
        }

        @Override
        public String getAttributeName() {
            return attr.getDisplayName();
        }

        @Override
        public String getAttributeIdentifier() {
            return attr.getIdentifier();
        }

        @Override
        public T getNullValue() {
            return null;
        }

        @Override
        public Lookup getLookup() {
            return lookup;
        }

        @Override
        public boolean isReadOnly() {
            return false;
        }
    }

    private static class RtcPlanItemEnumerationAttribute extends RtcPlanItemDefaultAttribute<RtcLiteral> {

        public RtcPlanItemEnumerationAttribute(IAttribute attr) {
            super(attr);
            ic.add(new RtcPlanItemEnumerationAttributePossibleValues(attr));
        }

        @Override
        public RtcLiteral getNullValue() {
            return RtcLiteral.NullValue;
        }

        @Override
        public Class<RtcLiteral> getValueType() {
            return RtcLiteral.class;
        }
    }

    private static class RtcPlanItemDurationAttribute extends RtcPlanItemDefaultAttribute<RtcDuration> {

        public RtcPlanItemDurationAttribute(IAttribute attr) {
            super(attr);
            ic.add(new RtcPlanItemDurationAttributePossibleValues());
            ic.add(new RtcDurationValueCheckerImpl());
        }

        @Override
        public RtcDuration getNullValue() {
            return RtcDurationImpl.NULL_VALUE;
        }

        @Override
        public Class<RtcDuration> getValueType() {
            return RtcDuration.class;
        }
    }

    private static class RtcPlanItemStringAttribute extends RtcPlanItemDefaultAttribute<String> {

        public RtcPlanItemStringAttribute(IAttribute attr) {
            super(attr);
        }

        @Override
        public String getNullValue() {
            return "";
        }

        @Override
        public Class<String> getValueType() {
            return String.class;
        }
    }

    private static class RtcPlanItemTimestampAttribute extends RtcPlanItemDefaultAttribute<Timestamp> {

        public RtcPlanItemTimestampAttribute(IAttribute attr) {
            super(attr);
        }

        @Override
        public Class<Timestamp> getValueType() {
            return Timestamp.class;
        }
    }

    private static class RtcPlanItemTypeAttribute extends RtcPlanItemDefaultAttribute<RtcWorkItemType> {

        public RtcPlanItemTypeAttribute(IAttribute attr, ActiveProjectAreaImpl area) {
            super(attr);
            ic.add(new RtcPlanItemTypeAttributePossibleValues(area));
        }

        @Override
        public Class<RtcWorkItemType> getValueType() {
            return RtcWorkItemType.class;
        }
    }

        private static class RtcPlanItemCategoryAttribute extends RtcPlanItemDefaultAttribute<RtcCategory> {

        public RtcPlanItemCategoryAttribute(IAttribute attr, ActiveProjectAreaImpl area) {
            super(attr);
            ic.add(new RtcPlanItemCategoryAttributePossibleValues(area));
        }

        @Override
        public Class<RtcCategory> getValueType() {
            return RtcCategory.class;
        }
    }

    private static class RtcPlanItemComplexityAttribute extends RtcPlanItemAttribute<Integer> {

        public RtcPlanItemComplexityAttribute() {
        }

        @Override
        public Class<Integer> getValueType() {
            return Integer.class;
        }

        @Override
        public Integer getNullValue() {
            return 0;
        }

        @Override
        public String getAttributeName() {
            return "Complexity";
        }

        @Override
        public String getAttributeIdentifier() {
            return "com.ibm.team.apt.attribute.planitem.complexity";
        }

        @Override
        public boolean isReadOnly() {
            return false;
        }
    }

    private static class RtcPlanItemProgressAttribute extends RtcPlanItemAttribute<String> {

        public RtcPlanItemProgressAttribute() {
        }

        @Override
        public Class<String> getValueType() {
            return String.class;
        }

        @Override
        public String getNullValue() {
            return "--";
        }

        @Override
        public String getAttributeName() {
            return "Progress";
        }

        @Override
        public String getAttributeIdentifier() {
            return "com.ibm.team.apt.attribute.planitem.progress";
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }
    }

    private static class RtcPlanItemStateAttribute extends RtcPlanItemAttribute<RtcWorkFlow> {

        //TODO: dolek: possible values for state actions
        public RtcPlanItemStateAttribute() {
        }

        @Override
        public Class<RtcWorkFlow> getValueType() {
            return RtcWorkFlow.class;
        }

        @Override
        public RtcWorkFlow getNullValue() {
            return new RtcWorkFlow() {

                @Override
                public String getId() {
                    return "unknowState";
                }

                @Override
                public String getName() {
                    return "Unknow state";
                }

                @Override
                public Image getIcon() {
                    return null;
                }
            };
        }

        @Override
        public String getAttributeName() {
            return "State";
        }

        @Override
        public String getAttributeIdentifier() {
            return "com.ibm.team.apt.attribute.planitem.state";
        }

        @Override
        public boolean isReadOnly() {
            return false;
        }
    }
}
