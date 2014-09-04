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
import com.comcast.cats.vision.event.PowerEvent;
import com.comcast.cats.vision.event.PressKeyResponseEvent;
import com.comcast.cats.vision.util.CatsVisionConstants;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.AbstractManagedTask;

/**
 * Would be the task that represents the settop remote handling task. The
 * execution of this class will be handled by ManagedThreads.
 * 
 * @author sajayjk
 * 
 */
public class PowerPressKeyTask extends AbstractManagedTask
{
    private Settop            settop;
    private PowerEvent        powerEvent;
    private static Logger     logger = Logger.getLogger( PowerPressKeyTask.class );

    public PowerPressKeyTask( Settop settop, PowerEvent powerEvent )
    {
        this.settop = settop;
        this.powerEvent = powerEvent;
        responseEvent = new PressKeyResponseEvent( settop, powerEvent );
    }

    @Override
    public void run()
    {
        handlePowerEvent( powerEvent );

    }

    private void handlePowerEvent( final PowerEvent powerEvent )
    {
        try
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Before PowerPressKeyTask :: Settop (" + settop.getHostMacAddress()
                        + ") received PowerCommand -" + powerEvent.getActionCommand().toString() );
            }

            if ( powerEvent.getActionCommand().toString() == CatsVisionConstants.POWER_ON )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.powerOn()" );
                }
                settop.powerOn();
            }
            else if ( powerEvent.getActionCommand().toString() == CatsVisionConstants.POWER_OFF )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.powerOff()" );
                }
                settop.powerOff();
            }
            else if ( powerEvent.getActionCommand().toString() == CatsVisionConstants.POWER_REBOOT )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Invoking settop.reboot()" );
                }
                settop.reboot();
            }
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "After PowerPressKeyTask :: Settop (" + settop.getHostMacAddress()
                        + ") received PowerCommand -" + powerEvent.getActionCommand() );
            }
        }
        catch ( Exception exception )
        {
            logger.error( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );

            responseEvent.setType( CatsEventType.POWER_FAIL_RESPONSE );
            responseEvent.setMessage( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );
        }

    }

    @Override
    public Object getIdentifier()
    {
        return settop;
    }
}
