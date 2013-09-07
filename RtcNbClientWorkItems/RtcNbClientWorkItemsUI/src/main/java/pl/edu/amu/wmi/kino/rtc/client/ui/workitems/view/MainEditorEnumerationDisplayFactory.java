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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler.OptionChooser;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.DateDisplay;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.EnumerationDisplay;

/**
 * 
 * @author Patryk Żywica
 * @author Bartosz Zaleski
 */
public class MainEditorEnumerationDisplayFactory implements DisplayFactory {

	@Override
	public <D extends Display> D createDisplay(Class<D> displayType,
			Lookup lookup) {
		if (displayType.equals(EnumerationDisplay.class)) {
			return displayType.cast(new MainEditorEnumerationDisplay());
		}
		return null;
	}

	private static class MainEditorEnumerationDisplay extends JPanel implements
			EnumerationDisplay, SwingDisplay {

		private static final long serialVersionUID = 3421243999997998L;
		private JLabel label;
		private JLabel icon;
		private JComboBox jComboBox1;
		private JPanel dialogPanel;
		private final List<InputHandler<?>> inputHandlers = Collections
				.synchronizedList(new LinkedList<InputHandler<?>>());
		private final Map<OptionChooser, List<OptionChooseHandler>> optionChooseHandlers = Collections
				.synchronizedMap(new HashMap<OptionChooser, List<OptionChooseHandler>>());
		private Map<Node, EnumerationDisplayElement> elements = new HashMap<Node, EnumerationDisplayElement>(
				8);

		MainEditorEnumerationDisplay() {
			super(new GridBagLayout());
			jComboBox1 = new JComboBox();
			icon = new JLabel();
			label = new JLabel();
			setOpaque(false);
			icon.setOpaque(false);
			label.setOpaque(false);

			GridBagConstraints c = new GridBagConstraints();

			JPanel leftPanel = new JPanel(new BorderLayout());
			leftPanel.setPreferredSize(new Dimension(175, 20));
			leftPanel.setOpaque(false);
			leftPanel.add(label, BorderLayout.WEST);
			leftPanel.add(icon, BorderLayout.EAST);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			add(leftPanel, c);

			JPanel rightPanel = new JPanel(new BorderLayout());
			rightPanel.add(jComboBox1, BorderLayout.CENTER);
			rightPanel.setOpaque(false);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 0.5;
			add(rightPanel, c);

			jComboBox1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized (optionChooseHandlers) {
						List<OptionChooseHandler> list = optionChooseHandlers
								.get(EnumerationDisplay.BASIC_SEARCH_OPTION);
						for (OptionChooseHandler h : list) {
							h.optionChosen(jComboBox1.getSelectedIndex());
						}
					}

				}
			});
		}

		@Override
		public void setBasicOptions(Node[] nodes) {
			for (Node n : nodes) {
				if (!elements.containsKey(n)) {
					// TODO : myśle ze ostatecznie to trzeba bedzie
					// zaimplementować własny list model
					// dla tego jComboBox1
					elements.put(n, new EnumerationDisplayElement(n));
					jComboBox1.addItem(elements.get(n));
				}
			}
		}

		@Override
		public void setExpandedOptions(Node rootNode, Node[] selectableNodes) {
			jComboBox1.insertItemAt(rootNode.getDisplayName(),
					jComboBox1.getItemCount());

			dialogPanel = new JPanel();
			dialogPanel.setLayout(new BorderLayout());
			JPanel filtrationPanel = new JPanel();
			filtrationPanel.setLayout(new BoxLayout(filtrationPanel,
					BoxLayout.PAGE_AXIS));
			filtrationPanel.add(new JLabel(NbBundle.getMessage(
					MainEditorEnumerationDisplayFactory.class,
					"JLabelFiltration")));
			filtrationPanel.add(new JTextField());
			dialogPanel.add(filtrationPanel, BorderLayout.NORTH);
			JPanel displayPanel = new JPanel();
			displayPanel.setLayout(new BoxLayout(displayPanel,
					BoxLayout.PAGE_AXIS));
			// Budowa listy
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
			JTree tree = new JTree(root);
			JScrollPane displayResults = new JScrollPane(tree);
			// Tu coĹ› jeszcze
			displayPanel
					.add(new JLabel(NbBundle.getMessage(
							MainEditorEnumerationDisplayFactory.class,
							"JLabelResults")));
			displayPanel.add(displayResults);
			dialogPanel.add(displayPanel, BorderLayout.SOUTH);

		}

		@Override
		public void showExpandedOptionsDialog() {
			DialogDescriptor dd = new DialogDescriptor(dialogPanel, "Search");
			Object retval = DialogDisplayer.getDefault().notify(dd);
		}

		@Override
		public HandlerRegistration addOptionHandler(OptionChooser source,
				final OptionChooseHandler h) {
			if (EnumerationDisplay.BASIC_SEARCH_OPTION.equals(source)) {
				if (!optionChooseHandlers.containsKey(source)) {
					optionChooseHandlers
							.put(source,
									Collections
											.synchronizedList(new LinkedList<OptionChooseHandler>()));
				}
				final List<OptionChooseHandler> list = optionChooseHandlers
						.get(source);

				list.add(h);
				return new HandlerRegistration(new Runnable() {

					@Override
					public void run() {
						list.remove(h);
					}
				});
			} else {
				return null;
			}
		}

		@Override
		public <T> HandlerRegistration addInputHandler(Input<T> input,
				final InputHandler<T> h) {
			if (input.equals(EnumerationDisplay.EXPANDED_SEARCH_INPUT)) {
				inputHandlers.add(h);
				return new HandlerRegistration(new Runnable() {

					@Override
					public void run() {
						inputHandlers.remove(h);
					}
				});
			}
			return null;
		}

		@Override
		public JComponent asComponent() {
			return this;
		}

		@Override
		public void setLabel(String labelText) {
			this.label.setText(labelText);
		}

		public void setSelected(Node node) {
			if (node.getIcon(BeanInfo.ICON_COLOR_32x32) != null) {
				icon.setIcon(new ImageIcon(node
						.getIcon(BeanInfo.ICON_COLOR_32x32)));
			}
			if (elements.containsKey(node)) {
				jComboBox1.setSelectedItem(elements.get(node));
			}
		}

		public void setId(String id) {
		}

	}

	private static class EnumerationDisplayElement {

		private Node node;

		EnumerationDisplayElement(Node node) {
			this.node = node;
		}

		@Override
		public String toString() {
			return node.getDisplayName();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final EnumerationDisplayElement other = (EnumerationDisplayElement) obj;
			if (this.node != other.node
					&& (this.node == null || !this.node.equals(other.node))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 97 * hash + (this.node != null ? this.node.hashCode() : 0);
			return hash;
		}
	}
}