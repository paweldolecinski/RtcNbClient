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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesFolder;

/**
 *
 * @author Patryk Żywica
 */
public class RtcRemoveFavoritesFolderAction extends AbstractAction implements ContextAwareAction {

    private static final long serialVersionUID = 5317567453546657L;
    private Lookup context;

    public RtcRemoveFavoritesFolderAction() {
        //TODO : i18n
        super("Remove favorites folder");
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        RtcRemoveFavoritesFolderAction action = new RtcRemoveFavoritesFolderAction();
        action.context = actionContext;
        return action;

    }

    @Override
    public boolean isEnabled() {
        if (context != null) {
            RtcFavoritesFolder folder = context.lookup(RtcFavoritesFolder.class);
            if (folder != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (context != null) {
            RtcFavoritesFolder folder = context.lookup(RtcFavoritesFolder.class);
            if (folder != null && folder.getParent()!=null) {
                //TODO : i18n
                DialogDescriptor desc = new DialogDescriptor("Delete "+folder.getName()+" ?", "Enter new folder name");

                Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
                dialog.setVisible(true);
                if (desc.getValue().equals(DialogDescriptor.OK_OPTION)) {
                    folder.getParent().remove(folder);
                }
            }
        }
    }
}
