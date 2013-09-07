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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.editors;

import javax.swing.JComponent;

/**
 *
 * Note that for convenience this interface extends <code>RtcWorkItemAttributeEditorDescription</code>.
 * Implementation of those methods should delegate to object returned by editor factory.
 *
 * @see RtcWorkItemAttributeEditorFactory#getEditorDescription(pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute, pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget) 
 *
 * @author Patryk Żywica
 */
public interface WorkItemAttributeEditor<T> extends WorkItemAttributeEditorDescripion<T> {

    public abstract JComponent getEditorComponent();

    public abstract T getValue();

    public abstract void setValue(T value);

    public abstract void reset();

    //maybe it will be moved to other subclass or removed
    public abstract boolean supportsTextEntry();

    public abstract void setTextValue(String text);

    public abstract String getTextValue();
    //
}
