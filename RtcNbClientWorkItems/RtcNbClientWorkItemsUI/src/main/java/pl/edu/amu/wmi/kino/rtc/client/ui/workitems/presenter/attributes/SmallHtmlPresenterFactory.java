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

import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.HtmlMarker;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.SmallTextMarker;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.SmallTextDisplay;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class SmallHtmlPresenterFactory implements
        LayoutFieldPresenterFactory<SmallTextDisplay> {

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
            if (attr.getLookup().lookup(SmallTextMarker.class) != null
                    && attr.getLookup().lookup(HtmlMarker.class) != null) {
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
    public Presenter<SmallTextDisplay> createPresenter(
            SmallTextDisplay display, WorkItemLayoutField field,
            RtcWorkItemWorkingCopy wiwc) {
        if (canCreate(field) > 0.0) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            return new SmallHtmlPresenter(display, attr.getLookup().lookup(HtmlMarker.class).getAttribute(), wiwc);
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
    public Class<SmallTextDisplay> getDisplayType() {
        return SmallTextDisplay.class;
    }

    /**
     * @author Paweł Doleciński
     * 
     */
    private static class SmallHtmlPresenter extends BasicPresenter<SmallTextDisplay> implements InputHandler<String> {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<String> attribute;

        /**
         * @param display
         * @param attribute
         * @param wiwc
         */
        SmallHtmlPresenter(SmallTextDisplay display,
                RtcWorkItemAttribute<String> attribute,
                RtcWorkItemWorkingCopy wiwc) {
            super(display);
            this.wiwc = wiwc;
            this.attribute = attribute;
        }

        /*
         * (non-Javadoc)
         * 
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onBind()
         */
        @Override
        protected void onBind() {
            getDisplay().setText(wiwc.getValue(attribute));
            getDisplay().setInfoStatus(SmallTextDisplay.Status.OK, "");
            getDisplay().setLabel(attribute.getDisplayName());
            getDisplay().setId(attribute.getId());
            registerHandler(getDisplay().addInputHandler(
                    SmallTextDisplay.TEXT_INPUT, this));

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
        public void valueEntered(String value) {
            try {
                wiwc.setValue(attribute, value);
                getDisplay().setInfoStatus(SmallTextDisplay.Status.OK, NbBundle.getMessage(SmallHtmlPresenter.class, "SmallHtmlPresenter.input.infoMessage"));
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(SmallTextDisplay.Status.ERROR,
                        NbBundle.getMessage(SmallHtmlPresenter.class, "SmallHtmlPresenter.input.errorMessage"));
            }
        }
    }
}
