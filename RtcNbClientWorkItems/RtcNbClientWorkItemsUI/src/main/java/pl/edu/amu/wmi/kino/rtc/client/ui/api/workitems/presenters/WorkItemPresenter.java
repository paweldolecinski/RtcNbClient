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
import pl.edu.amu.wmi.kino.netbeans.mvp.client.HasTabs;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;

/**
 *
 * @author Patryk Żywica
 */
public interface WorkItemPresenter
        extends Presenter<WorkItemPresenter.WorkItemDisplay>,HasSlots<WorkItemPresenter.WorkItemSlot>, HasTabs {

    void closeDisplay();

    HandlerRegistration addCloseHandler(CloseHandler h);


    public static interface CloseHandler {

        /**
         * This method will be called by WorkItemPresenter when from some reason
         * presenter closes itself. It should be used by presenter creator
         * to get information about its state.
         */
        void close();
    }

    public static interface WorkItemDisplay extends Display {
        
        public static final OptionChooseHandler.OptionChooser MODIFIED_OPTION = new OptionChooseHandler.OptionChooser();
        
        public static final int DISCARDED = 0;
        public static final int SAVED = 1;

        void addToSlot(WorkItemSlot slot, Display content);

        void removeFromSlot(WorkItemSlot slot, Display content);

        void clearSlot(WorkItemSlot slot);

        void setInSlot(WorkItemSlot slot, Display content);

        void addTab(String title, Display content);

        void removeTab(Display content);

        void removeTabs();

        void open();

        void closeDisplay();

        void showDialog(String title, String msg, String[] options, OptionChooseHandler handler);
        
        void setTitle(String title);
        
        void setModified(boolean modified);
        
        HandlerRegistration addOptionHandler(OptionChooseHandler.OptionChooser source, OptionChooseHandler h);
    }

    public static enum WorkItemSlot {

        HEADER,
    }
}
