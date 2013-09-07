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
package pl.edu.amu.wmi.kino.netbeans.multiview.impl;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;

/**
 *
 * @author Patryk Żywica
 */
public class KinoMultiViewEmptyMVDescription extends KinoMultiViewDescription {

    @Override
    public KinoMultiViewElement createElement() {
        return new KinoMultiViewElement() {

            private JComponent toolbar = new JPanel();
            private JPanel panel;

            @Override
            protected JComponent createInnerComponent() {
                if (panel == null) {

                    panel = new JPanel();
                    //TODO : bikol : internationalize
                    panel.add(new JLabel("Loading"));
                }
                return panel;
            }

            @Override
            public JComponent getToolbarRepresentation() {
                return toolbar;
            }
        };
    }

    @Override
    public String getDisplayName() {
        //TODO : bikol : internationalize
        return "Loading";
    }
}
