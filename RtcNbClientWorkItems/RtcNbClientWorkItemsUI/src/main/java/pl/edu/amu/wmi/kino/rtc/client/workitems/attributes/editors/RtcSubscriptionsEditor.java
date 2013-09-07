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
package pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.contributor.Contributor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.editors.custom.RtcSubscriptionsExtendedEditor;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.values.RtcWorkItemAttributePossibleValues;

/**
 *
 * @author Michał Wojciechowski
 * @author Dawid Holewa
 */
public class RtcSubscriptionsEditor extends PropertyEditorSupport implements ExPropertyEditor {

    private Lookup context;
    private HashMap<String, Contributor> values = new HashMap<String, Contributor>();
    private RtcWorkItemAttributePossibleValues pv;
    private List<Contributor> possibleValues;
    private PropertyEnv env;

    /**
     * Class constructor.
     * Gets object of {@link RtcContributorPossibleValues} class from lookup.
     * @param context contains objects of classes which are needed for us.
     */
    public RtcSubscriptionsEditor(Lookup context) {
        this.context = context;
        pv = this.context.lookup(RtcWorkItemAttributePossibleValues.class);
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        this.env = env;
    }

    /**
     * Creates new instance of {@link RtcSubscriptionsExtendedEditor}
     * object which represent custom editor for subscriptions.
     * @return instance of custom editor
     * @see RtcSubscriptionsExtendedEditor
     */
    @Override
    public Component getCustomEditor() {
        List<Contributor> list = (List<Contributor>) getValue();
//        List<RtcContributor> res = new ArrayList<RtcContributor>();
//        for (IContributorHandle iContributorHandle : list) {
//            res.add(new RtcContributorImpl(iContributorHandle));
//        }

        return new RtcSubscriptionsExtendedEditor(this, list, env);
    }

    /**
     * Gets list of {@link Contributor} objects which represent server users.
     * Receiving data from the server is in a separate thread.
     * @return list of possible contributors (users)
     * @see Contributor
     */
    public List<Contributor> getPossibleValuesOfContributors() {

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                possibleValues = pv.getPossibleValues();
            }
        }).run();

        return possibleValues;
    }

    /**
     * Informs the Netbeans platform that the custom editor is exist and we can use it.
     * @return true
     */
    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    /**
     * Gets string representation of list of subscribers.
     * @return initials of subscribers in one string
     */
    @Override
    public String getAsText() {
        @SuppressWarnings("unchecked")
        List<Contributor> list = (List<Contributor>) getValue();
        values.clear();
        for (Contributor contrib : list) {
            //RtcContributorImpl contrib = new RtcContributorImpl(iContributorHandle);
            String key = "";
            for (String n : contrib.getName().split(" ")) {
                key += n.substring(0, 1) + ".";
            }

            values.put(key, contrib);
        }

        String wynik = "";

        for (String key : values.keySet()) {
            wynik += key;
            wynik += " , ";
        }
        if (wynik.length() > 2) {
            wynik = wynik.substring(0, wynik.length() - 2);
        }
        return wynik.trim();
    }

    /**
     * Sets list of subscribers depending on string which is ist of users
     * initials separated by coma.
     * @param text string representation of subscriber list
     * @throws IllegalArgumentException
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // DelegatingItemList<IContributorHandle> list;
        List<Contributor> lista = new ArrayList();

        String[] keys = text.split(",");

        for (String string : keys) {
            Contributor con = values.get(string.trim());
            if (con != null) {
                lista.add(con);
            }
        }

        //list = new DelegatingItemList<IContributorHandle>(lista, DelegatingItemList.Mode.LIST);

        setValue(lista);
    }
}
