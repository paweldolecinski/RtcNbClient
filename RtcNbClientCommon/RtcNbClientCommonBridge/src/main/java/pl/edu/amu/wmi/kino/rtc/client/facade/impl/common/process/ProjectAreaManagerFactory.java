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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process;

import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.RepositoryDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveRepositoryImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = RepositoryDependentManagerFactory.class, path = "Rtc/RepositoryDependentManagerFactories")
public class ProjectAreaManagerFactory implements RepositoryDependentManagerFactory<ProjectAreaManager> {

    private HashMap<ActiveRepository, ProjectAreaManagerImpl> managers =
            new HashMap<ActiveRepository, ProjectAreaManagerImpl>(4);

    @Override
    public Class<ProjectAreaManager> getManagerType() {
        return ProjectAreaManager.class;
    }

    @Override
    public ProjectAreaManager getManager(ActiveRepository repo) {
        if (repo instanceof ActiveRepositoryImpl) {
            if (!managers.containsKey(repo)) {
                managers.put(repo, new ProjectAreaManagerImpl((ActiveRepositoryImpl) repo));
            }
            return managers.get(repo);
        } else {
            return null;
        }
    }

    @Override
    public String getManagerNamePrefix() {
        return "ProjectAreaManager";
    }

    @Override
    public String getManagerIdPrefix() {
        return "ProjectAreaManager";
    }
}
