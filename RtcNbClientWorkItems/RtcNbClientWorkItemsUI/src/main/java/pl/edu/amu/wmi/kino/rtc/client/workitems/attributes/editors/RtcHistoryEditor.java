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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryContent;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcHistoryItem;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;

/**
 *
 * @author Pawel Dolecinski
 * @author Dawid Holewa
 */
public class RtcHistoryEditor extends PropertyEditorSupport {

    private final Lookup context;
    private String html;
    private final JEditorPane jEditorPane1 = new javax.swing.JEditorPane();

    public RtcHistoryEditor(Lookup context) {
        this.context = context;
    }

    @Override
    public String getAsText() {
        return "";
    }

    public String getAsHTML() {
        String ret = "";

        // TODO: Change or remove
        //        String this_week = "";
        //        String last_week = "";
        //        String this_month = "";
        String other = "";

        RtcHistoryContent value = (RtcHistoryContent) getValue();
        value.computeHistory();
        List<RtcHistoryItem> history = value.getHistory();

        // It's have to be do by using variable "partial_ret" because we have to reverse list of RtcHistoryItem object.
        // In this way it will be faster.
        for (RtcHistoryItem rtcHistoryItem : history) {
            String partial_ret = "";
            Date date = rtcHistoryItem.getDate();
            partial_ret += "<span class=\"author\">" + rtcHistoryItem.getModifier() + "</span>&nbsp;&nbsp;<span class=\"date\">" + date.toString().substring(0, date.toString().length() - 4) + "</span><br/>";
            partial_ret += rtcHistoryItem.getContent() + "<br/><br/>";
            // TODO: Change or remove
            //            int count_days = (int) ((new Date()).getTime() - date.getTime()) / 1000 / 60 / 60 / 24;
            //            if (count_days <= 7) {
            //                this_week = partial_ret + this_week;
            //            } else if (count_days > 7 && count_days <= 14) {
            //                last_week = partial_ret + last_week;
            //            } else if (count_days > 14 && count_days <= 31) {
            //                this_month = partial_ret + this_month;
            //            } else {
            other = partial_ret + other;
            //            }

        }

        // TODO: Change or remove
        //        if(this_week.length() > 1)ret += "<h1>W tym tygodniu:</h1><br/>" + this_week;
        //        if(last_week.length() > 1) ret += "<hr><br/><h1>W zeszlym tygodniu:</h1><br/>" + last_week;
        //        if(this_month.length() > 1)ret += "<hr><br/><h1>W tym miesiacu:</h1><br/>" + this_month;
        //        if(other.length() > 1)ret += "<hr><br/><h1>Ponad miesiac temu:</h1><br/>" + other;
        ret += other;
        return ret;
    }

    @Override
    public Component getCustomEditor() {
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

        jEditorPane1.setContentType("text/html"); // NOI18N
        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(Color.white);
        jEditorPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
        jScrollPane1.setViewportView(jEditorPane1);

        jScrollPane1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setBorder(new EmptyBorder(0, 0, 0, 0));

        jEditorPane1.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    try {
                        if (!hle.getURL().toString().contains("com.ibm.team.repository.Contributor")) {
                            Desktop.getDesktop().browse(hle.getURL().toURI());
                        }
                    } catch (Exception ex) {
                        //Exceptions.printStackTrace(ex);
                        RtcLogger.getLogger(RtcHistoryEditor.class)
                                .log(Level.WARNING, ex.getLocalizedMessage(), ex);
                    }
                }
            }
        });
        /*
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                setEditorPaneHtml();
            }
        }).run();*/
        setEditorPaneHtml();

        return jScrollPane1;
    }

    private void setEditorPaneHtml() {
        html = getAsHTML();

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();

        styleSheet.addRule("body, table {font-family: Tahoma, Arial; font-size: 1em;}");
        styleSheet.addRule(".author {font-weight: bold;}");
        styleSheet.addRule(".date {color: gray;}");
        styleSheet.addRule(".deleted {background-color: #FFCCCC; text-decoration: line-through; margin-right: 2px;}");
        styleSheet.addRule(".inserted {background-color: #CCFF33; text-decoration: underline;}");
        styleSheet.addRule("h1 {color: #0790e0; font-weight:bold; font-size: 1.3em; }");
        styleSheet.addRule("th, td { border-style: solid; border-width: 1; border-color: gray;}");
        styleSheet.addRule("table { width: 400 px; max-width: 800px}");

        jEditorPane1.setEditorKit(kit);

        Document doc = kit.createDefaultDocument();
        jEditorPane1.setDocument(doc);
        jEditorPane1.setText(html);
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

//    public static ToStringProvider getStringProvider() {
//        return new ToStringProviderImpl();
//    }
//
//    public static class ToStringProviderImpl implements ToStringProvider<RtcHistoryContent> {
//
//        @Override
//        public String toString(RtcHistoryContent value) {
//            return value.toString();
//        }
//    }
}
