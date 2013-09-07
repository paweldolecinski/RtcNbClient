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
package pl.edu.amu.wmi.kino.rtc.client.connections.tab;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.connections.api.RtcProjectAreaDependentModuleFactory;
import pl.edu.amu.wmi.kino.rtc.client.experimental.RtcExperimentalSupport;

/**
 *
 * @author Patryk Żywica
 */
public class RtcActiveProjectAreaTabChildFactory extends ChildFactory<RtcProjectAreaDependentModuleFactory> implements LookupListener {

    private ActiveProjectArea area;
    private Lookup.Result<RtcProjectAreaDependentModuleFactory> result;

    public RtcActiveProjectAreaTabChildFactory(ActiveProjectArea area) {
        this.area = area;
        result = Lookups.forPath("Rtc/Modules/ProjectAreaNodeFactories").lookupResult(RtcProjectAreaDependentModuleFactory.class);
        result.addLookupListener(this);
    }

    @Override
    protected boolean createKeys(List<RtcProjectAreaDependentModuleFactory> toPopulate) {
        for(RtcProjectAreaDependentModuleFactory f : result.allInstances()){
            if(f.getClass().getName().equals("pl.edu.amu.wmi.kino.rtc.client.plans.RtcPlansModuleFactory")){
                if(Lookup.getDefault().lookup(RtcExperimentalSupport.class).isEnabled("plansModule")){
                    toPopulate.add(f);
                }
            }else{
                toPopulate.add(f);
            }
        }
        return true;
    }

    @Override
    protected Node[] createNodesForKey(RtcProjectAreaDependentModuleFactory key) {
        return key.createModuleNodes(area);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        refresh(false);
    }
}
