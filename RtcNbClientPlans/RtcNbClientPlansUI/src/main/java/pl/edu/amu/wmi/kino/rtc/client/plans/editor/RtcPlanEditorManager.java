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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor;

import java.util.Collection;
import java.util.HashMap;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewCloseHandler;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewTopComponentFactory;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.header.RtcPlanHeader;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.multiview.RtcMultiViewGroupFactory;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanEditorManager {

    private static HashMap<String, TopComponent> cmps = new HashMap<String, TopComponent>();

    public static TopComponent findEditor(RtcPlan plan) {
        if (cmps.get(plan.getPlanIdentifier()) == null) {
            Lookup factoryLookup = Lookups.forPath("Rtc/Modules/PlansModule/PlanMultiViewDescriptions");
            Collection<? extends RtcMultiViewGroupFactory> tmpFact =
                    factoryLookup.lookupAll(RtcMultiViewGroupFactory.class);
            KinoMultiViewGroup[] groups = new KinoMultiViewGroup[tmpFact.size()];
            int j = 0;
            for (RtcMultiViewGroupFactory f : tmpFact) {
                groups[j] = f.getGroup(plan);
                j++;
            }
            TopComponent tc = KinoMultiViewTopComponentFactory.createTopComponent(
                    groups,
                    new RtcPlanHeader(plan),
                    new RtcMultiViewCloseHandlerImpl(plan),
                    KinoMultiViewTopComponentFactory.KinoMultiViewTopComponentTabPosition.BOTTOM, "usingRationalTeamConcert.FindingWorkItems"); //TODO: monia: helpCtx
            cmps.put(plan.getPlanIdentifier(), tc);
            tc.setDisplayName(plan.getName() + " [" + plan.getIteration().getName() + "]");
        }
        return cmps.get(plan.getPlanIdentifier());
    }

    private RtcPlanEditorManager() {
    }

    private static class RtcMultiViewCloseHandlerImpl implements KinoMultiViewCloseHandler {

        private RtcPlan plan;

        public RtcMultiViewCloseHandlerImpl(RtcPlan plan) {
            this.plan = plan;
        }

        @Override
        public boolean canClose() {
            //TODO : bikol : check for plan modification state
            return true;
        }

        @Override
        public void close() {
            cmps.remove(plan.getPlanIdentifier());
        }
        
    }
    
}
