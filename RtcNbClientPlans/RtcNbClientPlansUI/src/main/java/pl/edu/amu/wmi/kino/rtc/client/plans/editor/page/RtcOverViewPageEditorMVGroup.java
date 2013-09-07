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

import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;

/**
 *
 * @author Patryk Żywica
 */
public class RtcOverViewPageEditorMVGroup extends KinoMultiViewGroup {

    private RtcPlan plan;
    private KinoMultiViewDescription[] descs;

    public RtcOverViewPageEditorMVGroup(RtcPlan plan) {
        this.plan = plan;
    }

    @Override
    public KinoMultiViewDescription[] getDescriptions() {
        if (descs == null) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    descs = new KinoMultiViewDescription[]{new RtcPageEditorMVDescription(plan.getOverviewPage(), plan)};
                    fireEvent(KinoMultiViewGroupEvent.DESCRIPTION_ADDED);
                }
            });
            descs=new KinoMultiViewDescription[0];
                    
        }
        return descs;
    }
}
