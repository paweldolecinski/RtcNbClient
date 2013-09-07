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
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

/**
 *
 * @author michu
 * @author Dawid Holewa
 */
public class RtcEnumerationEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory {

    private List<RtcLiteral> possibleValues;
    private InplaceEditor ed = null;
    private Lookup context;

    public RtcEnumerationEditor(Lookup context) {
        super();
        this.context = context;

        RtcWorkItemAttributePossibleValues pv = context.lookup(RtcWorkItemAttributePossibleValues.class);

        if (pv != null) {
            possibleValues = pv.getPossibleValues();
        } else {
            possibleValues = new ArrayList<RtcLiteral>();
        }
    }

    /*
    private void initValue() {
    Identifier iden;
    if (super.getValue() instanceof String) {
    iden = Identifier.create(ILiteral.class, (String) super.getValue());
    } else {
    iden = (Identifier) super.getValue();
    }
    if (possibleValues != null && super.getValue() != null) {
    for (RtcLiteral iLiteral : possibleValues) {
    if (iLiteral.getId().equals((iden).getStringIdentifier())) {
    actualValue = iLiteral;
    }
    }
    }
    }*/
    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
        ((Graphics2D) gfx).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (getValue() instanceof RtcLiteral) {
            RtcLiteral value = (RtcLiteral) getValue();
            Image i = value.getIcon();

            if (i != null) {
                ((Graphics2D) gfx).drawImage(i, 3, 1, null);
                ((Graphics2D) gfx).drawString(value.getName(), 25, 13);
            } else {
                ((Graphics2D) gfx).drawString(value.getName(), 3, 13);
            }
        }

    }

    @Override
    public String getAsText() {
        if (getValue() instanceof RtcLiteral) {
            return ((RtcLiteral) getValue()).getName();
        }

        return ((RtcLiteral) getValue()).getName();
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

    @Override
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace(possibleValues, context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private List<RtcLiteral> possibleValues;
        private JComboBox combobox;

        public Inplace(List<RtcLiteral> possibleValues, final Lookup context) {
            this.possibleValues = possibleValues;
            combobox = new JComboBox(possibleValues.toArray());
            combobox.setRenderer(new Render());
            combobox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Object o = ((JComboBox) e.getSource()).getSelectedItem();
                    editor.setValue(o);
                    context.lookup(RtcWorkItemAttribute.class).setValue((RtcLiteral) o);
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
        }

        @Override
        public void reset() {
            this.combobox.setSelectedItem(editor.getValue());
        }
        private PropertyEnv env;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
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
            return cmpnt == combobox || combobox.isAncestorOf(cmpnt);
        }

        @Override
        public void addActionListener(ActionListener al) {
        }

        @Override
        public void removeActionListener(ActionListener al) {
        }

        private class Render extends DefaultListCellRenderer {

			private static final long serialVersionUID = 3923629134466255474L;

			@Override
            public Component getListCellRendererComponent(JList combobox, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(combobox, value, index, isSelected, cellHasFocus);
                if (((RtcLiteral) value).getIcon() != null) {
                    Icon icon = ImageUtilities.image2Icon(((RtcLiteral) value).getIcon());
                    label.setIcon(icon);
                }
                return label;
            }
        }
    }
}
