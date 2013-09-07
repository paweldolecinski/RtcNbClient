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
package pl.edu.amu.wmi.kino.rtc.client.queries.result.actions;

import java.awt.EventQueue;
import java.util.HashMap;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.ResultsTopComponent;

/**
 *
 * @author Szymon Sadło
 */
public class PrevQueryAction extends CallableSystemAction implements LookupListener, Runnable {

    private Result<HashMap> res;
    private Result<RtcQuery> result;
    private HashMap<RtcQuery, RtcQuery> st;
    private RtcQuery previousQuery;

    public PrevQueryAction() {
        res = Utilities.actionsGlobalContext().lookupResult(HashMap.class);
        res.addLookupListener(this);
        result = Utilities.actionsGlobalContext().lookupResult(RtcQuery.class);
        result.addLookupListener(this);
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    public void performAction() {
        st = (HashMap<RtcQuery, RtcQuery>) res.allInstances().toArray()[0];
        RtcQuery query = (RtcQuery) result.allInstances().toArray()[0];
        if (st.get(query) != null) {
            previousQuery = st.get(query);
            EventQueue.invokeLater(this);
        } else {
            setEnabled(false);
        }
    }

    @Override
    public void run() {
        pl.edu.amu.wmi.kino.rtc.client.ui.queries.result.ResultsTopComponent tc = pl.edu.amu.wmi.kino.rtc.client.queries.result.ResultsTopComponent.findInstanceFor(previousQuery);
        //tc.refreshResults();
        tc.open();
        tc.requestActive();
    }

    @Override
    protected String iconResource() {
        return "pl/edu/amu/wmi/kino/rtc/client/queries/result/actions/prev_query.gif";
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(PrevQueryAction.class, "PrevQueryAction.name");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void resultChanged(LookupEvent le) {
        setEnabled(Utilities.actionsGlobalContext().lookup(RtcQuery.class) != null);
    }
}
