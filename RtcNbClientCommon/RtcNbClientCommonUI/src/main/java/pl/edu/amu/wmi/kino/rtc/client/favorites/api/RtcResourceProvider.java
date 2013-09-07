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
package pl.edu.amu.wmi.kino.rtc.client.favorites.api;

import org.openide.nodes.Node;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveRepository;

/**
 * Interface has to be implemented to store resource references in favorites.
 * <p>
 * Distinct <code>RtcResourceProvider</code> implementations are identified by their
 * full classnames so if you refactor and somehow change this classname - the old stored favorites
 * will be lost. System will propably not work properly if two implementations will
 * share the full classname (which is possible due to netbeans module classloader !)
 * so try to make this classnames distinct and test your classes with as much modules
 * enabled as possible.
 * </p>
 * <p>
 * <b>Important</b> - implementations of this interface have to be registered as services!
 * you can annotate them with <code>@ServiceProvider(service=RtcResourceProvider.class)</code>
 * @author psychollek
 */
public interface RtcResourceProvider {

    /**
     * Returns the node representing resource identified by resourceId
     * from passed repository connection.
     *
     * It is guarented thet this methodd will be called <b>not</b> from responsible tread.
     * @param resourceId unique across projectArea key to identify resource.
     * @param activeRepository project area to use for retrieving the resource.
     * @return Node representing the resource.
     * @throws UnableToGetTheReferenceException if there where any problems and reference could not have been created.
     */
    public Node getReference(String resourceId, ActiveRepository activeRepository) throws UnableToGetTheReferenceException;

    /**
     * Returns true if for given context, the <code>RtcResourceProvider</code>
     * decides it should be able to provide necesary values to store the resource.
     * <p>
     * Values shall be provided by getResouceId and getActiveRepository methods
     * </p>
     * @param context lookup to be searched for necesary objects
     * @return true if id apears valid, false otherwise.
     */
    public boolean enableForContext(Lookup context);

    /**
     * Returns Id which has to be unique across <code>ActiveRepository</code>
     * and provide enough information on what resource is referenced.
     * <p>
     * This string together with <code>ActiveRepository</code> can be used to
     * getReference
     * </p>
     *
     * @param context from which the reference is created
     * @return String to be persisted
     */
    public String getResourceId(Lookup context);
//TODO: javadoc: psychollek: lack of return

    /**
     * Returns <code>ActiveRepository</code> which is associated with the context
     * result of getResourceId from the same context have to be retrivable later
     * from this repository.
     * @param context from which the reference is created
     * @return 
     */
    public ActiveRepository getActiveRepository(Lookup context);
}
