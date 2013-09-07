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
package pl.edu.amu.wmi.kino.rtc.client.workitems.editors;

import java.beans.PropertyEditor;
import org.openide.util.Lookup;

/**
 *
 * @author psychollek
 * @author Pawel Dolecinski
 */
public interface ContextPropertyEditorFactory {

    /**
     * this method returns an editor to property found in context. it uses it's
     * best guess to do so and returns null if no editor can be created by it.
     * @param context
     * @return editor for the property in context
     */
    public PropertyEditor createPropertyEditorFromContext(Lookup context);
}
