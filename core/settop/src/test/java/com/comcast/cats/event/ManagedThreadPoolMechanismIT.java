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

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.event.impl.ManagedThreadPool;

public class ManagedThreadPoolMechanismIT implements CatsEventHandler
{
    private static String              serverBase        = "http://localhost:8080/";
    private static CatsContext         ctx;

    private static CatsEventDispatcher catsEventDispatcherThreaded;
    private ManagedThreadPool          managedThreadPool;
    private DummySource                dummySourceObject = new DummySource();
    static final Logger                logger            = LoggerFactory.getLogger( ManagedThreadPoolMechanismIT.class );

    public ManagedThreadPoolMechanismIT()
    {
        System.setProperty( "cats.server.url", serverBase );

        ctx = new CatsContext();
        assertNotNull( ctx );
        ctx.refresh();

        catsEventDispatcherThreaded = ctx.getBean( CatsEventDispatcher.class );
        assertNotNull( catsEventDispatcherThreaded );

        managedThreadPool = ctx.getBean( ManagedThreadPool.class );
        assertNotNull( managedThreadPool );

        catsEventDispatcherThreaded.addListener( this, CatsEventType.TRACE );
    }

    @Test
    public void testEventHandlingWithNullCatsEvent()
    {
        try
        {
            catsEventDispatcherThreaded.sendCatsEvent( null );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            Assert.assertEquals( "CatsEvent can't be null", illegalArgumentException.getMessage() );
        }
        catch ( Exception exception )
        {
            Assert.fail( exception.getMessage() );
        }
    }

    @Test
    public void testEventHandling()
    {

        try
        {
            TraceEvent traceEvent1 = new TraceEvent( 1, "1", null );
            traceEvent1.setPayload( "TraceEvent1 " );
            catsEventDispatcherThreaded.sendCatsEvent( traceEvent1 );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            Assert.assertEquals( "CatsEvent can't be null", illegalArgumentException.getMessage() );
        }
        catch ( Exception exception )
        {
            Assert.fail( exception.getMessage() );
        }
        try
        {
            TraceEvent traceEvent2 = new TraceEvent( 2, "2", null );
            traceEvent2.setPayload( "TraceEvent2 " );
            catsEventDispatcherThreaded.sendCatsEvent( traceEvent2 );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            Assert.assertEquals( "CatsEvent can't be null", illegalArgumentException.getMessage() );
        }
        catch ( Exception exception )
        {
            Assert.fail( exception.getMessage() );
        }
        try
        {
            TraceEvent traceEvent3 = new TraceEvent( 3, "3", null );
            traceEvent3.setPayload( "TraceEvent3 " );
            catsEventDispatcherThreaded.sendCatsEvent( traceEvent3 );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            Assert.assertEquals( "CatsEvent can't be null", illegalArgumentException.getMessage() );
        }
        catch ( Exception exception )
        {
            Assert.fail( exception.getMessage() );
        }
        try
        {
            TraceEvent traceEvent4 = new TraceEvent( 4, "4", null );
            traceEvent4.setPayload( "TraceEvent4 " );
            catsEventDispatcherThreaded.sendCatsEvent( traceEvent4 );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            Assert.assertEquals( "CatsEvent can't be null", illegalArgumentException.getMessage() );
        }
        catch ( Exception exception )
        {
            Assert.fail( exception.getMessage() );
        }

    }

    @Override
    public void catsEventPerformed( CatsEvent catsEvent )
    {
        if ( catsEvent instanceof TraceEvent )
        {

            TraceEvent traceEvent = ( TraceEvent ) catsEvent;

            Assert.assertNotNull( dummySourceObject );

            if ( traceEvent.getPayload().compareTo( "TraceEvent1 " ) == 0 )
            {
                /*
                 * New DummyTask with null value for CatsEvent
                 */
                managedThreadPool.addTask( new DummyTask( new DummySource(), null ) );

            }
            else if ( traceEvent.getPayload().compareTo( "TraceEvent2 " ) == 0 )
            {
                /*
                 * New DummyTask with null value for DummySource and CatsEvent
                 */
                managedThreadPool.addTask( new DummyTask( null, null ) );
            }
            else
            {
                managedThreadPool.addTask( new DummyTask( dummySourceObject, catsEvent ) );
            }

        }

    }
}
