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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.HasSlots;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;

/**
 *
 * @author Patryk Żywica
 */
public interface WorkItemTabPresenter extends Presenter<WorkItemTabPresenter.TabDisplay>, HasSlots<WorkItemTabPresenter.TabSlot> {

    
    
    public static interface TabDisplay extends Display {

        void addToSlot(TabSlot slot, Display content);

        void removeFromSlot(TabSlot slot, Display content);

        void clearSlot(TabSlot slot);

        void setInSlot(TabSlot slot, Display content);
    }

    public enum TabSlot {

        NONE_SECTION,
        DESCRIPTION,
        DETAILS,
        DISCUSSION,
        QUICKINFO,
        ATTACHMENTS,
        SUBSCRIBERS,
        LINKS,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,}
}
