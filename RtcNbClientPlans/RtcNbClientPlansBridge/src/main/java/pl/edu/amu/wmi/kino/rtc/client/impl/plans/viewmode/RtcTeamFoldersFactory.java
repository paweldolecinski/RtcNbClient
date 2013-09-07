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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.viewmode;

import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemFilter;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemGrouping;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewMode;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeFactory;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewModeManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemViewStyle;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.RtcPlanItemViewModeManagerImpl;

/**
 *
 * @author Patryk Żywica
 */
public class RtcTeamFoldersFactory implements RtcPlanItemViewModeFactory {

    /**
     * @see RtcPlanItemViewModeFactory
     * @param manager
     * @return
     */
    @Override
    public RtcPlanItemViewMode createViewMode(RtcPlanItemViewModeManager manager) {
        return new RtcTeamFolders(manager);
    }
}

/**
 *
 * @author Pawel Dolecinski
 */
class RtcTeamFolders extends RtcPlanItemViewModeImpl {

    private final String id = "com.ibm.team.apt.viewmodes.internal.teamFolders";    // NON-I18L
    private RtcPlanItemViewModeManagerImpl manager;

    RtcTeamFolders(RtcPlanItemViewModeManager manager) {
        this.manager = (RtcPlanItemViewModeManagerImpl) manager;
        this.name = NbBundle.getMessage(RtcTeamFolders.class, "planmode.name.teamFolders");
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RtcPlanItemAttribute[] getColumns() {
        if (columns == null) {
            columns = new ArrayList<RtcPlanItemAttribute>();

            RtcPlansManager plansManager = manager.getProjectArea().getLookup().lookup(RtcPlansManager.class);

            columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.type"));    // NON-I18L
            columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.summary"));    // NON-I18L
            columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.priority"));    // NON-I18L
            columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.state"));    // NON-I18L
        }

        return columns.toArray(new RtcPlanItemAttribute[]{});
    }

    @Override
    public RtcPlanItemViewModeManager getManager() {
        return manager;
    }

    @Override
    public void save() throws RtcSaveException {
        // TODO: dolek: implement
    }

    @Override
    public void discardChanges() {
        Lookup forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Filter");    // NON-I18L

        filters = new ArrayList((Collection<RtcPlanItemFilter>) forPath.lookupAll(RtcPlanItemFilter.class));
        forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Grouping");    // NON-I18L
        grouping = forPath.lookup(RtcPlanItemGrouping.class);
        forPath = Lookups.forPath("Rtc/Modules/PlansModule/Local/PlanItemViewModes/" + getId() + "/Sorting");    // NON-I18L
        sorting = forPath.lookup(RtcPlanItemSorting.class);
        columns = new ArrayList<RtcPlanItemAttribute>();

        RtcPlansManager plansManager = manager.getProjectArea().getLookup().lookup(RtcPlansManager.class);

        columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.type"));        // NON-I18L
        columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.summary"));              // NON-I18L
        columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.progress"));    // NON-I18L
        columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.priority"));    // NON-I18L
        columns.add(plansManager.getPlanItemAttribute("com.ibm.team.apt.attribute.planitem.state"));       // NON-I18L
        viewStyle = RtcPlanItemViewStyle.TREE;
        this.name = NbBundle.getMessage(RtcTeamFolders.class, "planmode.name.teamFolders");
        fireEvent(RtcPlanItemViewModeEvent.CHANGES_DISCARDED);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final RtcTeamFolders other = (RtcTeamFolders) obj;

        if ((this.id == null)
                ? (other.getId() != null)
                : !this.id.equals(other.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 53 * hash + ((this.id != null)
                ? this.id.hashCode()
                : 0);

        return hash;
    }
}
