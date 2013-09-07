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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.pages;

import com.ibm.team.apt.internal.common.wiki.IWikiPageAttachment;
import com.ibm.team.repository.client.IContentManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IContent;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContent;

/**
 *
 * @author Pawel Dolecinski
 */
public class RtcWikiContentImpl extends RtcContent {

    private final IWikiPageAttachment attachment;
    private final IContent content;

    RtcWikiContentImpl(IWikiPageAttachment attachment, IContent content) {
        this.attachment = attachment;
        this.content = content;

    }

    /**
     *
     * @param file
     * @return
     */
    @Override
    public FileOutputStream getContentStream(File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            IContentManager contentManager = ((ITeamRepository) attachment.getOrigin()).contentManager();
            contentManager.retrieveContent(content, outputStream, null);
        } catch (TeamRepositoryException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcWikiContentImpl.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } catch (FileNotFoundException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcWikiContentImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                //Exceptions.printStackTrace(ex);
                RtcLogger.getLogger(RtcWikiContentImpl.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
            }
        }
        return outputStream;

    }

    @Override
    public void writeContentIn(InputStream stream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLength() {
        return this.content.getRawLength();
    }

    @Override
    public String getContentType() {
        return content.getContentType();
    }

    @Override
    public String getCharacterEncoding() {
        return content.getCharacterEncoding();
    }

    @Override
    public String getLineDelimiter() {
        return content.getLineDelimiter().toString();
    }

    @Override
    public String getName()
    {
        return attachment.getName();
    }
}
