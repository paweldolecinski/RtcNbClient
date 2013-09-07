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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters;

import java.awt.Image;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;

/**
 *
 * @author Patryk Żywica
 */
public interface WorkItemHeaderPresenter
        extends Presenter<WorkItemHeaderPresenter.HeaderDisplay> {

    public static interface HeaderDisplay extends Display {

        void setSummary(String summary);

        void setId(String id);
        
        void setIcon(Image icon);
        
        <T> void addHandler(InputHandler.Input<T> input, InputHandler<T> h);
        
        <T> void removeHandler(InputHandler.Input<T> input, InputHandler<T> handler);
    }
    
    public static final InputHandler.Input<String> SUMMARY_INPUT = new InputHandler.Input<String>();
}
