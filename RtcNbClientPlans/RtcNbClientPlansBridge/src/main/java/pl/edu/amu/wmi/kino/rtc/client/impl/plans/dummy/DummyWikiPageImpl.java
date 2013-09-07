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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import java.util.LinkedList;
import java.util.List;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventSourceSupport;

/**
 *
 * @author Patryk Żywica
 */
public class DummyWikiPageImpl implements RtcPlanPage {

    private String name;
    private String content;
    private Lookup lookup;
    private InstanceContent ic = new InstanceContent();
    private List<RtcPlanPageAttachment> attachments = new LinkedList<RtcPlanPageAttachment>();
    private EventSourceSupport<RtcPlanPageEvent> eventSource = new EventSourceSupport<RtcPlanPageEvent>();

    public DummyWikiPageImpl(String name) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        this.name = name;
        this.lookup = new AbstractLookup(ic);
        this.content = hashCode() + "= Plan iteracji 1.0.4 (autor Patryk Żywica) (zakończenie 07.02.2010)\n"
                + "\n"
                + "\n"
                + "\n"
                + "==Podsumowanie\n"
                + "\n"
                + "\n"
                + "Celem tej iteracji jest udostępnienie użytkownikowi możliwości wyświetlenia struktury logicznej zapytania (z możliwościa wyświetlenia większości atrybutów tj wyliczeniowych, daty, napisów, oraz okresów czasu). Będzie to wymagało wykonania następujących zadań:\n"
                + "\n"
                + "#Implementacja AttributeInfo dla wszyskich obsługiwanych typów atrybutów\n"
                + "#Implementacja ExpressionWidgetDescriptionFactory tak aby zwracała odpowiednie ExpressionWidgety zaincjowane odpowiednimi obiektami ExpressionWidgetDesrtiption.\n"
                + "#Implementacja AttributeExpressionWidgetsDescription dla poszczególnych typów.\n"
                + "#Ustalenie sposobu i implementacja pozyskiwania odpowiednich AttributeInfo w zależności od dostarczonego IQueryableAttribute -  AttributeInfoFactory\n"
                + "#Poprawa implementacji ResultTopComponent tak aby spełniał wymagania funkcjonalne dla edytora zapytań (dodanie Toolbara z odpowiednimi akcjami, itp)\n"
                + "#Zdanie egzaminów sesji zimowej (dotyczy wszyskich !)";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPageContent() {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        return content;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        fireEvent(RtcPlanPageEvent.NAME_CHANGED);
    }

    @Override
    public void setPageContent(String content) {
        //assert !EventQueue.isDispatchThread() : "This method should not be called form event dispatch thread.";
        this.content = content;
        fireEvent(RtcPlanPageEvent.CONTENT_CHANGED);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public RtcPlanPageAttachment[] getAttachments() {
        return attachments.toArray(new RtcPlanPageAttachment[]{});
    }

    @Override
    public void addAttachment(RtcPlanPageAttachment attachemnt) {
        attachments.add(attachemnt);
    }

    @Override
    public void removeAttachment(RtcPlanPageAttachment attachment) {
        attachments.remove(attachment);
    }

    @Override
    public void removeAttachments() {
        attachments.clear();
    }

    public final void removeListener(EventListener<RtcPlanPageEvent> listener) {
        eventSource.removeListener(listener);
    }

    public final void fireEvent(RtcPlanPageEvent event) {
        eventSource.fireEvent(event);
    }

    public final void addListener(EventListener<RtcPlanPageEvent> listener) {
        eventSource.addListener(listener);
    }
}
