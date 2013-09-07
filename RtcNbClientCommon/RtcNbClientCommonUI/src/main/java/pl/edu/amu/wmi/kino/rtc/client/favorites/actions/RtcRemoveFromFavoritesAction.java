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
package pl.edu.amu.wmi.kino.rtc.client.favorites.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesEntry;

/**
 *
 * @author psychollek
 */
public class RtcRemoveFromFavoritesAction extends AbstractAction implements ContextAwareAction, HelpCtx.Provider {

	private static final long serialVersionUID = 4060084641216664056L;
	private RtcFavoritesEntry entry = null;

    public RtcRemoveFromFavoritesAction() {
        super(
                NbBundle.getMessage(
                RtcRemoveFromFavoritesAction.class,
                "RtcRemoveFromFavoritesAction.displayName"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (entry != null) {
            entry.getParent().remove(entry);
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        RtcRemoveFromFavoritesAction action = new RtcRemoveFromFavoritesAction();
        action.entry = actionContext.lookup(RtcFavoritesEntry.class);
        return action;
    }

    @Override
    public boolean isEnabled() {
        return (entry != null);
    }

    /**
     * @return the default help context
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
