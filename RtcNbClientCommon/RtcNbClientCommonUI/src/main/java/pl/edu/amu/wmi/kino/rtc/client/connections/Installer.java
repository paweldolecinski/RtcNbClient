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
package pl.edu.amu.wmi.kino.rtc.client.connections;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesServiceImpl;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void uninstalled() {
//        Handler[] handlers = RtcLogger.getLogger().getHandlers();
//        for (int i = 0; i < handlers.length; i++) {
//            Handler handler = handlers[i];
//            if (handler instanceof RtcTooltipHandler) {
//                RtcLogger.getLogger().removeHandler(handler);
//            }
//        }
    }

    @Override
    public void restored() {
        //System.out.println("instaluje handlerea");
        RtcLogger.getLogger(Installer.class).addHandler(new RtcTooltipHandler());
    }

    @Override
    public void close() {
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                Lookup.getDefault().lookup(RtcFavoritesServiceImpl.class).save();
            }
        });
    }
}
