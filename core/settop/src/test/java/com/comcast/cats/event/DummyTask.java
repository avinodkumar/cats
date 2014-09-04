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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.event.impl.AbstractManagedTask;

public class DummyTask extends AbstractManagedTask
{
    private DummySource   dummySourceObject;
    private CatsEvent     catsEvent;
    private static Logger logger = LoggerFactory.getLogger( DummyTask.class );

    public DummyTask( DummySource dummySourceObject, CatsEvent catsEvent )
    {
        this.catsEvent = catsEvent;
        this.dummySourceObject = dummySourceObject;
        responseEvent = new DummyPowerResponseEvent( "DummyTask", dummySourceObject, catsEvent );
    }

    @Override
    public void run()
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Perform some action." );
        }
        if ( catsEvent instanceof TraceEvent )
        {
            TraceEvent traceEvent = ( TraceEvent ) catsEvent;

            logger.info( "Received TraceEvent with payload -" + traceEvent.getPayload() );
        }
    }

    @Override
    public Object getIdentifier()
    {
        return dummySourceObject;
    }
}