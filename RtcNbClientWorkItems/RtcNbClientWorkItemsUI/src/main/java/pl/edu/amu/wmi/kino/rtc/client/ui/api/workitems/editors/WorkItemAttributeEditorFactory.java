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

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;

/**
 *
 * @author Patryk Żywica
 */
public interface WorkItemAttributeEditorFactory {

    /**
     * Creates new editor for given work item and attribute designed to be used in
     * given view target.
     *
     * Each invocation of this method should create always new editor.
     *
     * Remember that when editor is not necessary it should be garbage collected.
     * To allow this behavior do not place any strong references (e.g. strong listeners)
     * in work item or other facade objects. Editor lifetime is much shorter then work item's.
     *
     *
     * @param wc work item working copy that this editor will work with
     * @param attr work item attribute that editor will edit
     * @param target target used to determine which editor choose from all available for given attribute
     * @return newly created editor for work item and attribute
     */
    public abstract <T> WorkItemAttributeEditor<T> createEditor(
            RtcWorkItemWorkingCopy wc, RtcWorkItemAttribute<T> attr, RtcWorkItemViewTarget target);
    /**
     * Returns shared instance of editor description for given attribute and view target.
     *
     * Note that <code>WorkItemAttributeEditor</code> extends editor description so
     * it should provide the same information as object returned by this method.
     * @param <T>
     * @param attr
     * @param target
     * @return
     */
    public abstract <T> WorkItemAttributeEditorDescripion<T> getEditorDescription(RtcWorkItemAttribute<T> attr, RtcWorkItemViewTarget target);
}
