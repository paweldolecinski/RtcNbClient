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
package pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view;

import java.io.File;
import java.util.Date;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.Pair;

/**
 * 
 * @author Bartosz Zaleski <b4lzak@gmail.com>
 * @author Patryk Żywica
 */
public interface AttachmentsDisplay extends Display {

	public static final InputHandler.Input<String> NEW_FILE_PATH = new InputHandler.Input<String>();
	public static final InputHandler.Input<Pair<Integer, String>> RENAME_INPUT = new InputHandler.Input<Pair<Integer, String>>();
	public static final InputHandler.Input<Pair<Integer, String>> SAVE_INPUT = new InputHandler.Input<Pair<Integer, String>>();
	public static final OptionChooseHandler.OptionChooser REMOVE_OPTION = new OptionChooseHandler.OptionChooser();
	public static final OptionChooseHandler.OptionChooser OPEN_OPTION = new OptionChooseHandler.OptionChooser();

	void setAttachments(String[] names, Date[] creationDate,
			String[] createdBy, long[] size, String[] type);

	void openFile(File f);

	HandlerRegistration addOptionHandler(
			OptionChooseHandler.OptionChooser source, OptionChooseHandler h);

	<T> HandlerRegistration addInputHandler(InputHandler.Input<T> input,
			InputHandler<T> h);
}
