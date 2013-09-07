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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems;

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemCategory;

/**
 *
 * @author Patryk Żywica
 */
public class WorkItemCategoryImpl implements RtcWorkItemCategory {

    private ICategory category;

    WorkItemCategoryImpl(ICategoryHandle categoryHandle) {
        if (categoryHandle.hasFullState()) {
            this.category = (ICategory) categoryHandle.getFullState();
        } else {
            if (categoryHandle instanceof ICategory) {
                this.category = (ICategory) categoryHandle;
            } else {
                try {
                    this.category = (ICategory) ((ITeamRepository) categoryHandle.getOrigin()).itemManager().fetchCompleteItem(categoryHandle, IItemManager.DEFAULT, null);
                } catch (TeamRepositoryException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
    }
    
    

    public String getName() {
        return category.getName();
    }

    public String getHTMLDescription() {
        return category.getHTMLDescription().getXMLText();
    }
    public ICategory getICategory(){
        return category;
    }
}
