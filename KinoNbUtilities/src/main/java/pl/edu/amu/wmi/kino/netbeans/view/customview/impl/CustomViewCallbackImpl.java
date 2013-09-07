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

import java.awt.Color;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;
import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewNode;

/**
 *
 * @author Patryk Żywica
 */
public class CustomViewCallbackImpl implements CustomViewCallback {

    private CustomViewNode node;
    private Color selectedBackgroundColor;
    private Color selectedForegroundColor;
    private Color backgroundColor;
    private Color foregroundColor;

    public CustomViewCallbackImpl(CustomViewNode node, Color selectedBackgroundColor, Color selectedForegroundColor, Color backgroundColor, Color foregroundColor) {
        this.node = node;
        this.selectedBackgroundColor = selectedBackgroundColor;
        this.selectedForegroundColor = selectedForegroundColor;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }
    

    @Override
    public void expand() {
        node.expand();
    }

    @Override
    public void collapse() {
        node.collapse();
    }

    @Override
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    @Override
    public Color getSelectedForegroundColor() {
        return selectedForegroundColor;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public Color getForegroundColor() {
        return foregroundColor;
    }

}
