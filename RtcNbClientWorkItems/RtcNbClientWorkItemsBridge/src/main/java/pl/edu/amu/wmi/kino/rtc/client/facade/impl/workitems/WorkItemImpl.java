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

import com.ibm.icu.util.UResourceTypeMismatchException;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.WorkItemReferences;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.references.WorkItemReferencesImpl;

/**
 * 
 * @author Paweł Doleciński
 */
public class WorkItemImpl extends AbstractWorkItem {

    private IWorkItem wi;
    private WorkItemReferences references;

    /**
     * 
     * @param workitem
     */
    /*package*/ WorkItemImpl(IWorkItemHandle workitem,
            WorkItemManagerImpl manager) {
        super(manager);
        if (workitem.hasFullState()) {
            this.wi = (IWorkItem) workitem.getFullState();
        } else {
            // TODO resolve wihandle
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public IWorkItem getIWorkItem() {
        return wi;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        //Hash code of WIWC cannot change, beacuse any HashMap containers will break up.
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getIWorkItem() == null) ? 0 : getIWorkItem().getItemId().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RtcWorkItem)) {
            return false;
        }
        WorkItemImpl other = (WorkItemImpl) obj;
        if (getIWorkItem() == null) {
            if (other.getIWorkItem() != null) {
                return false;
            }
        } else if (!getIWorkItem().getItemId().equals(other.getIWorkItem().getItemId())) {
            return false;
        }
        return true;
    }

    public WorkItemReferences getReferences() {
        throw new UResourceTypeMismatchException("Cannot get references from non working copy workitem");
        //FIXME : move this method to abstract WI and implement in this way that it can be called on non working copy WI
    }
}
