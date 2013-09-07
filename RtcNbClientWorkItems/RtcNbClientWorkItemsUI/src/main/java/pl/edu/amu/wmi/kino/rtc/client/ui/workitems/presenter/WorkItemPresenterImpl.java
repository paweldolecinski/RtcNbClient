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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem.RtcWorkItemEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemHeaderPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events.AttributeChangeEvent;

/**
 *
 * @author Patryk Żywica
 */
public class WorkItemPresenterImpl
        extends BasicPresenter<WorkItemPresenter.WorkItemDisplay>
        implements WorkItemPresenter, AttributeChangeEvent.AttributeChangeHandler, OptionChooseHandler, EventListener<RtcWorkItemEvent> {

    private RtcWorkItemWorkingCopy wiwc;
    private final List<WorkItemPresenter.CloseHandler> closeHandlers =
            Collections.synchronizedList(new LinkedList<WorkItemPresenter.CloseHandler>());
    private RtcWorkItemLayout layout;
    private WorkItemHeaderPresenter header;
    private List<Presenter<?>> tabs = new LinkedList<Presenter<?>>();

    public WorkItemPresenterImpl(RtcWorkItemWorkingCopy wiwc, RtcWorkItemLayout layout, WorkItemPresenter.WorkItemDisplay display) {
        super(display);
        this.wiwc = wiwc;
        this.layout = layout;
    }

    @Override
    protected void onBind() {
        String title = wiwc.getValue(layout.getSummaryAttribute());
        if (title.length() < 43) {
            getDisplay().setTitle(wiwc.getValue(layout.getSummaryAttribute()));
        } else {
            getDisplay().setTitle(wiwc.getValue(layout.getSummaryAttribute()).substring(0, 43) + "..");
        }


        if (header != null) {
            header.bind();
        }
        for (Presenter<?> tab : tabs) {
            tab.bind();
        }

        registerHandler(AttributeChangeEvent.register(this, wiwc));
        getDisplay().addOptionHandler(WorkItemDisplay.MODIFIED_OPTION, this);
        wiwc.addListener(this);
    }

    @Override
    protected void onUnbind() {
        if (header != null) {
            header.unbind();
        }
        for (Presenter<?> tab : tabs) {
            tab.unbind();
        }
    }

    @Override
    public void refreshDisplay() {
    }

    @Override
    public void revealDisplay() {
        getDisplay().open();
        super.revealDisplay();
    }

    @Override
    public void closeDisplay() {
        int id = wiwc.getValue(layout.getIdAttribute());
        getDisplay().showDialog("can close", "can close wi " + id + " ?", new String[]{"yes", "no", "cancel"}, new OptionChooseHandler() {

            @Override
            public void optionChosen(int option) {
                switch (option) {
                    case 0:
                        //TODO save
                        getDisplay().closeDisplay();
                        notifyCloseHandlers();
                        break;
                    case 1:
                        getDisplay().closeDisplay();
                        notifyCloseHandlers();
                        break;
                }
            }
        });
    }

    @Override
    public HandlerRegistration addCloseHandler(final WorkItemPresenter.CloseHandler h) {
        closeHandlers.add(h);
        return new HandlerRegistration(new Runnable() {

            @Override
            public void run() {
                closeHandlers.remove(h);
            }
        });
    }

    private void notifyCloseHandlers() {
        synchronized (closeHandlers) {
            for (WorkItemPresenter.CloseHandler c : closeHandlers) {
                c.close();
            }
        }
    }

    public void addToSlot(WorkItemSlot slot, Presenter<?> content) {
        switch (slot) {
            case HEADER:
                assert header == null;
                assert content instanceof WorkItemHeaderPresenter;
                header = (WorkItemHeaderPresenter) content;
                getDisplay().addToSlot(slot, content.getDisplay());
        }
    }

    public void clearSlot(WorkItemSlot slot) {
        switch (slot) {
            case HEADER:
                getDisplay().removeFromSlot(slot, header.getDisplay());
                header = null;
        }
    }

    public void removeFromSlot(WorkItemSlot slot, Presenter<?> content) {
        switch (slot) {
            case HEADER:
                if (content == header) {
                    getDisplay().removeFromSlot(slot, header.getDisplay());
                    header = null;
                }
        }
    }

    public void setInSlot(WorkItemSlot slot, Presenter<?> content) {
        switch (slot) {
            case HEADER:
                assert content instanceof WorkItemHeaderPresenter;
                header = (WorkItemHeaderPresenter) content;
                getDisplay().addToSlot(slot, content.getDisplay());
        }
    }

    public void addTab(String title, Presenter<?> content) {
        getDisplay().addTab(title, content.getDisplay());
        tabs.add(content);
    }

    public void removeTab(Presenter<?> content) {
        getDisplay().removeTab(content.getDisplay());
        tabs.remove(content);
    }

    public void removeTabs() {
        getDisplay().removeTabs();
        tabs.clear();
    }

    public <H> void valueChanged(RtcWorkItemAttribute<H> attribute, H oldValue, H newValue) {
        getDisplay().setModified(true);
    }

    public void optionChosen(int option) {
        switch (option) {
            case WorkItemDisplay.DISCARDED:
                wiwc.discardChanges();
                getDisplay().setModified(false);
                break;
            case WorkItemDisplay.SAVED:
                wiwc.save();
                getDisplay().setModified(false);
                break;
        }
    }

    public void eventFired(RtcWorkItemEvent event) {
        if(event.equals(RtcWorkItemEvent.WORK_ITEM_SAVED) ||event.equals(RtcWorkItemEvent.WORK_ITEM_CHANGES_DISCARDED)){
            getDisplay().setModified(false);
        }else{
            getDisplay().setModified(true);
        }
    }
}
