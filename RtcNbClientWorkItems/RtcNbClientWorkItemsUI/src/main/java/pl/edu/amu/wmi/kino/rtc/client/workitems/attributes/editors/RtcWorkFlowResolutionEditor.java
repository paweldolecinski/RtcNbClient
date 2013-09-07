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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeEvents;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeListener;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;

/**
 *
 * @author michu
 * @author Dawid Holewa
 */
public class RtcWorkFlowResolutionEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory, RtcWorkItemAttributeListener {

    private Inplace ed = null;
    private Object actual_value = null;
    private RtcWorkItemAttributePrefferedValues pv;
    private RtcWorkItemAttribute attr;

    public RtcWorkFlowResolutionEditor(Lookup context) {

        pv = context.lookup(RtcWorkItemAttributePrefferedValues.class);

        attr = context.lookup(RtcWorkItemAttribute.class);
        attr.addListener(this);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
        ((Graphics2D) gfx).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        actual_value = (RtcWorkFlow) getValue();

        if (actual_value instanceof RtcWorkFlow) {
            Image i = ((RtcWorkFlow) actual_value).getIcon();

            if (i != null && ((RtcWorkFlow) actual_value).getName() != null) {
                ((Graphics2D) gfx).drawImage(i, 3, 1, null);
                ((Graphics2D) gfx).drawString(((RtcWorkFlow) actual_value).getName(), 25, 13);
            } else if (((RtcWorkFlow) actual_value).getName() != null) {
                ((Graphics2D) gfx).drawString(((RtcWorkFlow) actual_value).getName(), 3, 13);
            }
        } else if ((actual_value instanceof String)) {
            ((Graphics2D) gfx).drawString(getAsText(), 3, 13);
        }
    }

    @Override
    public String getAsText() {
        if (getValue() instanceof RtcWorkFlow) {
            return ((RtcWorkFlow) getValue()).getName();
        }

        return "";
    }

    @Override
    public void setValue(Object value) {
        if(value != null)
        super.setValue(value);

    }

    @Override
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace(pv.getPrefferedValues());
        }
        return ed;
    }

    @Override
    public void eventFired(RtcWorkItemAttribute source, RtcWorkItemAttributeEvents eventType) {
        if(eventType.equals(RtcWorkItemAttributeEvents.WORKFLOW_ACTION_CHANGED)) {
            ed.refreshPossibleValues(pv.getPrefferedValues());
        }
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private JComboBox combobox = new JComboBox();

        public void refreshPossibleValues(List<RtcWorkFlow> possibleValues) {
            combobox.removeAllItems();
            for (RtcWorkFlow f : possibleValues) {
                combobox.addItem(f);
            }
        }

        public Inplace(List<RtcWorkFlow> possibleValues) {
   
            for(RtcWorkFlow f : possibleValues) {
                combobox.addItem(f);
            }
            combobox.setRenderer(new Render());
            combobox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    editor.setValue(((JComboBox) e.getSource()).getSelectedItem());
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
            this.combobox.setSelectedItem(getValue());
            editor.setValue(getValue());
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
            return cmpnt == combobox || combobox.isAncestorOf(cmpnt);
        }

        @Override
        public void addActionListener(ActionListener al) {
        }

        @Override
        public void removeActionListener(ActionListener al) {
        }

        private class Render extends DefaultListCellRenderer {

			private static final long serialVersionUID = 4698013143833648146L;

			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null && ((RtcWorkFlow) value).getIcon() != null) {
                    Icon icon = ImageUtilities.image2Icon(((RtcWorkFlow) value).getIcon());
                    label.setIcon(icon);
                }
                return label;
            }
        }
    }

//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<RtcWorkFlow> {
//
//        @Override
//        public String toString(RtcWorkFlow value) {
//            return value.getName();
//        }
//    }
}
