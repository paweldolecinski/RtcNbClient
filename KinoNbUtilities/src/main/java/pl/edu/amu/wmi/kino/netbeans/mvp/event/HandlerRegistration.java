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
package pl.edu.amu.wmi.kino.netbeans.mvp.event;

/**
 * Registration returned from a call to <code>EventBus</code>
 * Use the handler registration to remove handlers when they are no longer
 * needed.
 *
 * @see EventBus#addHandler(pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent.Type, pl.edu.amu.wmi.kino.netbeans.mvp.event.EventHandler) 
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public final class HandlerRegistration {

    private Runnable remove;

    public HandlerRegistration(Runnable remove) {
        this.remove = remove;
    }

    /**
     * Removes the given handler from its manager.
     */
    public void removeHandler(){
        remove.run();
    }
}
