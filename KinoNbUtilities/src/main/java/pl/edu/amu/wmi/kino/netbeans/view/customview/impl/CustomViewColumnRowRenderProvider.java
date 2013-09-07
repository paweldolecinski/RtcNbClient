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
package pl.edu.amu.wmi.kino.netbeans.view.customview.impl;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.openide.nodes.Node;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnRenderProvider;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewRowRenderProvider;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewColumnRowRenderProvider implements CustomViewRowRenderProvider {

    private CustomViewColumnRenderProvider crp;
    private CustomViewColumnProvider cp;
    private Node node;
    private CustomViewCallback callback;
    private JPanel panel;

    public CustomViewColumnRowRenderProvider(CustomViewColumnRenderProvider crp, CustomViewColumnProvider cp, Node node) {
        this.crp = crp;
        this.cp = cp;
        this.node = node;
    }

    @Override
    public JComponent getPreColumn() {
        return crp.getPreColumn();
    }

    @Override
    public JComponent getPostColumn() {
        return crp.getPostColumn();
    }

    @Override
    public JComponent getMainColumn() {
        if (panel == null) {
            panel = new JPanel();
            BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
            panel.setLayout(boxLayout);
            panel.setBackground(callback.getBackgroundColor());
            panel.setForeground(callback.getForegroundColor());

            JComponent component;
            for (String column : cp.getColumns()) {
                component = crp.getColumn(column);
                panel.add(component);
            }
        }
        return panel;
    }

    @Override
    public boolean shouldDisplayPreColumn() {
        return crp.shouldDisplayPreColumn();
    }

    @Override
    public boolean shouldDisplayExpandColumn() {
        return crp.shouldDisplayExpandColumn();
    }

    @Override
    public boolean shouldDisplayPostColumn() {
        return crp.shouldDisplayPostColumn();
    }

    @Override
    public boolean isIndentAllowed() {
        return crp.isIndentAllowed();
    }

    @Override
    public void setSelected(boolean selected) {
        crp.setSelected(selected);
        if(selected){
            getMainColumn().setBackground(callback.getSelectedBackgroundColor());
            getMainColumn().setForeground(callback.getSelectedForegroundColor());
        }else{
            getMainColumn().setBackground(callback.getBackgroundColor());
            getMainColumn().setForeground(callback.getForegroundColor());
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
        crp.setExpanded(expanded);
    }

    @Override
    public void setFocused(boolean focused) {
        crp.setFocused(focused);
    }

    @Override
    public void setCallback(CustomViewCallback callback) {
        this.callback = callback;
        crp.setCallback(callback);
    }
}
