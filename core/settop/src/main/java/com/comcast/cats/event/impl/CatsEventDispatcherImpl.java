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

import static com.comcast.cats.event.CatsEventType.ALL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;

/**
 * Basic CatsEventDispatcher implementation class for sending CatsEvents.
 * 
 * @author cfrede001
 * 
 */
public class CatsEventDispatcherImpl implements CatsEventDispatcher
{
    protected static Logger                       logger          = LoggerFactory
                                                                          .getLogger( CatsEventDispatcherImpl.class );

    Map< CatsEventHandler, List< CatsEventType >> listeners       = new HashMap< CatsEventHandler, List< CatsEventType >>();
    Map< CatsEventHandler, Object >               listenerSources = new HashMap< CatsEventHandler, Object >();

    /**
     * Zero arg constructor is perfectly acceptable for this dispatcher, since
     * no external information is needed.
     */
    public CatsEventDispatcherImpl()
    {
        logger.trace( "CatsEventDispatcherImpl Constructor" );
    }

    /**
     * Synchronized method that add listener.
     * 
     * @param handler
     *            {@linkplain CatsEventHandler}
     * @param type
     *            - {@linkplain CatsEventType}
     */
    @Override
    public synchronized void addListener( CatsEventHandler handler, CatsEventType type )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "addListener to handler for single CatsEventType" );
        }
        List< CatsEventType > types = new ArrayList< CatsEventType >();
        types.add( type );
        listeners.put( handler, types );
    }

    /**
     * Method that add listener.
     * 
     * @param handler
     *            {@linkplain CatsEventHandler}
     * @param type
     *            - {@linkplain CatsEventType}
     * @param source
     *            - Object
     */
    @Override
    public void addListener( CatsEventHandler handler, CatsEventType type, Object source )
    {
        addListener( handler, type );
        listenerSources.put( handler, source );
    }

    /**
     * Synchronized method that add listener.
     * 
     * @param handler
     *            {@linkplain CatsEventHandler}
     * @param types
     *            - List of {@linkplain CatsEventType}
     */
    @Override
    public synchronized void addListener( CatsEventHandler handler, List< CatsEventType > types )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "addListener for CatsEventHandler[" + handler + "] for CatsEventType(s):" );
            for ( CatsEventType type : types )
            {
                logger.debug( "\t" + type );
            }
        }
        listeners.put( handler, types );
    }

    /**
     * Synchronized method that remove all listeners.
     * 
     * @param handler
     *            {@linkplain CatsEventHandler}
     */
    @Override
    public synchronized void removeListener( CatsEventHandler handler )
    {
        if ( listeners.containsKey( handler ) )
        {
            listeners.remove( handler );
        }
        if ( listenerSources.containsKey( handler ) )
        {
            listenerSources.remove( handler );
        }
    }

    /**
     * Is this handler listening to events coming from event.getSource(). If no
     * source is found, assume it is listening to all sources.
     * 
     * @param evt
     *            {@linkplain CatsEvent}
     * @param handler
     *            {@linkplain CatsEventHandler}
     * @return Boolean
     */
    protected boolean isHandlerValidSource( CatsEvent evt, CatsEventHandler handler )
    {
        Object source = null;
        if ( listenerSources.containsKey( handler ) )
        {
            source = listenerSources.get( handler );
        }
        else
        {
            /**
             * By default if you aren't listening to a specific source you're
             * listening to everything.
             */
            return true;
        }
        if ( source != null && evt.getSource() == source )
        {
            return true;
        }

        return false;
    }

    /**
     * Synchronized method that sends events to all listeners.
     * 
     * @param evt
     *            {@linkplain CatsEvent}
     * @throws IllegalArgumentException
     */
    @Override
    public synchronized void sendCatsEvent( CatsEvent evt ) throws IllegalArgumentException
    {
        if ( evt == null )
        {
            logger.warn( "CatsEvent is null throwing exception" );
            throw new IllegalArgumentException( "CatsEvent can't be null" );
        }
        if ( logger.isTraceEnabled() )
        {
            logger.trace( "sendCatsEvent " + evt.toString() );
        }

        CatsEventType type = evt.getType();

        for ( CatsEventHandler handler : listeners.keySet() )
        {
            if ( !isHandlerValidSource( evt, handler ) )
            {
                // If we're not listening to this source then just continue.
                continue;
            }
            List< CatsEventType > types = listeners.get( handler );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "handler [" + handler + "] is listening to types:" );
                for ( CatsEventType t : types )
                {
                    logger.debug( "\t" + t );
                }
            }
            if ( handlerListensToEvent( types, type ) )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "handler.catsEventPerformed(" + evt + ")" );
                }
                handler.catsEventPerformed( evt );
            }
        }
    }

    private boolean handlerListensToEvent( List< CatsEventType > types, CatsEventType type )
    {
        if ( types.contains( ALL ) )
        {
            return true;
        }
        else if ( types.contains( type ) )
        {
            return true;
        }
        return false;
    }
}
