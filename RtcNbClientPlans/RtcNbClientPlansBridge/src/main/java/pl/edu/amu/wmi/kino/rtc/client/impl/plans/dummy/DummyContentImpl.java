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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.content.RtcContent;

/**
 *
 * @author Patryk Żywica
 */
class DummyContentImpl extends RtcContent {

    public DummyContentImpl() {
    }

    @Override
    public FileOutputStream getContentStream(File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLineDelimiter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeContentIn(InputStream stream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
