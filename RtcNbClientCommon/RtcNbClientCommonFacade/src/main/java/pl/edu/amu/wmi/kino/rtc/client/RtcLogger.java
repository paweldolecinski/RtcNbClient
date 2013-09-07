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
package pl.edu.amu.wmi.kino.rtc.client;

import java.util.logging.Logger;

/**
 *
 * @author Patryk Żywica
 */
public class RtcLogger {

    private static final Logger logger = Logger.getLogger("RtcLogger");

    @Deprecated
    public static Logger getLogger() {
        Logger log = logger;
        return logger;
    }

    public static Logger getLogger(Class<?> clazz) {
        //FIXME : add proper i18n bundle
//        return Logger.getLogger(clazz.getName(), findName(clazz));
         return Logger.getLogger(clazz.getName());
    }

    public static Logger getLogger(String pack) {
        return Logger.getLogger(pack, pack + "Bundle");
    }

    /** Finds package name for given class */
    private static String findName(Class<?> clazz) {
        String pref = clazz.getName();
        int last = pref.lastIndexOf('.');

        if (last >= 0) {
            pref = pref.substring(0, last + 1);

            return pref + "Bundle"; // NOI18N
        } else {
            // base package, search for bundle
            return "Bundle"; // NOI18N
        }
    }

    private RtcLogger() {
    }
}
