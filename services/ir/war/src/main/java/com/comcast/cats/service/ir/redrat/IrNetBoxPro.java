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

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.IrPort;
import com.comcast.cats.telnet.TelnetConnection;

/**
 * Represents the IrNetBoxPro device.
 * 
 * @author skurup00c
 * 
 */
public class IrNetBoxPro extends RedRatDevice
{
    String                      ipAddress;
    List< IrPort >              irPorts;

    private static final Logger logger = LoggerFactory.getLogger( IrNetBoxPro.class );
    
    public static final int     WAIT_INTERVAL = 10 * 60;

    public IrNetBoxPro( long id, String ipAddress )
    {
        super( id );
        this.ipAddress = ipAddress;
    }

    @Override
    public List< IrPort > getIrPorts()
    {
        return irPorts;
    }

    public void setIrPorts( List< IrPort > irPorts )
    {
        this.irPorts = irPorts;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    @Override
    public IrPort getPort( int portNumber )
    {
        IrPort retVal = null;

        if ( irPorts != null )
        {
            for ( IrPort irPort : irPorts )
            {
                if ( irPort.getPortNumber() == portNumber )
                {
                    retVal = irPort;
                    break;
                }
            }
        }
        logger.debug( "getPort " + portNumber + " from irDevice " + ipAddress + " : " + retVal );
        return retVal;
    }

    @Override
    public String sendCommand( String command )
    {
        throw new UnsupportedOperationException( "Currently sending commands to the IRNetBox device is not supported" );
    }

    /**
     * Gets a connection that will allow to send commands to a port.
     * 
     * @param portNumber
     * @return {@link TelnetConnection}
     */
    public RedRatHubConnection getConnection( int portNumber )
    {
        RedRatHubConnection retVal = null;
    	RedRatHub redratHub = RedRatManagerImpl.getRedRatHub(this);
    	if(redratHub != null)
    	{
    		RedRatHubConnection telnetConnection = redratHub.getConnection( this, portNumber );
	        if(telnetConnection != null){
	            boolean isConnected = telnetConnection.isConnected();
	            logger.debug( "getConnection portConnection.isConnected() " + telnetConnection.isConnected() );
	            if ( !isConnected )
	            {
	                isConnected = connectTelnet( telnetConnection );
	            }
	            logger.debug( "getConnection portConnection.isConnected() " + isConnected );
	            logger.debug( "getConnection isConnected " + telnetConnection.isConnected() );
	            if ( !isConnected )
	            {
	            	retVal = null;
	            	telnetConnection.releaseConnection(); // to avoid being garbage collected.
	            }else{
	            	retVal = telnetConnection;
	            }
	        }
    	}
        logger.debug( "IrNetBoxPro  getConnection to port "+portNumber+" : "+retVal);
        return retVal;
    }
    
    /**
     * Equals and hashcode critical for redrat discovery and blacklisting mechanism
     */
    @Override
    public boolean equals(Object object){
        boolean isEqual = false;
        if(object instanceof IrNetBoxPro){
            
            if ( super.equals( object ) && ((IrNetBoxPro)object).getIpAddress().equals( getIpAddress() ) )
            {
                isEqual = true;
            }
        }
        return isEqual;
    }
    
    /**
     * Equals and hashcode critical for redrat discovery and blacklisting mechanism
     */
    @Override
    public int hashCode(){
        return 0;
    }
    
    
    /**
     * Connect to a {@link TelnetConnection}.
     * 
     * @param telnetConnection
     * @return true if connected successfully.
     */
    public  synchronized boolean connectTelnet( TelnetConnection telnetConnection )
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
                    retVal = telnetConnection.connect( true );
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
}
