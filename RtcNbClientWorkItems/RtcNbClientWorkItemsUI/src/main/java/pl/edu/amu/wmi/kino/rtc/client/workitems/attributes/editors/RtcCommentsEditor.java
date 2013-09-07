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
import java.util.List;

import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcCommentsExtendedPanel;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcComment;

/**
 * This editor should in Inplace editor dispay the last comment and after mouse
 * click should open something like chat window with all comments and field to write
 * new comment.
 * 
 * @author Pawel Dolecinski
 */
public class RtcCommentsEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private Lookup context;
    public String[] durTable = new String[4];
    private PropertyEnv env;
    RtcCommentsExtendedPanel panel;

    public RtcCommentsEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public String getAsText() {
        return NbBundle.getMessage(RtcCommentsEditor.class, "AddNewComment.name");
    }

    @Override
    public void setAsText(String s) {

        List<RtcComment> comments = (List<RtcComment>) getValue();
        setValue(comments);

    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

    @Override
    public Component getCustomEditor() {
        panel = new RtcCommentsExtendedPanel(this, env, context);
        return panel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
    }
}
