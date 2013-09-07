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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.panels;

import java.util.List;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;

/**
 *
 * @author Szymon Sadło
 */
public class ResultLayoutSortingChildFactory extends ChildFactory<RtcQueryColumn> {

    private List<RtcQueryColumn> sortingList;

    public ResultLayoutSortingChildFactory(List<RtcQueryColumn> sortingList) {
        this.sortingList = sortingList;
    }

    @Override
    protected boolean createKeys(List<RtcQueryColumn> list) {
        list.addAll(sortingList);
        return true;
    }

    @Override
    protected Node createNodeForKey(RtcQueryColumn t) {
        return new SortingNode(t);
    }

    class SortingNode extends AbstractNode {

        RtcQueryColumn c;

        public SortingNode(RtcQueryColumn c) {
            super(Children.LEAF, Lookups.singleton(c));
            this.c = c;
            setDisplayName(c.getColumnDisplayName());
        }

        @Override
        protected Sheet createSheet() {
            Sheet s = super.createSheet();
            Sheet.Set ss = s.get(Sheet.PROPERTIES);
            if (ss == null) {
                ss = Sheet.createPropertiesSet();
                s.put(ss);
            }
            ss.put(new SortingIndexProperty(c));
            return s;
        }
    }
}
