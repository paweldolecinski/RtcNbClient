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

import java.awt.EventQueue;
import java.awt.Image;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider.Value;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.process.common.ITeamAreaHandle;
import com.ibm.team.process.internal.common.TeamAreaHandle;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import java.util.Arrays;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.TeamAreaImpl;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class ContributorAttributeContentProvider implements
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
        if (AttributeTypes.CONTRIBUTOR.equals(ia.getAttributeType())) {
            ContributorManagerImpl cm = (ContributorManagerImpl) area.getLookup().lookup(ContributorManager.class);
            ProcessManagerImpl pm = (ProcessManagerImpl) area.getLookup().lookup(ProcessManager.class);
            InstanceContent ic = new InstanceContent();
            Lookup lookup = new AbstractLookup(ic);
            List<TeamArea> areas = new LinkedList<TeamArea>();
            for (Object iTeamArea : area.getProjectArea().getIProcessArea().getTeamAreas()) {
                if (iTeamArea instanceof ITeamAreaHandle) {
                    TeamAreaImpl ta = pm.findTeamArea((TeamAreaHandle) iTeamArea);
                    if (Arrays.asList(pm.getMembers(ta)).contains(cm.findContributor(area.getITeamRepository().loggedInContributor()))) {
                        areas.add(ta);
                    }
                }
            }

            ic.add(new ContributorPossibleValues(area));
            ic.add(new ContributorPrefferedValues(area,areas, ia.getProjectArea()));
            return lookup;
        } else {
            return null;
        }
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class ContributorPossibleValues implements PossibleValues<Contributor> {

        private ContributorManagerImpl cm;

        ContributorPossibleValues(ActiveProjectArea area) {
            this.cm = (ContributorManagerImpl) area.getLookup().lookup(ContributorManager.class);
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.
         * PossibleValues#getPossibleValues()
         */
        @Override
        public ValueProvider.Value<Contributor>[] getValues() {
            assert (!EventQueue.isDispatchThread());
            List<ValueProvider.Value<Contributor>> list = new LinkedList<ValueProvider.Value<Contributor>>();
            for (ContributorImpl contrib : cm.getAllContributors()) {
                list.add(new ValueImpl(contrib));
            }
            @SuppressWarnings({"unchecked", "rawtypes"})
            ValueProvider.Value<Contributor>[] r = list.toArray(new ValueProvider.Value[list.size()]);
            return r;
        }
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class ContributorPrefferedValues implements PrefferedValues<Contributor> {

        private IProjectAreaHandle pa;
        private ActiveProjectArea area;
        private List<TeamArea> areas;
        private ContributorManagerImpl cm;
        private ProcessManagerImpl pm;

        ContributorPrefferedValues(ActiveProjectArea area, List<TeamArea> areas, IProjectAreaHandle pa) {
            assert areas != null : "List of team ares cannot be null but could be empty.";
            assert area != null;
            this.pa = pa;
            this.area = area;
            this.areas = areas;
            this.cm = (ContributorManagerImpl) area.getLookup().lookup(ContributorManager.class);
            this.pm = (ProcessManagerImpl) area.getLookup().lookup(ProcessManager.class);
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.
         * PrefferedValues#getValues()
         */
        @Override
        public ValueProvider.Value<Contributor>[] getValues() {
            Set<ValueProvider.Value<Contributor>> users = new HashSet<ValueProvider.Value<Contributor>>();

            if (areas.isEmpty()) {
                assert (!EventQueue.isDispatchThread());
                ContributorImpl[] tab = cm.getAllContributors();
                users = new HashSet<ValueProvider.Value<Contributor>>(tab.length);
                for (ContributorImpl contrib : tab) {
                    users.add(new ValueImpl(contrib));
                }
            } else {
                for (TeamArea teamArea : areas) {
                    if (teamArea instanceof TeamAreaImpl) {
                        for (ContributorImpl c : pm.getMembers(teamArea)) {
                            users.add(new ValueImpl(c));
                        }
                    }
                }
            }
            @SuppressWarnings("unchecked")
            ValueProvider.Value<Contributor>[] r = users.toArray(new ValueProvider.Value[users.size()]);
            return r;
        }
    }

    private static class ValueImpl implements
            ValueProvider.Value<Contributor> {

        private final ContributorImpl val;

        private ValueImpl(ContributorImpl contributor) {
            this.val = contributor;
        }

        @Override
        public Contributor getValue() {
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
        public ValueProvider.Value<Contributor>[] getChildren() {
            @SuppressWarnings({"unchecked"})
            Value<Contributor>[] ret = new Value[]{};
            return ret;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }
    }
}
