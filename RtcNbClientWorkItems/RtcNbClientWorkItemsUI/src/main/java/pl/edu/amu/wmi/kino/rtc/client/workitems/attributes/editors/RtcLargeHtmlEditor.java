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
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Lookup;

/**
 * 
 * @author Pawel Dolecinski
 * @authot Michal Wojciechowski
 */
public class RtcLargeHtmlEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private final Lookup context;
    private Inplace ed;
    PropertyEnv env;

    public RtcLargeHtmlEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public String getAsText() {
        //return XMLString.createFromXMLText((String)getValue()).getXMLText().replaceAll("<br/>", "\n");
        return getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //setValue(XMLString.createFromXMLText(text).getXMLText().replaceAll("\n", "<br/>"));
        setValue(text);
    }

    @Override
    public boolean supportsCustomEditor() {

       
        return true;
    }

    @Override
    public Component getCustomEditor() {           
        return new ScrollPane(this, env);
    }

    private static class ScrollPane extends JScrollPane implements VetoableChangeListener {

		private static final long serialVersionUID = -1781142462567535151L;
		private final RtcLargeHtmlEditor editor;
        private final PropertyEnv env;
        private JTextArea textarea;

        private ScrollPane(final RtcLargeHtmlEditor editor, PropertyEnv env) {

            this.editor = editor;
            this.env = env;
            this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
            this.env.addVetoableChangeListener(this);

            textarea = new JTextArea(5, 30);
            setViewportView(textarea);

            textarea.setText(editor.getAsText());
            textarea.setAutoscrolls(false);
            textarea.setWrapStyleWord(true);
            textarea.setLineWrap(true);

            textarea.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    editor.setAsText(textarea.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    editor.setAsText(textarea.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    editor.setAsText(textarea.getText());
                }
            });

            setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }

        @Override
        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            editor.setAsText(textarea.getText());
        }
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        this.env.registerInplaceEditorFactory(this);
    }

    private static class Inplace implements InplaceEditor {

        private final JTextField textfield = new JTextField();
        private PropertyEditor editor = null;

        public Inplace() {
            textfield.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    editor.setAsText(textfield.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    editor.setAsText(textfield.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    editor.setAsText(textfield.getText());
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
            return textfield;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return textfield.getText();
        }

        @Override
        public void setValue(Object object) {
            textfield.setText((String) object);
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            Object value = (Object) editor.getValue();
            setValue(value);
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
            return component == textfield;
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

//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<String> {
//
//        @Override
//        public String toString(String value) {
//            return XMLString.createFromXMLText(value).getXMLText().replaceAll("<br/>", "\n");
//        }
//    }
}
