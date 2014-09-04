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
package com.comcast.cats.ir.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;

/**
 * Press key and hold command
 */
public class PressKeyAndHoldCommand extends PressKeyCommand
{

    /**
     * Repeat count;
     */
    Integer                     count        = 0;

    private static final String COMMAND_NAME = "PressKeyAndHold";
    private static final Logger logger       = LoggerFactory.getLogger( PressKeyAndHoldCommand.class );

    public PressKeyAndHoldCommand( RemoteCommand command, String irKeySet, Integer count )
    {
        super( command, irKeySet );
        setName( COMMAND_NAME );
        setCount( count );
        logger.trace( "PressKeyAndHoldCommand count " + count );
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount( Integer count )
    {
        if(count < 0){
            count = 0;
        }
        this.count = count;
    }
}
