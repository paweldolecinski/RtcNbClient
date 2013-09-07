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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor;

import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ProjectAreaDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.RepositoryDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveRepositoryImpl;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = RepositoryDependentManagerFactory.class, path = "Rtc/RepositoryDependentManagerFactories")
public class ContributorManagerFactory implements RepositoryDependentManagerFactory<ContributorManager> {

    private HashMap<ActiveRepository, ContributorManager> managers = new HashMap<ActiveRepository, ContributorManager>();

    @Override
    public ContributorManager getManager(ActiveRepository repo) {
        assert (repo != null);
        if (managers.get(repo) == null) {
            managers.put(repo, new ContributorManagerImpl((ActiveRepositoryImpl)repo));
        }
        return managers.get(repo);
    }

    @Override
    public Class<ContributorManager> getManagerType() {
        return ContributorManager.class;
    }

    @Override
    public String getManagerNamePrefix() {
        return "ContributorManager";
    }

    @Override
    public String getManagerIdPrefix() {
        return "ContributorManager";
    }    
}
