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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Lookup;

/**
 * This class is used to provide Inplace Editor and Custom Editor
 * which are used for editing of work item tags.
 * @author Pawel Dolecinski
 * @author Dawid Holewa
 */
public class RtcTagsEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    /**
     * Private Variables
     */
    private final Lookup context;
    private String oldValue;

    /**
     * Constructor of tags editor
     * @param it's reference to lookup which contains some needed objects
     * @see Lookup
     */
    public RtcTagsEditor(Lookup context) {
        this.context = context;
    }

    /**
     * This method gets String representation of the SeparatedStringList object
     * which contains tags assigned to Work Item.
     * This method overrides the method from parent class.
     *
     * @return empty string or String representation of list of tags
     */
    @Override
    public String getAsText() {
        List<String> value = (List<String>) getValue();

        if (value == null) {
            return "";
        }
        String ret = "";
        for (String string : value) {
            ret += string + ", ";
        }

        return ret;
    }

    @Override
    public void setAsText(String s) {
//        SeparatedStringList list = new SeparatedStringList();
//        if (s != null && s.length() > 1) {
//            list.clear();
//            for (String v : s.trim().split(",")) {
//                list.add(v.trim());
//            }
//        }
//
//        if(oldValue == null) {
//            oldValue = s;
//        } else {
//            if(!oldValue.equals(s))
//                context.lookup(RtcWorkItemIAttribute.class).setValue(list);
//        }
//
//        //hack
//
//
//        setValue(list);


    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        
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

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override
    public Component getCustomEditor() {
        final JTextArea textarea = new JTextArea(20, 50);
        textarea.setText(getAsText());
        textarea.setAutoscrolls(true);
        textarea.setWrapStyleWord(true);
        textarea.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!getAsText().equalsIgnoreCase(textarea.getText())) {
                    setAsText(textarea.getText());
                }
            }
        });
        //TODO: czy potrzebuje wogole custom editor?
        // owszem, potrzebujesz, gdy tylko moze pojawic sie wiecej textu zawsze jest potrebny.
        
        return textarea;
    }

    private static class Inplace implements InplaceEditor {

        private final JTextField textarea = new JTextField();
        private PropertyEditor editor = null;

        public Inplace(Lookup context) {
            textarea.setBorder(new LineBorder(Color.GRAY));
            textarea.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    editor.setAsText((String) getValue());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    editor.setAsText((String) getValue());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    editor.setAsText((String) getValue());
                }
            });
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            
            reset();
        }

        @Override
        public JComponent getComponent() {
            return textarea;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return textarea.getText();
        }

        @Override
        public void setValue(Object object) {
//            //SeparatedStringList value = (SeparatedStringList) object;
//            String svalue = null;
//            if (value != null) {
//                svalue = value.getSeparatedString();
//
//                if (svalue.length() >= 3) {
//                    svalue = svalue.substring(1, svalue.length() - 1).replace('|', ',');
//                } else {
//                    svalue = svalue.replace("|", "");
//                }
//
//                textarea.setText(svalue);
//            } else {
                textarea.setText("");
            //}
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            //SeparatedStringList value = (SeparatedStringList) editor.getValue();
            setValue(editor.getValue());
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
            return component == textarea;
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
//
//    public static class ToStringProviderImpl implements ToStringProvider<SeparatedStringList> {
//
//        @Override
//        public String toString(SeparatedStringList value) {
//            return value.toString();
//        }
//    }
//
//    public static ToStringProviderImpl getStringProvider() {
//        return new ToStringProviderImpl();
//    }
}
