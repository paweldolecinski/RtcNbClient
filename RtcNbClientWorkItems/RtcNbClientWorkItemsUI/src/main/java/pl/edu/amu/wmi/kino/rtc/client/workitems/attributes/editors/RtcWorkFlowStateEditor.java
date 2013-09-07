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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowState;

/**
 *
 * @author Pawel Dolecinski
 * @author Dawid Holewa
 */
public class RtcWorkFlowStateEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private InplaceEditor ed = null;
    private Lookup context;
    private final RtcWorkItemAttributePrefferedValues pv;
    private List<Object> states = new ArrayList<Object>();
    private Object actual_value = null;
    private Object server_value = null;

    public RtcWorkFlowStateEditor(Lookup context) {
        this.context = context;
        this.pv = this.context.lookup(RtcWorkItemAttributePrefferedValues.class);
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value instanceof RtcWorkFlow) {
            return ((RtcWorkFlow) value).getName();
        }

        return "";
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        
    }



    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
        ((Graphics2D) gfx).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        this.actual_value = getValue();
        if (actual_value instanceof RtcWorkFlow && actual_value !=null) {
            Image i = ((RtcWorkFlow) actual_value).getIcon();

            if (i != null) {
                ((Graphics2D) gfx).drawImage(i, 3, 1, null);
                ((Graphics2D) gfx).drawString(((RtcWorkFlow) actual_value).getName(), 25, 13);
            } else {
                ((Graphics2D) gfx).drawString(((RtcWorkFlow) actual_value).getName(), 1, 12);
            }
//        } else if (actual_value !=null && actual_value instanceof RtcWorkFlowState) {
//            Image i = ((RtcWorkFlowState) actual_value).getIcon();
//            String name = ((RtcWorkFlowState) actual_value).getName();
//            if (i != null && name != null) {
//                ((Graphics2D) gfx).drawImage(i, 3, 1, null);
//                ((Graphics2D) gfx).drawString(name, 25, 13);
//            } else if(name != null) {
//                ((Graphics2D) gfx).drawString(((RtcWorkFlowState) actual_value).getName(), 1, 13);
//            }
        } else {
            ((Graphics2D) gfx).drawString(getAsText(), 1, 13);
        }

    }

    @Override
    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (this.server_value == null) {
            this.server_value = getValue();
        }

        if (getValue() instanceof RtcWorkFlowState) {
            pv.setConstraint((RtcWorkFlowState) getValue());
        }

        actual_value = getValue();
        states.addAll(pv.getPrefferedValues());
        // states.contains doesn't work ...
        boolean is_in = false;
        for (Object obj : states) {
            if (obj.toString().equalsIgnoreCase(server_value.toString())) {
                is_in = true;
                break;
            }
        }

        if (!is_in) {
            states.add(server_value);
        }

        if (ed == null) {
            ed = new Inplace(states, actual_value, context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        public final JComboBox list;
        private PropertyEditor editor = null;

        private Inplace(List<Object> preffered, Object select, final Lookup context) {
            list = new JComboBox(preffered.toArray());
            list.setSelectedItem(select);
            list.setRenderer(new Render());
            list.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Object o = ((JComboBox) e.getSource()).getSelectedItem();
                    editor.setValue(o);
                    context.lookup(RtcWorkItemAttribute.class).setValue(o);
                }
            });
        }

        private class Render extends DefaultListCellRenderer {

			private static final long serialVersionUID = 985271168982793844L;

			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Image image = ((RtcWorkFlow) value).getIcon();
                
                if(image != null)
                {
                    Icon icon = ImageUtilities.image2Icon(image);
                    label.setIcon(icon);
                }
                return label;
            }
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return list;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return list.getSelectedItem();
        }

        @Override
        public void setValue(Object object) {
            if (object != null) {
                list.setSelectedItem(object);
                editor.setValue(object);
            }
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            Object value = (Object) editor.getValue();
            if (value != null) {
                list.setSelectedItem(value);
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
            return component == list;
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
//    public static ToStringProviderImpl getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//    public static class ToStringProviderImpl
//            implements ToStringProvider<RtcWorkFlow> {
//
//        @Override
//        public String toString(RtcWorkFlow value) {
//            return value.getName();
//        }
//    }
}

