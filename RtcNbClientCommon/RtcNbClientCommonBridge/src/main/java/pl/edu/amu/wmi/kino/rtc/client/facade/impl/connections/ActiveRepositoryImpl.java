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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections;

import com.ibm.team.repository.client.ITeamRepository;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.IndependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.RepositoryDependentManagerFactory;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.RtcIndependentLookupConvertorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.RtcRepositoryDependentLookupConvertorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage.RtcRepositoryConnectionImpl;

/**
 *
 * @author Patryk Żywica
 */
public class ActiveRepositoryImpl implements ActiveRepository {

    private RtcRepositoryConnectionImpl repositoryConnection;
    private Lookup lookup;

    public ActiveRepositoryImpl(RtcRepositoryConnectionImpl rc) {
        repositoryConnection = rc;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Lookup getLookup() {
        if (lookup == null) {
            Collection<? extends RepositoryDependentManagerFactory> repositoryDep =
                    Lookups.forPath("Rtc/RepositoryDependentManagerFactories").lookupAll(RepositoryDependentManagerFactory.class);
            Collection<? extends IndependentManagerFactory> inDep =
                    Lookups.forPath("Rtc/IndependentManagerFactories").lookupAll(IndependentManagerFactory.class);
            lookup = new ProxyLookup(
                    Lookups.fixed(repositoryDep.toArray(new RepositoryDependentManagerFactory[]{}), new RtcRepositoryDependentLookupConvertorImpl(this)),
                    Lookups.fixed(inDep.toArray(new IndependentManagerFactory[]{}), new RtcIndependentLookupConvertorImpl()));

        }
        return lookup;
    }

    public ITeamRepository getITeamRepository() {
        return repositoryConnection.getTeamRepository();
    }

    @Override
    public String getID() {
        return repositoryConnection.getTeamRepository().getId().getUuidValue();
    }

    @Override
    public RtcRepositoryConnectionImpl getRepositoryConnection() {
        return repositoryConnection;
    }
}
