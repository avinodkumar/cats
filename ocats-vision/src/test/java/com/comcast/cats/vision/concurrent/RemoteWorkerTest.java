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
package com.comcast.cats.vision.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.comcast.cats.AbstractSettop;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.CatsEventDispatcherImpl;
import com.comcast.cats.vision.MockSettop;
import com.comcast.cats.vision.event.ActionType;
import com.comcast.cats.vision.event.RemoteEvent;

public class RemoteWorkerTest implements CatsEventHandler
{
    protected final Log LOGGER = LogFactory.getLog( getClass() );

    public void example() throws InterruptedException
    {
        CatsEventDispatcher dispatcher = new CatsEventDispatcherImpl();

        Settop settop = new MockSettop()
        {
            private static final long serialVersionUID = 1L;

            public boolean pressKey( RemoteCommand command )
            {
               LOGGER.info( "pressKey" );
                return true;
            }
        };

        dispatcher.addListener( this, CatsEventType.REMOTE_RESPONSE );

        RemoteEvent evt = new RemoteEvent( ActionType.PRESS, RemoteCommand.GUIDE, "10", null );
        ExecutorService executor = Executors.newFixedThreadPool( 5 );

        executor.execute( new PressKeyWorker( settop, evt, dispatcher ) );
        executor.awaitTermination( 5, TimeUnit.SECONDS );
    }

    @Override
    public void catsEventPerformed( CatsEvent evt )
    {
       LOGGER.info( "Event Received" );
    }
}
