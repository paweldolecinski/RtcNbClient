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

import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.Identifier;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;

/**
 * TODO: dolek: javadoc
 * @author Pawel Dolecinski
 */
public class RtcLiteralImpl extends RtcLiteral {

    private final ILiteral literal;

    /**
     * TODO: dolek: javadoc
     * @param iLiteral
     */
    public RtcLiteralImpl(ILiteral iLiteral) {
        this.literal = iLiteral;

    }

    @Override
    public Image getIcon() {
        URL iconURL = literal.getIconURL();
        if(iconURL != null)
        	return ImageUtilities.icon2Image(new ImageIcon(iconURL));
        return null;
    }

    @Override
    public String getName() {
        return literal.getName();
    }

    @Override
    public String getId() {
        return literal.getIdentifier2().getStringIdentifier();
    }

    /**
     * Intended for internal use
     * @return
     */
    public ILiteral getLiteral()
    {
        return literal;
    }

    /**
     * TODO: dolek: javadoc
     * @return
     */
    public RtcLiteral getValue() {
        return this;
    }

    /**
     * TODO: dolek: javadoc
     * @return
     */
    public String getKey() {
        return getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj instanceof RtcLiteral) {
            RtcLiteral kv = (RtcLiteral) obj;
            return (kv.getId().equals(this.getId()));
        } else if (obj instanceof Identifier) {

            return this.getId().equals(((Identifier)obj).getStringIdentifier());
            
        } else {
            return literal.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
}
