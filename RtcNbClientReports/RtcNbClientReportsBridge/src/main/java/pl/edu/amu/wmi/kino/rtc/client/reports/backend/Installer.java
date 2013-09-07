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
package pl.edu.amu.wmi.kino.rtc.client.reports.backend;

import org.openide.modules.ModuleInfo;
import org.openide.modules.ModuleInstall;
import org.openide.modules.SpecificationVersion;
import org.openide.util.Lookup;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void validate() throws IllegalStateException {
        super.validate();
        for(ModuleInfo info : Lookup.getDefault().lookupAll(ModuleInfo.class)){
            if(info.getCodeName().equals("pl.edu.amu.wmi.kino.rtc.RtcNbClientLibsWrapper")){
                if(info.getSpecificationVersion().compareTo(new SpecificationVersion("0.3.3"))>=0){
                    throw new IllegalStateException();
                }
            }
        }
    }
}
