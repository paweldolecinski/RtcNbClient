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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems;

import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;

/**
 *
 * This manager should be used to deal with main work item editors opened in separate
 * TopComponent.
 * <p/>
 * Work item main editor is a fully functional editor for work item. It should present
 * all properties of work item to user.
 *
 * @author Patryk Żywica
 */
public interface WorkItemEditorManager {

    /**
     * Opens work item main editor as TopComponent in 'editor' location. If there is
     * opened editor for given work item is should get focus.
     *
     * @param wi work item for which editor should be opened
     * @return
     */
    void openEditor(RtcWorkItem wi);

    /**
     * Tries to close work item editor.
     * <p/>
     * Editor can veto this request and stay open after this call.
     *
     * @param wi work item for which editor should be closed
     */
    void closeEditor(RtcWorkItem wi);
}
