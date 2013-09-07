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
import java.util.ArrayList;
import java.util.List;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider.Value;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Deliverable;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.DeliverableImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class DeliverableAttributeContentProvider implements
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
        if (AttributeTypes.DELIVERABLE.equals(ia.getAttributeType())) {
            InstanceContent ic = new InstanceContent();
            Lookup lookup = new AbstractLookup(ic);
            ic.add(new ContributorPrefferedValues(area, ia.getProjectArea()));
            return lookup;
        } else {
            return null;
        }
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class ContributorPrefferedValues implements PrefferedValues<Deliverable> {

        private IProjectAreaHandle pa;
        private ActiveProjectAreaImpl area;
        private ProcessManagerImpl pm;

        ContributorPrefferedValues(ActiveProjectAreaImpl area, IProjectAreaHandle pa) {
            assert pa != null;
            this.pa = pa;
            this.area = area;
            pm = (ProcessManagerImpl) area.getLookup().lookup(ProcessManager.class);
            assert pm != null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.
         * PrefferedValues#getValues()
         */
        @Override
        public ValueProvider.Value<Deliverable>[] getValues() {
            List<ValueProvider.Value<Deliverable>> list = new ArrayList<ValueProvider.Value<Deliverable>>();
            for (DeliverableImpl d : pm.getDeliverables()) {
                list.add(new ValueImpl(d));
            }
            ValueProvider.Value<Deliverable>[] r = list.toArray(new ValueProvider.Value[list.size()]);
            return r;
        }
    }

    private static class ValueImpl implements
            ValueProvider.Value<Deliverable> {

        private final DeliverableImpl val;

        private ValueImpl(DeliverableImpl deliverable) {
            this.val = deliverable;
        }

        @Override
        public Deliverable getValue() {
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
        public Value<Deliverable>[] getChildren() {
            @SuppressWarnings({"unchecked"})
            Value<Deliverable>[] ret = new Value[]{};
            return ret;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }
    }
}
