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

import java.util.Collections;
import java.util.List;

/**
 * This class provides support for widgets that contain other widgets. It will
 * listen for {@link PresenterRevealedEvent}s and reveal itself if the source
 * was a direct child of this presenter.
 *
 * @since 0.0.3.0
 * @param <T>
 * @author Patryk Żywica
 */
public abstract class SwingContainerPresenter<T extends SwingContainerDisplay> extends SwingPresenter<T> {

    private final List<SwingPresenter<?>> presenters;
    private SwingPresenter<?> currentPresenter;

    /**
     *
     * @param display
     * @param eventBus
     * @param presenters
     */
    public SwingContainerPresenter(T display, SwingPresenter<?>... presenters) {
        super(display);
        this.presenters = new java.util.ArrayList<SwingPresenter<?>>(5);
        Collections.addAll(this.presenters, presenters);
    }

    /**
     * Adds the presenter, if the current presenter is unbound.
     *
     * @param presenter
     *            The presenter to add.
     * @return If added, returns <code>true</code>.
     */
    protected boolean addPresenter(SwingPresenter<?> presenter) {
        if (!isBound()) {
            presenters.add(presenter);
            return true;
        }
        return false;
    }

    @Override
    protected void onBind() {


        registerHandler(getEventBus().addHandler(PresenterRevealedEvent.getType(), new PresenterRevealedHandler() {

            @SuppressWarnings("element-type-mismatch")
            @Override
            public void onPresenterRevealed(PresenterRevealedEvent event) {
                if (presenters.contains(event.getPresenter())) {
                    showPresenter((SwingPresenter<?>) event.getPresenter());
                    revealDisplay();
                }
            }
        }));
    }

    @Override
    protected void onUnbind() {
        currentPresenter = null;
    }

    /**
     *
     * @return
     */
    protected SwingPresenter<?> getCurrentPresenter() {
        return currentPresenter;
    }

    /**
     *
     * @param presenter
     * @return
     */
    protected int indexOf(SwingPresenter<?> presenter) {
        return presenters.indexOf(presenter);
    }

    /**
     *
     * @param presenter
     */
    protected void showPresenter(SwingPresenter<?> presenter) {
        if (indexOf(presenter) >= 0) {
            currentPresenter = presenter;
            getDisplay().showComponent(presenter.getDisplay().asComponent());
        }
    }
}
