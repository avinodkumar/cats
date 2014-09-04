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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.TraceEvent;
import com.comcast.cats.event.TraceEventDispatcher;
import com.comcast.cats.event.TraceEventHandler;

/**
 * Convenience class for registering and sending just TraceEvents.
 * 
 * @author cfrede001
 * 
 */
@Named
public class TraceEventDispatcherImpl implements TraceEventDispatcher
{
    private static Logger                                      logger           = LoggerFactory.getLogger( TraceEventDispatcherImpl.class );
    CatsEventDispatcher                                        dispatcher;
    Map< TraceEventHandler, TraceEventIntermediateDispatcher > traceEventMapper = new HashMap< TraceEventHandler, TraceEventIntermediateDispatcher >();

    @Inject
    public TraceEventDispatcherImpl( CatsEventDispatcher dispatcher )
    {
        this.dispatcher = dispatcher;
    }

    /**
     * Add TraceListener by utilizing the underlying CatsEventDispatcher to
     * simplify implementation.
     * 
     * @param traceHandler 
     */
    @Override
    public void addTraceListener( TraceEventHandler traceHandler )
    {
        if ( traceEventMapper.containsKey( traceHandler ) )
        {
            logger.warn( "traceEventMapper doesn't contain traceHandler = " + traceHandler.toString() );
            return;
        }
        // Create new middle man who correlates TraceEvents with CatsEvents.
        TraceEventIntermediateDispatcher middleMan = new TraceEventIntermediateDispatcher( traceHandler );
        traceEventMapper.put( traceHandler, middleMan );
        dispatcher.addListener( middleMan, CatsEventType.TRACE );
    }

    /**
     * Removes this TraceEventHandler from receiving trace events.
     * 
     * @pre - TraceHandler instance that is registered to receive trace events.
     * @post - TraceHandler instance is unregistered and will no longer receive
     *       trace events.
     */
    @Override
    public void removeTraceListener( TraceEventHandler traceHandler )
    {
        if ( !traceEventMapper.containsKey( traceHandler ) )
        {
            logger.warn( "traceEventMapper doesn't contain traceHandler = " + traceHandler.toString() );
            return;
        }

        CatsEventHandler middleMan = traceEventMapper.get( traceHandler );
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Removing " + middleMan.toString() + " from CatsEventDispatcher" );
        }
        dispatcher.removeListener( middleMan );
        traceEventMapper.remove( traceHandler );
    }

    /**
     * To send trace event.
     * 
     * @param traceEvent {@linkplain TraceEvent}
     * @throws IllegalArgumentException
     */
    @Override
    public void sendTraceEvent( TraceEvent traceEvent ) throws IllegalArgumentException
    {
        dispatcher.sendCatsEvent( ( CatsEvent ) traceEvent );
    }

    /**
     * To get cats event dispatcher.
     * 
     * @return {@linkplain CatsEventDispatcher}
     */
    public CatsEventDispatcher getCatsEventDispatcher()
    {
        return dispatcher;
    }

    /**
     * To set cats event dispatcher.
     * 
     * @param dispatcher {@linkplain CatsEventDispatcher}
     */
    public void setCatsEventDispatcher( CatsEventDispatcher dispatcher )
    {
        this.dispatcher = dispatcher;
    }
}
