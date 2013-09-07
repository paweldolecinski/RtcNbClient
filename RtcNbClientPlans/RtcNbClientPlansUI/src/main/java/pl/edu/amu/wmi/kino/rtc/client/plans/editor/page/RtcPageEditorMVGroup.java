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

import java.util.ArrayList;
import java.util.HashMap;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan.RtcPlanEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPageEditorMVGroup extends KinoMultiViewGroup implements EventListener<RtcPlanEvent> {
//TODO : bikol : maybe it is possible to not use hashCode here

    private RtcPlan plan;
    private HashMap<Integer, RtcPageEditorMVDescription> hash =
            new HashMap<Integer, RtcPageEditorMVDescription>();
    private ArrayList<KinoMultiViewDescription> descs;

    public RtcPageEditorMVGroup(RtcPlan plan) {
        this.plan = plan;
        plan.addListener(this);
    }

    @Override
    public KinoMultiViewDescription[] getDescriptions() {
        if (descs == null) {
            descs = new ArrayList<KinoMultiViewDescription>();
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    for (RtcPlanPage page : plan.getPages()) {
                        descs.add(findDescription(page));
                    }
                    fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_ADDED);
                }
            });

        }
        return descs.toArray(new KinoMultiViewDescription[]{});
    }

    private KinoMultiViewDescription findDescription(RtcPlanPage page) {
        if (hash.get(page.hashCode()) == null) {
            hash.put(page.hashCode(), new RtcPageEditorMVDescription(page, plan));
        }
        return hash.get(page.hashCode());
    }

    @Override
    public void eventFired(RtcPlanEvent event) {
        switch (event) {
            case PAGE_ADDED:
                descs.clear();
                for (RtcPlanPage page : plan.getPages()) {
                    descs.add(findDescription(page));
                }
                fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_ADDED);
                break;
            case PAGE_REMOVED:
                descs.clear();
                for (RtcPlanPage page : plan.getPages()) {
                    descs.add(findDescription(page));
                }
                fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_REMOVED);
                break;
        }
    }
}
