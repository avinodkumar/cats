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

/**
 * Represents a command to add delay/sleep.
 * 
 * @author skurup00c
 * 
 */
public class DelayCommand extends CatsCommand
{
    /**
     * Delay in ms.
     */
    int                         delay;
    /**
     * default logger.
     */
    private static final Logger logger = LoggerFactory.getLogger( DelayCommand.class );

    /**
     * Delay in milliseconds.
     * 
     * @param delay
     */
    public DelayCommand( int delayMs )
    {
        super( "Delay Command" );
        setDelay( delayMs );
        logger.trace( "DelayCommand created " + delay );
    }

    public int getDelay()
    {
        return delay;
    }

    public void setDelay( int delay )
    {
        if(delay < 0){
            delay = 0;
        }
        this.delay = delay;
    }
}