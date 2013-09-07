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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcIllegalPlanAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.checkers.RtcDurationValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.editors.RtcDurationEditor;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.editors.RtcEnumerationEditor;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.editors.RtcStringEditor;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.editors.RtcTypeEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcDuration;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author Michal Wojciechowski
 */
@ServiceProvider(service = RtcAttributePropertySupportFactory.class)
public class RtcAttributePropertySupportFactory {

    public PropertySupport createPropertySupport(RtcPlanItem planItem, RtcPlanItemAttribute attribute) {

        if (attribute == null) {
            return null;
        }

        PropertySupport p = null;

        if (attribute.getValueType().equals(RtcLiteral.class)) {
            p = new PropertySupportRtcLiteralImpl(planItem, attribute);
        }
        if (attribute.getValueType().equals(String.class)) {
            p = new PropertySupportStringImpl(planItem, attribute);
        }
        if (attribute.getValueType().equals(RtcWorkItemType.class)) {
            p = new PropertySupportRtcTypeImpl(planItem, attribute);
        }
        if (attribute.getValueType().equals(RtcDuration.class)) {
            p = new PropertySupportRtcDurationImpl(planItem, attribute);
        }

        return p;
    }

    private class PropertySupportRtcTypeImpl
            extends PropertySupport<RtcWorkItemType>
            implements PropertyChangeListener {

        private RtcPlanItem planItem;
        private RtcPlanItemAttribute attribute;
        private PropertyEditor editor;
        private RtcWorkItemType value;

        public PropertySupportRtcTypeImpl(RtcPlanItem planItem, RtcPlanItemAttribute attribute) {
            super(attribute.getAttributeIdentifier(), attribute.getValueType(), attribute.getAttributeName(), attribute.getAttributeName(), true, true);
            this.planItem = planItem;
            this.attribute = attribute;
            this.value = (RtcWorkItemType) planItem.getPlanAttributeValue(attribute);
        }

        @Override
        public RtcWorkItemType getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(RtcWorkItemType val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = val;
            try {
                planItem.setPlanAttributeValue(attribute, val);
            } catch (RtcIllegalPlanAttributeValue ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                editor = new RtcTypeEditor(attribute.getLookup());
                editor.addPropertyChangeListener(this);
            }
            return editor;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() == null && !value.equals(editor.getValue())) {
                try {
                    setValue((RtcWorkItemType) editor.getValue());
                } catch (IllegalAccessException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (IllegalArgumentException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (InvocationTargetException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        }
    }

    private class PropertySupportRtcDurationImpl
            extends PropertySupport<RtcDuration>
            implements PropertyChangeListener {

        private RtcPlanItem planItem;
        private RtcPlanItemAttribute attribute;
        private PropertyEditor editor;
        private RtcDuration value;
        private RtcDurationValueChecker valueChecker;

        public PropertySupportRtcDurationImpl(RtcPlanItem planItem, RtcPlanItemAttribute attribute) {
            super(attribute.getAttributeIdentifier(), attribute.getValueType(), attribute.getAttributeName(), attribute.getAttributeName(), true, true);
            this.planItem = planItem;
            this.attribute = attribute;
            this.value = (RtcDuration) planItem.getPlanAttributeValue(attribute);
            this.valueChecker = attribute.getLookup().lookup(RtcDurationValueChecker.class);
        }

        @Override
        public RtcDuration getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(RtcDuration val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = val;
            try {
                planItem.setPlanAttributeValue(attribute, val);
            } catch (RtcIllegalPlanAttributeValue ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                editor = new RtcDurationEditor(attribute.getLookup());
                editor.addPropertyChangeListener(this);
            }
            return editor;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() == null && !value.equals(editor.getValue())) {
                try {
                    setValue((RtcDuration) editor.getValue());
                } catch (IllegalAccessException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (IllegalArgumentException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (InvocationTargetException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        }
    }

    private class PropertySupportRtcLiteralImpl 
            extends PropertySupport<RtcLiteral>
            implements PropertyChangeListener {

        private RtcPlanItem planItem;
        private RtcPlanItemAttribute attribute;
        private PropertyEditor editor;
        private RtcLiteral value;

        public PropertySupportRtcLiteralImpl(RtcPlanItem planItem, RtcPlanItemAttribute attribute) {
            super(attribute.getAttributeIdentifier(), attribute.getValueType(), attribute.getAttributeName(), attribute.getAttributeName(), true, true);
            this.planItem = planItem;
            this.attribute = attribute;
            this.value = (RtcLiteral) planItem.getPlanAttributeValue(attribute);
        }

        @Override
        public RtcLiteral getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(RtcLiteral val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = val;
            try {
                planItem.setPlanAttributeValue(attribute, val);
            } catch (RtcIllegalPlanAttributeValue ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                editor = new RtcEnumerationEditor(attribute.getLookup());
                editor.addPropertyChangeListener(this);
            }
            return editor;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() == null && !value.equals(editor.getValue())) {
                try {
                    setValue((RtcLiteral) editor.getValue());
                } catch (IllegalAccessException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (IllegalArgumentException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (InvocationTargetException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }
        }
    }

    private class PropertySupportStringImpl extends PropertySupport<String> {

        private RtcPlanItem planItem;
        private RtcPlanItemAttribute attribute;
        private PropertyEditor editor;
        private String value;

        public PropertySupportStringImpl(RtcPlanItem planItem, RtcPlanItemAttribute attribute) {
            super(attribute.getAttributeIdentifier(), attribute.getValueType(), attribute.getAttributeName(), attribute.getAttributeName(), true, true);
            this.planItem = planItem;
            this.attribute = attribute;
            this.value = (String) planItem.getPlanAttributeValue(attribute);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            value = val;
            try {
                planItem.setPlanAttributeValue(attribute, val);
            } catch (RtcIllegalPlanAttributeValue ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcAttributePropertySupportFactory.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                editor = new RtcStringEditor();
            }
            return editor;
        }
    }
}
