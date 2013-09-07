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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import java.awt.Image;
import java.util.List;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcAttachment;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowAction;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
abstract public class RtcWorkItem{

    /**
     *
     * @return
     */
    public abstract String getDisplayName();

    /**
     *
     * @param name
     */
    public abstract void setDisplayName(String name);

    /**
     *
     * @return
     */
    public abstract Contributor getOwner();

    /**
     *
     * @param owner
     */
    public abstract void setOwner(Contributor owner);

    /**
     *
     * @return
     */
    public abstract RtcWorkFlow getWorkFlowAction();

    /**
     *
     * @param flowAction
     */
    public abstract void setWorkFlowAction(RtcWorkFlowAction flowAction);

    /**
     *
     * @return
     */
    public abstract Image getIcon();

    /**
     *
     * @return
     */
    public abstract int getId();

    /**
     *
     * @return
     */
    public abstract String getDescription();

    /**
     *
     * @param description
     */
    public abstract void setDescription(String description);

    /**
     *
     * @return
     */
    public abstract RtcWorkItemType getType();

    /**
     *
     * @return
     */
    public abstract RtcCategory getCategory();

    /**
     *
     * @param category
     */
    public abstract void setCategory(RtcCategory category);

    /**
     *
     * @return
     */
    public abstract RtcWorkItem getParent();

    /**
     * 
     * @return
     */
    public abstract RtcWorkItem[] getChildren();

    /**
     *
     * @return
     */
    public abstract List<RtcAttachment> getAttachments();

    /**
     *
     * @param attribute
     * @param value
     */
    public abstract void setValue(RtcAttribute attribute, Object value);

    /**
     *
     * @param attribute
     * @return
     */
    public abstract Object getValue(RtcAttribute attribute);

    /**
     *
     * @return
     */
    public abstract boolean isDirty();

    /**
     *
     * @throws pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem.RtcWorkItemSavingException
     */
    public abstract void save() throws RtcWorkItemSavingException;

    /**
     *
     */
    public abstract void refresh();

    /**
     *
     */
    public abstract void merge();

    /**
     *
     */
    public static class RtcWorkItemSavingException extends Exception {

        static final long serialVersionUID = -3387516993124229948L;

        /**
         * 
         * @param massage
         */
        public RtcWorkItemSavingException(String massage) {
            super(massage);
        }
    }
}
