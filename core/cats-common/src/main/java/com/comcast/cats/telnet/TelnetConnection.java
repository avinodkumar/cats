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
package com.comcast.cats.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a generic telnet connection.
 *
 * @author skurup00c
 *
 */
public class TelnetConnection
{
    private InputStream         is;
    private PrintStream         os;
    private String              host;
    private Integer             port;
    private String              defaultPromptString;
    private TelnetClient        telnetClient;
    private Integer             defaultReadTimeout;
    protected Date              lastActiveTime;
    protected Boolean           isConnected          = false;
    private boolean isBusy = false;

    public static final int DEFAULT_READ_TIMEOUT = 60000;//1 * 60 * 1000;   one minute

    public static final int DEFAULT_TIMEOUT      = 300000;//5 * 60 * 1000;

    private static Logger       logger               = LoggerFactory.getLogger( TelnetConnection.class );

    /**
     * Creates a TelnetConnection insatnce.
     *
     * @param ip
     *            : of the telnet device
     * @param port
     *            : telnet port
     * @param defaultPromptString
     *            : default prompt string to be used. Usually ">".
     *
     */
    public TelnetConnection( String host, Integer port, String defaultPromptString )
    {
        this.host = host;
        this.port = port;
        this.defaultPromptString = defaultPromptString;
        this.telnetClient = new TelnetClient();
        setDefaultReadTimeout( DEFAULT_READ_TIMEOUT );
        lastActiveTime = new Date();
    }

    /**
     * Connect to the telnet client.
     *
     * @param isEnterRequired
     *            : sometime an ENTER key maybe required to reach the prompt.
     *
     * @return true: if connected.
     *
     * @throws SocketException
     * @throws IOException
     */
    public synchronized Boolean connect( Boolean isEnterRequired ) throws SocketException, IOException
    {
        if ( !isConnected )
        {
            try
            {
                telnetClient.connect( getHost(), getPort() );
                logger.info( "connected to telnet host " + host + " port " + port + " defaultPromptString "
                        + defaultPromptString );
            }
            catch ( SocketException e )
            {
                logger.warn( "Could not connect to telnetSession " + e.getMessage() );
                throw new SocketException( e.getMessage() );
            }
            is = telnetClient.getInputStream();
            os = new PrintStream( telnetClient.getOutputStream() );
            if ( isEnterRequired )
            {
                os.println();
            }

            isConnected = true;
            lastActiveTime = new Date();
        }

        return isConnected;
    }

    /**
     * Connect to the telnet client with a password.
     *
     * @param isEnterRequired
     *            : sometime an ENTER key maybe required to reach the prompt.
     *
     * @param password
     * @param passwordPromptString
     *            : the prompt that asks for a password : usually something like
     *            "Enter Password :"
     * @return true if connected successfully
     * @throws SocketException
     * @throws IOException
     */
    public synchronized Boolean connectWithPassword( String password, String passwordPromptString,
            Boolean isEnterRequired ) throws SocketException, IOException
    {
        if ( !isConnected && password != null && passwordPromptString != null )
        {
            try
            {
                telnetClient.connect( getHost(), getPort() );
                logger.info( "connected to telnet host " + host + " port " + port );
            }
            catch ( SocketException e )
            {
                logger.warn( "Could not connect to telnetSession " + e.getMessage() );
                throw new SocketException( e.getMessage() );
            }

            is = telnetClient.getInputStream();
            os = new PrintStream( telnetClient.getOutputStream() );

            if ( isEnterRequired )
            {
                os.println();
            }

            String passwordPrompt = readUntil( passwordPromptString );
            logger.debug( "passwordPrompt " + passwordPrompt );
            if ( passwordPrompt != null )
            {
                write( password );
                isConnected = true;
                lastActiveTime = new Date();
            }
            else
            {
                logger.info( "Prompt string could not be reached" );
                disconnect();
            }

        }
        return isConnected;
    }

    /**
     * Disconnects a telnet session.
     *
     * @throws IOException
     */
    public synchronized void disconnect() throws IOException
    {
        if ( is != null )
        {
            is.close();
        }
        if ( os != null )
        {
            os.close();
        }
        logger.trace( "dicsonnecting telnetClient" );
        if ( telnetClient.isConnected() )
        {
            telnetClient.disconnect();
        }
        is = null;
        os = null;
        isConnected = false;

        logger.info( "disconnected telnetConnection " + host + " port " + port );
    }

    /**
     * Status of telnet connection
     *
     * @return true if connected.
     */
    public synchronized Boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Send a command to the telnet session. Requires
     * TelnetConnection.isConnected() to be true.
     *
     * @param command
     *            to send
     * @return returned value in telnet client after execution of command.
     * @throws IOException
     */
    public synchronized String sendCommand( String command ) throws IOException
    {
        return sendCommand( command, defaultPromptString );
    }

    /**
     * Send a command to the telnet session, and read till the following prompt
     * instead of the default prompt.
     *
     * Requires TelnetConnection.isConnected() to be true.
     *
     * @param command
     * @param prompt
     * @return
     * @throws IOException
     */
    public synchronized String sendCommand( String command, String prompt ) throws IOException
    {
        isBusy  = true;
        logger.trace( "sendCommand " + command + " prompt " + prompt );
        String result = null;
        if ( isConnected && command != null )
        {
            write( command );
            result = readUntil( prompt );
        }
        lastActiveTime = new Date();
        isBusy = false;
        return result;
    }

    /**
     * if prompt string is not recieved within the default timeout value, the
     * read will be interrupted. This is important to avoid the connection to
     * hang on a read when the prompt string does not arrive. Default value is 1
     * minute.
     *
     * @return defaultTimeout value
     */
    public Integer getDefaultReadTimeout()
    {
        return defaultReadTimeout;
    }

    /**
     * Timeout to that closes the socket during a period of inactivity.
     *
     */
    public void setDefaultReadTimeout( Integer defaultReadTimeout )
    {
        this.defaultReadTimeout = defaultReadTimeout;
    }

    /**
     * This just provides the Output stream to write to the telnet connection.
     * Should be called only after a connect is called. This is to provide users
     * with an option to write stuff to the telnet connection other than what is
     * provided through this class. Its the responsibility of the user to
     * understand the working of TelnetConnection and the proper use of the
     * PrintStream
     *
     */
    public PrintStream getPrintStream()
    {
        return os;
    }

    /**
     * This just provides the input stream to write to the telnet connection.
     * Should be called only after a connect is called. This is to provide users
     * with an option to read stuff from the telnet connection other than what
     * is provided through this class. Its the responsibility of the user to
     * understand the working of TelnetConnection and the proper use of the
     * input
     *
     */
    public InputStream getInputStream()
    {
        return is;
    }

    /**
     * Get the last time a connect, or sendCommand was sent. can be used to
     * determine timeout to disconnect based on inactivity etc.
     *
     * @return
     */
    public Date getLastActiveTime()
    {
        return lastActiveTime;
    }

    public String getHost()
    {
        return host;
    }

    public Integer getPort()
    {
        return port;
    }

    public boolean isBusy()
    {
        return isBusy;
    }

    /**
     *
     * Reads Telnet response.
     *
     * @param pattern
     * @return
     * @throws IOException
     */
    private synchronized String readUntil( String pattern ) throws IOException
    {
        logger.trace( "readUntil " + pattern );
        String retVal = null;
        if ( is != null )
        {
            StringBuffer sb = new StringBuffer();
            char lastChar = pattern.charAt( pattern.length() - 1 );
            char ch;
            telnetClient.setSoTimeout( defaultReadTimeout );
            logger.trace( "getDefaultReadTimeout() " + defaultReadTimeout );
            while ( true )
            {
                try
                {
                    int readByte = is.read();
                    if ( readByte != -1 )
                    {
                        ch = ( char ) readByte;
                    }
                    else
                    {
                        logger.warn( "TelnetConnection: End of stream reached. Maybe remote end crashed " );
                        disconnect();
                        throw new IOException( "TelnetConnection: End of stream reached. Maybe remote end crashed " );
                    }
                }
                catch ( IOException e )
                {
                    logger.warn( "Error occured in TelnetConnect readUntil " + e.getMessage() );
                    disconnect();
                    throw new IOException( e );
                }
                sb.append( ch );
                if ( ch == lastChar )
                {
                    if ( sb.toString().endsWith( pattern ) )
                    {
                        String string = sb.toString();
                        retVal = string.substring( 0, string.length() - pattern.length() );
                        logger.trace( "readUntil string " + retVal );
                        break;
                    }
                }

            }
        }
        return retVal;
    }

    /**
     * Sends actual Telnet command.
     *
     * @param value
     */
    private synchronized void write( String value )
    {
        if ( null != os )
        {
            logger.trace( "write value " + value );
            os.println( value );
            os.flush();
        }
    }
}