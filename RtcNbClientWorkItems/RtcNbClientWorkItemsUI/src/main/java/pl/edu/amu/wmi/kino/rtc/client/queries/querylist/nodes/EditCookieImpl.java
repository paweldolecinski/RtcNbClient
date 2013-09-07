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
package pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes;

import java.awt.EventQueue;
import org.openide.cookies.EditCookie;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.QueryEditorTopComponent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

/**
 *
 * @author Patryk Żywica
 */
class EditCookieImpl implements EditCookie, Runnable {

    private RtcQuery query;
    private RtcQueriesSet set;

    public EditCookieImpl(RtcQuery query, RtcQueriesSet set) {
        this.query = query;
        this.set = set;
    }

    @Override
    public void edit() {
        EventQueue.invokeLater(this);
    }

    @Override
    public void run() {
        TopComponent tc = QueryEditorTopComponent.findInstanceFor(query, set);
        tc.open();
        tc.requestActive();
    }
}
