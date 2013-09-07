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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor;

/**
 *
 * @author Michał
 */
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.layout.Layout;

import java.awt.*;
import java.util.*;

class GalleryLayout implements Layout {

    private int gap;

    public GalleryLayout(int Gap) {
        this.gap = Gap;

    }

    @Override
    public void layout(Widget widget) {
        widget.getParentWidget().resolveBounds(null, widget.getScene().getClientArea().getBounds());
        Collection<Widget> children = widget.getChildren();
        int posX = 0;
        int posY = 0;
        int elements = 0;
        int maxHeight = 0;
        int levelNr = 0;
        int levelHeight = 0;

        for (Widget child : children) {
            elements++;
            Rectangle preferredBounds = child.getPreferredBounds();
            int x = preferredBounds.x;
            int y = preferredBounds.y;
            int width = preferredBounds.width;
            int height = preferredBounds.height;
            if (posX + width + gap * (elements)
                    > child.getScene().getView().getBounds().width) {
                elements = 0;
                posX = 0;
                posY = maxHeight + gap + levelHeight;
                levelHeight += gap + maxHeight;
                maxHeight = 0;
                levelNr++;
            }
            if (height > maxHeight) {
                maxHeight = height;
            }
            int lx = posX - x;
            int ly = posY - y;
            if (child.isVisible()) {
                child.resolveBounds(new Point(lx, ly), new Rectangle(x, y, width, height));
                posX += gap + child.getPreferredBounds().width;
            } else {
                child.resolveBounds(new Point(lx, ly), new Rectangle(x, y, 0, 0));
            }

        }
    }

    public boolean requiresJustification(Widget widget) {
        return true;
    }

    public void justify(Widget widget) {
        widget.revalidate();
    }
}
