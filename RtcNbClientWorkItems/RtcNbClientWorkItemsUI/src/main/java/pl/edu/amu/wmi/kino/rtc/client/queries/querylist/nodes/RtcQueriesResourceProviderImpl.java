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
package pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.UnableToGetTheReferenceException;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers.RtcProjectAreaResourceProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.Pair;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesManager;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProviders({
    @ServiceProvider(service = RtcResourceProvider.class),
    @ServiceProvider(service = RtcQueriesResourceProviderImpl.class)
    })

public class RtcQueriesResourceProviderImpl extends RtcProjectAreaResourceProvider {

    @Override
    public Node getReferenceFromProjectArea(String resourceId, ActiveProjectArea activeProjectArea) throws UnableToGetTheReferenceException {
        RtcQueriesManager manager = activeProjectArea.getLookup().lookup(RtcQueriesManager.class);
        Pair<RtcQuery, RtcQueriesSet> result = manager.findQuery(resourceId);
        if (result == null) {
            throw new UnableToGetTheReferenceException(resourceId, true);
        }
        RtcQueryNodeFactory factory = Lookup.getDefault().lookup(RtcQueryNodeFactory.class);
        return factory.createNodeWithoutFavorites(result.getSecond(), result.getFirst());
    }

    @Override
    public String getProjectAreaResourceId(ActiveProjectArea area, Lookup context) {
        RtcQuery query = context.lookup(RtcQuery.class);
        return query.getQueryIdentifier();
    }

    @Override
    public boolean enableForContext(Lookup context) {
        return context.lookup(RtcQuery.class) != null
                && context.lookup(ActiveRepository.class) != null;
    }

    @Override
    public ActiveRepository getActiveRepository(Lookup context) {
        return context.lookup(ActiveRepository.class);
    }
}
