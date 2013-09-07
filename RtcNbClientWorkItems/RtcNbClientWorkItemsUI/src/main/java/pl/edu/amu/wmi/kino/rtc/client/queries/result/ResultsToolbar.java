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

import java.awt.Component;
import javax.swing.JToolBar;
import org.openide.util.actions.CallableSystemAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.EditQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.NewQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.PrevQueryAction;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.actions.RefreshAction;

/**
 * Class responsible for creating toolbar and attaching actions for buttons in this
 * toolbar. Toolbar is designed to be shown in <code>ResultsTopComponent</code>.
 * It should be obtained by calling <code>ResultsToolbar.getDefault()</code>.
 *
 * @author Szymon Sadło
 */
@Deprecated
public class ResultsToolbar {

    private JToolBar bar;
    private static ResultsToolbar results;

    private ResultsToolbar() {
        bar = new JToolBar();
        bar.setFloatable(false);
        bar.addSeparator();

        Component newQuery = CallableSystemAction.get(NewQueryAction.class).getToolbarPresenter();


        Component editQuery = CallableSystemAction.get(EditQueryAction.class).getToolbarPresenter();
        Component prevQuery = CallableSystemAction.get(PrevQueryAction.class).getToolbarPresenter();
        Component refresh = CallableSystemAction.get(RefreshAction.class).getToolbarPresenter();

        bar.add(newQuery);
        bar.add(editQuery);
        bar.add(prevQuery);
        bar.addSeparator();
        bar.add(refresh);
    }

    /**
     * Method used to obtain an instance of <code>ResultsToolbar</code>
     * @return
     */
    public static ResultsToolbar getDefault() {
        if (results == null) {
            results = new ResultsToolbar();
        }
        return results;
    }

    public JToolBar getToolbar() {
        return bar;
    }
}
