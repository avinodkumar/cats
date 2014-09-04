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
package com.comcast.cats.utils;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.telnet.TelnetConnection;

/**
 * Utility to handle telnet connections.
 * 
 * @author skurup00c
 * 
 */
public class TelnetUtil
{
    public static final String  ERROR_STRING  = "Error Occurred : ";

    public static final int     WAIT_INTERVAL = 10 * 60;                                    // 10sec

    private static final Logger logger        = LoggerFactory.getLogger( TelnetUtil.class );

    /**
     * Connect to a {@link TelnetConnection}.
     * 
     * @param telnetConnection
     * @return true if connected successfully.
     */
    public static synchronized boolean connectTelnet( TelnetConnection telnetConnection )
    {
        boolean retVal = false;
        int retries = 0;
        boolean tryRetry;

        if ( telnetConnection != null )
        {
            do
            {
                logger.debug( "ConnectTelnet retries " + retries );
                try
                {
                    retVal = telnetConnection.connect( false );
                    logger.debug( "connectTelnet status " + retVal );
                    tryRetry = retVal;
                    break;
                }
                catch ( IOException e )
                {
                    logger.warn( "connectTelnet failed " + e.getMessage() );
                    tryRetry = true;
                    retries++;
                }

                try
                {
                    Thread.sleep( WAIT_INTERVAL );
                }
                catch ( InterruptedException e )
                {
                    logger.warn( "connectTelnet wait interrupted " + e.getMessage() );
                }
            } while ( tryRetry && retries < 3 );
        }

        return retVal;
    }

    /**
     * Send a command to a telnet Connection. It must be ensured that the
     * {@link TelnetConnection} is already connected.
     * 
     * @param telnetConnection
     * @param command
     * @return
     */
    public static String sendTelnetCommand( TelnetConnection telnetConnection, String command, String promptString )
    {
        synchronized(new Object()){
        String retVal = ERROR_STRING;
        if ( telnetConnection == null || command == null )
        {
            retVal += "TelnetConnection or command String should not be null. TelnetConnection : " + telnetConnection
                    + " Command : " + command;
        }
        else
        {
            int retries = 0;
            boolean tryRetry = false;
            do{
                try
                {
                    tryRetry = false;
                    retVal = telnetConnection.sendCommand( command, promptString );
                    if ( retVal == null )
                    {
                        logger.warn( "sendTelnetCommand returned null " );
                        retVal = ERROR_STRING
                                + "sendCommand returned null. Maybe telnet connection is not in connected state.";
                    }
                }
                catch ( IOException e )
                {
                    logger.warn( "sendTelnetCommand failed " + e.getMessage() );
                    try
                    {
                        telnetConnection.disconnect();
                    }
                    catch ( IOException e1 )
                    { }
                    connectTelnet( telnetConnection );
                    retVal += e.getMessage();
                    retries++;
                    tryRetry = true;
                }
            } while ( tryRetry && retries < 2 );
        }
        
        return retVal;
        }
    }

}
