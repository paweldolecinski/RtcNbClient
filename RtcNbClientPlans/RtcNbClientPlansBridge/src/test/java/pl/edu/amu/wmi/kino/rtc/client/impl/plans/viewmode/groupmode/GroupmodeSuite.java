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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemType;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;

/**
 *
 * @author dolek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcNoneGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcFolderGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcOwnerGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcTagsGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcIterationGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcPriorityGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcPlannedTimeGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcCategoryGroupModeTest.class, pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode.groupmode.RtcTeamGroupModeTest.class})
public class GroupmodeSuite {

    public static RtcPlanItem[] planItems = new RtcPlanItem[]{
        new RtcPlanItemMockup(new RtcContributorMockup("Pablo"), false, false),
        new RtcPlanItemMockup(new RtcContributorMockup("Pablo"), false, true),
        new RtcPlanItemMockup(new RtcContributorMockup("Pablo"), true, true),
        new RtcPlanItemMockup(new RtcContributorMockup("Donald"), true, false),
        new RtcPlanItemMockup(new RtcContributorMockup("Garfield"), true, true),
        new RtcPlanItemMockup(new RtcContributorMockup("Garfield"), true, false),
        new RtcPlanItemMockup(new RtcContributorMockup("Kulfon"), false, true),
        new RtcPlanItemMockup(new RtcContributorMockup("Kulfon"), true, false),};


    private static class RtcPlanItemMockup extends RtcPlanWorkItem {

        private final Contributor owner;
        private final boolean exec;

        public RtcPlanItemMockup(Contributor owner, boolean exec, boolean dirty) {
            this.owner = owner;
            this.exec = exec;
            this.dirty = dirty;
        }

        @Override
        public RtcPlanItemType getPlanItemType() {
            if (exec == true) {
                return RtcPlanItemType.EXECUTABLE;
            }
            return RtcPlanItemType.NON_EXECUTABLE;

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
        public RtcPlanItem getParentItem() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Contributor getOwner() {
            return owner;
        }

        @Override
        public String getPlanItemIdentifier() {
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RtcPlan getPlan() {
            return null;
        }

        @Override
        public RtcWorkItem getWorkItem() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isResolved() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addListener(EventListener<RtcPlanItemEvent> listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeListener(EventListener<RtcPlanItemEvent> listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class RtcContributorMockup implements Contributor {

        private final String name;

        private RtcContributorMockup(String name) {
            this.name = name;
        }

        @Override
        public String getUserId() {
            return name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getEmailAddress() {
            return name;
        }

        @Override
        public boolean isArchived() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RtcContributorMockup other = (RtcContributorMockup) obj;
            if ((this.name == null) ? (other.getName() != null) : !this.name.equals(other.getName())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }

    }
}
