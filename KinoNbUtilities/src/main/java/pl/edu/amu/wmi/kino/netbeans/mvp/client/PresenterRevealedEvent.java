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

import pl.edu.amu.wmi.kino.netbeans.mvp.event.EventBus;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.MvpEvent;

/**
 * Presenters can send this event to the {@link EventBus} to notify other
 * interested parties when the presenter has been 'revealed' on the screen. This
 * is particularly useful for situations where a presenter contains other
 * presenters and wants needs to reveal itself when a child presenter is
 * revealed.
 *
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public class PresenterRevealedEvent extends MvpEvent<PresenterRevealedHandler> {

    private static final MvpEvent.Type<PresenterRevealedHandler> TYPE = new MvpEvent.Type<PresenterRevealedHandler>();

    public static MvpEvent.Type<PresenterRevealedHandler> getType() {
        return TYPE;
    }

    /**
     * Fires a {@link PresenterRevealedEvent} into the {@link EventBus}, specifying that it
     * was the originator.
     *
     * @param eventBus  The event bus.
     * @param presenter The presenter.
     */
    public static void fire(EventBus eventBus, Presenter presenter) {
        fire(eventBus, presenter, true);
    }

    /**
     * Fires the event into the provided {@link EventBus}.
     *
     * @param eventBus   The event bus.
     * @param presenter  The presenter.
     * @param originator If <code>true</code>, this presenter was the originator for the request.
     */
    public static void fire(EventBus eventBus, Presenter presenter, boolean originator) {
        eventBus.fireEvent(new PresenterRevealedEvent(presenter, originator));
    }
    private final Presenter presenter;
    private boolean originator;

    /**
     * Constructs a new revelation event, specifying that it is the originator.
     *
     * @param presenter The presenter.
     */
    public PresenterRevealedEvent(Presenter presenter) {
        this(presenter, true);
    }

    /**
     * Constructs a new revelation event, with the specified 'originator'
     * status.
     *
     * @param presenter  The presenter that has been revealed.
     * @param originator If <code>true</code>, the presenter is the originator of
     *                   the revelation chain.
     */
    public PresenterRevealedEvent(Presenter presenter, boolean originator) {
        this.presenter = presenter;
        this.originator = originator;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    /**
     * Returns <code>true</code> if the presenter in this event originated the
     * revelation, or <code>false</code> if it is a consequence of being
     * revealed by a child presenter.
     *
     * @return <code>true</code> if the event was the originator.
     */
    public boolean isOriginator() {
        return originator;
    }

    @Override
    protected void dispatch(PresenterRevealedHandler handler) {
        handler.onPresenterRevealed(this);
    }

    @Override
    public MvpEvent.Type<PresenterRevealedHandler> getAssociatedType() {
        return getType();
    }
}
