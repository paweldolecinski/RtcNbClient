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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.factories.enumeration;

import org.openide.explorer.view.CheckableNode;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;

/**
 *
 * @author Patryk Żywica
 */
class CheckableNodeImpl implements CheckableNode {

    private RtcQueryAttributeValue value;
    private RtcEditableAttributeExpression expression;
    private Boolean selected;
    //TODO : for future : implement checkAlso somehow
    private RtcQueryAttributeValue[] checkAlso;
    private RtcQueryAttributeValueProvider provider;

    public CheckableNodeImpl(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider provider, RtcEditableAttributeExpression expression, boolean initialySelected, RtcQueryAttributeValue[] checkAlso) {
        this.value = value;
        this.expression = expression;
        this.selected = initialySelected;
        this.checkAlso = checkAlso;
        this.provider = provider;
    }

    public CheckableNodeImpl(RtcQueryAttributeValue value, RtcQueryAttributeValueProvider provider, RtcEditableAttributeExpression expression, boolean initialySelected) {
        this(value, provider, expression, initialySelected, new RtcQueryAttributeValue[]{});
    }

    @Override
    public boolean isCheckable() {
        return provider.isValueSelectable(value);
    }

    @Override
    public boolean isCheckEnabled() {
        return provider.isValueSelectable(value);
    }

    @Override
    public Boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(Boolean selected) {
        if (expression != null && !selected.equals(this.selected)) {
            RequestProcessor.getDefault().post(new SelectionRunnable(selected));
            this.selected = selected;
        }
    }

//    @Override
//    public void selectedValueChanged(RtcQueryAttributeValue oldValue, RtcQueryAttributeValue newValue) {
//        if(oldValue.equals(value) && newValue==null){
//
//        }
//    }
    private class SelectionRunnable implements Runnable {

        private Boolean sel;

        public SelectionRunnable(Boolean sel) {
            this.sel = sel;
        }

        @Override
        public void run() {
            if (sel.equals(Boolean.TRUE)) {
                expression.addSelectedValue(value);
//                for (RtcQueryAttributeValue val : checkAlso) {
//                    expression.addSelectedValue(val);
//                }
            } else {
                expression.removeSelectedValue(value);
//                for (RtcQueryAttributeValue val : checkAlso) {
//                    expression.removeSelectedValue(val);
//                }
            }
            
        }
    }
}
