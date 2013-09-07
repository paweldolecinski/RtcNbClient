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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.reports;

import java.awt.Image;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.openide.explorer.ExplorerManager;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.reports.RtcPlanReport;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcChartsMVElement extends KinoMultiViewElement {

    private ExplorerManager manager;
    private final RtcPlan plan;
    private RtcChartsPanel panel;
    private JPanel toolbar;

    public RtcChartsMVElement(RtcPlan plan) {
        this.plan = plan;
        ic.add(plan);
        panel = new RtcChartsPanel(plan);
        manager = new ExplorerManager();
        ic.add(manager);
    }

    @Override
    public JComponent createInnerComponent() {

        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.setReport("", "Loading...", null);
        SwingWorker<Image, Object> worker = new SwingWorker<Image, Object>() {
            private RtcPlanReport defaultReport = null;

            @Override
            protected Image doInBackground() throws Exception {
                defaultReport = plan.getPlansManager().getDefaultReport();
                return defaultReport.getChartFor(plan);
            }

            @Override
            protected void done() {
                try {
                    if (get() != null) {
                        panel.setReport(defaultReport.getName(), "",  ImageUtilities.image2Icon(get()));
                    } else {
                        panel.setReport("", "No work items were found.", null);
                    }
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcChartsMVElement.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcChartsMVElement.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        };
        worker.execute();
        return panel;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        if (toolbar == null) {
//            toolbar = new RtcPageToolbar();
            toolbar=new JPanel();
        }
        return toolbar;
    }
}


