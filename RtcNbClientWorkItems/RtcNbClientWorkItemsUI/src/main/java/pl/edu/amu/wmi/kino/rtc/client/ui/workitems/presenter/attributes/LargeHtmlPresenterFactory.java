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
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.LargeTextDisplay;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class LargeHtmlPresenterFactory implements
        LayoutFieldPresenterFactory<LargeTextDisplay> {

    @Override
    public double canCreate(WorkItemLayoutField field) {
        if (field.getType().equals(WorkItemLayoutField.Type.ATTRIBUTE)) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            if (attr.getLookup().lookup(HtmlMarker.class) != null) {
                return 0.5;
            }
        }
        return 0.0;
    }

    @Override
    public Presenter<LargeTextDisplay> createPresenter(
            LargeTextDisplay display, WorkItemLayoutField field,
            RtcWorkItemWorkingCopy wiwc) {
        if (canCreate(field) > 0.0) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            return new LargeHtmlPresenter(display, attr.getLookup().lookup(HtmlMarker.class).getAttribute(), wiwc);
        }
        throw new IllegalStateException();
    }

    @Override
    public Class<LargeTextDisplay> getDisplayType() {
        return LargeTextDisplay.class;
    }

    private static class LargeHtmlPresenter extends BasicPresenter<LargeTextDisplay> implements InputHandler<String> {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<String> attribute;

        LargeHtmlPresenter(LargeTextDisplay display,
                RtcWorkItemAttribute<String> attribute,
                RtcWorkItemWorkingCopy wiwc) {
            super(display);
            assert display != null;
            this.wiwc = wiwc;
            this.attribute = attribute;
        }

        @Override
        protected void onBind() {
            getDisplay().setText(wiwc.getValue(attribute));
            getDisplay().setInfoStatus(LargeTextDisplay.Status.OK, "");
            getDisplay().setLabel(attribute.getDisplayName());
            getDisplay().setId(attribute.getId());
            registerHandler(getDisplay().addInputHandler(
                    LargeTextDisplay.TEXT_INPUT, this));

        }

        @Override
        protected void onUnbind() {
        }

        @Override
        public void refreshDisplay() {
        }

        @Override
        public void valueEntered(String value) {
            try {
                wiwc.setValue(attribute, value);
                getDisplay().setInfoStatus(LargeTextDisplay.Status.OK, NbBundle.getMessage(LargeHtmlPresenter.class, "LargeHtmlPresenter.input.infoMessage"));
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(LargeTextDisplay.Status.ERROR,
                        NbBundle.getMessage(LargeHtmlPresenter.class, "LargeHtmlPresenter.input.errorMessage"));
            }
        }
    }
}
