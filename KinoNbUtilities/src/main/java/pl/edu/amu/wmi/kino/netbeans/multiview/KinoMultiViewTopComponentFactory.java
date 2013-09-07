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
package pl.edu.amu.wmi.kino.netbeans.multiview;

import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import pl.edu.amu.wmi.kino.netbeans.multiview.impl.KinoMultiViewCardLayoutCloneableTopComponent;

import pl.edu.amu.wmi.kino.netbeans.multiview.impl.KinoMultiViewCardLayoutTopComponent;

/**
 * 
 * @author Patryk Żywica
 */
public class KinoMultiViewTopComponentFactory {

	public static TopComponent createTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position, String helpCtx) {
		return new KinoMultiViewCardLayoutTopComponent(groups, header,
				closeHandler, position, Lookup.EMPTY, helpCtx);
	}

	public static TopComponent createTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position) {
		return new KinoMultiViewCardLayoutTopComponent(groups, header,
				closeHandler, position, Lookup.EMPTY);
	}

	public static TopComponent createTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position, Lookup mainLookup) {
		return new KinoMultiViewCardLayoutTopComponent(groups, header,
				closeHandler, position, mainLookup);
	}

	public static TopComponent createTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position, Lookup mainLookup,
			String helpCtx) {
		return new KinoMultiViewCardLayoutTopComponent(groups, header,
				closeHandler, position, mainLookup, helpCtx);
	}

	public static TopComponent createCloneableTopComponent(
			KinoMultiViewGroup[] groups, KinoMultiViewHeader header,
			KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position) {
		return new KinoMultiViewCardLayoutCloneableTopComponent(groups, header,
				closeHandler, position);
		// return new KinoMultiViewTabbedPaneTopComponent(groups, header,
		// closeHandler, position);
	}

	private KinoMultiViewTopComponentFactory() {
	}

	public enum KinoMultiViewTopComponentTabPosition {

		TOP, BOTTOM;
	}
}
