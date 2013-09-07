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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.Type;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcApprovalsEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcAttachmentsEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcCategoryEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcCommentsEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcContributorEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcDeliverableEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcDurationEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcEnumerationEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcHistoryEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcIterationEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcLargeHtmlEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcMediumHtmlEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcProjectAndTeamAreaEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcProjectAreaEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcSubscriptionsEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcTagsEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcTeamAreaEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcTimestampEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcTypeEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcWorkFlowResolutionEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcWorkFlowStateEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.RtcWorkItemEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.editors.ContextPropertyEditorFactory;

/**
 *
 * @author psychollek
 * @author Pawel Dolecinski
 */
@ServiceProvider(service = ContextPropertyEditorFactory.class)
public class ContextPropertyEditorFactoryImpl implements ContextPropertyEditorFactory {

    /**
     * this method returns an editor to property found in context. it uses it's
     * best guess to do so and returns null if no editor can be created by it.
     * @param context
     * @return editor for the property in context
     */
    @Override
    public PropertyEditor createPropertyEditorFromContext(Lookup context) {
        PropertyEditorSupport editor = null;

        RtcWorkItemAttribute attr = context.lookup(RtcWorkItemAttribute.class);

        if (attr != null) {

            Type type = attr.getAttributeType();
            if (type.equals(Type.ENUMERATION)) {
                editor = new RtcEnumerationEditor(context);
                
            } else if (type.equals(Type.CATEGORY)) {
                editor = new RtcCategoryEditor(context);

            } else if (type.equals(Type.ITERATION)) {
                editor = new RtcIterationEditor(context);

            } else if (type.equals(Type.SUBSCRIPTIONS)) {
                editor = new RtcSubscriptionsEditor(context);

            } else if (type.equals(Type.TYPE)) {
                editor = new RtcTypeEditor(context);

            } else if (type.equals(Type.TAGS)) {
                editor = new RtcTagsEditor(context);

            } else if (type.equals(Type.DURATION)) {
                editor = new RtcDurationEditor(context);

            } else if (type.equals(Type.MEDIUM_HTML)) {
                editor = new RtcMediumHtmlEditor(context);

            } else if (type.equals(Type.LARGE_HTML)) {
                editor = new RtcLargeHtmlEditor(context);

            } else if (type.equals(Type.MEDIUM_STRING)
                    || type.equals(Type.LARGE_STRING)) {

                editor = new RtcLargeHtmlEditor(context);
            } else if (type.equals(Type.PROJECT_AREA)) {
                editor = new RtcProjectAreaEditor(context);

            } else if (type.equals(Type.TEAM_AREA)) {
                editor = new RtcTeamAreaEditor(context);

            } else if (type.equals(Type.WORK_ITEM)) {
                editor = new RtcWorkItemEditor(context);

            } else if (type.equals(Type.TIMESTAMP)) {
                editor = new RtcTimestampEditor(context);

            } else if (type.equals(Type.DELIVERABLE)) {
                editor = new RtcDeliverableEditor(context);

            } else if (type.equals(Type.CONTRIBUTOR)) {
                editor = new RtcContributorEditor(context);

            } else if (type.equals(Type.COMMENTS)) {
                editor = new RtcCommentsEditor(context);

            } else if (type.equals(Type.APPROVALS)) {
                editor = new RtcApprovalsEditor(context);

            } else if (type.equals(Type.ATTACHMENTS)) {
                editor = new RtcAttachmentsEditor(context);

            } else if (type.equals(Type.LINKS)) {
                //editor = new RtcLinksEditor(context);

            } else if (type.equals(Type.FLOW_STATE)) {
                editor = new RtcWorkFlowStateEditor(context);

            } else if (type.equals(Type.FLOW_RESOLUTION)) {
                editor = new RtcWorkFlowResolutionEditor(context);

//            } else if (context.lookup(IWorkItemReferences.class) != null) {
//                editor = new RtcLinksEditor(context);
            } else if (type.equals(Type.TEAM_AND_PROJECT)) {
                editor = new RtcProjectAndTeamAreaEditor(context);
            } else if (type.equals(Type.HISTORY)) {
                editor = new RtcHistoryEditor(context);
            }
        }
        return editor;

    }
}
