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
package pl.edu.amu.wmi.kino.netbeans.multiview;

import javax.swing.Action;
import org.openide.windows.TopComponent;

/**
 *
 * @author Patryk Żywica
 */
public interface KinoMultiViewCallback {

    /** Activates this multi view element in enclosing multi view component
     * context, if enclosing multi view top component is opened.
     */
    public void requestActive();

    /** Selects this multi view element in enclosing component context,
     * if component is opened, but does not activate it
     * unless enclosing component is in active mode already.
     */
    public void requestVisible();

    /**
     * Creates the default TopComponent actions as defined by the Window System.
     * Should be used by the element when constructing it's own getActions() return value.
     */
    public Action[] createDefaultActions();

    /**
     * Update the multiview's topcomponent title.
     */
    public void updateTitle(String title);

    /**
     * Element can check if it's currently the selected element.
     */
    public boolean isSelectedElement();

    /**
     * Returns the enclosing Multiview's topcomponent.
     */
    public TopComponent getTopComponent();
}
