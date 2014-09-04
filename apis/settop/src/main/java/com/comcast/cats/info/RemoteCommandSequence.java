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
package com.comcast.cats.info;

import java.io.Serializable;

import com.comcast.cats.RemoteCommand;

/**
 * A simple bean to support sending sequence of remote commands.
 * 
 * @author ssugun00c
 * 
 */
public class RemoteCommandSequence implements Serializable
{
    private static final long serialVersionUID              = 1L;

    // 10 Repeat count =1 Second, Refer catsvision//RemoteController:keyPress(
    // RemoteButton remoteButton )
    public static final int   REPEAT_ONE_SEC                = 10;
    public static final int   NO_REPEAT                     = 0;
    public static final int   DELAY_ONE_SEC_IN_MILLISECONDS = 1000;
    public static final int   NO_DELAY                      = 0;

    private RemoteCommand     command;
    private Integer           repeatCount;
    private Integer           delay;

    /**
     * Constructor
     * 
     * @param command
     *            {@link RemoteCommand}
     * @param repeatCount
     * @param delay
     */
    public RemoteCommandSequence( RemoteCommand command, Integer repeatCount, Integer delay )
    {
        super();
        this.command = command;
        this.repeatCount = repeatCount;
        this.delay = delay;
    }

    /**
     * No argument constructor.
     */
    public RemoteCommandSequence()
    {
        super();
    }

    /**
     * To get the RemoteCommand
     * 
     * @return {@link RemoteCommand}
     */
    public RemoteCommand getCommand()
    {
        return command;
    }

    /**
     * To set the RemoteCommand
     * 
     * @param command
     *            {@link RemoteCommand}
     */
    public void setCommand( RemoteCommand command )
    {
        this.command = command;
    }

    /**
     * To get the repeat Count
     * 
     * @return integer
     */
    public Integer getRepeatCount()
    {
        return repeatCount;
    }

    /**
     * To set the repeat Count
     * 
     * @param repeatCount
     */
    public void setRepeatCount( Integer repeatCount )
    {
        this.repeatCount = repeatCount;
    }

    /**
     * To get the delay
     * 
     * @return integer
     */
    public Integer getDelay()
    {
        return delay;
    }

    /**
     * To set the delay
     * 
     * @param delay
     */
    public void setDelay( Integer delay )
    {
        this.delay = delay;
    }

    @Override
    public String toString()
    {
        return "Remote Command Sequence [Command=" + command + ", Repeat Count=" + repeatCount + ", Delay=" + delay
                + "]";
    }
}
