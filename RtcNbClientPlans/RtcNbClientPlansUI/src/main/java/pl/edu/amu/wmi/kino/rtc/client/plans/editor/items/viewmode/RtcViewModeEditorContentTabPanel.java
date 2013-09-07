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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.viewmode;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode.RtcPlanItemViewModeEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewStyle;

/**
 *
 * @author Patryk Żywica
 */
public class RtcViewModeEditorContentTabPanel extends JPanel implements HelpCtx.Provider {

    private static final long serialVersionUID = 142451555900L;
    private JPanel stylesPanel, groupingsPanel, sortingsPanel, filtersPanel, barsPanel;
    private ButtonGroup stylesButtonGroup, groupingButtonGroup, sortingButtonGroup, barsButtonGroup;
    private JRadioButton flatStyle, taskboardStyle, treeStyle;
    private ActionListener flatStyleListener, taskboardStyleListener, treeStyleListener;
    private HashMap<ButtonModel, RtcPlanItemGrouping> groupings =
            new HashMap<ButtonModel, RtcPlanItemGrouping>();
    private HashMap<ButtonModel, RtcPlanItemSorting> sortings =
            new HashMap<ButtonModel, RtcPlanItemSorting>();
    private HashMap<ButtonModel, RtcPlanItemFilter> filters =
            new HashMap<ButtonModel, RtcPlanItemFilter>();
    private HashMap<RtcPlanItemFilter, JCheckBox> filterChecks = new HashMap<RtcPlanItemFilter, JCheckBox>();
    private JRadioButton noBar, progressBar, loadBar;
    private ActionListener noBarListener, progressListener, loadListener;
    private EventListener<RtcPlanItemViewModeEvent> currentListener;
    private RtcPlanItemViewMode currentViewMode;

    public RtcViewModeEditorContentTabPanel() {
        setLayout(new GridLayout(1, 5, 15, 15));

        //creating style part
        stylesPanel = new JPanel();
        stylesPanel.setLayout(new BoxLayout(stylesPanel, BoxLayout.Y_AXIS));
        stylesPanel.setBackground(Color.WHITE);
        stylesPanel.setOpaque(true);
        stylesPanel.add(new JLabel(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Style.name")));
        stylesButtonGroup = new ButtonGroup();
        flatStyle = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Style.flat.name"));
        stylesButtonGroup.add(flatStyle);
        stylesPanel.add(flatStyle);
        taskboardStyle = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Style.taskboard.name"));
//        stylesButtonGroup.add(taskboardStyle);
//        stylesPanel.add(taskboardStyle);
        treeStyle = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Style.tree.name"));
        stylesButtonGroup.add(treeStyle);
        stylesPanel.add(treeStyle);

        add(stylesPanel);
        //end of creating style part

        //creating grouping part
        groupingsPanel = new JPanel();
        groupingsPanel.setLayout(new BoxLayout(groupingsPanel, BoxLayout.Y_AXIS));
        groupingsPanel.setBackground(Color.WHITE);
        groupingsPanel.setOpaque(true);
        add(groupingsPanel);
        //end of creating grouping part

        //creating sorting part
        sortingsPanel = new JPanel();
        sortingsPanel.setLayout(new BoxLayout(sortingsPanel, BoxLayout.Y_AXIS));
        sortingsPanel.setBackground(Color.WHITE);
        sortingsPanel.setOpaque(true);
        add(sortingsPanel);
        //end of creating sorting part

        //creating filters part
        filtersPanel = new JPanel();
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));
        filtersPanel.setBackground(Color.WHITE);
        filtersPanel.setOpaque(true);
        add(filtersPanel);
        //end of creationg filters part

        barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setBackground(Color.WHITE);
        barsPanel.setOpaque(true);
        barsButtonGroup = new ButtonGroup();
        loadBar = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Bar.load.name"));
        progressBar = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Bar.progress.name"));
        noBar = new JRadioButton(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Bar.none.name"));
        barsButtonGroup.add(noBar);
        barsButtonGroup.add(progressBar);
        barsButtonGroup.add(loadBar);
        add(barsPanel);
    }

    public void update(RtcPlanItemViewMode mode) {
        if (currentViewMode != null) {
            if (currentListener != null) {
                currentViewMode.removeListener(currentListener);
            }
        }
        currentListener = new FilterListener(mode);
        currentViewMode = mode;
        mode.addListener(currentListener);

        switch (mode.getViewStyle()) {
            case TASKBOARD:
                stylesButtonGroup.setSelected(taskboardStyle.getModel(), true);
                if (taskboardStyleListener != null) {
                    taskboardStyle.removeActionListener(taskboardStyleListener);
                }
                taskboardStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.TASKBOARD);
                taskboardStyle.addActionListener(taskboardStyleListener);
                break;
            case TREE:
                stylesButtonGroup.setSelected(treeStyle.getModel(), true);
                if (treeStyleListener != null) {
                    treeStyle.removeActionListener(treeStyleListener);
                }
                treeStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.TREE);
                treeStyle.addActionListener(treeStyleListener);
                break;
            case FLAT:
                stylesButtonGroup.setSelected(flatStyle.getModel(), true);
                if (flatStyleListener != null) {
                    flatStyle.removeActionListener(flatStyleListener);
                }
                flatStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.FLAT);
                flatStyle.addActionListener(flatStyleListener);
                break;
        }
        if (taskboardStyleListener != null) {
            taskboardStyle.removeActionListener(taskboardStyleListener);
        }
        taskboardStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.TASKBOARD);
        taskboardStyle.addActionListener(taskboardStyleListener);
        if (treeStyleListener != null) {
            treeStyle.removeActionListener(treeStyleListener);
        }
        treeStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.TREE);
        treeStyle.addActionListener(treeStyleListener);
        if (flatStyleListener != null) {
            flatStyle.removeActionListener(flatStyleListener);
        }
        flatStyleListener = new StyleChanger(mode, RtcPlanItemViewStyle.FLAT);
        flatStyle.addActionListener(flatStyleListener);
        //end of updating style part

        //updating grouping part
        groupingsPanel.removeAll();
        groupingsPanel.add(new JLabel(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Grouping.name")));
        groupingButtonGroup = new ButtonGroup();
        JRadioButton tmp;
        for (RtcPlanItemGrouping grouping : mode.getManager().getGrouping(mode)) {
            tmp = new JRadioButton(grouping.getDisplayName());
            groupings.put(tmp.getModel(), grouping);
            groupingButtonGroup.add(tmp);
            groupingsPanel.add(tmp);
            tmp.addActionListener(new GroupingChanger(mode, grouping));

            if (grouping.equals(mode.getGrouping())) {
                groupingButtonGroup.setSelected(tmp.getModel(), true);
            }
        }
        groupingsPanel.validate();
        groupingsPanel.repaint();
        //end of updating groupings

        //updating sorting part
        sortingsPanel.removeAll();
        sortingsPanel.add(new JLabel(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Sorting.name")));
        sortingButtonGroup = new ButtonGroup();
        for (RtcPlanItemSorting sorting : mode.getManager().getSortings(mode)) {
            tmp = new JRadioButton(sorting.getDisplayName());
            sortings.put(tmp.getModel(), sorting);
            sortingButtonGroup.add(tmp);
            sortingsPanel.add(tmp);
            tmp.addActionListener(new SortingChanger(mode, sorting));

            if (sorting.equals(mode.getSorting())) {
                sortingButtonGroup.setSelected(tmp.getModel(), true);
            }
        }
        sortingsPanel.validate();
        sortingsPanel.repaint();
        //end of updating sorting part

        //updating filters part
        filtersPanel.removeAll();
        filterChecks.clear();
        filtersPanel.add(new JLabel(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Filters.name")));
        JCheckBox chBox;
        List<RtcPlanItemFilter> selected = Arrays.asList(mode.getFilters());
        for (RtcPlanItemFilter filter : mode.getManager().getFilters(mode)) {
            chBox = new JCheckBox(filter.getDisplayName(), selected.contains(filter));
            filters.put(chBox.getModel(), filter);
            filtersPanel.add(chBox);
            filterChecks.put(filter, chBox);
            chBox.addItemListener(new FilterChanger(mode, filter));
        }
        filtersPanel.validate();
        filtersPanel.repaint();
        //end of updating filters part

        //updating bar part
        barsPanel.removeAll();
        barsPanel.add(new JLabel(NbBundle.getMessage(RtcViewModeEditorContentTabPanel.class, "ViewModeEditor.ContentTab.Bar.name")));
        for (RtcPlanItemViewMode.RtcBarType type : mode.getGrouping().getPossibleBarTypes()) {
            switch (type) {
                case NONE:
                    barsPanel.add(noBar);
                    if (noBarListener != null) {
                        noBar.removeActionListener(noBarListener);
                    }
                    noBarListener = new BarChanger(mode, RtcPlanItemViewMode.RtcBarType.NONE);
                    noBar.addActionListener(noBarListener);
                    if (type.equals(mode.getBarType())) {
                        barsButtonGroup.setSelected(noBar.getModel(), true);
                    }
                    break;
                case PROGRESS_BAR:
                    barsPanel.add(progressBar);
                    if (progressListener != null) {
                        progressBar.removeActionListener(progressListener);
                    }
                    progressListener = new BarChanger(mode, RtcPlanItemViewMode.RtcBarType.PROGRESS_BAR);
                    progressBar.addActionListener(progressListener);
                    if (type.equals(mode.getBarType())) {
                        barsButtonGroup.setSelected(progressBar.getModel(), true);
                    }
                    break;
                case LOAD_BAR:
                    barsPanel.add(loadBar);
                    if (loadListener != null) {
                        loadBar.removeActionListener(loadListener);
                    }
                    loadListener = new BarChanger(mode, RtcPlanItemViewMode.RtcBarType.LOAD_BAR);
                    loadBar.addActionListener(loadListener);
                    if (type.equals(mode.getBarType())) {
                        barsButtonGroup.setSelected(loadBar.getModel(), true);
                    }
                    break;
            }
        }
        barsPanel.validate();
        barsPanel.repaint();
        //end of updating bar part
    }

    private static class SortingChanger implements ActionListener {

        private RtcPlanItemViewMode mode;
        private RtcPlanItemSorting sorting;

        SortingChanger(RtcPlanItemViewMode mode, RtcPlanItemSorting sorting) {
            this.mode = mode;
            this.sorting = sorting;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!mode.getSorting().equals(sorting)) {
                mode.setSorting(sorting);
            }
        }
    }

    private static class GroupingChanger implements ActionListener {

        private RtcPlanItemViewMode mode;
        private RtcPlanItemGrouping grouping;

        GroupingChanger(RtcPlanItemViewMode mode, RtcPlanItemGrouping grouping) {
            this.mode = mode;
            this.grouping = grouping;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!mode.getGrouping().equals(grouping)) {
                mode.setGrouping(grouping);
            }
        }
    }

    private static class StyleChanger implements ActionListener {

        private RtcPlanItemViewMode mode;
        private RtcPlanItemViewStyle style;

        StyleChanger(RtcPlanItemViewMode mode, RtcPlanItemViewStyle style) {
            this.mode = mode;
            this.style = style;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!mode.getViewStyle().equals(style)) {
                mode.setViewStyle(style);
            }
        }
    }

    private static class BarChanger implements ActionListener {

        private RtcPlanItemViewMode mode;
        private RtcPlanItemViewMode.RtcBarType bar;

        BarChanger(RtcPlanItemViewMode mode, RtcPlanItemViewMode.RtcBarType bar) {
            this.mode = mode;
            this.bar = bar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!mode.getBarType().equals(bar)) {
                mode.setBarType(bar);
            }
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
                });
            }
        }
    }

    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
