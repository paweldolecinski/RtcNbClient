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
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertyDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.LargeTextDisplay;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.SmallTextDisplay;

/**
 *
 * @author Patryk Zywica
 */
public class NodeTextDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup){
        if (displayType.equals(SmallTextDisplay.class) || displayType.equals(LargeTextDisplay.class)) {
            return displayType.cast(new NodeTextDisplay());
        }
        return null;
    }

    private static class NodeTextDisplay extends PropertySupport<String> implements SmallTextDisplay,LargeTextDisplay, PropertyDisplay<String> {

        private static int index = 0;
        private String value;

        public NodeTextDisplay() {
            super("text" + index++, String.class, "", "", true,false);
        }

        public <T> HandlerRegistration addInputHandler(Input<T> input, InputHandler<T> h) {
            return new HandlerRegistration(new Runnable() {

                public void run() {
//                throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }

        public void setText(String text) {
            value = text;

        }

        public void setInfoStatus(SmallTextDisplay.Status status, String text) {
//        throw new UnsupportedOperationException("Not supported yet.");
        }
        
        public void setInfoStatus(LargeTextDisplay.Status status, String text) {
//        throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setHint(String hint) {
            setShortDescription(hint);
        }

        public void setLabel(String label) {
            setDisplayName(label);
        }

        public Property<String> asProperty() {
            return this;
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            this.value = val;
        }

        public void setId(String id) {
            setName(id);
        }
    }
}