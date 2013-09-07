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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes.RtcQueryAttributeManager;

/**
 *
 * @author Patryk Żywica
 */
public class RtcAddExpressionPanel extends JPanel implements ExplorerManager.Provider {

	private static final long serialVersionUID = -6172458274467539685L;
	RtcQueryAttributeManager attributeManager;
    ExplorerManager explorerManager = new ExplorerManager();

    public RtcAddExpressionPanel(RtcQueryAttributeManager manager) {
        super();
        this.attributeManager = manager;
        explorerManager.setRootContext(
                Lookup.getDefault().lookup(RtcQueryAttributeNodeFactory.class).getRootNode(manager));
        BeanTreeView btv = new BeanTreeView();
        btv.setRootVisible(false);
        setLayout(new BorderLayout());
        add(btv, BorderLayout.CENTER);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }
}
