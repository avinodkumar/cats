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

/**
 * Dummy handler for Test cases
 * 
 * @author
 * 
 */
public class DummyCatsEventHandler implements CatsEventHandler
{
    /**
     * Simple counter to check action performed method.
     */
    private int         actionPerformedCounter = 0;
    private CatsEvent   event                  = null;
    static final Logger logger                 = LoggerFactory.getLogger( DummyCatsEventHandler.class );

    public DummyCatsEventHandler()
    {
        logger.trace( "DummyCatsEventHandler Constructor" );
    }

    public void catsEventPerformed( CatsEvent evt )
    {
        this.event = evt;
        actionPerformedCounter++;
        logger.trace( "catsEventPerformed - DummyCatsEventHandler" );
    }

    public CatsEvent getLastEvent()
    {
        return event;
    }

    public int getActionPerformedCounter()
    {
        return actionPerformedCounter;
    }
}
