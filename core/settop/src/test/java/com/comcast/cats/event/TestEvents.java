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
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.comcast.cats.event.impl.CatsEventDispatcherImpl;

/**
 * Test basic events.
 * 
 * @author
 * 
 */
public class TestEvents
{
    CatsEventDispatcher dispatcher = new CatsEventDispatcherImpl();

    CatsEvent           vEvt       = new VideoEvent( 1, "1", null );
    CatsEvent           tEvt       = new TraceEvent( 2, "2", null );

    @Test
    public void testBasicHandler()
    {
        DummyCatsEventHandler handler = new DummyCatsEventHandler();

        dispatcher.addListener( handler, CatsEventType.ALL );
        dispatcher.sendCatsEvent( vEvt );

        assertEquals( 1, handler.getActionPerformedCounter() );

        CatsEvent lEvt = handler.getLastEvent();
        assertEquals( lEvt, vEvt );

        dispatcher.sendCatsEvent( tEvt );
        assertEquals( 2, handler.getActionPerformedCounter() );

        lEvt = handler.getLastEvent();
        assertEquals( lEvt, tEvt );
    }

    @Test
    public void testSpecificEvents()
    {
        DummyCatsEventHandler handler = new DummyCatsEventHandler();

        dispatcher.addListener( handler, CatsEventType.VIDEO );
        dispatcher.sendCatsEvent( vEvt );

        assertEquals( 1, handler.getActionPerformedCounter() );
        CatsEvent lEvt = handler.getLastEvent();

        assertEquals( lEvt, vEvt );
        dispatcher.sendCatsEvent( tEvt );

        assertEquals( 1, handler.getActionPerformedCounter() );
        lEvt = handler.getLastEvent();

        // The last event should still be the last video event we sent.
        assertEquals( lEvt, vEvt );
        assertNotSame( lEvt, tEvt );
    }
}
