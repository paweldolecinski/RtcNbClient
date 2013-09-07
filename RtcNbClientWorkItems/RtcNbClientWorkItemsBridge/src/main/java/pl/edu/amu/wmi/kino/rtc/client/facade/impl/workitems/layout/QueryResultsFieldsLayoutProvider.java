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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.layout;

import com.ibm.team.workitem.common.model.IWorkItem;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout.WorkItemTab;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout.WorkItemTabSection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.AttributesManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.SimpleAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.layout.api.WorkItemLayoutProvider;

/**
 *
 * @author Patryk Żywica
 */
public class QueryResultsFieldsLayoutProvider implements WorkItemLayoutProvider {

    public WorkItemFieldsLayout createFieldsLayout(IWorkItem wi, ActiveProjectAreaImpl area, RtcWorkItemViewTarget target) {
        if (target.equals(RtcWorkItemViewTarget.QUERIES)) {
            return createEditorLayout(wi, area);
        }
        return null;
    }

    
    private WorkItemFieldsLayout createEditorLayout(IWorkItem wi, ActiveProjectAreaImpl area) {
        AttributesManager attrsManager = area.getLookup().lookup(AttributesManager.class);
        WorkItemFieldsLayout layout = new WorkItemFieldsLayout();
        WorkItemTab tab = layout.new WorkItemTab("Attributes", WorkItemFieldsLayout.TabLayout.OVERVIEW);
        layout.add(tab);
        WorkItemTabSection sec = layout.new WorkItemTabSection("Attributes", WorkItemFieldsLayout.SectionSlot.NONE_SECTION);
        tab.add(sec);
        for (RtcWorkItemAttribute<?> aaa : attrsManager.getAttributes()) {
            if (aaa instanceof SimpleAttribute<?>) {
                SimpleAttribute<?> attr = (SimpleAttribute<?>) aaa;
                if (wi.hasAttribute(attr.getIAttribute()) && !attr.getIAttribute().isInternal()) {
                    sec.add(WorkItemLayoutField.createField(WorkItemLayoutField.Type.ATTRIBUTE,
                            Lookups.fixed(attr)));
                }
            }
        }
        return layout;
    }
//    private WorkItemFieldsLayout createEditorLayout(IWorkItem wi, ActiveProjectAreaImpl area) {
//        AttributesManager attrsManager = area.getLookup().lookup(AttributesManager.class);
//        ITeamRepository repo = (ITeamRepository) wi.getOrigin();
//        IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
//        try {
//            WorkItemFieldsLayout layout = new WorkItemFieldsLayout();
//            // TODO research on targets
//            // List<BindingTarget> targets = EditorPresentationManager
//            // .getBindingTargets();
//            // targets.get(0).getTargetId();
//            // EditorPresentation editor = ((WorkItemCommon) workItemClient)
//            // .getEditorPresentation("", wi, null);
//            EditorPresentation editor = ((WorkItemCommon) workItemClient).getEditorPresentation(wi, null);
//
//            Map<String, String> lays = editor.getTabLayouts();
//
//            List<AbstractPresentationDescriptor> tabs = editor.getPresentationsMap().get(editor.getEditorLayout());
//            for (AbstractPresentationDescriptor tabDesc : tabs) {
//                String lay = lays.get(tabDesc.getElementId());
//                WorkItemFieldsLayout.TabLayout tabLayout = WorkItemFieldsLayout.TabLayout.CUSTOM;
//                if (lay != null) {
//                    if (lay.equals("internalHeader")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.HEADER;
//                    } else if (lay.equals("builtInOverviewLayout")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.OVERVIEW;
//                    } else if (lay.equals("builtInLinksLayout")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.LINKS;
//                    } else if (lay.equals("builtInApprovalsLayout")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.APPROVALS;
//                    } else if (lay.equals("builtInHistoryLayout")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.HISTORY;
//                    } else if (lay.equals("builtInHLayout")) {
//                        tabLayout = WorkItemFieldsLayout.TabLayout.H_TAB;
//                    }
//                }
//                WorkItemTab tab = layout.new WorkItemTab(
//                        ((TabDescriptor) tabDesc).getTitle(), tabLayout);
//                layout.add(tab);
//
//                List<AbstractPresentationDescriptor> sections = editor.getPresentationsMap().get(tabDesc.getElementId());
//                if (sections != null) {
//
//                    for (AbstractPresentationDescriptor s : sections) {
//                        String slot = ((SectionDescriptor) s).getSlot();
//                        WorkItemFieldsLayout.SectionSlot slotSection = WorkItemFieldsLayout.SectionSlot.NONE_SECTION;
//                        if (slot != null) {
//                            if (slot.equals("details")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.DETAILS;
//                            } else if (slot.equals("description")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.DESCRIPTION;
//                            } else if (slot.equals("discussion")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.DISCUSSION;
//                            } else if (slot.equals("quickInfo")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.QUICKINFO;
//                            } else if (slot.equals("attachments")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.ATTACHMENTS;
//                            } else if (slot.equals("subscribers")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.SUBSCRIBERS;
//                            } else if (slot.equals("links")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.LINKS;
//                            } else if (slot.equals("left")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.LEFT;
//                            } else if (slot.equals("right")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.RIGHT;
//                            } else if (slot.equals("top")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.TOP;
//                            } else if (slot.equals("bottom")) {
//                                slotSection = WorkItemFieldsLayout.SectionSlot.BOTTOM;
//                            }
//                        }
//                        WorkItemTabSection sec = layout.new WorkItemTabSection(
//                                ((SectionDescriptor) s).getTitle(), slotSection);
//                        tab.add(sec);
//
//                        List<AbstractPresentationDescriptor> descriptions = editor.getPresentationsMap().get(s.getElementId());
//                        if (descriptions != null) {
//
//                            for (AbstractPresentationDescriptor d : descriptions) {
//                                PresentationDescriptor desc = (PresentationDescriptor) d;
//                                try {
//                                    sec.add(WorkItemLayoutField.createField(
//                                            WorkItemLayoutField.Type.ATTRIBUTE,
//                                            Lookups.singleton(attrsManager.getAttributeById(desc.getAttributeId()))));
//                                } catch (UnsupportedAttributeIdException e) {
//                                    //No action for non attribute
//                                }
//                            }
//                        }
//                        try {
//                            sec.add(WorkItemLayoutField.createField(WorkItemLayoutField.Type.ATTRIBUTE, 
//                                    Lookups.fixed(attrsManager.getAttributeById(IWorkItem.SUMMARY_PROPERTY, String.class))));
//                            sec.add(WorkItemLayoutField.createField(WorkItemLayoutField.Type.ATTRIBUTE, 
//                                    Lookups.fixed(attrsManager.getAttributeById(IWorkItem.ID_PROPERTY, Integer.class))));
//                        } catch (UnsupportedAttributeIdException ex) {
//                            Exceptions.printStackTrace(ex);
//                        }
//                    }
//                }
//            }
//            return layout;
//        } catch (TeamRepositoryException e) {
//            // TODO Logger and re-throw exception
//            RtcLogger.getLogger().severe(
//                    "Cannot get layout for WI " + wi.getId());
//        }
//
//        return null;
//    }
}
