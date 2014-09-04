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
package com.comcast.cats.event;

/**
 * Interface defining how to register a TraceEventHandler.
 * 
 * @author cfrede001
 * 
 */
public interface TraceEventDispatcher
{
    /**
     * Add TraceListener by utilizing the underlying CatsEventDispatcher to
     * simplify implementation.
     * 
     * @param traceHandler
     *            {@link TraceEventHandler}
     */
    void addTraceListener( TraceEventHandler traceHandler );

    /**
     * Removes this TraceEventHandler from receiving trace events.
     * 
     * @pre - TraceHandler instance that is registered to receive trace events.
     * @post - TraceHandler instance is unregistered and will no longer receive
     *       trace events.
     * @param traceHandler
     *            {@link TraceEventHandler}
     */
    void removeTraceListener( TraceEventHandler traceHandler );

    /**
     * For sending a trace event.
     * 
     * @param traceEvent
     *            {@link TraceEvent}
     */
    void sendTraceEvent( TraceEvent traceEvent );
}
