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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingWorker;

import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcPlanItemViewModeEvent;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.RtcPlanEditorManager;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.viewmode.RtcViewModesSelectionPanel;

/**
 *
 * @author Patryk Żywica
 */
public class RtcItemsPageRightPanel extends JPanel implements LookupListener {

    private static final long serialVersionUID = 124113224L;
    private InstanceContent ic;
    private RtcPlanItemViewMode currentlySelectedViewMode;
    private HashMap<RtcPlanItemViewMode, JRadioButton> viewModes =
            new HashMap<RtcPlanItemViewMode, JRadioButton>();
    private JPanel filtersPanel;
    private Result<RtcPlanItemViewMode> result;
    private HashMap<RtcPlanItemFilter, JCheckBox> filterChecks = new HashMap<RtcPlanItemFilter, JCheckBox>();
    private EventListener<RtcPlanItemViewModeEvent> currentListener;
    private RtcPlanItemViewMode currentViewMode;
    private Lookup lookup;

    public RtcItemsPageRightPanel(final RtcPlan plan, InstanceContent ic) {
        this.ic = ic;
        this.ic.add(plan);



        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.decode("#FDFDFD"));
        setBorder(javax.swing.BorderFactory.createLineBorder(Color.decode("#969FA9")));
        JLabel tmp;
        //create view modes panel
        add(new RtcViewModesSelectionPanel(plan, ic));
        add(new JSeparator());

        //create actions panel
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(Color.decode("#FDFDFD"));
        actionsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 6, 10, 0));
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        tmp = new JLabel(NbBundle.getMessage(RtcItemsPageRightPanel.class, "ActionsPart.name"));
        tmp.setFont(new Font("Tahoma", Font.BOLD, 11));
        tmp.setForeground(Color.decode("#194C7F"));
        actionsPanel.add(tmp);
        actionsPanel.add(new JLabel("Not implemented yet."));
//        add(actionsPanel);
//        add(new JSeparator());

        //create filters panel
        filtersPanel = new JPanel();
        filtersPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 6, 10, 0));
        filtersPanel.setBackground(Color.decode("#FDFDFD"));
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));
//        refreshFiltersPart(currentlySelectedViewMode);
        add(filtersPanel);
        add(new JSeparator());

        //create related panel
        final JPanel relatedPanel = new JPanel();
        relatedPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 6, 10, 0));
        relatedPanel.setBackground(Color.decode("#FDFDFD"));
        relatedPanel.setLayout(new BoxLayout(relatedPanel, BoxLayout.Y_AXIS));
        tmp = new JLabel(NbBundle.getMessage(RtcItemsPageRightPanel.class, "RelatedPart.name"));
        tmp.setFont(new Font("Tahoma", Font.BOLD, 11));
        tmp.setForeground(Color.decode("#194C7F"));
        relatedPanel.add(tmp);


        SwingWorker sw = new SwingWorker<RtcPlan[], Void>() {

            @Override
            protected RtcPlan[] doInBackground() throws Exception {
                return plan.getPlansManager().getRelatedPlans(plan);
            }

            @Override
            protected void done() {

                RtcPlan[] plans = null;
                try {
                    plans = get();
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcItemsPageRightPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcItemsPageRightPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }

                JButton relatedButton;

                for (RtcPlan rtcPlan : plans) {
                    relatedButton = new JButton();
                    relatedButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                    relatedButton.setContentAreaFilled(false);
                    relatedButton.setForeground(Color.decode("#000080"));
                    relatedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    relatedButton.setText("<html>" + rtcPlan.getName() + "[" + rtcPlan.getIteration().getName() + "]" + "</html>");
                    relatedButton.addActionListener(new RtcItemsPageRelatedPlansListener(rtcPlan));
                    relatedPanel.add(relatedButton);
                }
            }
        };
        sw.execute();

        add(relatedPanel);
    }

    void setLookup(Lookup lookup) {
        this.lookup = lookup;
        resultChanged(null);
        result = lookup.lookupResult(RtcPlanItemViewMode.class);
        result.addLookupListener(this);
        result.allInstances();

    }

    private void refreshContent(RtcPlanItemViewMode mode) {
        RequestProcessor.getDefault().post(new RefreshRunnable(mode));
    }

    private void refreshFiltersPart(RtcPlanItemViewMode mode) {
        EventQueue.invokeLater(new RtcFiltersPartRefresh(mode, mode.getManager().getFilters(mode)));
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        RtcPlanItemViewMode viewMode = lookup.lookup(RtcPlanItemViewMode.class);
        if (viewMode != null) {
            refreshContent(viewMode);
        }
    }

    private class RtcFiltersPartRefresh implements Runnable {

        private RtcPlanItemFilter[] filters;
        private RtcPlanItemViewMode mode;

        public RtcFiltersPartRefresh(RtcPlanItemViewMode mode, RtcPlanItemFilter[] filters) {
            this.filters = filters;
            this.mode = mode;
        }

        @Override
        public void run() {
            if (currentViewMode != null) {
                if (currentListener != null) {
                    currentViewMode.removeListener(currentListener);
                }
            }
            currentListener = new FilterListener(mode);
            currentViewMode = mode;
            mode.addListener(currentListener);
            filtersPanel.removeAll();
            filterChecks.clear();
            JCheckBox chBox;
            JLabel tmp = new JLabel(NbBundle.getMessage(RtcItemsPageRightPanel.class, "FiltersPart.name"));
            tmp.setFont(new Font("Tahoma", Font.BOLD, 11));
            tmp.setForeground(Color.decode("#194C7F"));
            filtersPanel.add(tmp);
            List<RtcPlanItemFilter> selected = Arrays.asList(mode.getFilters());
            for (RtcPlanItemFilter filter : filters) {
                chBox = new JCheckBox(filter.getDisplayName(), selected.contains(filter));
                filtersPanel.add(chBox);
                filterChecks.put(filter, chBox);
                chBox.addItemListener(new FilterChanger(mode, filter));
            }

//            filtersPanel.validate();
//            filtersPanel.repaint();
            validate();
        }
    }

    private class RtcItemsPageRelatedPlansListener implements ActionListener {

        private RtcPlan plan;

        RtcItemsPageRelatedPlansListener(RtcPlan plan) {
            this.plan = plan;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TopComponent findEditor = RtcPlanEditorManager.findEditor(plan);
            findEditor.open();
            findEditor.requestActive();
        }
    }

    private static class FilterChanger implements ItemListener {

        private RtcPlanItemViewMode mode;
        private RtcPlanItemFilter filter;

        FilterChanger(RtcPlanItemViewMode mode, RtcPlanItemFilter filter) {
            this.mode = mode;
            this.filter = filter;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            List<RtcPlanItemFilter> selected = Arrays.asList(mode.getFilters());
            if (e.getStateChange() == ItemEvent.SELECTED && !selected.contains(filter)) {
                mode.addFilter(filter);
            } else {
                if (e.getStateChange() == ItemEvent.DESELECTED && selected.contains(filter)) {
                    mode.removeFilter(filter);
                }
            }
        }
    }

    private class RefreshRunnable implements Runnable {

        private RtcPlanItemViewMode mode;

        public RefreshRunnable(RtcPlanItemViewMode mode) {
            this.mode = mode;
        }

        @Override
        public void run() {
            refreshFiltersPart(mode);
        }
    }

    private class FilterListener implements EventListener<RtcPlanItemViewMode.RtcPlanItemViewModeEvent> {

        private RtcPlanItemViewMode viewMode;

        public FilterListener(RtcPlanItemViewMode viewMode) {
            this.viewMode = viewMode;
        }

        @Override
        public void eventFired(RtcPlanItemViewModeEvent event) {
            if (event.equals(RtcPlanItemViewModeEvent.FILTER_ADDED) || event.equals(RtcPlanItemViewModeEvent.FILTER_REMOVED)) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        List<RtcPlanItemFilter> selected = Arrays.asList(viewMode.getFilters());
                        for (RtcPlanItemFilter f : viewMode.getManager().getFilters(viewMode)) {
                            if (filterChecks.containsKey(f)) {
                                if (selected.contains(f)) {
                                    if (!filterChecks.get(f).isSelected()) {
                                        filterChecks.get(f).setSelected(true);
                                    }
                                } else {
                                    if (filterChecks.get(f).isSelected()) {
                                        filterChecks.get(f).setSelected(false);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
