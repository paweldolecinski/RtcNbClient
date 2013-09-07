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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.storage;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Patryk Żywica
 */
public class RtcBasicConnectionInformationsFactory {

    private static Map<RtcBasicConnectionInformationImpl, RtcRepositoryConnectionImpl> toRC =
            new HashMap<RtcBasicConnectionInformationImpl, RtcRepositoryConnectionImpl>();
    private static Map<RtcRepositoryConnectionImpl, RtcBasicConnectionInformationImpl> toInfo =
            new HashMap<RtcRepositoryConnectionImpl, RtcBasicConnectionInformationImpl>();

    public static RtcBasicConnectionInformationImpl getBasicInformation(RtcRepositoryConnectionImpl rc) {
        if(!toInfo.containsKey(rc)){
            RtcBasicConnectionInformationImpl info = new RtcBasicConnectionInformationImpl(rc);
            toInfo.put(rc, info);
            toRC.put(info, rc);
        }
        return toInfo.get(rc);
    }

    public static RtcRepositoryConnectionImpl getRepositoryConnection(RtcBasicConnectionInformationImpl basicInfo) {
        if(!toRC.containsKey(basicInfo)){
            RtcRepositoryConnectionImpl rc = new RtcRepositoryConnectionImpl(basicInfo);
            toRC.put(basicInfo,rc);
            toInfo.put(rc,basicInfo);
        }
        return toRC.get(basicInfo);
    }
}
