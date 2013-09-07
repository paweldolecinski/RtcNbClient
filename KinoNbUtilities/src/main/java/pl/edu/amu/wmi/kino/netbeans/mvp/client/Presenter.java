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
package pl.edu.amu.wmi.kino.netbeans.mvp.client;

/**
 *
 * @param <D> 
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public interface Presenter<D extends Display> {
    /**
     * Called when the presenter is initialized. This is called before any other
     * methods. Any event handlers and other setup should be done here rather
     * than in the constructor.
     */
    void bind();


    /**
     * Called after the presenter and display have been finished with for the
     * moment.
     */
    void unbind();


    /**
     * Returns the {@link Display} for the current presenter.
     *
     * @return The display.
     */
    Display getDisplay();


    /**
     * Requests the presenter to refresh the contents of the display. This does
     * <b>not</b> force the display to be revealed on screen.
     */
    void refreshDisplay();


    /**
     * Requests the presenter to reveal the display on screen. It should
     * automatically ask any parent displays/presenters to reveal themselves
     * also. It should <b>not</b> trigger a refresh.
     */
    void revealDisplay();
}
