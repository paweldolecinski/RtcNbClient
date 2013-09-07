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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.page;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage.RtcPlanPageEvent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;

/**
 *
 * @author Patryk Żywica
 */
class RtcPageEditorMVDescription extends KinoMultiViewDescription implements EventListener<RtcPlanPageEvent> {

    private RtcPlanPage page;
    private RtcPlan plan;

    public RtcPageEditorMVDescription(RtcPlanPage page, RtcPlan plan) {
        this.plan = plan;
        this.page = page;
        page.addListener(this);
    }

    @Override
    public KinoMultiViewElement createElement() {
        ////System.out.println("tworzenie ELEMENTU:"+ (plan!=null)+"  "+(page!=null));
        return new RtcPageEditorMVElement(page, plan);
    }

    @Override
    public String getDisplayName() {
        return page.getName();
    }

    @Override
    public void eventFired(RtcPlanPageEvent event) {
        fireEvent(KinoMultiViewDescriptionEvent.NAME_CHANGED);
    }
}
