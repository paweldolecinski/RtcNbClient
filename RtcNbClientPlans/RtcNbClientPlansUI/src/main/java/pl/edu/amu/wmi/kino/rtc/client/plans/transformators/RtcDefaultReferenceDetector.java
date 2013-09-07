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

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;

import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContentUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage.RtcPlanPageEvent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;

/**
 *
 * @author michu
 */
public class RtcDefaultReferenceDetector implements IItemReferenceDetector,
EventListener<RtcPlanPage.RtcPlanPageEvent> {

    //managers
    private HashMap<String, RtcPlanPageAttachment> attachments;
    //plan page - needed for plan page attachments
    private RtcPlanPage planPage;

    public RtcDefaultReferenceDetector(RtcPlanPage planPage) {
        this.planPage = planPage;
        init();
        planPage.addListener(this);
    }

    private void init() {
        attachments = new HashMap<String, RtcPlanPageAttachment>();
        for (RtcPlanPageAttachment a : planPage.getAttachments()) {
            attachments.put(a.getName(), a);
        }
    }

    @Override
    public Reference detect(String text) {

        RtcPlanPageAttachment a = attachments.get(text);
        if (a != null) {

            URI u = null;
            try {
                u = RtcContentUtilities.convertToFile(a.getContent()).toURI();
            } catch (IOException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcDefaultReferenceDetector.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }

            if (u != null) {
                return new Reference(u);
            }
        }
        return null;
    }

    @Override
    public void eventFired(RtcPlanPageEvent event) {
        if(event.equals(RtcPlanPageEvent.CONTENT_CHANGED)) {
            init();
        }
    }
}
