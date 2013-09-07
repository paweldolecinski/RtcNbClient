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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcContributorEditorPanel;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;

/**
 * Contributor editor
 * @author Michal Wojciechowski
 * @author Dawid Holewa
 */
public class RtcContributorEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {
    // TODO: Add comments to class and methods

    private InplaceEditor ed = null;
    private final RtcWorkItemAttributePossibleValues possible;
    private List<Contributor> preffered;
    private List<Contributor> possibleValues = new ArrayList<Contributor>();
    private PropertyEnv env;
    private Lookup context;

    /**
     * Gets obects of needed class from lookup.
     * Sets <b>preffered</b> and <b>possible</b> values for contributors.
     * Possile values is lst of all possible user on the serwer.
     * Preefered values is list of all  user in our team.
     * @param context
     */
    public RtcContributorEditor(Lookup context) {
        this.context = context;
        possible = context.lookup(RtcWorkItemAttributePossibleValues.class);
        RtcWorkItem wi = context.lookup(RtcWorkItem.class);
        RtcWorkItemAttributePrefferedValues pv = context.lookup(RtcWorkItemAttributePrefferedValues.class);
        RtcCategory cat = wi.getCategory();

        pv.setConstraint(cat.getAssociatedTeamAreas());
        preffered = pv.getPrefferedValues();
    }

    @Override
    public Component getCustomEditor() {
        return new RtcContributorEditorPanel(this, env);
    }

    public List<Contributor> getPossibleValues() {
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                possibleValues = possible.getPossibleValues();
            }
        }).run();

        return possibleValues;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (getValue() instanceof Contributor) {
            return ((Contributor) getValue()).getName();
        }

//        if (value != null) {
//            RtcContributorImpl rtcContributorImpl = new RtcContributorImpl((IContributorHandle) getValue());
//            return rtcContributorImpl.getName();
//        }
        return "";
    }

    @Override
    public void setValue(Object value) {

        if (value instanceof Contributor) {
            super.setValue(
                    ((Contributor) value));
            if (!preffered.contains((Contributor) value)) {
                preffered.add((Contributor) value);

            }
        } else {
            super.setValue(value);
        }

    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
        env.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        Contributor rtcContributor = (Contributor) getValue();
        if (!preffered.contains(rtcContributor)) {
            preffered.add(rtcContributor);
        }
        if (ed == null) {
            ed = new Inplace(preffered, rtcContributor, context);
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private final JComboBox list;
        private PropertyEditor editor = null;
        private Contributor contributor;
        private List<Contributor> preffered;

        private Inplace(List<Contributor> preffered, Contributor contributor, final Lookup context) {
            this.preffered = preffered;
            list = new JComboBox(preffered.toArray());
            list.setSelectedItem(contributor);
            this.contributor = contributor;
            list.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    editor.setValue(((JComboBox) e.getSource()).getSelectedItem());


                    //IContributorHandle o = (IContributorHandle) ((Contributor) ((JComboBox) e.getSource()).getSelectedItem()).getContributor().getItemHandle();
                    //if (!((IContributorHandle)editor.getValue()).getItemId().equals(o.getItemId())) {
                    context.lookup(RtcWorkItemAttribute.class).setValue((Contributor) ((JComboBox) e.getSource()).getSelectedItem());
                    //}
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
            /*
            RtcContributorImpl rtcContributorImpl = new RtcContributorImpl((IContributorHandle) getValue());


            if (rtcContributorImpl.equals(contributor)) {
            return;
            }

            boolean found = false;
            for (Contributor c : preffered) {
            if (rtcContributorImpl.getContributor().getItemHandle().getItemId().equals(((RtcContributorImpl) c).getContributor().getItemHandle().getItemId())) {
            list.setSelectedItem(rtcContributorImpl);
            found = true;
            break;
            }
            }


            if (!found) {
            list.addItem(rtcContributorImpl);
            list.setSelectedItem(rtcContributorImpl);
            preffered.add(rtcContributorImpl);
            }

            //list.setSelectedItem(contributor);
             * */
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

//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<IContributorHandle> {
//
//        @Override
//        public String toString(IContributorHandle value) {
//            return new RtcContributorImpl(value).getName();
//        }
//    }
}
