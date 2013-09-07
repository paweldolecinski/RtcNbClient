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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcAttachment;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcAttachmentsExtendedPanel;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcAttachmentsEditor extends PropertyEditorSupport {

    private Lookup context;

    public RtcAttachmentsEditor(Lookup context) {
        this.context = context;
    }

    //this method should be like in Discusion
    //everything should be in custom editor.
    @Override
    public String getAsText() {
        String ret = "";
        List<RtcAttachment> attachments = (List<RtcAttachment>) getValue();
        for (RtcAttachment attachment : attachments) {

            ret += attachment.getName() + ", ";
                attachment.getId();
                try {
                    attachment.getFile();
                } catch (IOException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcAttachmentsEditor.class)
                            .log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
        }
        return ret;

    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public Component getCustomEditor() {
        return new RtcAttachmentsExtendedPanel(this);
    }

}
