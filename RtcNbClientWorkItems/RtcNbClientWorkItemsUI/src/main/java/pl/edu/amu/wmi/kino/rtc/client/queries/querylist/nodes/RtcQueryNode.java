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
package pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes;
import org.openide.util.NbBundle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery.RtcQueryEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQuery;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;

/**
 * Used for showing Rtc Queries in any explorer view for example in RTC
 * panel in Work Items/My Queries.
 * <p>
 * This node takes its actions from <code>layer.xml</code>. All actions registered in
 * Rtc/Modules/Queries/QueryActions will be shown in <code>QueryNode</code>'s context menu.
 * </p><p>
 * Action with the lowest position attribute is used as default action for node.
 * OpenAction is declared with position equal to 100, so if you declare action with
 * position less then 100 your action will become default one.
 * </p>
 * @author Patryk Żywica
 */
public class RtcQueryNode extends AbstractNode implements EventListener<RtcQueryEvent> {

    private Action[] actions;
    private InstanceContent content;
    private SaveCookie saveCookie = new SaveCookieImpl();
    private boolean isSaveCookieSet = false;

    public RtcQueryNode(InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        this.content = content;

        RtcQuery query = getLookup().lookup(RtcQuery.class);
        if (query == null) {
            throw new IllegalStateException(NbBundle.getMessage(RtcQueryNode.class, "RtcQueryNodewithoutLookup"));
        }
        query.addListener(this);
        //set this to enable query assabler, you have to change some other classes in model
        //and uncomment few lines in QueryChildren.
        //setChildren(new QueryChildren(getLookup()));
    }

    /** Gets the preferred action for this node.
     * This action can but need not to be one from the action array returned
     * from {@link #getActions(boolean)}.
     * In case it is, the context menu created from those actions
     * is encouraged to highlight the preferred action.
     * <p>
     * This implementation returns getActions(true)[0].
     * </p>
     * @return the preferred action, or <code>null</code> if there is none
     * actions in getAction(true)
     */
    @Override
    public Action getPreferredAction() {
        return getActions(true).length > 0 ? getActions(true)[0] : null;
    }

    /**
     * Returns all actions registered in layer.xml under
     * Rtc/Moduler/Queries/QueryActions directory. Actions are sorted by
     * position atribute.
     *
     * @param context whether to find actions for context meaning or for the
     *   node itself
     * @return a list of actions (may include nulls for separators)
     */
    @Override
    public Action[] getActions(boolean context) {
        if (actions == null) {
            List<? extends Action> list = Utilities.actionsForPath("Rtc/Modules/QueriesModule/QueryActions");
            actions = list.toArray(new Action[]{});
        }
        return actions;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = Sheet.createPropertiesSet();

        Property<String> prop = new RtcQueryNamePropImpl(String.class, getLookup().lookup(RtcQuery.class));
        set.put(prop);

        sheet.put(set);
        return sheet;
    }

    @Override
    public void eventFired(RtcQueryEvent event) {
        RtcQuery query = getLookup().lookup(RtcQuery.class);
        RtcEditableQuery eQuery = getLookup().lookup(RtcEditableQuery.class);
        if (eQuery != null) {
            //in case of any modification
            if (eQuery.isModified() && !isSaveCookieSet) {
                content.add(saveCookie);
                isSaveCookieSet = true;
            }
        }
        switch (event) {
            case STATEMENT_CHANGED:
                break;
            case EDITABLE_NAME_CHANGED:
                firePropertyChange("editableName", null, null);
                break;
            case NAME_CHANGED:
                setDisplayName(query.getName());
                break;
            case QUERY_SYNCHRONIZED_WITH_SERVER:
                firePropertyChange("editableName", null, null);
                setDisplayName(query.getName());
                break;
            case CHANGES_DISCARDED:
                firePropertyChange("editableName", null, null);
            case QUERY_SAVED:
                setDisplayName(query.getName());
                if (isSaveCookieSet) {
                    content.remove(saveCookie);
                    isSaveCookieSet = false;
                }
                break;
            default:
        }
    }

    private class SaveCookieImpl implements SaveCookie {

        @Override
        public void save() throws IOException {
            RtcEditableQuery query = getLookup().lookup(RtcEditableQuery.class);
            if (query != null) {
                try {
                    query.save();
                } catch (RtcSaveException ex) {
                    RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
                }
            } else {
                throw new IllegalStateException(NbBundle.getMessage(RtcQueryNode.class, "SaveCookie"));
            }
        }
    }
}

class RtcQueryNamePropImpl extends Property<String> {

    private RtcQuery query;

    public RtcQueryNamePropImpl(Class<String> valueType, RtcQuery query) {
        super(valueType);
        this.query = query;
        setName("editableName");
        setDisplayName(NbBundle.getMessage(RtcQueryNode.class, "Query.name"));
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return query.getEditableName();
    }

    @Override
    public boolean canWrite() {
        return query instanceof RtcEditableQuery;
    }

    @Override
    public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ((RtcEditableQuery) query).setEditableName(val);
    }
}
