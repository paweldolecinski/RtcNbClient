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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.nodes.providers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openide.explorer.propertysheet.PropertyPanel;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.netbeans.view.customview.CustomViewCallback;
import pl.edu.amu.wmi.kino.netbeans.view.customview.nodecontent.CustomViewColumnRenderProvider;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlansManager;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItem;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.checker.RtcPlanItemCheckerPopupFactory;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.attributes.RtcAttributePropertySupportFactory;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcDefaultViewColumnRenderProvider implements CustomViewColumnRenderProvider {

    private Node node;
    private RtcPlanItem planItem = null;
    private CustomViewCallback callback;
    private RtcPlansManager manager = null;
    private HashMap<String, JComponent> cachedColumns = new HashMap<String, JComponent>();
    private static int maxHeight, prefferedHeight, minHeight;
    private JComponent precolumn;

    public RtcDefaultViewColumnRenderProvider(Node node) {
        this.node = node;
        this.planItem = node.getLookup().lookup(RtcPlanItem.class);
        this.manager = node.getLookup().lookup(RtcPlan.class).getPlansManager();

        RtcDefaultViewColumnRenderProvider.maxHeight = 50;
        RtcDefaultViewColumnRenderProvider.prefferedHeight = 23;
        RtcDefaultViewColumnRenderProvider.minHeight = 23;
    }

    private JPanel getPanel(Node node, final String columnId, CustomViewCallback callback) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(callback.getBackgroundColor());

        if (columnId.equals("id")) {

            panel.setBackground(Color.decode("#F9F9F9"));

            JLabel label = new JLabel(planItem.getPlanItemIdentifier());
            label.setForeground(Color.decode("#000080"));
            panel.add(label);
            panel.setMaximumSize(new Dimension(30, maxHeight));
            panel.setMinimumSize(new Dimension(30, minHeight));
            panel.setPreferredSize(new Dimension(30, prefferedHeight));

        } else if (columnId.equals("icon")) {
            panel.add(new JLabel(ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/items/dummyPlanItemIcon.png", false)));
            panel.setMaximumSize(new Dimension(20, maxHeight));
            panel.setMinimumSize(new Dimension(20, minHeight));
            panel.setPreferredSize(new Dimension(20, prefferedHeight));

        } else {

            //mozna by wprowadzic dodatkowa metode do atrybutu ktora mowi, czy dany atrybut
            //powinien sie rozciagac lub nie?

            panel.setPreferredSize(new Dimension(150, prefferedHeight));
            panel.setMinimumSize(new Dimension(150, minHeight));

            if (columnId.equals("summary")) {
                panel.setMaximumSize(new Dimension(1500, maxHeight));
            } else if (columnId.equals("workItemType")) {
                panel.setMaximumSize(new Dimension(20, maxHeight));
                panel.setPreferredSize(new Dimension(20, prefferedHeight));
                panel.setMinimumSize(new Dimension(20, minHeight));
                panel.setLayout(new FlowLayout());
            } else {
                panel.setMaximumSize(new Dimension(150, maxHeight));
                panel.setLayout(new FlowLayout());
            }

            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    RtcAttributePropertySupportFactory f = Lookup.getDefault().lookup(RtcAttributePropertySupportFactory.class);
                    RtcPlanItemAttribute a = manager.getPlanItemAttribute(columnId);
//                    //System.out.println("KLASA: " + a.getValueType().getSimpleName());

                    if (a == null) {
                        panel.add(new JLabel("Null"));
                    } else {

                        PropertySupport ps = f.createPropertySupport(planItem, a);

                        if (ps != null) {
                            PropertyPanel p = new PropertyPanel(ps);
                            
                            p.setMinimumSize(new Dimension(16, minHeight));
                            p.setPreferredSize(new Dimension(150, prefferedHeight));
                            p.setOpaque(false);
                            
                            if(a.isReadOnly()) {
                                //p.setPreferences(PropertyPanel.PREF_READ_ONLY);
                                //p.setBorder(BorderFactory.createEmptyBorder());
                            }

                            if (columnId.equals("summary")) {
                                p.setMaximumSize(new Dimension(1500, maxHeight));
                            } else if (columnId.equals("workItemType")) {
                                p.setMaximumSize(new Dimension(20, maxHeight));
                                p.setPreferredSize(new Dimension(20, prefferedHeight));
                            } else {
                                p.setMaximumSize(new Dimension(150, maxHeight));
                            }
                            panel.add(p, BorderLayout.LINE_START);

                        } else {
                            panel.add(new JLabel(planItem.getPlanAttributeValue(a).toString()), BorderLayout.LINE_START);
                        }
                    }
                    cachedColumns.put(columnId, panel);
                }
            });
        }
        return panel;
    }

    @Override
    public JComponent getColumn(String columnId) {
        JComponent cell;

        if (planItem != null) {
            if (cachedColumns.containsKey(columnId)) {
                cell = cachedColumns.get(columnId);
            } else {

                JPanel c = getPanel(node, columnId, callback);
                cachedColumns.put(columnId, c);
                cell = c;
            }
        } else {
            JPanel c = getPanel(node, columnId, callback);
            cachedColumns.put(columnId, c);
            cell = c;
        }
        return cell;
    }

    @Override
    public boolean shouldDisplayPreColumn() {
        return true;
    }

    @Override
    public boolean shouldDisplayExpandColumn() {
        return true;
    }

    @Override
    public boolean shouldDisplayPostColumn() {
        return false;
    }

    @Override
    public boolean isIndentAllowed() {
        return true;
    }

    @Override
    public void setSelected(boolean selected) {

        for(Iterator<Entry<String, JComponent>> iter = cachedColumns.entrySet().iterator(); iter.hasNext(); ) {
            Entry<String, JComponent> entry = iter.next();
            if(selected) {
                entry.getValue().setBackground(callback.getSelectedBackgroundColor());
            } else {
                if(entry.getKey().equals("id") || entry.getKey().equals("error")) {
                    entry.getValue().setBackground(Color.decode("#F9F9F9"));
                } else {
                    entry.getValue().setBackground(callback.getBackgroundColor());
                }
            }
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFocused(boolean focused) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCallback(CustomViewCallback callback) {
        this.callback = callback;
    }

    @Override
    public JComponent getPreColumn() {
        //TODO : for future : planItem should be initialized before
        if (precolumn == null) {
            RtcPlan plan = node.getLookup().lookup(RtcPlan.class);
            precolumn = RtcPlanItemCheckerPopupFactory.createPlanItemCheckerPopup(planItem, plan);
            cachedColumns.put("_precolumn", precolumn);
        }
        return precolumn;
    }

    @Override
    public JComponent getPostColumn() {
        return null;
    }
}

class ExpandActionListener implements ActionListener {

    private CustomViewCallback callback;

    public ExpandActionListener(CustomViewCallback callback) {
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        callback.expand();
    }
}

class CollapseActionListener implements ActionListener {

    private CustomViewCallback callback;

    public CollapseActionListener(CustomViewCallback callback) {
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        callback.collapse();
    }
}
