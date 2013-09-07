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
package pl.edu.amu.wmi.kino.rtc.client.connections.services;

import java.lang.String;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;

/**
 * Class represents project area as a Node in Services tab.
 * Listening for changes in project area it represents.
 * @author Michał Wojciechowski
 * @author Tomasz Adamski (tomasz.adamski@gmail.com)
 * @see ProjectArea
 */
class RtcProjectAreaNode extends AbstractNode implements EventListener<RtcConnectionsManagerEvent> {

    private InstanceContent ic;

    public RtcProjectAreaNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        //TODO : bikol : linten on changes in project area
//        this.rtcProjectArea.addListener(this);
        setDisplayName(getProjectArea().getName());
        setShortDescription(NbBundle.getMessage(RtcProjectAreaNode.class, "RtcProjectAreaNode.tooltip"));
        updateIcon();
        updateLookup();
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        manager.addListener(this);
    }

    @Override
    public Action[] getActions(boolean b) {
        List<Action> actions = new LinkedList<Action>();
        actions.addAll(Utilities.actionsForPath("Rtc/Modules/CoreModule/Nodes/ProjectAreaActions"));
        actions.addAll(Arrays.asList(super.getActions(b)));
        return actions.toArray(new Action[]{});
    }

    @Override
    public Action getPreferredAction() {
        return getActions(true)[0];
    }

    @Override
    protected Sheet createSheet() {
        Sheet toReturn = new Sheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        toReturn.put(set);
        try {
            Property<String> prop = new PropertySupport.Reflection<String>(getProjectArea(), String.class, "getName", null);
            //TODO : i18n
            prop.setName("Project area name");
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcProjectAreaNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        try {
            Property<Boolean> prop = new PropertySupport.Reflection<Boolean>(getProjectArea(), Boolean.class, "isArchived", null);
            //TODO : i18n
            prop.setName("Project area archived");
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcProjectAreaNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return toReturn;
    }

    /**
     * After changes in {@link ProjectArea} this method sets icon appropriate
     * for current status
     * @see ProjectArea
     */
    @Override
    public void eventFired(RtcConnectionsManagerEvent event) {
        switch (event) {
            case ACTIVE_PROJECT_AREA_LIST_CHANGED:
                updateIcon();
                updateLookup();
                break;
        }
    }

    private void updateLookup() {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        if (manager.isActiveProjectArea(getRepositoryConnection(), getProjectArea())) {
            if (getLookup().lookup(ActiveProjectArea.class) == null) {
                ic.add(manager.activateProjectArea(getRepositoryConnection(), getProjectArea()));
            }
        } else {
            ActiveProjectArea area;
            while ((area = getLookup().lookup(ActiveProjectArea.class)) != null) {
                ic.remove(area);
            }
        }
    }

    private void updateIcon() {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        if (manager.isActiveProjectArea(getRepositoryConnection(), getProjectArea())) {
            setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/projectarea.gif");
        } else {
            setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/projectarea_inactive.gif");
        }
    }

    private RtcRepositoryConnection getRepositoryConnection() {
        return getLookup().lookup(RtcRepositoryConnection.class);
    }

    private ProjectArea getProjectArea() {
        return getLookup().lookup(ProjectArea.class);
    }

    /**
     * If current node represents not active project area then calling this method will
     * activate it.
     *
     * @return activeProjectArea represented by this node
     */
    @SuppressWarnings("unused")
	private ActiveProjectArea getActiveProjectArea() {
        ActiveProjectArea activeArea = getLookup().lookup(ActiveProjectArea.class);
        if (activeArea == null) {
            RtcConnectionsManager connectionsManager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
            RtcRepositoryConnection repositoryConnection = getLookup().lookup(RtcRepositoryConnection.class);
            ProjectArea projectArea = getLookup().lookup(ProjectArea.class);
            activeArea = connectionsManager.activateProjectArea(repositoryConnection, projectArea);
            ic.add(activeArea);
        }
        return activeArea;
    }

	@Override
	public HelpCtx getHelpCtx() {
		return new HelpCtx("usingRationalTeamConcert.ProjectArea"); //TODO monia: helpCtx
	}
    
}
