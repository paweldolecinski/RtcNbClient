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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.api;

import java.util.LinkedList;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.queries.editor.FrameWidget;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeVariable;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;

//z tej klasy maja dziedziczyc factory do wszyskich widgetow edytorow
/**
 *
 * @author Patryk Żywica
 */
public abstract class AbstractAttributeExpressionWidgetFactory implements AttributeExpressionWidgetFactory {

    @Override
    public final Widget[] createAttributeExpressionWidget(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        if (canCreate(expression,query) == false) {
            return null;
        } else {
            //to musi byc zaimplementowane tak aby w oparciu o metody createDesc, i obie z
            //toolbara wyswietlac poprawnego frame widgeta.
            //mozesz tu bez obaw wywolywac metody createDescription i inne (jak jak jest w pierwszej lini
            //wywolane can create. Nic nieszkodzi ze sa one niezaimplementowane tutaj,
            //ale napewno gdzies beda i wtedy beda cos zwracać.
            if (canCreateValueWidgets(expression,query)) {
                LinkedList<Widget> widgets = new LinkedList<Widget>();
                if (expression.getSelectedValues().length + expression.getSelectedVariables().length == 0) {
                    FrameWidget frameWidget = new FrameWidget(scene, expression);
                    frameWidget.addToDescription(createDescriptionForValue(expression,query, null, scene));
                    for (Widget w : createRightToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.FALSE);
                    }
                    for (Widget w : createLeftToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.TRUE);
                    }
                    for (Widget w : createFooter(expression,query, scene)) {
                        frameWidget.addToFooter(w);
                    }
                    widgets.add(frameWidget);
                }
                for (RtcQueryAttributeValue val : expression.getSelectedValues()) {
                    FrameWidget frameWidget = new FrameWidget(scene, expression);
                    frameWidget.addToDescription(createDescriptionForValue(expression, query,val, scene));
                    for (Widget w : createRightToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.FALSE);
                    }
                    for (Widget w : createLeftToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.TRUE);
                    }
                    for (Widget w : createFooter(expression,query, scene)) {
                        frameWidget.addToFooter(w);
                    }
                    widgets.add(frameWidget);
                }
                for (RtcQueryAttributeVariable var : expression.getSelectedVariables()) {
                    FrameWidget frameWidget = new FrameWidget(scene, expression);
                    frameWidget.addToDescription(createDescriptionForVariable(expression,query, var, scene));
                    for (Widget w : createRightToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.FALSE);
                    }
                    for (Widget w : createLeftToolbar(expression,query, scene)) {
                        frameWidget.addToTolbar(w, Boolean.TRUE);
                    }
                    for (Widget w : createFooter(expression,query, scene)) {
                        frameWidget.addToFooter(w);
                    }
                    widgets.add(frameWidget);
                }
                return widgets.toArray(new Widget[]{});
            } else {
                FrameWidget frameWidget = new FrameWidget(scene, expression);
                frameWidget.addToDescription(createDescription(expression,query, scene));
                for (Widget w : createRightToolbar(expression,query, scene)) {
                    frameWidget.addToTolbar(w, Boolean.FALSE);
                }
                for (Widget w : createLeftToolbar(expression,query, scene)) {
                    frameWidget.addToTolbar(w, Boolean.TRUE);
                }
                for (Widget w : createFooter(expression,query, scene)) {
                    frameWidget.addToFooter(w);
                }
                return new Widget[]{frameWidget};
            }
        }
    }

    /**
     * Do not call <code>createAttributeExpressionWidget</code> from this method.
     * @param expression
     * @return
     */
    public abstract boolean canCreate(RtcEditableAttributeExpression expression,RtcQuery query);

    public abstract boolean canCreateValueWidgets(RtcEditableAttributeExpression expression,RtcQuery query);

    public abstract boolean canCreateVariableWidgets(RtcEditableAttributeExpression expression,RtcQuery query);

    /**
     * Do not call <code>createAttributeExpressionWidget</code> from this method.
     * @param expression
     * @param scene
     * @return
     */
    public Widget createDescription(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        return new LabelWidget(scene, NbBundle.getMessage(AbstractAttributeExpressionWidgetFactory.class, "UnsupportedAttributeType.msg"));
    }

    /**
     *
     * Given <code>RtcQueryAtributeValue</code> may be <code>null</code> when widget
     * should be created for empty value, not selected in expression.
     *
     * @param expression
     * @param value may be null.
     * @param scene
     * @return
     */
    public Widget createDescriptionForValue(RtcEditableAttributeExpression expression,RtcQuery query, RtcQueryAttributeValue value, Scene scene) {
        return new LabelWidget(scene, NbBundle.getMessage(AbstractAttributeExpressionWidgetFactory.class, "UnsupportedAttributeType.msg"));
    }

    public Widget createDescriptionForVariable(RtcEditableAttributeExpression expression,RtcQuery query, RtcQueryAttributeVariable variable, Scene scene){
        return new LabelWidget(scene, NbBundle.getMessage(AbstractAttributeExpressionWidgetFactory.class, "UnsupportedAttributeType.msg"));
    }

    /**
     * This method should return additional widgets that should be present in toolbar.
     * Do not call <code>createAttributeExpressionWidget</code> from this method.
     * @param expression
     * @param scene
     * @return
     */
    public Widget[] createRightToolbar(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        return new Widget[]{};
    }

    /**
     * This method should return additional widgets that should be present in toolbar.
     *
     * Do not call <code>createAttributeExpressionWidget</code> from this method.
     * @param expression
     * @param scene
     * @return
     */
    public Widget[] createLeftToolbar(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        //domysle zwracamy pusta tablice bo, przyciski usuwania i dodawania nowego, maja
        //byc dodawane w createAttributeExpressionWidget, ta metoda ma zwracać tylko dodatkowe
        //widgety, np przycisk wyszukania użytkownika.
        return new Widget[]{};
    }

    public Widget[] createFooter(RtcEditableAttributeExpression expression,RtcQuery query, Scene scene) {
        return new Widget[]{};
    }
}
