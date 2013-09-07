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
package pl.edu.amu.wmi.kino.rtc.client.queries.model.attributes;

/**
 *
 * This is a data type. It represents some kind of value that can be used inside
 * query expressions and attributes. Object of this class are constant, which means
 * that you cannot modify its content - to provide new value you have to create new one.
 *
 * <p>
 * Objects of this class should be obtained from for example values providers or value checkers.
 * </p><p>
 * You shouldn't create your own value implementation, because in most cases
 * it will be unsupported.
 * </p>
 * @author Patryk Żywica
 */
public interface RtcQueryAttributeValue {
}
