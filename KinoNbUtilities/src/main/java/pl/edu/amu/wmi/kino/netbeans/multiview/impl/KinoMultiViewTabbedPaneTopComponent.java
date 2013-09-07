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
package pl.edu.amu.wmi.kino.netbeans.multiview.impl;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Provider;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewCallback;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewCloseHandler;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription.KinoMultiViewDescriptionEvent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription.KinoMultiViewDescriptionListener;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup.KinoMultiViewGroupEvent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup.KinoMultiViewGroupListener;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewHeader;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewTopComponentFactory.KinoMultiViewTopComponentTabPosition;

//TODO : bikol : add icon support
//-complete callback support
//-fix can close handling, now are two independent and not fully implemented -
//in multiView element and in topcomponent
/**
 *
 * @author Patryk Żywica
 */
public class KinoMultiViewTabbedPaneTopComponent extends TopComponent {

    //setting default selected tab
    private final int defaultSelectedTab = 0;
    private static final long serialVersionUID = 123476543366L;
    private JTabbedPane pane;
    private KinoMultiViewGroup[] groups;
    private List<List<KinoMultiViewDescription>> descs;
    private HashMap<KinoMultiViewDescription, KinoMultiViewElement> elements = new HashMap<KinoMultiViewDescription, KinoMultiViewElement>();
    private ArrayList<KinoMultiViewDescription> indexes = new ArrayList<KinoMultiViewDescription>();
    private HashMap<KinoMultiViewDescription, Integer> descToIndex =
            new HashMap<KinoMultiViewDescription, Integer>();
    private Lookup lookup;
    //TODO : bikol : each element have to have its own callback
    private KinoMultiViewCallback callback;
    private KinoMultiViewHeader header;
    private KinoMultiViewCloseHandler closeHanler;
    private KinoMultiViewTopComponentTabPosition position;

    public KinoMultiViewTabbedPaneTopComponent(KinoMultiViewGroup[] groups, KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler, KinoMultiViewTopComponentTabPosition position) {
        //initalization of class fields
        this.lookup = Lookups.proxy(new ProviderImpl());
        this.groups = groups;
        this.position = position;
        if (!header.isUpdateToolbarSupported()) {
            this.header = new KinoMultiViewHeaderWrapper(header);
        } else {
            this.header = header;
        }

        this.closeHanler = closeHandler;
        this.callback = new KinoMVCallbackImpl();

        //fetching all multiview descriptions from given groups
        descs = new ArrayList<List<KinoMultiViewDescription>>(groups.length);
        int i = 0;
        for (KinoMultiViewGroup f : groups) {
            descs.add(new LinkedList<KinoMultiViewDescription>(Arrays.asList(f.getDescriptions())));
            f.addListener(new KinoMultiViewDescriptionFactoryListenerImpl(i));
            i++;
        }
        pane = new JTabbedPane();
        updatePane(defaultSelectedTab);

        //setting tab placemnt
        if (this.position.equals(KinoMultiViewTopComponentTabPosition.BOTTOM)) {
            pane.setTabPlacement(JTabbedPane.BOTTOM);
        } else {
            pane.setTabPlacement(JTabbedPane.TOP);
        }

        //adding selected pane change listener
        pane.addChangeListener(new KinoMultiViewTabbedPaneChangeListener());

        //adding components to topcomponent
        BorderLayout bl = new BorderLayout();
        setLayout(bl);
        add(this.header, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
    }

    /**
     * Updated JTabbedPane and selects tab at given index
     * @param selected
     */
    private void updatePane(int selected) {
        indexes.clear();
        pane.removeAll();
        int i = 0;
        for (List<KinoMultiViewDescription> tmp : descs) {
            for (KinoMultiViewDescription desc : tmp) {
                addDescriptionListener(desc);
                indexes.add(desc);
                descToIndex.put(desc, i);
                pane.add(desc.getDisplayName(), null);
                i++;
            }
        }
        showTabAt(selected);
    }

    private void addDescriptionListener(KinoMultiViewDescription desc) {
        if (!descToIndex.containsKey(desc)) {
            desc.addListener(new KinoMultiViewDescriptionListenerImpl(desc));
        }
    }

    /**
     * Update JTabbedPane and selects tab corresponding to given description or default if
     * not found.
     * @param toSelect
     */
    private void updatePane(KinoMultiViewDescription toSelect) {
        indexes.clear();
        pane.removeAll();
        int selected = defaultSelectedTab;
        int i = 0;
        for (List<KinoMultiViewDescription> tmp : descs) {
            for (KinoMultiViewDescription desc : tmp) {
                addDescriptionListener(desc);
                indexes.add(desc);
                descToIndex.put(desc, i);
                pane.add(desc.getDisplayName(), null);
                //search for index to select
                if (desc == toSelect) {
                    selected = i;
                }
                i++;
            }
        }
        showTabAt(selected);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    private KinoMultiViewElement getElementFor(KinoMultiViewDescription desc) {
        if (!elements.containsKey(desc)) {
            KinoMultiViewElement element = desc.createElement();
            element.setMultiViewCallback(callback);
            elements.put(desc, element);
            return element;
        } else {
            return elements.get(desc);
        }
    }

    private void updateToolbarFor(int index) {
        assert header.isUpdateToolbarSupported() : "Wrapper header should always support update";
        if (index >= 0) {
            KinoMultiViewDescription desc = indexes.get(index);
            KinoMultiViewElement element = getElementFor(desc);
            header.updateToolbar(element.getToolbarRepresentation());
        } else {
            header.updateToolbar(null);
        }
    }

    private void updateComponentAt(int index) {
        if (index >= 0) {
            KinoMultiViewDescription desc = indexes.get(index);
            KinoMultiViewElement element = getElementFor(desc);
            pane.setComponentAt(index, element.getVisualRepresentation());
        }
    }

    private void showTabAt(int index) {

        updateToolbarFor(index);
        updateComponentAt(index);
        if (pane.getSelectedIndex() != index) {
            pane.setSelectedIndex(index);
        }
        //to update and fire events from lookup
        getLookup().lookup(Object.class);
    }

    private class KinoMultiViewTabbedPaneChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            showTabAt(index);
        }
    }

    private class KinoMultiViewDescriptionFactoryListenerImpl
            implements KinoMultiViewGroupListener, Runnable {

        private int index;
        private KinoMultiViewDescription descToSelect;

        KinoMultiViewDescriptionFactoryListenerImpl(int index) {
            this.index = index;
        }

        /**
         * This method updates tabs in topcomponent. it is called by multiview group.
         *
         * if given event is DESCRIPTION_ADDED then it tries to find all added descriptions.
         * If only one is added then topcomponent will be updated and tab representing
         * this description will be selected.
         * If many descriptions are added then tab representing currently selected description will
         * be selected after topcomponent's update.
         *
         * If given event is DESCRIPTION_REMOVED then topcomponent will be updated and
         * if description corresponding to selected tab wasn't removed then tab
         * representing currently selected description will be selected after update.
         * Else is description corresponding to selected tab was  removed then
         * default selected tab will be selected after update.
         *
         * if given event is DESCRIPTION_ORDER_CHANGED then after update tab representing currently
         * selected description will be selected.
         *
         *
         * @param event
         */
        @Override
        public void eventFired(KinoMultiViewGroupEvent event) {
            //TODO : for future , it can cause errors when next event will be fired before
            //last one finish
            switch (event) {
                case DESCRIPTION_ADDED:
                    //TODO : for future : it is done in inefficient way
                    LinkedList<KinoMultiViewDescription> added = new LinkedList<KinoMultiViewDescription>();
                    for (KinoMultiViewDescription d : groups[index].getDescriptions()) {
                        if (!descs.get(index).contains(d)) {
                            added.add(d);
                        }
                    }
                    if (added.size() == 1) {
                        descToSelect = added.get(0);
                    } else {
                        if (added.size() == 0) {
                            descToSelect = indexes.get(pane.getSelectedIndex());
                        } else {
                            descToSelect = null;
                        }
                    }
                    break;
                case DESCRIPTION_ORDER_CHANGED:
                case DESCRIPTION_REMOVED:
                    descToSelect = indexes.get(pane.getSelectedIndex());
                    break;
            }
            descs.get(index).clear();
            descs.get(index).addAll(new ArrayList<KinoMultiViewDescription>(
                    Arrays.asList(groups[index].getDescriptions())));
            EventQueue.invokeLater(this);
        }

        @Override
        public void run() {
            if (descToSelect != null) {
                updatePane(descToSelect);
                descToSelect = null;
            } else {
                updatePane(defaultSelectedTab);
            }
        }
    }

    @Override
    protected void componentClosed() {
        closeHanler.close();
    }

    @Override
    public boolean canClose() {
        return closeHanler.canClose();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    private class ProviderImpl implements Provider {

        @Override
        public Lookup getLookup() {
            int index = pane.getSelectedIndex();
            if (index >= 0) {
                return elements.get(indexes.get(index)).getLookup();
            } else {
                return Lookup.EMPTY;
            }

        }
    }

    private class KinoMVCallbackImpl implements KinoMultiViewCallback {

        @Override
        public void requestActive() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void requestVisible() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Action[] createDefaultActions() {
            return KinoMultiViewTabbedPaneTopComponent.this.getActions();
        }

        @Override
        public void updateTitle(String title) {
            KinoMultiViewTabbedPaneTopComponent.this.setDisplayName(title);
        }

        @Override
        public boolean isSelectedElement() {
            //TODO : implement this method
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TopComponent getTopComponent() {
            return KinoMultiViewTabbedPaneTopComponent.this;
        }
    }

    private class KinoMultiViewHeaderWrapper extends KinoMultiViewHeader {

        private static final long serialVersionUID = 1L;
        private JComponent currentToolbar;
        private KinoMultiViewHeader header;

        KinoMultiViewHeaderWrapper(KinoMultiViewHeader header) {
            this.header = header;
            setLayout(new BorderLayout());
            add(header, BorderLayout.CENTER);
        }

        @Override
        public boolean isUpdateToolbarSupported() {
            return true;
        }

        @Override
        public void updateToolbar(JComponent toolbar) {
            if (currentToolbar != null) {
                remove(currentToolbar);
            }
            currentToolbar = toolbar;
            if (currentToolbar != null) {
                add(currentToolbar, BorderLayout.NORTH);
            }
        }
    }

    private class KinoMultiViewDescriptionListenerImpl implements KinoMultiViewDescriptionListener {

        private KinoMultiViewDescription desc;

        KinoMultiViewDescriptionListenerImpl(KinoMultiViewDescription desc) {
            this.desc = desc;
        }

        @Override
        public void eventFired(KinoMultiViewDescriptionEvent event) {
            pane.setTitleAt(descToIndex.get(desc), desc.getDisplayName());
        }
    }
}
