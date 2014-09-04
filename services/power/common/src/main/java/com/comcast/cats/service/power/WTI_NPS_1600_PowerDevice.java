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
package com.comcast.cats.service.power;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.comcast.cats.service.power.util.PowerConstants.NEWLINE;
import static com.comcast.cats.service.power.util.PowerConstants.NPS_PROMPT;
import static com.comcast.cats.service.power.util.PowerConstants.LOGIN;
import static com.comcast.cats.service.power.util.PowerConstants.PASSWORD;
import static com.comcast.cats.service.power.util.PowerConstants.SLASH;
import static com.comcast.cats.service.power.util.PowerConstants.CMD_TO_SUPPRESS_CONFIRMATION_PROMPT;
import static com.comcast.cats.service.power.util.PowerConstants.SPACE;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_ON;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_OFF;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_BOOT;
import static com.comcast.cats.service.power.util.PowerConstants.STATUS_UNKNOWN;
import static com.comcast.cats.service.power.util.PowerConstants.PLUG_STATUS_CMD;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_STATUS_ONE;
import static com.comcast.cats.service.power.util.PowerConstants.POWER_STATUS_ZERO;

public class WTI_NPS_1600_PowerDevice extends PowerControllerDevice
{
    private static final Logger log = LoggerFactory.getLogger( PowerDeviceConnection.class );
    private String              host;
    private Integer             port;
    private String              username;
    private String              password;
    private TelnetClient        client;
    private InputStream         in;
    private OutputStream        out;
    private String              lastString;

    public WTI_NPS_1600_PowerDevice( String host, Integer port, String username, String password )
    {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

    }

    protected boolean sendCmd( String cmd, boolean echo )
    {
        if ( !cmd.endsWith( NEWLINE ) )
        {
            cmd += NEWLINE;
        }
        try
        {
            if ( null != this.out )
            {
                out.write( cmd.getBytes() );
                if ( echo )
                {
                    log.info( "writing to " + host + ": " + cmd );
                }
                out.flush();
                return true;
            }
        }
        catch ( IOException ioe )
        {
            log.error( "IOException: " + ioe.getMessage() );
        }
        return false;
    }

    /**
     *This method reads a character at a time from the socket input stream
     * until EOF is reached, or the substring of the search string is found.
     * Once string is found the input stream is interrupted Immediately and
     * returns
     * 
     * @return true, if string is found, else false.
     */
    protected boolean waitFor( String str )
    {
        StringBuilder buff = new StringBuilder();
        int c;
        try
        {
            try
            {
                Thread.sleep( 100 );
            }
            catch ( InterruptedException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            c = in.read();
            
            while ( -1 != c )
            {
                buff.append( ( char ) c );
                if ( buff.indexOf( str ) != -1 )
                {
                    log.debug( "Found: '" + str + "'" );
                    lastString = buff.toString();
                    return true;
                }
                c = in.read();
                
            }
            lastString = buff.toString();
        }
        catch ( IOException ioe )
        {
            log.error( "IOException: " + ioe.getMessage() );
        }
        return false;
    }

    /**
     * This device requires login, which is different than previous devices.
     */
    protected boolean login()
    {
        if ( !waitFor( LOGIN ) )
        {
            return false;
        }
        sendCmd( username, true );
        if ( !waitFor( PASSWORD ) )
        {
            return false;
        }
        sendCmd( password, true );
        return waitFor( NPS_PROMPT );
    }

    private boolean sendPowerCommand( String command, int outlet )
    {
        boolean send = sendCmd( SLASH + command + SPACE + outlet + CMD_TO_SUPPRESS_CONFIRMATION_PROMPT, true );
        if ( send && waitFor( NPS_PROMPT ) )
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean powerOn( int outlet )
    {
    	log.info("POWER ON in WTI_NPS_1600_PowerDevice");
        boolean result =  sendPowerCommand( POWER_ON, outlet );
        return result;
    }

    @Override
    public boolean powerOff( int outlet )
    {
    	log.info("POWER OFF in WTI_NPS_1600_PowerDevice");
        boolean result = sendPowerCommand( POWER_OFF, outlet );
        return result;
    }

    @Override
    public boolean powerToggle( int outlet )
    {
    	log.info(" POWER REBOOT in WTI_NPS_1600_PowerDevice ");
        boolean result = sendPowerCommand( POWER_BOOT, outlet );
        return result;
    }

    /**
     * Status looks like this: /S <outlet>\r\n [0|1] - '0' or '1' NPS> //Back to
     * prompt.
     * 
     * @return
     */
    protected String parseStatus()
    {
        // Wait for the previous end of the last command.
        waitFor( NEWLINE );
        // Wait for the new NPS command.
        waitFor( NPS_PROMPT );
        // Before the NPS command the status should have been outputted as
        // either '0' or '1', so let's look for it.
        if ( lastString.contains( POWER_STATUS_ZERO ) )
        {
            return POWER_OFF;
        }
        else if ( lastString.contains( POWER_STATUS_ONE ) )
        {
            return POWER_ON;
        }
        return STATUS_UNKNOWN;
    }

    @Override
    public String getOutletStatus( int outlet )
    {
        sendCmd( PLUG_STATUS_CMD + outlet, true );
        return parseStatus();
    }

    public boolean logout()
    {
        boolean returnVal = true;
        
        try
        {
            client.disconnect();
        }
        catch ( IOException e )
        {
            log.error( "IOException :"+e );
        }
        return returnVal;

    }

    @Override
    public void createPowerDevConn()
    {        
        client = new TelnetClient();

        client.setConnectTimeout( 5000 );

        try
        {
            client.connect( host, port );
        }
        catch ( SocketException socketException )
        {
            throw new UnableToCreatePowerControllerDevice( socketException );
        }
        catch ( IOException ioException )
        {
            throw new UnableToCreatePowerControllerDevice( ioException );
        }
        in = client.getInputStream();
        
        out = client.getOutputStream();
        
        login();        
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
