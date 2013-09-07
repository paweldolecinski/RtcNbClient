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
package pl.edu.amu.wmi.kino.netbeans.multiview;

import java.awt.Image;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Patryk Żywica
 */
public abstract class KinoMultiViewDescription {

    private final Set<KinoMultiViewDescriptionListener> listeners = Collections.synchronizedSet(new HashSet<KinoMultiViewDescriptionListener>());

    /**
     * Creates and returns associated multi view element. it is called just once
     * during the lifecycle of the multiview component.
     *
     * @return the multiview element associated with this description.
     */
    public abstract KinoMultiViewElement createElement();

    public abstract String getDisplayName();

    public Image getIcon() {
        return ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/editor/resources/emptyIcon.gif");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Use this method to register new listener to this object.
     *
     * If given listener is actually registered in this object does nothing.
     *
     * @param listener to be added to listen to changes in this object. Not null.
     */
    public final void addListener(KinoMultiViewDescriptionListener listener) {
        listeners.add(listener);
    }

    /**
     * Use this method to remove listener from this object.
     *
     * If given listener isn't registered to this object does nothing.
     *
     * @param listener to be unregistered from this object. Not null.
     */
    public final void removeListener(KinoMultiViewDescriptionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires given event to all registered listeners.
     * @param event to be fired.
     */
    protected final void fireEvent(KinoMultiViewDescriptionEvent event) {
        synchronized (listeners) {
            for (KinoMultiViewDescriptionListener l : listeners) {
                RequestProcessor.getDefault().post(new EventRunner(event, l));
            }
        }
    }

    /**
     * Listener interface for <code>KinoMultiViewDescription</code>.
     * If you want to listen for changes in this <code>KinoMultiViewDescription</code>
     * you have to implement this interface and pass this implementation to
     * <code>KinoMultiViewDescription.addListener()</code> method.
     *
     * @autor Patryk Żywica
     */
    public interface KinoMultiViewDescriptionListener {

        public void eventFired(KinoMultiViewDescriptionEvent event);
    }

    /**
     * This enumeration defines all possible events for <code>RtcPlanMultiViewDescriptionFactory</code>.
     *
     * @author Patryk Żywica
     */
    public enum KinoMultiViewDescriptionEvent {

        NAME_CHANGED,
        ELEMENT_CHANGED,
        ICON_CHANGED;
    }

    private static class EventRunner implements Runnable {

        private KinoMultiViewDescriptionEvent event;
        private KinoMultiViewDescriptionListener listener;

        public EventRunner(KinoMultiViewDescriptionEvent event, KinoMultiViewDescriptionListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.eventFired(event);
        }
    }
}
