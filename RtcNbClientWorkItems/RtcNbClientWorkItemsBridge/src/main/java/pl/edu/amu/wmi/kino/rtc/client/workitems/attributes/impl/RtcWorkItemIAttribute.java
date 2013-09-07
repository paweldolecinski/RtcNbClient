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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.impl;

import java.awt.Image;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcWorkItemImpl;

import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkingCopyEvent;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 * this class shall be used to enclose IAttribute based attributes
 * @author psychollek
 */
public class RtcWorkItemIAttribute<T> extends RtcWorkItemAttribute<T> {

    private final InstanceContent ic;
    //this ensures, that I'll have ic instantinated on any jvm.
    //and it's pretty neat ;]
    private Lookup lookup = new AbstractLookup((ic = new InstanceContent()));

    @SuppressWarnings("unchecked")
    public RtcWorkItemIAttribute(Class<T> valueType, ToStringProvider<T> stringProvider, IAttribute iAttribute, RtcWorkItem wi) {
        super((T) (wi == null ? null : ((RtcWorkItemImpl) wi).getWorkItem().getValue(iAttribute)), valueType);
        ic.add(stringProvider);
        ic.add(iAttribute);
        // HACK
        ic.add(this);
        // end of HACK


        if (wi != null) {
            ic.add(wi);
        }
    }

    /**
     * this constructor can accept additional content to it's context - then 
     * available in getLookup() of resulting object.
     * @param valueType class of value that will be available through this attribute
     * @param stringProvider stringProvider for the value type
     * @param iAttribute IAttribute that is a base of this attribute
     * @param wi workItemWorkingCopy that is associated with this attribute - can be null
     * @param aditionalContextContent any other context that should be available in lookup
     */
    public RtcWorkItemIAttribute(Class<T> valueType, ToStringProvider<T> stringProvider, IAttribute iAttribute, RtcWorkItem wi, Object... aditionalContextContent) {
        this(valueType, stringProvider, iAttribute, wi);
        for (int i = 0; i < aditionalContextContent.length; i++) {
            ic.add(aditionalContextContent[i]);
        }
    }

    @Override
    public String toString() {
        return lookup.lookup(ToStringProvider.class).toString(value);
    }

    @Override
    public String getAttributeId() {
        return lookup.lookup(IAttribute.class).getIdentifier();
    }

    @Override
    public Image getIcon() {
        IconProvider icon = lookup.lookup(IconProvider.class);
        if (icon != null) {
            return lookup.lookup(IconProvider.class).getIcon(value);
        }
        return super.getIcon();
    }

    @Override
    public String getAttributeDisplayName() {
        return lookup.lookup(IAttribute.class).getDisplayName();
    }

    @Override
    public String getShortDesc() {
        return lookup.lookup(IAttribute.class).getFullTextKind();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void getValueInvoked() {
        if (value == null) {
            RtcWorkItem wi = lookup.lookup(RtcWorkItem.class);
            value = (T) wi.getValue(this);
        }
    }

    @Override
    protected void setValueInvoked(T value) {
        RtcWorkItem wi = lookup.lookup(RtcWorkItem.class);
        wi.setValue(this, value);
    }

    @Override
    public boolean isWritable() {
        //TODO: chceck if this method returns true always when attribute really is not writable
        IAttribute attribute = lookup.lookup(IAttribute.class);
        //attribute.isImmutable(); -this might actually do something simillar - no javadoc though
        return !attribute.isReadOnly();
    }

    @Override
    public Type getAttributeType() {
        String t = lookup.lookup(IAttribute.class).getAttributeType();
        if (AttributeTypes.LARGE_HTML.equals(t)) {
            return Type.LARGE_HTML;
        } else if (AttributeTypes.MEDIUM_HTML.equals(t)) {
            return Type.MEDIUM_HTML;
        } else if (AttributeTypes.LARGE_STRING.equals(t)) {
            return Type.LARGE_STRING;
        } else if (AttributeTypes.MEDIUM_STRING.equals(t)) {
            return Type.MEDIUM_STRING;
        } else {
            return Type.STRING;
        }
    }

    public interface ToStringProvider<T> {

        public String toString(T value);
    }

    public interface IconProvider<T> {

        public Image getIcon(T value);
    }

    public static ToStringProvider getStringProvider() {
        return new ToStringProvider() {

            @Override
            public String toString(Object value) {
                return value.toString();
            }
        };
    }

    public class WorkingCopyListener implements IWorkingCopyListener {

        @Override
        public void workingCopyEvent(WorkingCopyEvent wce) {
            if (wce.hasType(WorkingCopyEvent.SAVED) || wce.hasType(WorkingCopyEvent.SAVE_CANCELED) || wce.hasType(WorkingCopyEvent.REVERTED)) {
//                IWorkItem wi = lookup.lookup(IWorkItem.class);
//                IAttribute attribute = lookup.lookup(IAttribute.class);
//                value = (T) wi.getValue(attribute);
                getValueInvoked();
            }
        }
    }
}
