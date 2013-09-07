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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.monitor;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 *
 * @author Pawel Dolecinski
 */
public class ProgressMonitor implements IProgressMonitor {

    /**
     * Do nothing.
     * 
     * @param name
     * @param totalWork
     */
    @Override
    public void beginTask(String name, int totalWork) {
    }

    /**
     * Do nothing.
     *
     */
    @Override
    public void done() {
    }

    /**
     * Do nothing.
     *
     * @param work
     */
    @Override
    public void internalWorked(double work) {
    }

    /**
     *
     * @return always false
     */
    @Override
    public boolean isCanceled() {
        return false;
    }

    /**
     * Do nothing.
     * @param value
     */
    @Override
    public void setCanceled(boolean value) {
    }

    /**
     * Do nothing.
     *
     * @param name
     */
    @Override
    public void setTaskName(String name) {
    }

    /**
     * Do nothing.
     *
     * @param name
     */
    @Override
    public void subTask(String name) {
    }

    /**
     * Do nothing.
     * @param work
     */
    @Override
    public void worked(int work) {
    }
}
