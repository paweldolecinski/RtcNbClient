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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Patryk Żywica
 */
public class RtcContentUtilities {

    /**
     *
     * @param content
     * @return
     * @throws IOException
     */
    public static File convertToFile(RtcContent content) throws IOException {
        File file;
        String name = content.getName();
        if (name.indexOf('.') == -1) {
            String ext = guessFileExtension(content.getContentType());
            if (ext != null) {
                name += "." + ext; //$NON-NLS-1$
            }
        }
        name = name.replace(':', '_').replace('/', '_').replace('\\', '_');
        file = File.createTempFile("tmp", '_' + name); //$NON-NLS-1$

        file.deleteOnExit();
        content.getContentStream(file);//
        //
        file.setReadOnly();
        return file;
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static RtcContent convertToContent(File file) throws IOException {

        
        return null;

    }

    /**
     * 
     * @param content
     * @return
     */
    public static RtcContent convertToContent(String content) {
        //TODO : dolek :implement this
        throw new UnsupportedOperationException();
    }

    private RtcContentUtilities() {
    }

    private static String guessFileExtension(String mimeType) {

        if (mimeType == null) {
            return null;
        }

        if ("text/plain".equals(mimeType)) //$NON-NLS-1$
        {
            return "txt"; //$NON-NLS-1$
        }
        int index = mimeType.lastIndexOf('/');
        if (index == -1 || index == mimeType.length() - 1) {
            return null;
        }

        return mimeType.substring(index + 1);
    }
}
