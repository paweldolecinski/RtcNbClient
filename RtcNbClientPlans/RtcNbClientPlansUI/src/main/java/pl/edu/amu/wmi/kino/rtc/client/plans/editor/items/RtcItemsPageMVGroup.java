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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items;

import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.rtc.client.experimental.RtcExperimentalSupport;

/**
 *
 * @author Patryk Żywica
 */
public class RtcItemsPageMVGroup extends KinoMultiViewGroup {

    private RtcPlan plan;
    private RtcItemsPageMVDescription[] descs;

    public RtcItemsPageMVGroup(RtcPlan plan) {
        this.plan = plan;
    }

    @Override
    public KinoMultiViewDescription[] getDescriptions() {
        if (descs == null) {
            RtcExperimentalSupport ex = Lookup.getDefault().lookup(RtcExperimentalSupport.class);
            if (ex.isEnabled("plansModuleItemsPage")) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        descs = new RtcItemsPageMVDescription[]{new RtcItemsPageMVDescription(plan)};
                        fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_ADDED);
                    }
                });
            }
            descs = new RtcItemsPageMVDescription[]{};
        }
        return descs;
    }
}
