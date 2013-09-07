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
package pl.edu.amu.wmi.kino.rtc.client.plans.transformators;

import java.net.URI;

public interface IItemReferenceDetector {
	
	public class Reference {
		
		private URI fUri;
		//private Location fLocation;
		private String fId;
		
		public Reference(URI uri) {
			this(uri, null);
		}
		
		public Reference(URI uri, String id) {
			fUri= uri;
			fId= id;
		}
		
		//public Reference(Location location) {
		//	this(location, null);
		//}
		
		//public Reference(Location location, String id) {
		//	fLocation= location;
		//	fId= id;
		//}
		
		public String getReference() {
			if(fUri != null)
				return fUri.toString();
			return null;
		}
		
		public void setUri(URI uri) {
			fUri= uri;
		}
		
		//public void setLocation(Location location) {
		//	fLocation= location;
		//}
		
		//public Location getLocation() {
		//	return fLocation;
		//}
		
		public URI getUri() {
			return fUri;
		}
		
		public void setId(String id) {
			fId= id;
		}
		
		public String getId() {
			return fId;
		}
	}
	
	public abstract Reference detect(String text);
     
}
