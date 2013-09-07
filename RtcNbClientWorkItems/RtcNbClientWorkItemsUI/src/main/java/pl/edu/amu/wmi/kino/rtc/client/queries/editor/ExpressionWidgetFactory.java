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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor;

import javax.swing.JOptionPane;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.api.AttributeExpressionWidgetFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableTermExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import org.openide.util.NbBundle;
/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = ExpressionWidgetFactory.class)
public class ExpressionWidgetFactory {

    public Widget[] createExpressionWidget(RtcEditableExpression expression,RtcQuery query, Scene scene) {
        if (expression instanceof RtcEditableTermExpression) {
            return new Widget[]{new TermWidget(scene,query, (RtcEditableTermExpression) expression)};
        } else {
            if (expression instanceof RtcEditableAttributeExpression) {
                Lookup lookup = Lookups.forPath("Rtc/Modules/QueriesModule/AttributeExpressionWidgetFactories");
                for (AttributeExpressionWidgetFactory f : lookup.lookupAll(AttributeExpressionWidgetFactory.class)) {
                    Widget[] widget = f.createAttributeExpressionWidget((RtcEditableAttributeExpression) expression,query, scene);
                    if (widget != null) {
                        return widget;
                    }
                }
                //Jesli nieznajdzie zadnej odpowiedniej trzeba zwrocic jakis
                //domyslny chociazby wyswietlajacy nazwe i komunikat ze niema
                //edytora dla tego typu
                JOptionPane.showMessageDialog(null, NbBundle.getMessage(ExpressionWidgetFactory.class, "Nothing.text")); // poprawione
                return new Widget[]{new FrameWidget(scene, (RtcEditableAttributeExpression) expression)};
            } else {
                JOptionPane.showMessageDialog(null, NbBundle.getMessage(ExpressionWidgetFactory.class, "Nothing.text"));
                return new Widget[]{new FrameWidget(scene, null)};
            }
        }
    }
}
