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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.RtcItemsPageRightPanel;

/**
 *
 * @author Patryk Żywica
 */
public class RtcViewModesSelectionPanel extends JPanel {

	private static final long serialVersionUID = -4797954290231064405L;
	private HashMap<RtcPlanItemViewMode, JRadioButton> viewModes =
            new HashMap<RtcPlanItemViewMode, JRadioButton>();
    private RtcPlanItemViewMode currnetlySelectedViewMode;
    private InstanceContent ic;

    public RtcViewModesSelectionPanel(final RtcPlan plan, InstanceContent ic) {
        this.ic = ic;
        JLabel tmp;
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 6, 10, 0));
        setBackground(Color.decode("#FDFDFD"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //System.out.println("wypisuje selection");
        tmp = new JLabel(NbBundle.getMessage(RtcItemsPageRightPanel.class, "ViewModesPart.name"));
        tmp.setFont(new Font("Tahoma", Font.BOLD, 11));
        tmp.setForeground(Color.decode("#194C7F"));
        add(tmp);
        final ButtonGroup buttonGroup = new ButtonGroup();

        final RtcPlanItemViewModeManager viewModeManager =
                plan.getPlansManager().getActiveProjectArea().getLookup().lookup(RtcPlanItemViewModeManager.class);

        final SwingWorker sw2 = new SwingWorker<RtcPlanItemViewMode, Void>() {

            @Override
            protected RtcPlanItemViewMode doInBackground() throws Exception {
                return viewModeManager.getDefaultViewMode(plan.getPlanType());
            }

            @Override
            protected void done() {

                RtcPlanItemViewMode defaultViewMode = null;
                try {
                    //System.out.println("wywoluje sw2");
                    defaultViewMode = get();
                    if (defaultViewMode != null) {
                        buttonGroup.setSelected(viewModes.get(defaultViewMode).getModel(), true);
                    }
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcViewModesSelectionPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcViewModesSelectionPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
                JButton editButton = new JButton(NbBundle.getMessage(RtcViewModesSelectionPanel.class, "ViewModeSelectionPanel.editButton.name"));
                editButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                editButton.setContentAreaFilled(false);
                editButton.setForeground(Color.decode("#000080"));
                editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                editButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        RtcViewModeEditorCallback callback =
                                Utilities.actionsGlobalContext().lookup(RtcViewModeEditorCallback.class);
                        if (!callback.isOpenned()) {
                            callback.openEditor();
                        } else {
                            callback.closeEditor();
                        }
                    }
                });
                add(editButton);
            }
        };

        SwingWorker sw = new SwingWorker<RtcPlanItemViewMode[], Void>() {

            @Override
            protected RtcPlanItemViewMode[] doInBackground() throws Exception {
                return viewModeManager.getViewModes(plan.getPlanType());
            }

            @Override
            protected void done() {
                List<RtcPlanItemViewMode> modes = null;
                try {
                    modes = new LinkedList<RtcPlanItemViewMode>(Arrays.asList(get()));
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcViewModesSelectionPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcViewModesSelectionPanel.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }

                JRadioButton radioButton;

                for (RtcPlanItemViewMode mode : modes) {
                    radioButton = new JRadioButton(mode.getDisplayName());
                    viewModes.put(mode, radioButton);
                    radioButton.getModel().addItemListener(new RtcItemsPageViewModeItemsListener(mode));
                    buttonGroup.add(radioButton);
                    add(radioButton);
                }

                sw2.execute();

            }
        };
        sw.execute();
    }

    private class RtcItemsPageViewModeItemsListener implements ItemListener {

        private RtcPlanItemViewMode viewMode;

        RtcItemsPageViewModeItemsListener(RtcPlanItemViewMode viewMode) {
            this.viewMode = viewMode;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (currnetlySelectedViewMode != viewMode) {
                    ic.add(viewMode);
                    if (currnetlySelectedViewMode != null) {
                        ic.remove(currnetlySelectedViewMode);
                    }
                    currnetlySelectedViewMode = viewMode;
                }
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
            }
        }
    }
}
