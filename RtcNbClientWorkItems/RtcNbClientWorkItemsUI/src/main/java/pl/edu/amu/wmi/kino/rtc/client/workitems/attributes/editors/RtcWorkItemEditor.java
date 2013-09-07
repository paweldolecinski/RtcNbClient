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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors;

import java.beans.PropertyEditorSupport;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWorkItemEditor extends PropertyEditorSupport {

    private Lookup context;

    public RtcWorkItemEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public String getAsText() {
        RtcWorkItem wi = context.lookup(RtcWorkItem.class);

        if (getValue() != null) {
            //item = (IWorkItem) itemManager.fetchCompleteItem((IWorkItemHandle) getValue(), IItemManager.DEFAULT, null);
            return wi.getId() + ": " + wi.getDescription();
        }

        return "";
    }
//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<IWorkItemHandle> {
//
//        @Override
//        public String toString(IWorkItemHandle value) {
//            if (value.hasFullState()) {
//                return ((IWorkItem) value.getFullState()).getHTMLSummary().getPlainText();
//            }
//            return value.getStateId().toString();
//        }
//    }
}
