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
package pl.edu.amu.wmi.kino.netbeans.multiview.impl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Provider;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewCloseHandler;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewDescription;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewElement;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup.KinoMultiViewGroupEvent;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewGroup.KinoMultiViewGroupListener;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewHeader;
import pl.edu.amu.wmi.kino.netbeans.multiview.KinoMultiViewTopComponentFactory.KinoMultiViewTopComponentTabPosition;

//TODO : bikol :complete callback support
//-fix can close handling, now are two independent and not fully implemented -
//in multiView element and in topcomponent
/**
 * 
 * @author Patryk Żywica
 */
public class KinoMultiViewCardLayoutTopComponent extends TopComponent {

	private static final long serialVersionUID = 1L;
	// kino multiView
	private KinoMultiViewGroup[] groups;
	private KinoMultiViewHeader header;
	private KinoMultiViewCloseHandler closeHandler;
	private KinoMultiViewTopComponentTabPosition position;
	// components
	private JPanel cardPanel, headerPanel, toolbarPanel;
	private CardLayout cardLayout;
	private ArrayList<KinoMultiViewDescription>[] descriptions;
	private HashMap<KinoMultiViewDescription, KinoMultiViewElement> elements = new HashMap<KinoMultiViewDescription, KinoMultiViewElement>();
	// toolbar panel
	private JComponent currentToolbar;
	private JPanel switchButtonsPanel;
	private HashMap<ButtonModel, KinoMultiViewDescription> buttonsToDesc = new HashMap<ButtonModel, KinoMultiViewDescription>();
	private ButtonGroup switchButtonGroup;
	private KinoMultiViewDescription selectedDescription;
	private KinoMultiViewDescription emptyDescription = new KinoMultiViewEmptyMVDescription();
	// component panel
	private HashMap<KinoMultiViewDescription, String> components = new HashMap<KinoMultiViewDescription, String>();
	private int uniqueComponentId = 0;
	// lookup
	private Lookup lookup;
	private Object lock = new Object();
	private String helpCtxId = "";

	public KinoMultiViewCardLayoutTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position, Lookup mainLookup,
			String helpCtx) {
		this(groups, header, closeHandler, position, mainLookup);
		this.helpCtxId = helpCtx;
	}

	public KinoMultiViewCardLayoutTopComponent(KinoMultiViewGroup[] groups,
			KinoMultiViewHeader header, KinoMultiViewCloseHandler closeHandler,
			KinoMultiViewTopComponentTabPosition position, Lookup mainLookup) {
		this.groups = groups;
		this.header = header;
		this.closeHandler = closeHandler;
		this.position = position;

		this.lookup = Lookups.proxy(new KinoMultiViewLookupProviderImpl(
				mainLookup));
		associateLookup(lookup);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		toolbarPanel = new JPanel(new BorderLayout());
		headerPanel = new JPanel(new BorderLayout());
		setLayout(new BorderLayout());
		add(headerPanel, BorderLayout.NORTH);
		add(cardPanel, BorderLayout.CENTER);

		headerPanel.setLayout(new BorderLayout());
		headerPanel.add(toolbarPanel, BorderLayout.NORTH);
		headerPanel.add(header, BorderLayout.CENTER);
		synchronized (lock) {
			descriptions = (ArrayList<KinoMultiViewDescription>[]) new ArrayList[groups.length + 1];

			switchButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,
					0));

			int nonZeroCheck = 0;
			for (int i = 0; i < groups.length; i++) {
				descriptions[i] = new ArrayList<KinoMultiViewDescription>();
				descriptions[i].addAll(Arrays.asList(groups[i]
						.getDescriptions()));
				nonZeroCheck += descriptions[i].size();
				groups[i].addListener(new KinoMultiViewGroupListenerImpl(i));
			}

			// adding loading group at the end of the list
			descriptions[groups.length] = new ArrayList<KinoMultiViewDescription>();
			descriptions[groups.length].add(emptyDescription);

			// if there is no descriptions to show then show loading desc
			if (nonZeroCheck == 0) {
				selectedDescription = descriptions[groups.length].get(0);
			} else {
				selectedDescription = descriptions[0].get(0);
			}
		}
		refreshSwitchButtons();

		toolbarPanel.add(switchButtonsPanel, BorderLayout.WEST);

	}

	@Override
	public Lookup getLookup() {
		return lookup;
	}

	@Override
	public HelpCtx getHelpCtx() {
		return new HelpCtx(helpCtxId);
	}

	private void showDescription(KinoMultiViewDescription desc) {
		// check if updating lookup is necessary here
		getLookup().lookup(Object.class);
		EventQueue.invokeLater(new KinoMultiViewTabSwitcher(desc));
	}

	private KinoMultiViewDescription getSelectedDescription() {
		return selectedDescription;
		// return buttonsToDesc.get(switchButtonGroup.getSelection());
	}

	private KinoMultiViewElement getMVElement(
			KinoMultiViewDescription description) {
		// TODO : pass MV callback there
		if (!elements.containsKey(description)) {
			elements.put(description, description.createElement());
		}
		return elements.get(description);
	}

	@Override
	public int getPersistenceType() {
		return TopComponent.PERSISTENCE_NEVER;
	}

	synchronized private void refreshSwitchButtons() {
		synchronized (lock) {
			switchButtonsPanel.removeAll();
			JToggleButton tmp;
			switchButtonGroup = new ButtonGroup();
			for (List<KinoMultiViewDescription> list : descriptions) {
				for (KinoMultiViewDescription d : list) {
					tmp = new KinoMultiViewSwitchButton(d);
					tmp.setBorderPainted(false);
					switchButtonsPanel.add(tmp);
					switchButtonGroup.add(tmp);
					buttonsToDesc.put(tmp.getModel(), d);

					tmp.addItemListener(new KinoMultiViewSwitchButtonItemListener(
							d));
					if (d.equals(selectedDescription)) {
						tmp.setSelected(true);
					}
				}
				JSeparator sep = new JSeparator(JSeparator.VERTICAL);
				switchButtonsPanel.add(sep);
			}
		}
		validate();
	}

	private class KinoMultiViewTabSwitcher implements Runnable {

		private KinoMultiViewDescription desc;

		public KinoMultiViewTabSwitcher(KinoMultiViewDescription desc) {
			this.desc = desc;
		}

		@Override
		public void run() {
			if (EventQueue.isDispatchThread()) {
				if (desc != null) {
					KinoMultiViewElement element = getMVElement(desc);
					// update toolbar
					if (currentToolbar != null) {
						toolbarPanel.remove(currentToolbar);
					}
					currentToolbar = element.getToolbarRepresentation();
					toolbarPanel.add(currentToolbar, BorderLayout.EAST);
					toolbarPanel.validate();
					toolbarPanel.repaint();
					headerPanel.validate();
					// update component
					String name;
					if (!components.containsKey(desc)) {
						name = Integer.toString(uniqueComponentId);
						uniqueComponentId++;
						components.put(desc, name);
						cardPanel.add(element.getVisualRepresentation(), name);
					} else {
						name = components.get(desc);
					}
					cardLayout.show(cardPanel, name);
					// post this on request processor to do non GUI updates
					RequestProcessor.getDefault().post(this);
				} else {
					// TODO : handle null desc here by showing empty component
				}
			} else {
				// after upadating all GUI it have to be called to update lookup
				// and fire events from it
				getLookup().lookup(Object.class);
			}
		}
	}

	private class KinoMultiViewGroupListenerImpl implements
			KinoMultiViewGroupListener {

		private int index;

		KinoMultiViewGroupListenerImpl(int index) {
			this.index = index;
		}

		@Override
		public void eventFired(KinoMultiViewGroupEvent event) {
			synchronized (lock) {
				descriptions[index].clear();
				descriptions[index].addAll(Arrays.asList(groups[index]
						.getDescriptions()));
				if (descriptions[index].size() > 0) {
					if (selectedDescription == emptyDescription) {
						// if currently selected is empty and we added new one
						// change selected and remove empty
						selectedDescription = descriptions[index].get(0);
						descriptions[groups.length].clear();
					}
				}
			}
			// handle upadate in MV switch buttons
			refreshSwitchButtons();
		}
	}

	private class KinoMultiViewLookupProviderImpl extends ProxyLookup implements
			Provider {
		private Lookup lookup;

		KinoMultiViewLookupProviderImpl(Lookup lookup) {
			super(lookup);
			this.lookup = lookup;
		}

		@Override
		public Lookup getLookup() {
			KinoMultiViewDescription desc = getSelectedDescription();
			if (desc != null) {
				KinoMultiViewElement element = getMVElement(desc);
				this.setLookups(element.getLookup(), lookup);
			}
			return this;
		}
	}

	private class KinoMultiViewSwitchButtonItemListener implements ItemListener {

		private KinoMultiViewDescription desc;

		public KinoMultiViewSwitchButtonItemListener(
				KinoMultiViewDescription desc) {
			this.desc = desc;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedDescription = desc;
				showDescription(desc);
			}
			if (e.getStateChange() == ItemEvent.DESELECTED) {
			}
		}
	}
}
