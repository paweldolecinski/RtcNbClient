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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.lookup;

import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.providers.RtcQueryAttributeLookupProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.internal.attributes.values.RtcQueryAttributeTimestampValueCheckerImpl;

public class TimestampQueryAttributeLookupProvider implements RtcQueryAttributeLookupProvider {

    private IQueryableAttribute attribute;
    private ActiveProjectArea area;

    public TimestampQueryAttributeLookupProvider(IQueryableAttribute attribute, ActiveProjectArea area) {
        this.attribute = attribute;
        this.area = area;
    }

    @Override
    public Lookup createLookup() {
        return Lookups.singleton(new RtcQueryAttributeTimestampValueCheckerImpl());
    }
}
