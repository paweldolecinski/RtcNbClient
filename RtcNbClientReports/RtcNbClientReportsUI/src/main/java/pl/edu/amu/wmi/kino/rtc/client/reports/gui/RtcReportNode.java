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
package pl.edu.amu.wmi.kino.rtc.client.reports.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportActionReference;
import pl.edu.amu.wmi.kino.rtc.client.reports.RtcReportsRetriever.RtcReportReference;

/**
 *
 * @author psychollek
 */
public class RtcReportNode extends AbstractNode {

    private RtcReportNode(RtcReportReference reportReference, InstanceContent ic) {
        super(reportReference.getChildrenFactory() == null
                ? Children.LEAF
                : Children.create(
                new RtcReportsChildrenFactory(
                reportReference.getChildrenFactory()),
                true),
                new AbstractLookup(ic));
        ic.add(reportReference);
    }

    public RtcReportNode(RtcReportReference reportReference) {
        this(reportReference, new InstanceContent());
        setIconBaseWithExtension(reportReference.getIconBaseWithExtension());
        setDisplayName(reportReference.getDisplayName());
        setShortDescription(reportReference.getShortDescription());
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> retVal = new ArrayList<Action>();
        //if(context){
        RtcReportActionReference[] actions = getLookup().lookup(RtcReportReference.class).getActions();

        for (int i = 0; i < actions.length; i++) {
            retVal.add(new RtcReportAction(actions[i]));
        }
        //}
        retVal.addAll(Arrays.asList(super.getActions(context)));
        return retVal.toArray(new Action[retVal.size()]);
    }

    @Override
    public Action getPreferredAction() {
        RtcReportActionReference defaultAction = getLookup().lookup(RtcReportReference.class).getDefaultAction();
        return defaultAction == null ? null : new RtcReportAction(defaultAction);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcReportNode.class);
    }



    public static class RtcReportAction extends AbstractAction implements HelpCtx.Provider {

		private static final long serialVersionUID = 9139476249839088305L;
		private final RtcReportActionReference action;

        private RtcReportAction(RtcReportActionReference action) {
            super(action.getDisplayName(), new ImageIcon(ImageUtilities.loadImage(action.getIconBaseWithExtension(), true)));
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            action.performAction();
        }
/**
 *
 * @return the defult help context
 */
        @Override
        public HelpCtx getHelpCtx() {
            return new HelpCtx(RtcReportAction.class);
        }
    }
}
