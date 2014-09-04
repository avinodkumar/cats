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

import static com.comcast.cats.vision.util.CatsVisionConstants.DIAG_BUTTON_NAME;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.decorator.SettopDiagnostic;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.AbstractManagedTask;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.event.PressKeyResponseEvent;

/**
 * Would be the task that represents the settop remote handling task. The
 * execution of this class will be handled by ManagedThreads.
 * 
 * @author liya
 * 
 */
public class ConfigButtonTask extends AbstractManagedTask
{
    private Settop            settop;

    private ConfigButtonEvent configEvent;

    private static Logger     logger = Logger.getLogger( ConfigButtonTask.class );

    public ConfigButtonTask( Settop settop, ConfigButtonEvent configEvent )
    {
        this.settop = settop;

        this.configEvent = configEvent;

        responseEvent = new PressKeyResponseEvent( settop, configEvent );
    }

    @Override
    public void run()
    {
        handleConfigButtonEvent( configEvent );

    }

    protected void handleConfigButtonEvent( final ConfigButtonEvent configButtonEvent )
    {
        try
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Before ConfigButtonTask :: Settop (" + settop.getHostMacAddress()
                        + ") received config command -" + configEvent.getButtonName() );
            }
            if ( configEvent.getButtonName().equals( DIAG_BUTTON_NAME ) )
            {

                boolean retVal = false;
                if ( settop instanceof SettopDiagnostic )
                {
                    retVal = ( ( SettopDiagnostic ) settop ).showDiagMenu();
                }
                else
                {
                    logger.warn( settop.getHostMacAddress()
                            + " is not decorated to appropriate SettopDiagnostic implementation" );

                    responseEvent
                            .setMessage( "Diagnostic Menu is not supported for ["
                                    + settop.getHostMacAddress()
                                    + "]. Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                                    + settop );
                }
                if ( !retVal )
                {
                    logger.error( "Couldn't Invoke DIAG SCREEN for settop -" + settop.getHostMacAddress() );

                    responseEvent.setType( CatsEventType.CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE );

                    responseEvent.setMessage( "Couldn't Invoke DIAG SCREEN for settop -" + settop.getHostMacAddress() );
                }
            }
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "After ConfigButtonTask :: Settop (" + settop.getHostMacAddress()
                        + ") received config command -" + configEvent.getButtonName() );
            }
        }
        catch ( Exception exception )
        {
            logger.error( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );

            responseEvent.setType( CatsEventType.CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE );

            responseEvent.setMessage( "Error Occurred for settop '" + settop.getHostMacAddress() + "'-" + exception );
        }
    }

    @Override
    public Object getIdentifier()
    {
        return settop;
    }
}
