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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.presenter.attributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.openide.util.NbBundle;

import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.DateMarker;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.DateDisplay;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class DatePresenterFactory implements
        LayoutFieldPresenterFactory<DateDisplay> {

    /*
     * (non-Javadoc)
     * 
     * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
     * LayoutFieldPresenterFactory
     * #canCreate(pl.edu.amu.wmi.kino.rtc.client
     * .facade.api.workitems.RtcWorkItemAttribute)
     */
    @Override
    public double canCreate(WorkItemLayoutField field) {
        if (field.getType().equals(WorkItemLayoutField.Type.ATTRIBUTE)) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            if (attr.getLookup().lookup(DateMarker.class) != null) {
                return 1.0;
            }
        }
        return 0.0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
     * LayoutFieldPresenterFactory
     * #createPresenter(pl.edu.amu.wmi.kino.netbeans.mvp.client.Display,
     * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute,
     * pl
     * .edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy)
     */
    @Override
    public Presenter<DateDisplay> createPresenter(DateDisplay display,
            WorkItemLayoutField field, RtcWorkItemWorkingCopy wiwc) {
        if (canCreate(field) > 0.0) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            return new DatePresenter(display, attr.getLookup().lookup(DateMarker.class).getAttribute(), wiwc);
        }
        throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
     * LayoutFieldPresenterFactory#getDisplayType()
     */
    @Override
    public Class<DateDisplay> getDisplayType() {
        return DateDisplay.class;
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class DatePresenter extends BasicPresenter<DateDisplay>
            implements InputHandler<Date> {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<Date> attribute;
        private ArrayList formats;

        /**
         * @param display
         * @param attribute
         * @param wiwc
         */
        DatePresenter(DateDisplay display,
                RtcWorkItemAttribute<Date> attribute,
                RtcWorkItemWorkingCopy wiwc) {
            super(display);
            this.wiwc = wiwc;
            this.attribute = attribute;
            this.formats = new ArrayList();

            // add new date formats here
            this.formats.add("yyyy-MM-dd");
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onBind()
         */
        @Override
        protected void onBind() {
            getDisplay().setDate(wiwc.getValue(attribute));
            getDisplay().setLabel(attribute.getDisplayName());
            getDisplay().setId(attribute.getId());
            getDisplay().setInfoStatus(DateDisplay.Status.OK, NbBundle.getMessage(DatePresenter.class, "DatePresenter.input.infoMessage"));
            registerHandler(getDisplay().addInputHandler(
                    DateDisplay.DATE_INPUT, this));

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onUnbind()
         */
        @Override
        protected void onUnbind() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter#refreshDisplay()
         */
        @Override
        public void refreshDisplay() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler#
         * valueEntered(java.lang.Object)
         */
        @Override
        public void valueEntered(Date value) {
            try {
                wiwc.setValue(attribute, value);
//                getDisplay().setDate(value);
                getDisplay().setInfoStatus(DateDisplay.Status.OK, NbBundle.getMessage(DatePresenter.class, "DatePresenter.input.infoMessage"));
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(DateDisplay.Status.WARNING, NbBundle.getMessage(DatePresenter.class, "DatePresenter.input.warningWarning"));
            }
        }

        private String convert(Date value) {
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            return output.format(value);
        }

        private String convert(Date value, String format) {
            SimpleDateFormat output = new SimpleDateFormat(format);
            return output.format(value);
        }

        private Date convert(String value) throws IllegalArgumentException {

            Iterator dateFormats = this.formats.iterator();
            SimpleDateFormat parser = new SimpleDateFormat();

            while (dateFormats.hasNext()) {
                parser.applyPattern((String) dateFormats.next());
                try {
                    return parser.parse(value);
                } catch (ParseException ex) {
                    // do nothing
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
