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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes;

import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.internal.AuditableCommon;
import com.ibm.team.workitem.common.model.IAttribute;
import java.util.logging.Level;

/**
 * Base implementation for all standard attributes
 * 
 * @author Paweł Doleciński
 */
public class SimpleAttribute<T> implements RtcWorkItemAttribute<T> {

	private final Class<T> valueType;
	private IAttribute attribute;
	private ProxyLookup lookup;

	/* package */SimpleAttribute(Class<T> valueType, IAttribute attribute,
			ProxyLookup pl) {
		this.valueType = valueType;
		this.lookup = pl;
		this.attribute = attribute;
	}

	public String getAttributeId() {
		return attribute.getIdentifier();
	}

	public boolean idEditable() {
		return !attribute.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute
	 * #getLookup()
	 */
	@Override
	public Lookup getLookup() {
		return lookup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute
	 * #getValueType()
	 */
	@Override
	public Class<T> getValueType() {
		return valueType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute
	 * #getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return attribute.getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute
	 * #getDefaultValue()
	 */
	@Override
	public T getDefaultValue() {
		AuditableCommon clientLibrary = (AuditableCommon) ((ITeamRepository) attribute
				.getOrigin()).getClientLibrary(AuditableCommon.class);
		try {
			Object defaultValue = attribute.getDefaultValue(clientLibrary,
					null, null);
		} catch (TeamRepositoryException ex) {
			//RtcLogger.getLogger(SimpleAttribute.class).warning(e.getMessage());
                        RtcLogger.getLogger(SimpleAttribute.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute
	 * #getNullValue()
	 */
	@Override
	public T getNullValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAttribute getIAttribute() {
		return attribute;
	}

    public String getId() {
        return attribute.getIdentifier();
    }
}
