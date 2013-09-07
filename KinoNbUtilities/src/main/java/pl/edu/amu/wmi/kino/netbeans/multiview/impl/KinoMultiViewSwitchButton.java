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

import java.awt.EventQueue;
import javax.swing.JToggleButton;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription.KinoMultiViewDescriptionEvent;

/**
 *
 * @author Patryk Żywica
 */
public class KinoMultiViewSwitchButton extends JToggleButton implements KinoMultiViewDescription.KinoMultiViewDescriptionListener {

    private static final long serialVersionUID = 32413537876L;
    private KinoMultiViewDescription desc;

    public KinoMultiViewSwitchButton(KinoMultiViewDescription desc) {
        super(desc.getDisplayName());
        this.desc = desc;
        setIcon(ImageUtilities.image2Icon(desc.getIcon()));
        setName(desc.getDisplayName());
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        desc.removeListener(this);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        desc.addListener(this);
    }

    @Override
    public void eventFired(KinoMultiViewDescriptionEvent event) {
        if (event.equals(KinoMultiViewDescriptionEvent.ICON_CHANGED)
                || event.equals(KinoMultiViewDescriptionEvent.NAME_CHANGED)) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    setIcon(ImageUtilities.image2Icon(desc.getIcon()));
                    setText(desc.getDisplayName());
                }
            });
        }
    }
}
