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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.common.advisor;

import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Patryk Żywica
 */
public class Advisor {

    public static void displayAdviceAsTooltip(Advice advice) {
        NotificationDisplayer.getDefault().notify(
                advice.getName(),
                advice.getIcon() == null ? new ImageIcon() : ImageUtilities.image2Icon(advice.getIcon()),
                advice.getMessage(),
                advice.getAction(),
                mapPriority(advice.getPriority()));
    }

    private static NotificationDisplayer.Priority mapPriority(AdvicePriority ap) {
        switch (ap) {
            case HIGH:
                return NotificationDisplayer.Priority.HIGH;
            case NORMAL:
                return NotificationDisplayer.Priority.NORMAL;
            case LOW:
                return NotificationDisplayer.Priority.LOW;
            default:
                return NotificationDisplayer.Priority.SILENT;
        }
    }

    public static interface Advice {

        String getName();

        String getMessage();

        ActionListener getAction();

        AdvicePriority getPriority();

        Image getIcon();
    }

    public enum AdvicePriority {

        SILENT,
        LOW,
        NORMAL,
        HIGH,}

    public static class ExceptionInformation implements Advice {

        private String msg, name;

        public ExceptionInformation(Exception ex) {
            msg=ex.getLocalizedMessage();
            //TODO : i18n
            name="Exception thrown";
        }

        
        public String getName() {
            return name;
        }

        public String getMessage() {
            return msg;
        }

        public ActionListener getAction() {
            return null;
        }

        public AdvicePriority getPriority() {
            return AdvicePriority.NORMAL;
        }

        public Image getIcon() {
            return null;
        }
    }
}
