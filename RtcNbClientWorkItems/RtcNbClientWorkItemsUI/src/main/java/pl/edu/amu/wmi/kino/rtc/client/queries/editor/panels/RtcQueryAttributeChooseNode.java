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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.panels;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;

/**
 *
 * @author Szymon Sadło
 */
class RtcQueryAttributeChooseNode extends AbstractNode {

    private Image icon;

    public RtcQueryAttributeChooseNode(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider vals) {
        this(value, vals, null);
    }

    public RtcQueryAttributeChooseNode(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider vals, Lookup lookup) {
        super(vals.getChildValues(value).length > 0 ? Children.create(new RtcQueryAttributeValueChildFactory(value, vals), true) : Children.LEAF, lookup);
        try {
            icon = vals.getIconFor(value);
        } catch (IllegalArgumentException ex) {
            icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        try {
            setDisplayName(vals.getDisplayName(value));
        } catch (IllegalArgumentException ex) {
            setDisplayName(value.toString());
        }
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

class RtcQueryAttributeValueChildFactory extends ChildFactory<RtcQueryAttributeValue> {

    private RtcQueryAttributeValue value;
    private RtcQueryAttributeValueProvider vals;

    public RtcQueryAttributeValueChildFactory(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider vals) {
        this.value = value;
        this.vals = vals;
    }

    @Override
    protected boolean createKeys(List<RtcQueryAttributeValue> toPopulate) {
        for (RtcQueryAttributeValue v : vals.getChildValues(value)) {
            toPopulate.add(v);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcQueryAttributeValue key) {
        return new RtcQueryAttributeChooseNode(key, vals);
    }
}
