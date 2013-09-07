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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.layout;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemFieldsLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayout;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemLayoutManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.AbstractWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.AttributesManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.UnsupportedAttributeIdException;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.workitem.common.model.IWorkItem;
import java.util.EnumMap;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.layout.api.WorkItemLayoutProvider;

public class DefaultLayoutManager implements RtcWorkItemLayoutManager {

    private Map<RtcWorkItemViewTarget, Map<RtcWorkItemType, RtcWorkItemLayout>> layouts = new EnumMap<RtcWorkItemViewTarget, Map<RtcWorkItemType, RtcWorkItemLayout>>(RtcWorkItemViewTarget.class);
    private ActiveProjectAreaImpl area;
    private AttributesManager attrsManager;

    public DefaultLayoutManager(ActiveProjectAreaImpl area) {
        this.area = area;
        this.attrsManager = area.getLookup().lookup(AttributesManager.class);

    }

    @Override
    public RtcWorkItemLayout getLayout(RtcWorkItem wi,
            RtcWorkItemViewTarget target) {
        assert (wi instanceof AbstractWorkItem) : wi.getClass().getName();

        RtcWorkItemAttribute<RtcWorkItemType> attributeById = null;
        try {
            attributeById = attrsManager.getAttributeById(
                    IWorkItem.TYPE_PROPERTY, RtcWorkItemType.class);
        } catch (UnsupportedAttributeIdException e) {
            // shouldn't happen
            RtcLogger.getLogger().log(Level.SEVERE, "", e);
        }

        RtcWorkItemType type = wi.getValue(attributeById);

        if (!layouts.containsKey(target)) {
            layouts.put(target,
                    new HashMap<RtcWorkItemType, RtcWorkItemLayout>(4));
        }
        Map<RtcWorkItemType, RtcWorkItemLayout> types = layouts.get(target);
        if (!types.containsKey(type)) {
            RtcWorkItemLayout l = createLayout((AbstractWorkItem) wi, target);
            types.put(type, l);
        }

        return types.get(type);
    }

    private RtcWorkItemLayout createLayout(final AbstractWorkItem wi,
            final RtcWorkItemViewTarget target) {

        return new RtcWorkItemLayout() {

            @Override
            public RtcWorkItemAttribute<RtcWorkItemType> getTypeAttribute() {
                RtcWorkItemAttribute<RtcWorkItemType> attributeById = null;
                try {
                    attributeById = attrsManager.getAttributeById(
                            IWorkItem.TYPE_PROPERTY, RtcWorkItemType.class);
                } catch (UnsupportedAttributeIdException e) {
                    // shouldn't happen
                    RtcLogger.getLogger().log(Level.SEVERE, "", e);
                }
                return attributeById;
            }

            @Override
            public RtcWorkItemAttribute<String> getSummaryAttribute() {
                RtcWorkItemAttribute<String> attributeById = null;
                try {
                    attributeById = attrsManager.getAttributeById(
                            IWorkItem.SUMMARY_PROPERTY, String.class);
                } catch (UnsupportedAttributeIdException e) {
                    // shouldn't happen
                    RtcLogger.getLogger().log(Level.SEVERE, "", e);
                }
                return attributeById;
            }

            @Override
            public RtcWorkItemAttribute<Integer> getIdAttribute() {
                RtcWorkItemAttribute<Integer> attributeById = null;
                try {
                    attributeById = attrsManager.getAttributeById(
                            IWorkItem.ID_PROPERTY, Integer.class);
                } catch (UnsupportedAttributeIdException e) {
                    // shouldn't happen
                    RtcLogger.getLogger().log(Level.SEVERE, "", e);
                }
                return attributeById;
            }

            @Override
            public RtcWorkItemAttribute<String> getDescriptionAttribute() {
                RtcWorkItemAttribute<String> attributeById = null;
                try {
                    attributeById = attrsManager.getAttributeById(
                            IWorkItem.DESCRIPTION_PROPERTY, String.class);
                } catch (UnsupportedAttributeIdException e) {
                    // shouldn't happen
                    RtcLogger.getLogger().log(Level.SEVERE, "", e);
                }
                return attributeById;
            }

            @Override
            public WorkItemFieldsLayout getFieldsLayout() {
                Lookup l = Lookups.forPath("Rtc/Modules/WorkItems/Layouts");
                for (WorkItemLayoutProvider p : l.lookupAll(WorkItemLayoutProvider.class)) {
                    WorkItemFieldsLayout lay = p.createFieldsLayout(wi.getIWorkItem(), area, target);
                    if (lay != null) {
                        return lay;
                    }
                }
                throw new IllegalStateException();
            }
        };
    }
}
