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
package pl.edu.amu.wmi.kino.rtc.client.queries.result;

import org.openide.explorer.view.OutlineView;

/**
 * Code which is used here is copied from developement version of NetBeans 6.9. It could
 * change if code in NetBeans sources will change. The reason of copying this code
 * was to make a possibility to use these methods and keep the module to be compatible
 * with earlier versions of NetBeans.
 * Class is under developement.
 *
 * 
 * @author Szymon Sadlo
 */
@Deprecated
public class RtcOutline extends OutlineView {

	private static final long serialVersionUID = -2928384983462120526L;
	private PropertiesRowModel rowModel;

    public RtcOutline() {
        super();
        rowModel = new PropertiesRowModel();
    }

//    /**
//     * Adds a property column which will match any property with the passed
//     * name.
//     *
//     * @param name The programmatic name of the property
//     * @param displayName A localized display name for the property which can
//     * be shown in the table header
//     * @since 6.25
//     */
//    public final void addPropertyColumn(String name, String displayName) {
//        addPropertyColumn(name, displayName, null);
//    }
//
//    /**
//     * Adds a property column which will match any property with the passed
//     * name.
//     *
//     * @param name The programmatic name of the property
//     * @param displayName A localized display name for the property which can
//     * be shown in the table header
//     * @param description The description which will be used as a tooltip in
//     * the table header
//     * @since 6.25
//     */
//    public final void addPropertyColumn(String name, String displayName, String description) {
//        Parameters.notNull("name", name); //NOI18N
//        Parameters.notNull("displayName", displayName); //NOI18N
//        Property[] p = rowModel.getProperties();
//        Property[] nue = new Property[p.length + 1];
//        System.arraycopy(p, 0, nue, 0, p.length);
//        nue[p.length] = new PrototypeProperty(name, displayName, description);
//        setProperties(nue);
//    }
//
//    /**
//     * Remove the first property column for properties named <code>name</code>
//     * @param name The <i>programmatic</i> name of the Property, i.e. the
//     * return value of <code>Property.getName()</code>
//     *
//     * @return true if a column was removed
//     * @since 6.25
//     */
//    public final boolean removePropertyColumn(String name) {
//        Parameters.notNull("name", name); //NOI18N
//        Property[] props = rowModel.getProperties();
//        List<Property> nue = new LinkedList<Property>(Arrays.asList(props));
//        boolean found = false;
//        for (Iterator<Property> i = nue.iterator(); i.hasNext();) {
//            Property p = i.next();
//            if (name.equals(p.getName())) {
//                found = true;
//                i.remove();
//                break;
//            }
//        }
//        if (found) {
//            props = nue.toArray(new Property[props.length - 1]);
//            setProperties(props);
//        }
//        return found;
//    }
//
//    /**
//     * Set the description (table header tooltip) for the property column
//     * representing properties that have the passed programmatic (not display)
//     * name.
//     *
//     * @param columnName The programmatic name (Property.getName()) of the
//     * column
//     * @param description Tooltip text for the column header for that column
//     */
//    public final void setPropertyColumnDescription(String columnName, String description) {
//        Parameters.notNull("columnName", columnName); //NOI18N
//        Property[] props = rowModel.getProperties();
//        for (Property p : props) {
//            if (columnName.equals(p.getName())) {
//                p.setShortDescription(description);
//            }
//        }
//    }
//
//    /**
//     * Set all of the non-tree columns property names and display names.
//     *
//     * @param namesAndDisplayNames An array, divisible by 2, of
//     * programmatic name, display name, programmatic name, display name...
//     * @since 6.25
//     */
//    public final void setPropertyColumns(String... namesAndDisplayNames) {
//        if (namesAndDisplayNames.length % 2 != 0) {
//            throw new IllegalArgumentException("Odd number of names and " + //NOI18N
//                    "display names: " + Arrays.asList(namesAndDisplayNames)); //NOI18N
//        }
//        Property[] props = new Property[namesAndDisplayNames.length / 2];
//        for (int i = 0; i < namesAndDisplayNames.length; i += 2) {
//            props[i / 2] = new PrototypeProperty(namesAndDisplayNames[i], namesAndDisplayNames[i + 1]);
//        }
//        setProperties(props);
//    }
//
//    static final class PrototypeProperty extends PropertySupport.ReadOnly<Object> {
//
//        PrototypeProperty(String name, String displayName) {
//            this(name, displayName, null);
//        }
//
//        PrototypeProperty(String name, String displayName, String description) {
//            super(name, Object.class, displayName, description);
//        }
//
//        @Override
//        public Object getValue() throws IllegalAccessException,
//                InvocationTargetException {
//            throw new AssertionError();
//        }
//
//        @Override
//        public void setValue(Object val) throws IllegalAccessException,
//                IllegalArgumentException,
//                InvocationTargetException {
//            throw new AssertionError();
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            return o != null && o instanceof Property
//                    && getName().equals(((Property) o).getName());
//        }
//
//        @Override
//        public int hashCode() {
//            return getName().hashCode();
//        }
//    }
}
