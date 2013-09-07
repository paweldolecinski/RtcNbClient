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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import org.openide.util.actions.SystemAction;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.pages.RtcPlanPageAttachment;
import pl.edu.amu.wmi.kino.rtc.client.plans.actions.RtcShowAttachmentsTableAction;

/**
 *
 * @author Bartosz Zaleski
 */
public class RtcPageEditingPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 26610944353534L;
    private RtcAttachmentPane attachmentPane;
    private JTextPane editedPane;
    private JScrollPane scrollingPane;
    
    

    public RtcPageEditingPanel(RtcPlanPageAttachment[] ra) {
        initComponents();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        scrollingPane=new JScrollPane();
        editedPane=new JTextPane();
        
        scrollingPane.setViewportView(editedPane);
        scrollingPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollingPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.gridx=0;
        c.gridy=0;
        c.fill=GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        this.add(scrollingPane,c);
        attachmentPane=new RtcAttachmentPane(ra);
        c.gridx=0;
        c.gridy=1;
        c.weighty=0;
        this.add(attachmentPane,c);

        //editedPane.setEnabled(true);
        //editedPane.setVisible(true);
        //editedPane.setEditable(true);
    }

    public void setEditedText(String text){
        editedPane.setText(text);
    }

    public String getEditedText(){
        return editedPane.getText();
    }

    public JScrollPane getTextEditor(){
        return scrollingPane;
    }
    
    public RtcAttachmentPane getAttachmentPane(){
        return attachmentPane;
    }

    public void doResizing(double ratio){
        double a=0;
        GridBagConstraints c = ((GridBagLayout)getLayout()).getConstraints(attachmentPane);
        GridBagConstraints d = ((GridBagLayout)getLayout()).getConstraints(scrollingPane);
        GridBagConstraints e = ((GridBagLayout)getLayout()).getConstraints(attachmentPane.getToolbar());
        GridBagConstraints f = ((GridBagLayout)getLayout()).getConstraints(attachmentPane.getScrollPane());
        
        if(c.weighty+ratio*c.weighty>d.weighty-ratio*c.weighty){
            a=(d.weighty-ratio*c.weighty)/(c.weighty+ratio*c.weighty);
            if(a<=0.001){
                a=0.001;
            }
            c.weighty=1;
            d.weighty=a;
        }
        else{
            a=(c.weighty+ratio*c.weighty)/(d.weighty-ratio*c.weighty);
            if(a<=0.001){
                a=0.001;
            }
            d.weighty=1;
            c.weighty=a;
        }
        if(c.weighty>=0.02){
            SystemAction.get(RtcShowAttachmentsTableAction.class).setBooleanState(true);
        }
        else{
            SystemAction.get(RtcShowAttachmentsTableAction.class).setBooleanState(false);

        }

        ((GridBagLayout)getLayout()).setConstraints(attachmentPane, c);
        ((GridBagLayout)getLayout()).setConstraints(scrollingPane, d);
        attachmentPane.revalidate();
        attachmentPane.doLayout();
        revalidate();
        doLayout();
    }
    public void bla(){
        GridBagConstraints c = ((GridBagLayout)getLayout()).getConstraints(attachmentPane);
        GridBagConstraints d = ((GridBagLayout)getLayout()).getConstraints(scrollingPane);
        JOptionPane.showMessageDialog(null, c.weighty+" "+d.weighty);
        /*if(c.weighty<=0.01){
            c.weighty=0.01;
        }
        if(d.weighty<=0.01){
            d.weighty=0.01;
        }
        ((GridBagLayout)((RtcPageEditingPanel)attToolbar.getParent().getParent()).getLayout()).setConstraints(((RtcPageEditingPanel)attToolbar.getParent().getParent()).getAttachmentPane(), c);
        ((GridBagLayout)((RtcPageEditingPanel)attToolbar.getParent().getParent()).getLayout()).setConstraints(((RtcPageEditingPanel)attToolbar.getParent().getParent()).getTextEditor(), d);
        ((RtcPageEditingPanel)attToolbar.getParent().getParent()).revalidate();
        ((RtcPageEditingPanel)attToolbar.getParent().getParent()).doLayout();
*/
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 451, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
