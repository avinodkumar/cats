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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a generic CATS command. The CATS Command is a fluent API. It can
 * be build upon so that one command can represent an individual command or a
 * group of commands.
 * 
 * for example
 * 
 * 1) CatsCommand pressKeyCommand = new CatsCommand("presskey"); 2) CatsCommand
 * goToGuideCommand = new CatsCommand("EXIT").add(new CatsCommand("GUIDE")); 3)
 * CatsCommand enableCC = new
 * CatsCommand("EXIT").add(goToGuideCommand).add(navigateAndSelectCommand);
 * 
 * It implements the iterator interface and the hasNext and next methods will
 * ensure returning the logically next command that should be invoked.
 * 
 * @author skurup00c
 * 
 */
public class CatsCommand implements Iterator< CatsCommand >
{
    /**
     * List of commands that need to be executed.
     */
    private List< CatsCommand > catsCommands   = new ArrayList<CatsCommand>();

    /**
     * Keeps a count of the current command, for use with next() method.
     */
    private int                 currentCount   = 0;
    /**
     * Keeps a count of the current command, for use with hasNext() methods.
     */
    private int                 hasNextCounter = 0;
    /**
     * The size of all commands in this cats command and all children commands.
     */
    private int                 activeSize     = 0;
    /**
     * Name of the command;
     */
    private String              name = "";
    /**
     * default logger.
     */
    private static final Logger logger         = LoggerFactory.getLogger( CatsCommand.class );
    
    public CatsCommand()
    {
        activeSize++;
        catsCommands.add( this );
        logger.trace( "Created CatsCommand " + name );
    }
    
    public CatsCommand( String name )
    {
        this();
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Add a {@link CatsCommand} as a child to this command. The added command
     * will be executed after this command is executed.
     * 
     */
    public CatsCommand add( CatsCommand command )
    {
        logger.trace( "CatsCommand add " + command );
        if ( command != null )
        {
            catsCommands.add( command );
            if ( command.catsCommands.size() > 0 )
            {
                activeSize += command.activeSize;
            }
            else
            {
                activeSize++;
            }
        }else{
            throw new IllegalArgumentException("Command should not be null");
        }
        logger.debug( "CatsCommand activeSize " + activeSize );
        return this;
    }

    @Override
    public boolean hasNext()
    {
        boolean hasNext = false;

        logger.debug( "CatsCommand hasNextCounter " + hasNextCounter );
        logger.debug( "CatsCommand activeSize " + activeSize );

        if ( hasNextCounter < activeSize )
        {
            hasNextCounter++;
            hasNext = true;
        }
        else
        {
            hasNextCounter = 0; // start over again for the next time it is
                                // called.
            hasNext = false;
        }
        logger.debug( "CatsCommand hasNext " + hasNext );
        return hasNext;
    }

    @Override
    public CatsCommand next()
    {
        CatsCommand command = null;
        logger.debug( "CatsCommand currentCount " + currentCount );
        if ( currentCount == 0 )
        {
            currentCount++;
            command = this;
        }
        else
        {
            if ( currentCount < catsCommands.size() )
            {
                command = catsCommands.get( currentCount );
                // If this command is a lone command like pressKey or something
                // return it as it has to be executed next.
                // If this command is encapsulating command like GoToGuide etc,
                // then return the first child of this command.

                if ( command.catsCommands.size() > 0 )
                {
                    CatsCommand childCommand = command.next();
                    if ( childCommand == null )
                    {
                        // if no more children present, return next of the
                        // current command.
                        currentCount++;
                        logger.debug( "CatsCommand next " + currentCount );
                        return next();
                    }
                    else
                    {
                        logger.debug( "CatsCommand next " + currentCount );
                        return childCommand;
                    }
                }
                else
                {
                    currentCount++;
                    logger.debug( "CatsCommand next " + currentCount );
                    return command;
                }
            }
            currentCount = 0;
        }
        logger.debug( "CatsCommand next " + currentCount );
        return command;
    }

    @Override
    public void remove()
    {
        catsCommands.remove( currentCount );
    }

    public String toString()
    {
        return name;
    }

}
