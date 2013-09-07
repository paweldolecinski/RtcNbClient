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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.presenter.attributes;

import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.WorkItemLayoutField;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider.Value;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.LayoutFieldPresenterFactory;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.EnumerationDisplay;

/**
 * 
 * @author Paweł Doleciński
 */
@ServiceProvider(service = LayoutFieldPresenterFactory.class, path = "Rtc/Modules/WorkItems/Editor/Presenter")
public class BasicEnumerationPresenterFactory implements
		LayoutFieldPresenterFactory<EnumerationDisplay> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
	 * LayoutFieldPresenterFactory #canCreate(pl.edu.amu.wmi.kino.rtc.client
	 * .facade.api.workitems.RtcWorkItemAttribute)
	 */
	public double canCreate(WorkItemLayoutField field) {
		if (field.getType().equals(WorkItemLayoutField.Type.ATTRIBUTE)) {
			RtcWorkItemAttribute<?> attr = field.getLookup().lookup(
					RtcWorkItemAttribute.class);
			if (attr.getLookup().lookup(PrefferedValues.class) != null) {
				return .5;
			}
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
	 * LayoutFieldPresenterFactory
	 * #createPresenter(pl.edu.amu.wmi.kino.netbeans.mvp.client.Display,
	 * pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemAttribute,
	 * pl
	 * .edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemWorkingCopy)
	 */
	public Presenter<EnumerationDisplay> createPresenter(
			EnumerationDisplay display, WorkItemLayoutField field,
			RtcWorkItemWorkingCopy wiwc) {
		if (canCreate(field) > 0.0) {
			RtcWorkItemAttribute<?> attr = field.getLookup().lookup(
					RtcWorkItemAttribute.class);
			return doCreatePresenter(display, attr, wiwc);
		}
		throw new IllegalStateException();
	}

	private <T> Presenter<EnumerationDisplay> doCreatePresenter(
			EnumerationDisplay display, RtcWorkItemAttribute<T> attribute,
			RtcWorkItemWorkingCopy wiwc) {
		return new BasicEnumerationPresenter<T>(display, attribute, wiwc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.
	 * LayoutFieldPresenterFactory#getDisplayType()
	 */
	public Class<EnumerationDisplay> getDisplayType() {
		return EnumerationDisplay.class;
	}

	/**
	 * @author Paweł Doleciński
	 * 
	 */
	private static class BasicEnumerationPresenter<T> extends
			BasicPresenter<EnumerationDisplay> {

		private RtcWorkItemWorkingCopy wiwc;
		private RtcWorkItemAttribute<T> attribute;
		private Map<T, Node> nodes = new HashMap<T, Node>(8);
		private List<T> basicValues = new LinkedList<T>();

		/**
		 * @param display
		 * @param attribute
		 * @param wiwc
		 */
		BasicEnumerationPresenter(EnumerationDisplay display,
				RtcWorkItemAttribute<T> attribute, RtcWorkItemWorkingCopy wiwc) {
			super(display);
			this.wiwc = wiwc;
			this.attribute = attribute;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onBind()
		 */
		@Override
		protected void onBind() {

			new RequestProcessor().post(new Runnable() {

				@Override
				public void run() {
					PrefferedValues<?> pref = attribute.getLookup().lookup(
							PrefferedValues.class);
					Value<?>[] values = pref.getValues();
					doSetOptions((Value<T>[]) values, false);
				}
			});
			new RequestProcessor().post(new Runnable() {

				@Override
				public void run() {
					PossibleValues<?> poss = attribute.getLookup().lookup(
							PossibleValues.class);
					if (poss != null) {
						Value<?>[] values = poss.getValues();
						doSetOptions((Value<T>[]) values, true);
					}
				}
			});

			registerHandler(getDisplay().addOptionHandler(
					EnumerationDisplay.BASIC_SEARCH_OPTION,
					new OptionChooseHandler() {

						@Override
						public void optionChosen(int option) {
							setValue(option);
							RtcLogger
									.getLogger(BasicEnumerationPresenter.class)
									.info("option " + option);
						}
					}));

			getDisplay().setId(attribute.getId());
			getDisplay().setLabel(attribute.getDisplayName());

		}

		private void setValue(int value) {
			T t = basicValues.get(value);
			RtcLogger.getLogger(BasicEnumerationPresenter.class).info("t " + t);
			wiwc.setValue(attribute, t);
			Node n = nodes.get(t);
			if (n != null) {
				RtcLogger.getLogger(BasicEnumerationPresenter.class).info(
						"n " + n);
				getDisplay().setSelected(n);
			}
		}

		private void doSetOptions(ValueProvider.Value<T>[] values,
				boolean extended) {
			Node[] ret = new Node[values.length];
			for (int i = 0; i < values.length; i++) {
				nodes.put(values[i].getValue(), new EnumerationNode(values[i]));
				ret[i] = nodes.get(values[i].getValue());
				if (extended) {

				} else {
					basicValues.add(values[i].getValue());
				}
			}
			if (extended) {
				getDisplay().setExpandedOptions(ret[0], ret);
			} else {

				getDisplay().setBasicOptions(ret);
				Node n = nodes.get(wiwc.getValue(attribute));
				if (n != null) {
					getDisplay().setSelected(n);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * pl.edu.amu.wmi.kino.netbeans.mvp.client.BasicPresenter#onUnbind()
		 */
		@Override
		protected void onUnbind() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * pl.edu.amu.wmi.kino.netbeans.mvp.client.Presenter#refreshDisplay()
		 */
		public void refreshDisplay() {
		}

		private static class EnumerationNode extends AbstractNode {

			private Image icon;

			EnumerationNode(final ValueProvider.Value<?> val) {
				super(Children.create(
						new ChildFactory<ValueProvider.Value<?>>() {

							@Override
							protected boolean createKeys(
									List<Value<?>> toPopulate) {
								toPopulate.addAll(Arrays.asList(val
										.getChildren()));
								return true;
							}

							@Override
							protected Node createNodeForKey(Value<?> key) {
								return new EnumerationNode(key);
							}
						}, true));
				setDisplayName(val.getDisplayName());
				this.icon = val.getIcon();
			}

			@Override
			public Image getIcon(int type) {
				return icon;
			}
		}
	}
}
