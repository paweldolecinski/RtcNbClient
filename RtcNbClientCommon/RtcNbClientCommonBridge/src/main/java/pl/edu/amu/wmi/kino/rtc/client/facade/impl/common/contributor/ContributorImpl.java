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

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;


/**
 * @author Paweł Doleciński
 *
 */
public class ContributorImpl implements Contributor {

	private IContributor contributor = null;
	private IContributorHandle contributorHandle;
	private String name;
	private String userId;
	private String mail;
	private boolean arch;

	/**
	 * Create wrapper for Contributor using IContributorHandle
	 * @param value
	 */
	/* package */ContributorImpl(IContributorHandle value) {
		this.contributorHandle = value;
	}

	/**
	 * Intended for internal use
	 * @return
	 */
	public IContributorHandle getContributorHandle() {
		return contributorHandle;
	}

	/* (non-Javadoc)
	 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor#getName()
	 */
	@Override
	public String getName() {
		if (contributor == null) {
			createFullItem();
		}
		if (name == null) {
			name = contributor.getName();
		}
		return name;
	}

	/**
	 * Intended for internal use
	 * 
	 * @return
	 */
	public IContributor getIContributor() {
		if (contributor == null) {
			createFullItem();
		}
		return contributor;
	}

	/* (non-Javadoc)
	 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor#getUserId()
	 */
	@Override
	public String getUserId() {
		if (contributor == null) {
			createFullItem();
		}
		if (userId == null) {
			userId = contributor.getUserId();
		}
		return userId;
	}

	/* (non-Javadoc)
	 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor#getEmailAddress()
	 */
	@Override
	public String getEmailAddress() {
		if (contributor == null) {
			createFullItem();
		}
		if (mail == null) {
			mail = contributor.getEmailAddress();
		}
		return mail;
	}

	/* (non-Javadoc)
	 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor#isArchived()
	 */
	@Override
	public boolean isArchived() {
		if (contributor == null) {
			createFullItem();
			arch = contributor.isArchived();
		}
		return arch;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contributor) {
			Contributor kv = (Contributor) obj;
			return (kv.getUserId().equals(this.getUserId()));
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash
				+ (this.getUserId() != null ? this.getUserId().hashCode() : 0);
		return hash;
	}

	private void createFullItem() {
		if (contributorHandle instanceof IContributor) {
			this.contributor = (IContributor) contributorHandle;
		}
		if (contributorHandle.hasFullState()) {
			this.contributor = (IContributor) contributorHandle.getFullState();
		} else {
			try {
				this.contributor = (IContributor) ((ITeamRepository) contributorHandle
						.getOrigin()).itemManager().fetchCompleteItem(
						contributorHandle, IItemManager.DEFAULT, null);
			} catch (TeamRepositoryException ex) {
				RtcLogger.getLogger(ContributorImpl.class).log(Level.SEVERE,
						ex.getLocalizedMessage(), ex);
			}
		}
	}
}
