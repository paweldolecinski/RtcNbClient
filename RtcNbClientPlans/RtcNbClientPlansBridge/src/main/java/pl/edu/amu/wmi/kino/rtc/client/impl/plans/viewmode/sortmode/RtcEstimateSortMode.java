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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.sortmode;

import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.order.RtcIllegalOrderException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDuration;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcEstimateSortMode extends RtcPlanItemSorting {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcEstimateSortMode.class, "sortmode.name.estimate");
    }

    @Override
    public void insertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before) throws RtcIllegalOrderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertBefore(RtcPlanItem item, RtcPlanItem before) throws RtcIllegalOrderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertAfter(RtcPlanItem item, RtcPlanItem after) throws RtcIllegalOrderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertBetween(RtcPlanItem item, RtcPlanItem after, RtcPlanItem before) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertBefore(RtcPlanItem item, RtcPlanItem before) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canInsertAfter(RtcPlanItem item, RtcPlanItem after) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compare(RtcPlanItem item1, RtcPlanItem item2) {
        RtcPlansManager plansManager = item1.getPlan().getPlansManager();
        RtcPlanItemAttribute priorityAttr = plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.estimate");
        RtcDuration v1 = (RtcDuration) item1.getPlanAttributeValue(priorityAttr);
        RtcDuration v2 = (RtcDuration) item2.getPlanAttributeValue(priorityAttr);

        int result = 0;
        if (v1 == null || v2 == null) {
            result = (v1 == null ? 1 : 0) - (v2 == null ? 1 : 0);
        } else {
            result = v1.getDurationInMillis() > v2.getDurationInMillis() ? 1 : 0;
        }

        return result != 0 ? result : item1.getPlanItemIdentifier().compareTo(item2.getPlanItemIdentifier());
    }

}
