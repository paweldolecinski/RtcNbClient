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

import com.ibm.team.workitem.common.model.IApprovalState;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.workitems.approvals.RtcApprovalState;

/**
 *
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcApprovalStateImpl implements RtcApprovalState{
    private final IApprovalState state;
    private final ImageIcon image;

    public RtcApprovalStateImpl(IApprovalState state, String imagePath) {
        this.state = state;
        image = new ImageIcon(ImageUtilities.loadImage(imagePath));
    }

    @Override
    public String getIdentifier() {
        return state.getIdentifier();
    }

    @Override
    public String getDisplayName() {
        return state.getDisplayName();
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

    public IApprovalState getState() {
        return state;
    }

    public RtcApprovalState getValue() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RtcApprovalState) {
            RtcApprovalState kv = (RtcApprovalState) obj;
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
