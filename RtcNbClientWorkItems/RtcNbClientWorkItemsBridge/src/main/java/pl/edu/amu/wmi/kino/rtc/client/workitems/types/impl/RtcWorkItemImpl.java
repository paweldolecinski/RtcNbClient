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
package pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.openide.util.Exceptions;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.TeamArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProjectAreaImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcAttribute;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItemsManager;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcAttachment;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcCategory;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlow;
import pl.edu.amu.wmi.kino.rtc.client.workitems.workflow.RtcWorkFlowAction;

import com.ibm.team.links.common.IItemReference;
import com.ibm.team.links.common.IReference;
import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.process.common.advice.TeamOperationCanceledException;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.repository.common.ItemNotFoundException;
import com.ibm.team.repository.common.PermissionDeniedException;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IDetailedStatus;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.IWorkingCopyListener;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.client.internal.UnmergableChangesException;
import com.ibm.team.workitem.client.internal.WorkItemWorkingCopyImpl;
import com.ibm.team.workitem.client.internal.WorkItemWorkingCopyManager;
import com.ibm.team.workitem.common.internal.util.DelegatingItemList;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttachment;
import com.ibm.team.workitem.common.model.IAttachmentHandle;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IDeliverableHandle;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.model.WorkItemEndPoints;

/**
 * 
 * @author Pawel Dolecinski
 */
@Deprecated
public class RtcWorkItemImpl extends RtcWorkItem {

	private IWorkItem wi;
	private WorkItemWorkingCopy workingCopy;
	private RtcWorkItemTypeImpl type;
	private WorkItemWorkingCopyManager copyManager;
	private transient List<IWorkingCopyListener> listeners = new ArrayList<IWorkingCopyListener>();
	private String name;
	private String description;
	private RtcCategory category;
	private Contributor owner;
	private final ActiveProjectArea area;

	/**
	 * 
	 * @param wi
	 */
	public RtcWorkItemImpl(IWorkItem wi, ActiveProjectArea area) {
		this.wi = wi;
		this.name = wi.getHTMLSummary().getXMLText();
		this.description = wi.getHTMLDescription().getXMLText();
		this.area = area;
	}

	/**
	 * 
	 * @param teamArea
	 * @param area
	 * @param type
	 * @param clientLibrary
	 */
	public RtcWorkItemImpl(ProcessArea teamArea, RtcWorkItemType type,
			IWorkItemClient clientLibrary, ActiveProjectArea area) {
		try {
			if (teamArea instanceof TeamArea) {
				// ITeamArea team = ((RtcTeamAreaImpl) teamArea).getTeamArea();
				// IProjectAreaHandle projectArea = team.getProjectArea();
			} else if (teamArea instanceof ProjectAreaImpl) {
				IProjectAreaHandle projectArea = ((ProjectAreaImpl) teamArea)
						.getIProcessArea();
				IWorkItemType workItemType = clientLibrary.findWorkItemType(
						projectArea, type.getId(), null);
				IWorkItemHandle handle = clientLibrary
						.getWorkItemWorkingCopyManager().connectNew(
								workItemType, null);
				this.workingCopy = clientLibrary
						.getWorkItemWorkingCopyManager().getWorkingCopy(handle);
				this.wi = workingCopy.getWorkItem();
				this.name = wi.getHTMLSummary().getXMLText();
				this.description = wi.getHTMLDescription().getXMLText();
				// this.owner = new RtcContributorImpl(wi.getOwner());
			}
		} catch (TeamRepositoryException ex) {
			Exceptions.printStackTrace(ex);
		}
		this.area = area;

	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public void setDisplayName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Contributor getOwner() {
		return owner;
	}

	@Override
	public void setOwner(Contributor owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Image getIcon() {
		if (type == null) {
			type = new RtcWorkItemTypeImpl(wi);
		}
		return type.getIcon();
	}

	/**
	 * 
	 * @return
	 */
	public IWorkItem getWorkItem() {
		return wi;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getId() {
		return wi.getId();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public RtcWorkItemType getType() {
		if (type == null) {
			type = new RtcWorkItemTypeImpl(wi);
		}
		return type;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public RtcCategory getCategory() {
		if (category == null) {
			// category = new RtcCategoryImpl(wi.getCategory());
		}
		return category;
	}

	/**
	 * 
	 * @param category
	 */
	@Override
	public void setCategory(RtcCategory category) {
		this.category = category;
		wi.setCategory((ICategoryHandle) ((RtcCategoryImpl) category)
				.getCategory().getItemHandle());
	}

	@Override
	public RtcWorkItem[] getChildren() {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		List<RtcWorkItem> res = new ArrayList<RtcWorkItem>();
		IWorkItemReferences references = workingCopy.getReferences();
		List<IReference> list;
		RtcWorkItemsManager manager = area.getLookup().lookup(
				RtcWorkItemsManager.class);
		list = references.getReferences(WorkItemEndPoints.CHILD_WORK_ITEMS);
		IWorkItem child = null;
		for (IReference iReference : list) {
			IItemHandle resolve = (IItemHandle) iReference.resolve();
			if (resolve.hasFullState()) {
				child = (IWorkItem) resolve.getFullState();
			} else {
				try {
					child = (IWorkItem) ((ITeamRepository) resolve.getOrigin())
							.itemManager().fetchCompleteItem(resolve,
									IItemManager.DEFAULT, null);
				} catch (TeamRepositoryException ex) {
					RtcLogger.getLogger().log(Level.SEVERE,
							ex.getLocalizedMessage());
				}
			}
			if (child != null) {
				res.add(manager.findWorkItem(child.getId()));
			}
		}

		return res.toArray(new RtcWorkItem[] {});
	}

	@Override
	public RtcWorkItem getParent() {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		RtcWorkItem res = null;
		IWorkItemReferences references = workingCopy.getReferences();
		List<IReference> list;
		RtcWorkItemsManager manager = area.getLookup().lookup(
				RtcWorkItemsManager.class);
		list = references.getReferences(WorkItemEndPoints.PARENT_WORK_ITEM);
		IWorkItem child = null;
		for (IReference iReference : list) {
			IItemHandle resolve = (IItemHandle) iReference.resolve();
			if (resolve.hasFullState()) {
				child = (IWorkItem) resolve.getFullState();
			} else {
				try {
					child = (IWorkItem) ((ITeamRepository) resolve.getOrigin())
							.itemManager().fetchCompleteItem(resolve,
									IItemManager.DEFAULT, null);
				} catch (TeamRepositoryException ex) {
					RtcLogger.getLogger().log(Level.SEVERE,
							ex.getLocalizedMessage());
				}
			}
			if (child != null) {
				res = manager.findWorkItem(child.getId());
			}
		}
		return res;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isDirty() {
		if (workingCopy == null) {
			return false;
		}
		return workingCopy.isDirty();
	}

	/**
     *
     */
	@Override
	public void refresh() {
		copyManager.refresh((IWorkItemHandle) wi.getItemHandle());
	}

	/**
     *
     */
	@Override
	public void merge() {
		try {
			copyManager
					.mergeWithCurrent(workingCopy.getWorkItem(), false, null);
			// thrown an exception if changes are to complicated to be merged
			// automaticly
		} catch (UnmergableChangesException e) {
			try {
				// so we can overide our changes with data from server
				copyManager.mergeWithCurrent(workingCopy.getWorkItem(), true,
						null);
			} catch (TeamRepositoryException ex) {
				Exceptions.printStackTrace(ex);
			}
		} catch (TeamRepositoryException ex) {
			Exceptions.printStackTrace(ex);
		}
	}

	/**
	 * 
	 * @throws pl.edu.amu.wmi.kino.rtc.client.workitems.RtcWorkItem.RtcWorkItemSavingException
	 */
	@Override
	public void save() throws RtcWorkItemSavingException {
		// RTC API sucks - and no more comments need here
		if (workingCopy != null && workingCopy.isDirty()) {
			try {
				IDetailedStatus save = workingCopy.save(null); // try to save
																// work item
				if (!save.isOK()) {

					if (workingCopy.isStale()) { // if our WI is stale we should
													// update it
						throw new RtcWorkItemSavingException(save.getMessage());
						// //TODO: create a nice dialog with question about
						// merging changes;./
						// Object[] options =
						// {NbBundle.getMessage(WorkItemNode.class,
						// "ChangesOnServerCancelButton.name"),
						// NbBundle.getMessage(WorkItemNode.class,
						// "ChangesOnServerDropChangesButton.name"),
						// NbBundle.getMessage(WorkItemNode.class,
						// "ChangesOnServerMergeButton.name")};
						// int res = JOptionPane.showOptionDialog(null,
						// NbBundle.getMessage(WorkItemNode.class,
						// "ChangesOnServerMerge.name"),
						// NbBundle.getMessage(WorkItemNode.class,
						// "ChangesOnServerMergeTitle.name"),
						// JOptionPane.YES_NO_CANCEL_OPTION,
						// JOptionPane.WARNING_MESSAGE, null, options,
						// options[0]);
						// if (res == 2) {
						// //TODO: michu: this should be also in refresh action
						// in work item editor
						// // 1st we try to merge all modification
						// try {
						// copyManager.mergeWithCurrent(wc.getWorkItem(), false,
						// null);
						// //thrown an exception if changes are to complicated
						// to be merged automaticly
						// } catch (UnmergableChangesException e) {
						// // so we can overide our changes with data from
						// server
						// //Here should be a question if we want to lose our
						// unmergable data
						// int result = JOptionPane.showConfirmDialog(null,
						// NbBundle.getMessage(WorkItemNode.class,
						// "Unmergable.name"),
						// NbBundle.getMessage(WorkItemNode.class,
						// "UnmergableTitle.name"),
						// JOptionPane.OK_CANCEL_OPTION,
						// JOptionPane.QUESTION_MESSAGE);
						// if (result == JOptionPane.OK_OPTION) {
						// copyManager.mergeWithCurrent(workingCopy.getWorkItem(),
						// true, null);
						// }
						// }
						// } else if (res == 1) {
						// copyManager.mergeWithCurrent(workingCopy.getWorkItem(),
						// true, null);
						// }
						// if (res == 2 || res == 1) {
						// save = workingCopy.save(null); //and after update
						// when our WI isn't stale we can again try to save
						// if (!save.isOK()) {
						// throw new
						// TeamOperationCanceledException(save.getOperationReport(),
						// save.getMessage()); //exception during saving
						//
						// } else {
						// copyManager.disconnect(workingCopy.getWorkItem());
						// ((WorkItemWorkingCopyImpl) workingCopy).saved(save);
						// workingCopy = null;
						// }
						// }
					} else {
						throw new TeamOperationCanceledException(
								save.getOperationReport(), save.getMessage()); // exception
																				// during
																				// saving,
																				// e.g
																				// permission
																				// denide
					}

				} else {
					copyManager.disconnect(workingCopy.getWorkItem());
					((WorkItemWorkingCopyImpl) workingCopy).saved(save);
					workingCopy = null;
				}
				// ((WorkItemWorkingCopyImpl)workingCopy).saved(save); // after
				// everything this method make some cleanup and fire events:
				// SAVED or SAVE_CANCELED

			} catch (TeamOperationCanceledException ex) {
				throw new RtcWorkItemSavingException(ex.getLocalizedMessage());
				// JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param attribute
	 * @param value
	 */
	@Override
	public void setValue(RtcAttribute attribute, Object value) {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		// This ugly code is becouse of RTC API specyfication.
		// In RTC value for setter often isn't the same value which we have from
		// getter.
		// It depends on kind of attribute (built in or not build in) and on
		// type:
		// enumeric, item, list of items or common types like string, int,
		// timestamp.
		IAttribute attr = attribute.getLookup().lookup(IAttribute.class);
		if (wi.hasAttribute(attr) && wi.isWorkingCopy()) {
			if (value instanceof Identifier && wi.hasBuiltInAttribute(attr)) {
				@SuppressWarnings("unchecked")
				String iden = ((Identifier<ILiteral>) value)
						.getStringIdentifier();
				wi.setValue(attr, iden);

			} else if (value instanceof Identifier
					&& !wi.hasBuiltInAttribute(attr)) {
				wi.setValue(attr, (Identifier) value);
			} else if (value instanceof IItemHandle) {
				IItemHandle handle = ((IItemHandle) value);
				wi.setValue(attr, handle);
			} else if (value instanceof RtcWorkItemType) {
				wi.setValue(attr, ((RtcWorkItemType) value).getId());
			} else if (value instanceof RtcDurationImpl) {
				wi.setValue(attr,
						((RtcDurationImpl) value).getDurationInMillis());
			} else if (value instanceof RtcLiteralImpl) {
				wi.setValue(attr, ((RtcLiteralImpl) value).getLiteral()
						.getIdentifier2());
			} else {
				wi.setValue(attr, value);
			}
		}
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(RtcAttribute attribute) {
		Object ret = null;
		IAttribute attr = attribute.getLookup().lookup(IAttribute.class);
		if (attr == null) {
			return null;
		}
		Object value = wi.getValue(attr);

		if (AttributeTypes.DURATION.equals(attr.getAttributeType())) {
			ret = new RtcDurationImpl(((Long) value));
		} // Enums
		else if (AttributeTypes.TYPE.equals(attr.getAttributeType())) {
			ret = new RtcWorkItemTypeImpl(wi);
		} else if (value instanceof ICategoryHandle) {
			// ret = new RtcCategoryImpl((ICategoryHandle) value);
		} else if (value instanceof Identifier) {
			Identifier identifier = (Identifier) value;
			RtcWorkItemAttributePossibleValues lookup = attribute.getLookup()
					.lookup(RtcWorkItemAttributePossibleValues.class);
			if (lookup == null) {
				return null;
			}
			List<RtcLiteral> possibleValues = lookup.getPossibleValues();

			for (RtcLiteral iLiteral : possibleValues) {
				if (iLiteral.getId().equals((identifier).getStringIdentifier())) {
					ret = iLiteral;
				}
			}
		} // Deliverable
		else if (value instanceof IDeliverableHandle) {
			ret = new RtcDeliverableImpl((IDeliverableHandle) value);
		} // Iteration
		else if (value instanceof IIterationHandle) {
//			ret = new RtcIterationImpl((IIterationHandle) value);
		} else if (value instanceof IContributorHandle) {
			// ret = new RtcContributorImpl((IContributorHandle) value);
		} // Items types like contributors, project and team areas, etc.
		else if (value instanceof DelegatingItemList) {
			List res = new ArrayList<IItemHandle>();
			DelegatingItemList<IItemHandle> list = (DelegatingItemList) wi
					.getValue(attr);
			for (IItemHandle object : list) {
				if (object instanceof IContributorHandle) {
					// res.add(new RtcContributorImpl((IContributorHandle)
					// object));

				}
			}
			ret = res;
		} // Standard types like int, string, timestamp, etc.
		else if (value != null) {
			ret = value;
		}

		return ret;
	}

	/**
	 * 
	 * @param listener
	 */
	public void addWorkingCopyListener(IWorkingCopyListener listener) {
		if (workingCopy == null) {
			listeners.add(listener);
		} else {
			workingCopy.addWorkingCopyListener(listener);
		}
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public RtcWorkFlow getWorkFlowAction() {
		return new RtcWorkFlowStateImpl(new RtcWorkFlowInfoImpl(wi),
				wi.getState2());
	}

	/**
	 * 
	 * @param action
	 */
	@Override
	public void setWorkFlowAction(RtcWorkFlowAction action) {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		workingCopy.setWorkflowAction(((RtcWorkFlowActionImpl) action)
				.getIdentifier().getStringIdentifier());
	}

	/**
	 * 
	 * @param rtcWorkFlowResolution
	 */
	public void setResolution(RtcWorkFlowResolutionImpl rtcWorkFlowResolution) {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		wi.setResolution2(rtcWorkFlowResolution.getIdentifier());
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public List<RtcAttachment> getAttachments() {
		if (workingCopy == null || !wi.isWorkingCopy()) {
			getWorkingCopy();
		}
		IWorkItemReferences references = workingCopy.getReferences();
		List<RtcAttachment> list = new ArrayList<RtcAttachment>();
		try {
			list = resolveAllAttachments(references
					.getReferences(WorkItemEndPoints.ATTACHMENT));
		} catch (TeamRepositoryException ex) {
			Exceptions.printStackTrace(ex);
		}

		return list;
	}

	private List<RtcAttachment> resolveAllAttachments(
			List<IReference> references) throws TeamRepositoryException {
		List<RtcAttachment> list = new ArrayList<RtcAttachment>(
				references.size());
		for (IReference attachmentRef : references) {
			if (attachmentRef.isItemReference()) {
				IItemHandle referencedItem = ((IItemReference) attachmentRef)
						.getReferencedItem();
				if (referencedItem instanceof IAttachmentHandle) {
					try {
						IAttachment attachment = getAuditableClient()
								.resolveAuditable(
										(IAttachmentHandle) referencedItem,
										IAttachment.FULL_PROFILE, null);
						// list.add(new RtcAttachmentImpl(attachment,
						// attachmentRef));
					} catch (TeamRepositoryException e) {
						if (e instanceof ItemNotFoundException
								|| e instanceof PermissionDeniedException) {
							continue; // Ignore those attachments that got
										// deleted or hidden
						}
						throw e;
					}
				}
			} else if (attachmentRef.isURIReference()) {
				// RtcAttachmentImpl a = new RtcAttachmentImpl(null,
				// attachmentRef);

				// list.add(new RtcAttachmentImpl(attachmentRef));
			}
		}

		return list;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		copyManager.disconnect(workingCopy.getWorkItem());
	}

	/**
	 * 
	 * @return
	 */
	protected IAuditableClient getAuditableClient() {
		ITeamRepository repository = getRepository();
		return (IAuditableClient) repository
				.getClientLibrary(IAuditableClient.class);
	}

	private void getWorkingCopy() {
		try {
			IWorkItemClient wiclient = (IWorkItemClient) getRepository()
					.getClientLibrary(IWorkItemClient.class);
			// first I have to connect to wi and then i can get working copy.
			copyManager = (WorkItemWorkingCopyManager) wiclient
					.getWorkItemWorkingCopyManager();
			copyManager.connect(wi, IWorkItem.FULL_PROFILE, null);
			workingCopy = wiclient.getWorkItemWorkingCopyManager()
					.getWorkingCopy(wi);
			for (IWorkingCopyListener l : listeners) {
				workingCopy.addWorkingCopyListener(l);
			}
			listeners.clear();
			this.wi = workingCopy.getWorkItem();
		} catch (TeamRepositoryException ex) {
			RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
		}
	}

	private ITeamRepository getRepository() {
		return (ITeamRepository) wi.getOrigin();
	}
}
