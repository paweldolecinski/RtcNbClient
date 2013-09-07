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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.approvals.impl;

import com.ibm.team.workitem.common.model.IApprovalType;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalType;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
class RtcApprovalTypeImpl implements RtcApprovalType {

    private final IApprovalType type;
    private final ImageIcon image;

    public RtcApprovalTypeImpl(IApprovalType type, String imagePath) {
        this.type = type;
        if (type == null) {
            image = new ImageIcon(ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/workitems/icons/contributor.gif"));
        } else {
            image = new ImageIcon(ImageUtilities.loadImage(imagePath));
        }
    }

    @Override
    public String getIdentifier() {
        if (type == null) {
            return "contributor";
        }
        return type.getIdentifier();
    }

    @Override
    public String getDisplayName() {
        if (type == null) {
            return "";
        }
        return type.getDisplayName();
    }

    @Override
    public Icon getIcon() {
        return image;
    }

    public String getKey() {
        return getDisplayName();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public IApprovalType getType() {
        return type;
    }

    public RtcApprovalType getValue() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcApprovalType) {
            RtcApprovalType kv = (RtcApprovalType) obj;
            return (kv.getIdentifier().equals(this.getIdentifier()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getIdentifier() != null ? this.getIdentifier().hashCode() : 0);
        return hash;
    }
}
