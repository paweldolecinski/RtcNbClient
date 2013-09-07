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
package pl.edu.amu.wmi.kino.rtc.client.favorites.nodes;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.favorites.RtcFavoritesEntry;

/**
 *
 * @author Patryk Żywica
 */
public class RtcFavoritesBrokenEntryNode extends AbstractNode {

    private InstanceContent ic;

    public RtcFavoritesBrokenEntryNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        setDisplayName(getEntry().getName());
        setShortDescription(getEntry().getPath());
        //TODO : set broken icon here
    }

    @Override
    public String getHtmlDisplayName() {
        return "<html><s><font color=\"#ff0000\">"+getEntry().getName()+"</font></s></html>";
    }

    @Override
    protected Sheet createSheet() {
        Sheet toReturn = new Sheet();
        Set set =Sheet.createPropertiesSet();
//TODO : i18n
        Property prop = new PropertySupport.ReadOnly<String>("entryName",String.class,"Name",""){

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getEntry().getName();
            }

        };
        set.put(prop);

        prop = new PropertySupport.ReadOnly<String>("entryPath",String.class,"Path",""){

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getEntry().getPath();
            }

        };
        set.put(prop);

        prop = new PropertySupport.ReadOnly<String>("entryProvider",String.class,"Provider",""){

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getEntry().getProviderId();
            }

        };
        set.put(prop);

        prop = new PropertySupport.ReadOnly<String>("resourceId",String.class,"Resource ID",""){

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getEntry().getResourceId();
            }

        };
        set.put(prop);

        prop = new PropertySupport.ReadOnly<String>("connectionId",String.class,"Connection ID",""){

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return getEntry().getconnectionId();
            }

        };
        set.put(prop);

        toReturn.put(set);
        return toReturn;
    }

    private RtcFavoritesEntry getEntry() {
        return getLookup().lookup(RtcFavoritesEntry.class);
    }


}
