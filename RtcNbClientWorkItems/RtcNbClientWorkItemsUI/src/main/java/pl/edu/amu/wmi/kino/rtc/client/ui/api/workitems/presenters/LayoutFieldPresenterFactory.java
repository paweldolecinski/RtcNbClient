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

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;

/**
 * Factory for presenters. WorkItemPresenterManager will search for best presenter 
 * given layout field.
 * <p/>
 * Value returned from canCreate method should be interpreted as match level. Number
 * that is greater than zero, returned from this method means that this factory is
 * able to create presenter for given field. WorkItemPresenterManager will collect all
 * results and factory with highest match level will be used to create presenter.
 * <p/>
 * All buildin presenters returns values between 1.0 (match) and 0.0 (no match).
 * <p/>
 * Instances should be registered in <code>Rtc/Modules/WorkItems/Editor/Presenter</code> in layer.xml.
 * 
 * @author Patryk Żywica
 */
public interface LayoutFieldPresenterFactory<T extends Display> {

    double canCreate(WorkItemLayoutField field);

    Class<T> getDisplayType();

    Presenter<T> createPresenter(T display, WorkItemLayoutField field, RtcWorkItemWorkingCopy wiwc);
}
