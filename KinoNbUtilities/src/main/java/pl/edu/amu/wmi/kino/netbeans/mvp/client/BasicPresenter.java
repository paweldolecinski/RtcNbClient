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

import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventBus;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;

/**
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public abstract class BasicPresenter<D extends Display> implements Presenter<D> {

    /**
     * The display for the presenter.
     */
    private final D display;
    /**
     * The {@link EventBus} for the application.
     */
    private final EventBus eventBus;
    private Collection<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>(5);
    private boolean bound = false;

    public BasicPresenter(D display) {
        this.display = display;
        this.eventBus = Lookup.getDefault().lookup(EventBus.class);
    }

    /**
     * Getter for EventBus.
     * @return event bus
     */
    protected EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public synchronized void bind() {
        if (!bound) {
            onBind();
            bound = true;
        }
    }

    /**
     * Any {@link HandlerRegistration}s added will be removed when
     * {@link #unbind()} is called. This provides a handy way to track event
     * handler registrations when binding and unbinding.
     *
     * @param handlerRegistration The registration.
     */
    protected void registerHandler(HandlerRegistration handlerRegistration) {
        if (null == handlerRegistration) {
            throw new IllegalArgumentException("null 'handlerRegistration' argument.");
        }
        handlerRegistrations.add(handlerRegistration);
    }

    protected void unRegisterHandler(HandlerRegistration handlerRegistration) {
        if (null == handlerRegistration) {
            throw new IllegalArgumentException("null 'handlerRegistration' argument.");
        }
        handlerRegistrations.remove(handlerRegistration);
        handlerRegistration.removeHandler();
    }

    /**
     * This method is called when unbinding the presenter. Any handler
     * registrations recorded with {@link #registerHandler(HandlerRegistration)}
     * will have already been removed at this point.
     */
    @Override
    public synchronized void unbind() {
        if (bound) {
            for (HandlerRegistration reg : handlerRegistrations) {
                reg.removeHandler();
            }
            handlerRegistrations.clear();

            onUnbind();
            bound = false;
        }
    }

    /**
     * This method is called when binding the presenter. Any additional bindings
     * should be done here.
     */
    protected abstract void onBind();

    /**
     * This method is called when unbinding the presenter. Any handler
     * registrations recorded with {@link #registerHandler(HandlerRegistration)}
     * will have already been removed at this point.
     */
    protected abstract void onUnbind();

    /**
     * Checks if the presenter has been bound. Will be set to false after a call
     * to {@link #unbind()}.
     *
     * @return The current bound status.
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * Returns the display for the presenter.
     *
     * @return The display.
     */
    @Override
    public D getDisplay() {
        return display;
    }

    /**
     * Triggers a {@link PresenterRevealedEvent}. Subclasses should override
     * this method and call <code>super.revealDisplay()</code> if they need to
     * perform extra operations when being revealed.
     */
    @Override
    public void revealDisplay() {
        eventBus.fireEvent(new PresenterRevealedEvent(this));
    }
}
