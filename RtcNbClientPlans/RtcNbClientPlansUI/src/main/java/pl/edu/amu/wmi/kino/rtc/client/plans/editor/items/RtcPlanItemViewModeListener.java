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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items;

import java.util.Collection;

import org.openide.explorer.ExplorerManager;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcPlanItemViewModeEvent;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.filters.RtcPlanItemFilteringFilterNode;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.filters.RtcPlanItemGroupingFilterNode;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanItemViewModeListener implements 
        LookupListener, EventListener<RtcPlanItemViewMode.RtcPlanItemViewModeEvent>,
        Runnable {

    private ExplorerManager manager;
    private Node rootNode;
    private RtcPlanItemViewMode currentMode;

    public RtcPlanItemViewModeListener(Node node, ExplorerManager manager) {
        this.manager = manager;
        this.rootNode = node;

    }

    void updateNode(Node newRoot){
        rootNode=newRoot;
        updateManagerRootContext();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        ////System.out.println(ev.getSource().getClass().getSimpleName());
        Result<RtcPlanItemViewMode> result = (Result) ev.getSource();

        Collection<? extends RtcPlanItemViewMode> modes = result.allInstances();
        if (!modes.isEmpty()) {
            RtcPlanItemViewMode mode = modes.iterator().next();
            if (currentMode != mode) {
                if (currentMode != null) {
                    currentMode.removeListener(this);
                }
                currentMode = mode;
                mode.addListener(this);

                //System.out.println("wywolyje set root context na " + manager.hashCode());
                updateManagerRootContext();
            } else {
                //System.out.println("niebylo zmiany");
            }
        } else {
            //System.out.println("niema view moda w lookupie");
            //do nothing
        }
    }

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     */
    static Node createRootNode(Node rootNode,RtcPlanItemViewMode mode) {
        // maybe we have to use copy here
        Node resultNode;

        resultNode = new FilterNode(rootNode);
        //tu sa aplikowane pokolei wszyskie filtery, groupingi i sortingi

        for (RtcPlanItemFilter f : mode.getFilters()) {
            resultNode = new RtcPlanItemFilteringFilterNode(resultNode, f);
        }
        resultNode = new RtcPlanItemGroupingFilterNode(resultNode, mode);
        //resultNode = new RtcPlanItemSortingFilterNode(resultNode, mode.getSorting());

        return resultNode;
    }

    @Override
    public void eventFired(RtcPlanItemViewModeEvent event) {
        if (event.equals(RtcPlanItemViewModeEvent.BARTYPE_CHANGED)
                || event.equals(RtcPlanItemViewModeEvent.CHANGES_DISCARDED)
                || event.equals(RtcPlanItemViewModeEvent.FILTER_ADDED)
                || event.equals(RtcPlanItemViewModeEvent.FILTER_REMOVED)
                || event.equals(RtcPlanItemViewModeEvent.GROUPING_CHANGED)
                || event.equals(RtcPlanItemViewModeEvent.SORTING_CHANGED)
                || event.equals(RtcPlanItemViewModeEvent.STYLE_CHANGED)) {
            //System.out.println("jest zmiana w view modzie");
            updateManagerRootContext();
        }
    }
    
    private void updateManagerRootContext() {
        RequestProcessor.getDefault().post(this);
    }

    @Override
    public void run() {
        manager.setRootContext(createRootNode(rootNode,currentMode));
    }
}
