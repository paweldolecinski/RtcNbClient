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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDeliverable;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcDeliverableEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory {

    private RtcWorkItemAttributePossibleValues possibleValues;
    private InplaceEditor ed = null;
    private Lookup context;

    public RtcDeliverableEditor(Lookup context) {
        this.context = context;
        possibleValues = context.lookup(RtcWorkItemAttributePossibleValues.class);
    }

    @Override
    public String getAsText() {
        if (getValue() instanceof RtcDeliverable) {
            return ((RtcDeliverable) getValue()).getName();
        }

//        if (getValue() != null) {
//            RtcDeliverable d = new RtcDeliverableImpl((IDeliverableHandle) getValue());
//            return d.getName();
//        }
        return "";
    }

    @Override
    public void setValue(Object value) {

//        if (value instanceof RtcDeliverableImpl) {
//            if(((RtcDeliverableImpl) value).getDeliverable() != null)
//                super.setValue((IDeliverableHandle) ((RtcDeliverableImpl) value).getDeliverable().getItemHandle());
//        } else {
        super.setValue(value);
        //}

    }

    @Override
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace(possibleValues.getPossibleValues(), context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private List<RtcDeliverable> possibleValues;
        private JComboBox combobox;
        private Lookup context;
        private RtcDeliverable unassigned;

        public Inplace(List<RtcDeliverable> possibleValues, Lookup context) {
            this.possibleValues = possibleValues;
            this.context = context;

            combobox = new JComboBox(possibleValues.toArray());
            //unassigned = new RtcDeliverableImpl(null);
            combobox.addItem(unassigned);
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
            if (editor.getValue() == null) {
                this.combobox.setSelectedItem(unassigned);
            } else {
                this.combobox.setSelectedItem((RtcDeliverable) editor.getValue());
            }

        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();

            combobox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {


                    if (((RtcDeliverable) ((JComboBox) e.getSource()).getSelectedItem()) == null) {
                        return;
                    }

                    editor.setValue(((JComboBox) e.getSource()).getSelectedItem());

                    //IDeliverableHandle d = (IDeliverableHandle) ((RtcDeliverableImpl) ((JComboBox)e.getSource()).getSelectedItem()).getDeliverable().getItemHandle();


                    context.lookup(RtcWorkItemAttribute.class).setValue((RtcDeliverable) ((JComboBox) e.getSource()).getSelectedItem());


                }
            });
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
//
//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<IDeliverableHandle> {
//
//        @Override
//        public String toString(IDeliverableHandle value) {
//            return new RtcDeliverableImpl(value).getName();
//        }
//    }
}
