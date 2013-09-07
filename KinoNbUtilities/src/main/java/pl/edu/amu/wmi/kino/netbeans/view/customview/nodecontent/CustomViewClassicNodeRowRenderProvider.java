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
package pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent;

import java.beans.BeanInfo;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewClassicNodeRowRenderProvider implements CustomViewRowRenderProvider {

    private Node node;
    private CustomViewCallback callback;
    private JLabel label;

    public CustomViewClassicNodeRowRenderProvider(Node node) {
        this.node = node;
        label = new JLabel(node.getDisplayName(), ImageUtilities.image2Icon(node.getIcon(BeanInfo.ICON_COLOR_16x16)), JLabel.LEFT);
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
        return label;
    }

    @Override
    public boolean shouldDisplayPreColumn() {
        return true;
    }

    @Override
    public boolean shouldDisplayExpandColumn() {
        return true;
    }

    @Override
    public boolean shouldDisplayPostColumn() {
        return true;
    }

    @Override
    public boolean isIndentAllowed() {
        return true;
    }

    @Override
    public void setSelected(boolean selected) {
        ////System.out.println("Wywolanie setSelected w ClassicNode #"+hashCode()+" : "+selected);
        if(selected){
            label.setBackground(callback.getSelectedBackgroundColor());
            label.setForeground(callback.getSelectedForegroundColor());
        }else{
            label.setBackground(callback.getBackgroundColor());
            label.setForeground(callback.getForegroundColor());
        }
        
    }

    @Override
    public void setExpanded(boolean expanded) {
        //
    }

    @Override
    public void setFocused(boolean focused) {
        //
    }

    @Override
    public void setCallback(CustomViewCallback callback) {
        this.callback=callback;
    }
}
