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
import java.util.List;
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

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;

public class TagsQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public TagsQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public Lookup createLookup() {
        IWorkItemClient wiClient = (IWorkItemClient) area.getITeamRepository().getClientLibrary(IWorkItemClient.class);
        return Lookups.singleton(new TagsPossibleValues(area, wiClient));
    }
}

class TagsPossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private IWorkItemClient wiClient;
    private ActiveProjectAreaImpl area;
    private Image  icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public TagsPossibleValues(ActiveProjectArea area, IWorkItemClient wiClient) {
        this.wiClient = wiClient;
        this.area = (ActiveProjectAreaImpl) area;
    }

    @Override
    public RtcQueryAttributeValue[] getValues() {
        assert (!EventQueue.isDispatchThread());
        List<String> tags;
        try {
            tags = wiClient.findTags(area.getProjectArea().getIProcessArea(), null);

            ArrayList<TagValueImpl> valimpl = new ArrayList<TagValueImpl>();
            for (String tag : tags) {
                valimpl.add(new TagValueImpl(tag));
            }
            return valimpl.toArray(new RtcQueryAttributeValueImpl[]{});
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(TagsPossibleValues.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            return new RtcQueryAttributeValue[]{};
        }
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof TagValueImpl) {
            return ((TagValueImpl) value).getTag();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof String) {
            return new TagValueImpl((String) obj);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return true;
    }
}

class TagValueImpl extends RtcQueryAttributeValueImpl {

    private String wi;

    public TagValueImpl(String wi) {
        super(wi);
        this.wi = wi;
    }

    public String getTag() {
        return wi;
    }
}
