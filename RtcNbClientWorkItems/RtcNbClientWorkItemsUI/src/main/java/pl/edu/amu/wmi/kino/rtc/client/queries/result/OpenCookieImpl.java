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
package pl.edu.amu.wmi.kino.rtc.client.queries.result;

import java.awt.EventQueue;
import org.openide.cookies.OpenCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.ui.queries.result.ResultsTopComponent;

/**
 * Used to handle Open action from context menu of defined query - creates
 * <code>ResultsTopComponent</code> displaying results for selected query.
 * <p>
 *
 * <code>ResultsTopComponent</code> is created in separate thread.
 *</p>
 *
 * @author Szymon Sadlo
 * @see ResultsTopComponent
 */
@Deprecated
public class OpenCookieImpl implements OpenCookie, Runnable {

    private RtcQuery query;

    public OpenCookieImpl(RtcQuery query) {
        this.query = query;
    }

    @Override
    public void open() {
        EventQueue.invokeLater(this);
    }

    @Override
    public void run() {
        ResultsTopComponent tc = pl.edu.amu.wmi.kino.rtc.client.queries.result.ResultsTopComponent.findInstanceFor(query);
        tc.refreshResults();
        tc.open();
        tc.requestActive();
    }
}
