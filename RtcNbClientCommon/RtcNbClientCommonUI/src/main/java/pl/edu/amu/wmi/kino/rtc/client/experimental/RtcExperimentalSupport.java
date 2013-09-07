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
package pl.edu.amu.wmi.kino.rtc.client.experimental;

import java.awt.Dialog;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcExperimentalSupport.class)
public class RtcExperimentalSupport {

    private final static int ASK_USER = 0;
    private final static int ACCEPT_ALWAYS = 10;
    private final static int ACCEPT_RESTART = 20;
    private final static int DECLINE_ALWAYS = 30;
    private final static int DECLINE_RESTART = 40;
    private Preferences prefernces = NbPreferences.forModule(RtcExperimentalSupport.class);
    private Map<String, Integer> restart = new HashMap<String, Integer>(5);
    private Map<String, String> toName = new HashMap<String, String>(5);
    private Map<String, String> toDescription = new HashMap<String, String>(5);

    public synchronized boolean isEnabled(String experimentalFutureDescriptor) {
        if(!toName.containsKey(experimentalFutureDescriptor)){
            RtcLogger.getLogger().log(Level.WARNING,"Experimental featureID "+experimentalFutureDescriptor+ "not registered in Experimental Support");
            return true;
        }
        int b = prefernces.getInt(experimentalFutureDescriptor, ASK_USER);
        if (restart.containsKey(experimentalFutureDescriptor)) {
            b = restart.get(experimentalFutureDescriptor);
        }
        if (b == ASK_USER) {
            //we have to ask user in special dialog
            RtcExperimentalPanel panel = new RtcExperimentalPanel(
                    toName.get(experimentalFutureDescriptor),
                    toDescription.get(experimentalFutureDescriptor));
            DialogDescriptor desc = new DialogDescriptor(
                    panel,
                    NbBundle.getMessage(RtcExperimentalSupport.class, "RtcExperimentalSupport.dialog.title"),
                    true,
                    DialogDescriptor.YES_NO_OPTION,
                    DialogDescriptor.NO_OPTION,
                    null);
            Dialog dialog = DialogDisplayer.getDefault().createDialog(desc);
            dialog.setVisible(true);
            boolean toReturn;
            if (desc.getValue().equals(DialogDescriptor.YES_OPTION)) {
                toReturn = true;
                switch (panel.getIndex()) {
                    case 1:
                        restart.put(experimentalFutureDescriptor, ACCEPT_RESTART);
                        break;
                    case 2:
                        prefernces.putInt(experimentalFutureDescriptor, ACCEPT_ALWAYS);
                        break;
                    default:

                }
            } else {
                toReturn = false;
                switch (panel.getIndex()) {
                    case 1:
                        restart.put(experimentalFutureDescriptor, DECLINE_RESTART);
                        break;
                    case 2:
                        prefernces.putInt(experimentalFutureDescriptor, DECLINE_ALWAYS);
                        break;
                    default:

                }
            }

            return toReturn;
        } else {
            if (restart.containsKey(experimentalFutureDescriptor)) {
                int tmp = restart.get(experimentalFutureDescriptor);
                return tmp == ACCEPT_RESTART;
            } else {
                //in the end we have to check for always options
                return b == ACCEPT_ALWAYS;
            }
        }
    }

    public void addExperimentalFuture(String futureID, String futureName, String futureDescription) {
        toName.put(futureID, futureName != null ? futureName : futureID);
        toDescription.put(futureID, futureDescription != null ? futureDescription : "");
    }

    public void removeExperimentalFuture(String futureID) {
        toName.remove(futureID);
        toDescription.remove(futureID);
    }
}
