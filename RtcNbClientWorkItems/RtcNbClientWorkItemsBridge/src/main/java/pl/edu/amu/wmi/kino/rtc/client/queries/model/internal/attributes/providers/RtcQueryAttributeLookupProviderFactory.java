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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers;

import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.model.AttributeTypes;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.ApprovalStateAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.ApprovalTypeAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.BooleanAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.CategoryQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.ContributorQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.DeliverableQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.DurationQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.EmptyQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.EnumerationQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.FloatQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.IntegerQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.InternalStateQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.IterationQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.LongQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.ProjectAreaQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.StringQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.TagsQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.TeamAreaQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.TimestampQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.TypeQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup.WorkItemAttributeLookupProvider;

/**
 *
 * @author Patryk Żywica
 */
public class RtcQueryAttributeLookupProviderFactory {

    private ActiveProjectArea area;

    public RtcQueryAttributeLookupProviderFactory(ActiveProjectArea area) {
        this.area = area;
    }

    public RtcQueryAttributeLookupProvider getLookupProvider(IQueryableAttribute attribute) {
        if (attribute.getIdentifier().equals("internalState")) {
            return new InternalStateQueryAttributeLookupProvider(attribute, area);
        }
        if (AttributeTypes.isEnumerationAttributeType(attribute.getAttributeType())) {
            return new EnumerationQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.TYPE)) {
            return new TypeQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.PROJECT_AREA)
                || attribute.getAttributeType().equals(AttributeTypes.PROJECT_AREA_LIST)) {
            return new ProjectAreaQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.TIMESTAMP)) {
            return new TimestampQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.CONTRIBUTOR)
                || attribute.getAttributeType().equals(AttributeTypes.CONTRIBUTOR_LIST)) {
            return new ContributorQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.DURATION)) {
            return new DurationQueryAttributeLookupProvider(attribute, area);
        }
        if (AttributeTypes.STRING_TYPES.contains(attribute.getAttributeType())) {
            return new StringQueryAttributeLookupProvider(attribute, area);
        }
        //TODO : for future : maybe it is necessary to support html in different way
        if (AttributeTypes.HTML_TYPES.contains(attribute.getAttributeType())) {
            return new StringQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.INTEGER)) {
            return new IntegerQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.LONG)
                || attribute.getAttributeType().equals(AttributeTypes.FILE_SIZE)) {
            return new LongQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.FLOAT)) {
            return new FloatQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.SUBSCRIPTIONS)) {
            return new ContributorQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.COMMENTS)) {
            return new ContributorQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.CATEGORY)) {
            return new CategoryQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.TEAM_AREA)
                || attribute.getAttributeType().equals(AttributeTypes.TEAM_AREA_LIST)) {
            return new TeamAreaQueryAttributeLookupProvider(attribute, area);
        }
        //TODO : for future : it is possible to return team areas, but we should implement
        // process area possible values, because not all precess areas are team areas
        if (attribute.getAttributeType().equals(AttributeTypes.PROCESS_AREA)
                || attribute.getAttributeType().equals(AttributeTypes.PROCESS_AREA_LIST)) {
            return new TeamAreaQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals(AttributeTypes.ITERATION)) {
            return new IterationQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.DELIVERABLE))) {
            return new DeliverableQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.TAGS))
                || attribute.getAttributeType().equals((AttributeTypes.TAG))) {
            return new TagsQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.APPROVALS))) {
            return new ContributorQueryAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.APPROVAL_STATE))) {
            return new ApprovalStateAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.APPROVAL_TYPE))) {
            return new ApprovalTypeAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.WORK_ITEM))
                || attribute.getAttributeType().equals((AttributeTypes.WORK_ITEM_LIST))) {
            return new WorkItemAttributeLookupProvider(attribute, area);
        }
        if (attribute.getAttributeType().equals((AttributeTypes.BOOLEAN))) {
            return new BooleanAttributeLookupProvider(attribute, area);
        }
        //this is unsupported in eclipse, because it is nonsense to set this expression
        if (attribute.getAttributeType().equals(AttributeTypes.REFERENCE)) {
            return new EmptyQueryAttributeLookupProvider(attribute, area);
        }
        //this is unsupported in eclipse, because it is nonsense to set this expression
        if (attribute.getAttributeType().equals(AttributeTypes.TYPE)) {
            return new EmptyQueryAttributeLookupProvider(attribute, area);
        }
        //this is unsupported in eclipse, because it is nonsense to set this expression
        if (attribute.getAttributeType().equals(AttributeTypes.CONTENT)) {
            return new EmptyQueryAttributeLookupProvider(attribute, area);
        }

        return new EmptyQueryAttributeLookupProvider(attribute, area);
    }
}
