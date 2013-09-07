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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.ContributorManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.Attachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.contributor.ContributorManagerImpl;

import com.ibm.team.links.common.IReference;
import com.ibm.team.repository.client.IContentManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.common.model.IAttachment;

/**
 * 
 * @author Patryk Żywica
 */
public class AttachmentImpl implements Attachment {

    private IAttachment attachment;
    private IReference reference;
    private ActiveProjectArea area;

    public AttachmentImpl(ActiveProjectArea area, IAttachment attachment,
            IReference reference) {
        assert reference != null && attachment != null;
        this.attachment = attachment;
        this.reference = reference;
        this.area = area;
    }

    AttachmentImpl(ActiveProjectArea area, IReference attachmentRef) {
        assert attachmentRef != null;
        this.area = area;
        this.reference = attachmentRef;
    }

    @Override
    public String getName() {
        if (attachment != null) {
            return attachment.getName();
        } else {
            return reference.getComment();
        }
    }

    @Override
    public File getFile() throws IOException {
        if (attachment == null) {
            return new File(reference.createURI());
        }
        try {
            return createFile(attachment);
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(AttachmentImpl.class).log(Level.WARNING,
                    "Error while fetching file", ex);
            throw new IllegalStateException();
        }
    }

    @Override
    public Date getCreationDate() {
        if (attachment == null) {
            return null;
        }
        return attachment.getCreationDate();
    }

    @Override
    public Contributor getCreator() {
        if (attachment == null) {
            return null;
        }
        ContributorManager cm = area.getLookup().lookup(
                ContributorManager.class);
        assert cm instanceof ContributorManagerImpl : "ContributorManagerImpl instance excpected";

        ContributorManagerImpl impl = (ContributorManagerImpl) cm;
        return impl.findContributor(attachment.getCreator());
    }

    @Override
    public long getSize() {
        if (attachment == null) {
            return new File(reference.createURI()).length();
        }
        return attachment.getContent().getEstimatedConvertedLength();
    }

    @Override
    public void setName(String name) {
        attachment.setName(name);
    }

    public IAttachment getIAttachment() {
        return attachment;
    }

    public IReference getIReference() {
        return reference;
    }

    private static File createFile(IAttachment attachment) throws IOException,
            TeamRepositoryException {
        ensureResolved(attachment);
        File file;
        String name = attachment.getName();
        if (name.indexOf('.') == -1) {
            String ext = guessFileExtension(attachment.getContent().getContentType());
            if (ext != null) {
                name += "." + ext; //$NON-NLS-1$
            }
        }
        name = name.replace(':', '_').replace('/', '_').replace('\\', '_');
        file = File.createTempFile("tmp", '_' + name); //$NON-NLS-1$
        file.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            IContentManager contentManager = ((ITeamRepository) attachment.getOrigin()).contentManager();
            contentManager.retrieveContent(attachment.getContent(),
                    outputStream, null);
        } finally {
            outputStream.close();
        }
        file.setReadOnly();
        return file;
    }

    private static void ensureResolved(IAttachment attachment)
            throws TeamRepositoryException {
        if (attachment.isPropertySet(IAttachment.CONTENT_PROPERTY)
                && attachment.isPropertySet(IAttachment.NAME_PROPERTY)) {
            return;
        }
        IAuditableClient auditableClient = (IAuditableClient) ((ITeamRepository) attachment.getOrigin()).getClientLibrary(IAuditableClient.class);
        attachment = auditableClient.resolveAuditable(attachment,
                IAttachment.DEFAULT_PROFILE, null);
    }

    private static String guessFileExtension(String mimeType) {

        if (mimeType == null) {
            return null;
        }

        if ("text/plain".equals(mimeType)) // $NONI18N
        {
            return "txt"; // $NONI18N
        }
        int index = mimeType.lastIndexOf('/');
        if (index == -1 || index == mimeType.length() - 1) {
            return null;
        }

        return mimeType.substring(index + 1);
    }
}
