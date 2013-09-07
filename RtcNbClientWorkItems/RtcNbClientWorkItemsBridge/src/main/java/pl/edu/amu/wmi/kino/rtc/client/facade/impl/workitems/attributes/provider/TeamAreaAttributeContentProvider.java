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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;

import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.TeamAreaImpl;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class TeamAreaAttributeContentProvider implements
        AttributeContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider
     * .WorkItemAttributeContentProvider
     * #createLookup(com.ibm.team.workitem.common.model.IAttribute,
     * pl.edu.amu.wmi.kino.rtc.client.impl.connections.ActiveProjectAreaImpl)
     */
    @Override
    public Lookup createLookup(IAttribute ia, ActiveProjectAreaImpl area) {
        if (AttributeTypes.TEAM_AREA.equals(ia.getAttributeType())) {
            InstanceContent ic = new InstanceContent();
            Lookup lookup = new AbstractLookup(ic);
            ic.add(new TeamAreaPrefferedValues(area));
            return lookup;
        } else {
            return null;
        }
    }

    private static class TeamAreaPrefferedValues implements PrefferedValues<TeamArea> {

        private final ActiveProjectAreaImpl area;
        private ProcessManagerImpl pm;

        public TeamAreaPrefferedValues(
                ActiveProjectAreaImpl activeProjectArea) {
            this.area = activeProjectArea;
            pm = (ProcessManagerImpl) area.getLookup().lookup(ProcessManager.class);
        }

        @Override
        public ValueProvider.Value<TeamArea>[] getValues() {
            List<ValueProvider.Value<TeamArea>> list = new LinkedList<ValueProvider.Value<TeamArea>>();
            for (TeamAreaImpl ta : pm.getTeamAreas()) {
                list.add(new ValueImpl(ta, pm));
            }
            @SuppressWarnings("unchecked")
            ValueProvider.Value<TeamArea>[] r = list.toArray(new ValueProvider.Value[list.size()]);
            return r;
        }

        private final class ValueImpl implements
                ValueProvider.Value<TeamArea> {

            private final TeamAreaImpl val;
            private ProcessManagerImpl pm;

            private ValueImpl(TeamAreaImpl val, ProcessManagerImpl pm) {
                this.val = val;
                this.pm = pm;
            }

            @Override
            public TeamArea getValue() {
                return val;
            }

            @Override
            public String getDisplayName() {
                return val.getName();
            }

            @Override
            public Image getIcon() {
                return null;
            }

            @Override
            public Value<TeamArea>[] getChildren() {
                List<Value<TeamArea>> ret = new LinkedList<ValueProvider.Value<TeamArea>>();
                for (TeamAreaImpl ta : pm.getTeamAreas(val)) {
                    ret.add(new ValueImpl(ta, pm));
                }

                @SuppressWarnings("unchecked")
                ValueProvider.Value<TeamArea>[] r = ret.toArray(new ValueProvider.Value[ret.size()]);
                return r;
            }

            @Override
            public boolean isSelectable() {
                return true;
            }
        }
    }
}
