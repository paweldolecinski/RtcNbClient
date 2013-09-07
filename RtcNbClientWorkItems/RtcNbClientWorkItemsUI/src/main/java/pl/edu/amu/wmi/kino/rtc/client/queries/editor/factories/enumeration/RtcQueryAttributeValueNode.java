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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.factories.enumeration;

import java.awt.Image;
import java.awt.image.BufferedImage;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Patryk Żywica
 */
class RtcQueryAttributeValueNode extends AbstractNode {

    private Image icon;

    public RtcQueryAttributeValueNode(Children children, Image icon, InstanceContent ic) {
        super(children, new AbstractLookup(ic));
        assert (icon != null);
        this.icon = icon;
    }

    public RtcQueryAttributeValueNode(Children children, Image icon) {
        this(children, icon, new InstanceContent());
    }

    public RtcQueryAttributeValueNode(Children children) {
        this(children, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new InstanceContent());
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Image getIcon(int type) {
        return icon;
    }
}
