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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans;

import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ProjectAreaDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service=ProjectAreaDependentManagerFactory.class,path="Rtc/ProjectAreaDependentManagerFactories")
public class RtcPlansManagerFactoryImpl implements ProjectAreaDependentManagerFactory<RtcPlansManager> {

    private HashMap<ActiveProjectArea, RtcPlansManager> managers = new HashMap<ActiveProjectArea, RtcPlansManager>(6);

    /**
     * For given project area this method will return instance of plans manager,
     * always the same instance.
     *
     * @param area which is project area for which plans manager will be returned
     * @return plans manager assigned to projecta rea
     */
    @Override
    public RtcPlansManager getManager(ActiveProjectArea area) {
        assert (area != null);
        if (managers.get(area) == null) {
            managers.put(area, new RtcPlansManagerImpl((ActiveProjectAreaImpl) area));
        }
        return managers.get(area);
    }

    /**
     * This method represents type of manager. In this case it will be always
     * RtcPlanManager Class object.
     *
     * @return plans manager class
     */
    @Override
    public Class<RtcPlansManager> getManagerType() {
        return RtcPlansManager.class;
    }

    /**
     * Prefix of plans manager is "PlansManager"
     *
     * @return prefix name of this manager
     */
    @Override
    public String getManagerNamePrefix() {
        return "PlansManager"; //NON-L18N
    }

    /**
     * Prefix id of plans manager is "PlansManager"
     *
     * @return plans manager prefix id
     */
    @Override
    public String getManagerIdPrefix() {
        return "PlansManager"; //NON-L18N
    }
}
