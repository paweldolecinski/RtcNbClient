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
package pl.edu.amu.wmi.kino.rtc.client.workitems;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Action;

import org.openide.actions.PropertiesAction;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttributeLabelFactory.LabelProvider;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttributeLabelFactory.LabelSniffer;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem.RtcWorkItemSavingException;
import pl.edu.amu.wmi.kino.rtc.client.workitems.actions.DevOpenWorkItemAction;
import pl.edu.amu.wmi.kino.rtc.client.workitems.actions.RtcOpenWorkItemAction;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeEvents;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttribute.RtcWorkItemAttributeListener;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSet.RtcWorkItemAttributeSection;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.RtcWorkItemAttributeSetFactory;

/**
 *
 * @author psychollek
 */
@Deprecated
public class WorkItemNode extends AbstractNode {

    private InstanceContent ic;
    private Reference<Sheet> sheetRef = null;

    private WorkItemNode(RtcWorkItem wi, ActiveProjectArea activeProjectArea, InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        this.ic.add(activeProjectArea);
        this.ic.add(this);
        this.ic.add(new LabelSnifferImpl());
        this.ic.add(wi);
        //injectWorkingCopy(wi, activeProjectArea.getRepositoryConnection());
    }

    public WorkItemNode(RtcWorkItem wi, ActiveProjectArea activeProjectArea) {
        this(wi, activeProjectArea, new InstanceContent());
    }


    @Override
    protected Sheet createSheet() {

        PropertiesAction act = null;

        if (sheetRef == null || sheetRef.get() == null) {
            if (getLookup().lookup(RtcWorkItemAttributeSet.class) == null) {
                createAttributeSet();
            }

            RtcWorkItemAttributeSet attributes = getLookup().lookup(RtcWorkItemAttributeSet.class);
            Sheet sheet = new Sheet();

            Iterator<RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory> it = attributes.iterator();

            //this retrieves "none" section - header
//            RtcWorkItemAttributeSection all = it.next().get(0);
//            Sheet.Set allSet = Sheet.createPropertiesSet();
//            allSet.setName("all_attrs");
//            allSet.setDisplayName("Section just for testing");
//            for (Iterator<RtcWorkItemAttribute> it2 = all.iterator(); it2.hasNext();) {
//                RtcWorkItemAttribute workItemAttr = it2.next();
//                if (workItemAttr != null) {
//                    PropertySupport propertySupport = workItemAttr.getPropertySupport();
//                    propertySupport.setHidden(true);
//                    allSet.put(propertySupport);
//                }
//
//            }
//            allSet.setHidden(true);
//            sheet.put(allSet);

            RtcWorkItemAttributeSection none = it.next().get(0);

            //this section sets the first tab - which has to be named "Properties" (or
            //whatever other name of the default name of properties in the nb platform
            //language bundle) - so I can't set the "tabName" of it.
            // we have to remember that 'none' section could be the only one section

            {
                RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory category = null;
                String categoryName = "";
                if (it.hasNext()) {
                    category = it.next();
                    categoryName = category.getDisplayName();
                }
                //this section puts "none" section to every tab
                Sheet.Set noneSet = Sheet.createPropertiesSet();
                noneSet.setName(categoryName + ":" + none.getDisplayName());
                noneSet.setDisplayName("");
                for (Iterator<RtcWorkItemAttribute> it2 = none.iterator(); it2.hasNext();) {
                    RtcWorkItemAttribute workItemAttr = it2.next();
                    noneSet.put(workItemAttr.getPropertySupport());
                }
                sheet.put(noneSet);
                //end of "none" section
                if (category != null) {
                    for (Iterator<RtcWorkItemAttributeSet.RtcWorkItemAttributeSection> it1 = category.iterator(); it1.hasNext();) {
                        RtcWorkItemAttributeSet.RtcWorkItemAttributeSection section = it1.next();
                        String sectionName = section.getDisplayName();
                        Sheet.Set set = Sheet.createPropertiesSet();
                        set.setName(categoryName + ":" + sectionName);
                        set.setDisplayName(sectionName);
                        for (Iterator<RtcWorkItemAttribute> it2 = section.iterator(); it2.hasNext();) {
                            RtcWorkItemAttribute workItemAttr = it2.next();
                            set.put(workItemAttr.getPropertySupport());
                        }
                        sheet.put(set);
                    }
                }
            }

            while (it.hasNext()) {
                RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory category = it.next();
                String categoryName = category.getDisplayName();

                //this section puts "none" section to every tab
                Sheet.Set noneSet = Sheet.createPropertiesSet();
                noneSet.setName(categoryName + ":" + none.getDisplayName());
                noneSet.setDisplayName("");
                noneSet.setValue("tabName", categoryName);
                for (Iterator<RtcWorkItemAttribute> it2 = none.iterator(); it2.hasNext();) {
                    RtcWorkItemAttribute workItemAttr = it2.next();
                    noneSet.put(workItemAttr.getPropertySupport());
                }
                sheet.put(noneSet);
                //end of "none" section

                for (Iterator<RtcWorkItemAttributeSet.RtcWorkItemAttributeSection> it1 = category.iterator(); it1.hasNext();) {
                    RtcWorkItemAttributeSet.RtcWorkItemAttributeSection section = it1.next();
                    String sectionName = section.getDisplayName();
                    Sheet.Set set = Sheet.createPropertiesSet();
                    set.setName(categoryName + ":" + sectionName);
                    set.setDisplayName(sectionName);
                    set.setValue("tabName", categoryName);
                    for (Iterator<RtcWorkItemAttribute> it2 = section.iterator(); it2.hasNext();) {
                        RtcWorkItemAttribute workItemAttr = it2.next();
                        set.put(workItemAttr.getPropertySupport());
                    }
                    sheet.put(set);
                }
            }

            sheetRef = new WeakReference<Sheet>(sheet);

        }
        return sheetRef.get();
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>();
        actions.add(RtcOpenWorkItemAction.createContextAwareAction(getLookup()));
        actions.add(DevOpenWorkItemAction.createContextAwareAction(getLookup()));
        Action[] tmp = super.getActions(context);
        for (int i = 0; i < tmp.length; i++) {
            actions.add(tmp[i]);
        }
        return actions.toArray(new Action[0]);
    }

    @Override
    public Action getPreferredAction() {
        return RtcOpenWorkItemAction.createContextAwareAction(getLookup());
    }

    public boolean isModified() {
        return getLookup().lookup(RtcWorkItem.class).isDirty();
    }

    public void dropChanges() {
        getLookup().lookup(RtcWorkItem.class).refresh();
    }

    public void refresh() {
        getLookup().lookup(RtcWorkItem.class).merge();
    }

    private void createAttributeSet() {
        RtcWorkItemAttributeSet attrs = new RtcWorkItemAttributeSet();

        Collection<? extends RtcWorkItemAttributeSetFactory> factories = Lookup.getDefault().lookupAll(RtcWorkItemAttributeSetFactory.class);

        for (Iterator<? extends RtcWorkItemAttributeSetFactory> it = factories.iterator(); it.hasNext();) {
            RtcWorkItemAttributeSetFactory factory = it.next();
            if (factory.canCreateFromContext(getLookup())) {
                attrs = factory.createAttributeSet(getLookup());
                break;
            }
        }
        attachSaveCookieListener(attrs);
        ic.add(attrs);
    }

    private void attachSaveCookieListener(RtcWorkItemAttributeSet attrs) {

        RtcWorkItemAttributeListener listener = new RtcWorkItemAttributeListenerImpl();

        for (Iterator<RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory> it = attrs.iterator(); it.hasNext();) {
            RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory category = it.next();
            for (Iterator<RtcWorkItemAttributeSection> it1 = category.iterator(); it1.hasNext();) {
                RtcWorkItemAttributeSection section = it1.next();
                for (Iterator<RtcWorkItemAttribute> it2 = section.iterator(); it2.hasNext();) {
                    it2.next().addListener(listener);
                }
            }
        }
    }

    private void exposeSaveCookie() {
        if (getLookup().lookup(SaveCookie.class) == null) {
            ic.add(new WorkItemSaveCookie(getLookup().lookup(RtcWorkItem.class), getLookup().lookup(ActiveProjectArea.class), ic));
        }
    }

    public static class WorkItemSaveCookie implements SaveCookie {

        private RtcWorkItem workItem;
        private InstanceContent ic;

        public WorkItemSaveCookie(RtcWorkItem workItem, ActiveProjectArea pa, InstanceContent ic) {
            this.workItem = workItem;
            this.ic = ic;
        }

        @Override
        public void save() {
            try {
                workItem.save();
                ic.remove(this);
            } catch (RtcWorkItemSavingException ex) {
                RtcLogger.getLogger(WorkItemNode.class)
                        .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
    }

    private class LabelSnifferImpl implements LabelSniffer {

        @Override
        public LabelProvider findLabelProvider(String attrId) {
            if (getLookup().lookup(RtcWorkItemAttributeSet.class) == null) {
                createAttributeSet();
            }
            RtcWorkItemAttributeSet set = getLookup().lookup(RtcWorkItemAttributeSet.class);
            for (Iterator<RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory> it = set.iterator(); it.hasNext();) {
                RtcWorkItemAttributeSet.RtcWorkItemAttributeCategory cat = it.next();
                for (Iterator<RtcWorkItemAttributeSection> it1 = cat.iterator(); it1.hasNext();) {
                    RtcWorkItemAttributeSection sec = it1.next();
                    for (Iterator<RtcWorkItemAttribute> it2 = sec.iterator(); it2.hasNext();) {
                        RtcWorkItemAttribute attr = it2.next();
                        if (attr.getAttributeId().equals(attrId)) {
                            return new LabelProvider(attr);
                        }
                    }
                }
            }
            return null;
        }
    }

    public final class RtcWorkItemAttributeListenerImpl implements RtcWorkItemAttributeListener {

        @Override
        public void eventFired(RtcWorkItemAttribute source, RtcWorkItemAttributeEvents eventType) {
            exposeSaveCookie();
        }
    }
}
