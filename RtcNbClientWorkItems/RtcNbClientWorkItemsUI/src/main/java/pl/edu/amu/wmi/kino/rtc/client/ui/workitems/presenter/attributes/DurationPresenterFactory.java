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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.DurationMarker;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.events.AttributeChangeEvent;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.SmallTextDisplay;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class DurationPresenterFactory implements LayoutFieldPresenterFactory<SmallTextDisplay> {

    @Override
    public double canCreate(WorkItemLayoutField field) {
        if (field.getType().equals(WorkItemLayoutField.Type.ATTRIBUTE)) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            if (attr.getLookup().lookup(DurationMarker.class) != null) {
                return 1.0;
            }
        }
        return 0.0;
    }

    /* (non-Javadoc)
     * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory#createPresenter(pl.edu.amu.wmi.kino.netbeans.mvp.client.Display, pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute, pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy)
     */
    @Override
    public Presenter<SmallTextDisplay> createPresenter(SmallTextDisplay display, WorkItemLayoutField field, RtcWorkItemWorkingCopy wiwc) {
        if (canCreate(field) > 0.0) {
            RtcWorkItemAttribute<?> attr = field.getLookup().lookup(RtcWorkItemAttribute.class);
            return new DurationPresenter(display, attr.getLookup().lookup(DurationMarker.class).getAttribute(), wiwc);
        }
        throw new IllegalStateException();
    }

    /* (non-Javadoc)
     * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory#getDisplayType()
     */
    @Override
    public Class<SmallTextDisplay> getDisplayType() {
        return SmallTextDisplay.class;
    }

    private static class DurationPresenter extends BasicPresenter<SmallTextDisplay> implements InputHandler<String>, AttributeChangeEvent.AttributeChangeHandler {

        private RtcWorkItemWorkingCopy wiwc;
        private RtcWorkItemAttribute<Long> attribute;
        private Long value;
        private static Integer hoursPerDay = 8;
        private static Integer daysPerWeek = 5;
        private String seconds, minutes, hours, days, weeks;

        /**
         * @param display
         * @param attribute
         * @param wiwc
         */
        DurationPresenter(SmallTextDisplay display, RtcWorkItemAttribute<Long> attribute, RtcWorkItemWorkingCopy wiwc) {
            super(display);
            this.wiwc = wiwc;
            this.attribute = attribute;

            seconds = NbBundle.getMessage(DurationPresenter.class, "DurationSecondRegex");
            minutes = NbBundle.getMessage(DurationPresenter.class, "DurationMinuteRegex");
            hours = NbBundle.getMessage(DurationPresenter.class, "DurationHourRegex");
            days = NbBundle.getMessage(DurationPresenter.class, "DurationDayRegex");
            weeks = NbBundle.getMessage(DurationPresenter.class, "DurationWeekRegex");
        }

        /* (non-Javadoc)
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onBind()
         */
        @Override
        protected void onBind() {
            value = wiwc.getValue(attribute);
            getDisplay().setText(convert(value));
            getDisplay().setInfoStatus(SmallTextDisplay.Status.OK, NbBundle.getMessage(DurationPresenter.class, "DurationPresenter.input.infoMessage"));
            getDisplay().setLabel(attribute.getDisplayName());
            getDisplay().setId(attribute.getId());
            registerHandler(getDisplay().addInputHandler(SmallTextDisplay.TEXT_INPUT, this));
            AttributeChangeEvent.register(this, wiwc);
        }

        /* (non-Javadoc)
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onUnbind()
         */
        @Override
        protected void onUnbind() {
        }

        /* (non-Javadoc)
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter#refreshDisplay()
         */
        @Override
        public void refreshDisplay() {
        }

        /* (non-Javadoc)
         * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler#valueEntered(java.lang.Object)
         */
        @Override
        public void valueEntered(String v) {
            try {
                Long oldValue = this.value;
                Long newValue = convert(v);
                wiwc.setValue(attribute, newValue);
                getDisplay().setInfoStatus(SmallTextDisplay.Status.OK, NbBundle.getMessage(DurationPresenter.class, "DurationPresenter.input.infoMessage"));
                value = newValue;
                getEventBus().fireEventFromSource(new AttributeChangeEvent<Long>(attribute, oldValue, newValue), wiwc);
            } catch (IllegalArgumentException ex) {
                getDisplay().setInfoStatus(SmallTextDisplay.Status.ERROR, NbBundle.getMessage(DurationPresenter.class, "DurationPresenter.input.errorMessage"));
            }
        }

        private String convert(Long l) {

            String secondsString = NbBundle.getMessage(DurationPresenter.class, "DurationSecond");
            String minutesString = NbBundle.getMessage(DurationPresenter.class, "DurationMinute");
            String hoursString = NbBundle.getMessage(DurationPresenter.class, "DurationHour");
            String daysString = NbBundle.getMessage(DurationPresenter.class, "DurationDay");
            String weeksString = NbBundle.getMessage(DurationPresenter.class, "DurationWeek");


            Long seconds = l / 1000;
            Long weeks = seconds / (hoursPerDay * daysPerWeek * 3600);
            seconds = seconds - (weeks * (hoursPerDay * daysPerWeek * 3600));
            Long days = seconds / (hoursPerDay * 3600);
            seconds = seconds - (days * (hoursPerDay * 3600));
            Long hours = seconds / 3600;
            seconds = seconds - (hours * 3600);
            Long minutes = seconds / 60;
            seconds = seconds - (minutes * 60);

            String result = "";
            if (weeks != 0) {
                result += weeks + " " + weeksString + " ";
            }
            if (days != 0) {
                result += days + " " + daysString + " ";
            }
            if (hours != 0) {
                result += hours + " " + hoursString + " ";
            }
            if (minutes != 0) {
                result += minutes + " " + minutesString + " ";
            }
            if (seconds != 0) {
                result += seconds + " " + secondsString;
            }

            return result.trim();

        }

        private long convert(String input) throws IllegalArgumentException {
            input = " " + input.replaceAll(",", ".");
            Long result = new Long(0);
            Pattern weeksP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + weeks + ")");
            Matcher weeksM = weeksP.matcher(input);
            while (weeksM.find()) {


                Float weeksF = Float.parseFloat(weeksM.group(1));
                Float m = weeksF * 3600000;
                result += m.longValue() * daysPerWeek * hoursPerDay;
            }
            Pattern daysP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + days + ")");
            Matcher daysM = daysP.matcher(input);
            while (daysM.find()) {

                Float daysF = Float.parseFloat(daysM.group(1));

                Float m = daysF * 3600000;
                result += m.longValue() * hoursPerDay;
            }
            Pattern hoursP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + hours + ")");
            Matcher hoursM = hoursP.matcher(input);
            while (hoursM.find()) {

                Float hoursF = Float.parseFloat(hoursM.group(1));

                Float m = hoursF * 3600000;
                result += m.longValue();
            }
            Pattern minutesP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + minutes + ")");
            Matcher minutesM = minutesP.matcher(input);
            while (minutesM.find()) {

                Float minutesF = Float.parseFloat(minutesM.group(1));
                Float m = minutesF * 60000;
                result += m.longValue();
            }
            Pattern secondsP = Pattern.compile("\\s([0-9]+((\\.)[0-9]+)?)\\s*(" + seconds + ")");
            Matcher secondsM = secondsP.matcher(input);
            while (secondsM.find()) {
                Float secondsF = Float.parseFloat(secondsM.group(1));
                Float m = secondsF * 1000;
                result += m.longValue();
            }
            return result;
        }

        @Override
        public <H> void valueChanged(RtcWorkItemAttribute<H> attribute, H oldValue, H newValue) {
            if (this.attribute.equals(attribute)) {
                if (!newValue.equals(oldValue) && !newValue.equals(value)) {
                    getDisplay().setText("" + newValue);
                }
            }
        }
    }
}
