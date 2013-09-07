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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jdesktop.swingx.renderer.JRendererLabel;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeEvents;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeListener;

/**
 * This is an utility class, for creating labels out of attributes.
 * @author psychollek
 */
public class RtcAttributeLabelFactory {

    public static JLabel createLabelRepresentation(RtcWorkItemAttribute attr) {
        JLabel label = new JRendererLabel();

        if ((attr.getAttributeId().equals("internalPriority")
                || attr.getAttributeId().equals("internalSeverity")
                || attr.getAttributeId().equals("workItemType"))
                && (attr.getIcon() != null)) {
            label.setIcon(new ImageIcon(attr.getIcon()));
            return label;
        } else {
            if (attr.getIcon() != null) {
                label.setIcon(new ImageIcon(attr.getIcon()));
            }
            label.setText(attr.toString());
        }
        return label;
    }

    public static class LabelProvider implements RtcWorkItemAttributeListener {

        private final JLabel label;

        public LabelProvider(RtcWorkItemAttribute attr) {
            this.label = createLabelRepresentation(attr);
            attr.addListener(this);
        }

        public JLabel getLabel() {
            return label;
        }

        @Override
        public void eventFired(RtcWorkItemAttribute source, RtcWorkItemAttributeEvents eventType) {
            if (source.getIcon() != null) {
                label.setIcon(new ImageIcon(source.getIcon()));
            }
            label.setText(source.toString());
        }
    }

    public static interface LabelSniffer {

        public LabelProvider findLabelProvider(String attrId);
    }
}
