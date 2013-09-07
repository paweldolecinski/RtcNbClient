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
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @since 0.2.1.4
 * @author Patryk Żywica
 */
public abstract class RtcContent {
//TODO : dokumentacja skopiowana z RTC plain java API

    /**
     *
     * @param file
     * @return
     */
    public abstract FileOutputStream getContentStream(File file);

    /**
     *
     * @param stream
     */
    public abstract void writeContentIn(InputStream stream);

    /**
     * 
     * @return
     */
    public abstract long getLength();

    /**
     * Returns the content type identifier that describes the nature of the
     * content. The identifier format follows the Internet Media Types
     * specification for media types (e.g. text/plain, application/unknown) as
     * defined in s RFC 2046.
     * <p>
     * The encoding will be specified separately by {@link #getCharacterEncoding()};
     *
     * @return the content type identifier that describes the nature of the
     *         content; never <code>null</code>
     * @ShortOp This is a short operation; it may block only momentarily; safe
     *          to call from a responsive thread.
     */
    public abstract String getContentType();

    /**
     * Returns the character encoding for the content. This is an IANA character
     * set encoding name such as "8859_1", "us-ascii", "iso-8859-1", "UTF8", or
     * "shift_jis". The official registry of "charset" (i.e., character
     * encoding) names, with references to documents defining their meanings, is
     * kept by IANA at http://www.iana.org/assignments/character-sets.
     *
     * @return the character encoding for this content or <code>null</code> if
     *         unknown or not applicable to this content type.
     * @ShortOp This is a short operation; it may block only momentarily; safe
     *          to call from a responsive thread.
     */
    public abstract String getCharacterEncoding();

    /**
     * The preferred line delimiter to be used when the content is retrieved.
     * Valid values include {@link #LINE_DELIMITER_PLATFORM}
     * {@link #LINE_DELIMITER_NONE}, {@link #LINE_DELIMITER_LF},
     * {@link #LINE_DELIMITER_CRLF}, {@link #LINE_DELIMITER_CR}
     *
     * @return 
     * @returns the line delimiter for the content.
     * The default is {@link #LINE_DELIMITER_NONE}
     */
    public abstract String getLineDelimiter();

    /**
     * 
     * @return
     */
    public abstract String getName();
}
