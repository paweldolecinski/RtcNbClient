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

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;

/**
 *
 * @author Patryk Żywica
 */
public class RtcViewModeEditor extends JPanel implements LookupListener, HelpCtx.Provider {

    private static final long serialVersionUID = 543461457756L;
    private RtcViewModeEditorNamePanel namePanel;
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
    private Result<RtcPlanItemViewMode> result;
    private RtcViewModeEditorCallback callback;
    private RtcViewModeEditorContentTabPanel contentTab;
    private Lookup context;

    public RtcViewModeEditor(RtcViewModeEditorCallback callback,Lookup context) {
        super(new BorderLayout());
        this.callback = callback;
        this.context=context;
        this.namePanel = new RtcViewModeEditorNamePanel(callback);

        result = context.lookupResult(RtcPlanItemViewMode.class);
        result.addLookupListener(this);
        result.allInstances();

        RtcPlanItemViewMode viewMode = context.lookup(RtcPlanItemViewMode.class);

        namePanel.update(viewMode);

        contentTab = new RtcViewModeEditorContentTabPanel();
        contentTab.update(viewMode);
        tabbedPane.add(NbBundle.getMessage(RtcViewModeEditor.class, "ViewModeEditor.ContentTab.name"), contentTab);
        tabbedPane.add(NbBundle.getMessage(RtcViewModeEditor.class, "ViewModeEditor.ViewLayoutTab.name"), new RtcViewModeEditorLayoutTabPanel());

        add(namePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        refreshContent(viewMode);
    }

    private void refreshContent(RtcPlanItemViewMode viewMode) {
        EventQueue.invokeLater(new RefreshRunnable(viewMode));
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        RtcPlanItemViewMode viewMode = context.lookup(RtcPlanItemViewMode.class);
        if (viewMode != null) {
            refreshContent(viewMode);
        }
    }

    private class RefreshRunnable implements Runnable {

        private RtcPlanItemViewMode mode;

        public RefreshRunnable(RtcPlanItemViewMode mode) {
            this.mode = mode;
        }

        @Override
        public void run() {
            namePanel.update(mode);
            contentTab.update(mode);
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
