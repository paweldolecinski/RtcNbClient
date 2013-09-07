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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProviderFactory;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.model.IWorkItem;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;

/**
 * @see RtcQueryAttributeManager
 * @author Patryk Żywica
 */
public class RtcQueryAttributeManagerImpl extends RtcQueryAttributeManager {

    private ActiveProjectAreaImpl area;
    private static HashMap<ActiveProjectArea, RtcQueryAttributeManagerImpl> managers =
            new HashMap<ActiveProjectArea, RtcQueryAttributeManagerImpl>();
    private RtcQueryAttributeLookupProviderFactory providerFactory;
    private Map<String, RtcQueryAttributeImpl> cache = new HashMap<String, RtcQueryAttributeImpl>();

    private RtcQueryAttributeManagerImpl(ActiveProjectAreaImpl area) {
        this.area = area;
        this.providerFactory = new RtcQueryAttributeLookupProviderFactory(area);
    }

    public static RtcQueryAttributeManagerImpl getFor(ActiveProjectAreaImpl area) {
        if (!managers.containsKey(area)) {
            managers.put(area, new RtcQueryAttributeManagerImpl(area));
        }
        return managers.get(area);
    }

    /**
     * @see RtcQueryAttributeManager#getQueryAttributes()
     * @return
     */
    @Override
    public RtcQueryAttributeImpl[] getQueryAttributes() {
        assert (!EventQueue.isDispatchThread());
        IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
        IAuditableCommon aCommon = (IAuditableCommon) area.getITeamRepository().getClientLibrary(IAuditableCommon.class);
        ArrayList<RtcQueryAttributeImpl> result = new ArrayList<RtcQueryAttributeImpl>();

        List<IQueryableAttribute> attrs;
        try {
            attrs = factory.findAllAttributes(area.getProjectArea().getIProcessArea(), aCommon, null);
            for (IQueryableAttribute attr : attrs) {
                result.add(getQueryAttribute(attr));
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcQueryAttributeManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return result.toArray(new RtcQueryAttributeImpl[]{});
    }

    public RtcQueryAttributeImpl getQueryAttribute(IQueryableAttribute attr) {
        assert (!EventQueue.isDispatchThread());
        if (!cache.containsKey(attr.getIdentifier())) {
            if (attr.getChildAttributes() != null && attr.getChildAttributes().size() > 0) {

                ArrayList<RtcQueryAttributeImpl> children = new ArrayList<RtcQueryAttributeImpl>();
                for (IQueryableAttribute a : attr.getChildAttributes()) {
                    children.add(getQueryAttribute(a));
                }
                RtcQueryAttributeImpl[] attrs = children.toArray(new RtcQueryAttributeImpl[]{});

                cache.put(attr.getIdentifier(), new RtcQueryAttributeImpl(
                        attr,
                        attrs,
                        providerFactory.getLookupProvider(attr),
                        area));
            } else {
                cache.put(attr.getIdentifier(), new RtcQueryAttributeImpl(
                        attr,
                        providerFactory.getLookupProvider(attr),
                        area));
            }
        }
        return cache.get(attr.getIdentifier());
    }
}
