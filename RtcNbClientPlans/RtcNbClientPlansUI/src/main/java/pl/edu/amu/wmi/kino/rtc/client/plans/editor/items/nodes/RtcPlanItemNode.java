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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.RtcAddNewPlanElementAction;

/**
 *
 * @author Patryk Żywica
 */
public class RtcPlanItemNode extends RtcAbstractNode {

    private InstanceContent ic;
    private RtcPlan plan;
    private RtcPlanItem planItem;

    public RtcPlanItemNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        this.planItem = getLookup().lookup(RtcPlanItem.class);
        this.plan = getLookup().lookup(RtcPlan.class);

        setDisplayName(planItem.getName());
        setChildren(Children.create(new RtcPlanItemChildren(
                getLookup().lookup(RtcPlanItem.class),
                getLookup().lookup(RtcPlan.class)), true));
    }

    public void replaceInstance(Object oldO, Object newO) {
        if (oldO != null) {
            ic.remove(oldO);
        }
        ic.add(newO);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>();
        actions.add((new RtcAddNewPlanElementAction().createContextAwareInstance(getLookup())));
        Action[] tmp = super.getActions(context);
        for (int i = 0; i < tmp.length; i++) {
            actions.add(tmp[i]);
        }
        return actions.toArray(new Action[0]);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = new Sheet();
        Sheet.Set set = Sheet.createPropertiesSet();

        //TODO
        /*
        RtcPlanItemAttribute[] attributes = plan.getPlansManager().getPlanItemAttributes();
        for(RtcPlanItemAttribute attribute : attributes) {

        //temporary hack
        if (attribute.getAttributeIdentifier().equals("priority")) {
        //zamiast pobierac property supporty  z atrybutu to stwórz RtcAtributePropertySupprotFactory
        //ktore z RtcPlanITemAtrribute bedzie robielo PropertySupprot
        //oczywiscie to bedzie w module GUI ( tylko i wyłacznie na zycznie Pawła),
        //te propertysupportFactory bedzie szukalo odpowiedniego edytorka i tworzylo propery support
        PropertySupport p = attribute.getPropertySupport(planItem);
        if (p != null) {
        set.put(p);
        }
        planItem.getPlanAttributeValue(attribute);
        }
        }
         * */


        sheet.put(set);

        return sheet;
    }

    /*
    @Override
    public Image getIcon(int type) {
    return getLookup().lookup(RtcPlanItem.class).get...
    }*/
    /**
     * Gets  help context for this action.
     * @return the help context for this action
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
