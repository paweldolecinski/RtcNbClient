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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers;

import java.awt.Color;
import javax.swing.JComponent;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewRowRenderProvider;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping.RtcPlanItemGroup;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers.panels.RtcGroupPanel;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcGroupRowRenderProvider implements CustomViewRowRenderProvider {

    private RtcPlanItemGroup group;
    private RtcGroupPanel panel;
    private CustomViewCallback callback;

    public RtcGroupRowRenderProvider(RtcPlanItemGroup group) {
        this.group = group;
    }

    @Override
    public JComponent getPreColumn() {
        return null;
    }

    @Override
    public JComponent getPostColumn() {
        return null;
    }

    @Override
    public JComponent getMainColumn() {
        return panel;
    }

    @Override
    public boolean shouldDisplayPreColumn() {
        return false;
    }

    @Override
    public boolean shouldDisplayExpandColumn() {
        return true;
    }

    @Override
    public boolean shouldDisplayPostColumn() {
        return false;
    }

    @Override
    public boolean isIndentAllowed() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            panel.setBackground(callback.getSelectedBackgroundColor());
            panel.setForeground(callback.getSelectedForegroundColor());
        } else {
            panel.setBackground(Color.decode("#F3F3F3"));
            panel.setForeground(callback.getForegroundColor());
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFocused(boolean focused) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCallback(CustomViewCallback callback) {
        this.callback = callback;
        panel = new RtcGroupPanel(callback, group);
        panel.setBackground(Color.decode("#F3F3F3"));
        panel.setForeground(callback.getForegroundColor());
    }
}
