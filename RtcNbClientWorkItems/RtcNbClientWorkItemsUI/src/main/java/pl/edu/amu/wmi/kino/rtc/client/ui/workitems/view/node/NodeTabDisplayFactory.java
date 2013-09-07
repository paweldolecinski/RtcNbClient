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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view.node;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertyDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertySetDisplay;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemTabPresenter.TabSlot;

/**
 *
 * @author Patryk Zywica
 */
public class NodeTabDisplayFactory implements DisplayFactory {

    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (WorkItemTabPresenter.TabDisplay.class.equals(displayType)) {
            return (D) new NodeTabDisplay();
        }
        return null;
    }

    private static class NodeTabDisplay implements WorkItemTabPresenter.TabDisplay, PropertySetDisplay {

//        private EnumMap<TabSlot,Sheet.Set> sets = new EnumMap<TabSlot, Sheet.Set>(TabSlot.class);
        private Sheet.Set set = new Sheet.Set();

        public void addToSlot(TabSlot slot, Display content) {
            if (content instanceof PropertyDisplay<?>) {
                PropertyDisplay<?> pd = (PropertyDisplay<?>) content;

                set.put(pd.asProperty());
                //                if(!sets.containsKey(slot)){
//                    Sheet.Set set = new Sheet.Set();
//                    set.setDisplayName(slot.name());
//                    sets.put(slot, set);
//                }
//                sets.get(slot).put(pd.asProperty());
            }
        }

        public void removeFromSlot(TabSlot slot, Display content) {
            if (content instanceof PropertyDisplay<?>) {
                PropertyDisplay<?> pd = (PropertyDisplay<?>) content;
//                sets.get(slot).remove(pd.asProperty().getName());
                set.remove(pd.asProperty().getName());
            }
        }

        public void clearSlot(TabSlot slot) {
        }

        public void setInSlot(TabSlot slot, Display content) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Sheet.Set asPropertySet() {
//            return sets.values().toArray(new Sheet.Set[]{});
            return set;
        }
    }
}
