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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcTypeEditor extends PropertyEditorSupport
        implements ExPropertyEditor, InplaceEditor.Factory {

    private InplaceEditor ed = null;
    private Lookup context;
    private List<RtcWorkItemType> possibleValues;

    public RtcTypeEditor(Lookup context) {
        this.context = context;
        possibleValues = context.lookup(RtcPlanItemAttributePossibleValues.class).
                getPossibleValues();
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

    private static class Inplace implements InplaceEditor, ActionListener {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private List<RtcWorkItemType> possibleValues;
        private JLabel label;
        private PropertyEnv env;
        private RtcWorkItemType type;
        private JPopupMenu popup;

        public Inplace(List<RtcWorkItemType> possibleValues, final Lookup context) {
            this.possibleValues = possibleValues;
            label = new JLabel();
            label.setMinimumSize(new Dimension(20, 16));
            label.setPreferredSize(new Dimension(20, 16));

            label.setOpaque(false);
            label.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    showPopup(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    showPopup(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setOpaque(true);
                    label.setBackground(Color.decode("#99CCFF"));
                    label.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setOpaque(false);
                    label.repaint();
                }

                private void showPopup(MouseEvent e) {
                    popup.show(label, 0, label.getHeight());
                }
            });

            popup = new JPopupMenu();
            popup.setInvoker(label);

            for (RtcWorkItemType l : possibleValues) {
                JMenuItem item = new RtcMenuItem(l);
                if (l.getIcon() != null) {
                    item.setIcon(ImageUtilities.image2Icon(l.getIcon()));
                }
                popup.add(item);
                item.addActionListener(this);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof RtcMenuItem) {
                RtcMenuItem menuItem = (RtcMenuItem) e.getSource();
                editor.setValue(menuItem.getType());
                setValue(menuItem.getType());
            }
        }

        @Override
        public JComponent getComponent() {
            return label;
        }

        @Override
        public Object getValue() {
            return type;
        }

        @Override
        public void setValue(Object object) {
            RtcWorkItemType l = (RtcWorkItemType) object;
            type = l;
            if(l.getIcon() != null)
                label.setIcon(ImageUtilities.image2Icon(l.getIcon()));
        }

        @Override
        public void reset() {
            setValue(editor.getValue());
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            this.editor = propertyEditor;
            this.env = env;
            this.type = (RtcWorkItemType) editor.getValue();
            reset();
            popup.setLocation(0, label.getHeight());
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
            return cmpnt == label || label.isAncestorOf(cmpnt);
        }

        @Override
        public void addActionListener(ActionListener al) {
        }

        @Override
        public void removeActionListener(ActionListener al) {
        }

        private class RtcMenuItem extends JMenuItem {
			private static final long serialVersionUID = -3864887120900758576L;
			private RtcWorkItemType type;

            public RtcMenuItem(Icon icon, RtcWorkItemType type) {
                super(type.getDisplayName(), icon);
                this.type = type;
            }

            public RtcMenuItem(RtcWorkItemType type) {
                super(type.getDisplayName());
                this.type = type;
            }

            public RtcWorkItemType getType() {
                return type;
            }
        }

    }
}
