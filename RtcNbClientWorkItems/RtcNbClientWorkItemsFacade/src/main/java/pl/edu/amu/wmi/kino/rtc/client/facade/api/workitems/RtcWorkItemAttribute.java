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
package pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems;

import org.openide.util.Lookup;

/**
 * 
 * @author Patryk Żywica
 */
public interface RtcWorkItemAttribute<T> {

	public abstract Class<T> getValueType();

	public abstract String getDisplayName();

	public abstract T getDefaultValue();

	public abstract T getNullValue();
        
        public abstract String getId();

	/**
	 * Attribute's lookup exposes features of attribute. It may contain value
	 * providers, value checkers and other objects that will be used by UI to
	 * find out most appropriate display method of this attribute in given
	 * profile.
	 * 
	 * <p>
	 * Other non UI content is also allowed.
	 * </p>
	 * 
	 * @return lookup of this attribute.
	 */
	public abstract Lookup getLookup();

}
