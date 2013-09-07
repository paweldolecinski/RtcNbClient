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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
//import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemIAttribute.IconProvider;
//import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemIAttribute.ToStringProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

/**
 * Default Work Item Type editor. It uses combobox to show/edit results.
 * You have to remember that possible values are IWorkItemType type. From this object
 * you can get name, icon and so on. And this type should be used in combobox. But when you get
 * selected value and set value you have to send String which is not name but Identifier.
 *
 * @author Michal Wojciechowski
 * @author Dawid Holewa
 */
public class RtcTypeEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory {

    private RtcWorkItemType actual_value = (RtcWorkItemType) getValue();
    private final List<RtcWorkItemType> types;
    private InplaceEditor ed = null;
    private Lookup context;

    /**
     * Default constructor.
     * @param context should contain IAttribute and IWorkItem objects
     */
    public RtcTypeEditor(Lookup context) {
        this.context = context;
        this.types = context.lookup(RtcWorkItemAttributePossibleValues.class).getPossibleValues();
        //this.actual_value = new RtcWorkItemTypeImpl(wi);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
        ((Graphics2D) gfx).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ((Graphics2D) gfx).drawImage(actual_value.getIcon(), 3, 1, null);
        ((Graphics2D) gfx).drawString(actual_value.getDisplayName(), 25, 13);
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value != null) {
            if (value instanceof RtcWorkItemType) {
                return ((RtcWorkItemType) value).getDisplayName();
            }
//            else if (value instanceof String) {
//                return actual_value.getDisplayName();
//            }
        }
        return "";
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof RtcWorkItemType) {
            RtcWorkItemType obj = (RtcWorkItemType) value;
            super.setValue((RtcWorkItemType) value);
            this.actual_value = obj;
            if (!types.contains(obj)) {
                types.add(obj);
            }
        } else {
            super.setValue(value);
        }

    }

    /**
     *
     * @return
     */
    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace(types, actual_value, context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private final JComboBox list;
        private PropertyEditor editor = null;

        private Inplace(List<RtcWorkItemType> preffered, RtcWorkItemType select, final Lookup context) {
            list = new JComboBox(preffered.toArray());
            list.setRenderer(new Render());
            list.setSelectedItem(select);
            list.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    editor.setValue(((JComboBox) e.getSource()).getSelectedItem());

                    context.lookup(RtcWorkItemAttribute.class).setValue((RtcWorkItemType) ((JComboBox) e.getSource()).getSelectedItem());
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
            RtcWorkItemType value = (RtcWorkItemType) object;
            if (value != null) {
                list.setSelectedItem(value);
                editor.setValue(value);
            }
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            /*
            if (editor.getValue() != null) {
                list.setSelectedItem(editor.getValue());
            }*/
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

        private class Render extends DefaultListCellRenderer {

			private static final long serialVersionUID = -3926528563040672026L;

			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (((RtcWorkItemType) value).getIcon() != null) {
                    Icon icon = ImageUtilities.image2Icon(((RtcWorkItemType) value).getIcon());
                    label.setIcon(icon);
                }
                return label;
            }
        }
    }
}
