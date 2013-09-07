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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.presenter.tabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.Pair;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.Attachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.AttachmentsMarker;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.references.WorkItemReferences;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.AttachmentsDisplay;

/**
 * 
 * @author Paweł Doleciński
 * @author Bartosz Zaleski
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class WorkItemAttachmentsPresenterFactory implements
        LayoutFieldPresenterFactory<AttachmentsDisplay> {

    @Override
    public double canCreate(WorkItemLayoutField field) {
        if (field.getType().equals(WorkItemLayoutField.Type.NON_ATTRIBUTE)
                && field.getLookup().lookup(AttachmentsMarker.class) != null) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    public Class<AttachmentsDisplay> getDisplayType() {
        return AttachmentsDisplay.class;
    }

    @Override
    public Presenter<AttachmentsDisplay> createPresenter(
            AttachmentsDisplay display, WorkItemLayoutField field,
            RtcWorkItemWorkingCopy wiwc) {
        return new AttachmentsPresenter(display, wiwc);
    }

    private static class AttachmentsPresenter extends BasicPresenter<AttachmentsDisplay> {

        private WorkItemReferences references;
        private List<Attachment> attachments;

        public AttachmentsPresenter(AttachmentsDisplay display,
                RtcWorkItemWorkingCopy wiwc) {
            super(display);
            references = wiwc.getReferences();
        }

        @Override
        protected void onBind() {
            final Attachment[] atts = references.getAttachments();
            List<Attachment> a = Arrays.asList(atts);
            attachments = new ArrayList<Attachment>(a);
            registerHandler(getDisplay().addInputHandler(
                    AttachmentsDisplay.NEW_FILE_PATH,
                    new InputHandler<String>() {

                        @Override
                        public void valueEntered(String value) {
                            File file = new File(value);
                            try {
                                references.addAttachment(file);
                                final Attachment[] atts = references.getAttachments();
                                List<Attachment> a = Arrays.asList(atts);
                                attachments = new ArrayList<Attachment>(a);
                                provideAttachments(attachments);
                            } catch (IOException e) {
                                RtcLogger.getLogger(AttachmentsPresenter.class).warning("Error while adding file.");
                            }

                        }
                    }));
            registerHandler(getDisplay().addInputHandler(
                    AttachmentsDisplay.SAVE_INPUT,
                    new InputHandler<Pair<Integer, String>>() {

                        @Override
                        public void valueEntered(Pair<Integer, String> value) {
                            try {
                                File f1 = attachments.get(value.getFirst()).getFile();
                                File f2 = new File(value.getSecond());
                                InputStream in = new FileInputStream(f1);
                                OutputStream out = new FileOutputStream(f2);
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                in.close();
                                out.close();
                            } catch (IOException e) {
                                RtcLogger.getLogger(AttachmentsPresenter.class).warning("Error while saving file.");
                            }

                        }
                    }));
            registerHandler(getDisplay().addInputHandler(
                    AttachmentsDisplay.RENAME_INPUT,
                    new InputHandler<Pair<Integer, String>>() {

                        @Override
                        public void valueEntered(Pair<Integer, String> value) {
                            // TODO Auto-generated method stub
                        }
                    }));
            registerHandler(getDisplay().addOptionHandler(
                    AttachmentsDisplay.OPEN_OPTION, new OptionChooseHandler() {

                @Override
                public void optionChosen(int option) {
                    try {
                        getDisplay().openFile(attachments.get(option).getFile());
                    } catch (IOException ex) {
                        RtcLogger.getLogger(AttachmentsPresenter.class).warning("Error while opening file.");
                    }
                }
            }));
            registerHandler(getDisplay().addOptionHandler(
                    AttachmentsDisplay.REMOVE_OPTION,
                    new OptionChooseHandler() {

                        @Override
                        public void optionChosen(int option) {
                            attachments.remove(option);
                            provideAttachments(attachments);
                        }
                    }));
            provideAttachments(attachments);
        }

        private void provideAttachments(final List<Attachment> atts) {
            String[] names = new String[atts.size()];
            Date[] creationDate = new Date[atts.size()];
            String[] createdBy = new String[atts.size()];
            long[] size = new long[atts.size()];
            String[] type = new String[atts.size()];

            for (int i = 0; i < atts.size(); i++) {
                names[i] = atts.get(i).getName();
                creationDate[i] = atts.get(i).getCreationDate();
                Contributor creator = atts.get(i).getCreator();
                if (creator != null) {
                    createdBy[i] = atts.get(i).getCreator().getName();
                } else {
                    createdBy[i] = "";
                }
                size[i] = atts.get(i).getSize();
                type[i] = atts.get(i).getName();
            }
            getDisplay().setAttachments(names, creationDate, createdBy, size,
                    type);
        }

        @Override
        protected void onUnbind() {
        }

        @Override
        public void refreshDisplay() {
        }
    }
}
