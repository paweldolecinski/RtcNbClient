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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view.node;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.NodeDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.PropertySetDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler.OptionChooser;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemHeaderPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter.WorkItemSlot;
import pl.edu.amu.wmi.kino.rtc.client.workitems.actions.DevOpenWorkItemAction;

/**
 *
 * @author Patryk Żywica
 */
public class NodeDisplayFactory implements DisplayFactory {

    private static class WorkItemNodeDisplay extends AbstractNode implements WorkItemPresenter.WorkItemDisplay, WorkItemHeaderPresenter.HeaderDisplay, NodeDisplay {

        Map<Sheet.Set, String> sets = new HashMap<Sheet.Set, String>();

        public WorkItemNodeDisplay(Lookup lookup) {
            super(Children.LEAF, lookup);
        }

        @Override
        public Action[] getActions(boolean context) {
            List<Action> actions = new ArrayList<Action>(4);
            actions.add(DevOpenWorkItemAction.createContextAwareAction(getLookup()));
            Action[] tmp = super.getActions(context);
            for (int i = 0; i < tmp.length; i++) {
                actions.add(tmp[i]);
            }
            return actions.toArray(new Action[0]);
        }

        @Override
        public Image getIcon(int type) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public Action getPreferredAction() {
            return DevOpenWorkItemAction.createContextAwareAction(getLookup());
        }

        public void open() {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void closeDisplay() {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void showDialog(String title, String msg, String[] options, OptionChooseHandler handler) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Node asNode() {
            return this;
        }

        public void addToSlot(WorkItemSlot slot, Display content) {
            switch (slot) {
                case HEADER:

            }
        }

        public void removeFromSlot(WorkItemSlot slot, Display content) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setInSlot(WorkItemSlot slot, Display content) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addTab(String title, Display content) {
            //FIXME : implement this more eficent, by not calling asPropertySet until create sheet method is called
            //and caching displasys in memory
            if (content instanceof PropertySetDisplay) {
                PropertySetDisplay psd = (PropertySetDisplay) content;
                psd.asPropertySet().setName(title);
                sets.put(psd.asPropertySet(), title);
                getSheet().put(psd.asPropertySet());
            }
        }

        public void removeTab(Display content) {
            if (content instanceof PropertySetDisplay) {
                PropertySetDisplay psd = (PropertySetDisplay) content;
                getSheet().remove(sets.get(psd.asPropertySet()));
            }
        }

        public void removeTabs() {
            
            //TODO : implement
        }

        public void clearSlot(WorkItemSlot slot) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        
        
        //Header Display implementation
        private String name;
        private int id = -1;

        public void setSummary(String summary) {
            name = summary;
            updateDisplayName();
        }

        public void setId(String identyfier) {
            this.id = id;
            updateDisplayName();
        }

        private void updateDisplayName() {
            if (id > 0 && name != null) {
                setDisplayName("" + id + ": " + name);
            } else {
                if (name != null) {
                    setDisplayName(name);
                } else {
                    setDisplayName("" + id);
                }
            }
        }

        public <T> void addHandler(Input<T> input, InputHandler<T> h) {
            // no support
        }

        public <T> void removeHandler(Input<T> input, InputHandler<T> handler) {
            // no support
        }

        public void setTitle(String title) {
//            setDisplayName(title);
        }

        public void setModified(boolean modified) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public HandlerRegistration addOptionHandler(OptionChooser source, OptionChooseHandler h) {
//            throw new UnsupportedOperationException("Not supported yet.");
            return null;
        }

        public void setIcon(Image icon) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

		@Override
		public HelpCtx getHelpCtx() {
			return new HelpCtx("usingRationalTeamConcert.EditingWorkItems");
		}
        
        
    }

    public <D extends Display> D createDisplay(Class<D> displayType, Lookup lookup) {
        if (displayType.equals(WorkItemPresenter.WorkItemDisplay.class)) {
            return (D) new WorkItemNodeDisplay(lookup);
        }
        return null;
    }
}
