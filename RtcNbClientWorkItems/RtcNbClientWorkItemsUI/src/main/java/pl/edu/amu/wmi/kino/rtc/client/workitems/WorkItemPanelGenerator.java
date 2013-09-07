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

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.openide.explorer.propertysheet.PropertyPanel;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet;
import pl.edu.amu.wmi.kino.rtc.client.workitems.panels.RtcRoundedSectionPanel;

/**
 *
 * @author michu
 */
@ServiceProvider(service = WorkItemPanelGenerator.class)
public class WorkItemPanelGenerator {

    public void fillProperies(RtcWorkItemAttributeSet.RtcWorkItemAttributeSection sec,
            JPanel sectionPanel,
            RtcRoundedSectionPanel sectionHeader) {

        sectionHeader.setLabelText(sec.getDisplayName());

        int size = sec.size();
        boolean xAxis = false;

        for (final RtcWorkItemAttribute attr : sec) {
            if (attr.getPropertySupport() == null) {
                continue;
            }
            PropertyPanel propertyPanel = new PropertyPanel(attr.getPropertySupport());
            propertyPanel.setMinimumSize(new Dimension(10, 10));

            RtcWorkItemAttribute.Type type = ((RtcWorkItemAttribute) attr).getAttributeType();


            JPanel contener = new JPanel();

            if (type.equals(RtcWorkItemAttribute.Type.LARGE_HTML)
                    || type.equals(RtcWorkItemAttribute.Type.LARGE_STRING)
                    || type.equals(RtcWorkItemAttribute.Type.COMMENTS)
                    || type.equals(RtcWorkItemAttribute.Type.APPROVALS)
                    || type.equals(RtcWorkItemAttribute.Type.ATTACHMENTS)
                    || type.equals(RtcWorkItemAttribute.Type.HISTORY)
                    || type.equals(RtcWorkItemAttribute.Type.SUBSCRIPTIONS)
                    || type.equals(RtcWorkItemAttribute.Type.LINKS)) {

                if (attr.getPropertySupport().getPropertyEditor() == null || attr.getPropertySupport().getPropertyEditor().supportsCustomEditor() == false) {
                    continue;
                }

                propertyPanel.setPreferences(PropertyPanel.PREF_CUSTOM_EDITOR);
                contener.setLayout(new BoxLayout(contener, BoxLayout.Y_AXIS));
                xAxis = false;
            } else {
                //propertyPanel.setPreferences(PropertyPanel.PREF_TABLEUI);
                contener.setLayout(new BoxLayout(contener, BoxLayout.X_AXIS));
                contener.setMaximumSize(new Dimension(10000, 25));
                xAxis = true;
            }

            JLabel attrLabel = new JLabel(attr.getAttributeDisplayName());
            attrLabel.setAlignmentX(0);
            attrLabel.setBackground(Color.white);
            attrLabel.setPreferredSize(new Dimension(130, 20));
            attrLabel.setForeground(Color.decode("#000080"));

            propertyPanel.setAlignmentX(0);
            propertyPanel.setOpaque(false);

            if (size > 1 || xAxis == true) {
                contener.add(attrLabel);
            }

            contener.add(propertyPanel);
            contener.setAlignmentX(0);
            contener.setAlignmentY(0);
            contener.setBackground(Color.white);
            contener.setBorder(new EmptyBorder(0, 0, 5, 0));

            sectionPanel.add(contener);
        }
    }
}
