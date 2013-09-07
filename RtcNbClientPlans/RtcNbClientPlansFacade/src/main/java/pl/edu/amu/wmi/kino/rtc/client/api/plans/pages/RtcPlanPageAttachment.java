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
package pl.edu.amu.wmi.kino.rtc.client.api.plans.pages;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

/**
 * @since 0.2.1.4
 * @author Patryk Żywica
 */
public abstract class RtcPlanPageAttachment {

    /**
     *
     * @return
     */
    public abstract String getName();

    /**
     *
     * @return
     */
    public abstract Contributor getCreator();

    /**
     *
     * @return
     */
    public abstract String getDescription();

    /**
     * 
     * @return
     */
    public abstract RtcContent getContent();

    /**
     * 
     * @return
     */
    public abstract boolean isPredefined();

    /**
     * 
     * @param page
     */
    public abstract void setOwner(RtcPlanPage page);
}
