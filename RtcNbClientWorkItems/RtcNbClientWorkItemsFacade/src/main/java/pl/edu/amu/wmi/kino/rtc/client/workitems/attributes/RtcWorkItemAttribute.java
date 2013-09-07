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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes;

import java.awt.Image;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.openide.nodes.PropertySupport;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributeValueChecker;
import pl.edu.amu.wmi.kino.rtc.client.workitems.editors.ContextPropertyEditorFactory;

/**
 * This is a base class for all worItem attributes.
 * It is container for attribute, meaning that , it's methods shall be connected
 * through listeners to edited workitem.
 * @author psychollek
 */
@Deprecated
public abstract class RtcWorkItemAttribute<T> extends RtcAttribute{

    protected T value;
    private final Class<T> valueType;
    private transient List<RtcWorkItemAttributeListener> listeners = new ArrayList<RtcWorkItemAttributeListener>();

    /**
     * This returns value of the attribute.
     * @return value of the attribute - might be null - if attribute is empty.
     */
    public final T getValue() {
        getValueInvoked();
        return value;
    }

    public final void setValue(T value) {
        T oldValue = this.value;
        this.value = value;

        if ((oldValue == null && this.value != null) || (oldValue != null && !oldValue.equals(this.value))) {
            setValueInvoked(value);
            fireEvent(RtcWorkItemAttributeEvents.VALUE_CHANGED);
        }
    }

    public RtcWorkItemAttribute(Class<T> valueType) {
        this.value = null;
        this.valueType = valueType;
    }

    public RtcWorkItemAttribute(T value, Class<T> valueType) {
        this(valueType);
        this.value = value;
    }

    /**
     * this method has to be implemented by every subclass of this class and will be used
     * to provide fallback description of the value of workItem.
     * @return String representation of the value.
     */
    @Override
    public abstract String toString();

    /**
     * this method return icon representing the value - one can override this method.
     * this method should provide rather small image - it will be used in tables in gui.
     * default implementation returns null.
     * @return Image representing the value - may be null.
     */
    public Image getIcon() {
        return null;
    }

    /**
     * this method shall provide display name of the attribute type, not for the
     * value itself (for example : owner instead John Doe).
     * This one shall be treated as kind static method - and the only reason it
     * is not static, is that static methods cannot be abstrack, and thus necesarly
     * hidden by extending classes.
     * This name will be used in labels and such - thus shall be internationalizable.
     * @return display name of the attriute type.
     */
    public abstract String getAttributeDisplayName();

    /**
     * This method shall provide short description of the attribute type, not for the
     * value itself (for example : "the owner of the workitem" instead "John Doe").
     * This one shall be treated as kind static method - and the only reason it
     * is not static, is that static methods cannot be abstrack, and thus necesarly
     * hidden by extending classes.
     * This description will be used in popup help and thus shall be internationalizable.
     * @return short description of the attriute type.
     */
    public abstract String getShortDesc();

    /**
     * this method returns the id of the attribute, by which it can be called - it have to be unique
     * in a workItemContext. default implementation uses getAttributeDisplayName() which should be changed
     * where possible and later on this method should be made abstract, so any implementation
     * will have to provide own method of providing an id.
     * @return Programatic name of the attribute.
     */
    public String getAttributeId() {
        return getAttributeDisplayName();
    }

    public abstract Type getAttributeType();


    /**
     * callback method invoked before returning the value - do not call "getValue()"
     * here !
     */
    protected abstract void getValueInvoked();

    /**
     * callback method invoked after setting the value - do not call "setValue()"
     * here !
     * @param value
     */
    protected abstract void setValueInvoked(T value);

    /**
     * this method shall be overwriten if method of getting the propertyeditor
     * for this attribut is difrent then standard one - which is invoked if this
     * method returns null.
     * @return
     */
    protected PropertyEditor getCustomPropertyEditor() {
        PropertyEditor editor = null;
        Collection<? extends ContextPropertyEditorFactory> editorFactories = Lookup.getDefault().lookupAll(ContextPropertyEditorFactory.class);
        for (Iterator<? extends ContextPropertyEditorFactory> it = editorFactories.iterator(); it.hasNext();) {
            ContextPropertyEditorFactory factory = it.next();
            editor = factory.createPropertyEditorFromContext(getLookup());
            if (editor != null) {
                return editor;
            }
        }
        return editor;
    }

    public final PropertySupport<T> getPropertySupport() {
        return new PropertySupportImpl(valueType);
    }

    public static enum Type {

        LARGE_HTML,
        MEDIUM_HTML,
        LARGE_STRING,
        MEDIUM_STRING,
        PROJECT_AREA,
        TEAM_AREA,
        WORK_ITEM,
        TIMESTAMP,
        DELIVERABLE,
        CONTRIBUTOR,
        SUBSCRIPTIONS,
        STRING,
        ENUMERATION,
        CATEGORY,
        ITERATION,
        TYPE,
        TAGS,
        DURATION,
        LINKS,
        HISTORY,
        COMMENTS,
        APPROVALS,
        ATTACHMENTS,
        FLOW_STATE,
        FLOW_RESOLUTION,
        TEAM_AND_PROJECT;
    }

    private class PropertySupportImpl extends PropertySupport<T> {

        PropertyEditor editor = null;

        public PropertySupportImpl(Class<T> type) {
            super(getAttributeDisplayName(), type, getAttributeDisplayName(), getShortDesc(), true, true);
        }

        @Override
        public T getValue() throws IllegalAccessException, InvocationTargetException {
            try {
                return RtcWorkItemAttribute.this.getValue();
            } catch (RuntimeException ex) {
                throw new InvocationTargetException(ex);
            }
        }

        @Override
        public void setValue(T val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            try {
                RtcWorkItemAttributeValueChecker<T> vc = getLookup().lookup(RtcWorkItemAttributeValueChecker.class);
                if (vc != null) {
                    if (!vc.isvalueProper(value)) {
                        throw new IllegalArgumentException();
                    }
                }
                RtcWorkItemAttribute.this.setValue(val);
            } catch (RuntimeException ex) {
                throw new InvocationTargetException(ex);
            }
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                editor = getCustomPropertyEditor() != null ? getCustomPropertyEditor() : super.getPropertyEditor();
            }
            return editor;
        }

        @Override
        public boolean canWrite() {
            return isWritable();
        }

        @Override
        public boolean canRead() {
            return isReadable();
        }

        @Override
        public boolean isExpert() {
            return RtcWorkItemAttribute.this.isExpert();
        }

        @Override
        public boolean isHidden() {
            return RtcWorkItemAttribute.this.isHidden();
        }

        @Override
        public boolean isPreferred() {
            return RtcWorkItemAttribute.this.isPreferred();
        }
    }

    /**
     * overwrite this method if you want to controll writebility of the property
     * default - true
     * @return weather the attribute value can be changed
     */
    public boolean isWritable() {
        return true;
    }

    /**
     * overwrite this method if you want to controll readibility of the property
     * default - true
     * @return weather the attribute value can be read
     */
    public boolean isReadable() {
        return true;
    }

    /**
     *
     * @return true if this is expert attribute
     */
    public boolean isExpert() {
        return false;
    }

    /**
     *
     * @return true if this attribute should not be seen by humans
     */
    public boolean isHidden() {
        return false;
    }

    /**
     *
     * @return true if this attribute needs more attention
     */
    public boolean isPreferred() {
        return false;
    }

    protected void fireEvent(RtcWorkItemAttributeEvents eventType) {
        for (Iterator<RtcWorkItemAttributeListener> it = listeners.iterator(); it.hasNext();) {
            it.next().eventFired(this, eventType);
        }
    }

    public synchronized void addListener(RtcWorkItemAttributeListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(RtcWorkItemAttributeListener listener) {
        listeners.remove(listener);
    }

    public static interface RtcWorkItemAttributeListener {

        public void eventFired(RtcWorkItemAttribute source, RtcWorkItemAttributeEvents eventType);
    }

    public static enum RtcWorkItemAttributeEvents {

        VALUE_CHANGED,
        WORKFLOW_ACTION_CHANGED
    }
}
