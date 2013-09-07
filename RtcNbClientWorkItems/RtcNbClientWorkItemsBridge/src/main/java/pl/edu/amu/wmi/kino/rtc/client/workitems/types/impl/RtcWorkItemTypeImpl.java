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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl;

import java.awt.Image;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import org.openide.util.ImageUtilities;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;

/**
 * 
 * @author Pawel Dolecinski
 */
//FIXME remove extends RtcWorkItemType
public class RtcWorkItemTypeImpl extends RtcWorkItemType implements pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemType{

    private IWorkItemType type;
    private Image icon = null;

    public RtcWorkItemTypeImpl(IWorkItemType rtcWorkItemType) {
        this.type = rtcWorkItemType;
    }

    /**
     * 
     * @param workItem from where type will be fetched
     */
    public RtcWorkItemTypeImpl(IWorkItem workItem) {
        IWorkItemClient wiClient = (IWorkItemClient) ((ITeamRepository) workItem.getOrigin()).getClientLibrary(IWorkItemClient.class);
        try {
            this.type = wiClient.findWorkItemType(workItem.getProjectArea(), workItem.getWorkItemType(), null);
            //assert type == null : "RtcWorkItemTypeImpl: type cannot be null";
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcWorkItemTypeImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public List<String> getAliases() {
        return type.getAliases();
    }

    @Override
    public String getId() {
        return type.getIdentifier();
    }

    @Override
    public String getDisplayName() {
        if (type == null) {
            return "error";
        } else {
            return type.getDisplayName();
        }
    }

    @Override
    public String getCategory() {
        return type.getCategory();
    }

    @Override
    public Image getIcon() {
        URL iconURL = type.getIconURL();
        if (icon == null && iconURL != null) {
            icon = ImageUtilities.icon2Image(new ImageIcon(iconURL));

        }
        return icon;
    }

    /**
     * Intended for internal use
     * @return work item type represented by RTC IWorkitemType object
     */
    public IWorkItemType getType() {
        return type;
    }

    /**
     * Intended for internal use
     * @return RtcWorkItemType type object representing this object
     */
    public RtcWorkItemType getValue() {
        return this;
    }

    /**
     * Intended for internal use
     * @return display name of work item type
     */
    public String getKey() {
        return getDisplayName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcWorkItemType) {
            RtcWorkItemType kv = (RtcWorkItemType) obj;
            return (kv.getId().equals(this.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

	@Override
	public Image getWorkItemIcon() {
		return getIcon();
	}
}
