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

/*
import org.eclipse.core.runtime.Assert;

import com.ibm.team.foundation.common.DetectedTextLink;
import com.ibm.team.foundation.common.LinkDetector;
import com.ibm.team.foundation.common.TextLinkDetectorManager;
import com.ibm.team.foundation.common.URILinkDetector;
import com.ibm.team.repository.common.Location;
import com.ibm.team.repository.common.TeamRepositoryException;

import com.ibm.team.apt.internal.common.wiki.IWikiPageAttachment;
import com.ibm.team.apt.internal.common.wiki.IWikiPageAttachmentService;
*/

/**
 * Detects references to Oid Locations, 
 * attachments (@see {@link IWikiPageAttachment}), and
 * work items (@see {@link LinkDetector}).
 * 
 * @see TextLinkDetectorManager#createLinkDetector()
 * @since 0.6
 */
public class DefaultReferenceDetector implements IItemReferenceDetector {

    public Reference detect(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	/*
	private LinkDetector fDelegate;
	private URI fBaseUri;
	protected Map<String, IWikiPageAttachment> fAttachmentsMap= new HashMap<String, IWikiPageAttachment>();
	
	public DefaultReferenceDetector(URI baseUri){
		this(baseUri, false);
	}
	
	public DefaultReferenceDetector(URI baseUri, boolean isServer) {
		Assert.isNotNull(baseUri);
		
		fBaseUri= baseUri;
		fDelegate= TextLinkDetectorManager.getDefault().createLinkDetector(isServer);
		fDelegate.setBaseURI(baseUri);
		fDelegate.addTextLinkDetector(new URILinkDetector());
	}
	
	public void setAttachments(List<IWikiPageAttachment> attachments) {
		Assert.isNotNull(attachments);
		
		fAttachmentsMap.clear();
		for (IWikiPageAttachment attachment: attachments) {
			fAttachmentsMap.put(attachment.getName(), attachment);
		}
	}
	
	public Reference detect(String text) {
		
		IWikiPageAttachment attachment= fAttachmentsMap.get(text);
		if(attachment != null) {
			final String query= "itemId=" + attachment.getItemId().getUuidValue(); //$NON-NLS-1$ 
			return new Reference(Location.serviceLocation(fBaseUri.toASCIIString(), IWikiPageAttachmentService.class, attachment.getName(), query));
		}
		
		List<DetectedTextLink> match= fDelegate.match(text);
		if(!match.isEmpty()) {
			List uris= match.get(0).createURIs();
			if(!uris.isEmpty()) {
				URI uri= (URI) match.get(0).createURIs().get(0);
				
				try {
					final Location location= Location.location(uri);
					return safeReference(location);
					
				} catch (TeamRepositoryException e) {
					return null;
				}
			}
		}
		
		if(fBaseUri != null) {
			URI uri;
			try {
				uri= new URI(text);
			} catch (URISyntaxException e) {
				return null;
			}
			
			Location tmp;
			try {
				tmp= Location.location(uri);
				Location location= Location.location(tmp, fBaseUri.toASCIIString(), null);
				return safeReference(location);
			} catch (IllegalArgumentException e) {
				return null; // in case of invalid UUIDs
			} catch (TeamRepositoryException e) {
				return null;
			}
		}
		
		return null;
	}

	private static Reference safeReference(Location location) {
		if(location.getType() == null)
			return null;
		
		return new Reference(location);
	}
         *
         */
}