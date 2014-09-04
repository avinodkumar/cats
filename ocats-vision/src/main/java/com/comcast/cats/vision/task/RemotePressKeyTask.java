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
package com.comcast.cats.vision.task;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.vision.event.ActionType;
import com.comcast.cats.vision.event.PressKeyResponseEvent;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.AbstractManagedTask;

/**
 * Would be the task that represents the settop remote handling task. The
 * execution of this class will be handled by ManagedThreads.
 * 
 * @author sajayjk
 * 
 */
public class RemotePressKeyTask extends AbstractManagedTask
{
    private Settop            settop;
    private RemoteEvent       remoteEvent;
    private static Logger     logger = Logger.getLogger( RemotePressKeyTask.class );

    public RemotePressKeyTask( Settop settop, RemoteEvent remoteEvent )
    {
        this.settop = settop;
        this.remoteEvent = remoteEvent;
        responseEvent = new PressKeyResponseEvent( settop, remoteEvent );
    }

    @Override
    public void run()
    {
        handleRemoteEvent( remoteEvent );

    }

    private void handleRemoteEvent( final RemoteEvent remoteEvent )
    {
        try
        {
            if ( logger.isDebugEnabled() )
            {
                logger.info( "Before RemotePressKeyTask :: Settop (" + settop.getHostMacAddress()
                        + ") received RemoteCommand -" + remoteEvent.getRemoteCommand() );
            }

            if ( remoteEvent.getActionType() == ActionType.PRESS )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.pressKey()" );
                }
                settop.pressKey( remoteEvent.getRemoteCommand() );

            }
            else if ( remoteEvent.getActionType() == ActionType.PRESS_AND_HOLD )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.pressKeyAndHold()" );
                }
                settop.pressKeyAndHold( remoteEvent.getRemoteCommand(), remoteEvent.getCount() );
            }
            // modification to handle direct tune events from the remote.
            else if ( remoteEvent.getActionType() == ActionType.TUNE )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.tune()" );
                }
                settop.tune( remoteEvent.getChannelNumber() );

            }
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "After RemotePressKeyTask :: Settop (" + settop.getHostMacAddress()
                        + ") received RemoteCommand -" + remoteEvent.getRemoteCommand() );
            }
        }
        catch ( Exception exception )
        {
            logger.error( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );

            responseEvent.setType( CatsEventType.REMOTE_FAIL_RESPONSE );
            responseEvent.setMessage( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );
        }
    }

    @Override
    public Object getIdentifier()
    {
        return settop;
    }
}
