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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;

import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.NodePopupFactory;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeValue;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.values.RtcQueryAttributeValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableAttributeExpression;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.RtcOutline;

/**
 *
 * @author Patryk Żywica
 * @author Szymon Sadlo
 */
public class PossibleValuesSelectionPanel extends JPanel implements ExplorerManager.Provider {

	private static final long serialVersionUID = 5992769642202225602L;
	private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JTextField jTextField1;
    private RtcQueryAttributeValueProvider values;
    private ExplorerManager manager;
    private RtcEditableAttributeExpression expression;
    private ContributorProviderNode cpn;

    public PossibleValuesSelectionPanel(RtcQueryAttributePossibleValues poss, RtcEditableAttributeExpression expression) {
        values = poss;
        this.expression = expression;
        manager = new ExplorerManager();
        initComponents();
    }

    public RtcEditableAttributeExpression getExpression() {
        return expression;
    }

    public HashMap<RtcQueryAttributeValue, Boolean> getSelectedValues() {
        return ContributorSelectedValuesMap.getMap();
    }

    private void initComponents() {
        RtcOutline outlineView = new RtcOutline();
        NodePopupFactory npf = new NodePopupFactory();
        npf.setShowQuickFilter(true);
        outlineView.setNodePopupFactory(npf);
        outlineView.setPreferredSize(new Dimension(940, 250));
        outlineView.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        Outline outline = outlineView.getOutline();
        outline.setRootVisible(false);
        outline.setShowGrid(false);
        outline.setShowHorizontalLines(false);
        outline.setShowVerticalLines(false);
        outline.setTableHeader(null);
        outlineView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outlineView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        /* HEADER */
        //TODO: Szymon : naprawić layout
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(940, 66));
        header.setBackground(Color.WHITE);
        JPanel header_left = new JPanel(new GridBagLayout());
        header_left.setPreferredSize(new Dimension(865, 66));
        header_left.setBackground(Color.white);
        GridBagConstraints headerCon = new GridBagConstraints();
        headerCon.fill = GridBagConstraints.BASELINE;
        headerCon.gridx = 0;
        headerCon.gridy = 0;
        JLabel he = new JLabel(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "HeaderLabel.name"));
        he.setFont(new Font("Tahoma", Font.BOLD, 15));
        header_left.add(he, headerCon);
        headerCon.fill = GridBagConstraints.BASELINE;
        headerCon.gridx = 0;
        headerCon.gridy = 1;
        header_left.add(new JLabel(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "DescriptionLabel.name")), headerCon);
        JPanel header_right = new JPanel(new BorderLayout());
        header_right.setPreferredSize(new Dimension(75, 66));
        header_right.add(new JLabel(new ImageIcon(getClass().getResource("/pl/edu/amu/wmi/kino/rtc/client/queries/editor/factories/enumeration/contributor.gif"))),
                BorderLayout.EAST);
        header.add(header_left, BorderLayout.WEST);
        header.add(header_right, BorderLayout.EAST);
        header.revalidate();
        /* HEADER */

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jTextField1 = new JTextField();
        jLabel2 = new JLabel();
        jPanel2 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();

        setName("Form"); // NOI18N

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setForeground(new Color(255, 255, 255));
        jPanel1.setMinimumSize(new Dimension(940, 66));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new Dimension(940, 66));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(header, GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(header, GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE));

        jLabel1.setText(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "SearchLabel.name"));
        jLabel1.setName("jLabel1"); // NOI18N

        jTextField1.setText(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "SearchFieldDefault.name"));
        jTextField1.setName("jTextField1"); // NOI18N
        /*jTextField1.addKeyListener(new KeyAdapter() {

        @Override
        public void keyTyped(KeyEvent e) {
        cpn = new ContributorProviderNode(values, expression, jTextField1.getText());
        //}
        manager.setRootContext(cpn);
        }
        });*/
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(922, 250));
        jPanel2.validate();

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(outlineView, GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(outlineView, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE));

        jButton1.setText(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "SelectAll.name"));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        cpn = new ContributorProviderNode(values, expression, Boolean.TRUE);
                        manager.setRootContext(cpn);
                    }
                });
            }
        });

        jButton2.setText(NbBundle.getMessage(PossibleValuesSelectionPanel.class, "DeselectAll.name"));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        cpn = new ContributorProviderNode(values, expression, Boolean.FALSE);
                        manager.setRootContext(cpn);
                    }
                });
            }
        });


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)/*.addComponent(jLabel1).addComponent(jTextField1, GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)*/.addComponent(jLabel2).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1))).addContainerGap()).addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(17, 17, 17)/*.addComponent(jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)*/.addComponent(jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2)).addContainerGap(30, Short.MAX_VALUE)));

        cpn = new ContributorProviderNode(values, expression);
        manager.setRootContext(cpn);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
}
