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

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;

/**
 *
 * @author Michal Wojciechowski
 */
class DummyPriorityEnumerationPossibleValues implements RtcPlanItemAttributePossibleValues<RtcLiteral> {

    private List<RtcLiteral> list;

    public DummyPriorityEnumerationPossibleValues() {
        ////assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        
    }



    @Override
    public List<RtcLiteral> getPossibleValues() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        
        if(list == null)
            fillList();

        return list;
    }

    private void fillList() {
        list = new ArrayList<RtcLiteral>();
        list.add(new RtcLiteral() {

            @Override
            public Image getIcon() {
                return null;
            }

            @Override
            public String getName() {
                return "Unassigned";
            }

            @Override
            public String getId() {
                return "0";
            }
        });

        list.add(new RtcLiteral() {

            @Override
            public Image getIcon() {
                return null;
            }

            @Override
            public String getName() {
                return "Low";
            }

            @Override
            public String getId() {
                return "1";
            }
        });

        list.add(new RtcLiteral() {

            @Override
            public Image getIcon() {
                return null;
            }

            @Override
            public String getName() {
                return "Medium";
            }

            @Override
            public String getId() {
                return "2";
            }
        });
        list.add(new RtcLiteral() {

            @Override
            public Image getIcon() {
                return null;
            }

            @Override
            public String getName() {
                return "High";
            }

            @Override
            public String getId() {
                return "2";
            }
        });
    }
}
