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
package pl.edu.amu.wmi.kino.rtc.client.impl;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openide.util.Lookup;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.PlansSuite;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProjectArea;

/**
 *
 * @author Pawel Dolecinski
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({pl.edu.amu.wmi.kino.rtc.client.impl.plans.PlansSuite.class})
public class ImplSuite {

    ActiveProjectArea area = new ActiveProjectArea() {

        @Override
        public Lookup getLookup() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ProjectArea getProjectArea() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getID() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RtcRepositoryConnection getRepositoryConnection() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PlansSuite.class);
    }
}
