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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLink;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLinkSection;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLinks;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcLinksEditor extends PropertyEditorSupport {

    private Lookup context;

    public RtcLinksEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public Component getCustomEditor() {
        return new JPanel();
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public String getAsText() {

        RtcLinks links = (RtcLinks) getValue();
        List<RtcLinkSection> sections = links.getLinkSections();
        String ret = "";
        for (RtcLinkSection section : sections) {
            //System.out.println(section.getName());
            ret += section.getName() + ", ";
            for (Iterator<RtcLink> it = section.getLinksInSection(section).iterator(); it.hasNext();) {
                RtcLink link = it.next();
                //System.out.println("Link: " + link.getComment());
            }

        }
        return ret;
    }

//    public static ToStringProviderImpl getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//    public static class ToStringProviderImpl implements ToStringProvider<RtcLinks> {
//
//        @Override
//        public String toString(RtcLinks value) {
//            return value.toString();
//        }
//    }
}
