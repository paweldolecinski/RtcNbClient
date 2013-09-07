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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.development;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.ui.queries.result.ResultsTopComponent;
import org.openide.util.NbBundle;
public final class DevQueryWindow implements ActionListener, Runnable {

    private RtcQuery query;

    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(this);
    }

    @Override
    public void run() {
        if (!EventQueue.isDispatchThread()) {
            RtcConnectionsManager rcm = Lookup.getDefault().lookup(RtcConnectionsManager.class);
            if (rcm.getActiveProjectAreas().length == 0) {
                JOptionPane.showMessageDialog(null, NbBundle.getMessage(DevQueryWindow.class, "NoAreaConnection.text"));
            } else {
                ActiveProjectArea area = rcm.getActiveProjectAreas()[0];

                RtcQueriesManager qm = area.getLookup().lookup(RtcQueriesManager.class);
                if (qm.getPersonalQueriesSet().getQueries().length == 0) {
                    JOptionPane.showMessageDialog(null, NbBundle.getMessage(DevQueryWindow.class, "NoPerQ.text"));
                } else {
                    query = qm.getPersonalQueriesSet().getQueries()[0];
                    EventQueue.invokeLater(this);
                }
            }
        } else {
            ResultsTopComponent tc = ResultsTopComponent.findInstanceFor(query);
            tc.refreshResults();
            tc.open();
            tc.requestActive();
        }
    }
}
