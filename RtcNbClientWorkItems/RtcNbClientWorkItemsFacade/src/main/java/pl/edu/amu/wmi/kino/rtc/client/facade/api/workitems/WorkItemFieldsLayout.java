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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems;

import java.util.ArrayList;

/**
 * @author psychollek
 * @author Pawel Dolecinski
 * @author Patryk Żywica
 */
public final class WorkItemFieldsLayout extends ArrayList<WorkItemFieldsLayout.WorkItemTab> {

    private static final long serialVersionUID = -4824042760898598856L;

    public final class WorkItemTab extends ArrayList<WorkItemTabSection> {

        private static final long serialVersionUID = -2936963911326833747L;
        private String displayName;
        private TabLayout layout;

        public String getDisplayName() {
            return displayName;
        }

        public TabLayout getLayout() {
            return layout;
        }

        /**
         * @param displayName
         * @param layout
         *            not null
         */
        public WorkItemTab(String displayName, TabLayout layout) {
            this.displayName = displayName;
            this.layout = layout;
        }
    }

    public final class WorkItemTabSection extends ArrayList<WorkItemLayoutField> {

        private static final long serialVersionUID = 3696093424152411304L;
        private String displayName;
        private SectionSlot slot;

        public String getDisplayName() {
            return displayName;
        }

        public SectionSlot getSlot() {
            return slot;
        }

        public WorkItemTabSection(String displayName, SectionSlot slot) {
            this.displayName = displayName;
            this.slot = slot;
        }
    }

    public static enum SectionSlot {

        NONE_SECTION, DESCRIPTION, DETAILS, DISCUSSION, QUICKINFO, ATTACHMENTS, SUBSCRIBERS, LINKS, LEFT, RIGHT, TOP, BOTTOM
    }

    public static enum TabLayout {

        HEADER, OVERVIEW, LINKS, APPROVALS, HISTORY, H_TAB, CUSTOM
    }
}
