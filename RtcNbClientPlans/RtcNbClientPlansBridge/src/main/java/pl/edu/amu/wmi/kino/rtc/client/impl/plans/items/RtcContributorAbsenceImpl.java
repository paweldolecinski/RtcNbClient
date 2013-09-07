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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items;

import java.util.Date;

import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcContributorAbsence;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.apt.internal.client.OutOfOfficeItem;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcContributorAbsenceImpl extends RtcContributorAbsence {

    private final OutOfOfficeItem absence;
    private final RtcPlan plan;
    private EventSourceSupport<RtcPlanItem.RtcPlanItemEvent> eventSource = new EventSourceSupport<RtcPlanItemEvent>();
	private final ActiveProjectAreaImpl area;

    RtcContributorAbsenceImpl(OutOfOfficeItem absence, RtcPlan plan, ActiveProjectAreaImpl area) {
        this.absence = absence;
        this.plan = plan;
		this.area = area;
    }

    /**
     *
     * @return
     */
    @Override
    public Date getStartDate() {
        return absence.getStart();
    }

    /**
     *
     * @return
     */
    @Override
    public Date getEndDate() {
        return absence.getEnd();
    }

    @Override
    public RtcPlanItemType getPlanItemType() {
        return RtcPlanItemType.ABSENCE;
    }

    @Override
    public RtcPlanItem[] getChildItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends RtcPlanItem> T[] getChildItems(Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return absence.getHTMLSummary().getXMLText();
    }

    @Override
    public Contributor getOwner() {
    	ContributorManagerImpl lookup = (ContributorManagerImpl) area.getLookup().lookup(ContributorManager.class);
        return lookup.findContributor(absence.getOwner());
    }

    @Override
    public String getPlanItemIdentifier() {
        return absence.toString();
    }

    @Override
    public RtcPlanItem getParentItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getPlanAttributeValue(RtcPlanItemAttribute<T> attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> void setPlanAttributeValue(RtcPlanItemAttribute<T> attribute, T value) throws RtcIllegalPlanAttributeValue {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override
    public RtcPlan getPlan() {
        return plan;
    }

    public final void removeListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanItemEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanItemEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
