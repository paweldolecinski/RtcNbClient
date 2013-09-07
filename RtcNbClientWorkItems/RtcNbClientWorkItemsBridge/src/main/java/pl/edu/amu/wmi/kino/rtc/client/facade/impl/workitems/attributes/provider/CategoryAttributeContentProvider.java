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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider.Value;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemCategory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.WorkItemManagerImpl;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class CategoryAttributeContentProvider implements
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
        if (AttributeTypes.CATEGORY.equals(ia.getAttributeType())) {
            InstanceContent ic = new InstanceContent();
            Lookup lookup = new AbstractLookup(ic);
            ic.add(new CategoryPrefferedValues((WorkItemManagerImpl) area.getLookup().lookup(RtcWorkItemManager.class), ia.getProjectArea()));

            return lookup;
        } else {
            return null;
        }
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class CategoryPrefferedValues implements
            PrefferedValues<RtcWorkItemCategory> {

        private IProjectAreaHandle pa;
        private WorkItemManagerImpl wim;

        CategoryPrefferedValues(WorkItemManagerImpl wim, IProjectAreaHandle pa) {
            this.wim = wim;
            this.pa = pa;
        }

        @Override
        public ValueProvider.Value<RtcWorkItemCategory>[] getValues() {
            //TODO : reimplememt using WorkItemMamager
            assert (!EventQueue.isDispatchThread());
            List<ValueProvider.Value<RtcWorkItemCategory>> list = new LinkedList<ValueProvider.Value<RtcWorkItemCategory>>();
            IWorkItemCommon workItemCommon = (IWorkItemCommon) ((ITeamRepository) pa.getOrigin()).getClientLibrary(IWorkItemCommon.class);
            try {
                List<ICategory> findCategories = workItemCommon.findCategories(
                        pa, ICategory.DEFAULT_PROFILE, null);
                for (ICategory iCategory : findCategories) {
                    list.add(new CategoryValue(wim.findWorkItemCategory(iCategory)));
                }
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(CategoryAttributeContentProvider.class).log(Level.SEVERE,
                        ex.getLocalizedMessage(), ex);
            }
            @SuppressWarnings("unchecked")
            ValueProvider.Value<RtcWorkItemCategory>[] ret = list.toArray(new ValueProvider.Value[list.size()]);
            return ret;
        }
    }

    /**
     * @author Patryk Żywica
     */
    private static class CategoryValue implements
            ValueProvider.Value<RtcWorkItemCategory> {

        RtcWorkItemCategory category;

        CategoryValue(RtcWorkItemCategory category) {
            this.category = category;
        }

        public RtcWorkItemCategory getValue() {
            return category;
        }

        public String getDisplayName() {
            return category.getName();
        }

        public Image getIcon() {
            return null;
        }

        public Value<RtcWorkItemCategory>[] getChildren() {
            @SuppressWarnings({"unchecked"})
            Value<RtcWorkItemCategory>[] ret = new Value[]{};
            return ret;
        }

        public boolean isSelectable() {
            return true;
        }
    }
}
