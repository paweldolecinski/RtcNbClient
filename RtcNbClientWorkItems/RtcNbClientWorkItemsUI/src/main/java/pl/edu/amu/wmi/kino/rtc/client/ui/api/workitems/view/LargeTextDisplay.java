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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;

/**
 * Display for large (many lines) text inputs.
 * 
 * @author Patryk Żywica
 */
public interface LargeTextDisplay extends Display {

    public static final InputHandler.Input<String> TEXT_INPUT = new InputHandler.Input<String>();

     <T> HandlerRegistration addInputHandler(InputHandler.Input<T> input, InputHandler<T> h);

    void setText(String text);

    void setInfoStatus(Status status, String text);

    void setLabel(String label);

    /**
     * Sets unique id, that describes what this display is displaying.
     * 
     * Used e.g. in properties as property name to distinguish between properties in set.
     * 
     * @param id 
     */
    void setId(String id);

    public enum Status {

        OK,
        INFO,
        WARNING,
        ERROR,
    }
}
