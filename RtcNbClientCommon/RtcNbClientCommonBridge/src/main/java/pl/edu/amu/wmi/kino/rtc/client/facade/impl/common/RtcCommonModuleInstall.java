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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common;

import org.openide.modules.ModuleInfo;
import org.openide.modules.ModuleInstall;
import org.openide.modules.SpecificationVersion;
import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;

/**
 * It's a class responsible for Module LifeCycle. To disable or change to another,
 * edit <code>OpenIDE-Module-Install</code> property in Module Manifest.
 * @author Dawid Holewa
 * @author Tomasz Adamski
 */
public class RtcCommonModuleInstall extends ModuleInstall {
    private static final long serialVersionUID = 2342123432L;

    /**
     * This method is saving the repositories connections data at closing of module.
     */
    @Override
    public void close() {
        //System.out.println("wywolanie close");
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        manager.shutdown();
    }
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
