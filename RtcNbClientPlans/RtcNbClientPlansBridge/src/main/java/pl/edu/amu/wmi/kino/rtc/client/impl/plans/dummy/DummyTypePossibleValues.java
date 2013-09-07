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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.ImageUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author michu
 */
public class DummyTypePossibleValues implements RtcPlanItemAttributePossibleValues<RtcWorkItemType> {

    private List<RtcWorkItemType> list;

    public DummyTypePossibleValues() {
        
    }

    @Override
    public List<RtcWorkItemType> getPossibleValues() {
        if(list == null) {
            list = new ArrayList<RtcWorkItemType>();
            final Image image = ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/editor/items/dummyPlanItemIcon.png", false);

            list.add(new RtcWorkItemType() {

                @Override
                public List<String> getAliases() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getId() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getDisplayName() {
                    return "Defekt";
                }

                @Override
                public String getCategory() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Image getIcon() {
                    return image;
                }
            });

            list.add(new RtcWorkItemType() {

                @Override
                public List<String> getAliases() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getId() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getDisplayName() {
                    return "Task";
                }

                @Override
                public String getCategory() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Image getIcon() {
                    return image;
                }
            });

            list.add(new RtcWorkItemType() {

                @Override
                public List<String> getAliases() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getId() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String getDisplayName() {
                    return "Story";
                }

                @Override
                public String getCategory() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Image getIcon() {
                    return image;
                }
            });
        }
        return list;
    }

}
