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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.ValueCreator;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeValueImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcContributorPossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.impl.RtcContributorPrefferedValues;

import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;

public class ContributorQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectAreaImpl area;

    public ContributorQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.area = (ActiveProjectAreaImpl) area;
        this.attribute = attribute;
    }

    @Override
    public Lookup createLookup() {
        InstanceContent ic = new InstanceContent();
        Lookup lookup = new AbstractLookup(ic);
        List<TeamArea> areas = new LinkedList<TeamArea>();
        ProcessManager pm = area.getLookup().lookup(ProcessManager.class);;
        ContributorManager cm = area.getLookup().lookup(ContributorManager.class);
    	Contributor loggedInContributor = cm.getLoggedInContributor();
    	
        TeamArea[] teamAreas = pm.getTeamAreas();
        for (TeamArea ta : teamAreas) {
            //}
        	Contributor[] members = pm.getMembers(ta);
			int binarySearch = Arrays.binarySearch(members, loggedInContributor);
            if (binarySearch >= 0) {
                areas.add(ta);
            }
        }
        ic.add(new ContributorPrefferedValues(areas, area));
        ic.add(new ContributorPossibleValues(area));
        return lookup;
    }
}

class ContributorPossibleValues implements RtcQueryAttributePossibleValues, ValueCreator {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public ContributorPossibleValues(ActiveProjectAreaImpl area) {
        this.area = area;
    }

    @Override
    public ContributorValueImpl[] getValues() {
        assert (!EventQueue.isDispatchThread());
        RtcContributorPossibleValues poss = new RtcContributorPossibleValues(area);
        ArrayList<ContributorValueImpl> valimpl =
                new ArrayList<ContributorValueImpl>();
        for (Contributor w : poss.getPossibleValues()) {
            if (w instanceof ContributorImpl) {
                ContributorImpl wi = (ContributorImpl) w;
                valimpl.add(new ContributorValueImpl(wi));
            }
        }
        return valimpl.toArray(new ContributorValueImpl[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof ContributorValueImpl) {
            return ((ContributorValueImpl) value).getRtcContributor().getName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public RtcQueryAttributeValue getValueForObject(Object obj) throws IllegalArgumentException {
        if (obj instanceof IContributorHandle) {
            IContributorHandle handle = (IContributorHandle) obj;
            for (ContributorValueImpl val : getValues()) {
                if (val.getRtcContributor().getIContributor().getItemId().getUuidValue().equals(handle.getItemId().getUuidValue())) {
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

class ContributorPrefferedValues implements RtcQueryAttributePrefferedValues {

    private RtcQueryAttributeValue[] children = new RtcQueryAttributeValue[]{};
    private ActiveProjectAreaImpl area;
    private Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private List<TeamArea> areas;

    public ContributorPrefferedValues(List<TeamArea> areas, ActiveProjectAreaImpl area) {
        this.area = area;
        this.areas = areas;
    }

    @Override
    public RtcQueryAttributeValue[] getValues() {

        ArrayList<RtcQueryAttributeValueImpl> valimpl =
                new ArrayList<RtcQueryAttributeValueImpl>();
        RtcContributorPrefferedValues poss = new RtcContributorPrefferedValues(areas, area);

        for (Contributor w : poss.getPrefferedValues()) {
            if (w instanceof ContributorImpl) {
                ContributorImpl wi = (ContributorImpl) w;
                valimpl.add(new ContributorValueImpl(wi));
            }
        }
        return valimpl.toArray(new RtcQueryAttributeValue[]{});
    }

    @Override
    public Image getIconFor(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return icon;
    }

    @Override
    public String getDisplayName(RtcQueryAttributeValue value) throws IllegalArgumentException {
        if (value instanceof ContributorValueImpl) {
            return ((ContributorValueImpl) value).getRtcContributor().getName();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public RtcQueryAttributeValue[] getChildValues(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return children;
    }

    @Override
    public boolean isValueSelectable(RtcQueryAttributeValue value) throws IllegalArgumentException {
        return true;
    }
}

class ContributorValueImpl extends RtcQueryAttributeValueImpl {

    private ContributorImpl wi;

    public ContributorValueImpl(ContributorImpl wi) {
        super(wi.getIContributor());
        this.wi = wi;
    }

    public ContributorImpl getRtcContributor() {
        return wi;
    }
}
