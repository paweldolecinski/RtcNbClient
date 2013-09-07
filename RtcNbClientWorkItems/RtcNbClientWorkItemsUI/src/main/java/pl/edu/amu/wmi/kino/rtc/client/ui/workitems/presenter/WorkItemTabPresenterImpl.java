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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.presenter;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter;

/**
 *
 * @author Patryk Żywica
 */
public class WorkItemTabPresenterImpl extends BasicPresenter<WorkItemTabPresenter.TabDisplay> implements WorkItemTabPresenter {

    private Map<TabSlot, LinkedList<Presenter<?>>> presenters = new EnumMap<TabSlot, LinkedList<Presenter<?>>>(TabSlot.class);

    public WorkItemTabPresenterImpl(RtcWorkItemWorkingCopy wiwc, WorkItemFieldsLayout.WorkItemTab tab, TabDisplay display) {
        super(display);
    }

    @Override
    protected void onBind() {
        for (LinkedList<Presenter<?>> list : presenters.values()) {
            for (Presenter<?> p : list) {
                p.bind();
            }
        }
    }

    @Override
    protected void onUnbind() {
        for (LinkedList<Presenter<?>> list : presenters.values()) {
            for (Presenter<?> p : list) {
                p.unbind();
            }
        }
    }

    public void refreshDisplay() {
    }

    public void addToSlot(TabSlot slot, Presenter<?> content) {
        if (!presenters.containsKey(slot)) {
            presenters.put(slot, new LinkedList<Presenter<?>>());
        }
        presenters.get(slot).add(content);
        getDisplay().addToSlot(slot, content.getDisplay());
    }

    public void clearSlot(TabSlot slot) {
        presenters.remove(slot);
        getDisplay().clearSlot(slot);
    }

    public void removeFromSlot(TabSlot slot, Presenter<?> content) {
        if (presenters.containsKey(slot)) {
            presenters.get(slot).remove(content);
        }
        getDisplay().removeFromSlot(slot, content.getDisplay());
    }

    public void setInSlot(TabSlot slot, Presenter<?> content) {
        presenters.remove(slot);
        presenters.put(slot, new LinkedList<Presenter<?>>());
        presenters.get(slot).add(content);
        getDisplay().setInSlot(slot, content.getDisplay());
    }
}
