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
import java.util.Date;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertyDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.DateDisplay;

/**
 *
 * @author Patryk Zywica
 */
public class NodeDateDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(DateDisplay.class)) {
            return displayType.cast(new NodeDateDisplay());
        }
        return null;
    }

    private static class NodeDateDisplay extends PropertySupport<String> implements PropertyDisplay<String>, DateDisplay {

        private static int index = 0;
        
        private Date value;

        public NodeDateDisplay() {
            super("date"+ index++, String.class, "", "", true, false);
        }

        @Override
        public Property<String> asProperty() {
            return this;
        }

        @Override
        public void setDate(Date date) {
            value = date;
        }

        @Override
        public void setInfoStatus(Status status, String text) {

        }

        @Override
        public void setLabel(String label) {
            setDisplayName(label);
        }

        @Override
        public <T> HandlerRegistration addInputHandler(Input<T> source, InputHandler<T> h) {
            return new HandlerRegistration(new Runnable() {

                @Override
                public void run() {
//                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return value.toString();
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = new Date(val);
        }

        @Override
        public void setId(String id) {
            setName(id);
        }
    }
}
