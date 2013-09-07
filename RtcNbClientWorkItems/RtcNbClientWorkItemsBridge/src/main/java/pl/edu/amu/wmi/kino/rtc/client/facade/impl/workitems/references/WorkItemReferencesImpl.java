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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.references;

import com.ibm.team.links.common.ILink;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.Attachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.WorkItemReferences;

import com.ibm.team.links.common.IItemReference;
import com.ibm.team.links.common.IReference;
import com.ibm.team.links.common.factory.IReferenceFactory;
import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.repository.common.ItemNotFoundException;
import com.ibm.team.repository.common.PermissionDeniedException;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IAttachment;
import com.ibm.team.workitem.common.model.IAttachmentHandle;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.WorkItemEndPoints;

/**
 * 
 * @author Patryk Żywica
 */
public class WorkItemReferencesImpl implements WorkItemReferences {

    private IWorkItemReferences references;
    private IAuditableClient auditableClient;
    private List<AttachmentImpl> attachments = new LinkedList<AttachmentImpl>();
    private ActiveProjectArea area;
    private boolean resolved = false;

    public WorkItemReferencesImpl(ActiveProjectArea area, WorkItemWorkingCopy wi) {
        auditableClient = (IAuditableClient) wi.getTeamRepository().getClientLibrary(IAuditableClient.class);
        references = wi.getReferences();
        this.area = area;
    }

    /**
     * This method gets File object from attachment, then this file is converted
     * to rtc IReference. This is a common behaviour in RTC.
     *
     * @param file
     * @throws IOException
     *             if getting file from attachment failed
     */
    @Override
    public void addAttachment(File file) throws IOException {
        if (file != null && file.exists() && file.isFile()) {
            String name = file.getName();

            IReference reference = IReferenceFactory.INSTANCE.createReferenceFromURI(file.toURI(), file.getName(), "", name.substring(name.lastIndexOf(".") + 1));
            references.add(WorkItemEndPoints.ATTACHMENT, reference);
            resolved = false;
        }
    }

    @Override
    public void addAttachment(Attachment attachment) {
        if (attachment instanceof AttachmentImpl) {
            attachments.add((AttachmentImpl) attachment);
        }
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (attachment instanceof AttachmentImpl) {
            IReference ref = ((AttachmentImpl) attachment).getIReference();
            references.remove(ref);
            resolved = false;
        }
    }

    @Override
    public Attachment[] getAttachments() {
        if (!resolved) {
            try {
                attachments = resolveAllAttachments(references.getReferences(WorkItemEndPoints.ATTACHMENT));
                resolved = true;
            } catch (TeamRepositoryException ex) {
                RtcLogger.getLogger(WorkItemReferencesImpl.class).log(
                        Level.WARNING, "Fetching attachments error", ex);
                attachments = new LinkedList<AttachmentImpl>();
            }
        }
        return attachments.toArray(new AttachmentImpl[]{});
    }

    private List<AttachmentImpl> resolveAllAttachments(
            List<IReference> references) throws TeamRepositoryException {
        List<AttachmentImpl> list = new ArrayList<AttachmentImpl>(
                references.size());
        for (IReference attachmentRef : references) {
            if (attachmentRef.isItemReference()) {
                IItemHandle referencedItem = ((IItemReference) attachmentRef).getReferencedItem();
                if (referencedItem instanceof IAttachmentHandle) {
                    try {
                        IAttachment attachment = auditableClient.resolveAuditable(
                                (IAttachmentHandle) referencedItem,
                                IAttachment.FULL_PROFILE, null);
                        list.add(new AttachmentImpl(area, attachment,
                                attachmentRef));
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
                ILink link = attachmentRef.getLink();
                URI createURI = attachmentRef.createURI();
                String extraInfo = attachmentRef.getExtraInfo();
                Object resolve = attachmentRef.resolve();
                list.add(new AttachmentImpl(area,
                        attachmentRef));
            }
        }
        return list;
    }
}
