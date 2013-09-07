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
import javax.swing.JComponent;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Patryk Żywica
 */
public abstract class KinoMultiViewElement {

    protected InstanceContent ic = new InstanceContent();
    private AbstractLookup lookup = new AbstractLookup(ic);
    protected KinoMultiViewCallback callback;
    private JComponent visualRepresentation;

    public JComponent getVisualRepresentation() {
        if (visualRepresentation == null) {
            visualRepresentation = createInnerComponent();
        }
        return visualRepresentation;
    }

    /**
     *
     * @return component that have to be shown inside this multiview element
     */
    protected abstract JComponent createInnerComponent();

    public abstract JComponent getToolbarRepresentation();

    public Action[] getActions() {
        return callback.createDefaultActions();
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void componentOpened() {
        //
    }

    public void componentClosed() {
        //
    }

    public void componentShowing() {
        //
    }

    public void componentHidden() {
        //
    }

    public void componentActivated() {
        //
    }

    public void componentDeactivated() {
        //
    }

    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    public void setMultiViewCallback(KinoMultiViewCallback callback) {
        this.callback = callback;
    }

    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    public enum CloseOperationState {

        STATE_OK;
    }
}
