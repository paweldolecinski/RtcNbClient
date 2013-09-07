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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types;

import java.util.Date;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcHistoryItem {

    private String fModifier;
    private String fContent;
    private Date fModifiedDate;
    private boolean fIsCreation;

    public RtcHistoryItem(Date modifiedDate, String modifier, String content, boolean isCreation) {
        fModifiedDate = modifiedDate;
        fModifier = modifier;
        fContent = content;
        fIsCreation = isCreation;
    }

    public String getContent() {
        return fContent.replace("<del>", "<span class=\"deleted\">").replace("</del>", "</span>").replace("<ins>", "<span class=\"inserted\">").replace("</ins>", "</span>");
    }

    public String getModifier() {
        return fModifier;
    }

    public Date getDate() {
        return fModifiedDate;
    }

    public boolean isCreation() {
        return fIsCreation;
    }
}
