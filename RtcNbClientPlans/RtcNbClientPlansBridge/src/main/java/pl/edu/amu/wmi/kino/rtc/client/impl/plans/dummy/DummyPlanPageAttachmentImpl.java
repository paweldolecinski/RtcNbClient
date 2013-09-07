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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContent;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

/**
 *
 * @author Patryk Żywica
 */
public class DummyPlanPageAttachmentImpl extends RtcPlanPageAttachment {

    private RtcContent content;
    private String name = "dummy attachment";

    DummyPlanPageAttachmentImpl(Contributor contributor, String name, String description) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Contributor getCreator() {
        return new Contributor() {

            @Override
            public String getUserId() {
                return "dummyUserID";
            }

            @Override
            public String getName() {
                return "dummy User";
            }

            @Override
            public String getEmailAddress() {
                return "dummy@dumy.com";
            }

            @Override
            public boolean isArchived() {
                return false;
            }
        };
    }

    @Override
    public String getDescription() {
        return "dummy description";
    }

    @Override
    public RtcContent getContent() {
        if (content == null) {
            content = new DummyContentImpl();
        }
        return content;
    }

    @Override
    public boolean isPredefined() {
        return true;
    }

    @Override
    public void setOwner(RtcPlanPage page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
