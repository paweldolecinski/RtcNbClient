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
package pl.edu.amu.wmi.kino.rtc.client.plans.actions.dialogs;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.RtcPlan;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemChecker;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemCheckerUtilities;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.checker.RtcPlanItemProblem;

/**
 *
 * @author Bartosz Zaleski <b4lzak@gmail.com>
 */
public class RtcPlanChecksDialog extends javax.swing.JPanel {
    private static final long serialVersionUID = 32499557534L;
    private JTable table;


    public RtcPlanChecksDialog() {
        initComponents();
        SwingWorker tableWorker=new SwingWorker<Object[][],Void>() {
            protected int licznik;
            @Override
            protected Object[][] doInBackground() throws Exception {
                RtcPlan plan=Utilities.actionsGlobalContext().lookup(RtcPlan.class);

                RtcPlanItemChecker rpic[]=plan.getPlanItemCheckers();
                for(int i=0;i<plan.getPlanItemCheckers().length;i++){
                    licznik+=rpic[i].getProblems().length;
                }
                Object zaw[][]=new Object[licznik][3];
                licznik=0;
                ImageIcon ikonka;
                RtcPlanItemProblem.RtcSeverity sev;
                for(int i=0;i<rpic.length;i++){
                    for(int j=0;j<rpic[i].getProblems().length;j++){
                        sev=rpic[i].getProblems()[j].getSeverity();
                        if(RtcPlanItemCheckerUtilities.compareSeverities(RtcPlanItemProblem.RtcSeverity.WARNING,sev)>=0){
                            switch(sev){
                                case FATAL_ERROR:
                                case ERROR:
                                    ikonka=ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/checker/resources/checkError.gif",false);
                                    break;
                                case WARNING:
                                    ikonka=ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/checker/resources/checkWarning.gif",false);
                                    break;
                                case NONE:
                                case INFO:
                                default:
                                    ikonka=ImageUtilities.loadImageIcon("pl/edu/amu/wmi/kino/rtc/client/plans/editor/checker/resources/blankIcon.gif",false);
                            }
                            zaw[licznik][0]=ikonka;
                            zaw[licznik][1]="<html>"+rpic[i].getProblems()[j].getMessage()+"</html>";
                            zaw[licznik][2]=rpic[i].getProblems()[j].getPlanItem().getName();
                            //zaw[licznik][3]=null;
                            licznik++;
                        }
                    }
                }
                Object z[][]=new Object[licznik][3];
                for(int i=0;i<licznik;i++){
                    for(int j=0;j<3;j++){
                        z[i][j]=zaw[i][j];
                    }
                }
               // //System.out.println("licznik: "+z.length+" "+z[0].length);
                setLayout(new BorderLayout());
                String nazwy[]=new String[3];
                nazwy[0]=" ";
                nazwy[1]="Description";
                nazwy[2]="Work item";
                //nazwy[3]=" ";
                DefaultTableModel model = new DefaultTableModel(z, nazwy){
                    private static final long serialVersionUID = 6638859557534L;
                    @Override
                    public boolean isCellEditable(int row, int column) {
                         return false;
                    }
                };
                table =new JTable(model){
                    private static final long serialVersionUID = 965229557534L;
                    @Override
                    public Class getColumnClass(int column){
                        if(column==0){
                            return ImageIcon.class;
                        }
                        else{
                            return String.class;
                        }
                    }
                };

                

                return zaw;
            }

            @Override
            protected void done() {

                ////System.out.println("licznik: "+licznik);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                table.setRowSelectionAllowed(true);
                table.setColumnSelectionAllowed(false);
                table.getColumnModel().getColumn(0).setResizable(false);
                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                table.getColumnModel().getColumn(0).setMaxWidth(20);
                // table.getColumnModel().getColumn(3).setPreferredWidth(10);
                table.getColumnModel().getColumn(1).setMinWidth(30);
                table.getColumnModel().getColumn(2).setMinWidth(20);

                table.setFillsViewportHeight(true);
                table.getTableHeader().setReorderingAllowed(false);

        }
        };
        tableWorker.execute();
        while(!tableWorker.isDone()){
            ////System.out.println("false na checkach");
        }
        JScrollPane scrollPane = new JScrollPane(table);
        ////System.out.println("size tabeli "+table==null);
        setLayout(new BorderLayout());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    static class ImageRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 66559534L;

        public ImageRenderer() { super(); }

        @Override
        public void setValue(Object value) {
            if(value !=null){
                setIcon(new ImageIcon((String)value));
            }
        }
    }
}


