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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.page;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPage;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.cookies.RtcTwoStateEditSwitchCookie;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.items.RtcPageEditingPanel;
import pl.edu.amu.wmi.kino.rtc.client.plans.editor.toolbars.RtcPageToolbar;
import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.RtcDefaultReferenceDetector;
import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.RtcVariableResolver;
import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.WikiTransformer;

/**
 *
 * @author Patryk Żywica
 */
class RtcPageEditorMVElement extends KinoMultiViewElement {
    private RtcPlanPage page;
    private RtcPlan plan;
    private CardLayout cl=new CardLayout();
    private JPanel panel=new JPanel(cl);
    private RtcPageEditingPanel editor;
    
    public RtcPageEditorMVElement(RtcPlanPage page, RtcPlan plan) {
        this.plan=plan;
        this.page=page;
        this.editor=new RtcPageEditingPanel(page.getAttachments());
        ic.add(this.page);
        ic.add(this.plan);
        ic.add(new RtcPageEditorMVElementCookie());
        ic.add(editor);
    }

    @Override
    public JComponent getToolbarRepresentation() {
        RtcPageToolbar rpt=new RtcPageToolbar();
        return rpt;

    }


    @Override
    public JComponent createInnerComponent() {
       SwingWorker iComponentWorker=new SwingWorker<JTextPane,Void>() {

            @Override
            protected JTextPane doInBackground() throws Exception {
                JTextPane pane=new JTextPane();
                
                pane.setContentType("text/html");
                ////System.out.println(Lookup.getDefault().lookup(WikiTransformer.class).transform(page.getPageContent()));
                WikiTransformer wk = Lookup.getDefault().lookup(WikiTransformer.class);
                
                wk.setItemReferenceDetector(new RtcDefaultReferenceDetector(page));
                wk.setVariableResolver(new RtcVariableResolver(plan, page));

                pane.setText(wk.transform(page.getPageContent()));

                pane.setEditable(false);
                pane.setVisible(true);       
                

                return pane;
            }

            @Override
            protected void done() {
                try{
                    JTextPane pane=get();
                    panel.setBorder(BorderFactory.createEmptyBorder());
                    panel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                    JScrollPane scp=new JScrollPane(pane);
                    scp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    panel.add(editor,"editor");
                    panel.add(scp,"view");
                    cl.show(panel,"view");
                }catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPageEditorMVElement.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                } catch (ExecutionException ex) {
                    //Exceptions.printStackTrace(ex);
                    RtcLogger.getLogger(RtcPageEditorMVElement.class).log(Level.WARNING, ex.getLocalizedMessage(), ex);
                }
            }


       };
       iComponentWorker.execute();

       return panel;
    }

    class RtcPageEditorMVElementCookie implements RtcTwoStateEditSwitchCookie{
        private boolean editing=false;
        @Override
        public void setEditing(boolean b){
            Component[] tab=panel.getComponents();
            //JOptionPane.showMessageDialog(null," "+(tab[0] instanceof JTextPane)+" or "+(tab[1] instanceof JTextPane));
            if(b){
                if(tab[0] instanceof RtcPageEditingPanel) {
                    //JOptionPane.showMessageDialog(null,"pierwsza opcja ,w zerze jest editor, a sta jest "+b);
                    ((RtcPageEditingPanel) tab[0]).setEditedText(page.getPageContent());
                    
                }
                else{
                    //JOptionPane.showMessageDialog(null,"pierwsza opcja, w 1 jest editor, a sta jest "+b);
                    ((RtcPageEditingPanel)tab[1]).setEditedText(page.getPageContent());
                    
                }
            }
            else{
                if(tab[0] instanceof RtcPageEditingPanel){
                    //JOptionPane.showMessageDialog(null,"druga opcja, w zerze jest editor, a sta jest "+b);
                    page.setPageContent(((RtcPageEditingPanel)tab[0]).getEditedText());
                    ((JTextPane)((JScrollPane)tab[1]).getViewport().getView()).setText(Lookup.getDefault().lookup(WikiTransformer.class).transform(page.getPageContent()));
                }
                else{
                    //JOptionPane.showMessageDialog(null,"druga opcja, w 1 jest editor, a sta jest "+b);
                    page.setPageContent(((RtcPageEditingPanel)tab[1]).getEditedText());
                    ((JTextPane)((JScrollPane)tab[0]).getViewport().getView()).setText(Lookup.getDefault().lookup(WikiTransformer.class).transform(page.getPageContent()));
                }
            }
            cl.next(panel);
            editing=b;
        }

        @Override
        public boolean isEditing() {
            return editing;
        }

    }
}
