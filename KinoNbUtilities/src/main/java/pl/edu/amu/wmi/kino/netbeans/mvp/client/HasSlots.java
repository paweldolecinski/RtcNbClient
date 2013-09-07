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
 * Interface of objects containing slots in which {@link Presenter} can
 * be inserted.
 * <p />
 * Slots should be defined by enum declared by presenter.
 *
 * @param <T> enum type
 * @author Patryk Żywica
 */
public interface HasSlots<T> {

    /**
     * This method adds some content in a specific slot of the {@link Presenter}.
     * The attached {@link Display} should manage this slot when its
     * {@link Display#addToSlot(T, JComponent)} is called.
     * <p />
    //TODO : bikol : check this doc
     * Contrary to the {@link #setInSlot} method, no
     * {@link com.gwtplatform.mvp.client.proxy.ResetPresentersEvent} is fired,
     * so {@link PresenterWidget#onReset()} is not invoked.
     * <p />
     * For more details on slots, see {@link HasSlots}.
     *
     * @param slot An enum object identifying which slot this content is being
     *          added into.
     * @param content The content, a {@link Presenter}. Passing {@code null}
     *          will not add anything.
     */
    void addToSlot(T slot, Presenter<?> content);

    /**
     * This method clears the content in a specific slot.
    //TODO
     * No {@link com.gwtplatform.mvp.client.proxy.ResetPresentersEvent} is fired.
     * The attached {@link Display} should manage this slot when its
     * {@link Display#setInSlot(T, JComponent)} is called. It should also clear
     * the slot when the {@link Display#setInSlot(T, JComponent)} method is
     * called with {@code null} as a parameter.
     * <p />
     * For more details on slots, see {@link HasSlots}.
     *
     * @param slot An enum object identifying which slot to clear.
     */
    void clearSlot(T slot);

    /**
     * This method removes some content in a specific slot of the
     * {@link Presenter}.
    //TODO :
     * No {@link com.gwtplatform.mvp.client.proxy.ResetPresentersEvent} is fired.
     * The attached {@link Display} should manage this slot when its
     * {@link Display#removeFromSlot(T, JComponent)} is called.
     * <p />
     * For more details on slots, see {@link HasSlots}.
     *
     * @param slot An enum object identifying which slot this content is being
     *          removed from.
     * @param content The content, a {@link Presenter}. Passing {@code null}
     *          will not remove anything.
     */
    void removeFromSlot(T slot, Presenter<?> content);

    /**
     * This method sets some content in a specific slot of the {@link Presenter}.
    //TODO
     * A {@link com.gwtplatform.mvp.client.proxy.ResetPresentersEvent} will be fired
     * after the top-most visible presenter is revealed, resulting in a call to
     * {@link PresenterWidget#onReset()}.
     * <p />
     * For more details on slots, see {@link HasSlots}.
     *
     * @param slot An enum object identifying which slot this content is being
     *          set into. The attached view should know what to do with this slot.
     * @param content The content, a {@link Presenter}. Passing {@code null}
     *          will clear the slot.
     */
    void setInSlot(T slot, Presenter<?> content);

    /**
     * This method sets some content in a specific slot of the {@link Presenter}.
     * The attached {@link Display} should manage this slot when its
     * {@link Display#setInSlot(T, JComponent)} is called. It should also clear the
     * slot when the {@code setInSlot} method is called with {@code null} as a
     * parameter.
     * <p />
     * For more details on slots, see {@link HasSlots}.
     *
     * @param slot An enum object identifying which slot this content is being
     *          set into.
     * @param content The content, a {@link Presenter}. Passing {@code null}
     *          will clear the slot.
    //TODO :
     * @param performReset Pass {@code true} if you want a
     *          {@link com.gwtplatform.mvp.client.proxy.ResetPresentersEvent} to be fired
     *          after the content has been added and this presenter is visible, pass
     *          {@code false} otherwise.
     */
//    void setInSlot(T slot, Presenter<?> content, boolean performReset);
}
