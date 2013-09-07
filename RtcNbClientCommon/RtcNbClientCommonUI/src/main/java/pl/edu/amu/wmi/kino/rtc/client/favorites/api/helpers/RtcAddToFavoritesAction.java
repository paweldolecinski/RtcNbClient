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
package pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcFavoritesService;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;

/**
 * Helper class - action that invokes the <code>RtcFavoritesService</code> - you probably
 * won't need to create it in other manner.
 * <p>
 * If you add this to node - the favorites
 * service shall try to remove this action from it when displayed in favorites,
 * but beter option is to use <code>RtcAddToFavoritesFilterNode</code> and not use it in provider.
 * </p>
 * @author psychollek
 */
public class RtcAddToFavoritesAction extends AbstractAction implements ContextAwareAction{

	private static final long serialVersionUID = 1793101782816194911L;
	private Lookup context = null;


    /**
     * this constructor creates instance of action, which will be disabled by default
     * use createContextAwareInstance(context) to get actual action.
     */
    public RtcAddToFavoritesAction(){
        super(NbBundle.getMessage(RtcAddToFavoritesAction.class,
                                  "RtcAddToFavoritesAction.displayName"),
              ImageUtilities.loadImageIcon(
                            "pl/edu/amu/wmi/kino/rtc/client/favorites/api/helpers/add_to_favorites.gif"
                            , true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RtcResourceProvider provider = context.lookup(RtcResourceProvider.class);
        if(context != null){
            Lookup.getDefault().lookup(RtcFavoritesService.class)
                .addToFavorites(provider,
                                provider.getResourceId(context),
                                provider.getActiveRepository(context));
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        RtcAddToFavoritesAction action = new RtcAddToFavoritesAction();
        action.context = actionContext;
        return action;
    }

    @Override
    public boolean isEnabled() {
        RtcResourceProvider provider = context.lookup(RtcResourceProvider.class);
        return provider == null ? false : provider.enableForContext(context);
    }

}
