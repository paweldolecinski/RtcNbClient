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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveRepositoryImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreaManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectAreasManagerEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 * 
 * @author Patryk Żywica
 */
public class ProjectAreaManagerImpl implements ProjectAreaManager {

    private ActiveRepositoryImpl repo;
    private Map<String, ProjectAreaImpl> areas;
    private EventSourceSupport<ProjectAreasManagerEvent> eventSource = new EventSourceSupport<ProjectAreasManagerEvent>();

    /*package*/ ProjectAreaManagerImpl(ActiveRepositoryImpl repo) {
        this.repo = repo;
    }

    @Override
    public ProjectArea[] getProjectAreas() {
        assert !EventQueue.isDispatchThread();
        if (areas == null) {
            initAreas();

        }
        return areas.values().toArray(new ProjectArea[]{});
    }

    
    private void initAreas() {
        try {
            areas = new HashMap<String, ProjectAreaImpl>(6);
            ITeamRepository rc = repo.getITeamRepository();
            IProcessItemService pis = (IProcessItemService) rc.getClientLibrary(IProcessItemService.class);
            for (IProjectArea a : (List<IProjectArea>) pis.findAllProjectAreas(
                    null, null)) {
                if (!a.isArchived()) {
                    ProjectAreaImpl tmp = new ProjectAreaImpl(a);
                    areas.put(tmp.getId(), tmp);
                }
            }
        } catch (TeamRepositoryException ex) {
            // TODO: i18n
            RtcLogger.getLogger(ProjectAreaManagerImpl.class).log(Level.SEVERE,
                    "Error while fetching project areas", ex);
        }
    }

    @Override
    public ProjectAreaImpl findProjectArea(String id) {
        if (areas == null) {
            initAreas();
        }
        return areas.get(id);
    }
    
    public ProjectAreaImpl findProjectArea(IProjectAreaHandle handle){
        return areas.get(handle.getItemId().getUuidValue());
    }

    @Override
    public void synchronizeWithServer() {
        // TODO : implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void removeListener(EventListener<ProjectAreasManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(ProjectAreasManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<ProjectAreasManagerEvent> listener) {
        eventSource.addListener(listener);
    }
}
