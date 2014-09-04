/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.event.impl;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.TraceEvent;
import com.comcast.cats.event.TraceEventHandler;

/**
 * Simple class used to handle bubbling up CatsEvents as TraceEvents.
 * @author cfrede001
 *
 */
public class TraceEventIntermediateDispatcher implements CatsEventHandler {
	TraceEventHandler handler;
	
	/**
     * TraceEventIntermediateDispatcher.
     * 
     * @param handler {@linkplain TraceEventHandler}
     */
	public TraceEventIntermediateDispatcher(TraceEventHandler handler) {
		this.handler = handler;
	}

	/**
     * executed for traceEvent Performed.
     * 
     * @param evt {@linkplain CatsEvent}
     */
	@Override
	public void catsEventPerformed(CatsEvent evt) {
		if(evt instanceof TraceEvent) {
			handler.traceEventPerformed((TraceEvent) evt);
		}
	}
}
