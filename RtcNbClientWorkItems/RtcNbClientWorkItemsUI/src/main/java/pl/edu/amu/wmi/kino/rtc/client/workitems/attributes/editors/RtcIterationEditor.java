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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;

/**
 * Iteration editor
 * @author Dawid Holewa
 */
public class RtcIterationEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory {

    private RtcWorkItemAttributePrefferedValues prefferedValues;
    private InplaceEditor ed = null;
    private Lookup context;

    public RtcIterationEditor(Lookup context) {
        this.context = context;
        prefferedValues = context.lookup(RtcWorkItemAttributePrefferedValues.class);
    }

    @Override
    public String getAsText() {
        if (getValue() instanceof Iteration) {
            return ((Iteration) getValue()).getName();
        }
//
//        if (getValue() != null) {
//            Iteration d = new RtcIterationImpl((IIterationHandle) getValue());
//            return d.getName();
//        }
        return "";
    }

    @Override
    public void setValue(Object value) {
//        if (value instanceof RtcIterationImpl) {
//            super.setValue((IIterationHandle) ((RtcIterationImpl) value).getIteration().getItemHandle());
//        } else {
            super.setValue(value);
       // }

    }

    @Override
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            if (getValue() != null) {
                Iteration d = (Iteration) getValue();
                ed = new Inplace(prefferedValues.getPrefferedValues(), d, context);
            }
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private List<Iteration> possibleValues;
        private JComboBox combobox;

        public Inplace(List<Iteration> possibleValues, Iteration d, final Lookup context) {
            this.possibleValues = possibleValues;

            combobox = new JComboBox(possibleValues.toArray());

            combobox.setSelectedItem(d);

            combobox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    editor.setValue(((JComboBox) e.getSource()).getSelectedItem());
                    //IIterationHandle i = (IIterationHandle) ((RtcIterationImpl) ((JComboBox) e.getSource()).getSelectedItem()).getIteration().getItemHandle();
                    context.lookup(RtcWorkItemAttribute.class).setValue((Iteration) ((JComboBox) e.getSource()).getSelectedItem());
                }
            });
        }

        @Override
        public JComponent getComponent() {
            return this.combobox;
        }

        @Override
        public Object getValue() {
            return this.combobox.getSelectedItem();
        }

        @Override
        public void setValue(Object object) {
            this.combobox.setSelectedItem(object);
            editor.setValue(object);
        }

        @Override
        public void reset() {
            this.combobox.setSelectedItem((Iteration) editor.getValue());
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public void clear() {
            editor = null;
            model = null;
        }

        @Override
        public boolean supportsTextEntry() {
            //throw new UnsupportedOperationException("Not supported yet.");
            return false;
        }

        @Override
        public void addActionListener(ActionListener al) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void removeActionListener(ActionListener al) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            //throw new UnsupportedOperationException("Not supported yet.");
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
            return cmpnt == combobox || combobox.isAncestorOf(cmpnt);
        }
    }

//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<IIterationHandle> {
//
//        @Override
//        public String toString(IIterationHandle value) {
//            return new RtcIterationImpl(value).getName();
//        }
//    }
}
