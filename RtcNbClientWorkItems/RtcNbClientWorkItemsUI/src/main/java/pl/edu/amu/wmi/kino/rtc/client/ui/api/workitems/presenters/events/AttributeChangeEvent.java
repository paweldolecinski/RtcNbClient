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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events;

import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventBus;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent.Type;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events.AttributeChangeEvent.AttributeChangeHandler;

/**
 *
 * @author Patryk Żywica
 */
public class AttributeChangeEvent<T> extends MvpEvent<AttributeChangeHandler> {

    private static final Type<AttributeChangeHandler> TYPE = new Type<AttributeChangeHandler>();
    
    private RtcWorkItemAttribute<T> attribute;
    private T oldValue,newValue;

    public AttributeChangeEvent(RtcWorkItemAttribute<T> attribute, T oldValue, T newValue) {
        this.attribute = attribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public Type<AttributeChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AttributeChangeHandler handler) {
        handler.valueChanged(attribute, oldValue, newValue);
    }
    
    public static HandlerRegistration register(AttributeChangeHandler h,Object source){
        return Lookup.getDefault().lookup(EventBus.class).addHandlerToSource(TYPE, source, h);
    }
    
    public interface AttributeChangeHandler extends EventHandler{
        
        <H> void valueChanged(RtcWorkItemAttribute<H> attribute, H oldValue,H newValue);
        
    }
}
