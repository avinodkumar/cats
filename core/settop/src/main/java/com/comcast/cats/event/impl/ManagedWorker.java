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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ManagedWorker is responsible for one settop's activities. The tasks will be
 * added to it's event queue and then tasks will be executed in the order in
 * which it is received in the queue.
 * 
 * @author sajayjk
 * 
 */
public class ManagedWorker implements Runnable
{

    private List< AbstractManagedTask >        eventQueue = new ArrayList< AbstractManagedTask >();
    private Object                             identifier;

    private static CatsEventDispatcherThreaded catsEventDispatcherThreaded;

    private static ManagedThreadPool           managedThreadPool;
    private static final Logger                logger     = LoggerFactory.getLogger( ManagedWorker.class );

    /**
     * Constructor
     * 
     */
    public ManagedWorker( Object identifier )
    {
        this.identifier = identifier;
    }

    /**
     * Set cats event Dispatcher.
     * 
     * @param dispatcher {@linkplain CatsEventDispatcherThreaded}
     */
    public static void setCatsEventDispatcher( CatsEventDispatcherThreaded dispatcher )
    {
        catsEventDispatcherThreaded = dispatcher;
    }

    /**
     * Set managed thread pool
     * 
     * @param threadPool
     */
    public static void setManagedThreadPool( ManagedThreadPool threadPool )
    {
        managedThreadPool = threadPool;
    }

    /**
     * For every item in the queue, remove it and send the events.
     */
    public void run()
    {
        while ( !eventQueue.isEmpty() )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Processing eventQueue" );
            }
            AbstractManagedTask managedTask = ( AbstractManagedTask ) ( eventQueue.get( 0 ) );
            eventQueue.remove( 0 );
            managedTask.run();

            if ( managedTask.getCatsResponseEvent() != null )
            {
                catsEventDispatcherThreaded.sendCatsEvent( managedTask.getCatsResponseEvent() );
            }
        }
        managedThreadPool.freeWorker( identifier );
    }

    /**
     * Adds tasks to event queue.
     * 
     * @param managedTask
     *            task to be executed
     */
    public void addToQueue( final AbstractManagedTask managedTask )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Adding task to eventQueue" );
        }
        eventQueue.add( managedTask );

    }
}
