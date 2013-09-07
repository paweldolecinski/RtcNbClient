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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXDatePicker;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;

/**
 *
 *
 * @author Son
 */
public class RtcTimestampEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private Lookup context;

    public RtcTimestampEditor(Lookup context) {
        this.context = context;
    }



    @Override
    public String getAsText() {
        Date d = (Date) getValue();
        if (d != null) {
            return new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(d);
        }
        //return "";
        return NbBundle.getMessage(RtcTimestampEditor.class, "NoDate");
        
    }

    @Override
    public void setAsText(String s) {
        try {
            setValue(new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));


        } catch (ParseException pe) {
            IllegalArgumentException iae = new IllegalArgumentException(NbBundle.getMessage(RtcTimestampEditor.class, "CannotAnalizeDate"));
            throw iae;
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Date) {
            Timestamp t = new Timestamp(((Date)value).getTime());
            super.setValue(t);

            if(value == null) {
                value = t;
            } else {
                if(!value.equals(t)) {
                    value = t;
                    //context.lookup(RtcWorkItemIAttribute.class).setValue(value);
                }
            }
        } else {
            super.setValue(value);
        }
        
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }

    private InplaceEditor ed = null;

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace(context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private Lookup context;

        private final JXDatePicker picker = new JXDatePicker();
        {
            picker.setFormats(new SimpleDateFormat("MM/dd/yy"));
        }
        private PropertyEditor editor = null;

        public Inplace(final Lookup context) {

            /*
            Date d = (Date) editor.getValue();
            if (d != null) {
                picker.setDate(d);
            }*/
            picker.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(picker.getDate() == null)
                        return;

                    editor.setValue(picker.getDate());
                    context.lookup(RtcWorkItemAttribute.class).setValue(new Timestamp((picker.getDate().getTime())));
                }
            });

            //why this is comment? isn't work?

//            picker.addPropertyChangeListener(new PropertyChangeListener() {
//
//                @Override
//                public void propertyChange(PropertyChangeEvent evt) {
//
//                    if(evt.getNewValue() == null)
//                        return;
//
//                    editor.setValue(evt.getNewValue());
//                    /*
//                    Timestamp t = new Timestamp(((Date) evt.getNewValue()).getTime());
//
//                    if(context.lookup(RtcWorkItemIAttribute.class) != null)
//                        context.lookup(RtcWorkItemIAttribute.class).setValue(t);
//                     *
//                     */
//                }
//            });
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return picker;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }


        @Override
        public Object getValue() {
            return picker.getDate();
        }

        @Override
        public void setValue(Object object) {
            if (object != null) {
                picker.setDate((Date) object);
            }
            else
                picker.setDate(Calendar.getInstance().getTime());
           editor.setValue(picker.getDate());
        }

        @Override
        public boolean supportsTextEntry() {
            return false;
        }

        @Override
        public void reset() {
            
            Date d = (Date) editor.getValue();
            if (d != null) {
                picker.setDate(d);
                
                //editor.setValue(d);
            }
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
        private PropertyModel model;

        @Override
        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component) {
            return component == picker || picker.isAncestorOf(component);
        }

        @Override
        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}
