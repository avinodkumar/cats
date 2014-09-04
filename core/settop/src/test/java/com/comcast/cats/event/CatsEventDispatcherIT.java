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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.event.impl.CatsEventDispatcherImpl;

/**
 * Integration test for {@link CatsEventDispatcher} and
 * {@link CatsEventDispatcherImpl} <br>
 * <br>
 * <br>
 * 
 * @author subinsugunan
 * 
 */
public class CatsEventDispatcherIT
{
    private static String              serverBase = "http://localhost:8080/";
    private static CatsContext         ctx;
    private static CatsEventDispatcher catsEventDispatcher;
    static final Logger                logger     = LoggerFactory.getLogger( CatsEventDispatcherIT.class );

    public CatsEventDispatcherIT()
    {
        System.setProperty( "cats.server.url", serverBase );
        ctx = new CatsContext();
        assertNotNull( ctx );
        ctx.refresh();

        catsEventDispatcher = ( CatsEventDispatcher ) ctx.getBean( CatsEventDispatcherImpl.class );
        assertNotNull( catsEventDispatcher );
    }

    /**
     * Basic test to make sure the action performed method is getting called.
     */
    @Test
    public void testSendCatsEvent()
    {
        DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
        catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );

        CatsEvent vEvt = new VideoEvent( 1, "1", null );
        // CatsEvent tEvt = new TraceEvent(2,"2",null);

        int eventCounter = 3;

        for ( int i = 0; i < eventCounter; i++ )
        {
            catsEventDispatcher.sendCatsEvent( vEvt );
        }

        assertEquals( eventCounter, dummyHandler.getActionPerformedCounter() );
    }

    /**
     * Test null condition.
     */
    @Test( expected = IllegalArgumentException.class )
    public void testSendNullCatsEvent()
    {
        catsEventDispatcher.sendCatsEvent( null );
    }

    /**
     * This method will check whether the handler is added as a listener or not.
     * 
     */
    @Test
    public void testAddListener()
    {
        try
        {
            DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );

            Field fieldListenersMap = CatsEventDispatcherImpl.class.getDeclaredField( "listeners" );
            fieldListenersMap.setAccessible( true );

            assertFalse( fieldListenersMap.getName().isEmpty() );

            @SuppressWarnings( "unchecked" )
            Map< CatsEventHandler, List< CatsEventType >> listeners = ( Map< CatsEventHandler, List< CatsEventType >> ) fieldListenersMap
                    .get( catsEventDispatcher );

            int noOfListenersAdded = 1;
            assertEquals( noOfListenersAdded, listeners.size() );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    /**
     * This method will check whether duplicate handler can be added as a
     * listener or not.
     * 
     */
    @Test
    public void testAddDuplicateListener()
    {
        try
        {
            DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );
            // Duplicate entries
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );

            Field fieldListenersMap = CatsEventDispatcherImpl.class.getDeclaredField( "listeners" );
            fieldListenersMap.setAccessible( true );

            assertFalse( fieldListenersMap.getName().isEmpty() );

            @SuppressWarnings( "unchecked" )
            Map< CatsEventHandler, List< CatsEventType >> listeners = ( Map< CatsEventHandler, List< CatsEventType >> ) fieldListenersMap
                    .get( catsEventDispatcher );

            int noOfListenersAdded = 1;
            assertEquals( noOfListenersAdded, listeners.size() );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    /**
     * This method will check whether the same handler can be used for a list of
     * events.
     * 
     */
    @Test
    public void testAddListenerList()
    {
        try
        {
            DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
            List< CatsEventType > types = new ArrayList< CatsEventType >();
            types.add( CatsEventType.TRACE );
            types.add( CatsEventType.VIDEO );
            types.add( CatsEventType.VIDEO_CTRL );
            catsEventDispatcher.addListener( dummyHandler, types );

            Field fieldListenersMap = CatsEventDispatcherImpl.class.getDeclaredField( "listeners" );
            fieldListenersMap.setAccessible( true );

            assertFalse( fieldListenersMap.getName().isEmpty() );

            @SuppressWarnings( "unchecked" )
            Map< CatsEventHandler, List< CatsEventType >> listeners = ( Map< CatsEventHandler, List< CatsEventType >> ) fieldListenersMap
                    .get( catsEventDispatcher );

            int noOfListenersAdded = 1;
            assertEquals( noOfListenersAdded, listeners.size() );

            CatsEvent vEvt = new VideoEvent( 1, "1", null );
            CatsEvent tEvt = new TraceEvent( 2, "2", null );
            CatsEvent vcEvt = new VideoCtrlEvent( 3, "3", null );

            int eventCounter = 3;

            for ( int i = 0; i < eventCounter; i++ )
            {
                catsEventDispatcher.sendCatsEvent( vEvt );// 3
                catsEventDispatcher.sendCatsEvent( tEvt );// 3
            }

            catsEventDispatcher.sendCatsEvent( vcEvt );// 1

            // 3+3+1=7
            assertEquals( 7, dummyHandler.getActionPerformedCounter() );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    /**
     * This method will check whether the same handler can be used for all
     * events.
     * 
     */
    @Test
    public void testAddAllListenerList()
    {
        try
        {
            DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
            List< CatsEventType > types = new ArrayList< CatsEventType >();
            types.add( CatsEventType.ALL );
            catsEventDispatcher.addListener( dummyHandler, types );

            Field fieldListenersMap = CatsEventDispatcherImpl.class.getDeclaredField( "listeners" );
            fieldListenersMap.setAccessible( true );

            assertFalse( fieldListenersMap.getName().isEmpty() );

            @SuppressWarnings( "unchecked" )
            Map< CatsEventHandler, List< CatsEventType >> listeners = ( Map< CatsEventHandler, List< CatsEventType >> ) fieldListenersMap
                    .get( catsEventDispatcher );

            int noOfListenersAdded = 1;
            assertEquals( noOfListenersAdded, listeners.size() );

            CatsEvent vEvt = new VideoEvent( 1, "1", null );
            CatsEvent tEvt = new TraceEvent( 2, "2", null );
            CatsEvent vcEvt = new VideoCtrlEvent( 3, "3", null );

            int eventCounter = 3;

            for ( int i = 0; i < eventCounter; i++ )
            {
                catsEventDispatcher.sendCatsEvent( vEvt );// 3
                catsEventDispatcher.sendCatsEvent( tEvt );// 3
            }

            catsEventDispatcher.sendCatsEvent( vcEvt );// 1

            // 3+3+1=7
            assertEquals( 7, dummyHandler.getActionPerformedCounter() );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    /**
     * Test basic removal operation.
     * 
     */
    @Test
    public void testRemoveListener()
    {
        try
        {
            DummyCatsEventHandler dummyHandler = new DummyCatsEventHandler();
            catsEventDispatcher.addListener( dummyHandler, CatsEventType.VIDEO );

            Field fieldListenersMap = CatsEventDispatcherImpl.class.getDeclaredField( "listeners" );
            fieldListenersMap.setAccessible( true );

            assertFalse( fieldListenersMap.getName().isEmpty() );

            @SuppressWarnings( "unchecked" )
            Map< CatsEventHandler, List< CatsEventType >> listeners = ( Map< CatsEventHandler, List< CatsEventType >> ) fieldListenersMap
                    .get( catsEventDispatcher );

            int noOfListenersAdded = 1;
            assertEquals( noOfListenersAdded, listeners.size() );

            catsEventDispatcher.removeListener( dummyHandler );
            noOfListenersAdded = 0;

            assertEquals( noOfListenersAdded, listeners.size() );
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
    }
}
