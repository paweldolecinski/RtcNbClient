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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.LargeTextDisplay;
import pl.edu.amu.wmi.kino.rtc.client.ui.workitems.utils.StringUtils;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class LargeTextPresenterFactory implements
        LayoutFieldPresenterFactory<LargeTextDisplay> {

    public double canCreate(WorkItemLayoutField field) {
        if (field.getType().equals(WorkItemLayoutField.Type.ATTRIBUTE)) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            if (attr.getValueType().equals(String.class)) {
                return 0.5;
            }
        }
        return 0.0;
    }

    public Presenter<LargeTextDisplay> createPresenter(
            LargeTextDisplay display, WorkItemLayoutField field,
            RtcWorkItemWorkingCopy wiwc) {
        if (canCreate(field) > 0.0) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            return new LargeTextPresenter(display, (RtcWorkItemAttribute<String>) attr, wiwc);
        }
        throw new IllegalStateException();
    }

    public Class<LargeTextDisplay> getDisplayType() {
        return LargeTextDisplay.class;
    }

    private static class LargeTextPresenter extends BasicPresenter<LargeTextDisplay> implements InputHandler<String> {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<String> attribute;

        LargeTextPresenter(LargeTextDisplay display,
                RtcWorkItemAttribute<String> attribute,
                RtcWorkItemWorkingCopy wiwc) {
            super(display);
            this.wiwc = wiwc;
            this.attribute = attribute;
        }

        @Override
        protected void onBind() {
            String text = wiwc.getValue(attribute);
            if(text!=null){
                getDisplay().setText(StringUtils.escapeHtml(wiwc.getValue(attribute)));
            }else{
                getDisplay().setText("");
            }
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
                getDisplay().setInfoStatus(LargeTextDisplay.Status.OK, NbBundle.getMessage(LargeTextPresenter.class, "LargeTextPresenter.input.infoMessage"));
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(LargeTextDisplay.Status.ERROR,
                        NbBundle.getMessage(LargeTextPresenter.class, "LargeTextPresenter.input.errorMessage"));
            }
        }

        private String convert(String value) {
            final StringBuilder toReturn = new StringBuilder();
            final StringCharacterIterator iterator = new StringCharacterIterator(value);
            char character = iterator.current();
            while (character != CharacterIterator.DONE) {
                if (character == '<') {
                    toReturn.append("&lt;");
                } else if (character == '>') {
                    toReturn.append("&gt;");
                } else if (character == '&') {
                    toReturn.append("&amp;");
                } else if (character == '\"') {
                    toReturn.append("&quot;");
                } else if (character == '\t') {
                    appendChar(9, toReturn);
                } else if (character == '!') {
                    appendChar(33, toReturn);
                } else if (character == '#') {
                    appendChar(35, toReturn);
                } else if (character == '$') {
                    appendChar(36, toReturn);
                } else if (character == '%') {
                    appendChar(37, toReturn);
                } else if (character == '\'') {
                    appendChar(39, toReturn);
                } else if (character == '(') {
                    appendChar(40, toReturn);
                } else if (character == ')') {
                    appendChar(41, toReturn);
                } else if (character == '*') {
                    appendChar(42, toReturn);
                } else if (character == '+') {
                    appendChar(43, toReturn);
                } else if (character == ',') {
                    appendChar(44, toReturn);
                } else if (character == '-') {
                    appendChar(45, toReturn);
                } else if (character == '.') {
                    appendChar(46, toReturn);
                } else if (character == '/') {
                    appendChar(47, toReturn);
                } else if (character == ':') {
                    appendChar(58, toReturn);
                } else if (character == ';') {
                    appendChar(59, toReturn);
                } else if (character == '=') {
                    appendChar(61, toReturn);
                } else if (character == '?') {
                    appendChar(63, toReturn);
                } else if (character == '@') {
                    appendChar(64, toReturn);
                } else if (character == '[') {
                    appendChar(91, toReturn);
                } else if (character == '\\') {
                    appendChar(92, toReturn);
                } else if (character == ']') {
                    appendChar(93, toReturn);
                } else if (character == '^') {
                    appendChar(94, toReturn);
                } else if (character == '_') {
                    appendChar(95, toReturn);
                } else if (character == '`') {
                    appendChar(96, toReturn);
                } else if (character == '{') {
                    appendChar(123, toReturn);
                } else if (character == '|') {
                    appendChar(124, toReturn);
                } else if (character == '}') {
                    appendChar(125, toReturn);
                } else if (character == '~') {
                    appendChar(126, toReturn);
                } else {
                    //the char is not a special one
                    //add it to the result as is
                    toReturn.append(character);
                }
                character = iterator.next();
            }
            return toReturn.toString();
        }

        private static void appendChar(Integer charCode, StringBuilder builder) {
            String padding = "";
            if (charCode <= 9) {
                padding = "00";
            } else if (charCode <= 99) {
                padding = "0";
            }
            String toReturn = "&#" + padding + charCode.toString() + ";";
            builder.append(toReturn);
        }
    }
}
