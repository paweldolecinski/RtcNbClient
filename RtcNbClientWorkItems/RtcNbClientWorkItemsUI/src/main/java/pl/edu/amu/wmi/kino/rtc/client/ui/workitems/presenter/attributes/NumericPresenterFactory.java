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
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.SmallTextDisplay;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class NumericPresenterFactory implements
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
            if (attr.getValueType().equals(Integer.class)
                    || attr.getValueType().equals(Long.class)
                    || attr.getValueType().equals(Short.class)
                    || attr.getValueType().equals(Float.class)
                    || attr.getValueType().equals(Double.class)
                    || attr.getValueType().equals(Byte.class)) {
                return .5;
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
            return doCreatePresenter(display, attr, wiwc);
        }
        throw new IllegalStateException();
    }

    private <T> Presenter<SmallTextDisplay> doCreatePresenter(
            SmallTextDisplay display, RtcWorkItemAttribute<T> attribute,
            RtcWorkItemWorkingCopy wiwc) {
        return new NumericPresenter<T>(display, attribute, wiwc);
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
     * @param <T>
     * 
     */
    private static class NumericPresenter<T> extends BasicPresenter<SmallTextDisplay> implements InputHandler<String> {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<T> attribute;

        /**
         * @param display
         * @param attribute
         * @param wiwc
         * @param clazz 
         */
        NumericPresenter(SmallTextDisplay display,
                RtcWorkItemAttribute<T> attribute,
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
            getDisplay().setText(
                    "" + wiwc.getValue(attribute));
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
                wiwc.setValue(attribute, attribute.getValueType().cast(value));
                getDisplay().setInfoStatus(SmallTextDisplay.Status.OK, NbBundle.getMessage(NumericPresenter.class, "NumericPresenter.input.infoMessage"));
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(SmallTextDisplay.Status.ERROR,
                        NbBundle.getMessage(NumericPresenter.class, "NumericPresenter.input.errorMessage"));
            } catch (ClassCastException ex) {
                getDisplay().setInfoStatus(SmallTextDisplay.Status.ERROR,
                        NbBundle.getMessage(NumericPresenter.class, "NumericPresenter.input.errorCastMessage"));
            }
        }

        private String convert(Object value) {
            if (this.attribute.getValueType() == Integer.class) {
                return ((Integer) value).toString();
            }
            return "";
        }

        private T convert(String value) {
            if (this.attribute.getValueType() == Integer.class) {
                return (T) new Integer(Integer.parseInt(value));
            }
            return (T) new Object();
        }
    }
}
