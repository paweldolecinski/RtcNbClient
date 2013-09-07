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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.filters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.viewmode.RtcPlanItemSorting;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.utilities.RtcFilterNodeUtilities;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcPlanItemSortingFilterNode extends FilterNode {

    public RtcPlanItemSortingFilterNode(Node root, RtcPlanItemSorting sorting) {
        super(root);

        HashMap<RtcPlanItem, Node> map = RtcFilterNodeUtilities.getPlanItemsMap(root);
        List<Node> list = getFirstDepthParentsOfPlanItemNodes(root);

        /*
        for (Node node : list) {
            if (node instanceof RtcAbstractNode) {
                org.openide.nodes.Children ch = Children.create(new RtcPlanItemSortingChildren(node, map, sorting), false);
                ((RtcAbstractNode) node).setNewChildren(ch);
            }
        }*/

        org.openide.nodes.Children c = Children.create(new RtcPlanItemSortingChildren(root, map, sorting), false);
        setChildren(c);
        setDisplayName(sorting.getDisplayName());
    }

    /**
     * This method should return list of every possible parent of RtcPlanItemNode.
     * It had to recursivly explore each node -
     * there might be a situation where groups could containts NOT
     * same counter of subgroups.
     *
     * @param root
     * @return
     */
    private List<Node> getFirstDepthParentsOfPlanItemNodes(Node root) {
        List<Node> list = new LinkedList<Node>();

        //huj wie co robie zle

        for (Node node : root.getChildren().getNodes()) {
            if (node.getChildren().getNodesCount() > 0) {
                if(!list.contains(node)) {
                    list.add(root);

                    //
                    //return list;
                }
            } else {
                list.addAll(getFirstDepthParentsOfPlanItemNodes(node));
            }
        }

        return list;
    }

        /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
