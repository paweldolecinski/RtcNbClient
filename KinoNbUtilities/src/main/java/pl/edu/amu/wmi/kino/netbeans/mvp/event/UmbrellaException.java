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
package pl.edu.amu.wmi.kino.netbeans.mvp.event;

import java.util.Set;

/**
 * A {@link RuntimeException} that collects a {@link Set} of child
 * {@link Throwable}s together. Typically thrown after loop, with all of the
 * exceptions thrown during that loop, but delayed so that the loop finishes
 * executing.
 * 
 * @since 0.0.3.0
 * @author Patryk Żywica
 */
public class UmbrellaException extends RuntimeException {

	private static final long serialVersionUID = -3985656603255679390L;
	/**
	 * The causes of the exception.
	 */
	private Set<Throwable> causes;

	public UmbrellaException(Set<Throwable> causes) {
		super(
				"One or more exceptions caught, see full set in UmbrellaException#getCauses",
				causes.size() == 0 ? null : causes.toArray(new Throwable[0])[0]);
		this.causes = causes;
	}

	/**
	 * Get the set of exceptions that caused the failure.
	 * 
	 * @return the set of causes
	 */
	public Set<Throwable> getCauses() {
		return causes;
	}

}