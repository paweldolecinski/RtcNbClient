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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor.panels;
import org.openide.util.NbBundle;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node.Property;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttribute;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryColumn;

/**
 *
 * @author Szymon Sad�o
 */
public class ResultLayoutPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

	private static final long serialVersionUID = -3377295888180582320L;
	private RtcQuery query;
    private RtcQueryAttribute[] attr;
    private RtcQueryColumn[] col;
    private ArrayList<String> allColumnsList;
    private ArrayList<RtcQueryColumn> sortingColumnsList;
    private ArrayList<String> sortingAvailColumnsList;
    private ArrayList<String> choosenColumnsList;
    private ExplorerManager manager;
    private Outline outline;
    private ArrayList sortingAvailDefaultColumnsList;
    private ArrayList allDefaultColumnsList;

    public ResultLayoutPanel(RtcQuery query) {
        this.query = query;
        manager = new ExplorerManager();
        allColumnsList = new ArrayList();
        allDefaultColumnsList = new ArrayList();
        choosenColumnsList = new ArrayList();
        sortingColumnsList = new ArrayList();
        sortingAvailColumnsList = new ArrayList();
        sortingAvailDefaultColumnsList = new ArrayList();
        initComponents();
        loadComponentData();
    }

    private void loadComponentData() {
        outline = outlineView1.getOutline();
        Property[] props = new Property[]{
            new SortingIndexProperty(manager.getExploredContext().getLookup().lookup(RtcQueryColumn.class))
        };

        outlineView1.setProperties(props);
        outline.setRootVisible(false);

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                attr = query.getQueriesManager().getQueryAttributeManager().getQueryAttributes();
                col = query.getColumns();

                for (int i = 0; i < col.length; i++) {
                    choosenColumnsList.add(col[i].getColumnDisplayName());
                }

                for (int i = 0; i < attr.length; i++) {
                    allColumnsList.add(attr[i].getDisplayName());
                }
                allColumnsList.removeAll(choosenColumnsList);
                allDefaultColumnsList.addAll(allColumnsList);

                for (int i = 0; i < col.length; i++) {
                    if (col[i].isSortColumn()) {
                        sortingColumnsList.add(col[i]);
                    }
                }

                for (int i = 0; i < attr.length; i++) {
                    sortingAvailColumnsList.add(attr[i].getDisplayName());
                }
                sortingAvailDefaultColumnsList.addAll(sortingAvailColumnsList);

                jList2.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!jList2.isSelectionEmpty()) {
                            jButton1.setEnabled(true);
                            jButton2.setEnabled(true);
                            jButton5.setEnabled(true);
                        } else {
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton5.setEnabled(false);
                        }
                    }
                });

                jList1.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!jList1.isSelectionEmpty()) {
                            jButton4.setEnabled(true);
                        } else {
                            jButton4.setEnabled(false);
                        }
                    }
                });

                jList3.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!jList3.isSelectionEmpty()) {
                            jButton9.setEnabled(true);
                        } else {
                            jButton9.setEnabled(false);
                        }
                    }
                });

                outline.addFocusListener(new FocusListener() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        if (outline.getSelectedRow() != -1) {
                            jButton13.setEnabled(true);
                            jButton14.setEnabled(true);
                            jButton10.setEnabled(true);
                        }
                        else {
                            jButton13.setEnabled(false);
                            jButton14.setEnabled(false);
                            jButton10.setEnabled(false);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        
                    }
                });

                refreshList1();
                refreshList2();
                refreshList3();
                refreshOutline();
            }
        });
    }

    private void refreshList1() {
        jList1.setModel(new javax.swing.AbstractListModel() {

			private static final long serialVersionUID = -8233655698040987119L;
			String[] strings = allColumnsList.toArray(new String[allColumnsList.size()]);

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void refreshList2() {
        jList2.setModel(new javax.swing.AbstractListModel() {

			private static final long serialVersionUID = -7368214351793251059L;
			String[] strings = choosenColumnsList.toArray(new String[choosenColumnsList.size()]);

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void refreshList3() {
        jList3.setModel(new javax.swing.AbstractListModel() {

			private static final long serialVersionUID = -541254443901964752L;
			String[] strings = sortingAvailColumnsList.toArray(new String[sortingAvailColumnsList.size()]);

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void refreshOutline() {
        manager.setRootContext(new ResultLayoutSortingNode(sortingColumnsList));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        outlineView1 = new org.openide.explorer.view.OutlineView();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel2 = new GradientJPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new GradientJPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1050, 611));

        jLabel1.setForeground(new java.awt.Color(25, 76, 127));
        jLabel1.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel1.text")); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
			private static final long serialVersionUID = 9069767891306440324L;
			String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel() {
			private static final long serialVersionUID = -7671542899784761030L;
			String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jButton1.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton1.text")); // NOI18N
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton2.text")); // NOI18N
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton4.text")); // NOI18N
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton5.text")); // NOI18N
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton6.text")); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton7.text")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(25, 76, 127));
        jLabel2.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel2.text")); // NOI18N

        jLabel3.setForeground(new java.awt.Color(25, 76, 127));
        jLabel3.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel3.text")); // NOI18N

        jList3.setModel(new javax.swing.AbstractListModel() {
			private static final long serialVersionUID = -4224373266399317147L;
			String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jButton8.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton8.text")); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton9.text")); // NOI18N
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton10.text")); // NOI18N
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton11.text")); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton12.text")); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        outlineView1.setBorder(null);

        jButton13.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton13.text")); // NOI18N
        jButton13.setEnabled(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jButton14.text")); // NOI18N
        jButton14.setEnabled(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel4.setForeground(new java.awt.Color(25, 76, 127));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(947, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setForeground(jLabel4.getForeground());
        jLabel5.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addContainerGap(960, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel6.setForeground(new java.awt.Color(25, 76, 127));
        jLabel6.setText(NbBundle.getMessage(ResultLayoutPanel.class, "ResultLayoutPanel.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(14, 14, 14)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2)))
                                .addComponent(jLabel2)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(outlineView1, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton14))))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                        .addGap(73, 73, 73)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(outlineView1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14)))))
                .addGap(51, 51, 51))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //Columns ">" button
        List selected = Arrays.asList(jList1.getSelectedValues());
        allColumnsList.removeAll(selected);
        choosenColumnsList.addAll(selected);
        refreshList1();
        refreshList2();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Columns "Set defaults" button
        ArrayList defaults = new ArrayList();
        defaults.add("Typ");
        defaults.add("Identyfikator");
        defaults.add("Status");
        defaults.add("Priorytet");
        defaults.add("Istotność");
        defaults.add("Podsumowanie");
        defaults.add("Należy do");
        defaults.add("Utworzone przez");
        allColumnsList.clear();
        allColumnsList.addAll(allDefaultColumnsList);
        choosenColumnsList.clear();
        choosenColumnsList.addAll(defaults);
        refreshList1();
        refreshList2();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //Sorting "Set defaults" button
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //Columns "<" button
        List selected = Arrays.asList(jList2.getSelectedValues());
        allColumnsList.addAll(selected);
        choosenColumnsList.removeAll(selected);
        refreshList1();
        refreshList2();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //Columns ">>" button
        int start = 0;
        int end = jList1.getModel().getSize() - 1;
        jList1.setSelectionInterval(start, end);
        List selected = Arrays.asList(jList1.getSelectedValues());
        allColumnsList.removeAll(selected);
        choosenColumnsList.addAll(selected);
        refreshList1();
        refreshList2();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //Columns "<<" button
        int start = 0;
        int end = jList2.getModel().getSize() - 1;
        jList2.setSelectionInterval(start, end);
        List selected = Arrays.asList(jList2.getSelectedValues());
        allColumnsList.addAll(selected);
        choosenColumnsList.removeAll(selected);
        refreshList1();
        refreshList2();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Columns "Up" button
        String selected = (String) jList2.getSelectedValue();
        int ind = choosenColumnsList.indexOf(selected);
        if (ind > 0) {
            String chcopy = choosenColumnsList.get(ind - 1);
            choosenColumnsList.set(ind - 1, choosenColumnsList.get(ind));
            choosenColumnsList.set(ind, chcopy);
            jList2.setSelectedIndex(ind - 1);
        }
        refreshList2();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Columns "Down" button
        String selected = (String) jList2.getSelectedValue();
        int ind = choosenColumnsList.indexOf(selected);
        if (ind < choosenColumnsList.size()) {
            String chcopy = choosenColumnsList.get(ind + 1);
            choosenColumnsList.set(ind + 1, choosenColumnsList.get(ind));
            choosenColumnsList.set(ind, chcopy);
            jList2.setSelectedIndex(ind + 1);
        }
        refreshList2();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private org.openide.explorer.view.OutlineView outlineView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    class GradientJPanel extends JPanel {

		private static final long serialVersionUID = 3321329906843469369L;

		@Override
        public void paintComponent(Graphics g) {
            int panelHeight = getHeight();
            int panelWidth = getWidth();
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(250, -350 + panelHeight, Color.decode("#194C7F"), 250, panelHeight, Color.white);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }
    }
}
