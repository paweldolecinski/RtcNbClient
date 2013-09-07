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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;

/**
 *
 * @author Patryk Żywica
 */
public interface Attachment {

    /**
     *
     * @return a file name
     */
    public String getName();

    /**
     *
     * @return a file created temporary on local machine
     * @throws IOException if file creating failed
     */
    public File getFile() throws IOException;

    /**
     *
     * @return attachment upload date
     */
    public Date getCreationDate();

    /**
     *
     * @return who upload attachment
     */
    public Contributor getCreator();

    /**
     *
     * @return size of attached file in bytes
     */
    public long getSize();

    /**
     * Setting new name of attachment
     * @param name which is a new name for attachment
     */
    public void setName(String name);
}
