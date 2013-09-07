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

import java.io.IOException;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSource;

/**
 *
 * @author Patryk Żywica
 */
public interface RtcPlanPage extends EventSource<RtcPlanPage.RtcPlanPageEvent> {

    /**
     *
     * @return
     */
    public abstract String getName();

    /**
     * 
     * @param name
     */
    public abstract void setName(String name);

    /**
     * 
     * @return
     */
    public abstract Lookup getLookup();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @return
     * @throws IOException 
     */
    public abstract String getPageContent();

    /**
     * This can be long running operation. Do not call on event dispatch thread.
     * @param content
     */
    public abstract void setPageContent(String content);

    /**
     * @since 0.2.1.4
     * @return
     */
    public abstract RtcPlanPageAttachment[] getAttachments();

    /**
     * @since 0.2.1.4
     * @param attachemnt
     */
    public abstract void addAttachment(RtcPlanPageAttachment attachemnt);

    /**
     * @since 0.2.1.4
     * @param attachment
     */
    public abstract void removeAttachment(RtcPlanPageAttachment attachment);

    /**
     * @since 0.2.1.4
     */
    public abstract void removeAttachments();

    /**
     * This enumeration defines all possible events for <code>RtcPlanPage</code>.
     *
     * @author Patryk Żywica
     */
    public enum RtcPlanPageEvent {

        /**
         * This event should be called when plan page content was changed.
         */
        CONTENT_CHANGED,
        /**
         * This event should be called when plan page name was changed.
         */
        NAME_CHANGED;
    }
}
