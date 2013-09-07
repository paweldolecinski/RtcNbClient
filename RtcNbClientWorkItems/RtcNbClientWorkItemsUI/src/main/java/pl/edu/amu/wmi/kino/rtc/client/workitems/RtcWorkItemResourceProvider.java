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

//package pl.edu.amu.wmi.kino.rtc.client.workitems.impl;
//
//package pl.edu.amu.wmi.kino.rtc.client.workitems.providers;
//
//import com.ibm.team.process.common.IProjectArea;
//import com.ibm.team.repository.client.ITeamRepository;
//import com.ibm.team.repository.client.internal.ItemManager;
//import com.ibm.team.repository.common.TeamRepositoryException;
//import com.ibm.team.workitem.client.IWorkItemClient;
//import com.ibm.team.workitem.common.model.IWorkItem;
//import java.util.logging.Level;
//import org.openide.nodes.Node;
//import org.openide.util.Lookup;
//import org.openide.util.Lookup.Template;
//import org.openide.util.lookup.ServiceProvider;
//import pl.edu.amu.wmi.kino.rtc.client.connection.api.RtcActiveProjectArea;
//import pl.edu.amu.wmi.kino.rtc.client.connection.api.RtcLogger;
//import pl.edu.amu.wmi.kino.rtc.client.favorites.api.RtcResourceProvider;
//import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemNodeFactory;
//
///**
// *
// * @author Tomasz Adamski
// */
//@ServiceProvider(service=RtcResourceProvider.class)
//public class RtcWorkItemResourceProvider implements RtcResourceProvider {
//
//    @Override
//    public Node getReference(String resourceId, RtcActiveProjectArea projectArea) throws UnableToGetTheReference {
////        ITeamRepository teamRepository = (ITeamRepository) projectArea.getProjectArea().getOrigin();
////        IWorkItemClient workItemClient = (IWorkItemClient) teamRepository.getClientLibrary(IWorkItemClient.class);
////        IWorkItem wi=null;
////        try {
////            wi = workItemClient.findWorkItemById(Integer.parseInt(resourceId), IWorkItem.FULL_PROFILE, null);
////        } catch (TeamRepositoryException ex) {
////            throw new UnableToGetTheReference(resourceId, true);
////        }
////        return (new RtcWorkItemNodeFactory()).createNodeForWorkItem(wi, projectArea);
//    }
//
//    @Override
//    public boolean enableForContext(Lookup context) {
//        if(context.lookup(new Template(IWorkItem.class))!=null){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String getResourceId(Lookup context) {
//        IWorkItem wi = context.lookup(IWorkItem.class);
//        return String.valueOf(wi.getId());
//    }
//
//    @Override
//    public RtcActiveProjectArea getProjectArea(Lookup context) {
//        final IWorkItem wi = context.lookup(IWorkItem.class);
//        return new RtcActiveProjectArea() {
//
//            @Override
//            public IProjectArea getProjectArea() {
//                try {
//                    return (IProjectArea) getRepositoryConnection().itemManager().fetchCompleteItem(wi.getProjectArea(), ItemManager.DEFAULT, null);
//                } catch (TeamRepositoryException ex) {
//                    RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
//                }
//                return null;
//            }
//
//            @Override
//            public ITeamRepository getRepositoryConnection() {
//                return (ITeamRepository) wi.getOrigin();
//            }
//        };
//    }
//
//}
