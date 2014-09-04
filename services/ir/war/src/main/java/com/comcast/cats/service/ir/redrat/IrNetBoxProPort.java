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
package com.comcast.cats.service.ir.redrat;

import static com.comcast.cats.service.ir.redrat.RedRatCommands.IPADDRESS_ARGUMENT;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.IRNETBOX_IR_COMMAND;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.KEYSET_ARGUMENT;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.KEY_ARGUMENT;
import static com.comcast.cats.service.ir.redrat.RedRatCommands.PORT_ARGUMENT;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.ir.commands.CatsCommand;
import com.comcast.cats.ir.commands.DelayCommand;
import com.comcast.cats.ir.commands.IrCommand;
import com.comcast.cats.ir.commands.PressKeyAndHoldCommand;
import com.comcast.cats.ir.commands.PressKeyCommand;
import com.comcast.cats.service.WebServiceReturn;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.telnet.TelnetConnection;

/**
 * Represents a port of the IrNetBox device.
 * 
 * @author skurup00c
 * 
 */
public class IrNetBoxProPort extends RedRatDevicePort
{
    int                         portNumber;

    /**
     * Represents a count value of 35 for for press and hold.
     */
    private static final String REPEAT_COUNT_35          = "_repeat35";

    /**
     * Format for key that has a count value.
     */
    String                      repeatCountFormat        = REPEAT_COUNT_35;

    private static final Logger logger                   = LoggerFactory.getLogger( IrNetBoxProPort.class );

    private static final String PRESSKEY_EXPECTED_RESULT = "OK";

    public static final int     WAIT_INTERVAL            = 500;

    public IrNetBoxProPort( int portNumber, IrNetBoxPro irDevice )
    {
        super( portNumber, irDevice );
        this.portNumber = portNumber;
    }

    @Override
    public synchronized WebServiceReturn sendCommand( CatsCommand catsCommand )
    {
        WebServiceReturn retVal = new WebServiceReturn();
        retVal.setResult( WebServiceReturnEnum.FAILURE );

        String commandString = null;
        String expectedResult = null;

        logger.debug( "sendCommand() CatsCommand " + catsCommand );
        if ( catsCommand != null )
        {
            while ( catsCommand.hasNext() )
            {
                CatsCommand command = catsCommand.next();
                if ( command instanceof DelayCommand )
                {
                    logger.debug( "Its a DelayCommand() " + command );
                    try
                    {
                        Thread.sleep( ( ( DelayCommand ) command ).getDelay() );
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }

                    retVal.setResult( WebServiceReturnEnum.SUCCESS );
                    retVal.setMessage( "CATS Command slept for " + ( ( DelayCommand ) command ).getDelay() + " ms." );
                }
                else if ( command instanceof IrCommand )
                {
                    logger.debug( "sendCommand() catsCommand.next() " + command );
                    commandString = getDeviceUnderstandablePressKeyCommand( command );
                    expectedResult = getExpectedResult( command );
                    logger.debug( "commandString " + commandString + " expectedResult  " + expectedResult );
                    if ( commandString != null )
                    {
                        retVal = sendCommand( commandString, expectedResult );
                        if(retVal.getResult().equals( WebServiceReturnEnum.FAILURE )){
                            logger.warn( "sendCommand()  retVal.getMessage() " + retVal.getMessage() );
                        }
                    }
                    else
                    {
                        retVal.setMessage( "IrNetBoxPro does not know how to handle this command" );

                    }
                }
                else
                {
                    retVal.setMessage( "IrNetBoxPro does not know how to handle this command" );
                }
            }
        }
        else
        {
            retVal.setMessage( "Command is null" );
        }

        return retVal;
    }

    /**
     * Send a command via the {@link TelnetConnection}
     * 
     * @param commandString
     * @param expectedResult
     * @return
     */
    private WebServiceReturn sendCommand( String commandString, String expectedResult )
    {
        WebServiceReturn retVal = new WebServiceReturn();
        retVal.setResult( WebServiceReturnEnum.FAILURE );

        String response = null;
        RedRatHubConnection telnetConnection = ( ( IrNetBoxPro ) redratDevice ).getConnection( portNumber );
        logger.debug( "sendCommand()  telnetConnection " + telnetConnection );

        if ( telnetConnection != null )
        {
            response = sendTelnetCommand( telnetConnection, commandString );
            logger.debug( "sendCommand()  response " + response );
            
            if ( !expectedResult.equals( response ) )
            {
                retVal.setMessage( "RedRat did not return an expected Result. Command " + commandString
                        + " : Expected Result " + expectedResult + " : returned response " + response );
            }
            else
            {
                retVal.setResult( WebServiceReturnEnum.SUCCESS );
                retVal.setMessage( response );
            }
            
        }else{
            logger.warn( "Didnt get a telnetConnection to RedratHub. " + telnetConnection );
            retVal.setMessage( "Could not connect to RedRatHub" );
        }
        
       
        return retVal;
    }

    private String getDeviceUnderstandablePressKeyCommand( CatsCommand command )
    {
        String commandString = null;

        if ( command instanceof PressKeyCommand )
        {
            PressKeyCommand pressKeyCommand = ( PressKeyCommand ) command;
            commandString = IRNETBOX_IR_COMMAND
                    .replace( IPADDRESS_ARGUMENT, ( ( IrNetBoxPro ) redratDevice ).getIpAddress() )
                    .replace( KEYSET_ARGUMENT, pressKeyCommand.getIrKeySet() )
                    .replace( PORT_ARGUMENT, String.valueOf( portNumber ) );

            String key = pressKeyCommand.getRemoteCommand().toString();
            if ( command instanceof PressKeyAndHoldCommand )
            {
                key = key.concat( repeatCountFormat );
            }
            commandString = commandString.replace( KEY_ARGUMENT, key );
        }
        return commandString;
    }

    private String getExpectedResult( CatsCommand command )
    {
        String expectedResult = null;

        if ( command instanceof PressKeyCommand )
        {
            expectedResult = PRESSKEY_EXPECTED_RESULT;
        }
        return expectedResult;
    }

    private String sendTelnetCommand( RedRatHubConnection telnetConnection, String command )
    {
        String retVal = "";
        int retries = 0;
        boolean tryRetry = false;
        do
        {
            try
            {
                logger.info( "sendCommand "+command );
                long start = System.currentTimeMillis();
                retVal = telnetConnection.sendCommand( command );
                long end = System.currentTimeMillis();
                if(end-start > 1500){
                	logger.warn( "sendCommand "+command+" response "+retVal+" time taken by hub "+(end-start)+"ms" );
                }else{
                	logger.trace( "sendCommand "+command+" response "+retVal+" time taken by hub "+(end-start)+"ms" );
                }
                tryRetry = false;
            }
            catch ( IOException e )
            {
                logger.warn( "connectTelnet failed " + e.getMessage() );
                tryRetry = true;
                retries++;
                // try reconnecting
                try
                {
                    telnetConnection.disconnect();
                    telnetConnection.connect( false );
                }
                catch ( IOException e2 )
                {

                    logger.warn( "Could not reconnect. The hub may have crashed. " + e2.getMessage() );
                }
                if ( retries == 3 )
                {
                    logger.warn( "sendTelnetCommand failed " + e.getMessage() );
                    retVal += e.getMessage();
                    telnetConnection.releaseConnection(); //avoid being GC. Return to pool instead.
                }
                else
                {
                    try
                    {
                        Thread.sleep( WAIT_INTERVAL );
                    }
                    catch ( InterruptedException e1 )
                    {
                        logger.warn( "connectTelnet wait interrupted " + e1.getMessage() );
                    }
                }
            }
        } while ( tryRetry && retries < 3 );

        return retVal;
    }
}
