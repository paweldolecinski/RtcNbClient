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

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemHeaderPresenter;

/**
 *
 * @author Patryk Żywica
 */
public class WorkItemHeaderPresenterImpl
        extends BasicPresenter<WorkItemHeaderPresenter.HeaderDisplay>
        implements WorkItemHeaderPresenter, InputHandler<String> {

    private RtcWorkItemWorkingCopy wi;
    private RtcWorkItemLayout layout;

    public WorkItemHeaderPresenterImpl(RtcWorkItemWorkingCopy wi, RtcWorkItemLayout layout, HeaderDisplay display) {
        super(display);
        this.wi = wi;
        this.layout = layout;
    }

    @Override
    protected void onBind() {
        RtcWorkItemAttribute<String> sa = layout.getSummaryAttribute();
        String summ = wi.getValue(sa);
        getDisplay().setSummary(summ);
        RtcWorkItemAttribute<Integer> ia = layout.getIdAttribute();
        int id = wi.getValue(ia);
        RtcWorkItemAttribute<RtcWorkItemType> ta = layout.getTypeAttribute();
        RtcWorkItemType type = wi.getValue(ta);
        getDisplay().setId(type.getDisplayName()+" "+id);
        getDisplay().setIcon(type.getWorkItemIcon());
        getDisplay().addHandler(SUMMARY_INPUT, this);
    }

    @Override
    protected void onUnbind() {
        getDisplay().removeHandler(SUMMARY_INPUT, this);
    }

    public void refreshDisplay() {
        getDisplay().setSummary(wi.getValue(layout.getSummaryAttribute()));
        getDisplay().setId(null);
    }

    public void valueEntered(String value) {
        wi.setValue(layout.getSummaryAttribute(), value);
        //event should be fired by wi via workitem working copy listener - eventbus bridge
    }
}
