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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.progress.RtcProgressInfo;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcBarType;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcFolderGroupMode extends RtcPlanItemGrouping {

    private static final FolderElement TOPITEMS_FOLDER = new FolderElement("_4zeBgNZHEdyw0aNSmwtbxQ", "Top Items"); //$NON-NLS-1$
    private static final FolderElement DEFECTS_FOLDER = new FolderElement("_82-6QNZHEdyw0aNSmwtbxQ", "Defects & Enhancements"); //$NON-NLS-1$

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(RtcFolderGroupMode.class, "groupmode.name.folder");
    }

    @Override
    public RtcPlanItemGroup[] groupItems(RtcPlanItem[] planItems, RtcPlan plan) {

        List<RtcPlanItemGroup<FolderElement>> res = new ArrayList<RtcPlanItemGroup<FolderElement>>();

        HashMap<FolderElement, ArrayList<RtcPlanItem>> map = new HashMap<FolderElement, ArrayList<RtcPlanItem>>();
        map.put(TOPITEMS_FOLDER, new ArrayList<RtcPlanItem>());
        map.put(DEFECTS_FOLDER, new ArrayList<RtcPlanItem>());

        for (RtcPlanItem item : planItems) {
            if (item.getPlanItemType() == RtcPlanItemType.NON_EXECUTABLE) {
                map.get(TOPITEMS_FOLDER).add(item);
            } else {
                map.get(DEFECTS_FOLDER).add(item);
            }

        }

        Set<Entry<FolderElement, ArrayList<RtcPlanItem>>> entrySet = map.entrySet();
        for (Entry<FolderElement, ArrayList<RtcPlanItem>> entry : entrySet) {
            res.add(new RtcPlanItemFolderGroup(entry.getKey(), entry.getValue().toArray(new RtcPlanItem[]{})));
        }

        return res.toArray(new RtcPlanItemGroup[]{});
    }

    @Override
    public RtcBarType[] getPossibleBarTypes() {
        return new RtcBarType[]{RtcBarType.PROGRESS_BAR};
    }

    private static class RtcPlanItemFolderGroup implements RtcPlanItemGroup<FolderElement> {

        private final FolderElement folder;
        private final RtcPlanItem[] planItems;
        private Lookup lookup;
        private InstanceContent ic = new InstanceContent();

        public RtcPlanItemFolderGroup(FolderElement folder, RtcPlanItem[] planItems) {
            this.folder = folder;
            this.planItems = planItems;
            if (planItems.length > 0 && planItems[0].getPlan() != null) {
                RtcPlansManager plansManager = planItems[0].getPlan().getPlansManager();
                RtcProgressInfo progressInfo = plansManager.getProgressInfo(planItems, planItems[0].getPlan().getComplexityComputator());
                ic.add(progressInfo);
                ic.add(planItems[0].getPlan().getComplexityComputator());
            }

            ic.add(folder);
            lookup = new AbstractLookup(ic);
        }

        @Override
        public String getGroupLabel() {
            return folder.getLabel();
        }

        @Override
        public Lookup getLookup() {
            return lookup;
        }

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        @Override
        public RtcPlanItemGroup[] getChildGroups() {
            return new RtcPlanItemGroup[]{};
        }

        /**
         * This can be long running operation. Do not call on event dispatch thread.
         * @return
         */
        @Override
        public RtcPlanItem[] getPlanItems() {
            return planItems;
        }
    }

    public static class FolderElement {

        private final String id;
        private String label;

        private FolderElement(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
