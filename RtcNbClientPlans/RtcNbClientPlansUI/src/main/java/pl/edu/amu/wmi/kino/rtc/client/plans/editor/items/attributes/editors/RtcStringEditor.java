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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.editors;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author Michal Wojciechowski
 */

public class RtcStringEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory{

    private InplaceEditor ed = null;


    @Override
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private JTextField textField;
        private JLabel label;
        private PropertyEnv env;

        public Inplace() {
            textField = new JTextField();
            textField.setBorder(BorderFactory.createEmptyBorder());
            textField.setEditable(false);
            textField.setOpaque(false);
        }

        @Override
        public JComponent getComponent() {
            return textField;
        }

        @Override
        public Object getValue() {
            return textField.getText();
        }

        @Override
        public void setValue(Object object) {
            textField.setText((String) object);
        }

        @Override
        public void reset() {
            textField.setText((String) editor.getValue());
        }


        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            this.editor = propertyEditor;
            this.env = env;
            reset();
        }

        @Override
        public void clear() {
            editor = null;
            model = null;
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
            this.model = pm;
        }

        @Override
        public boolean isKnownComponent(Component cmpnt) {
            return cmpnt == textField || textField.isAncestorOf(cmpnt);
        }

        @Override
        public void addActionListener(ActionListener al) {

        }

        @Override
        public void removeActionListener(ActionListener al) {

        }

    }

}
