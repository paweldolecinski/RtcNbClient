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
package pl.edu.amu.wmi.kino.rtc.client.workitems.providers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet.RtcWorkItemAttributeSection;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSetFactory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemCategoryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemContributorAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemDeliverableAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemDurationAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemEnumerationAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemHistoryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemIAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemIterationAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemSubscriptionsAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemTagsAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemTeamAndProjectAreaAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemTimestampAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemTypeAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl.RtcWorkItemWorkFlowStateAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcCategoryPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcContributorPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcContributorPrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcDeliverablePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcDurationValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcEnumerationPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcIterationPrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcProjectAreaPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcTeamAreaPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcTypePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcWorkFlowActionPrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcDurationImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcHistoryContentImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowInfoImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkFlowStateImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;

import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.internal.WorkItemCommon;
import com.ibm.team.workitem.common.internal.model.WorkItemAttributes;
import com.ibm.team.workitem.common.internal.presentations.AbstractPresentationDescriptor;
import com.ibm.team.workitem.common.internal.presentations.EditorPresentation;
import com.ibm.team.workitem.common.internal.presentations.PresentationDescriptor;
import com.ibm.team.workitem.common.internal.presentations.SectionDescriptor;
import com.ibm.team.workitem.common.internal.presentations.TabDescriptor;
import com.ibm.team.workitem.common.internal.util.DelegatingItemList;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.Identifier;

/**
 *
 * @author psychollek
 * @author Pawel Dolecinski
 */
@Deprecated
@ServiceProvider(service = RtcWorkItemAttributeSetFactory.class)
public class RtcWorkItemAttributeSetFactoryImpl implements RtcWorkItemAttributeSetFactory {

    /**
     * this method inspects the context for objects it needs.
     * @param context - context from the information about attributes should be retrieved
     * @return true if objects necesary for creation of Attribute set are present
     */
    @Override
    public boolean canCreateFromContext(Lookup context) {
        if (context.lookup(RtcWorkItem.class) != null && context.lookup(ActiveProjectArea.class) != null) {
            return true;
        }
        return false;
    }

    /**
     * this method creates RtcWorkItemAttributeSet from context provided. It can return null
     * if canCreateFromContext(context) = false.
     * @param context
     * @return set of attributes created from context or null if canCreateFromContext(context) = false;
     */
    @Override
    public RtcWorkItemAttributeSet createAttributeSet(Lookup context) {
        RtcWorkItem workitem = context.lookup(RtcWorkItem.class);
        ActiveProjectAreaImpl activeProjectArea = context.lookup(ActiveProjectAreaImpl.class);
        RtcWorkItemAttributeSet set = new RtcWorkItemAttributeSet();
        if (workitem instanceof RtcWorkItemImpl) {
            RtcWorkItemImpl wi = (RtcWorkItemImpl) workitem;
            ITeamRepository repo = (ITeamRepository) wi.getWorkItem().getOrigin();
            IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
            try {
                List<IAttribute> attrs = new ArrayList<IAttribute>();
                if(activeProjectArea != null)
                    attrs = workItemClient.findAttributes(activeProjectArea.getProjectArea().getIProcessArea(), null);


                EditorPresentation editor = ((WorkItemCommon) workItemClient).getEditorPresentation(wi.getWorkItem(), null);
                Map<String, String> lays = editor.getTabLayouts();

                List<AbstractPresentationDescriptor> tabs = editor.getPresentationsMap().get(editor.getEditorLayout());
                for (AbstractPresentationDescriptor tab : tabs) {
                    String lay = lays.get(tab.getElementId());
                    RtcWorkItemAttributeSet.CategoryLayout layout = RtcWorkItemAttributeSet.CategoryLayout.CUSTOM;
                    if (lay != null) {
                        if (lay.equals("internalHeader")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.HEADER;
                        } else if (lay.equals("builtInOverviewLayout")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.OVERVIEW;
                        } else if (lay.equals("builtInLinksLayout")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.LINKS;
                        } else if (lay.equals("builtInApprovalsLayout")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.APPROVALS;
                        } else if (lay.equals("builtInHistoryLayout")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.HISTORY;
                        } else if (lay.equals("builtInHLayout")) {
                            layout = RtcWorkItemAttributeSet.CategoryLayout.H_TAB;
                        }
                    }
                    RtcWorkItemAttributeCategory cat = set.new RtcWorkItemAttributeCategory(((TabDescriptor) tab).getTitle(), layout);
                    set.add(cat);

                    List<AbstractPresentationDescriptor> sections = editor.getPresentationsMap().get(tab.getElementId());
                    if (sections != null) {

                        for (AbstractPresentationDescriptor s : sections) {
                            String slot = ((SectionDescriptor) s).getSlot();
                            RtcWorkItemAttributeSet.SectionSlot slotSection = RtcWorkItemAttributeSet.SectionSlot.NONE_SECTION;
                            if (slot != null) {
                                if (slot.equals("details")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.DETAILS;
                                } else if (slot.equals("description")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.DESCRIPTION;
                                } else if (slot.equals("discussion")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.DISCUSSION;
                                } else if (slot.equals("quickInfo")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.QUICKINFO;
                                } else if (slot.equals("attachments")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.ATTACHMENTS;
                                } else if (slot.equals("subscribers")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.SUBSCRIBERS;
                                } else if (slot.equals("links")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.LINKS;
                                } else if (slot.equals("left")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.LEFT;
                                } else if (slot.equals("right")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.RIGHT;
                                } else if (slot.equals("top")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.TOP;
                                } else if (slot.equals("bottom")) {
                                    slotSection = RtcWorkItemAttributeSet.SectionSlot.BOTTOM;
                                }
                            }
                            RtcWorkItemAttributeSection sec = set.new RtcWorkItemAttributeSection(((SectionDescriptor) s).getTitle(), slotSection);
                            cat.add(sec);

                            List<AbstractPresentationDescriptor> descriptions = editor.getPresentationsMap().get(s.getElementId());
                            if (descriptions != null) {

                                for (AbstractPresentationDescriptor d : descriptions) {
                                    PresentationDescriptor desc = (PresentationDescriptor) d;
                                    String kind = desc.getKind();
                                    IAttribute attribute = getAttribute(wi.getWorkItem(), desc);
                                    if (attribute != null && wi.getWorkItem().hasAttribute(attribute)) {
                                        if (attrs.contains(attribute)) {
                                            attrs.remove(attribute);
                                        }
                                        RtcWorkItemIAttribute attr = getRtcIAttribute(wi, attribute, activeProjectArea);
                                        if (attr != null) {
                                            wi.addWorkingCopyListener(attr.new WorkingCopyListener());
                                            sec.add(attr);
                                        }
                                    } else {
                                        RtcWorkItemAttribute rtcAttribute = getRtcAttribute(wi, kind, activeProjectArea, context);
                                        if (rtcAttribute != null) {
                                            sec.add(rtcAttribute);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                // in this place i'm adding to set a few important attributes
                // that have not occurred previously.
                RtcWorkItemAttributeSection section = set.get(0).get(0);
                for (IAttribute attr : attrs) {
                    if (wi.getWorkItem().hasAttribute(attr) && (attr.getIdentifier().equals("creationDate")
                            || attr.getIdentifier().equals("id")
                            //|| attr.getIdentifier().equals("modified")
                            || attr.getIdentifier().equals("creator")
                            || attr.getIdentifier().equals("owner")
                            || attr.getIdentifier().equals("workItemType")
                            || attr.getIdentifier().equals("internalPriority")
                            || attr.getIdentifier().equals("internalSeverity"))) {
                        RtcWorkItemIAttribute a = getRtcIAttribute(wi, attr, activeProjectArea);
                        if (attr != null) {
                            wi.addWorkingCopyListener(a.new WorkingCopyListener());
                            section.add(a);
                        }
                    }
                }
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
            }
        }
        return set;
    }

    private RtcWorkItemAttribute getRtcAttribute(RtcWorkItemImpl wi, String kind, ActiveProjectAreaImpl activeProjectArea, Lookup context) {

        RtcWorkItemAttribute attr = null;
        if (kind.equals("com.ibm.team.workitem.kind.internal.discussion")) {
//            attr = new RtcWorkItemDiscussionAttribute(
//                    Collection.class, RtcWorkItemDiscussionAttribute.getStringProvider(), wi, new RtcCommentsImpl(wi.getWorkItem()));
//            wi.addWorkingCopyListener(((RtcWorkItemDiscussionAttribute) attr).new WorkingCopyDiscussionListener());
        } else if (kind.equals("com.ibm.team.workitem.kind.internal.teamarea")) {
            attr = new RtcWorkItemTeamAndProjectAreaAttribute(
                    RtcCategory.class, RtcWorkItemTeamAndProjectAreaAttribute.getStringProvider(), wi);
            wi.addWorkingCopyListener(((RtcWorkItemTeamAndProjectAreaAttribute) attr).new WorkingCopyTeamAreaListener());
        } else if (kind.equals("com.ibm.team.workitem.kind.workflow.state")) {
            RtcWorkFlowInfoImpl rtcWorkFlowInfo = new RtcWorkFlowInfoImpl(wi.getWorkItem());
            attr = new RtcWorkItemWorkFlowStateAttribute(
                    RtcWorkFlow.class, RtcWorkItemWorkFlowStateAttribute.getStringProvider(), new RtcWorkFlowStateImpl(rtcWorkFlowInfo, wi.getWorkItem().getState2()),
                    wi, new RtcWorkFlowActionPrefferedValues(rtcWorkFlowInfo));
            wi.addWorkingCopyListener(((RtcWorkItemWorkFlowStateAttribute) attr).new WorkingCopyStateListener());

//        } else if (kind.equals("com.ibm.team.workitem.kind.workflow.resolution")) {
//            RtcWorkFlowInfoImpl rtcWorkFlowInfo = new RtcWorkFlowInfoImpl(wi.getWorkItem());
//            RtcWorkFlowResolutionPrefferedValues pref = new RtcWorkFlowResolutionPrefferedValues(rtcWorkFlowInfo, context.lookup(WorkItemWorkingCopy.class));
//            attr = new RtcWorkItemWorkFlowResolutionAttribute(
//                    RtcWorkFlow.class, RtcWorkItemWorkFlowResolutionAttribute.getStringProvider(), new RtcWorkFlowResolutionImpl(rtcWorkFlowInfo, wi.getWorkItem().getResolution2()),
//                    wi, pref);
//            wi.addWorkingCopyListener(((RtcWorkItemWorkFlowResolutionAttribute) attr).new WorkingCopyActionListener());

//        } else if (kind.equals("com.ibm.team.workitem.kind.internal.links")) {
//            attr = new RtcWorkItemLinksAttribute(RtcLinks.class, RtcWorkItemLinksAttribute.getStringProvider(), new RtcLinksImpl(context.lookup(WorkItemWorkingCopy.class)));
//            wi.addWorkingCopyListener(((RtcWorkItemLinksAttribute) attr).new WorkingCopyLinksListener());

//        } else if (kind.equals("com.ibm.team.workitem.kind.internal.attachments")) {
//            attr = new RtcAttachmentsAttribute(Collection.class, RtcAttachmentsAttribute.getStringProvider(), wi, new RtcAttachmentsImpl(wi));
//            wi.addWorkingCopyListener(((RtcAttachmentsAttribute) attr).new WorkingCopyAttachmentsListener());

        } else if (kind.equals("com.ibm.team.workitem.kind.internal.approvals")) {
//            attr = new RtcWorkItemApprovalsAttribute(RtcApproval.class, RtcWorkItemApprovalsAttribute.getStringProvider(), wi, new RtcApprovalsImpl(wi.getWorkItem()), new RtcContributorPossibleValues(activeProjectArea));
//            wi.addWorkingCopyListener(((RtcWorkItemApprovalsAttribute) attr).new WorkingCopyApprovalsListener());

        } else if (kind.equals("com.ibm.team.workitem.kind.internal.history")) {
            attr = new RtcWorkItemHistoryAttribute(RtcHistoryContent.class,
                    RtcWorkItemHistoryAttribute.getStringProvider(), new RtcHistoryContentImpl(wi.getWorkItem()), wi);
            wi.addWorkingCopyListener(((RtcWorkItemHistoryAttribute) attr).new WorkingCopyHistoryListener());
        }
        return attr;
    }

    private RtcWorkItemIAttribute getRtcIAttribute(RtcWorkItemImpl wi, IAttribute attribute, ActiveProjectAreaImpl activeProjectArea) {

        String type = attribute.getAttributeType();

        if (AttributeTypes.isEnumerationAttributeType(type)) {

            RtcWorkItemIAttribute attr = new RtcWorkItemEnumerationAttribute(
                    RtcLiteral.class, RtcWorkItemEnumerationAttribute.getStringProvider(), attribute, wi, new RtcEnumerationPossibleValues(attribute), RtcWorkItemEnumerationAttribute.getIconProvider());
            return attr;

        } else {

            RtcWorkItemIAttribute<? extends Object> attr = null;
            if (type.equals(AttributeTypes.CONTRIBUTOR)) {
                attr = new RtcWorkItemContributorAttribute(
                        Contributor.class, RtcWorkItemContributorAttribute.getStringProvider(), attribute, wi,
                        new RtcContributorPossibleValues(activeProjectArea), new RtcContributorPrefferedValues(wi.getCategory().getAssociatedTeamAreas(), activeProjectArea));

            } else if (type.equals(AttributeTypes.LONG)
                    || type.equals(AttributeTypes.DURATION)) {
                attr = new RtcWorkItemDurationAttribute(
                        RtcDurationImpl.class, RtcWorkItemDurationAttribute.getStringProvider(), attribute, wi, new RtcDurationValueChecker());
            } else if (type.equals(AttributeTypes.SMALL_STRING)
                    || type.equals(AttributeTypes.MEDIUM_STRING)
                    || type.equals(AttributeTypes.LARGE_STRING)
                    || AttributeTypes.STRING_TYPES.contains(type)) {
                attr = new RtcWorkItemIAttribute<String>(
                        String.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi);
            } else if (type.equals(AttributeTypes.MEDIUM_HTML)) {
                attr = new RtcWorkItemIAttribute<String>(
                        String.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi);
            } else if (type.equals(AttributeTypes.LARGE_HTML)
                    || AttributeTypes.HTML_TYPES.contains(type)) {
                attr = new RtcWorkItemIAttribute<String>(
                        String.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi);
            } else if (type.equals(AttributeTypes.TIMESTAMP)) {
                attr = new RtcWorkItemTimestampAttribute(
                        Timestamp.class, RtcWorkItemTimestampAttribute.getStringProvider(), attribute, wi);
            } else if (type.equals(AttributeTypes.CONTRIBUTOR_LIST)) {
                attr = new RtcWorkItemIAttribute<DelegatingItemList>(
                        DelegatingItemList.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi);

            } else if (type.equals(AttributeTypes.CATEGORY)) {
                attr = new RtcWorkItemCategoryAttribute(
                        RtcCategory.class, RtcWorkItemCategoryAttribute.getStringProvider(), attribute, wi, new RtcCategoryPossibleValues(activeProjectArea));

            } else if (type.equals(AttributeTypes.ITERATION)) {
                attr = new RtcWorkItemIterationAttribute(
                        Iteration.class, RtcWorkItemIterationAttribute.getStringProvider(), attribute, wi, new RtcIterationPrefferedValues(activeProjectArea));

            } else if (type.equals(AttributeTypes.SUBSCRIPTIONS)) {
                attr = new RtcWorkItemSubscriptionsAttribute(
                        Collection.class, RtcWorkItemSubscriptionsAttribute.getStringProvider(), attribute, wi, new RtcContributorPossibleValues(activeProjectArea));

            } else if (type.equals(AttributeTypes.TAGS)) {
                attr = new RtcWorkItemTagsAttribute(
                        List.class, RtcWorkItemTagsAttribute.getStringProvider(), attribute, wi);

            } else if (type.equals(AttributeTypes.TYPE)) {
                attr = new RtcWorkItemTypeAttribute(
                        RtcWorkItemType.class, RtcWorkItemTypeAttribute.getStringProvider(), attribute, wi, new RtcTypePossibleValues(activeProjectArea), RtcWorkItemTypeAttribute.getIconProvider());

            } else if (type.equals(AttributeTypes.PROJECT_AREA)) {
                attr = new RtcWorkItemIAttribute<IProjectAreaHandle>(
                        IProjectAreaHandle.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi, new RtcProjectAreaPossibleValues(activeProjectArea));

            } else if (type.equals(AttributeTypes.TEAM_AREA)) {
                attr = new RtcWorkItemIAttribute<ITeamAreaHandle>(
                        ITeamAreaHandle.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi, new RtcTeamAreaPossibleValues(activeProjectArea));

            } else if (type.equals(AttributeTypes.WORK_ITEM_LIST)) {
                //attr = new RtcWorkItemIAttribute<IProjectAreaHandle>(
                //    IProjectAreaHandle.class, RtcProjectAreaEditor.getStringProvider(), attribute, wi);
            } else if (type.equals(AttributeTypes.WORK_ITEM)) {
                attr = new RtcWorkItemIAttribute<IWorkItemHandle>(
                        IWorkItemHandle.class, RtcWorkItemIAttribute.getStringProvider(), attribute, wi);

            } else if (type.equals(AttributeTypes.DELIVERABLE)) {
                attr = new RtcWorkItemDeliverableAttribute(
                        RtcDeliverable.class, RtcWorkItemDeliverableAttribute.getStringProvider(), attribute, wi, new RtcDeliverablePossibleValues(activeProjectArea));
            } else if (type.equals(AttributeTypes.CUSTOM_ATTRIBUTE)) {
            } else if (type.equals(AttributeTypes.APPROVALS)) {
            } else if (type.equals(AttributeTypes.COMMENTS)) {
            } else {
                /*
                 * Basic types: int, string and... custom attribute, html
                 */
                Class T = ((RtcWorkItemImpl) wi).getWorkItem().getValue(attribute) == null ? null : ((RtcWorkItemImpl) wi).getWorkItem().getValue(attribute).getClass();
                attr = new RtcWorkItemIAttribute<Object>(
                        T,
                        new RtcWorkItemIAttribute.ToStringProvider<Object>() {

                            @Override
                            public String toString(Object value) {
                                return value.toString();
                            }
                        }, attribute, wi);
            }

            return attr;
        }
    }

    private IAttribute getAttribute(IWorkItem item, PresentationDescriptor desc) throws TeamRepositoryException {
        ITeamRepository repository = (ITeamRepository) item.getOrigin();
        WorkItemCommon wiCommon = (WorkItemCommon) repository.getClientLibrary(IWorkItemCommon.class);

        Identifier<IAttribute> attributeId = desc.getAttributeId();
        if (attributeId == null) {
            return null;
        }
        String attributeIdVal = WorkItemAttributes.getAttributeId(attributeId);
        return wiCommon.findAttribute(item.getProjectArea(), attributeIdVal, null);
    }
}
