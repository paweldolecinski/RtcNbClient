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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.IContributorLicenseType;
import com.ibm.team.repository.common.ILicenseAdminService;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorLicense;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveRepositoryImpl;

/**
 *
 * @author Bartosz Zaleski 
 * @author Patryk Żywica
 */
public class ContributorManagerImpl implements ContributorManager {

    private ActiveRepositoryImpl repo;
    private Map<String, ContributorImpl> contributors = new HashMap<String, ContributorImpl>();
    private ContributorImpl[] contributorsCache;
    private EventSourceSupport<ContributorManagerEvent> eventSource = new EventSourceSupport<ContributorManagerEvent>();
    /*package*/ ContributorManagerImpl(ActiveRepositoryImpl repo) {
        this.repo = repo;
    }

    @Override
    public ContributorLicenseImpl[] getContributorLicences(Contributor contributor) {
        IContributorHandle contr = ((ContributorImpl) contributor).getIContributor();
        String[] contrLicenses;
        List<ContributorLicenseImpl> result = new ArrayList<ContributorLicenseImpl>();
        try {
            contrLicenses = ((ILicenseAdminService) ((ITeamRepository) contr.getOrigin()).getClientLibrary(ILicenseAdminService.class)).getAssignedLicenses(contr);
            IContributorLicenseType[] licenses = ((ILicenseAdminService) ((ITeamRepository) contr.getOrigin()).getClientLibrary(ILicenseAdminService.class)).getLicenseTypes();
            for (int i = 0; i < contrLicenses.length; i++) {
                for (int j = 0; j < licenses.length; j++) {
                    if (licenses[j].getName().equals(contrLicenses[i])) {
                        result.add(new ContributorLicenseImpl(licenses[j]));
                    }
                }
            }
            return result.toArray(new ContributorLicenseImpl[]{});
        } catch (TeamRepositoryException ex) {
            //ex.printStackTrace();
            RtcLogger.getLogger(ContributorManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } catch (Exception e) {
            //e.printStackTrace();
            RtcLogger.getLogger(ContributorManagerImpl.class).log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return new ContributorLicenseImpl[]{};
    }

    @Override
    public void synchronizeWithServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ContributorImpl getLoggedInContributor() {
        return findContributor(repo.getITeamRepository().loggedInContributor());
    }

    public ContributorImpl[] getContributors() {
        if (contributorsCache == null) {
            try {
                @SuppressWarnings("rawtypes")
                List tab = repo.getITeamRepository().contributorManager().fetchAllContributors(null);
                ArrayList<ContributorImpl> result = new ArrayList<ContributorImpl>(tab.size());
                for (Object o : tab) {
                    if (o instanceof IContributorHandle) {
                        ContributorImpl contrib = findContributor((IContributorHandle) o);
                        if (!contrib.isArchived()) {
                            result.add(contrib);
                        }
                    }
                }
                contributorsCache = result.toArray(new ContributorImpl[]{});
            } catch (TeamRepositoryException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(ContributorManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
        return contributorsCache;
    }

    public ContributorImpl[] getAllContributors() {
        if (contributorsCache == null) {
            try {
                @SuppressWarnings("rawtypes")
                List tab = repo.getITeamRepository().contributorManager().fetchAllContributors(null);
                ArrayList<ContributorImpl> result = new ArrayList<ContributorImpl>(tab.size());
                for (Object o : tab) {
                    if (o instanceof IContributorHandle) {
                        result.add(findContributor((IContributorHandle) o));
                    }
                }
                contributorsCache = result.toArray(new ContributorImpl[]{});
            } catch (TeamRepositoryException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(ContributorManagerImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

        }
        return contributorsCache;
    }

    public ContributorImpl findContributor(IContributorHandle handle) {
        if (!contributors.containsKey(handle.getItemId().getUuidValue())) {
            ContributorImpl impl = new ContributorImpl(handle);
            contributors.put(handle.getItemId().getUuidValue(), impl);
        }
        return contributors.get(handle.getItemId().getUuidValue());
    }

    public final void removeListener(EventListener<ContributorManagerEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(ContributorManagerEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<ContributorManagerEvent> listener) {
        eventSource.addListener(listener);
    }
    
}
