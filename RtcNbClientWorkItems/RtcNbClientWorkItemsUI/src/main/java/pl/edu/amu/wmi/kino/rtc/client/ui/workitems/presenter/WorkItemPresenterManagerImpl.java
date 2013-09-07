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

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.NodeDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayoutManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemHeaderPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenterManager;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter;
import org.openide.util.NbBundle;
/**
 *
 * @author Patryk Żywica
 */
public class WorkItemPresenterManagerImpl implements WorkItemPresenterManager {

    private static final RequestProcessor rp = new RequestProcessor(WorkItemPresenterManagerImpl.class);

    public WorkItemPresenterManagerImpl(ActiveProjectArea area) {
    }

    @Override
    public WorkItemPresenter createPresenter(RtcWorkItem wi, RtcWorkItemViewTarget target) {
        RtcWorkItemLayoutManager lm = wi.getManager().getProjectArea().getLookup().lookup(RtcWorkItemLayoutManager.class);
        return createWIPresenter(
                getWorkItemWorkingCopy(wi),
                lm.getLayout(wi, target),
                "Rtc/Modules/WorkItems/Editor/Display/" + target.name().toLowerCase());
    }

    @Override
    public Node createNodePresenter(RtcWorkItem wi, RtcWorkItemViewTarget target) {
        //TODO : bikol :returned node should manage presenter binding and unbinding via somekind of callback
        System.out.println(target.toString());
        RtcWorkItemLayoutManager lm = wi.getManager().getProjectArea().getLookup().lookup(RtcWorkItemLayoutManager.class);
        final WorkItemPresenter presenter = createWIPresenter(
                getWorkItemWorkingCopy(wi),
                lm.getLayout(wi, target),
                "Rtc/Modules/WorkItems/Node/Display/" + target.name().toLowerCase());
        rp.post(new Runnable() {

            public void run() {
                presenter.bind();
            }
        });
        NodeDisplay nd = (NodeDisplay) presenter.getDisplay();
        return nd.asNode();
    }

    private WorkItemPresenterImpl createWIPresenter(RtcWorkItemWorkingCopy wiwc, RtcWorkItemLayout layout, String lookupPath) {
        InstanceContent ic = new InstanceContent();
        ic.add(wiwc);
        AbstractLookup l = new AbstractLookup(ic);
        WorkItemPresenter.WorkItemDisplay disp = findDisplay(WorkItemPresenter.WorkItemDisplay.class, lookupPath, l);
        if (disp == null) {
            throw new NullPointerException(NbBundle.getMessage(WorkItemPresenterManagerImpl.class, "NoDisplay.exp"));
        } else {
            WorkItemPresenterImpl wiPresenter = new WorkItemPresenterImpl(wiwc, layout, disp);
            WorkItemHeaderPresenter hPresenter = createHeaderPresenter(wiwc, layout, lookupPath);
            if (hPresenter != null) {
                wiPresenter.setInSlot(WorkItemPresenter.WorkItemSlot.HEADER, hPresenter);
            }
            for (WorkItemFieldsLayout.WorkItemTab tab : layout.getFieldsLayout()) {
                WorkItemTabPresenter tPres = createTabPresenter(wiwc, tab, lookupPath + "/" + tab.getLayout().name().toLowerCase());
                if (tPres != null) {
                    for (WorkItemFieldsLayout.WorkItemTabSection s : tab) {
                        for (WorkItemLayoutField lf : s) {
                            Presenter<?> attrPres = createFieldPresenter(
                                    wiwc,
                                    lf,
                                    lookupPath);
                            if (attrPres != null) {
                                tPres.addToSlot(mapSlots(s.getSlot()), attrPres);
                            }
                        }
                    }
                    wiPresenter.addTab(tab.getDisplayName(), tPres);
                }
            }
            return wiPresenter;
        }
    }

    private Presenter<?> createFieldPresenter(RtcWorkItemWorkingCopy wiwc, WorkItemLayoutField field, String lookupPath) {
        LayoutFieldPresenterFactory<?> factory = null;
        double result = 0.0;
        for (LayoutFieldPresenterFactory<?> f : Lookups.forPath("Rtc/Modules/WorkItems/Editor/Presenter").lookupAll(LayoutFieldPresenterFactory.class)) {
            double tmp = f.canCreate(field);
            if (tmp > result) {
                result = tmp;
                factory = f;
            }
        }
        if (factory != null) {
            return doCreateFieldPresenter(factory, wiwc, field, lookupPath);
        } else {
            return null;
        }
    }

    /**
     * Method used to capture generic wildcard in factory and display.
     * 
     * @param <T>
     * @param f
     * @param wiwc
     * @param attr
     * @param lookupPath
     * @return 
     */
    private <T extends Display> Presenter<T> doCreateFieldPresenter(LayoutFieldPresenterFactory<T> f, RtcWorkItemWorkingCopy wiwc, WorkItemLayoutField field, String lookupPath) {
        T display = findDisplay(f.getDisplayType(), lookupPath, Lookup.EMPTY);
        if (display != null) {
            return f.createPresenter(display, field, wiwc);
        } else {
            return null;
        }
    }

    private <T extends Display> T findDisplay(Class<T> displayType, String lookupPath, Lookup lookup) {
        T disp = null;
        for (DisplayFactory df : Lookups.forPath(lookupPath).lookupAll(DisplayFactory.class)) {
            disp = df.createDisplay(displayType, lookup);
            if (disp != null) {
                break;
            }
        }
        return disp;
    }

    private WorkItemTabPresenter createTabPresenter(RtcWorkItemWorkingCopy wiwc, WorkItemFieldsLayout.WorkItemTab tab, String lookupPath) {
//        System.out.println("create tab presenter "+tab.getDisplayName()+" path "+lookupPath);
        WorkItemTabPresenter.TabDisplay disp = findDisplay(WorkItemTabPresenter.TabDisplay.class, lookupPath, Lookup.EMPTY);
        if (disp == null) {
            return null;
        } else {
            WorkItemTabPresenterImpl tabPres = new WorkItemTabPresenterImpl(wiwc, tab, disp);
            return tabPres;
        }
    }

    private WorkItemHeaderPresenter createHeaderPresenter(RtcWorkItemWorkingCopy wiwc, RtcWorkItemLayout layout, String lookupPath) {
        WorkItemHeaderPresenter.HeaderDisplay disp = findDisplay(WorkItemHeaderPresenter.HeaderDisplay.class, lookupPath, Lookup.EMPTY);
        if (disp == null) {
            return null;
        } else {
            WorkItemHeaderPresenterImpl header = new WorkItemHeaderPresenterImpl(wiwc, layout, disp);
            return header;
        }
    }

    private RtcWorkItemWorkingCopy getWorkItemWorkingCopy(RtcWorkItem wi) {
        if (wi instanceof RtcWorkItemWorkingCopy) {
            return (RtcWorkItemWorkingCopy) wi;
        } else {
            return wi.getManager().getWorkingCopy(wi);
        }
    }

    private WorkItemTabPresenter.TabSlot mapSlots(WorkItemFieldsLayout.SectionSlot slot) {
        switch (slot) {
            case ATTACHMENTS:
                return WorkItemTabPresenter.TabSlot.ATTACHMENTS;
            case BOTTOM:
                return WorkItemTabPresenter.TabSlot.BOTTOM;
            case DESCRIPTION:
                return WorkItemTabPresenter.TabSlot.DESCRIPTION;
            case DETAILS:
                return WorkItemTabPresenter.TabSlot.DETAILS;
            case DISCUSSION:
                return WorkItemTabPresenter.TabSlot.DISCUSSION;
            case LEFT:
                return WorkItemTabPresenter.TabSlot.LEFT;
            case LINKS:
                return WorkItemTabPresenter.TabSlot.LINKS;
            case NONE_SECTION:
                return WorkItemTabPresenter.TabSlot.NONE_SECTION;
            case QUICKINFO:
                return WorkItemTabPresenter.TabSlot.QUICKINFO;
            case RIGHT:
                return WorkItemTabPresenter.TabSlot.RIGHT;
            case SUBSCRIBERS:
                return WorkItemTabPresenter.TabSlot.SUBSCRIBERS;
            case TOP:
                return WorkItemTabPresenter.TabSlot.TOP;
            default:
                throw new IllegalStateException();
        }
    }
}
