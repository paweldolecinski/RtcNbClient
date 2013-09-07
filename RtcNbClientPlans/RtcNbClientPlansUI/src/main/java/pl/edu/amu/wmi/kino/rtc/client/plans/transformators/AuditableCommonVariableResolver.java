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
package pl.edu.amu.wmi.kino.rtc.client.plans.transformators;

import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.IItemReferenceDetector.Reference;

/*
import com.ibm.team.repository.common.IAuditable;
import com.ibm.team.repository.common.ItemNotFoundException;
import com.ibm.team.repository.common.Location;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.model.Item;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.apt.internal.common.wiki.transformer.IItemReferenceDetector.Reference;
*/

public class AuditableCommonVariableResolver extends WorkItemVariableResolver {

    @Override
    public String resolveWorkItemProperty(Reference context, String property) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
	private IAuditableCommon fAuditableCommon;
	
	public AuditableCommonVariableResolver(IAuditableCommon auditableCommon) {
		fAuditableCommon= auditableCommon;
	}
	
	@Override
	public String resolveWorkItemProperty(Reference context, String property) {
		if(context == null || context.getLocation() == null)
			return null;
		
		try {
			Location location= context.getLocation();
			IAuditable item= fAuditableCommon.resolveAuditableByLocation(location, ItemProfile.createProfile(location.getItemType(), property), null);
			Object value= ((Item)item).getPropertyValue(property);
			return String.valueOf(value);
			
		} catch (IllegalArgumentException e) {
			// wrong property
		} catch (ItemNotFoundException e) {
			// ignore
		} catch (TeamRepositoryException e) {
			// ignore
		}
		
		return null;
	}
     *
     */
}
