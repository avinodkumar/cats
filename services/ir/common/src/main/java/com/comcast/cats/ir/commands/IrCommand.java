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
 * Represents all IrCommands.
 * 
 * @author skurup00c
 * 
 */
public abstract class IrCommand extends CatsCommand
{
    /**
     * A remoteCommand that is represented in this IrCommand.
     */
    RemoteCommand               remoteCommand;
    /**
     * The remote type.
     */
    String                      irKeySet;
    /**
     * default logger.
     */
    private static final Logger logger = LoggerFactory.getLogger( IrCommand.class );

    public IrCommand( String name, RemoteCommand remoteCommand, String irKeySet )
    {
        super( name );
        this.remoteCommand = remoteCommand;
        this.irKeySet = irKeySet;
        logger.trace( "IrCommand name " + name + " remoteCommand " + remoteCommand + " irKeySet " + irKeySet );
    }

    public RemoteCommand getRemoteCommand()
    {
        return remoteCommand;
    }

    public void setRemoteCommand( RemoteCommand remoteCommand )
    {
        this.remoteCommand = remoteCommand;
    }

    public String getIrKeySet()
    {
        return irKeySet;
    }

    public void setIrKeySet( String irKeySet )
    {
        this.irKeySet = irKeySet;
    }
}
