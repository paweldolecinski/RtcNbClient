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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertyDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler.OptionChooser;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.EnumerationDisplay;

/**
 *
 * @author Patryk Zywica
 */
public class NodeEnumerationDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(EnumerationDisplay.class)) {
            return displayType.cast(new NodeEnumerationDisplay());
        }
        return null;
    }
    //FIXME : it should not be String here, only for tests

    private static class NodeEnumerationDisplay extends PropertySupport<String> implements PropertyDisplay<String>, EnumerationDisplay {

        private static int index = 0;
        private String value = "NoValue";
        private PropertyEditor editor;

        public NodeEnumerationDisplay() {
            super("enum" + index++, String.class, "", "", true, false);
        }

        public Property<String> asProperty() {
            return this;
        }

        public void setBasicOptions(Node[] nodes) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setSelected(Node node) {
            value = node.getDisplayName();
        }

        public void setExpandedOptions(Node rootNode, Node[] selectableNodes) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setLabel(String labelText) {
            setDisplayName(labelText);
        }

        public void showExpandedOptionsDialog() {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public HandlerRegistration addOptionHandler(OptionChooser source, OptionChooseHandler h) {
            return new HandlerRegistration(new Runnable() {

                public void run() {
//                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }

        public <T> HandlerRegistration addInputHandler(Input<T> input, InputHandler<T> h) {
            return new HandlerRegistration(new Runnable() {

                public void run() {
//                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = value;
        }

        public void setId(String id) {
            setName(id);
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if(editor==null){
                editor=new Editor(super.getPropertyEditor());
            }
            return editor;
        }
        
    }

    private static class Editor implements PropertyEditor {

        private PropertyEditor ed;

        Editor(PropertyEditor ed) {
            this.ed = ed;
        }

        public String toString() {
            return ed.toString();
        }

        public int hashCode() {
            return ed.hashCode()+5451;
        }

        public boolean equals(Object obj) {
            return ed.equals(obj);
        }

        public boolean supportsCustomEditor() {
            return false;
        }

        public void setValue(Object value) {
            ed.setValue(value);
        }

        public void setAsText(String text) throws IllegalArgumentException {
            ed.setAsText(text);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            ed.removePropertyChangeListener(listener);
        }

        public void paintValue(Graphics gfx, Rectangle box) {
            ed.paintValue(gfx, box);
        }

        public boolean isPaintable() {
            return ed.isPaintable();
        }

        public Object getValue() {
            return ed.getValue();
        }

        public String[] getTags() {
            return ed.getTags();
        }

        public String getJavaInitializationString() {
            return ed.getJavaInitializationString();
        }

        public Component getCustomEditor() {
            return ed.getCustomEditor();
        }

        public String getAsText() {
            return ed.getAsText();
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            ed.addPropertyChangeListener(listener);
        }
    }
}
