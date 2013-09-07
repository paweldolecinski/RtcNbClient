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
package pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider;

import java.awt.EventQueue;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.DevelopmentLine;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.Iteration;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.process.ProcessManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.PrefferedValues;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.ValueProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.common.process.ProcessManagerImpl;
import pl.edu.amu.wmi.kino.rtc.client.facade.impl.connections.ActiveProjectAreaImpl;

import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IDevelopmentLine;
import com.ibm.team.process.common.IIterationHandle;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;

/**
 * @author Paweł Doleciński
 * 
 */
@ServiceProvider(service = AttributeContentProvider.class, path = "Rtc/Modules/WorkItems/Impl/AttributeContent")
public class IterationAttributeContentProvider implements
		AttributeContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.edu.amu.wmi.kino.rtc.client.facade.impl.workitems.attributes.provider
	 * .WorkItemAttributeContentProvider
	 * #createLookup(com.ibm.team.workitem.common.model.IAttribute,
	 * pl.edu.amu.wmi.kino.rtc.client.impl.connections.ActiveProjectAreaImpl)
	 */
	@Override
	public Lookup createLookup(IAttribute ia, ActiveProjectAreaImpl area) {
		if (AttributeTypes.ITERATION.equals(ia.getAttributeType())) {
			InstanceContent ic = new InstanceContent();
			Lookup lookup = new AbstractLookup(ic);
			ic.add(new IterationPossibleValues(area));
			ic.add(new IterationPrefferedValues(area, ia));
			return lookup;
		} else {
			return null;
		}
	}

	/**
	 * @author Paweł Doleciński
	 * 
	 */
	private static class IterationPrefferedValues implements
			PrefferedValues<Iteration> {

		private IAttribute ia;
		private ActiveProjectArea area;

		public IterationPrefferedValues(ActiveProjectArea area, IAttribute ia) {
			assert ia != null : "EnumerationPrefferedValues: argument in constructor cannnot be null";
			this.ia = ia;
			this.area = area;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.attributes.
		 * PrefferedValues#getValues()
		 */
		@Override
		public ValueProvider.Value<Iteration>[] getValues() {
			assert (!EventQueue.isDispatchThread());
			List<ValueProvider.Value<Iteration>> ret = new LinkedList<ValueProvider.Value<Iteration>>();
			ITeamRepository repo = ((ITeamRepository) ia.getOrigin());
			IProcessItemService pis = (IProcessItemService) repo
					.getClientLibrary(IProcessItemService.class);
			try {
				List<?> allItems = pis.fetchCompleteIterationStructure(
						ia.getProjectArea(), null);
				ProcessManager processManager = area.getLookup().lookup(
						ProcessManager.class);
				if (processManager instanceof ProcessManagerImpl) {
					ProcessManagerImpl pm = (ProcessManagerImpl) processManager;

					for (Object item : allItems) {
						if (item instanceof IIterationHandle) {
							IIterationHandle iteration = (IIterationHandle) item;
							Iteration iter = pm.findIteration(iteration);
							if (!iter.isArchived()) {
								ValueImpl val = new ValueImpl(iter);
								ret.add(val);
							}
						} else if (item instanceof IDevelopmentLine) {
							// IDevelopmentLine devLine = (IDevelopmentLine)
							// item;
							// devLine.getIterations();
						}

					}
				}
			} catch (TeamRepositoryException ex) {
				RtcLogger.getLogger(IterationPrefferedValues.class).log(Level.SEVERE,
						ex.getLocalizedMessage(), ex);
			}
			@SuppressWarnings("unchecked")
			ValueProvider.Value<Iteration>[] r = ret
					.toArray(new ValueProvider.Value[ret.size()]);
			return r;
		}

		private final class ValueImpl implements ValueProvider.Value<Iteration> {
			private final Iteration val;

			private ValueImpl(Iteration iteration) {
				this.val = iteration;
			}

			@Override
			public Iteration getValue() {
				return val;
			}

			@Override
			public String getDisplayName() {
				return val.getName();
			}

			@Override
			public Image getIcon() {
				return null;
			}

			@Override
			public Value<Iteration>[] getChildren() {
				List<Value<Iteration>> ret = new LinkedList<ValueProvider.Value<Iteration>>();

				@SuppressWarnings("unchecked")
				ValueProvider.Value<Iteration>[] r = ret
						.toArray(new ValueProvider.Value[ret.size()]);
				return r;
			}

			@Override
			public boolean isSelectable() {
				return true;
			}
		}

	}

	/**
	 * @author Paweł Doleciński
	 * 
	 */
	class IterationPossibleValues implements PossibleValues<DevelopmentLine> {

		private ActiveProjectAreaImpl area;

		public IterationPossibleValues(ActiveProjectAreaImpl area) {
			this.area = area;

		}

		@Override
		public ValueProvider.Value<DevelopmentLine>[] getValues() {
			DevelopmentLine[] l = area.getLookup().lookup(ProcessManager.class)
					.getDevelopmentLines();
			List<ValueProvider.Value<DevelopmentLine>> ret = new LinkedList<ValueProvider.Value<DevelopmentLine>>();

			for (int i = 0; i < l.length; i++) {
				ret.add(new ValueImpl(l[i]));
			}

			@SuppressWarnings("unchecked")
			ValueProvider.Value<DevelopmentLine>[] r = ret
					.toArray(new ValueProvider.Value[ret.size()]);

			return r;
		}

		// FIXME
		private final class ValueImpl implements
				ValueProvider.Value<DevelopmentLine> {
			private final DevelopmentLine val;

			private ValueImpl(DevelopmentLine val) {
				this.val = val;
			}

			@Override
			public DevelopmentLine getValue() {
				return val;
			}

			@Override
			public String getDisplayName() {
				return val.getName();
			}

			@Override
			public Image getIcon() {
				return null;
			}

			@Override
			public Value<DevelopmentLine>[] getChildren() {
				List<Value<DevelopmentLine>> ret = new LinkedList<ValueProvider.Value<DevelopmentLine>>();

				@SuppressWarnings("unchecked")
				ValueProvider.Value<DevelopmentLine>[] r = ret
						.toArray(new ValueProvider.Value[ret.size()]);
				return r;
			}

			@Override
			public boolean isSelectable() {
				return true;
			}
		}

	}

}
