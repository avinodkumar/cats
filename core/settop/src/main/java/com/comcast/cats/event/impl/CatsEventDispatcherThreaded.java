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

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.event.CatsEvent;

/**
 * Extend the existing CatsEventDispatcher implementation to a threaded one
 * using a queue mechanism.
 * 
 * @author cfrede001
 * 
 */
@Named
public final class CatsEventDispatcherThreaded extends CatsEventDispatcherImpl implements Runnable
{
    private static final Logger           logger                    = LoggerFactory
                                                                            .getLogger( CatsEventDispatcherThreaded.class );
    Thread                                dispatcherThread;

    public static final Integer           CATS_EVENT_QUEUE_CAPACITY = 512;
    protected static final CatsEventQueue queue                     = new CatsEventQueue( CATS_EVENT_QUEUE_CAPACITY );

    /**
     * Constructor
     * 
     */
    public CatsEventDispatcherThreaded()
    {
        super();
        dispatcherThread = new Thread( ( Runnable ) this );
        logger.info( "Starting CatsEventDispatcherThreaded" );
        dispatcherThread.setDaemon( true );
        dispatcherThread.start();
    }

    /**
     * To send Cats event.
     * 
     * @param evt
     *            {@linkplain CatsEvent}
     */
    @Override
    public void sendCatsEvent( CatsEvent evt )
    {
        try
        {
            if ( evt == null )
            {
                logger.warn( "CatsEventDispatcherThreaded CatsEvent is null throwing exception" );
                throw new IllegalArgumentException( "CatsEvent can't be null" );
            }
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "CatsEventDispatcherThreaded CatsEventQueue.put( " + evt + " )" );
            }
            queue.put( evt );
        }
        catch ( InterruptedException e )
        {
            logger.error( "CatsEventDispatcherThreaded sendCatsEvent() interruptedException :" + e.getMessage() );
        }
    }

    /**
     * For every item in the queue remove it and send the events.
     */
    @Override
    public void run()
    {
        while ( true )
        {
            CatsEvent catsEvent;
            try
            {

                catsEvent = queue.take();
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "CatsEventDispatcherThreaded CatsEventQueue.take(" + catsEvent + ")" );
                }

                super.sendCatsEvent( catsEvent );
            }
            catch ( InterruptedException e )
            {
                logger.warn( "CatsEventDispatcherThreaded : queue.take was interrupted", e );
            }
        }
    }
}
